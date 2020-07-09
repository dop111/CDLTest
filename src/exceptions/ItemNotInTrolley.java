package exceptions;

public class ItemNotInTrolley extends Throwable {
	public ItemNotInTrolley() {
		super("Item not found in trolley");
	}
}
