package com.abc;

import java.util.ArrayList;
import java.util.List;

public class Bank {

	private List<Customer> customers;

	public Bank() {
		customers = new ArrayList<Customer>();
	}

	/*
	 * Add customer to bank object
	 * 
	 * @param customer object
	 * 
	 * @throws NullPointerException if the customer is null
	 */
	public void addCustomer(Customer customer) {
		if (customer == null) {
			throw new NullPointerException();
		}
		synchronized (customers) {
			customers.add(customer);
		}
	}

	/*
	 * Return customer summary
	 * 
	 * @return user-friendly English string which provides summary information
	 * regarding all aucstomers
	 */
	public String customerSummary() {
		String summary = "Customer Summary";
		synchronized (customers) {
			for (Customer c : customers)
				summary += "\n - " + c.getName() + " (" + format(c.getNumberOfAccounts(), "account") + ")";
		}
		return summary;
	}

	// Make sure correct plural of word is created based on the number passed
	// in:
	// If number passed in is 1 just return the word otherwise add an 's' at the
	// end
	private String format(int number, String word) {
		return number + " " + (number == 1 ? word : word + "s");
	}

	/*
	 * Returns all interest paid across all customers who are part of the bank
	 * 
	 * @return dollar value for total interest paid to all customers
	 */
	public double totalInterestPaid() {
		double total = 0;
		synchronized (customers) {
			for (Customer c : customers)
				total += c.totalInterestEarned();
		}
		return total;
	}

	/*
	 * Returns the name of the first customer for the bank
	 * 
	 * @return name of the first customer
	 */
	public String getFirstCustomer() {
		synchronized (customers) {
			if (customers.isEmpty()) {
				return null;
			}
			return customers.get(0).getName();
		}
	}
}
