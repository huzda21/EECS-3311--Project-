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
	

	public Booking(String bookingId,Room room, LocalDateTime startTime, LocalDateTime endTime, String status,double deposit,double total,LocalDateTime checkInTime, User bookedBy) {
		super();
		this.bookingId = bookingId;
		this.room=room;
		this.startTime = startTime;
		this.endTime = endTime;
		this.status = status;
		this.deposit = deposit;
		this.total = total;
		this.checkInTime = checkInTime;
		this.bookedBy = bookedBy;
	}
	
	public void createBooking() {
	    this.status = "BOOKED";
	}
	
	
	public boolean editBooking(LocalDateTime start, LocalDateTime end) {
		
		if (!Booking.roomAvailable(room, start, end)) {
		    return false;
		}
		
	    if(LocalDateTime.now().isBefore(startTime)) {
	        this.startTime = start;
	        this.endTime = end;
	        return true;
	    }
	    return false;
	}	

	
	public boolean cancelBooking() {
	    if(LocalDateTime.now().isBefore(startTime)) {
	        status = "CANCELLED";
	        return true;
	    }
	    return false;
	}
	
	public boolean extendBooking(LocalDateTime newEnd) {

	    //cant extend if booking already ended
	    if (LocalDateTime.now().isAfter(endTime)) {
	        return false;
	    }

	    if (!newEnd.isAfter(endTime)) {
	        return false;
	    }
	    
	    //check if another booking conflicts
	    for (Booking b : AppData.bookings) {

	        if (b == this)
	            continue;

	        if (b.getRoom() == room &&
	            b.getStartTime().isBefore(newEnd) &&
	            b.getEndTime().isAfter(endTime)) {

	            return false;
	        }
	    }
	   

	    double extraHours =
	            java.time.Duration.between(endTime, newEnd).toMinutes() / 60.0;

	    total += extraHours * deposit;

	    endTime = newEnd;

	    return true;

	}
	

	
	public boolean checkIn(Badge badge) {

	    LocalDateTime now = LocalDateTime.now();

	    //already checked in 
	    if (checkInTime != null) {
	        return false;
	    }
	    
	    //cant check in before the booking starts
	    if (now.isBefore(startTime))
	        return false;

	    //cant check in past 30 mins
	    if (now.isAfter(startTime.plusMinutes(30)))
	        return false;
	    
	    

	    checkInTime = now;
	    status = "IN USE";
	    return true;
	}

	
	public double cost(){
		return total;
	}
	



	public boolean depositBack() {

	    if (checkInTime != null &&
	        !checkInTime.isAfter(startTime.plusMinutes(30))) {

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
	
	//check if room is available
	public static boolean roomAvailable(Room room, LocalDateTime start, LocalDateTime end) {

			for (Booking b : AppData.bookings) {
					if (b.getStatus().equals("CANCELLED"))
						continue;
					if (b.getRoom() == room &&
							start.isBefore(b.getEndTime()) &&
							end.isAfter(b.getStartTime())) {
						return false;
					}
			}
			return true;
	}
	
	public User getBookedBy() {
	    return bookedBy;
	}

	public double cost() {
		return this.total;
	}

	public String getDescription() {
		return "Standard Room Booking (" + (room != null ? room.getRoomNumber() : "N/A") + ")";
	}

}
