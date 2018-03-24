package main.java.karora.flightpricetracker;

/**
 * The Flight class contains information about one specific flight.
 * @author kashish
 *
 */
public class Flight {
	/*
	* Required parameters.
	*/
	private String airline;
	private String startCity;
	private String destination;
	private int numStops;
	private String departureTime; //format: h:mm a
	private String arrivalTime; //format: h:mm a
	private String duration; //format: h'h' m'm'

	/*
	 * Creates a new Flight with a FlightBuilder object.
	 * @param builder	The builder object
	 */
	private Flight(FlightBuilder builder) {
		this.airline = builder.airline;
		this.startCity = builder.startCity;
		this.destination = builder.destination;
		this.numStops = builder.numStops;
		this.departureTime = builder.departureTime;
		this.arrivalTime = builder.arrivalTime;
		this.duration = builder.duration;

	}
	
	/**
	 * Gets the flight's airline.
	 * @return String 	The airline's name
	 */
	public String getAirline() {
		return this.airline;
	}
	
	/**
	 * Gets the departure city of the flight.
	 * @return String	The departure city's name
	 */
	public String getStartCity() {
		return this.startCity;
	}
	
	/**
	 * Gets the arrival city of the flight.
	 * @return String	The arrival city's name
	 */
	public String getDestination() {
		return this.destination;
	}

	/**
	 * Gets the number of stops (layovers) during the flight.
	 * @return int	  The number of stops
	 */
	public int getNumStops() {
		return this.numStops;
	}

	/**
	 * Gets the departure time of the flight.
	 * @return String	The departure time in the format "h:mm a"
	 */
	public String getDepartureTime() {
		return this.departureTime;
	}

	/**
	 * Gets the arrival time of the flight at the final destination.
	 * @return String 	The arrival time in the format "h:mm a"
	 */
	public String getArrivalTime() {
		return this.arrivalTime;
	}
	
	/**
	 * Gets the length of the entire flight.
	 * @return String	The duration in the format "h'h' m'm'"
	 */
	public String duration() {
		return this.duration;
	}

	
	/**
	 * Returns a string HTML div element that will be used to display information about the 
	 * flight.
	 * @return String
	 */
	public String toString() {
	
		String divElem = "<div class = 'flight'>";
		
		divElem +=  "<div class = 'airline' style = 'display:inline-block'>" + this.airline + "</div>"
					+ "<div class = 'departure' style = 'display:inline-block'>"
						+ "<div class = 'time'>" + this.departureTime + "</div>"
						+ "<div class = 'city'>" + this.startCity + "</div>"
					+ "</div>"
					+ "<div class = 'stops' style = 'display:inline-block'>"
						+ "<div class = 'duration'>" + this.duration + "</div>"
						+ "<div class = 'axis'> -----------> </div>"
						+ "<div class = 'numStops'>" + (this.numStops > 0 ? this.numStops + (this.numStops == 1 ? " stop" : " stops") : "nonstop") + "</div>"
					+ "</div>"
					+ "<div class = 'arrival' style = 'display:inline-block'>"
						+ "<div class = 'time'>" + this.arrivalTime + "</div>"
						+ "<div class = 'city'>" + this.destination + "</div>"
					+ "</div>";
		
		return divElem + "</div>";
		
	}
	
	/**
	 * FlightBuilder is a builder class used to create a Flight object.
	 * @author kashish
	 *
	 */
	public static class FlightBuilder {
		
		/*
		* Required parameters
		*/
		private String airline;
		private String startCity;
		private String destination;
		private Integer numStops;
		private String departureTime;
		private String arrivalTime;
		private String duration;
		
		/**
		 * Sets the airline for the flight.
		 * @param carrier	String, the airline's name
		 * @return this		FlightBuilder, current object
		 */
		public FlightBuilder airline(String airline) {
			this.airline = airline;
			return this;
		}
		
		/**
		 * Sets the departure city of the flight.
		 * @param city		String, the city's name
		 * @return this		FlightBuilder, current object
		 */
		public FlightBuilder startCity(String city) {
			this.startCity = city;
			return this;
		}
		
		/**
		 * Sets the arrival city of the flight.
		 * @param city		String, the arrival city
		 * @return this		FlightBuilder, current object
		 */
		public FlightBuilder destination(String city) {
			this.destination = city;
			return this;
		}

		/**
		 * Sets the number of stops during the entire flight.
		 * @param numStops		int, the number of stops
		 * @return this			FlightBuilder, current object
		 */
		public FlightBuilder numStops(int numStops) {
			this.numStops = numStops;
			return this;
		}

		/**
		 * Sets the flight's departure and arrival time at the final destination.
		 * @param departure		String, departure time in the format "h:mm a"
		 * @param arrival		String, arrival time at the final destination in the format "h:mm a"
		 * @return this			FlightBuilder, current object
		 */
		public FlightBuilder time(String departure, String arrival) {
			this.departureTime = departure;
			this.arrivalTime = arrival;
			return this;
		}
		
		/**
		 * Sets the duration of the entire flight.
		 * @param duration		String, the length of the entire flight in the format "h'h' m'm'"
		 * @return this			FlightBuilder, current object
		 */
		public FlightBuilder duration(String duration) {
			this.duration = duration;
			return this;
		}

		/**
		 * Builds a new Flight object with all of the required parameters. If one or more parameters
		 * are missing, throws an IllegalStateException error.
		 * @return Flight object	A new Flight object with the required parameters
		 */
		public Flight build() {
			if(this.airline == null ||
		            this.startCity == null ||
		            this.destination == null ||
		            this.numStops == null ||
		            this.departureTime == null ||
		            this.arrivalTime == null ||
		            this.duration == null) {
					throw new IllegalStateException("One or more parameters are not given.");
			}
			
			
			return new Flight(this);
		}

		
	}

}