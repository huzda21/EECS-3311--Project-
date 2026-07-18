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
	private User bookedBy;
	public Booking(String bookingId,Room room, LocalDateTime startTime, LocalDateTime endTime, String status,double deposit,double total,LocalDateTime checkInTime,User bookedBy) {
		super();
		this.bookingId = bookingId;
		this.room=room;
		this.startTime = startTime;
		this.endTime = endTime;
		this.status = status;
		this.deposit = deposit;
		this.total = total;
		this.checkInTime = checkInTime;
		this.bookedBy=bookedBy;
	}
	
	public void createBooking() {
		this.status="CONFIRMED";
	}
	
	
	public boolean editBooking(LocalDateTime start, LocalDateTime end) {
	    if(LocalDateTime.now().isBefore(startTime)) {
	        this.startTime= start;
	        this.endTime= end;
	        return true;
	    }
	    return false;
	}	

	
	public boolean cancelBooking() {
	    if(LocalDateTime.now().isBefore(startTime)) {
	        status ="CANCELLED";
	        room.setStatus("AVAILABLE");
	        return true;
	    }
	    return false;
	}

	
	public boolean extendBooking(LocalDateTime updatedEnd) {
		if(updatedEnd==null||!updatedEnd.isAfter(endTime)||!LocalDateTime.now().isBefore(endTime)||!room.isAvailable()||!roomExtension(room, startTime, updatedEnd, this.bookingId)) {
			return false;
		}
		double rate=bookedBy.getHourlyRate();
		double extension=java.time.Duration.between(endTime, updatedEnd).toMinutes()/60.0;
		this.total+=rate *extension;
		this.endTime = updatedEnd;
		return true;
	}
	
	public static boolean roomExtension(Room room2, LocalDateTime startTime2, LocalDateTime end, String bookingId2) {
		for(Booking b:AppData.bookings) {
			if(b.getRoom()!=room2) continue;
			if(bookingId2!=null&&b.getBookingId().equals(bookingId2)) continue;
			if("CANCELLED".equals(b.getStatus())) continue;
			boolean overlaps=startTime2.isBefore(b.getEndTime())&&b.getStartTime().isBefore(end);
			if (overlaps) {
				return false;
			}
		}
		return true;
	}

	public boolean checkIn(Badge badge) {
		this.checkInTime=LocalDateTime.now();
		this.status="CONFIRMED";
		return true;
	}
	
	public double cost(){
		return total;
	}
	


	public boolean depositBack() {

	    if (checkInTime!=null&&!checkInTime.isAfter(startTime.plusMinutes(30))) {
	    	total-=deposit;
	        return true;
	    }

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
	public User getBookedBy() {
		return this.bookedBy;
	}
	
	

}
