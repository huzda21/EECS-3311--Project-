package main;

import java.util.ArrayList;
import java.util.List;

public class Sensor{
    private String sensorId;
    private Room room;
	private List<SensorObserver> observers = new ArrayList<>();


    public Sensor(String sensorId, Room room) {
        this.sensorId = sensorId;
        this.room = room;
    }

    public void findOccupancy() {
       room.getRoomNumber();
    }

    public void scanBadge(Badge badge) {
        badge.getBadgeId();
    }

    public void sendData() {
        room.getRoomNumber();
    }
	
    public void addObserver(SensorObserver observer) {
        observers.add(observer);
    }
	
}
