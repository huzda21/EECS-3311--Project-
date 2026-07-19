package main;

public class ProjectorDecorator extends BookingDecorator {
    public ProjectorDecorator(Booking booking) {
        super(booking);
    }

    @Override
    public double calculateTotalCost() {
        return super.calculateTotalCost() + 15.00; // Adds $15 premium
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " + HD Projector Add-on";
    }
}