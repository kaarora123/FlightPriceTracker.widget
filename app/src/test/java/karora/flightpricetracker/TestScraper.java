package test.java.karora.flightpricetracker;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import main.java.karora.flightpricetracker.NoResultsException;
import main.java.karora.flightpricetracker.Scraper;
import main.java.karora.flightpricetracker.ticket.*;

public class TestScraper {

	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	private ArrayList<Ticket> tickets;
	
	@Test
	public void testOneWayTrip() throws UnknownHostException, IOException, InterruptedException, NoResultsException {
		tickets = Scraper.getTopThreeTickets(1, "https://www.kayak.com/flights/BOS-LAX/2018-04-02?sort=bestflight_a");
		assertTrue(tickets.get(0) instanceof OneWayTicket);
		OneWayTicket ticket = (OneWayTicket)tickets.get(0);
		assertTrue(ticket.getFlight() != null);
	}
	
	@Test
	public void testRoundTrip() throws UnknownHostException, IOException, InterruptedException, NoResultsException {
		tickets  = Scraper.getTopThreeTickets(2, "https://www.kayak.com/flights/JFK-LAX/2018-06-04/2018-06-16?sort=price_a");
		assertTrue(tickets.get(0) instanceof RoundTripTicket);
		RoundTripTicket ticket = (RoundTripTicket)tickets.get(0);
		assertTrue(ticket.getInbound() != null && ticket.getOutbound() != null);
	}
	
	@Test
	public void testMultiCityTrip() throws UnknownHostException, IOException, InterruptedException, NoResultsException {
		try {
			
			ArrayList<Ticket> tickets1 = Scraper.getTopThreeTickets(3, "https://www.kayak.com/flights/BOS-LAX/2018-04-04/LAX-ORD/2018-04-08/ORD-BOS/2018-06-16");
			assertTrue(tickets1.get(0) instanceof MultiCityTicket);
			
			MultiCityTicket ticket1 = (MultiCityTicket) tickets1.get(0);
			assertTrue(ticket1.getFlights() != null);
			
			ArrayList<Ticket> tickets2 = Scraper.getTopThreeTickets(2, "https://www.kayak.com/flights/JFK-LAX/2018-04-04/LAX-ORD/2018-04-16?sort=duration_a");
			assertTrue(tickets2.get(0) instanceof MultiCityTicket);
			
			MultiCityTicket ticket2 = (MultiCityTicket) tickets1.get(0);
			assertTrue(ticket2.getFlights() != null);
			
			
		} catch (NoResultsException e) {
			thrown.expect(NoResultsException.class);
		}
		
	}
	
	@Test
	public void testInvalidIATACode() throws UnknownHostException, IOException, InterruptedException, NoResultsException {
		//test with IATA code = XYZ
		thrown.expect(NoResultsException.class);
		tickets  = Scraper.getTopThreeTickets(2, "https://www.kayak.com/flights/XYZ-JFK/2018-06-04/2018-06-16?sort=price_a");
	}
	
}
