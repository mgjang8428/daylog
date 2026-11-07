export default interface ResponseV1<T> {
	isSuccess: boolean
	timestamp: string
	result: T
	errorCode: string | null
	errorMessage: string | null
}
