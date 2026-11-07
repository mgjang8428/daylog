import useSignoutViewModel from "@/viewmodel/account/useSignoutViewModel"
import { Button, Text, View } from "react-native"

export default function MyInfo() {
	const { doSignout, isProceedSignout } = useSignoutViewModel()
	return (
		<View>
			<Text>myinfo</Text>
			<Button title="로그아웃" onPress={doSignout} disabled={isProceedSignout} />
		</View>
	)
}
