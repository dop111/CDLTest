package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map.Entry;

import businessLogic.ProductDAO;
import businessLogic.TrolleyTotalCalculator;
import data.Offer;
import data.Product;
import data.ProductCounter;
import data.ShoppingTrolley;
import exceptions.InvalidItemInTrolley;
import exceptions.ProductNotFound;

public class CheckOutMain {

	public static void main(String[] args) {

		ProductDAO productDatabase = new ProductDAO();
		ShoppingTrolley shoppingTrolley = new ShoppingTrolley();

		System.out.println("Enter product to add to your shopping trolley.");
		System.out.println("Available items: ");

		// Printing Catalogue
		System.out.printf("%-12s%-12s%-12s%-12s\n", "SKU", "Name", "Price", "Current Offer");
		for (Entry<String, Product> e : productDatabase.getAllAvailableProducts()) {
			Product product = e.getValue();
			try {
				Offer offer = productDatabase.getProductOffer(e.getKey());
				System.out.printf("%-12s%-12s£%-11.2f%-12s\n", product.getSKU(), product.getName(), product.getPrice(),
						(offer != null) ? offer.getName() : "-");
			} catch (ProductNotFound ex) {
				continue;
			}
		}

		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

		// Shopping
		String order = "";
		while (true) {
			try {
				System.out.println("\nProduct SKU to add (type \"checkout\" to stop):");
				order = input.readLine();

				if (order.equals("checkout")) // stop shopping with checkout command
					break;

				Product product = productDatabase.getProduct(order);
				shoppingTrolley.addItem(product);

			} catch (IOException e1) {
				System.out.println("Error: An unexpected error occured.");
				System.exit(1);// close application (should never happen in theory)
			} catch (ProductNotFound e1) {
				System.out.println("Error: No such product in the database. Please try again.\n");
				continue;
			}
		}

		// Printing Receipt
		TrolleyTotalCalculator totalCalculator = new TrolleyTotalCalculator(shoppingTrolley, productDatabase);
		try {
			System.out.println("Thank you for shopping with us!");
			System.out.println("Your Receipt: ");

			float total = totalCalculator.calculateTrolleyTotal(); // calculate trolley

			System.out.printf("%-12s%-12s%-12s\n", "Product", "Quantity", "Applied Offer");
			for (Entry<Product, ProductCounter> e : shoppingTrolley.getAllItemsInTrolley()) {
				String appliedOffer = (totalCalculator.getOfferAppliedTo(e.getKey()) == null) ? "-"
						: totalCalculator.getOfferAppliedTo(e.getKey());
				System.out.printf("%-12s%-12s%-12s\n", e.getKey().getName(), e.getValue().getCount(), appliedOffer);
			}
			System.out.printf("Your total is: £%.2f", total);
		} catch (InvalidItemInTrolley e1) {
			System.out.println("Error: An unexpected error occured.");
			System.exit(1);// close application (should never happen in theory)
		}

	}
}