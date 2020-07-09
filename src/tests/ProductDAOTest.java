package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Iterator;
import java.util.Map;

import org.junit.jupiter.api.Test;

import businessLogic.ProductDAO;
import data.Offer;
import data.Product;
import exceptions.OfferNotFound;
import exceptions.ProductNotFound;

class ProductDAOTest {

	private ProductDAO productDatabase;

	// initialize test object
	private void InitializeTestObject() {
		productDatabase = new ProductDAO();
		productDatabase.addProduct("A", new Product("A", 0.23f, "A"));
		productDatabase.addProduct("B", new Product("B", 0.23f, "B"));
		productDatabase.addProduct("C", new Product("C", 0.23f, "C"));
		productDatabase.addOffer("A offer", new Offer("A offer", 5, 1.23f));
		productDatabase.addOffer("B offer", new Offer("B offer", 5, 1.23f));

		try {
			productDatabase.attachOfferToProduct("A offer", "A");
			productDatabase.attachOfferToProduct("B offer", "B");
			productDatabase.attachOfferToProduct("B offer", "C");
		} catch (ProductNotFound e) {
			fail("Can't find added product");
		} catch (OfferNotFound e) {
			fail("Can't find added offer");
		}
	}

	@Test
	void getProductTest() {
		InitializeTestObject();
		// test with valid SKU
		String validSKU = "A";

		Product p = null;

		try {
			p = productDatabase.getProduct(validSKU);
			assertNotNull(p);
			assertEquals(p.getSKU(), validSKU);
		} catch (ProductNotFound e) {
			fail("Existing product not found!");
		}

		// test with invalid SKU
		String invalidSKU = "E";

		p = null;
		try {
			p = productDatabase.getProduct(invalidSKU);
			fail("Non existing item found!");
		} catch (ProductNotFound e) {

		}

		// test with null
		p = null;
		try {
			p = productDatabase.getProduct(null);
			fail("Non existing item found!");
		} catch (ProductNotFound e) {

		}

	}

	@Test
	void addProductTest() {
		InitializeTestObject();
		productDatabase.addProduct("L", new Product("L", 0.12f, "L"));

		try {
			Product p = productDatabase.getProduct("L");
			assertNotNull(p);
			assertEquals(p, productDatabase.getProduct("L"));
		} catch (ProductNotFound e) {
			fail("Product was not added!");
		}
	}

	@Test
	void removeProductTest() {
		InitializeTestObject();
		// Valid remove
		String validSKU = "A";
		try {
			productDatabase.removeProduct(validSKU);
		} catch (ProductNotFound e) {
			fail("Existing product not found when trying to remove it!");
		}

		try {
			productDatabase.getProduct(validSKU);
			fail("Product wasn't removed from database");
		} catch (ProductNotFound e2) {
		}

		try {
			productDatabase.getOffer("A offer");
			fail("Offer wasn't removed along with only attached product!");
		} catch (OfferNotFound e1) {
		}

		try {
			productDatabase.getProductOffer(validSKU);
			fail("Product still present in the product/offer connection table.");
		} catch (ProductNotFound e1) {
		}

		// Invalid remove
		String inavliddSKU = "E";
		try {
			productDatabase.removeProduct(inavliddSKU);
			fail("Non existing item removed!");
		} catch (ProductNotFound e) {

		}
	}

	@Test
	void getOfferTest() {
		InitializeTestObject();
		// valid
		try {
			Offer o = productDatabase.getOffer("A offer");
			assertNotNull(o);
			assertEquals(o.getName(), "A offer");
		} catch (OfferNotFound e) {
			fail("Existing offer not found!");
		}

		// invalid
		try {
			productDatabase.getOffer("Z offer");
			fail("Nonexisting offer found!");
		} catch (OfferNotFound e) {
		}
	}

	@Test
	void getProductOfferTest() {
		InitializeTestObject();
		// get existing offer
		try {
			Offer o = productDatabase.getProductOffer("A");
			assertNotNull(o);
			assertEquals(o.getName(), "A offer");
		} catch (ProductNotFound e) {
			fail("Existing product offer not found!");
		}

		// get nonexisting offer
	}

	@Test
	void addOfferTest() {
		InitializeTestObject();
		productDatabase.addOffer("Z offer", new Offer("Z offer", 5, 1.12f));

		try {
			Offer o = productDatabase.getOffer("Z offer");
			assertNotNull(o);
			assertEquals(o, productDatabase.getOffer("Z offer"));
		} catch (OfferNotFound e) {
			fail("Offer not added to database!");
		}
	}

	@Test
	void removeOfferTest() {
		InitializeTestObject();
		// valid remove
		String validOfferName = "B offer";

		try {
			productDatabase.removeOffer(validOfferName);
		} catch (OfferNotFound e) {
			fail("Existing offer not found when trying to remove it.");
		}

		try {
			productDatabase.getOffer(validOfferName);
			fail("Offer wasn't removed!");
		} catch (OfferNotFound e) {
		}

		// check all products - make sure offer isn't attached to any of them
		Iterator<Map.Entry<String, Product>> it = productDatabase.getAllAvailableProducts().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Product> pair = it.next();
			try {
				Offer offer = productDatabase.getProductOffer(pair.getKey());
				if (offer != null && offer.getName().equals(validOfferName)) {
					fail("Offer wasn't completely removed from database. Still attached to one or more products.");
				}
			} catch (ProductNotFound e) {
			}
		}

		// invalid remove
		String invalidOfferName = "U offer";

		try {
			productDatabase.removeOffer(invalidOfferName);
			fail("Non existant offer found when trying to remove it!");
		} catch (OfferNotFound e) {

		}
	}
}
