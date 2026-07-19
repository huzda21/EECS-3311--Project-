package main;

public class AvailableState implements RoomState {

	@Override
	public void enable(Room room) {
	}

	@Override
	public void disable(Room room) {
		room.setState(new DisabledState());
	}

	@Override
	public void close(Room room) {
		room.setState(new ClosedState());
	}

	@Override
	public String getStatus() {
		return "AVAILABLE";
	}
}