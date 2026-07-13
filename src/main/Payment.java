package main;

import java.time.LocalDateTime;

public abstract class Payment {
	private String paymentId;
	protected double amount;
	private LocalDateTime timePaid;
	private static int counter=0;
	
	public Payment(double amount) {
		counter++;
		this.paymentId=counter+"paid";
		this.amount=amount;
	}
	
	
	
	public String getPaymentId() {
		return paymentId;
	}



	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}



	public double getAmount() {
		return amount;
	}



	public void setAmount(double amount) {
		this.amount = amount;
	}



	public abstract void pay();
	public abstract void reufnd();
	
	
}
