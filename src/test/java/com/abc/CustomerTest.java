package com.abc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

public class CustomerTest {
	private static final double DOUBLE_DELTA = 1e-15;

	@Mock
	Account mockCheckingAccount;

	@Mock
	Account mockSavingsAccount;

	@Before
	public void setupMock() {
		mockCheckingAccount = Mockito.mock(Account.class);
		mockSavingsAccount = Mockito.mock(Account.class);
	}

	@Test
	// Test customer statement generation
	public void testApp() {
		Customer henry = new Customer("Henry").openAccount(mockCheckingAccount).openAccount(mockSavingsAccount);

		Mockito.when(mockCheckingAccount.sumTransactions()).thenReturn(100.0);
		Mockito.when(mockSavingsAccount.sumTransactions()).thenReturn(3800.0);

		double savingsInterest = 10.0;
		double checkingInterest = 1.0;

		Mockito.when(mockCheckingAccount.interestEarned()).thenReturn(checkingInterest);
		Mockito.when(mockSavingsAccount.interestEarned()).thenReturn(savingsInterest);

		Mockito.when(mockCheckingAccount.getAccountType()).thenReturn(Account.Type.CHECKING);
		Mockito.when(mockSavingsAccount.getAccountType()).thenReturn(Account.Type.SAVINGS);

		List<Transaction> checkingTransactions = new ArrayList<Transaction>();
		checkingTransactions.add(new Transaction(100.0));

		List<Transaction> savingsTransactions = new ArrayList<Transaction>();
		savingsTransactions.add(new Transaction(4000.0));
		savingsTransactions.add(new Transaction(-200.0));

		Mockito.when(mockCheckingAccount.getTransactions()).thenReturn(checkingTransactions);
		Mockito.when(mockSavingsAccount.getTransactions()).thenReturn(savingsTransactions);

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
