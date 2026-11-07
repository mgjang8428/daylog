import { ACCESS_TOKEN_STORED_KEY, REFRESH_TOKEN_STORED_KEY } from "@/model/common/consts"
import { api } from "@/model/network/api"
import {
	REISSUE_API_V1,
	SIGNIN_API_V1,
	SIGNOUT_API_V1,
	SIGNUP_API_V1
} from "@/model/network/apiUrl"
import tokenStorage from "@/model/store/tokenStorage"
import SigninRequest from "@/model/v1/account/dto/request/signinRequest"
import SignupRequest from "@/model/v1/account/dto/request/signupRequest"
import ReissueResponse from "@/model/v1/account/dto/response/reissueResponse"
import SigninResponse from "@/model/v1/account/dto/response/signinResponse"
import ResponseV1 from "@/model/v1/dto/responseV1"

import { useSigninStore } from "@/model/store/signinStore"
import ReissueRequest from "@/model/v1/account/dto/request/reissueRequest"
import SignoutRequest from "@/model/v1/account/dto/request/signoutRequest"
import { AxiosResponse } from "axios"

class AccountService {
	/**
	 * 회원가입
	 * @param request 회원가입 요청 dto
	 */
	public async signup(request: SignupRequest): Promise<void> {
		// 회원가입 요청 api
		await api.post(SIGNUP_API_V1, request).then((response: AxiosResponse<ResponseV1<void>>) => {
			return response.data.result
		})
	}

	/**
	 * 로그인
	 * @param request 로그인 요청 dto
	 */
	public async signin(request: SigninRequest): Promise<void> {
		// 로그인 요청 api
		const response: SigninResponse = await api
			.post(SIGNIN_API_V1, request)
			.then((response: AxiosResponse<ResponseV1<SigninResponse>>) => {
				return response.data.result
			})
		// accessToken, refreshToken 저장
		await this.setTokens(response.accessToken, response.refreshToken)
	}

	/**
	 * 로그아웃
	 */
	public async signout(): Promise<void> {
		// 보유하고 있던 refreshToken 가져오기
		const refreshToken = await tokenStorage.get(REFRESH_TOKEN_STORED_KEY)
		// refreshToken이 없을 시
		if (refreshToken === null) {
			useSigninStore.getState().storeSignout()
			throw new Error("EMPTY_REFRESHTOKEN")
		}
		// 토큰 정리
		await this.deleteTokens()
		// refreshToken
		const request: SignoutRequest = { refreshToken: refreshToken }
		// 로그아웃 요청 api
		await api.post(SIGNOUT_API_V1, request)
	}

	/**
	 * 토큰 재발행 및 저장
	 */
	public async reIssue(): Promise<void> {
		// refreshToken 가져오기
		const refreshToken = await tokenStorage.get(REFRESH_TOKEN_STORED_KEY)
		if (refreshToken == null) throw new Error("REFRESHTOKEN_EMPTY")
		// api 토큰 재발행 요청
		const request: ReissueRequest = { refreshToken: refreshToken }
		return await api
			.post(REISSUE_API_V1, request)
			.then(async (response: AxiosResponse<ResponseV1<ReissueResponse>>) => {
				if ((await tokenStorage.get(REFRESH_TOKEN_STORED_KEY)) !== refreshToken) {
					throw new Error("SESSION_CHANGED")
				}
				const result = response.data.result
				await this.setTokens(result.accessToken, result.refreshToken)
			})
			.catch(async (error) => {
				if ((await tokenStorage.get(REFRESH_TOKEN_STORED_KEY)) !== refreshToken) {
					throw new Error("SESSION_CHANGED")
				}
				throw error
			})
	}

	/**
	 * 토큰 재발행 에러 처리
	 */
	public async reIssueError(): Promise<void> {
		alert("재로그인이 필요합니다.")
		await this.deleteTokens().catch(() => {})
		useSigninStore.getState().storeSignout()
	}

	/**
	 * 토큰 세팅 처리
	 * @param accessToken accessToken
	 * @param refreshToken refreshToken
	 */
	private async setTokens(accessToken: string, refreshToken: string): Promise<void> {
		await tokenStorage.set(ACCESS_TOKEN_STORED_KEY, accessToken)
		await tokenStorage.set(REFRESH_TOKEN_STORED_KEY, refreshToken)
	}

	/**
	 * 토큰 삭제 처리
	 */
	private async deleteTokens(): Promise<void> {
		await tokenStorage.remove(ACCESS_TOKEN_STORED_KEY)
		await tokenStorage.remove(REFRESH_TOKEN_STORED_KEY)
	}
}

export const accountService = new AccountService()