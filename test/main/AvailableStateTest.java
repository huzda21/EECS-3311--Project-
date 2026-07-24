package main;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

public class AvailableStateTest {
	private Room room;
	private AvailableState availableState;
	
	@BeforeEach
	void setup() {
		room=new Room("105","LSB",150,"AVAILABLE");
		availableState=new AvailableState();
	}
	
	@Test
	void testGetStatus() {
		assertEquals("AVAILABLE",availableState.getStatus());
	}
	
	@Test
	void testEnable() {
		availableState.enable(room);
		assertEquals("AVAILABLE",room.getStatus());
	}
	
	@Test
	void testChangeStatus() {
		availableState.disable(room);
		assertEquals("DISABLED",room.getStatus());
	}
	
	@Test
	void testStatusClosed() {
		availableState.close(room);
		assertEquals("CLOSED",room.getStatus());
	}
	
	@Test
	void testCorrectDisabled() {
		availableState.disable(room);
		assertTrue(room.getStatus().equals("DISABLED"));
		assertFalse(room.isAvailable());
	}
	
	@Test
	void testDefaultState() {
		Room room2=new Room("1002","LAS",40);
		assertEquals("AVAILABLE",room2.getStatus());
	}
	
	@Test
	void testSameBehaviour() {
		AvailableState a2=new AvailableState();
		assertEquals(availableState.getStatus(),a2.getStatus());
	}
	
	@Test
	void testDisableToClose() {
		availableState.disable(room);	
		room.close();
		assertEquals("CLOSED",room.getStatus());
	}
	
	@Test
	void testInitial() {
		assertTrue(room.isAvailable());
	}
	
	@Test
	void testScope() { //Testing to see if other attributes remain the same
		availableState.disable(room);
		assertEquals("105",room.getRoomNumber());
		assertEquals("LSB",room.getBuilding());
		assertEquals(150,room.getCapacity());
	}
	
	
}
