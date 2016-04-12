package com.abc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TransactionTest {
	@Test
	public void testTransaction() {
		Transaction t = new Transaction(5);
		assertTrue(t instanceof Transaction);
		assertNotNull(t.getDate());
	}

	@Test
	public void testDescription() {
		Transaction deposit = new Transaction(10);
		Transaction withdrawal = new Transaction(-10);
		Transaction transfer = new Transaction(10, "transfer");

		assertEquals(deposit.getDescription(), "deposit");
		assertEquals(withdrawal.getDescription(), "withdrawal");
		assertEquals(transfer.getDescription(), "transfer");
	}
}
