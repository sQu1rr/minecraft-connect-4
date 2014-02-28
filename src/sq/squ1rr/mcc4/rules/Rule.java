/* COPYRIGHT (C) 2014 Aleksandr Belkin. All Rights Reserved. */
package sq.squ1rr.mcc4.rules;

/**
 * Rule base class.
 * @author Aleksandr Belkin
 */
abstract class Rule {
	
	/** list of all possible IDs */
	private final int[] ids;
	
	/** current selected ID */
	private int id = 0;
	
	/**
	 * Create a rule with all possible IDs
	 * @param _ids
	 */
	Rule(int[] _ids) {
		ids = _ids;
		id = _ids[0];
	}
	
	 /**
	  * Get current ID
	  * @return
	  */
	int getId() {
		return id;
	}
	
	/**
	 * Set new ID
	 * @param _id
	 */
	void setId(int _id) {
		id = _id;
	}
	
	/**
	 * Get all possible IDs
	 * @return
	 */
	int[] getIds() {
		return ids;
	}
}
