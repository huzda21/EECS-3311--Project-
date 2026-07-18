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
		
		if(!end.isAfter(start)) {
			throw new IllegalStateException("tart time must be before end time");	

		}
		
		if(!Booking.roomExtension(room,start,end,null)) {
			throw new IllegalStateException("This room is already booked for part of that time range");
		}
		double bookingDuration=  Duration.between(start, end).toMinutes()/60.0;
		double rate=getHourlyRate()*bookingDuration;
		double deposit=getHourlyRate();
		
		counter++;
		String bookId=room.getRoomNumber()+""+counter;
		Booking newBooking=new Booking(bookId, room, start, end, "PENDING",deposit,rate, start,this);
		room.setStatus("BOOKED");
		return newBooking;
	}




	protected void setVerified(boolean b) {
		this.isVerified=b;
	}



	public abstract String  getRoleName();
	
	
	
}
