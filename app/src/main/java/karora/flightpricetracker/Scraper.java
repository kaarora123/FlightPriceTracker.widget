package main.java.karora.flightpricetracker;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlPage;


import main.java.karora.flightpricetracker.ticket.*;

/**
 * The Scraper class is used to scrape flight data from www.kayak.com.
 * @author kashish
 *
 */
public class Scraper {
	//Ticket attributes
	private static ArrayList<DomText> prices;
	private static ArrayList<DomAttr> bookingLinks;
	
	//Flight attributes
	private static ArrayList<DomText> carriers;
	private static ArrayList<DomText> departTime;
	private static ArrayList<DomText> departCity;
	private static ArrayList<DomText> stops;
	private static ArrayList<DomText> arrivalTime;
	private static ArrayList<DomText> arrivalCity;
	private static ArrayList<DomText> duration;
	private static ArrayList<DomText> meridiem;
	
	
	/**
	 * Scrapes the given URL and uses the data to create three new Ticket objects with the first
	 * three flights found on the HtmlPage.
	 * @param numFlights			int, the number of flights within the ticket(1 for a one way ticket, 2 for round trip, 2+ for multi-city)
	 * @return ArrayList<Ticket>	Three new Ticket objects
	 */
	public static ArrayList<Ticket> getTopThreeTickets(int numFlights, String url) throws IOException, InterruptedException, UnknownHostException, NoResultsException {
		
		scrape(url);
			
		ArrayList<Flight> flights = buildFlights(numFlights);
			
		return createTickets(flights, numFlights);
		
	}
	
	/*
	 * Uses the HtmlPage returned from the getPage method to scrape data from the given
	 * URL. The data are stored in ArrayLists.
	 * @param numFlights			int, the number of flights within the ticket(1 for a one way ticket, 2 for round trip, 2+ for multi-city)
	 * @param url					String, the url to get the data from
	 * @return Tickets array		An array of Tickets objects 
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws UnknownHostException
	 * @throws NoResultsException
	 */
	private static void scrape(String url) throws IOException, InterruptedException, UnknownHostException, NoResultsException {
		try {
			HtmlPage page = (HtmlPage) getPage(url);
			
			prices = (ArrayList<DomText>) (Object) page.getByXPath("//div[contains(@class, 'above-button')]//a//span[contains(@class, 'price')]/text()");
			if(prices.isEmpty()) {
				throw new NoResultsException();
			}
						
			
			bookingLinks = (ArrayList<DomAttr>) (Object) page.getByXPath("//a[contains(@class, 'booking-link')]//@href");
			carriers = (ArrayList<DomText>) (Object) page.getByXPath("//ol[contains(@class, 'flights')]" + getXPathStringDiv("times", "bottom"));
			departTime = (ArrayList<DomText>) (Object) page.getByXPath(getXPathStringSpan("depart-time"));
			departCity = (ArrayList<DomText>) (Object) page.getByXPath("//div[contains(@class, 'duration')]//div[contains(@class, 'bottom')]/span[1]/text()");
			stops = (ArrayList<DomText>) (Object) page.getByXPath(getXPathStringDiv("stops", "top"));
			arrivalTime = (ArrayList<DomText>) (Object) page.getByXPath(getXPathStringSpan("arrival-time"));
			arrivalCity = (ArrayList<DomText>) (Object) page.getByXPath("//div[contains(@class, 'duration')]//div[contains(@class, 'bottom')]/span[3]/text()");
			duration = (ArrayList<DomText>) (Object) page.getByXPath(getXPathStringDiv("duration", "top"));
			meridiem = (ArrayList<DomText>) (Object) page.getByXPath(getXPathStringSpan("time-meridiem"));
			
			//arrivalCity ArrayList has some extra spaces due to the way the text was formatted in HTML
			int i = 0;
			while (arrivalCity.size() != departCity.size()) {
				if(arrivalCity.get(i).getLength() == 1){
					arrivalCity.remove(i);
				} else {
					i++;
				}
			}
						
		} catch (ClassCastException e) {
			/*If the 'page' variable cannot be converted to an HtmlPage, the given url searched
			 for an invalid route */
			throw new NoResultsException();
		}
	}
	
