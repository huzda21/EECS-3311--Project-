package main;

public abstract class BookingDecorator extends Booking {
    protected Booking decoratedBooking;

    public BookingDecorator(Booking booking) {
        // Pass dummy values to super() to satisfy the legacy constructor signature safely
        super(booking.getBookingId(), booking.getRoomNumber(), 0); 
        this.decoratedBooking = booking;
    }

    @Override
    public double calculateTotalCost() {
        return decoratedBooking.calculateTotalCost();
    }

    @Override
    public String getDescription() {
        return decoratedBooking.getDescription();
    }
}