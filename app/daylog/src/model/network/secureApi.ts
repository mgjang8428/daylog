import {
	ACCESS_TOKEN_STORED_KEY,
	API_BASE_URL,
	API_TIMEOUT,
	TOKEN_TYPE
} from "@/model/common/consts"
import tokenStorage from "@/model/store/tokenStorage"
import AccountService from "@/model/v1/account/service/accountService"
import { AxiosError, AxiosResponse, InternalAxiosRequestConfig, create as axiosCreate } from "axios"

const accountService: AccountService = new AccountService()

// accessToken 재발급 중인지 판단
let isRefreshing = false
// 재발급 요청 대기 큐
let waitQueue: ((accessToken: string | null, refreshError?: unknown) => void)[] = []

/**
 * 대기 큐를 비우고 모든 요청을 재시도하도록 함
 * @param accessToken 새로 발급 받은 accessToken
 */
const flushQueue = (accessToken: string | null, refreshError?: unknown) => {
	waitQueue.forEach((callback) => callback(accessToken, refreshError))
	waitQueue = []
}

/**
 * secureApi: 인증 필요 api
 */
export const secureApi = axiosCreate({
	baseURL: API_BASE_URL,
	timeout: API_TIMEOUT,
	headers: { "Content-Type": "application/json" }
})

/**
 *  요청 인터셉터: 모든 요청에 SecureStore의 accessToken을 Authorization 헤더로 첨부
 */
secureApi.interceptors.request.use(
	async (config: InternalAxiosRequestConfig) => {
		const accessToken = await tokenStorage.get(ACCESS_TOKEN_STORED_KEY)
		// accessToken이 존재하면 Authorization 헤더에 토큰 추가
		if (accessToken) {
			config.headers.Authorization = `${TOKEN_TYPE} ${accessToken}`
		}
		return config
	},
	(error) => {
		return Promise.reject(error)
	}
)

secureApi.interceptors.response.use(
	(response: AxiosResponse) => response,
	async (error: AxiosError) => {
		// 요청 객체 가져오기
		const original = error.config as InternalAxiosRequestConfig & {
			_retry?: boolean
		}

		// 재발급 여부 판단
		if (!original || error.response?.status !== 401 || original._retry) {
			return Promise.reject(error)
		}

		// 재발급 상태 설정
		original._retry = true

		// 요청 객체 토큰 가져오기
		const sentAuthorization = original.headers.Authorization
		// 현재 저장 토큰 가져오기
		const currentAccessToken = await tokenStorage.get(ACCESS_TOKEN_STORED_KEY)
		// 토큰이 서로 다를 시 api 재요청
		if (currentAccessToken && sentAuthorization !== `${TOKEN_TYPE} ${currentAccessToken}`) {
			return secureApi(original)
		}

		// 재발급 요청이 완료될 때까지 큐에 넣어 대기
		if (isRefreshing) {
			return new Promise((resolve, reject) => {
				waitQueue.push((accessToken, refreshError) => {
					if (!accessToken) return reject(refreshError ?? error)
					original.headers.Authorization = `${TOKEN_TYPE} ${accessToken}`
					resolve(secureApi(original))
				})
			})
		}

		// 토큰 재발급
		isRefreshing = true
		let newAccessToken: string | null
		try {
			await accountService.reIssue()
			newAccessToken = await tokenStorage.get(ACCESS_TOKEN_STORED_KEY)
			if (newAccessToken == null) {
				throw new Error("SESSION_CHANGED")
			}
			flushQueue(newAccessToken)
		// 토큰 재발급 실패 시
		} catch (refreshError) {
			const sessionChanged =
				refreshError instanceof Error && refreshError.message === "SESSION_CHANGED"
			if (!sessionChanged) await accountService.reIssueError()
			flushQueue(null, refreshError)
			return Promise.reject(refreshError)
		} finally {
			isRefreshing = false
		}

		original.headers.Authorization = `${TOKEN_TYPE} ${newAccessToken}`
		return secureApi(original)
	}
)
