package com.abc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class BankTest {
	private static final double DOUBLE_DELTA = 1e-15;

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
		Account checkingAccount = new Account(Account.Type.CHECKING);
		Customer bill = new Customer("Bill");
		bill.openAccount(checkingAccount);

		Account savingsAccount = new Account(Account.Type.SAVINGS);
		bill.openAccount(savingsAccount);

		Account maxiSavingsAccount = new Account(Account.Type.MAXI_SAVINGS);
		bill.openAccount(maxiSavingsAccount);

		maxiSavingsAccount.deposit(3000.0);
		savingsAccount.deposit(1500.0);
		checkingAccount.deposit(100.0);

		Customer henry = new Customer("Henry");
		checkingAccount = new Account(Account.Type.CHECKING);
		henry.openAccount(checkingAccount);

		savingsAccount = new Account(Account.Type.SAVINGS);
		henry.openAccount(savingsAccount);

		maxiSavingsAccount = new Account(Account.Type.MAXI_SAVINGS);
		henry.openAccount(maxiSavingsAccount);

		maxiSavingsAccount.deposit(3000.0);
		maxiSavingsAccount.withdraw(1);
		maxiSavingsAccount.deposit(1);
		savingsAccount.deposit(1500.0);
		checkingAccount.deposit(100.0);

		bank.addCustomer(bill);
		bank.addCustomer(henry);

		assertEquals((0.1 + 2.0) * 2 + 3000 * 0.05 + 3000 * 0.001, bank.totalInterestPaid(), DOUBLE_DELTA);
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
