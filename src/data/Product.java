package data;

/**
 * A product
 * @author Daniele Palazzo
 *
 */

public class Product {

	private String name;
	private float price;
	private String SKU;

	public Product(String name, float price, String SKU) {
		this.name = name;
		this.price = price;
		this.SKU = SKU;
	}

	public String getName() {
		return name;
	}

	public float getPrice() {
		return price;
	}

	public String getSKU() {
		return SKU;
	}
}

