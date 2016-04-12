package com.abc;

import java.util.Date;

/*
 * Represents all transactions within an account
 */
public class Transaction {
	public final double amount;

	private final Date transactionDate;

	public Transaction(double amount) {
		this.amount = amount;
		this.transactionDate = DateProvider.getInstance().now();
	}

	/*
	 * Returns a defensive copy of the transaction date
	 * 
	 * @return transactionDate
	 */
	public Date getDate() {
		return new Date(transactionDate.getTime());
	}

}
