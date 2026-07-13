package main;

public class Room {
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

}
