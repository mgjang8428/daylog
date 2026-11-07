import { getTimeZones } from "@vvo/tzdb"
import { getCalendars } from "expo-localization"

export function getTimezoneAreas(): string[] {
	const areas = new Set<string>()
	for (const tz of getTimeZones()) {
		const idx = tz.name.indexOf("/")
		if (idx !== -1) {
			areas.add(tz.name.slice(0, idx))
		}
	}
	return [...areas].sort()
}

export function getTimezoneLocations(area: string): string[] {
	const prefix = `${area}/`
	const locations = getTimeZones()
		.filter((tz) => tz.name.startsWith(prefix))
		.map((tz) => tz.name.slice(prefix.length))
		.sort()
	return locations
}

export type TimezoneSelection = {
	area: string
	location: string
}

export function getDeviceTimezone(): TimezoneSelection | null {
	const deviceTz = getCalendars()[0]?.timeZone ?? Intl.DateTimeFormat().resolvedOptions().timeZone
	if (!deviceTz) return null

	const zones = getTimeZones()
	const matched =
		zones.find((tz) => tz.name === deviceTz) ?? zones.find((tz) => tz.group.includes(deviceTz))
	if (!matched) return null

	const idx = matched.name.indexOf("/")
	if (idx === -1) return null
	return {
		area: matched.name.slice(0, idx),
		location: matched.name.slice(idx + 1)
	}
}
