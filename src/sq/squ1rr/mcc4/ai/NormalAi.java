/* COPYRIGHT (C) 2014 Aleksandr Belkin. All Rights Reserved. */
package sq.squ1rr.mcc4.ai;

import sq.squ1rr.mcc4.rules.Player;

/**
 * Slightly modified easy AI, does the same thing for the opponent to
 * determine if it is useful to block his combination.
 * @author Aleksandr Belkin
 */
public class NormalAi extends EasyAi {
	/**
	 * Create and initialise AI
	 * @param grid
	 */
	public NormalAi(int[][] grid) {
		super(grid);
	}

	/*
	 * (non-Javadoc)
	 * @see ai.EasyAi#run()
	 */
	@Override
	public int run() {
		return super.run();
	}
	
	/*
	 * (non-Javadoc)
	 * @see ai.EasyAi#analyse(int, int, int)
	 */
	@Override
	protected int analyse(int col, int row, int com) {
		com = countCombinations(col, row, Player.PLAYER1);
		return super.analyse(col, row, com);
	}
}
