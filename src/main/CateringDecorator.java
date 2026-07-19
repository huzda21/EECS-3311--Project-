package main;

public class CateringDecorator extends BookingDecorator {
    
    public CateringDecorator(Booking booking) {
        super(booking);
    }

    /**
     * Intercepts the original cost flow and polymorphically 
     * adds a dynamic premium fee of $50.00 for the catering service.
     */
    @Override
    public double cost() {
        return super.cost() + 50.00;
    }

    /**
     * Intercepts the original description track to append 
     * the catering metadata to the invoice summary.
     */
    @Override
    public String getDescription() {
        return super.getDescription() + " + Premium Catering Add-on";
    }
}
