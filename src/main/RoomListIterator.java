package main;
import java.util.List;


public class RoomListIterator implements RoomIterator{
    private List<Room> rooms;
    private int index;

    public RoomListIterator(List<Room> rooms) {
        this.rooms = rooms;
        this.index = 0;
        
    }

	
	@Override
	public Room getNext() {
		if(hasMore()) {
			return rooms.get(index++);
		}
		return null;
		
	}

	@Override
	public boolean hasMore() {
		return  index< rooms.size();
	}
	
	
	
	
}
