package com.abc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

public class DateProviderTest {

	@Test
	public final void test() {
		DateProvider dateProvider = DateProvider.getInstance();

		assertNotNull(dateProvider);
		assertNotNull(dateProvider.now());
	}

	@Test
	public final void testDaysSince() {
		String startDateString = "2016-04-00";
		String endDateString = "2016-04-10";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date startDate = format.parse(startDateString);
			Date endDate = format.parse(endDateString);
			assertEquals(DateProvider.getInstance().daysSince(startDate, endDate), 10);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			assert (false);
		}

	}
}
