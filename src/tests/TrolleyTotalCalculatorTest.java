package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import businessLogic.ProductDAO;
import businessLogic.TrolleyTotalCalculator;
import data.Offer;
import data.Product;
import data.ShoppingTrolley;
import exceptions.InvalidItemInTrolley;
import exceptions.OfferNotFound;
import exceptions.ProductNotFound;

class TrolleyTotalCalculatorTest {

	private ProductDAO productDatabase;

	private TrolleyTotalCalculatorTest() {
		productDatabase = new ProductDAO();

		productDatabase.addProduct("A", new Product("Apple", 0.50f, "A"));
		productDatabase.addProduct("B", new Product("Beetroot", 0.30f, "B"));
		productDatabase.addProduct("C", new Product("Carrot", 0.20f, "C"));
		productDatabase.addProduct("D", new Product("Date", 0.15f, "D"));

		productDatabase.addOffer("3 for £1.30", new Offer("3 for £1.30", 3, 1.30f));
		productDatabase.addOffer("2 for £0.45", new Offer("2 for £0.45", 2, 0.45f));

		try {
			productDatabase.attachOfferToProduct("3 for £1.30", "A");
			productDatabase.attachOfferToProduct("2 for £0.45", "B");
		} catch (ProductNotFound | OfferNotFound e) {
		}

	}

	@Test
	void calculateTrolleyTotalTestCase1() {
		// no offers applied

		ShoppingTrolley shoppingTrolley = new ShoppingTrolley();

		TrolleyTotalCalculator calc = new TrolleyTotalCalculator(shoppingTrolley, productDatabase);

		Product A = null;
		Product B = null;
		try {
			A = productDatabase.getProduct("A");
			B = productDatabase.getProduct("B");
		} catch (ProductNotFound e) {
		}

		shoppingTrolley.addItem(A);
		shoppingTrolley.addItem(B);

		try {
			assertEquals(calc.calculateTrolleyTotal(), A.getPrice() + B.getPrice());
		} catch (InvalidItemInTrolley e) {
		}
	}

	@Test
	void calculateTrolleyTotalTestCase2() {
		// 1 offer applied once

		ShoppingTrolley shoppingTrolley = new ShoppingTrolley();
		TrolleyTotalCalculator calc = new TrolleyTotalCalculator(shoppingTrolley, productDatabase);

		Product A = null;
		Product B = null;
		try {
			A = productDatabase.getProduct("A");
			B = productDatabase.getProduct("B");
		} catch (ProductNotFound e) {
		}
		shoppingTrolley.addItem(A);
		shoppingTrolley.addItem(B);
		shoppingTrolley.addItem(B);
		shoppingTrolley.addItem(B);

		try {
			assertEquals(calc.calculateTrolleyTotal(), 1.25f);
		} catch (InvalidItemInTrolley e) {
		}
	}

	@Test
	void calculateTrolleyTotalTestCase3() {
		// 1 offer applied twice
		
		ShoppingTrolley shoppingTrolley = new ShoppingTrolley();
		TrolleyTotalCalculator calc = new TrolleyTotalCalculator(shoppingTrolley, productDatabase);

		Product A = null;
		Product B = null;
		try {
			A = productDatabase.getProduct("A");
			B = productDatabase.getProduct("B");
		} catch (ProductNotFound e) {
		}
		shoppingTrolley.addItem(A);
		shoppingTrolley.addItem(B);
		shoppingTrolley.addItem(B);
		shoppingTrolley.addItem(B);
		shoppingTrolley.addItem(B);
		shoppingTrolley.addItem(B);

		try {
			assertEquals(calc.calculateTrolleyTotal(), 1.70f);
		} catch (InvalidItemInTrolley e) {
		}
	}
	
	@Test
	void calculateTrolleyTotalTestCase4() {
		// multiple offers applied (and other products)
		ShoppingTrolley shoppingTrolley = new ShoppingTrolley();
		TrolleyTotalCalculator calc = new TrolleyTotalCalculator(shoppingTrolley, productDatabase);

		Product A = null;
		Product B = null;
		Product C = null;
		Product D = null;
		try {
			A = productDatabase.getProduct("A");
			B = productDatabase.getProduct("B");
			C = productDatabase.getProduct("C");
			D = productDatabase.getProduct("D");
		} catch (ProductNotFound e) {
		}
		shoppingTrolley.addItem(A);
		shoppingTrolley.addItem(B);
		shoppingTrolley.addItem(B);
		shoppingTrolley.addItem(B);
		shoppingTrolley.addItem(B);
		shoppingTrolley.addItem(A);
		shoppingTrolley.addItem(A);
		shoppingTrolley.addItem(A);
		shoppingTrolley.addItem(C);
		shoppingTrolley.addItem(C);
		shoppingTrolley.addItem(D);
		shoppingTrolley.addItem(D);
		shoppingTrolley.addItem(D);

		try {
			assertEquals(calc.calculateTrolleyTotal(), 3.55f);
		} catch (InvalidItemInTrolley e) {
		}
	}
}

