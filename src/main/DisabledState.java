package main;

public class DisabledState implements RoomState {

	@Override
	public void enable(Room room) {
		room.setState(new AvailableState());
	}

	@Override
	public void disable(Room room) {
	}

	@Override
	public void close(Room room) {
		room.setState(new ClosedState());
	}

	@Override
	public String getStatus() {
		return "DISABLED";
	}
}