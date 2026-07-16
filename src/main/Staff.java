package main;

public class Staff extends User{
	private Long staffNumber;

	public Staff(String id, String email, String password, boolean isVerified, Long staffNumber) {
		super(id, email, password, isVerified);
		this.staffNumber = staffNumber;
	}

	@Override
	public double getHourlyRate() {
		// TODO Auto-generated method stub
		return 40.0;
	}
	
	@Override
	public String getRoleName() {
	    return "Staff";
	}
	
	
}
