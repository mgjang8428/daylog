import { Tabs } from "expo-router"

export default function TabsLayout() {
	return (
		<Tabs screenOptions={{ headerShown: false }}>
			<Tabs.Screen
				name="home"
				options={{
					title: "Home"
				}}
			/>
			<Tabs.Screen
				name="timelog"
				options={{
					title: "TimeLog"
				}}
			/>
			<Tabs.Screen
				name="myinfo"
				options={{
					title: "MyInfo"
				}}
			/>
		</Tabs>
	)
}
