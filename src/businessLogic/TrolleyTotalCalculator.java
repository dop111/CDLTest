package businessLogic;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import data.Offer;
import data.Product;
import data.ProductCounter;
import data.ShoppingTrolley;
import exceptions.InvalidItemInTrolley;
import exceptions.ProductNotFound;

/**
 * A service for calculating a trolleys total
 * @author Daniele Palazzo
 *
 */
public class TrolleyTotalCalculator {

	private ShoppingTrolley trolley;
	private ProductDAO productDatabase;
	private HashMap<String, String> offersAppliedToProducts; // keep track of applied offers

	/**
	 * 
	 * @param trolley The trolley in question
	 * @param productDatabase The product database to use for getting the associated offers
	 */
	public TrolleyTotalCalculator(ShoppingTrolley trolley, ProductDAO productDatabase) {
		this.trolley = trolley;
		this.productDatabase = productDatabase;
		offersAppliedToProducts = new HashMap<>();
	}

	public ShoppingTrolley getTrolley() {
		return trolley;
	}

	public void setTrolley(ShoppingTrolley trolley) {
		this.trolley = trolley;
		offersAppliedToProducts.clear();
	}

	/**
	 * Calculate the total price based on product prices and offers
	 * @return Return the total for the trolley
	 * @throws InvalidItemInTrolley Thrown if the trolley contains an item which is not in the database
	 */
	public float calculateTrolleyTotal() throws InvalidItemInTrolley {
		float total = 0.0f;

		Set<Entry<Product, ProductCounter>> itemsInTrolley = trolley.getAllItemsInTrolley();

		for (Entry<Product, ProductCounter> productQuantityPair : itemsInTrolley) {

			Offer offer;
			try {
				offer = productDatabase.getProductOffer(productQuantityPair.getKey().getSKU()); //get offer associated with product
				if (offer != null && productQuantityPair.getValue().getCount() >= offer.getGroupNumber()) {

					if (offersAppliedToProducts.get(productQuantityPair.getKey().getSKU()) == null)
						offersAppliedToProducts.put(productQuantityPair.getKey().getSKU(), offer.getName());

					int offerAppliedCount = productQuantityPair.getValue().getCount() / offer.getGroupNumber();
					float offerTotal = offer.getPrice() * offerAppliedCount;
					total += offerTotal + ((productQuantityPair.getValue().getCount() % offer.getGroupNumber())
							* productQuantityPair.getKey().getPrice());

				} else {
					total += productQuantityPair.getKey().getPrice() * productQuantityPair.getValue().getCount();
				}
			} catch (ProductNotFound e1) {
				throw new InvalidItemInTrolley();
			}
		}

		return total;

	}

	public String getOfferAppliedTo(Product p) {
		return offersAppliedToProducts.get(p.getSKU());
	}
}
