package exceptions;

public class InvalidItemInTrolley extends Throwable {
	public InvalidItemInTrolley() {
		super("An invalid item was detected in the trolley");
	}
}