	/*
	 * Gets an HtmlPage with loaded JavaScript for the given URL.
	 * @param url			String, the url to get the HtmlPage for
	 * @return HtmlPage		Page loaded with JavaScript	
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws UnknownHostException
	 */
	private static HtmlPage getPage(String url) throws IOException, InterruptedException, UnknownHostException {
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		WebClient client = new WebClient(BrowserVersion.CHROME);
		client.getOptions().setThrowExceptionOnScriptError(false);
		client.getOptions().setThrowExceptionOnFailingStatusCode(false);
		client.getOptions().setJavaScriptEnabled(true);
		client.getOptions().setCssEnabled(false);
        client.getCookieManager().clearCookies();
		client.getCookieManager().setCookiesEnabled(true);
		client.addRequestHeader("Referer", url);
	
	
							
		HtmlPage page = client.getPage(url);
		
		
		/* this is causing a cookie rejected error */
//		int tries = 3;
//			
//		while (tries > 0) {
//			client.waitForBackgroundJavaScript(1000);
//				
//			String pageXML = page.asXml();
//			if(pageXML.contains("green complete")) break;
//					
//			synchronized (page) {
//				page.wait(500);
//			}
//			
//				
//			tries--;
//		}
			
		return page;

	
	}

	
	/*
	 * Builds numFlights*3 Flight objects with the first numFlights*3 elements in the Flight attributes
	 * ArrayLists because we only want the top 3 tickets found.
	 * @param numFlights			int, the number of flights in a single ticket
	 * @return ArrayList<Flight>
	 */
	private static ArrayList<Flight> buildFlights(int numFlights) {
		ArrayList<Flight> flights = new ArrayList<Flight>();
		
		int meridiemIndex = 0;
		
		for(int i = 0; i < numFlights*3; i++) {
			if(i > carriers.size()){
				break;
			}
			
			int numStops = stops.get(i).asText().equals("nonstop") ? 0 : stops.get(i).asText().split(",").length;
			
			Flight flight = new Flight.FlightBuilder().airline(carriers.get(i).asText()).startCity(departCity.get(i).asText())
										.destination(arrivalCity.get(i).asText()).numStops(numStops).duration(duration.get(i).asText())
										.time(departTime.get(i).asText() + " " + meridiem.get(meridiemIndex++).asText(), arrivalTime.get(i).asText() + " " + meridiem.get(meridiemIndex++).asText()).build();
			flights.add(flight);
			
		}
		
		return flights;
		
	}
	
	/*
	 * Creates three Ticket objects (if there are enough flights found) from an ArrayList of 
	 * Flight objects.
	 * @param flights				ArrayList<Flight>
	 * @param numFlights			int, the number of flights in a single ticket
	 * @return ArrayList<Ticket>	Three ticket objects
	 */
	private static ArrayList<Ticket> createTickets(ArrayList<Flight> flights, int numFlights) {
		
		ArrayList<Ticket> tickets = new ArrayList<Ticket>();
		
		for(int i = 0, j = 0; i < flights.size(); i+=numFlights, j++) {			
			int price = getPriceFromString(prices.get(j).asText());
			
			String bookingLink = bookingLinks.get(j).getValue();
				
			switch(numFlights) {
			
			case 1:
				tickets.add(new OneWayTicket(flights.get(i), price, bookingLink));
				break;
			case 2:
				//if the start city of the first flight and the destination of the second flight are the same
				boolean roundtrip = flights.get(i).getStartCity().equals(flights.get(i+1).getDestination());
				if(roundtrip) {
					tickets.add(new RoundTripTicket(flights.get(i), flights.get(i+1), price, bookingLink));
					break;
				}
				
			default:
				tickets.add(new MultiCityTicket(flights.subList(i, i+=numFlights), price, bookingLink));
			
			}
			
		}
		
		Collections.sort(tickets);
		return tickets;
		
	}
	
	
	private static int getPriceFromString(String priceString) {
		int price;
		
		try {
			price = (int)NumberFormat.getCurrencyInstance().parse(priceString).intValue();
		} catch (ParseException e) {
			price = -1;
		}
		
		return price;
	}
	
	
	private static String getXPathStringSpan(String className) {
		return "//span[contains(@class, '" + className + "')]/text()";
	}
	
	private static String getXPathStringDiv(String parentDivClass, String childDivClass) {
		return "//div[contains(@class, '" + parentDivClass + "')]//div[contains(@class, '" + childDivClass + "')]/text()";
	}
}
