package main;

public class ClosedState implements RoomState {

	@Override
	public void enable(Room room) {
		room.setState(new AvailableState());
	}

	@Override
	public void disable(Room room) {
		room.setState(new DisabledState());
	}

	@Override
	public void close(Room room) {
	}

	@Override
	public String getStatus() {
		return "CLOSED";
	}
}