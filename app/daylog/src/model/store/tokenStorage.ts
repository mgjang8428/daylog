import { REFRESH_TOKEN_STORED_KEY, TOKEN_ISSUER } from "@/model/common/consts"
import * as SecureStore from "expo-secure-store"
import { jwtDecode, JwtPayload } from "jwt-decode"
import { Platform } from "react-native"

const tokenStorage = {
	get: async (key: string): Promise<string | null> => {
		return Platform.OS === "web"
			? await Promise.resolve(localStorage.getItem(key))
			: await SecureStore.getItemAsync(key)
	},

	set: async (key: string, value: string): Promise<void> => {
		if (Platform.OS === "web") {
			localStorage.setItem(key, value)
		} else {
			await SecureStore.setItemAsync(key, value)
		}
	},

	remove: async (key: string): Promise<void> => {
		if (Platform.OS === "web") {
			localStorage.removeItem(key)
		} else {
			await SecureStore.deleteItemAsync(key)
		}
	},

	isSignin: async (): Promise<boolean> => {
		const refreshToken =
			Platform.OS === "web"
				? await Promise.resolve(localStorage.getItem(REFRESH_TOKEN_STORED_KEY))
				: await SecureStore.getItemAsync(REFRESH_TOKEN_STORED_KEY)

		if (refreshToken === null) {
			return false
		}

		try {
			const { iss, exp } = jwtDecode<JwtPayload>(refreshToken)
			if (iss !== TOKEN_ISSUER) {
				return false
			}
			if (exp === undefined) {
				return false
			}
			if (exp * 1000 < Date.now()) {
				return false
			}
			return true
		} catch (error) {
			console.log(error)
			return false
		}
	}
}

export default tokenStorage
