package com.watsoo.dms.enums;

public enum EventType {

	CLOSE_EYES("closeEyes"), DISTRACTION("distraction"), LOW_HEAD("lowHead"), DRINKING("drinking"), NO_FACE("noFace"),
	MOBILE_USE("phoneCalling"), SMOKING("smoking"), YAWNING("yawning"), POWER_CUT("powerCut"), ALL("All");

	private final String type;

	EventType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public static EventType fromType(String type) {
		for (EventType eventType : values()) {
			if (eventType.getType().equalsIgnoreCase(type)) {
				return eventType;
			}
		}
		return null;
	}
}
