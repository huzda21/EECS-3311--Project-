package main;

public class StaffBookingDecorator extends BookingDecorator {
    public StaffBookingDecorator(Booking booking) {
        super(booking);
    }

    @Override
    public double cost() {
        long minutes = java.time.Duration.between(getStartTime(), getEndTime()).toMinutes();
        double hours = minutes / 60.0;
        return hours * 40.0; // $40/hr for staff
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " [Staff Rate: $40/hr]";
    }
}