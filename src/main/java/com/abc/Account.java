package com.abc;

import java.util.ArrayList;
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
	 * Calculate the interest earned based on the current balance and the
	 * account type.
	 * 
	 * @return the dollar amount of all interest earned on this account.
	 */
	public double interestEarned() {
		double amount = sumTransactions();
		switch (accountType) {
		case SAVINGS:
			if (amount <= 1000)
				return amount * 0.001;
			else
				return 1 + (amount - 1000) * 0.002;
		case MAXI_SAVINGS:
			if (amount <= 1000)
				return amount * 0.02;
			if (amount <= 2000)
				return 20 + (amount - 1000) * 0.05;
			return 70 + (amount - 2000) * 0.1;
		default:
			return amount * 0.001;
		}
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

}
