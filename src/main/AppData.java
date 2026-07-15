import java.util.ArrayList;
import java.util.List;

/**
 * Plain in-memory data holder standing in for the CSV-backed "database"
 * the assignment says is acceptable to simulate persistence. Not a design
 * pattern itself - just wiring so the GUI panels share state.
 */
public class AppData {
    public static List<User> users = new ArrayList<>();
    public static List<Room> rooms = new ArrayList<>();
    public static List<Booking> bookings = new ArrayList<>();
    public static User currentUser = null;

    static {
        rooms.add(new Room("R100", "Bergeron Building", 10, "AVAILABLE"));
        rooms.add(new Room("R200", "Lassonde Building", 25,"AVAILABLE"));
        rooms.add(new Room("R300", "Life Sciences Building", 40,"AVAILABLE"));

        // A couple of seed accounts so login can be demoed immediately
        users.add(new Student("U-S001", "student1@my.yorku.ca", "pass123", true,219001234L));
        users.get(0).setVerified(true);
        users.add(new Faculty("U-F001", "faculty1@yorku.ca", "pass123",true, 50004949001L));
        users.get(1).setVerified(true);
    }
}