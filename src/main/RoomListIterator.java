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
		// TODO Auto-generated method stub
		if(hasMore()) {
			return rooms.get(index++);
		}
		return null;
		
	}

	@Override
	public boolean hasMore() {
		// TODO Auto-generated method stub
		return  index< rooms.size();
	}
	
	
	
	
}
