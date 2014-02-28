/* COPYRIGHT (C) 2014 Aleksandr Belkin. All Rights Reserved. */
package sq.squ1rr.mcc4;

import java.util.Date;

import sq.squ1rr.mcc4.board.BoardDialogue;
import sq.squ1rr.mcc4.board.BoardView;
import sq.squ1rr.mcc4.board.GameBoard;
import sq.squ1rr.mcc4.rules.GameRules;
import sq.squ1rr.mcc4.rules.GameRules.Fps;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Main game activity, the game is happening here.
 * Does not use XML layout.
 * @author Aleksandr Belkin
 */
public class GameActivity extends BaseActivity {
	/** game board */
	private GameBoard game;
	
	/** OpenGL game view */
	private BoardView view;
	
	/** time played last time-stamp */
	private long lastTimestamp;
	
	/** time played */
	private int timePlayed = 0;
	
	/*
	 * (non-Javadoc)
	 * @see sq.squ1rr.mcc4.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
 
		game = new GameBoard(this, savedInstanceState, getRules());
		view = new BoardView(this, game);
		
		view.setCountFps(getRules().getRule(GameRules.FPS) == Fps.COUNT);

        setContentView(view);
        
        game.setDialogueListener(listener);
	}

	/*
	 * (non-Javadoc)
	 * @see sq.squ1rr.mcc4.BaseActivity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		game.exportTo(savedInstanceState);
		super.onSaveInstanceState(savedInstanceState);
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		timePlayed = (int)((new Date().getTime() - lastTimestamp) / 1000);
		
		view.onPause();
		super.onPause();
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		lastTimestamp = new Date().getTime();
		
		view.onResume();
		super.onResume();
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	public void onDestroy() {
		game.onDestroy();
		super.onDestroy();
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		game.displayDialogue(); // don't want to exit activity
	}
	
	/**
	 * Return time in the game
	 * @return
	 */
	public int getTimePlayed() {
		timePlayed = (int)((new Date().getTime() - lastTimestamp) / 1000);
		
		return timePlayed;
	}
	
	/** Listener for the menu buttons */
	private OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View button) {
			switch(button.getId()) {
			case BoardDialogue.RESTART:
				game.initialise(true);
				game.hideDialogue();
				break;
			case BoardDialogue.MENU:
				Intent menuActivity = new Intent(
					GameActivity.this,
					MainMenuActivity.class
				);
				startActivity(menuActivity);
				finish();
				break;
			}
		}
	};
}
