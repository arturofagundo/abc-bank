package com.abc;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ DateProvider.class })
public class AccountTest {
	private static final double DOUBLE_DELTA = 1e-15;

	@Mock
	DateProvider mockDateProvider;

	Map<Integer, Double> testVector;

	public Account accountMockSetup(Account.Type type) {
		Account account = new Account(type);

		// Mock up a series of transactions using the test Vector
		Date d = DateProvider.getInstance().now();

		PowerMockito.mockStatic(DateProvider.class);
		PowerMockito.when(DateProvider.getInstance()).thenReturn(mockDateProvider);

		Iterator<Entry<Integer, Double>> entryIterator = testVector.entrySet().iterator();
		Entry<Integer, Double> currEntry = entryIterator.next();
		Date currTransDate = new Date(d.getTime() - currEntry.getKey() * 24 * 3600 * 1000);
		Mockito.when(mockDateProvider.now()).thenReturn(currTransDate);
		Mockito.when(mockDateProvider.daysSince(currTransDate)).thenReturn(currEntry.getKey());
		Mockito.when(mockDateProvider.now()).thenReturn(currTransDate);
		Mockito.when(mockDateProvider.daysSince(currTransDate)).thenReturn(currEntry.getKey());

		// Should make a deposit daysOfAccumInterest days in the past
		if (currEntry.getValue() > 0) {
			account.deposit(currEntry.getValue());
		} else {
			account.withdraw(-currEntry.getValue());
		}

		while (entryIterator.hasNext()) {
			Entry<Integer, Double> nextEntry = entryIterator.next();
			Date nextTransDate = new Date(d.getTime() - nextEntry.getKey() * 24 * 3600 * 1000);
			Mockito.when(mockDateProvider.daysSince(currTransDate, nextTransDate)).thenReturn(currEntry.getKey() - nextEntry.getKey());
			currEntry = nextEntry;
			currTransDate = nextTransDate;
			Mockito.when(mockDateProvider.now()).thenReturn(currTransDate);
			Mockito.when(mockDateProvider.daysSince(currTransDate)).thenReturn(currEntry.getKey());
			Mockito.when(mockDateProvider.now()).thenReturn(currTransDate);
			Mockito.when(mockDateProvider.daysSince(currTransDate)).thenReturn(currEntry.getKey());

			// Should make a deposit daysOfAccumInterest days in the past
			if (currEntry.getValue() > 0) {
				account.deposit(currEntry.getValue());
			} else {
				account.withdraw(-currEntry.getValue());
			}
		}
		List<Transaction> transactions = account.getTransactions();
		assertEquals(transactions.size(), testVector.size());

		return account;
	}

	@Before
	public void setup() {
		mockDateProvider = Mockito.mock(DateProvider.class);
		testVector = new HashMap<Integer, Double>();
		testVector.put(80, 50.0);
		testVector.put(40, -20.0);
		testVector.put(35, 20.0);
		testVector.put(5, -5.0);
	}

	@Test
	public final void testAccountChecking() {
		Account checkingAccount = accountMockSetup(Account.Type.CHECKING);
		assertEquals(checkingAccount.getAccountType(), Account.Type.CHECKING);

		// Calculate expected interest consistent with checking account
		// calculations
		Iterator<Entry<Integer, Double>> entryIterator = testVector.entrySet().iterator();
		Entry<Integer, Double> currEntry = entryIterator.next();
		double currBalance = currEntry.getValue();
		while (entryIterator.hasNext()) {
			Entry<Integer, Double> nextEntry = entryIterator.next();
			int daysOfAccumInterest = currEntry.getKey() - nextEntry.getKey();
			currBalance *= Math.pow(1 + 0.001 / 365, daysOfAccumInterest);
			currBalance += nextEntry.getValue();
			currEntry = nextEntry;
		}

		currBalance *= Math.pow(1 + 0.001 / 365, currEntry.getKey());
		double expectedInterest = currBalance - checkingAccount.sumTransactions();
		assertEquals(expectedInterest, checkingAccount.interestEarned(), DOUBLE_DELTA);
	}

