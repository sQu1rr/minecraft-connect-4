/* COPYRIGHT (C) 2014 Aleksandr Belkin. All Rights Reserved. */
package sq.squ1rr.mcc4;

import sq.squ1rr.mcc4.layout.LayoutManager;
import sq.squ1rr.mcc4.layout.McStyle;
import sq.squ1rr.mcc4.layout.LayoutManager.LayoutPart;
import sq.squ1rr.mcc4.layout.LayoutManager.Theme;
import sq.squ1rr.mcc4.layout.McButton;
import sq.squ1rr.mcc4.layout.McText;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * About menu layout. Builds the layout and handles user interactions.
 * @author Aleksandr Belkin
 */
public class AboutMenu extends MenuLayout {
	
	/** parent activity */
	private final MainMenuActivity activity;
	
	/*
	 * UI Views
	 */
	private McButton btnBack = null;
	
	/**
	 * Sets up theme
	 */
	public AboutMenu(MainMenuActivity _activity) {
		super(Theme.DIRT);
		
		activity = _activity;
	}
	
	/*
	 * (non-Javadoc)
	 * @see sq.squ1rr.mcc4.layout.MenuLayout#build(sq.squ1rr.mcc4.layout.LayoutManager)
	 */
	@Override
	public void build(LayoutManager layout) {
		buildHeader(layout);
		buildContent(layout);
		buildFooter(layout);
	}
	
	/**
	 * Build header views
	 * @param layout
	 */
	private void buildHeader(LayoutManager layout) {
		McText text = new McText(activity);
			text.setText(activity.getString(R.string.menu_about));
			text.setGravity(Gravity.CENTER_HORIZONTAL);
			text.setTextColor(McStyle.TEXT_COLOUR_TITLE);
		layout.add(text, LayoutPart.HEADER);
	}
	
	/**
	 * Build content views
	 * @param layout
	 */
	private void buildContent(LayoutManager layout) {
		// title
		McText title = new McText(activity);
			title.setText(activity.getString(R.string.about));
			title.setTextColor(McStyle.TEXT_COLOUR_TITLE);
		layout.add(title);
		
		title = new McText(activity);
		title.setText(activity.getString(R.string.about2));
		title.setTextColor(McStyle.TEXT_COLOUR_TITLE);
	layout.add(title);
	}
	
	/**
	 * Build footer views
	 * @param layout
	 */
	private void buildFooter(LayoutManager layout) {
		// back button
		btnBack = new McButton(activity);
			btnBack.setOnClickListener(clickListener);
			btnBack.setText(activity.getString(R.string.menu_back));
		layout.add(btnBack, LayoutPart.FOOTER);
	}
	
	/**
	 * Handles button clicks
	 */
	private OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			if(view == btnBack) {
				activity.onBackPressed();
			}
		}
	};
}
