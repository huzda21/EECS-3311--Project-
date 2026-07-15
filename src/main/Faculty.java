package main;

public class Faculty extends User{
	private Long employementNumber;

	public Faculty(String id, String email, String password, Long employementNumber) {
		super(id, email, password, isVerified);
		this.employementNumber = employementNumber;
	}

	@Override
	public double getHourlyRate() {
		// TODO Auto-generated method stub
		return 30.0;
	}

	@Override
	protected void setVerified(boolean b) {
		// TODO Auto-generated method stub
		
	}
	
	
}