	@Test
	public final void testAccountSavings() {
		Account savingsAccount = accountMockSetup(Account.Type.SAVINGS);
		assertEquals(savingsAccount.getAccountType(), Account.Type.SAVINGS);

		// Calculate expected interest consistent with checking account
		// calculations
		Iterator<Entry<Integer, Double>> entryIterator = testVector.entrySet().iterator();
		Entry<Integer, Double> currEntry = entryIterator.next();
		double currBalance = currEntry.getValue();
		while (entryIterator.hasNext()) {
			Entry<Integer, Double> nextEntry = entryIterator.next();
			int daysOfAccumInterest = currEntry.getKey() - nextEntry.getKey();
			for (int i = 0; i < daysOfAccumInterest; i++) {
				if (currBalance < 1000) {
					currBalance = currBalance * (1 + 0.001 / 365);
				} else {
					currBalance = 1000 * (1 + 0.001 / 365) + (currBalance - 1000) * (1 + 0.002 / 365);
				}
			}
			currBalance += nextEntry.getValue();
			currEntry = nextEntry;
		}

		for (int i = 0; i < currEntry.getKey(); i++) {
			if (currBalance < 1000) {
				currBalance = currBalance * (1 + 0.001 / 365);
			} else {
				currBalance = 1000 * (1 + 0.001 / 365) + (currBalance - 1000) * (1 + 0.002 / 365);
			}
		}
		double expectedInterest = currBalance - savingsAccount.sumTransactions();
		assertEquals(expectedInterest, savingsAccount.interestEarned(), DOUBLE_DELTA);
		/*
		 * int daysOfAccumInterest = 3;
		 * 
		 * Date d = DateProvider.getInstance().now(); Date dateBefore = new
		 * Date(d.getTime() - daysOfAccumInterest * 24 * 3600 * 1000);
		 * 
		 * PowerMockito.mockStatic(DateProvider.class);
		 * PowerMockito.when(DateProvider
		 * .getInstance()).thenReturn(mockDateProvider);
		 * Mockito.when(mockDateProvider.now()).thenReturn(dateBefore);
		 * Mockito.when
		 * (mockDateProvider.daysSince(dateBefore)).thenReturn(daysOfAccumInterest
		 * );
		 * 
		 * // Should make a deposit 365 days in the past
		 * savingsAccount.deposit(2000.0);
		 * 
		 * List<Transaction> transactions = savingsAccount.getTransactions();
		 * assertEquals(transactions.size(), 1);
		 * assertEquals(transactions.get(0).getDate().getTime(),
		 * dateBefore.getTime(), DOUBLE_DELTA);
		 * 
		 * double currBalance = 2000.0; for (int i = 0; i < daysOfAccumInterest;
		 * i++) { currBalance = 1000 * (1 + 0.001 / 365) + (currBalance - 1000)
		 * * (1 + 0.002 / 365); } double expectedInterest = currBalance - 2000;
		 * assertEquals(expectedInterest, savingsAccount.interestEarned(),
		 * DOUBLE_DELTA);
		 */
	}

	@Test
	public final void testAccountMaxiSavings() {
		Account maxiSavingsAccount = new Account(Account.Type.MAXI_SAVINGS);
		assertEquals(maxiSavingsAccount.getAccountType(), Account.Type.MAXI_SAVINGS);

		int daysOfAccumInterest = 20;

		Date d = DateProvider.getInstance().now();
		Date openingDate = new Date(d.getTime() - daysOfAccumInterest * 24 * 3600 * 1000);

		int daysSinceLastWithdrawal = 12;
		Date withdrawalDate = new Date(d.getTime() - daysSinceLastWithdrawal * 24 * 3600 * 1000);

		PowerMockito.mockStatic(DateProvider.class);
		PowerMockito.when(DateProvider.getInstance()).thenReturn(mockDateProvider);
		Mockito.when(mockDateProvider.now()).thenReturn(openingDate);
		Mockito.when(mockDateProvider.daysSince(openingDate)).thenReturn(daysOfAccumInterest);

		// Should make a deposit daysOfAccumInterest days in the past
		maxiSavingsAccount.deposit(1000.0);

		Mockito.when(mockDateProvider.now()).thenReturn(withdrawalDate);
		Mockito.when(mockDateProvider.daysSince(withdrawalDate)).thenReturn(daysSinceLastWithdrawal);
		Mockito.when(mockDateProvider.daysSince(openingDate, withdrawalDate)).thenReturn(daysOfAccumInterest - daysSinceLastWithdrawal);

		maxiSavingsAccount.withdraw(1.0);

		double currBalance = 1000.0;

		for (int i = 0; i < daysOfAccumInterest; i++) {
			if (i < (daysOfAccumInterest - daysSinceLastWithdrawal)) {
				currBalance = currBalance * (1 + 0.05 / 365);
			} else if (i == (daysOfAccumInterest - daysSinceLastWithdrawal)) {
				currBalance = (currBalance - 1.0) * (1 + 0.001 / 365);
			} else if (i < (daysOfAccumInterest - daysSinceLastWithdrawal + 10)) {
				currBalance = currBalance * (1 + 0.001 / 365);
			} else {
				currBalance = currBalance * (1 + 0.05 / 365);
			}
		}

		double expectedInterest = currBalance - 999.0;
		assertEquals(expectedInterest, maxiSavingsAccount.interestEarned(), DOUBLE_DELTA);
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
