package main;

public class CateringDecorator extends BookingDecorator {
    public CateringDecorator(Booking booking) {
        super(booking);
    }

    @Override
    public double calculateTotalCost() {
        return super.calculateTotalCost() + 50.00; // Adds $50 premium
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " + Premium Catering Add-on";
    }
}