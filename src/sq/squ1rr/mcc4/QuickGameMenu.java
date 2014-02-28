/* COPYRIGHT (C) 2014 Aleksandr Belkin. All Rights Reserved. */
package sq.squ1rr.mcc4;

import sq.squ1rr.mcc4.layout.LayoutManager;
import sq.squ1rr.mcc4.layout.McSelector;
import sq.squ1rr.mcc4.layout.McGroup;
import sq.squ1rr.mcc4.layout.McStyle;
import sq.squ1rr.mcc4.layout.McToken;
import sq.squ1rr.mcc4.layout.LayoutManager.LayoutPart;
import sq.squ1rr.mcc4.layout.LayoutManager.Theme;
import sq.squ1rr.mcc4.layout.McButton;
import sq.squ1rr.mcc4.layout.McText;
import sq.squ1rr.mcc4.rules.GameRules;
import sq.squ1rr.mcc4.rules.GameRules.GameType;
import sq.squ1rr.mcc4.rules.GameRules.Opponent;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

/**
 * Quick a=game menu layout. Builds the layout and handles user interactions.
 * @author Aleksandr Belkin
 */
public class QuickGameMenu extends MenuLayout {
    
    /** parent activity */
    private final MainMenuActivity activity;
    
    /** game rules */
    private final GameRules rules;
    
    /*
     * First turn label indices
     */
    private static final int TURN_AI = 0;
    private static final int TURN_PLAYER = 1;
    
    /*
     * UI Views
     */
    private McGroup grpDifficulty = null;
    private McSelector sctOpponent = null;
    private McSelector sctDifficulty = null;
    private McSelector sctTurn = null;
    private McToken sctTokens = null;
    private McButton btnBack = null;
    private McButton btnStart = null;
    
    /** turn labels */
    private final String[][] turnLabels;
    
    /**
     * Sets up theme
     */
    public QuickGameMenu(MainMenuActivity _activity) {
        super(Theme.DIRT);
        
        activity = _activity;
        rules = _activity.getRules();
        
        turnLabels = new String[2][2];
        
            turnLabels[TURN_AI][0] = activity.getString(
                R.string.quick_game_opponent_player
            );
            turnLabels[TURN_AI][1] = activity.getString(
                R.string.quick_game_opponent_ai
            );
            
            turnLabels[TURN_PLAYER][0] = activity.getString(R.string.player_1);
            turnLabels[TURN_PLAYER][1] = activity.getString(R.string.player_2);
        
        rules.setRule(GameRules.GAME_TYPE, GameType.QUICK);
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

        if(sctOpponent.getSelectedId() == Opponent.AI) onOpponentAi();
        else onOpponentPlayer();
    }
    
    /**
     * Build header views
     * @param layout
     */
    private void buildHeader(LayoutManager layout) {
        McText text = new McText(activity);
            text.setText(activity.getString(R.string.quick_game_title));
            text.setGravity(Gravity.CENTER_HORIZONTAL);
            text.setTextColor(McStyle.TEXT_COLOUR_TITLE);
        layout.add(text, LayoutPart.HEADER);
    }
    
    /**
     * Build content views
     * @param layout
     */
    private void buildContent(LayoutManager layout) {
        McText text;
        
        // Opponent
        text = new McText(activity);
            text.setText(activity.getString(R.string.quick_game_opponent));
        layout.add(text);
        
        sctOpponent = new McSelector(activity);
            sctOpponent.setOnClickListener(clickListener);
            sctOpponent.setLabels(rules.getIds(GameRules.OPPONENT));
            sctOpponent.selectId(rules.getRule(GameRules.OPPONENT));
        layout.add(sctOpponent);
        
        // Divider
        layout.addDivider(McStyle.getMenuPadding(activity));
        
        // Tokens
        text = new McText(activity);
            text.setText(activity.getString(R.string.select_token));
        layout.add(text);
        
        sctTokens = new McToken(activity);
            sctTokens.setGameRules(rules);
            sctTokens.setOnClickListener(clickListener);
            sctTokens.select(
                rules.getRule(GameRules.TOKEN),
                rules.getRule(GameRules.TOKEN2)
            );
        layout.add(sctTokens);
        
        // Turn
        text = new McText(activity);
            text.setText(activity.getString(R.string.quick_game_first_turn));
        layout.add(text);
        
        sctTurn = new McSelector(activity);
            sctTurn.setOnClickListener(clickListener);
            sctTurn.setIds(rules.getIds(GameRules.FIRST_TURN));
            sctTurn.selectId(rules.getRule(GameRules.FIRST_TURN));
        layout.add(sctTurn);
        
        // Difficulty
        grpDifficulty = new McGroup(activity);
            grpDifficulty.setOrientation(LinearLayout.VERTICAL);
            grpDifficulty.setChildParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            );
        layout.add(grpDifficulty);
            
        text = new McText(activity);
            text.setText(activity.getString(R.string.quick_game_difficulty));
        grpDifficulty.addView(text);
            
        sctDifficulty = new McSelector(activity);
            sctDifficulty.setOnClickListener(clickListener);
            sctDifficulty.setLabels(rules.getIds(GameRules.DIFFICULTY));
            sctDifficulty.selectId(rules.getRule(GameRules.DIFFICULTY));
        grpDifficulty.addView(sctDifficulty);
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
        
        // start button
        btnStart = new McButton(activity);
            btnStart.setOnClickListener(clickListener);
            btnStart.setText(activity.getString(R.string.menu_start));
        group.addView(btnStart);
    }
    
    /**
     * When opponent AI is selected
     */
    private void onOpponentAi() {
        grpDifficulty.setVisibility(View.VISIBLE);
        sctTurn.setLabels(turnLabels[TURN_AI]);
        sctTokens.setPlayers(1);
        activity.getRules().setRule(GameRules.OPPONENT, Opponent.AI);
    }
    
    /**
     * When opponent Player is selected
     */
    private void onOpponentPlayer() {
        grpDifficulty.setVisibility(View.GONE);
        sctTurn.setLabels(turnLabels[TURN_PLAYER]);
        sctTokens.setPlayers(2);
        activity.getRules().setRule(GameRules.OPPONENT, Opponent.PLAYER);
    }
    
    /**
     * Handles button clicks
     */
    private OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view == sctOpponent) {
                if(sctOpponent.getSelectedId() == Opponent.AI) onOpponentAi();
                else onOpponentPlayer();
            }
            else if(view == sctTokens) {
                rules.setRule(GameRules.TOKEN, sctTokens.getSelectedId(1));
                rules.setRule(GameRules.TOKEN2, sctTokens.getSelectedId(2));
            }
            else if(view == sctTurn) {
                rules.setRule(GameRules.FIRST_TURN, sctTurn.getSelectedId());
            }
            else if(view == sctDifficulty) {
                rules.setRule(
                    GameRules.DIFFICULTY,
                    sctDifficulty.getSelectedId()
                );
            }
            else if(view == btnBack) {
                activity.onBackPressed();
            }
            else if(view == btnStart) {
                Intent gameActivity = new Intent(activity, GameActivity.class);
                activity.startActivity(gameActivity);
                activity.finish();
            }
        }
    };
}
