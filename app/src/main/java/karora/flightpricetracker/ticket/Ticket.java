package main.java.karora.flightpricetracker.ticket;

import java.text.NumberFormat;
import java.util.Locale;


/**
 * The ticket class contains information about a plane ticket.
 * @author kashish
 *
 */
 public abstract class Ticket implements Comparable<Ticket> {
	protected int price;
	protected String bookingLink;

	
	/**
	 * Gets the price of the ticket.
	 * @return int		The price of the flight
	 */
	public int getPrice() {
		return this.price;
	}
	
	/**
	 * Sorts the tickets based on prices in ascending order.
	 * @param ticket	Ticket object
	 * @return int		a negative integer, zero, or a positive integer as this
	 * 					ticket is less than, equal to, or greater than the other.
	 */
	public int compareTo(Ticket ticket) {
		return this.price - ticket.price;
	}
	
	/*
	 * Returns a string HTML div element that will be used to display information about the
	 * plane ticket.
	 * @param flightDiv		String HTML div element/s containing information about the flight/s
	 * @return String	
	 */
	protected String toString(String flightDiv) {
		String divElem = "";
		
		//Change Locale if needed
		NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
		formatter.setMaximumFractionDigits(0);
		
		divElem += "<a class = 'ticket' href='https://www.kayak.com" + this.bookingLink + "'>"
					+ "<div class = 'price'>" + (this.price == -1 ? "Info" : formatter.format(this.price))  + "</div>"
					+ flightDiv
				+"</a>";
		
		return divElem;
	}
}
