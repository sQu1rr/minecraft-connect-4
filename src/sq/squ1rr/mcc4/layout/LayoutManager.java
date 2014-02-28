/* COPYRIGHT (C) 2014 Aleksandr Belkin. All Rights Reserved. */
package sq.squ1rr.mcc4.layout;

import sq.squ1rr.mcc4.MenuLayout;
import sq.squ1rr.mcc4.R;
import sq.squ1rr.mcc4.util.GlobalConstants;
import android.app.Activity;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;

/**
 * Layout manager manages the current activity layout.
 * @author Aleksandr Belkin
 */
public class LayoutManager {
	/** tag for debugging */
	private final static String TAG = LayoutManager.class.getName();
	
	/**
	 * Theme names
	 */
	public enum Theme {
		MAIN, DIRT;
	}
	
	/**
	 * Layout parts
	 */
	public enum LayoutPart {
		HEADER, CONTENT, FOOTER;
	}

	/** parents activity */
	private Activity activity;
	
	/** page layout */
	private LinearLayout page = null;
	
	/** header layout */
	private McGroup header = null;
	
	/** footer layout */
	private McGroup footer = null;
	
	/** menu layout */
	private McGroup menuContainer = null;
	
	/** current theme */
	private Theme theme = null;
	
	/** current layout */
	private MenuLayout layout = null;
	
	/**
	 * Create layout manager, does nothing besides
	 * @param _activity
	 */
	public LayoutManager(Activity _activity) {
		activity = _activity;
	}
	
	/**
	 * Returns parent activity
	 * @return activity
	 */
	public Activity getActivity() {
		return activity;
	}
	
	/**
	 * Sets new layout, doesn't update the theme layout if possible
	 * @param layout
	 */
	public void setLayout(MenuLayout _layout) {
		layout = _layout;
		if(theme != layout.getTheme()) {
			build(layout.getTheme());
		}
		menuContainer.removeAllViews();
		layout.build(this);
	}
	
	/**
	 * Refresh layout
	 */
	public void refresh() {
		if(header != null) header.removeAllViews();
		if(footer != null) footer.removeAllViews();
		menuContainer.removeAllViews();
		layout.build(this);
	}
	
	/**
	 * Create a theme layout based on theme chosen
	 * @param _theme
	 */
	private void build(Theme _theme) {
		theme = _theme;
		
		if(page == null) {
			ScrollView scrollView = new ScrollView(activity);
			scrollView.setFillViewport(true);
			page = new LinearLayout(activity);
			page.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT
			));
			scrollView.addView(page);
			activity.setContentView(scrollView);
		} else {
			page.removeAllViews();
		}
		
		switch(theme) {
		case MAIN: buildMainTheme(); break;
		case DIRT: buildDirtTheme(); break;
		default: 
			if(GlobalConstants.DEBUG) Log.wtf(TAG, "Unknown Layout Theme");
			throw new RuntimeException("Unknown Layout Theme");
		}
	}
	
	/**
	 * Create main theme layout with banner and simple menu.
	 * No header or footer.
	 */
	private void buildMainTheme() {
		// null header and footer
		header = footer = null;
		
		// dimensions
		int padding = McStyle.getMenuPadding(activity);
		int paddingTop = McStyle.getMenuBannerSpacing(activity);
		
		// build layout
		page.setBackgroundResource(R.drawable.menu_background);
			page.setOrientation(LinearLayout.VERTICAL);
			page.setPadding(0, padding, 0, 0);
		
		// banner
		ImageView banner = new ImageView(activity);
			banner.setImageResource(R.drawable.banner);
			page.addView(banner);
		
		// menu area
		menuContainer = new McGroup(activity);
			menuContainer.setChildParams(
					LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT
			);
			menuContainer.setOrientation(LinearLayout.VERTICAL);
			menuContainer.setPadding(padding, paddingTop, padding, padding);
		page.addView(menuContainer);
	}
	
	/**
	 * Create main theme layout with banner and simple menu.
	 * No header or footer.
	 */
	private void buildDirtTheme() {
		// dimensions
		int padding = McStyle.getMenuPadding(activity);
		
		// build layout		
		page.setBackgroundResource(R.drawable.minecraft_dirt_dark);
		page.setOrientation(LinearLayout.VERTICAL);
		
		BitmapDrawable background = (BitmapDrawable)page.getBackground();
			background.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
		
		// header
		header = new McGroup(activity);
			header.setChildParams(
					LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT
			);
			header.setOrientation(LinearLayout.VERTICAL);
			header.setPadding(padding, padding, padding, padding);
			page.addView(header);
		
		// menu wrapper weight adjustment
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
			LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1f
		);
		
		// menu wrapper
		LinearLayout content = new LinearLayout(activity);
			content.setOrientation(LinearLayout.VERTICAL);
			content.setBackgroundColor(0x80000000);
			content.setLayoutParams(layoutParams);
			page.addView(content);
		
		// menu area 
		menuContainer = new McGroup(activity);
			menuContainer.setChildParams(
					LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT
			);
			menuContainer.setOrientation(LinearLayout.VERTICAL);
			menuContainer.setLayoutParams(layoutParams);
			menuContainer.setPadding(padding, 0, padding, 0);
		
		// gradient size
		layoutParams = new LinearLayout.LayoutParams(
			LayoutParams.MATCH_PARENT, padding
		);
		
		// menu wrapper top gradient
		ImageView gradient = new ImageView(activity);
			gradient.setImageResource(R.drawable.dark_content_top);
			gradient.setLayoutParams(layoutParams);
			content.addView(gradient);
		
		// add menu container in between the gradients
		content.addView(menuContainer);
		
		// menu wrapper bottom gradient
		gradient = new ImageView(activity);
			gradient.setImageResource(R.drawable.dark_content_bottom);
			gradient.setLayoutParams(layoutParams);
			content.addView(gradient);
		
		// footer
		footer = new McGroup(activity);
			footer.setChildParams(
					LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT
			);
			footer.setOrientation(LinearLayout.VERTICAL);
			footer.setPadding(padding, padding, padding, padding);
			page.addView(footer);
	}
	
	/**
	 * Add a view to menu layout (not header and not footer)
	 * @param view
	 */
	public void add(View view) {
		add(view, menuContainer);
	}
	
	/**
	 * Add a view to menu layout part
	 * @param view
	 * @param part
	 */
	public void add(View view, LayoutPart part) {
		switch(part) {
		case HEADER:	add(view, header); break;
		case CONTENT:	add(view, menuContainer); break;
		case FOOTER:	add(view, footer); break;
		}
	}
	
	/**
	 * Internal function to add view to a layout part
	 * @param view
	 * @param where
	 */
	private void add(View view, McGroup where) {
		where.addView(view);
	}
	
	/**
	 * Adds a divider of a certain height
	 * @param height in px
	 */
	public void addDivider(int height) {
		View view = new View(activity);
		menuContainer.addView(view);

		// set divider height
		LayoutParams params = new LayoutParams(
			view.getLayoutParams()
		);
		params.height = height;
		view.setLayoutParams(params);
	}
}
