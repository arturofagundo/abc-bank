package com.abc;

import java.util.Calendar;
import java.util.Date;

public class DateProvider {
	// Moved to eager initialization to avoid thread synchronization issues
	// associated with lazy initialization
	private static DateProvider instance = new DateProvider();

	/*
	 * Enforce singleton property with a private constructor
	 */
	private DateProvider() {

	}

	/*
	 * Return singleton instance of DateProvider object
	 * 
	 * @return instance
	 */
	public static DateProvider getInstance() {
		return instance;
	}

	/*
	 * Get current time
	 * 
	 * @return current time
	 */
	public Date now() {
		return Calendar.getInstance().getTime();
	}
}
