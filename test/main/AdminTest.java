package main;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

class AdminTest {
	private Admin admin;
	private RoomList rooms;
	private Room room;

	@BeforeEach
	void setup() {
		admin=new Admin("0001","admin21@gmail.com","captainAmerica@1987","AD12345");
		rooms=new RoomList();
		room=new Room("105","LSB",150,"AVAILABLE");
	}

	@Test
	void testGetHourlyRate() {
		assertEquals(0,admin.getHourlyRate());
	}

	@Test
	void testGetRoleName() {
		assertEquals("Admin",admin.getRoleName());
	}

	@Test
	void testAddRoom() {
		admin.addRoom(room);
		boolean found=false;
		RoomIterator i=admin.getRoomIterator();
		while(i.hasMore()) {
			if(i.getNext().equals(room)) {
				found=true;
			}
		}
		assertTrue(found);
	}
	
	@Test
	void testCloseRoom() {
		admin.closeRoom(room);
		assertTrue(room.isClosed());
		assertEquals("CLOSED",room.getStatus());
		
	}
	
	@Test
	void testEnableRoom() {
		admin.disableRoom(room);
		admin.enableRoom(room);
		assertTrue(room.isAvailable());
		assertEquals("AVAILABLE",room.getStatus());
	}
	
	@Test
	void testDisableRoom() {
		admin.disableRoom(room);
		assertEquals("DISABLED",room.getStatus());
		assertFalse(room.isAvailable());
	}
	
	@Test
	void testGetAdminId(){
		assertEquals("AD12345",admin.getAdminId());
	}
	
	@Test
	void testEmptyIterator() {
		RoomIterator i=admin.getRoomIterator();
		assertFalse(i.hasMore());
	}
	
	@Test
	void testAddMultipleRoom() {
		Room room2=new Room("C","VH",300,"AVAILABLE");
		admin.addRoom(room);
		admin.addRoom(room2);		
		int count=0;
		RoomIterator i=admin.getRoomIterator();
		while(i.hasMore()) {
			i.getNext();
			count++;
		}
		assertEquals(2,count);
	}
	
	@Test
	void testAttributes() {
		assertEquals("0001",admin.getId());
		assertEquals("admin21@gmail.com",admin.getEmail());
	}
	
	
	
	

}

