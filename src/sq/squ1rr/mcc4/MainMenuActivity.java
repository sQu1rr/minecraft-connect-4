/* COPYRIGHT (C) 2014 Aleksandr Belkin. All Rights Reserved. */
package sq.squ1rr.mcc4;

import java.util.ArrayList;

import sq.squ1rr.mcc4.layout.LayoutManager;
import sq.squ1rr.mcc4.util.GlobalConstants;
import android.os.Bundle;
import android.util.Log;

/**
 * Main application activity. The program will launch from here.
 * Displays menu from which user may choose what to do next.
 * Does not use XML layout.
 * @author Aleksandr Belkin
 */
public class MainMenuActivity extends BaseActivity {
	/** tag name for debug */
	private static final String TAG = MainMenuActivity.class.getName();
	
	/** Menu IDs */
	public enum MenuId {
		MAIN_MENU, QUICK_GAME, STATS, OPTIONS, ABOUT;
	}
	
	/** currently shown menu */
	private MenuId menuId = null;
	
	/** layout manager to manage the current layout */
	private LayoutManager layout;
	
	/** history of opened menus */
	private final ArrayList<MenuId> history = new ArrayList<MenuId>();

	/*
	 * (non-Javadoc)
	 * @see sq.squ1rr.mcc4.util.FullscreenActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		layout = new LayoutManager(this);
		showMenu(MenuId.MAIN_MENU, false);
	}
	
	/**
	 * Shows selected menu
	 * @param menuId
	 */
	public void showMenu(MenuId menuId) {
		showMenu(menuId, true);
	}
	
	/**
	 * Show menu if it's not the same already
	 * @param menuId which menu
	 */
	private void showMenu(MenuId _menuId, boolean save) {
		if(menuId == _menuId) return;
		else {
			if(save) history.add(menuId);
			menuId = _menuId;
		}

		switch(menuId) {
		case MAIN_MENU: layout.setLayout(new MainMenu(this)); break;
		case QUICK_GAME: layout.setLayout(new QuickGameMenu(this)); break;
		case STATS: layout.setLayout(new StatsMenu(this)); break;
		case OPTIONS: layout.setLayout(new OptionsMenu(this)); break;
		case ABOUT: layout.setLayout(new AboutMenu(this)); break;
		default:
			if(GlobalConstants.DEBUG) Log.wtf(TAG, "Undefined Menu ID");
			throw new RuntimeException("Undefined Menu ID");
		}
	}
	
	/**
	 * Make this menu "first" in history
	 */
	public void resetHistory() {
		history.clear();
	}
	
	/**
	 * Refresh current layout
	 */
	public void refresh() {
		layout.refresh();
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		if(history.size() > 0) {
			showMenu(history.get(history.size() - 1), false);
			history.remove(history.size() - 1);
		} else super.onBackPressed();
	}
}
