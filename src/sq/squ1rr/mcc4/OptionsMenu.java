/* COPYRIGHT (C) 2014 Aleksandr Belkin. All Rights Reserved. */
package sq.squ1rr.mcc4;

import sq.squ1rr.mcc4.layout.LayoutManager;
import sq.squ1rr.mcc4.layout.LayoutManager.Theme;
import sq.squ1rr.mcc4.layout.McButton;
import sq.squ1rr.mcc4.layout.McSelector;
import sq.squ1rr.mcc4.layout.McStyle;
import sq.squ1rr.mcc4.layout.McText;
import sq.squ1rr.mcc4.rules.GameRules;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Options menu layout. Builds the layout and handles user interactions.
 * @author Aleksandr Belkin
 */
public class OptionsMenu extends MenuLayout {
	
	/** parent activity */
	private final MainMenuActivity activity;
	
	/** game rules */
	private final GameRules rules;
	
	/*
	 * Buttons
	 */
	private McButton btnBack = null;
	private McSelector sctFps = null;
	
	/**
	 * Sets up theme
	 */
	public OptionsMenu(MainMenuActivity _activity) {
		super(Theme.MAIN);
		activity = _activity;
		rules = _activity.getRules();
	}
	
	/*
	 * (non-Javadoc)
	 * @see sq.squ1rr.mcc4.MenuLayout#build(sq.squ1rr.mcc4.layout.LayoutManager)
	 */
	@Override
	public void build(LayoutManager layout) {
		McText text = new McText(activity);
			text.setText(activity.getString(R.string.fps_title));
		layout.add(text);
		
		sctFps = new McSelector(activity);
			sctFps.setOnClickListener(clickListener);
			sctFps.setLabels(rules.getIds(GameRules.FPS));
			sctFps.selectId(rules.getRule(GameRules.FPS));
		layout.add(sctFps);
		
		layout.addDivider(McStyle.getMenuBannerSpacing(activity));
		
		btnBack = new McButton(activity);
			btnBack.setOnClickListener(clickListener);
			btnBack.setText(activity.getString(R.string.menu_back));
		layout.add(btnBack);
	}
	
	/**
	 * Handles button clicks
	 */
	private OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			if(view == btnBack) {
				activity.onBackPressed();
			} else if(view == sctFps) {
				rules.setRule(GameRules.FPS, sctFps.getSelectedId());
			}
		}
	};
}
