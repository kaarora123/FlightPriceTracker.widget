package main.java.karora.flightpricetracker.ticket;

import java.util.List;

import main.java.karora.flightpricetracker.Flight;

/**
 * The MultiCityTicket class contains information about about a multi-city plane ticket, including
 * the flights and the overall price.
 * @author kashish
 *
 */
public class MultiCityTicket extends Ticket {
	private List<Flight> flights;
	
	/**
	 * Creates a new MultiCityTicket with an List of Flight objects and an overall price.
	 * @param flights		List of Flight objects
	 * @param price			int, the cost of the ticket
	 * @param bookingLink	String, link to see flight info and purchase ticket
	 */
	public MultiCityTicket(List<Flight> flights, int price, String bookingLink) {
		this.flights = flights;
		this.price = price;
		this.bookingLink = bookingLink;
	}
	
	/**
	 * Gets the flights in the trip.
	 * @return List<Flight>	An ArrayList of Flight objects
	 */
	public List<Flight> getFlights() {
		return this.flights;
	}
	
	public String toString() {
		String flightDivs = "";
		
		for(int i = 0; i < flights.size(); i++) {
			flightDivs += flights.get(i).toString();
		}
		
		return super.toString(flightDivs);
	}
}
