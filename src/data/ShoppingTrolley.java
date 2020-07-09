package data;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import exceptions.ItemNotInTrolley;

/**
 * A shopping trolley that can be used to hold the items the customer is intending to buy
 * @author Daniele Palazzo
 *
 */

public class ShoppingTrolley {

	private HashMap<Product, ProductCounter> itemsInTrolley;

	public ShoppingTrolley() {
		itemsInTrolley = new HashMap<>();
	}

	/**
	 * Add a product to the trolley
	 * @param p The product to add
	 */
	public void addItem(Product p) {
		// add new product or increment existing product count
		ProductCounter count = itemsInTrolley.get(p);
		if (count == null) {
			itemsInTrolley.put(p, new ProductCounter());
		} else {
			count.increment();
		}
	}

	/**
	 * Remove an item from the trolley
	 * @param p The item to remove
	 * @param number The Quantity to remove
	 * @throws ItemNotInTrolley Thrown if no such item is in the trolley
	 */
	public void removeItem(Product p, int number) throws ItemNotInTrolley {
		if (itemsInTrolley.get(p) == null)
			throw new ItemNotInTrolley();
		
		ProductCounter count = itemsInTrolley.get(p);
		if (count.getCount() == number) {
			itemsInTrolley.remove(p);
		} else {
			count.setCount(count.getCount()-number);
		}
	}

	public Set<Entry<Product, ProductCounter>> getAllItemsInTrolley() {
		return itemsInTrolley.entrySet();
	}
}
