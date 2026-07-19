package main;

public interface RoomState {

    void enable(Room room);

    void disable(Room room);

    void close(Room room);

    String getStatus();
}