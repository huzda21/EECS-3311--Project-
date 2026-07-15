package main;

import java.time.LocalDateTime;

public class Booking {

	Room room;
	private String bookingId;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private String status;
	private double deposit;
	private double total;
	private LocalDateTime checkInTime;
	private Payment payment;
	public Booking(String bookingId,Room room, LocalDateTime startTime, LocalDateTime endTime, String status,double deposit,double total,LocalDateTime checkInTime) {
		super();
		this.bookingId = bookingId;
		this.room=room;
		this.startTime = startTime;
		this.endTime = endTime;
		this.status = status;
		this.deposit = deposit;
		this.total = total;
		this.checkInTime = checkInTime;
	}
	
	public void createBooking() {
		this.status="CONFIRMED";
	}
	
	public void editBooking(LocalDateTime start,LocalDateTime end) {
		this.startTime=start;
		this.endTime=end;
	}
	
	public void cancelBooking() {
		this.status="CANCELLED";
	}
	
	public void extendBooking(LocalDateTime end) {
		if(this.room.isAvailable()) {
			this.endTime=end;
		}
	}
	
	public boolean checkIn(Badge badge) {
		this.checkInTime=LocalDateTime.now();
		this.status="CONFIRMED";
		return true;
	}
	
	public double cost(){
		return total;
	}
	
	public boolean depositBack(){
        if(checkInTime!=null) {
            System.out.println("[Booking " +bookingId+ "] Deposit applied to final cost.");
            return true;
        }
        System.out.println("[Booking " +bookingId+ "] Deposit forfeited (no check-in).");
        return false;
    }

	public Room getRoom() {
		return room;
	}

	public String getBookingId() {
		return bookingId;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public String getStatus() {
		return status;
	}

	public double getDeposit() {
		return deposit;
	}

	public double getTotal() {
		return total;
	}

	public LocalDateTime getCheckInTime() {
		return checkInTime;
	}
	
	public Payment getPayment() {
		return payment;
	}
	
	public void setPayment(Payment payment) {
		this.payment=payment;
	}
	
	

}
