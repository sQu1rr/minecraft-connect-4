/* COPYRIGHT (C) 2014 Aleksandr Belkin. All Rights Reserved. */
package sq.squ1rr.mcc4;

import sq.squ1rr.mcc4.layout.LayoutManager;
import sq.squ1rr.mcc4.layout.LayoutManager.Theme;
import sq.squ1rr.mcc4.layout.McButton;
import sq.squ1rr.mcc4.MainMenuActivity.MenuId;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Main menu layout. Builds the layout and handles user interactions.
 * @author Aleksandr Belkin
 */
public class MainMenu extends MenuLayout {
    
    /** parent activity */
    private final MainMenuActivity activity;
    
    /*
     * Buttons
     */
    private McButton btnQuickGame = null;
    private McButton btnStartGame = null;
    private McButton btnStats = null;
    private McButton btnOptions = null;
    private McButton btnAbout = null;
    
    /**
     * Sets up theme
     */
    public MainMenu(MainMenuActivity _activity) {
        super(Theme.MAIN);
        activity = _activity;
    }
    
    /*
     * (non-Javadoc)
     * @see sq.squ1rr.mcc4.MenuLayout#build(sq.squ1rr.mcc4.layout.LayoutManager)
     */
    @Override
    public void build(LayoutManager layout) {        
        btnQuickGame = new McButton(activity);
            btnQuickGame.setOnClickListener(clickListener);
            btnQuickGame.setText(activity.getString(R.string.menu_quick_game));
            layout.add(btnQuickGame);
        
        btnStartGame = new McButton(activity);
            btnStartGame.setOnClickListener(clickListener);
            btnStartGame.setText(activity.getString(R.string.menu_start_game));
            btnStartGame.setEnabled(false);
            layout.add(btnStartGame);
            
        btnStats = new McButton(activity);
            btnStats.setOnClickListener(clickListener);
            btnStats.setText(activity.getString(R.string.menu_stats));
            layout.add(btnStats);
            
        btnOptions = new McButton(activity);
            btnOptions.setOnClickListener(clickListener);
            btnOptions.setText(activity.getString(R.string.menu_options));
            layout.add(btnOptions);
            
        btnAbout = new McButton(activity);
            btnAbout.setOnClickListener(clickListener);
            btnAbout.setText(activity.getString(R.string.menu_about));
            layout.add(btnAbout);
    }
    
    /**
     * Handles button clicks
     */
    private OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view == btnQuickGame) {
                activity.showMenu(MenuId.QUICK_GAME);
            } else if(view == btnStats) {
                activity.showMenu(MenuId.STATS);
            } else if(view == btnOptions) {
                activity.showMenu(MenuId.OPTIONS);
            } else if(view == btnAbout) {
                activity.showMenu(MenuId.ABOUT);
            }
        }
    };
}
