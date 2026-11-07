import { API_BASE_URL, API_TIMEOUT } from "@/model/common/consts"
import { create as axiosCreate } from "axios"

/**
 * 인증 없는 api
 */
export const api = axiosCreate({
	baseURL: API_BASE_URL,
	timeout: API_TIMEOUT,
	headers: { "Content-Type": "application/json" }
})
