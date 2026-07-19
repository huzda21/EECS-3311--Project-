package main;

public class StudentBookingDecorator extends BookingDecorator {
    public StudentBookingDecorator(Booking booking) {
        super(booking);
    }

    @Override
    public double cost() {
        long minutes = java.time.Duration.between(getStartTime(), getEndTime()).toMinutes();
        double hours = minutes / 60.0;
        return hours * 20.0; // $20/hr for students
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " [Student Rate: $20/hr]";
    }
}
