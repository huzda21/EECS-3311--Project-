package main;

public class Partner extends User {
	private Long partnernumber;

	public Partner(String id, String email, String password, boolean isVerified, Long partnernumber) {
		super(id, email, password, isVerified);
		this.partnernumber = partnernumber;
	}

	@Override
	public double getHourlyRate() {
		// TODO Auto-generated method stub
		return 50.0;
	}
	
	@Override
	public String getRoleName() {
	    return "Partner";
	}
	
}
