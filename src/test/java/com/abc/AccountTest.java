package com.abc;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class AccountTest {
	private static final double DOUBLE_DELTA = 1e-15;

	@Test
	public final void testAccountChecking() {
		Account checkingAccount = new Account(Account.Type.CHECKING);
		assertEquals(checkingAccount.getAccountType(), Account.Type.CHECKING);

		checkingAccount.deposit(100.0);
		assertEquals(0.1, checkingAccount.interestEarned(), DOUBLE_DELTA);
	}

	@Test
	public final void testAccountSavings() {
		Account savingsAccount = new Account(Account.Type.SAVINGS);
		assertEquals(savingsAccount.getAccountType(), Account.Type.SAVINGS);

		savingsAccount.deposit(1500.0);
		assertEquals(2.0, savingsAccount.interestEarned(), DOUBLE_DELTA);
	}

	@Test
	public final void testAccountMaxiSavings() {
		Account maxiSavingsAccount = new Account(Account.Type.MAXI_SAVINGS);
		assertEquals(maxiSavingsAccount.getAccountType(), Account.Type.MAXI_SAVINGS);

		maxiSavingsAccount.deposit(3000.0);
		assertEquals(170.0, maxiSavingsAccount.interestEarned(), DOUBLE_DELTA);
	}

	@Test
	public final void testSumTransactions() {
		Account checkingAccount = new Account(Account.Type.CHECKING);

		checkingAccount.deposit(100);
		checkingAccount.withdraw(50);
		checkingAccount.deposit(200);

		double runningSum = 100 - 50 + 200;
		assertEquals(runningSum, checkingAccount.sumTransactions(), DOUBLE_DELTA);
	}

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public final void testNegativeTransactions() {
		thrown.expect(IllegalArgumentException.class);
		Account checkingAccount = new Account(Account.Type.CHECKING);

		checkingAccount.deposit(-50);
	}

	@Test
	public final void testAccountNumber() {
		Account account1 = new Account(Account.Type.CHECKING);
		Account account2 = new Account(Account.Type.SAVINGS);
		assertEquals(account1.getAccountNumber() + 1, account2.getAccountNumber());
	}

	@Test
	public final void testTransfer() {
		Account checkingAccount = new Account(Account.Type.CHECKING);
		Account savingsAccount = new Account(Account.Type.SAVINGS);
		checkingAccount.deposit(100, "deposit");
		checkingAccount.withdraw(50, "transfer to " + savingsAccount.getAccountNumber());
		savingsAccount.deposit(50, "transfer from " + checkingAccount.getAccountNumber());
		assertEquals(checkingAccount.sumTransactions(), 50, DOUBLE_DELTA);
		assertEquals(savingsAccount.sumTransactions(), 50, DOUBLE_DELTA);
	}
}
