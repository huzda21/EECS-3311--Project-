package main;

public class ProjectorDecorator extends BookingDecorator {
    
    public ProjectorDecorator(Booking booking) {
        super(booking);
    }

    /**
     * Intercepts the original cost flow and polymorphically 
     * adds a dynamic premium fee of $15.00 for the hardware asset.
     */
    @Override
    public double cost() {
        return super.cost() + 15.00;
    }

    /**
     * Intercepts the original description track to append 
     * the active structural metadata for your report's UI verification.
     */
    @Override
    public String getDescription() {
        return super.getDescription() + " + HD Projector Add-on";
    }
}
