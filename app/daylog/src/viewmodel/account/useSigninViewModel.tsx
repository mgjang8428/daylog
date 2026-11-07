import { ServiceErrorCode } from "@/model/common/serviceErrorCode"
import { useSigninStore } from "@/model/store/signinStore"
import SigninRequest from "@/model/v1/account/dto/request/signinRequest"
import AccountService from "@/model/v1/account/service/accountService"
import ResponseV1 from "@/model/v1/dto/responseV1"
import { AxiosError } from "axios"
import { router } from "expo-router"
import { useState } from "react"

const accountService = new AccountService()

export default function useSigninViewModel() {
	const [isProceedSignin, setIsProceedSignin] = useState(false)

	const [email, setEmail] = useState("")
	const [password, setPassword] = useState("")

	const [signinError, setSigninError] = useState(false)

	const { storeSignin } = useSigninStore()

	async function doSignin() {
		if (isProceedSignin) return

		setIsProceedSignin(true)
		setSigninError(false)
		const request: SigninRequest = {
			email: email,
			password: password
		}
		await accountService
			.signin(request)
			.then(() => {
				storeSignin()
				router.replace("/home")
			})
			.catch((error: AxiosError<ResponseV1<void>>) => {
				if (error.response?.data.errorCode === ServiceErrorCode.INVALID_CREDENTIALS) {
					setSigninError(true)
				} else {
					alert("서버 오류")
				}
			})
			.finally(() => {
				setIsProceedSignin(false)
			})
	}

	return {
		isProceedSignin,
		email,
		setEmail,
		password,
		setPassword,
		doSignin,
		signinError
	}
}
