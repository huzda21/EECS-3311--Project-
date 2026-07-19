package main;

public class PartnerBookingDecorator extends BookingDecorator {
    public PartnerBookingDecorator(Booking booking) {
        super(booking);
    }

    @Override
    public double cost() {
        long minutes = java.time.Duration.between(getStartTime(), getEndTime()).toMinutes();
        double hours = minutes / 60.0;
        return hours * 50.0; // $50/hr for partners
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " [Partner Rate: $50/hr]";
    }
}