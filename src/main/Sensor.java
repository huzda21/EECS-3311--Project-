//Incorporate observer pattern here
package main;

public class Sensor implements SensorObserver {
    private String sensorId;
    private Room room;

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

	
}
