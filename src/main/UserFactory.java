package main;
 
import java.util.HashMap;
import java.util.Map;
 
public class UserFactory {
    // Creator
    private static abstract class UserCreator {
        abstract User createUser(String id, String email, String password,boolean isVerified, Long idNumber);
 
        User register(String id, String email, String password,boolean isVerified, Long idNumber) {
            User user = createUser(id, email, password, isVerified, idNumber);
            System.out.println("Registered new " + user.getClass().getSimpleName() + ": " + email);
            return user;
        }
    } 
    // Concrete Creators
    private static class StudentCreator extends UserCreator {
        User createUser(String id, String email, String password, boolean isVerified, Long idNumber) {
            return new Student(id, email, password, isVerified, idNumber);
        }
    }
    private static class FacultyCreator extends UserCreator {
        User createUser(String id, String email, String password, boolean isVerified, Long idNumber) {
            return new Faculty(id, email, password, isVerified,idNumber);
        }
    }
    private static class StaffCreator extends UserCreator {
        User createUser(String id, String email, String password, boolean isVerified, Long idNumber) {
            return new Staff(id,email,password,isVerified,idNumber);
        }
    }
    private static class PartnerCreator extends UserCreator {
        User createUser(String id, String email, String password, boolean isVerified, Long idNumber) {
            return new Partner(id, email, password, isVerified, idNumber);
        }
    }
    // hashmap lookup
    private static final Map<String, UserCreator> creators = new HashMap<>();
    static {
        creators.put("STUDENT", new StudentCreator());
        creators.put("FACULTY", new FacultyCreator());
        creators.put("STAFF", new StaffCreator());
        creators.put("PARTNER", new PartnerCreator());
    }
    public static User createUser(String accountType, String id, String email,String password, boolean isVerified, Long idNumber) {
        UserCreator creator=creators.get(accountType.toUpperCase());
        if (creator ==null) {
            throw new IllegalArgumentException("Unsupported account type: " + accountType);
        }
        return creator.register(id, email, password, isVerified, idNumber);
    }
}
