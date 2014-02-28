/* COPYRIGHT (C) 2014 Aleksandr Belkin. All Rights Reserved. */
package sq.squ1rr.mcc4;

import sq.squ1rr.mcc4.layout.LayoutManager;
import sq.squ1rr.mcc4.layout.LayoutManager.Theme;

/**
 * Base class for layout handlers.
 * @author Aleksandr Belkin
 */
abstract public class MenuLayout {
	/** theme used */
	private final Theme theme;
	
	/**
	 * Create menu layout with theme
	 * @param _theme
	 */
	MenuLayout(Theme _theme) {
		theme = _theme;
	}
	
	/**
	 * Returns theme of the layout
	 * @return
	 */
	public Theme getTheme() {
		return theme;
	}
	
	/**
	 * Builds the layout (inserts buttons and such)
	 * @param layout
	 */
	abstract public void build(LayoutManager layout);
}
