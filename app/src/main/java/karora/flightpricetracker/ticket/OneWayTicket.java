package main.java.karora.flightpricetracker.ticket;

import main.java.karora.flightpricetracker.Flight;

/**
 * The OneWayTicket class contains information about a one way flight ticket, including the
 * flight and its price.
 * @author kashish
 *
 */
public class OneWayTicket extends Ticket {
	private Flight flight;
	
	/**
	 * Creates a new OneWayTicket with a Flight object and a price.
	 * @param flight			Flight, a Flight object representing the flight
	 * @param price				int, the cost of the ticket
	 * @param bookingLink		String, link to see flight info and purchase ticket
	 */
	public OneWayTicket(Flight flight, int price, String bookingLink) {
		this.price = price;
		this.flight = flight;
		this.bookingLink = bookingLink;
	}
	
	/**
	 * Gets the flight.
	 * @return Flight	A Flight object representing the flight.
	 */
	public Flight getFlight() {
		return this.flight;
	}
	
	public String toString() {
		return super.toString(flight.toString());
	}

}
