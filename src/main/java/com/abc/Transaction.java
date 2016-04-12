package com.abc;

import java.util.Date;

/*
 * Represents all transactions within an account
 */
public class Transaction {
	public final double amount;

	private final Date transactionDate;
	private final String description;

	public Transaction(double amount) {
		this.amount = amount;
		this.transactionDate = DateProvider.getInstance().now();
		if (amount > 0) {
			this.description = "deposit";
		} else {
			this.description = "withdrawal";
		}
	}

	public Transaction(double amount, String description) {
		this.amount = amount;
		this.description = description;
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

	/*
	 * Returns the transaction description
	 * 
	 * @return user-friendly description of the transaction
	 */
	public String getDescription() {
		return description;
	}
}
