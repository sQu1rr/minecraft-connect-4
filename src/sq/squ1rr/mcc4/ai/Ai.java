/* COPYRIGHT (C) 2014 Aleksandr Belkin. All Rights Reserved. */
package sq.squ1rr.mcc4.ai;

/**
 * Provides a simple interface for AI classes.
 * @author Aleksandr Belkin
 */
public interface Ai {
	/**
	 * Runs the logic, returns the column to put the chip in
	 * @return
	 */
	public int run();
}
