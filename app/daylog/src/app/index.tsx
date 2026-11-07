import { router } from "expo-router"
import { Button, Image, View } from "react-native"

export default function Index() {
	return (
		<View>
			<Image
				source={require("@assets/images/daylog_logo.png")}
				style={{ width: 300, height: 300 }}
			/>
			<Button
				title="로그인"
				onPress={() => {
					router.push("/signin")
				}}
			/>
			<Button
				title="가입"
				onPress={() => {
					router.push("/signup")
				}}
			/>
		</View>
	)
}
