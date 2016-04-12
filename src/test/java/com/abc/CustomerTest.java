package com.abc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CustomerTest {
	private static final double DOUBLE_DELTA = 1e-15;

	@Test
	// Test customer statement generation
	public void testApp() {

		Account checkingAccount = new Account(Account.Type.CHECKING);
		Account savingsAccount = new Account(Account.Type.SAVINGS);

		Customer henry = new Customer("Henry").openAccount(checkingAccount).openAccount(savingsAccount);

		checkingAccount.deposit(100.0);
		savingsAccount.deposit(4000.0);
		savingsAccount.withdraw(200.0);

		double savingsInterest = savingsAccount.interestEarned();
		double checkingInterest = checkingAccount.interestEarned();
		assertEquals(henry.totalInterestEarned(), savingsInterest + checkingInterest, DOUBLE_DELTA);
		assertEquals("Statement for Henry\n" + "\n" + "Checking Account\n" + "  deposit $100.00\n" + "Total $100.00\n" + "\n" + "Savings Account\n"
				+ "  deposit $4,000.00\n" + "  withdrawal $200.00\n" + "Total $3,800.00\n" + "\n" + "Total In All Accounts $3,900.00", henry.getStatement());
	}

	@Test
	public void testOneAccount() {
		Customer oscar = new Customer("Oscar").openAccount(new Account(Account.Type.SAVINGS));
		assertEquals(1, oscar.getNumberOfAccounts());
	}

	@Test
	public void testTwoAccount() {
		Customer oscar = new Customer("Oscar").openAccount(new Account(Account.Type.SAVINGS));
		oscar.openAccount(new Account(Account.Type.CHECKING));
		assertEquals(2, oscar.getNumberOfAccounts());
	}

	@Test
	public void testThreeAcounts() {
		Customer oscar = new Customer("Oscar").openAccount(new Account(Account.Type.SAVINGS));
		oscar.openAccount(new Account(Account.Type.CHECKING));
		oscar.openAccount(new Account(Account.Type.MAXI_SAVINGS));
		assertEquals(3, oscar.getNumberOfAccounts());
	}

	@Test
	public void testGetName() {
		Customer henry = new Customer("Henry");
		assertTrue("Henry".equals(henry.getName()));
	}
}
