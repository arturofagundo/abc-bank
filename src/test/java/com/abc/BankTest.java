package com.abc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

public class BankTest {
	private static final double DOUBLE_DELTA = 1e-15;

	@Mock
	Customer mockHenry;

	@Mock
	Customer mockBill;

	@Before
	public void setupMock() {
		mockHenry = Mockito.mock(Customer.class);
		mockBill = Mockito.mock(Customer.class);
	}

	@Test
	public void customerSummary() {
		Bank bank = new Bank();
		Customer john = new Customer("John");
		john.openAccount(new Account(Account.Type.CHECKING));
		bank.addCustomer(john);

		assertEquals("Customer Summary\n - John (1 account)", bank.customerSummary());
	}

	@Test
	public void testTotalInterestPaid() {
		Bank bank = new Bank();

		Mockito.when(mockHenry.totalInterestEarned()).thenReturn(100.0);
		Mockito.when(mockBill.totalInterestEarned()).thenReturn(50.0);
		bank.addCustomer(mockBill);
		bank.addCustomer(mockHenry);

		assertEquals(150.0, bank.totalInterestPaid(), DOUBLE_DELTA);
	}

	@Test
	public void testAddCustomer() {
		Bank bank = new Bank();
		assertNull(bank.getFirstCustomer());

		Customer firstCustomer = new Customer("First");
		Customer secondCustomer = new Customer("Second");

		bank.addCustomer(firstCustomer);
		bank.addCustomer(secondCustomer);

		assertNotNull(bank.getFirstCustomer());
	}
}
