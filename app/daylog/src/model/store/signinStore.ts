import tokenStorage from "@/model/store/tokenStorage"
import { create } from "zustand"

interface SigninState {
	storeIsLoading: boolean
	storeIsSignin: boolean
	storeRestore: () => Promise<void>
	storeSignin: () => void
	storeSignout: () => void
}

export const useSigninStore = create<SigninState>((set) => ({
	storeIsLoading: true,
	storeIsSignin: false,

	storeRestore: async () => {
		try {
			if (await tokenStorage.isSignin()) {
				set({
					storeIsLoading: false,
					storeIsSignin: true
				})
			} else {
				set({
					storeIsLoading: false,
					storeIsSignin: false
				})
			}
		} catch (error) {
			console.log(error)
			set({ storeIsLoading: false })
		} finally {
			set({ storeIsLoading: false })
		}
	},
	storeSignin: () => {
		set({ storeIsSignin: true })
	},
	storeSignout: () => {
		set({ storeIsSignin: false })
	}
}))
