package main;

public class Student  extends User{
	private Long studentNumber;

	public Student(String id, String email, String password, boolean isVerified, Long studentNumber) {
		super(id, email, password, isVerified);
		this.studentNumber = studentNumber;
	}

	@Override
	public double getHourlyRate() {
		// TODO Auto-generated method stub
		return 20.0;
	}
	
	@Override
	public String getRoleName() {
	    return "Student";
	}
}
