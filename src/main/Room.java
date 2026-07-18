package main;

public class Room implements SensorObserver{
	private String roomNumber;
	private String building;
	private int capacity;
	private String status;


	
	

	public Room(String roomNumber, String building, int capacity, String status) {
		super();
		this.roomNumber = roomNumber;
		this.building = building;
		this.capacity = capacity;
		this.status = status;

	}
	
	

	public String getBuilding() {
		return building;
	}



	public int getCapacity() {
		return capacity;
	}



	public String getStatus() {
		return status;
	}



	public String getRoomNumber() {
		// TODO Auto-generated method stub
		return roomNumber;
	}

	public boolean isAvailable() {
		// TODO Auto-generated method stub
		return status.equals("AVAILABLE");
	}

	public void close() {
		// TODO Auto-generated method stub
		this.status = "CLOSED";
		
	}

	public void enable() {
		// TODO Auto-generated method stub
		this.status="AVAILABLE";
		
	}
	
	public void disable() {
		this.status="DISABLE";
	}
	
	public boolean isClosed() {
		return status.equals("CLOSED");
	}
	//Incorporated with sensor [REQ5]
	@Override
	//If there are no active bookings currently, you can safely check into the room
    public void update(Badge badge) {
        Booking current = checkBooking();
        if (current != null) {
            current.checkIn(badge);
        }
    }
	
	//checking which booking matches the room that got scanned
    private Booking checkBooking() {
        for (Booking b : AppData.bookings) {
            if (b.getRoom() == this && b.getStatus().equals("CONFIRMED") && b.getCheckInTime() == null) {
                return b;
            }
        }
        return null;
    }

}
