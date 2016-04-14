package com.abc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
 * Associated with individual customers. This class calculates interest for accounts of
 * various types.
 */
public class Account {

	public static enum Type {
		CHECKING(0), SAVINGS(1), MAXI_SAVINGS(2);

		private final int value;

		Type(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	};

	private final Type accountType;
	private List<Transaction> transactions;
	private final int accountNumber;
	private static Integer nextAccountNumber = 1;

	/*
	 * Returns a defensive copy of the list of transactions
	 */
	public List<Transaction> getTransactions() {
		List<Transaction> result;
		synchronized (transactions) {
			result = new ArrayList<Transaction>(transactions);
		}
		return result;
	}

	public Account(Type accountType) {
		this.accountType = accountType;
		this.transactions = new ArrayList<Transaction>();
		synchronized (nextAccountNumber) {
			accountNumber = nextAccountNumber;
			nextAccountNumber++;
		}
	}

	/*
	 * Deposit money into the given account. This method creates a transaction.
	 * 
	 * @param amount Dollar value of deposit
	 * 
	 * @throws IllegalArgumentExeption if the amount is negative
	 */
	public void deposit(double amount) {
		if (amount <= 0) {
			throw new IllegalArgumentException("amount must be greater than zero");
		} else {
			synchronized (transactions) {
				transactions.add(new Transaction(amount));
			}
		}
	}

	/*
	 * Withdraw money from a given account. This method creates a transaction.
	 * 
	 * @param amount Dollar value of withdrawal
	 * 
	 * @throws IllegalArgumentException if the amount is negative
	 */
	public void withdraw(double amount) {
		if (amount <= 0) {
			throw new IllegalArgumentException("amount must be greater than zero");
		} else {
			synchronized (transactions) {
				transactions.add(new Transaction(-amount));
			}
		}
	}

	/*
	 * Deposit with description
	 * 
	 * @param amount Dollar value of deposit
	 * 
	 * @param description of deposit
	 * 
	 * @throws IllegalArgumentException if the amount is negative
	 */
	public void deposit(double amount, String description) {
		if (amount <= 0) {
			throw new IllegalArgumentException("amount must be greater than zero");
		} else {
			synchronized (transactions) {
				transactions.add(new Transaction(amount, description));
			}
		}
	}

	/*
	 * Withdrawal with description
	 * 
	 * @param amount Dollar value of withdrawal
	 * 
	 * @param description of withdrawal
	 * 
	 * @throws IllegalArgumentException if the amount is negative
	 */
	public void withdraw(double amount, String description) {
		if (amount <= 0) {
			throw new IllegalArgumentException("amount must be greater than zero");
		} else {
			synchronized (transactions) {
				transactions.add(new Transaction(-amount, description));
			}
		}
	}

	/*
	 * Calculate the interest earned based on the current balance and the
	 * account type.
	 * 
	 * @return the dollar amount of all interest earned on this account.
	 */
	public double interestEarned() {
		switch (accountType) {
		case SAVINGS:
			return savingsInterestEarned();
		case MAXI_SAVINGS:
			return maxiSavingsInterestEarned();
		default:
			return checkingInterestEarned();
		}
	}

	private double savingsInterestEarned() {
		if (transactions.isEmpty())
			return 0;

		double lowBalAPR = 0.001;
		double highBalAPR = 0.002;
		Iterator<Transaction> transactionIterator = transactions.iterator();
		Transaction currTransaction = transactionIterator.next();
		double currBalance = currTransaction.amount;
		int daysOfAccumInterest;
		while (transactionIterator.hasNext()) {
			Transaction nextTransaction = transactionIterator.next();
			daysOfAccumInterest = DateProvider.getInstance().daysSince(currTransaction.getDate(), nextTransaction.getDate());
			for (int i = 0; i < daysOfAccumInterest; i++) {
				if (currBalance < 1000) {
					currBalance = currBalance * (1 + lowBalAPR / 365);
				} else {
					currBalance = 1000 * (1 + lowBalAPR / 365) + (currBalance - 1000) * (1 + highBalAPR / 365);
				}
			}
			currTransaction = nextTransaction;
			currBalance += currTransaction.amount;
		}

		daysOfAccumInterest = DateProvider.getInstance().daysSince(currTransaction.getDate());
		for (int i = 0; i < daysOfAccumInterest; i++) {
			if (currBalance < 1000) {
				currBalance = currBalance * (1 + lowBalAPR / 365);
			} else {
				currBalance = 1000 * (1 + lowBalAPR / 365) + (currBalance - 1000) * (1 + highBalAPR / 365);
			}
		}
		double amount = sumTransactions();
		return currBalance - amount;
	}

