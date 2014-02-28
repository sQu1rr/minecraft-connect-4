/* COPYRIGHT (C) 2014 Aleksandr Belkin. All Rights Reserved. */
package sq.squ1rr.mcc4;

import sq.squ1rr.mcc4.rules.GameRules;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

/**
 * Activities that extend from this one will not have the status bar.
 * Creates game rules object that can be accessed from child activity.
 * It also automatically saves/loads game rules.
 * @author Aleksandr Belkin
 */
class BaseActivity extends Activity {
	
	/** game rules */
	private GameRules rules = new GameRules();
	
	/**
	 * Returns game rules object reference
	 * @return game rules
	 */
	public GameRules getRules() {
		return rules;
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 * 
	 * Makes activity full-screen
	 */
	@SuppressLint("NewApi") // we check the version in the code
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setTheme(R.style.FullscreenTheme);
		
		// load rules if needed
		if(savedInstanceState != null) {
			rules.importFrom(savedInstanceState);
		} else if(getIntent().getExtras() != null) {
			rules.importFrom(getIntent().getExtras());
		}
		
		getWindow().setFlags(
			WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
		if(Build.VERSION.SDK_INT >= 16) {
			// android 4.1 and higher
			View decorView = getWindow().getDecorView();
			int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
			decorView.setSystemUiVisibility(uiOptions);
			
			ActionBar actionBar = getActionBar();
			actionBar.hide();
        }
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		rules.exportTo(savedInstanceState);
		super.onSaveInstanceState(savedInstanceState);
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#startActivity(android.content.Intent)
	 */
	@Override
	public void startActivity(Intent intent) {
		intent.putExtras(rules.exportTo(new Bundle()));
		super.startActivity(intent);
	}
}
