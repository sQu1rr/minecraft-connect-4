/* COPYRIGHT (C) 2014 Aleksandr Belkin. All Rights Reserved. */
package sq.squ1rr.mcc4.layout;

import java.util.ArrayList;

import sq.squ1rr.mcc4.rules.GameRules;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Provides a menu to select a token for the user (or 2 users).
 * Available tokens are in @see rules.Tokens
 * Listener is fired ONLY when a new button is selected.
 * @author Aleksandr Belkin
 */
public class McToken extends McGroup {
	/*
	 * Player labels
	 */
	private static final String ONE = "I";
	private static final String TWO = "II";
	
	/** toggler list */
	private ArrayList<McToggler> togglers = new ArrayList<McToggler>();
	
	/** user click listener */
	private OnClickListener clickListener = null;
	
	/** selected index */
	private int selected = 0;
	
	/** player 2 selected index */
	private int selected2 = 0;
	
	/** number of players choosing */
	private int players = 1;
	
	/** player that is currently choosing */
	private int playerChoosing = 1;

	/**
	 * Create token selector
	 * @param context
	 */
	public McToken(Context context) {
		super(context);

		setOrientation(LinearLayout.HORIZONTAL);
		
		// should be as wide as content to be centred
		LayoutParams params = new LayoutParams(
	            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT
	    );
		setLayoutParams(params);
		setGravity(Gravity.CENTER);
		
		// children must wrap content (at least horizontally)
		setChildParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}
	
	/**
	 * Sets game rules
	 * @param rules
	 */
	public void setGameRules(GameRules rules) {
		initialiseTogglers(rules);
	}

	/**
	 * Initialise toggle buttons from rules.Token
	 */
	private void initialiseTogglers(GameRules rules) {
		int[] buttonIds = rules.getIds(GameRules.TOKEN);
		
		int index = 0;
		for(int id : buttonIds) {
			McToggler toggler = new McToggler(getContext());
				toggler.setImage(id);
				toggler.setId(index);
				toggler.setTag(Integer.valueOf(id));
				toggler.setOnClickListener(listener);
				toggler.setUncheckable(true);
			togglers.add(toggler);
			addView(toggler);
			index++;
		}
	}
	
	/**
	 * Select particular tokens by IDs
	 * @param selectedId
	 */
	public void select(int selectedId, int selectedId2) {
		// null selected indices
		selected = selected2 = 0;
		boolean secondIsSet = false;
		
		// try to find both indices on SEPARATE elements
		int index = 0;
		for(McToggler button : togglers) {
			if((Integer)button.getTag() == selectedId) {
				if(players == 2) button.setLabel(ONE);
				button.setChecked(true);
				selected = index;
			}
			else if((Integer)button.getTag() == selectedId2) {
				if(players == 2) {
					button.setChecked(true);
					button.setLabel(TWO);
				}
				selected2 = index;
				secondIsSet = true;
			}
			index++;
		}
		
		if(!secondIsSet) {
			// they pointed to the same element, choose any free
			// element for the selection 2
			selected2 = 0;
			for(McToggler button : togglers) {
				if(!button.isChecked()) {
					if(players == 2) {
						button.setChecked(true);
						button.setLabel(TWO);
					}
					break;
				}
				selected2++;
			}
			
			clickListener.onClick(this); // we chose another token
		}
	}
	
	/**
	 * Sets number of players that choosing tokens
	 * @param n
	 */
	public void setPlayers(int n) {
		players = n;
		playerChoosing = 1;
		
		// default buttons
		for(McToggler button : togglers) {
			button.setChecked(false);
			button.setEnabled(true);
			button.setLabel(null);
			button.setSuperSelect(false);
			if(n == 2) button.setUncheckable(false);
			else button.setUncheckable(true);
			button.invalidate();
		}
		
		// select old value(s)
		select(
			(Integer)togglers.get(selected).getTag(),
			(Integer)togglers.get(selected2).getTag()
		);
		
		// we use super select here
		if(players == 2) togglers.get(selected).setSuperSelect(true);
	}
	
	/**
	 * Returns selected toggler ordinal ID
	 * @return
	 */
	public int getSelectedIndex(int which) {
		return which == 1 ? selected : selected2;
	}
	
	/**
	 * Return selected ID
	 * @return
	 */
	public int getSelectedId(int which) {
		int index = which == 1 ? selected : selected2;
		return (Integer)togglers.get(index).getTag();
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.view.View#setOnClickListener(android.view.View.OnClickListener)
	 */
	@Override
	public void setOnClickListener(OnClickListener _clickListener) {
		clickListener = _clickListener;
	}
	
	/**
	 * Deactivates all checked buttons when a new one is clicked
	 */
	private OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			McToggler button = (McToggler)view;
			
			// if button isn't checked, make it super selection and
			// change player who is choosing (if needed)
			if(!button.isChecked()) {
				if(players == 2) {
					button.setSuperSelect(true);
					
					if(button.getId() == selected) {
						playerChoosing = 1;
						togglers.get(selected2).setSuperSelect(false);
					}
					else {
						playerChoosing = 2;
						togglers.get(selected).setSuperSelect(false);
					}
				}
				return;
			}
			
			// who's choosing
			if(playerChoosing == 1) selected = view.getId();
			else selected2 = view.getId();
			
			// super-select new button
			if(players == 2) button.setSuperSelect(true);
			
			// unselect other buttons and apply labels if needed
			for(int i = 0; i < togglers.size(); ++i) {
				McToggler toggler = togglers.get(i);
				if(i == selected) {
					if(players == 2) toggler.setLabel(ONE);
				}
				else if(players == 2 && i == selected2) {
					toggler.setLabel(TWO);
				}
				else {
					toggler.setChecked(false);
					toggler.setSuperSelect(false);
				}
			}
			
			if(players == 1 && selected == selected2) {
				selected2 = (selected2 + 1) % togglers.size();
			}
			
			// call user listener
			if(clickListener != null) clickListener.onClick(McToken.this);
		}
	};
}