	private double maxiSavingsInterestEarned() {
		double lowAPR = 0.001;
		double highAPR = 0.05;
		Iterator<Transaction> transactionIterator = transactions.iterator();
		Transaction currTransaction = transactionIterator.next();
		double currBalance = currTransaction.amount;
		int daysOfAccumInterest;
		int daysOfLowAPR = 0;
		while (transactionIterator.hasNext()) {
			Transaction nextTransaction = transactionIterator.next();
			daysOfAccumInterest = DateProvider.getInstance().daysSince(currTransaction.getDate(), nextTransaction.getDate());
			for (int i = 0; i < daysOfAccumInterest; i++) {
				if (daysOfLowAPR > 0) {
					currBalance = currBalance * (1 + lowAPR / 365);
					daysOfLowAPR--;
				} else {
					currBalance = currBalance * (1 + highAPR / 365);
				}
			}
			currTransaction = nextTransaction;
			if (currTransaction.amount < 0) {
				daysOfLowAPR = 10;
			}
			currBalance += currTransaction.amount;
		}

		daysOfAccumInterest = DateProvider.getInstance().daysSince(currTransaction.getDate());
		for (int i = 0; i < daysOfAccumInterest; i++) {
			if (daysOfLowAPR > 0) {
				currBalance = currBalance * (1 + lowAPR / 365);
				daysOfLowAPR--;
			} else {
				currBalance = currBalance * (1 + highAPR / 365);
			}
		}
		double amount = sumTransactions();
		return currBalance - amount;
	}

	/*
	 * Assume all transactions end in the past. Assume customer is well-behaved
	 * and never allows his balance to go below 0.
	 */
	private double checkingInterestEarned() {
		if (transactions.isEmpty())
			return 0;

		double checkingAPR = 0.001;
		Iterator<Transaction> transactionIterator = transactions.iterator();
		Transaction currTransaction = transactionIterator.next();
		double currBalance = currTransaction.amount;
		int daysOfAccumInterest;
		while (transactionIterator.hasNext()) {
			Transaction nextTransaction = transactionIterator.next();
			daysOfAccumInterest = DateProvider.getInstance().daysSince(currTransaction.getDate(), nextTransaction.getDate());
			currBalance *= Math.pow(1 + checkingAPR / 365, daysOfAccumInterest);
			currTransaction = nextTransaction;
			currBalance += currTransaction.amount;
		}

		daysOfAccumInterest = DateProvider.getInstance().daysSince(currTransaction.getDate());
		currBalance *= Math.pow(1 + checkingAPR / 365, daysOfAccumInterest);
		double amount = sumTransactions();
		return currBalance - amount;
	}

	/*
	 * Calculate the sum of all transactions for the account.
	 * 
	 * @return the dollar amount of the sum of all transactions
	 */
	public double sumTransactions() {
		double amount = 0.0;
		synchronized (transactions) {
			for (Transaction t : transactions)
				amount += t.amount;
		}
		return amount;
	}

	public Type getAccountType() {
		return accountType;
	}

	/*
	 * Return unique account number
	 * 
	 * @returns account number
	 */
	public int getAccountNumber() {
		return accountNumber;
	}
}
