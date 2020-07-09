package businessLogic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import data.Offer;
import data.Product;
import exceptions.OfferNotFound;
import exceptions.ProductNotFound;

/**
 * A Data Access Object for accessing the products (and offers) database 
 * @author Daniele Palazzo
 *
 */

public class ProductDAO {

	// the database - could be replaced by a real SQL database connection
	// In that case, these could be used as cache
	private HashMap<String, Product> productTable;
	private HashMap<String, Offer> offerTable;
	private HashMap<Product, Offer> productOfferConTable;

	public ProductDAO() {
		productTable = new HashMap<>();
		offerTable = new HashMap<>();
		productOfferConTable = new HashMap<>();

		// fill product table (would already be in real database)
		productTable.put("A", new Product("Apple", 0.50f, "A"));
		productTable.put("B", new Product("Beetroot", 0.30f, "B"));
		productTable.put("C", new Product("Carrot", 0.20f, "C"));
		productTable.put("D", new Product("Date", 0.15f, "D"));

		// fill offer table (would already be in real database)
		offerTable.put("3 for £1.30", new Offer("3 for £1.30", 3, 1.30f));
		offerTable.put("2 for £0.45", new Offer("2 for £0.45", 2, 0.45f));

		// connect products to offers (would be handled by database)
		productOfferConTable.put(productTable.get("A"), offerTable.get("3 for £1.30"));
		productOfferConTable.put(productTable.get("B"), offerTable.get("2 for £0.45"));
	}
	
	/**
	 * Get product from database
	 * @param SKU Unique product identified of the desired product
	 * @return Requested product
	 * @throws ProductNotFound Exception is thrown if the product is not in the database
	 */

	public Product getProduct(String SKU) throws ProductNotFound {
		if (productTable.get(SKU) == null)
			throw new ProductNotFound();
		else
			return productTable.get(SKU);
	}
	
	/**
	 * Add a new product to the database
	 * @param SKU The SKU of the new product, if it matches an old SKU, that product will be replaced in the database
	 * @param product The new product to put in the database
	 */
	public void addProduct(String SKU, Product product) {
		productTable.put(SKU, product);
	}

	/**
	 * Remove an existing product from the database
	 * @param SKU The unique identifier of the product
	 * @throws ProductNotFound Thrown if no such product exists in the database
	 */
	public void removeProduct(String SKU) throws ProductNotFound {
		Product p = getProduct(SKU);
		productTable.remove(SKU);

		// remove connection to offer if present in database (keep data integrity)
		Offer o = productOfferConTable.get(p);
		productOfferConTable.remove(p);
		if (o != null && !productOfferConTable.containsValue(o)) // offer attached to any other products? if not, remove
																	// it
			offerTable.remove(o.getName());
	}

	/**
	 * Get the specified offer from the database
	 * @param offerName The name of the offer
	 * @return Returns an offer object representing the offer
	 * @throws OfferNotFound Thrown if no such offer is in the database
	 */
	public Offer getOffer(String offerName) throws OfferNotFound {
		if (offerTable.get(offerName) == null)
			throw new OfferNotFound();
		else
			return offerTable.get(offerName);
	}

	/**
	 * Get the offer associated with a specific product (or null if there is no offer for that product)
	 * @param SKU The unique identifier for the product in question
	 * @return Returns an offer object describing the associated offer
	 * @throws ProductNotFound Thrown if no such product exists in the database
	 */
	public Offer getProductOffer(String SKU) throws ProductNotFound {
		Product p = getProduct(SKU);
		return productOfferConTable.get(p);
	}

	/**
	 * Add a new offer to the database (will not be associated with any products until manually done so using 'attachOfferToProduct()')
	 * @param offerName The name of the offer that may be displayed to the customer
	 * @param offer The offer object describing the offer
	 */
	public void addOffer(String offerName, Offer offer) {
		offerTable.put(offerName, offer);
	}

	/**
	 * Remove an offer from the database (and from all associated products)
	 * @param offerName The name of the offer to remove
	 * @throws OfferNotFound Thrown if no such offer exists in the database
	 */
	public void removeOffer(String offerName) throws OfferNotFound {
		Offer o = getOffer(offerName);
		offerTable.remove(offerName);

		// remove all connections to products (keep data integrity)
		Iterator<Map.Entry<Product, Offer>> it = productOfferConTable.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Product, Offer> pair = it.next();
			if (pair.getValue().equals(o)) {
				it.remove();
			}
		}
	}

	/**
	 * Associate an offer with a specific product
	 * @param offerName The name of the offer in question
	 * @param SKU The unique identifier of the product in question
	 * @throws ProductNotFound Thrown if no such product exists in the database
	 * @throws OfferNotFound Thrown if no such offer exists in the database
	 */
	public void attachOfferToProduct(String offerName, String SKU) throws ProductNotFound, OfferNotFound {
		productOfferConTable.put(getProduct(SKU), getOffer(offerName));
	}

	/**
	 * Remove the offer associated with a specific product
	 * @param SKU The unique identifier of the product in question
	 * @throws ProductNotFound Thrown if no such product exists in the database
	 */
	public void removeOfferFromProduct(String SKU) throws ProductNotFound {
		productOfferConTable.remove(getProduct(SKU));
	}

	/**
	 * Gets all available products from the database
	 * @return
	 */
	public Set<Entry<String, Product>> getAllAvailableProducts() {
		return productTable.entrySet();
	}

	/**
	 * Gets all available items from the database
	 * @return
	 */
	public Set<Entry<String, Offer>> getAllAvailableOffers() {
		return offerTable.entrySet();
	}

}
