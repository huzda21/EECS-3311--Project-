package main;

public abstract class BookingDecorator extends Booking {
    protected Booking decoratedBooking;

    public BookingDecorator(Booking booking) {
        // Pass the inner booking's real data straight to the super constructor
        super(
            booking.getBookingId(), 
            booking.getRoom(), 
            booking.getStartTime(), 
            booking.getEndTime(), 
            booking.getStatus(), 
            booking.getDeposit(), 
            booking.getTotal(), 
            booking.getCheckInTime(), 
            booking.getPayment() != null ? null : null // Add matching bookedBy parameter if available
        );
        this.decoratedBooking = booking;
    }

    @Override
    public double cost() {
        return decoratedBooking.cost();
    }

    @Override
    public String getDescription() {
        return decoratedBooking.getDescription();
    }
}
