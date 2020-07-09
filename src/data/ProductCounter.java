package data;

/**
 * A mutable wrapper for int to use in place of Integer (which is not mutable)
 * @author Daniele Palazzo
 *
 */
public class ProductCounter {
	private int count;
	
	public ProductCounter() {
		count = 1;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	public void increment() {
		++count;
	}
}
