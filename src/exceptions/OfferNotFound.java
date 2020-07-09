package exceptions;

public class OfferNotFound extends Throwable {
	public OfferNotFound() {
		super("Offer not found in the database");
	}
}
