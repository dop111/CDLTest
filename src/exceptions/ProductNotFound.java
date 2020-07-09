package exceptions;

public class ProductNotFound extends Throwable {
	public ProductNotFound() {
		super("Product not found in the database");
	}
}
