package main.java.karora.flightpricetracker.ticket;

import main.java.karora.flightpricetracker.Flight;

/**
 * The RoundTripTicket class contains information about a round trip flight ticket, including the
 * flights and the ticket price.
 * @author kashish
 *
 */
public class RoundTripTicket extends Ticket {
	private Flight outbound;
	private Flight inbound;
	
	/**
	 * Creates a new RoundTripTicket with an outbound and inbound flight and an overall price.
	 * @param outbound		Flight, a Flight object representing the outbound flight
	 * @param inbound		Flight, a Flight object representing the inbound flight
	 * @param price			int, the cost of the ticket
	 * @param bookingLink	String, link to see flight info and purchase ticket
	 */
	public RoundTripTicket(Flight outbound, Flight inbound, int price, String bookingLink) {
		this.outbound = outbound;
		this.inbound = inbound;
		this.price = price;
		this.bookingLink = bookingLink;
	}
	
	/**
	 * Gets the outbound flight of the trip.
	 * @return Flight	A Flight object representing the outbound flight
	 */
	public Flight getOutbound() {
		return this.outbound;
	}
	
	/**
	 * Gets the inbound flight of the trip.
	 * @return Flight	 A Flight object representing the inbound flight
	 */
	public Flight getInbound() {
		return this.inbound;
	}
	
	public String toString() {
		return super.toString(this.outbound.toString() + this.inbound.toString());
	}
}
