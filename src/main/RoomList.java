package main;
import java.util.ArrayList;
import java.util.List;


public class RoomList implements RoomCollection{
	private List<Room> rooms = new ArrayList<>();
	
	@Override
	public RoomIterator createIterator() {
		return new RoomListIterator(rooms);
	}
	
	public void addRoom(Room room ) {
		rooms.add(room);
		
	}
}
