package main;

public class CreditDebit extends Payment{
	private String cardNumber;
	private int cvc;
	private String expiry;

	public CreditDebit(double amount,String cardNumber, int cvc,String expiry) {
		super(amount);
		this.cardNumber=cardNumber;
		this.cvc=cvc;
		this.expiry=expiry;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void pay() {
		// TODO Auto-generated method stub
		System.out.println("Card ending in "+cardNumber.substring(cardNumber.length()-4)+"has been charged "+amount);
	}

	@Override
	public void refund() {
		// TODO Auto-generated method stub
		System.out.println("Card ending in "+cardNumber.substring(cardNumber.length()-4)+"has been refunded "+amount);

	}
	
	
	
}
