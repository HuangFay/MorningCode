package com.morning.forum.model;

public enum PostStatus {
	SHOW(1),
	HIDE(0);
	
	private final int intValue;

	PostStatus(int intValue) {
        this.intValue = intValue;
    }

    public int toInt() {
        return intValue;
    }
}
