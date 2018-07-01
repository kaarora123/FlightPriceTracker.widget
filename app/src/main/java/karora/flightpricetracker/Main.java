package main.java.karora.flightpricetracker;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import main.java.karora.flightpricetracker.ticket.Ticket;

public class Main {

	public static void main(String[] args) {
		System.setProperty("apple.awt.UIElement", "true");
		
		if(args.length < 4) {
			printErrorMessage("Please provide all required arguments.");
			System.exit(0);
		}
		
		if(!validateArgs(args)) 
			System.exit(0);	
		
		try {
			ArrayList<Ticket> tickets = Scraper.getTopThreeTickets(getNumFlights(args), getURL(args));
			String html = createTripHeading(args) + "<hr id = 'headingSep'>";
			html += "<div class = 'tickets'>";
			for (Ticket ticket: tickets) {
				html += ticket.toString() + "<hr>";
			}
			html += "</div>";
			
			System.out.print(html);
			
		} catch (NoResultsException e) {
			System.out.println("<div class = 'error'>Sorry, no results found for your search :(</div>");
		} catch(UnknownHostException e) {
			printErrorMessage("Could not connect to kayak.com.");
		} catch (IOException|InterruptedException e) {
			printErrorMessage(e.getMessage());
		} finally {
			System.exit(0);
		}
			
	}
	
	/*
	 * Creates the url for the given trip.
	 * @param args		String[]
	 * @return String 	url
	 */
	private static String getURL(String[] args) {
		String url = "https://www.kayak.com/flights";
		
		for(int i = 1; i < args.length - 1; i++) {
			if(args[i].matches(".*\\d+.*")) {
				url += "/" + formatDate(args[i]);
			} else {
				url += "/" + args[i].toUpperCase();
			}
		}
		
		url += "?sort=" + args[args.length - 1] + "_a";
		return url;
	}
	
	/*
	 * Formats the given date string into the required format for the URL.
	 * @param dateString
	 * @return String
	 */
	private static String formatDate(String dateString) {
		Date date = createDateObject(dateString);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(date);
	}
	
	/*
	 * Returns the number of flights in the trip.
	 * @param args		String[]
	 * @return int		
	 */
	private static int getNumFlights(String[] args) {
		int numFlights = 0;
		
		switch(args[0].toLowerCase()) {
		
		case "oneway":
			numFlights = 1;
			break;
		case "roundtrip":
			numFlights = 2;
			break;
		case "multicity":
			numFlights = (args.length - 2) / 2;
			break;
		}
		
		return numFlights;
	}
	
	/*
	 * Creates an HTML div element with information about the given trip, including
	 * the route/s and date/s.
	 * @param args		String[]
	 * @return String	div element
	 */
	private static String createTripHeading(String[] args) {
		String divElem = "<div class = 'trip'>";
		String startDate = args[2];
		String endDate = args[args.length - 2];
		
		divElem += createRouteHeading(args);
		divElem += "<div id = 'separator'>✈</div>";
		
		switch(args[0].toLowerCase()) {
		
		case "oneway":
			divElem += "<div id = 'date'>" + startDate.substring(0, startDate.length()-3) + "</div>";
			break;
		default:
			divElem += "<div id = 'date'>" + startDate + " - " + endDate + "</div>";
		}
		
		return divElem + "</div>";
	}

	/*
	 * Formats the given route/s for the trip heading.
	 * @param args		String[]
	 * @return String	div element
	 */
	private static String createRouteHeading(String[] args) {
		String divElem = "<div id='route'>";
		switch(args[0].toLowerCase()) {
		
		case "oneway":
			divElem += formatRoute(args[1], " → ");
			break;
		case "roundtrip":
			divElem += formatRoute(args[1], " ⇄ ");
			break;
		case "multicity":
			for(int i = 1; i < args.length-2; i+=2) {
				
				divElem += formatRoute(args[i], " → ") + (i != args.length-3 ? ", " : "" );
			}
			break;
		}
		
		return divElem + "</div>";
	}
	
	private static String formatRoute(String route, String separator) {
		return route.substring(0, 3).toUpperCase() + separator + route.substring(4).toUpperCase();
	}
	
	
	/**
	 * Validates the arguments given in the main method. Makes sure that all arguments
	 * were given and given correctly.
	 * @param args			String[]
	 * @return boolean		true if the arguments are valid, false otherwise
	 */
	public static boolean validateArgs(String[] args) {
		String ticketType = args[0];
		String sortType = args[args.length -1];
		
		boolean validTicketType = ticketType.equalsIgnoreCase("oneway") || ticketType.equalsIgnoreCase("roundtrip") || ticketType.equalsIgnoreCase("multicity");
		boolean validSortType = sortType.equalsIgnoreCase("bestflight") || sortType.equalsIgnoreCase("price") || sortType.equalsIgnoreCase("duration");
		
		boolean result = validTicketType && validSortType && validateFlights(args);
		
		if(!validTicketType) 
			printErrorMessage("Invalid ticket type.");
		else if (!validSortType) 
			printErrorMessage("Invalid sort type.");
		
		return result;
		
	}
	
