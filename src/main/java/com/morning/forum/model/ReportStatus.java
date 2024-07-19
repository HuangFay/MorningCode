package com.morning.forum.model;

public enum ReportStatus {
	PENDING(0),
	ACCEPT(1),
	REJECT(2);
	
	private final int intValue;

	ReportStatus(int intValue) {
        this.intValue = intValue;
    }

    public int toInt() {
        return intValue;
    }
}
