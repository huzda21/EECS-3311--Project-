package main;

public abstract class BookingDecorator extends Booking {
    protected Booking decoratedBooking;

    public BookingDecorator(Booking booking) {
        super(
            booking.getBookingId(), 
            booking.getRoom(), 
            booking.getStartTime(), 
            booking.getEndTime(), 
            booking.getStatus(), 
            booking.getDeposit(), 
            booking.getTotal(), 
            booking.getCheckInTime(), 
            booking.getBookedBy()
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
}