	/*
	 * Validates the flights given based on the ticketType.
	 * @param args		String[]
	 * @return boolean	true if the flights are valid, false otherwise
	 */
	private static boolean validateFlights(String[] args) {
		boolean result = true;
		
		switch(args[0].toLowerCase()) {
			
		case "oneway": 
			result = validateOneWay(args);
			break;
		case "roundtrip":
			result = validateRoundTrip(args);
			break;
		case "multicity":
			result = validateMultiCity(args);
			break;
		}
		
		return result;
		
	}
	
	
	/*
	 * Validates one way flight args. Checks if all the required
	 * arguments were given and that the given route and date are valid.
	 * @param args		String[]
	 * @return boolean 	true if the flight is valid, false otherwise
	 */
	private static boolean validateOneWay(String[] args) {
		boolean result;
		
		if(args.length != 4) {
			printErrorMessage("Four arguments required for one-way ticket but " + args.length + " were given.");
			result = false;
		} else {
			//because there is only one date in a one-way trip, use the same date for both args
			result = validateRoute(args[1]) && validateDates(args[2], args[2]);
		}
		
		return result;
		
	}
	
	/*
	 * Validates round trip flight args. Checks if all the required
	 * arguments were given and that the given route and dates are valid.
	 * @param args			String[]
	 * @return boolean		true if the flights are valid, false otherwise
	 */
	private static boolean validateRoundTrip(String[] args) {
		boolean result;
		
		if(args.length != 5) {
			printErrorMessage("Please make sure you have given two dates for your round trip ticket.");
			result = false;
			
		} else {
			result = validateRoute(args[1]) && validateDates(args[2], args[3]);
		}
		
		return result;
	}
	
	/*
	 * Validates multi-city flight args. Checks if the number of given routes
	 * and dates are equal and valid.
	 * @param args		String[]
	 * @return boolean	true if the flights are valid, false otherwise
	 */
	private static boolean validateMultiCity(String[] args) {
		boolean result = true;
		
		//subtract 2 from length to ignore ticketType and sortType args
		if((args.length - 2) % 2 != 0) {
			printErrorMessage("Number of flights and dates are not equal.");
			result = false;
		} else {
			String previousDate = args[2];
			for(int i = 1; i < args.length-2; i+=2) {
				if(!validateRoute(args[i]) || !validateDates(previousDate, args[i+1])) {
					result = false;
					break;
				}
				previousDate = args[i+1];
				
			}
		}
		
		return result;
	}

	
	/*
	 * Checks if the first given date is before the second given date. Also
	 * checks if dates are within 1 year of the current date.
	 * @param d1			String, first date
	 * @param d2			String, second date
	 * @return boolean		true if the dates are valid, false otherwise
	 */
	private static boolean validateDates(String d1, String d2) {
		Date date1 = createDateObject(d1);
		Date date2 = createDateObject(d2);
		boolean result;
		
		if(date2.before(date1)) {
			printErrorMessage("Flight leaves before you arrive. Please adjust dates.");
			result = false;
		} else {
			Calendar cal = Calendar.getInstance();
			Date today = cal.getTime();
			cal.add(Calendar.YEAR, 1);
			Date nextYear = cal.getTime();
			
			boolean validDate = date1.after(today) && date2.after(today);
			boolean withinOneYear = date1.before(nextYear) && date2.before(nextYear);
			
			result = validDate && withinOneYear;
			
			if(!validDate) 
				printErrorMessage("Invalid date.");
			else if(!withinOneYear) 
				printErrorMessage("Your trip must start and end within 1 year of today.");
		}
		
		return result;
	}
	
	/*
	 * Creates a Date object with the given string.
	 * @param dateString		String (format: M/d/yy)
	 * @return Date
	 */
	private static Date createDateObject(String dateString) {
		Date date = new Date();
		try {
			SimpleDateFormat dt = new SimpleDateFormat("M/d/yy");
			date = dt.parse(dateString);
		} catch (ParseException e) {
			printErrorMessage("Incorrect date format.");
			System.exit(0);
		}
		
		return date;

	}
	
	/*
	 * Checks if either of the airport codes have special chars or a length not equal to 3. 
	 * Also checks if both airport codes are the same. 
	 * @param route		String (format: code1-code2)
	 * @return boolean
	 */
	private static boolean validateRoute(String route) {
		boolean result = true;
		String[] airports = route.split("-");		
		String regex = "[a-zA-Z]*";
		
		if(airports.length != 2) {
			result = false;
		
		} else {
			
			boolean allLetters = airports[0].matches(regex) && airports[1].matches(regex);
			boolean IATACodes = airports[0].length() == 3 && airports[1].length() == 3;
			boolean diffAirports = !airports[0].equalsIgnoreCase(airports[1]);
			
			result = allLetters && IATACodes && diffAirports;
			
		}
				
		if(!result) 
			printErrorMessage("Invalid route.");
		
		return result;
	}
	
	
	private static void printErrorMessage(String message) {
		System.out.print("<div class = 'error'>Error: " + message + "</div>");
		
	}
}
