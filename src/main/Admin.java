package main;
import java.util.*;

public class Admin extends User{
	private String adminId;
	protected RoomList roomList = new RoomList();
	
	public Admin(String id, String email, String password, String adminId) {
		// TODO Auto-generated constructor stub
		super(id, email, password,true);
		this.adminId = adminId;
	}
	
	
	public String getAdminId() {
		return adminId;
	}
	
	public void addRoom(Room room) {
		roomList.addRoom(room);
	}
	
	public void closeRoom(Room room) {
		room.close();
	}
	
	public void enableRoom(Room room) {
		room.enable();
	}
	
	public void disableRoom(Room room) {
		room.disable();
	}
	
	
	public RoomIterator getRoomIterator(){
    	return roomList.createIterator();
}



	@Override
	public double getHourlyRate() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public String getRoleName() {
		// TODO Auto-generated method stub
		return "Admin";
	}


	


	
	

}
