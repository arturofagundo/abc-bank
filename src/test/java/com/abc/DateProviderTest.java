package com.abc;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class DateProviderTest {

	@Test
	public final void test() {
		DateProvider dateProvider = DateProvider.getInstance();

		assertNotNull(dateProvider);
		assertNotNull(dateProvider.now());
	}

}
