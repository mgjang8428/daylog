import { useSigninStore } from "@/model/store/signinStore"
import AccountService from "@/model/v1/account/service/accountService"
import { useState } from "react"

const accountService = new AccountService()

export default function useSignoutViewModel() {
	const [isProceedSignout, setIsProceedSignout] = useState(false)
	const { storeSignout } = useSigninStore()

	async function doSignout() {
		if (isProceedSignout) return
		setIsProceedSignout(true)
		storeSignout()
		await accountService
			.signout()
			.catch(() => {
				console.log("로그아웃 실패 (서버)")
			})
			.finally(() => {
				setIsProceedSignout(false)
			})
	}
	return { doSignout, isProceedSignout }
}
