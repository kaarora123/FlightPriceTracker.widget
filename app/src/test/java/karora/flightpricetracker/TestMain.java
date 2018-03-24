package test.java.karora.flightpricetracker;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.java.karora.flightpricetracker.Main;
/*
 * Tests are accurate at the time they were written (3/24/18).
 */
public class TestMain{
	
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	
	@Before
	public void setUpStream() {
	    System.setOut(new PrintStream(outContent));
	}

	@After
	public void restoreStream() {
	    System.setOut(System.out);
	}
	
	@Test
	public void testInvalidOneWay() throws IOException {
		Main.validateArgs(new String[] {"oneway", "BOS-LAX", "4/2/18", "4/16/18", "price"});
        assertEquals(getErrorDiv("Four arguments required for one-way ticket but 5 were given."), outContent.toString());
	}
	
	@Test
	public void testInvalidRoundTrip() {
		Main.validateArgs(new String[] {"roundtrip", "LAX-JFK", "5/2/18", "bestflight"});
		assertEquals(getErrorDiv("Please make sure you have given two dates for your round trip ticket."), outContent.toString());	
	}
	
	@Test
	public void testInvalidMultiCity() {
		Main.validateArgs(new String[] {"multicity", "JFK-ORD", "ORD-LHR", "4/24/18", "duration"});
		assertEquals(getErrorDiv("Number of flights and dates are not equal."), outContent.toString());
		
	}
	
	@Test
	public void testInvalidTicketType() {
		Main.validateArgs(new String[] {"one", "JFK-ORD","4/24/18", "duration"});
		assertEquals(getErrorDiv("Invalid ticket type."), outContent.toString());
	}
	
	@Test
	public void testInvalidSortType() {
		Main.validateArgs(new String[] {"oneway", "JFK-ORD","4/24/18", "bestprice"});
		assertEquals(getErrorDiv("Invalid sort type."), outContent.toString());
	}
	
	@Test
	public void testInvalidRoutes() {
		Main.validateArgs(new String[] {"roundtrip", "JFK-ORDS","4/24/18", "5/17/18", "bestflight"});
	
		Main.validateArgs(new String[] {"oneway", "JFK.ORD","4/24/18", "price"});
		
		Main.validateArgs(new String[] {"oneway", "JFK-JFK","4/24/18", "duration"});
		
		assertEquals(getErrorDiv("Invalid route.") + getErrorDiv("Invalid route.") + getErrorDiv("Invalid route."), outContent.toString());
		
	}
	
	@Test
	public void testInvalidDate() {
		Main.validateArgs(new String[] {"multicity", "JFK-LHR", "5/3/18", "LHR-CDG", "4/2/18", "price"});
		Main.validateArgs(new String[] {"multicity", "JFK-LHR", "5/3/17", "LHR-CDG", "4/2/18", "price"});
		Main.validateArgs(new String[] {"multicity", "JFK-LHR", "5/3/18", "LHR-CDG", "4/2/19", "price"});
		
		assertEquals(getErrorDiv("Flight leaves before you arrive. Please adjust dates.") 
						+ getErrorDiv("Invalid date.") 
						+ getErrorDiv("Your trip must start and end within 1 year of today."), 
					outContent.toString());
	}
	
	private String getErrorDiv(String message) {
		return "<div class = 'error'>Error: " + message + "</div>";
	}

}
