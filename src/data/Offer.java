package data;

/**
 * An offer / discount that can be applied to anything
 * @author Daniele Palazzo
 *
 */

public class Offer {
	private String name;
	private int groupNumber;
	private float price;
	
	public Offer(String name, int groupNumber,float price) {
		this.name = name;
		this.groupNumber = groupNumber;
		this.price = price;
	}
	
	public String getName() {
		return name;
	}
	
	public int getGroupNumber() {
		return groupNumber;
	}
	
	public float getPrice() {
		return price;
	}

	@Override
	public String toString() {
		return getName();
	}
	
	
}
