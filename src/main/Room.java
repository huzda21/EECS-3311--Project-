package main;

public class Room implements SensorObserver {
	private String roomNumber;
	private String building;
	private int capacity;
	private String status;
	private RoomState state;

	
	

	public Room(String roomNumber, String building, int capacity, String status) {
		super();
		this.roomNumber = roomNumber;
		this.building = building;
		this.capacity = capacity;
		this.status = status;

		switch (status) {
			case "DISABLED":
				this.state = new DisabledState();
				break;
			case "CLOSED":
				this.state = new ClosedState();
				break;
			default:
				this.state = new AvailableState();
				break;
		}
	}
	
	public Room(String roomNumber, String building, int capacity) {
	    this(roomNumber, building, capacity, "AVAILABLE");
	}
	

	public String getBuilding() {
		return building;
	}



	public int getCapacity() {
		return capacity;
	}



	public String getStatus() {
		return state.getStatus();
	}



	public String getRoomNumber() {
		
		return roomNumber;
	}

	public boolean isAvailable() {
	return state.getStatus().equals("AVAILABLE");
	}

	public void setStatus(String status) {
		this.status=status;
	}

	public void setState(RoomState state) {
		this.state = state;
		this.status = state.getStatus();
	}
	public void close() {
    	state.close(this);
	}

	public void enable() {
    	state.enable(this);
	}

	public void disable() {
		state.disable(this);
	}
	
	public boolean isClosed() {
		return state.getStatus().equals("CLOSED");
	}


	@Override
	public void update(Badge badge) {
	    System.out.println("Room " + roomNumber + " notified by sensor.");
	}

}
