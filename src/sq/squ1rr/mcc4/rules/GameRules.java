/* COPYRIGHT (C) 2014 Aleksandr Belkin. All Rights Reserved. */
package sq.squ1rr.mcc4.rules;

import android.os.Bundle;
import sq.squ1rr.mcc4.R;

/**
 * Provides interface to manipulate and access game rules.
 * Provides functionality to save and load game rules.
 * @author Aleksandr Belkin
 */
public class GameRules {
	/**
	 * Difficulty settings
	 */
	public class Difficulty extends Rule {
		public static final int PEACEFUL	= R.string.difficulty_0;
		public static final int EASY		= R.string.difficulty_1;
		public static final int NORMAL		= R.string.difficulty_2;
		public static final int HARD		= R.string.difficulty_3;
		

		Difficulty() {
			super(new int[] { PEACEFUL, EASY, NORMAL, HARD });
		}
	}

	/**
	 * First Turn settings
	 */
	public class FirstTurn extends Rule {
		public static final int PLAYER1		= Player.PLAYER1;
		public static final int PLAYER2		= Player.PLAYER2;
		

		FirstTurn() {
			super(new int[] { PLAYER1, PLAYER2});
		}
	}

	/**
	 * Game Type settings
	 */
	public class GameType extends Rule {
		public static final int QUICK	= 1;
		

		GameType() {
			super(new int[] { QUICK });
		}
	}

	/**
	 * Opponent settings
	 */
	public class Opponent extends Rule {
		public static final int PLAYER	= R.string.quick_game_opponent_player;
		public static final int AI		= R.string.quick_game_opponent_ai;
		

		Opponent() {
			super(new int[] { PLAYER, AI });
		}
	}

	/**
	 * Token settings
	 */
	public class Token extends Rule {
		public static final int DIAMOND		= R.drawable.diamond;
		public static final int REDSTONE	= R.drawable.redstone;
		public static final int MAGMA		= R.drawable.magma;
		

		Token() {
			super(new int[] { DIAMOND, REDSTONE, MAGMA });
		}
	}
	
	/**
	 * Count FPS
	 */
	public class Fps extends Rule {
		public static final int NO		= R.string.fps_hide;
		public static final int COUNT	= R.string.fps_show;
		
		Fps() {
			super(new int[] { NO, COUNT });
		}
	}

	/*
	 * All possible rules
	 */
	public static final int DIFFICULTY	= 0;
	public static final int FIRST_TURN	= 1;
	public static final int GAME_TYPE	= 2;
	public static final int OPPONENT	= 3;
	public static final int TOKEN		= 4;
	public static final int TOKEN2		= 5; // 2nd player token
	public static final int FPS			= 6;
	
	/** rules storage */
	private final Rule[] rules;
	
	/**
	 * Creates Game rules
	 */
	public GameRules() {
		rules = new Rule[] {
				new Difficulty(),
				new FirstTurn(),
				new GameType(),
				new Opponent(),
				new Token(),
				new Token(),
				new Fps()
		};
	}
	
	/**
	 * Returns current rule state
	 * @param rule
	 * @return
	 */
	public int getRule(int rule) {
		return rules[rule].getId();
	}
	
	/**
	 * Sets new rule state
	 * @param rule
	 * @param value
	 */
	public void setRule(int rule, int value) {
		rules[rule].setId(value);
	}
	
	/**
	 * Returns all possible IDs for the rule
	 * @param rule
	 * @return
	 */
	public int[] getIds(int rule) {
		return rules[rule].getIds();
	}
	
	public Bundle exportTo(Bundle bundle) {
		int[] bundleRules = new int[rules.length];
		for(int i = 0; i < rules.length; ++i) {
			bundleRules[i] = rules[i].getId();
		}
		
		bundle.putIntArray("rules", bundleRules);
		return bundle;
	}
	
	public void importFrom(Bundle bundle) {
		int[] bundleRules = bundle.getIntArray("rules");
		for(int i = 0; i < bundleRules.length; ++i) {
			rules[i].setId(bundleRules[i]);
		}
	}
}
