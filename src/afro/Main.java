package afro;

/**
 *
 * @author dmn
 */
public class Main {

	public static void main(String[] args) {
		System.out.println("This is a Java library. But from command line can be run in couple of ways.\n"
				+ "Usage:\n"
				+ "\tjava -cp afro.jar " + Mixer.class.getCanonicalName() + "\n"
				+ "\tjava -cp afro.jar " + HrefLister.class.getCanonicalName() + "\n"
				+ "");
	}
}
