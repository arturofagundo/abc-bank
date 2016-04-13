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
	 * Return the number of days between the specified dates
	 * 
	 * @param startDate
	 * 
	 * @param endDate
	 * 
	 * @return number of days
	 * 
	 * @throws NullPointerException if either date is null
	 * 
	 * @throws IllegalArgumentException if the startDate comes after the endDate
	 */
	public int daysSince(Date startDate, Date endDate) {
		if (startDate == null || endDate == null) {
			throw new NullPointerException();
		}

		long startTime = startDate.getTime();
		long endTime = endDate.getTime();
		if (startTime > endTime) {
			throw new IllegalArgumentException();
		}

		// Ignore error associated with time changes for now. Alternatively this
		// could
		// be calculated by adding days to the startDate, until the
		// startDate.getTime()
		// exceeded endDate.getTime()
		return (int) ((endTime - startTime) / (24 * 60 * 60 * 1000));
	}

	/*
	 * Return the number of days between the specified date and now
	 * 
	 * @param startDate
	 * 
	 * @return number of days
	 * 
	 * @throws NullPointerException if either date is null
	 * 
	 * @throws IllegalArgumentException if the startDate comes after the endDate
	 */
	public int daysSince(Date startDate) {
		Date endDate = now();

		return daysSince(startDate, endDate);
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
