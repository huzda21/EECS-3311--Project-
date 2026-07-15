package main;

import java.time.*;
public abstract class User {
	private String id;
	private String email;
	private String password;
	protected boolean isVerified;
	private static int counter=0;
	public User(String id, String email, String password, boolean isVerified) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.isVerified = isVerified;
	}
	
	
	
	
	public String getId() {
		return id;
	}




	public String getEmail() {
		return email;
	}




	public String getPassword() {
		return password;
	}




	public boolean isVerified() {
		return isVerified;
	}




	public boolean login(String passowrd) {
		return this.password.equals(passowrd);
	}
	
	public void logout() {
		System.out.println(email+" is logged out");
	}
	
	public abstract double getHourlyRate();
	
	
	public Booking booking(Room room, LocalDateTime start, LocalDateTime end) {
		if(!isVerified) {
			throw new IllegalStateException("Account is not verified");
		}
		
		if(!room.isAvailable()) {
			throw new IllegalStateException("The room you are looking for isn't available");	
		}
		double bookingDuration=  Duration.between(start, end).toHours();
		double rate=getHourlyRate()*bookingDuration;
		double deposit=getHourlyRate();
		
		counter++;
		String bookId=room.getRoomNumber()+""+counter;
		Booking newBooking=new Booking(bookId, start, end, "PENDING",deposit,rate, start);
		
		return newBooking;
	}




	protected void setVerified(boolean b) {
		this.isVerified=b;
	}



	public abstract String  getRoleName();
	
	
	
}
