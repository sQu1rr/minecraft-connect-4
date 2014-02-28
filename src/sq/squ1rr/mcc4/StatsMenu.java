/* COPYRIGHT (C) 2014 Aleksandr Belkin. All Rights Reserved. */
package sq.squ1rr.mcc4;

import sq.squ1rr.mcc4.layout.LayoutManager;
import sq.squ1rr.mcc4.layout.McGroup;
import sq.squ1rr.mcc4.layout.McStyle;
import sq.squ1rr.mcc4.layout.LayoutManager.LayoutPart;
import sq.squ1rr.mcc4.layout.LayoutManager.Theme;
import sq.squ1rr.mcc4.layout.McButton;
import sq.squ1rr.mcc4.layout.McText;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

/**
 * Stats menu layout. Builds the layout and handles user interactions.
 * @author Aleksandr Belkin
 */
public class StatsMenu extends MenuLayout {
	
	/** parent activity */
	private final MainMenuActivity activity;
	
	/*
	 * UI Views
	 */
	private McButton btnBack = null;
	private McButton btnClear = null;
	
	/** stats database */
	private final Stats stats;
	
	/**
	 * Sets up theme
	 */
	public StatsMenu(MainMenuActivity _activity) {
		super(Theme.DIRT);
		
		activity = _activity;
		
		stats = new Stats(_activity);
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
			text.setText(activity.getString(R.string.menu_stats));
			text.setGravity(Gravity.CENTER_HORIZONTAL);
			text.setTextColor(McStyle.TEXT_COLOUR_TITLE);
		layout.add(text, LayoutPart.HEADER);
	}
	
	/**
	 * Build content views
	 * @param layout
	 */
	private void buildContent(LayoutManager layout) {
		createRow(layout, Stats.STATS_GAMES, R.string.stats_games);
		createRow(layout, Stats.STATS_PVPS, R.string.stats_pvps);
		createRow(layout, Stats.STATS_WINS, R.string.stats_wins);
		createRow(layout, Stats.STATS_DEFEATS, R.string.stats_defeats);
		createRow(layout, Stats.STATS_DRAWS, R.string.stats_draws);
		createRow(layout, Stats.STATS_TIME, R.string.stats_time);
	}
	
	/**
	 * Create a row of statistics (description on the left, value on the right)
	 * @param layout
	 * @param stat
	 * @param str
	 */
	private void createRow(LayoutManager layout, int stat, int str) {
		McGroup group = new McGroup(activity);
			group.setOrientation(LinearLayout.HORIZONTAL);
			group.setChildParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT, 
					1f
			);
		
		// description
		McText title = new McText(activity);
			title.setText(activity.getString(str));
			title.setTextColor(McStyle.TEXT_COLOUR_TITLE);
		group.addView(title);
		
		// value
		int val = stats.get(stat);
		McText value = new McText(activity);
			value.setText(String.valueOf(val == -1 ? 0 : val));
			value.setTextColor(McStyle.TEXT_COLOUR_TITLE);
			value.setGravity(Gravity.RIGHT);
		group.addView(value);
			
		layout.add(group);
	}
	
	/**
	 * Build footer views
	 * @param layout
	 */
	private void buildFooter(LayoutManager layout) {
		McGroup group = new McGroup(activity);
			group.setOrientation(LinearLayout.HORIZONTAL);
			group.setChildParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT, 
					1f
			);
		layout.add(group, LayoutPart.FOOTER);
		
		// back button
		btnBack = new McButton(activity);
			btnBack.setOnClickListener(clickListener);
			btnBack.setText(activity.getString(R.string.menu_back));
		group.addView(btnBack);
		
		// clear button
		btnClear = new McButton(activity);
			btnClear.setOnClickListener(clickListener);
			btnClear.setText(activity.getString(R.string.stats_clear));
		group.addView(btnClear);
	}
	
	/**
	 * Handles button clicks
	 */
	private OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			if(view == btnClear) {
				stats.deleteAll();
				activity.refresh();
			} else if(view == btnBack) {
				activity.onBackPressed();
			}
		}
	};
}
