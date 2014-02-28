/* COPYRIGHT (C) 2014 Aleksandr Belkin. All Rights Reserved. */
package sq.squ1rr.mcc4.board;

import sq.squ1rr.mcc4.board.BoardLogic.Outcome;
import sq.squ1rr.mcc4.layout.McButton;
import sq.squ1rr.mcc4.layout.McGroup;
import sq.squ1rr.mcc4.layout.McStyle;
import sq.squ1rr.mcc4.layout.McText;
import sq.squ1rr.mcc4.R;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

/**
 * Displays dialogue when 'menu' button is clicked during the game or
 * the game is over.
 * @author Aleksandr Belkin
 */
public class BoardDialogue extends Dialog {
	/*
	 * Action constants
	 */
	public static final int RESTART = 1;
	public static final int MENU = 2;
	
	/*
	 * Interface elements
	 */
	private McButton btnReturn = null;
	private McButton btnRestart = null;
	private McButton btnMenu = null;
	private McText line1 = null;
	private McText line2 = null;
	
	/** hide the dialogue when back buttons is pressed */
	private boolean exitOnBack = true;
	
	/** AI state */
	private boolean ai;
	
	/**
	 * Constructs and initialises the dialogue
	 * @param _context
	 */
	public BoardDialogue(Context context) {
		super(context, R.style.BorderDialogue);
		
		initialiseLayout();
		initialiseInterface();
	}
	
	/**
	 * Sets listener for buttons that need external one
	 * @param listener
	 */
	public void setOnClickListener(View.OnClickListener listener) {
		btnRestart.setOnClickListener(listener);
		btnMenu.setOnClickListener(listener);
	}
	
	/**
	 * Initialise dialogue interface layout
	 */
	private void initialiseLayout() {
		final Context context = getContext();
		
		// Create layout for buttons (provides paddings and stuff)
		McGroup layout = new McGroup(getContext());
			layout.setOrientation(LinearLayout.VERTICAL);
			layout.setChildParams(
				LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT
			);
			layout.setPadding(
				McStyle.getMenuPadding(context),
				McStyle.getMenuPadding(context),
				McStyle.getMenuPadding(context),
				McStyle.getMenuPadding(context)
			);
		
		// line that says who won or lost
		line1 = new McText(context);
			line1.setTextSize(
				TypedValue.COMPLEX_UNIT_DIP,
				McStyle.TEXT_SIZE_GAME_LINE1
			);
			line1.setTextColor(McStyle.TEXT_COLOUR_TITLE);
			line1.setGravity(Gravity.CENTER_HORIZONTAL);
		layout.addView(line1);
		
		// line that says if its a win or loss
		line2 = new McText(context);
			line2.setTextSize(
					TypedValue.COMPLEX_UNIT_DIP,
					McStyle.TEXT_SIZE_GAME_LINE2
				);
			line2.setTextColor(McStyle.TEXT_COLOUR_TITLE);
			line2.setGravity(Gravity.CENTER_HORIZONTAL);
		layout.addView(line2);
		
		// return to game button
		btnReturn = new McButton(context);
			btnReturn.setText(context.getString(R.string.game_menu_return));
			btnReturn.setOnClickListener(returnListener);
		layout.addView(btnReturn);
		
		// restart the game button
		btnRestart = new McButton(context);
			btnRestart.setId(RESTART);
			btnRestart.setText(context.getString(R.string.game_menu_restart));
		layout.addView(btnRestart);
		
		// return to the main menu button
		btnMenu = new McButton(context);
			btnMenu.setId(MENU);
			btnMenu.setText(context.getString(R.string.game_menu_menu));
		layout.addView(btnMenu);
		
		// set up the content view
		setContentView(layout);
	}
	
	/**
	 * Initialise dialogue interface
	 */
	@SuppressLint({ "NewApi", "InlinedApi" })
	private void initialiseInterface() {
		// prevent back button from hiding the dialogue when it's not needed
		setOnKeyListener(new Dialog.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface arg0, int keyCode,
								 KeyEvent event) {
				return !(keyCode == KeyEvent.KEYCODE_BACK && exitOnBack);
			}
		});
		
		// set up the dialogue interface (so it's not default small)
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		    lp.copyFrom(getWindow().getAttributes());
		    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		    lp.height = WindowManager.LayoutParams.MATCH_PARENT;
	    getWindow().setAttributes(lp);
	    
	    // set window to fullscreen to avoid flicking status bar
	    getWindow().setFlags(
			WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
		if(Build.VERSION.SDK_INT >= 16) {
	    	// android 4.1 and higher
	    	View decorView = getWindow().getDecorView();
	    	int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
	    	decorView.setSystemUiVisibility(uiOptions);
	    }
	}
	
	public void setAi(boolean _ai) {
		ai = _ai;
	}
	
	public void setOutcome(Outcome outcome) {
		// set button to enabled if game is not finished
		btnReturn.setEnabled(outcome == Outcome.NOTHING);
		
		// dialogue can be closed on back button if the game is not finished
		exitOnBack = outcome == Outcome.NOTHING;
		
		// #1 line size
		line1.setTextSize(
			TypedValue.COMPLEX_UNIT_DIP,
			ai ? McStyle.TEXT_SIZE_GAME_LINE1_AI : McStyle.TEXT_SIZE_GAME_LINE1
		);
		
		line1.setVisibility(View.INVISIBLE);
		line2.setVisibility(View.INVISIBLE);
		
		// set text
		if(outcome != Outcome.NOTHING) {
			line2.setVisibility(View.VISIBLE);
			if(outcome != Outcome.DRAW) {
				line1.setVisibility(View.VISIBLE);
				
				if(ai) {
					line1.setText(R.string.outcome_you);
					if(outcome == Outcome.P1_WINS) {
						line2.setText(R.string.outcome_won);
					} else line2.setText(R.string.outcome_lost);
				} else {
					if(outcome == Outcome.P1_WINS) {
						line1.setText(R.string.outcome_player1);
					} else line1.setText(R.string.outcome_player2);
					line2.setText(R.string.outcome_win);
				}
			}
			else line2.setText(R.string.outcome_draw);
		}
	}
	
	/**
	 * Closes the dialogue when fired (hides)
	 */
	private View.OnClickListener returnListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			hide();
		}
	};
}
