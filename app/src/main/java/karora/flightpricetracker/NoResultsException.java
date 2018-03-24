package main.java.karora.flightpricetracker;

public class NoResultsException extends Exception {

	public NoResultsException() {
	}

	public NoResultsException(String message) {
		super(message);
	}

	public NoResultsException(Throwable cause) {
		super(cause);
	}

	public NoResultsException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoResultsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
