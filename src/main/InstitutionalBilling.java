package main;

public class InstitutionalBilling extends Payment{
	
	
	private String empNumber;
	
	public InstitutionalBilling(double amount,String empNumber) {
		super(amount);
		this.empNumber=empNumber;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void pay() {
		// TODO Auto-generated method stub
		System.out.println("Employee"+empNumber+"has been charged "+amount);

	}

	@Override
	public void refund() {
		// TODO Auto-generated method stub
		System.out.println("Employee"+empNumber+"has been refunded "+amount);

	}

}
