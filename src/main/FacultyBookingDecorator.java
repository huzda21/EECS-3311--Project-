package main;

public class FacultyBookingDecorator extends BookingDecorator {
    public FacultyBookingDecorator(Booking booking) {
        super(booking);
    }

    @Override
    public double cost() {
        long minutes = java.time.Duration.between(getStartTime(), getEndTime()).toMinutes();
        double hours = minutes / 60.0;
        return hours * 30.0; // $30/hr for faculty
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " [Faculty Rate: $30/hr]";
    }
}
