/* COPYRIGHT (C) 2014 Aleksandr Belkin. All Rights Reserved. */
package sq.squ1rr.mcc4.ai;

import sq.squ1rr.mcc4.rules.Player;

/**
 * Quite basic AI. Tries to add a new chip to the longest combination,
 * overlooks if it's not possible to finish this combination though.
 * Overlooks gaped combinations too. Overlook upper diagonals too.
 * @author Aleksandr Belkin
 */
public class EasyAi extends PeacefulAi {
	/** number of columns */
	protected final int cols;
	
	/** number of rows */
	protected final int rows;
	
	/**
	 * Create and initialise AI
	 * @param grid
	 */
	public EasyAi(int[][] grid) {
		super(grid);
		
		cols = grid.length;
		rows = grid[0].length;
	}

	/*
	 * (non-Javadoc)
	 * @see ai.PeacefulAi#run()
	 */
	@Override
	public int run() {
		int col = -1;
		int com = -1;
		
		for(int i = 0; i < grid.length; ++i) {
			if(grid[i][0] != 0) continue;
			int j = findRow(i);
			int a = analyse(i, j, 0); // analyse every possible column
			if(a > com) {
				com = a;
				col = i;
			}
		}
		if(com < 1) return super.run();
		return col;
	}
	
	/**
	 * Find empty row in a column
	 * @param col
	 * @return
	 */
	protected int findRow(int col) {
		int row = 0;
		while(row < rows - 1 && grid[col][row+1] == 0) row++;
		return row;
	}
	
	/**
	 * Analyses the column, returns the number which represents the length
	 * of the longest combination that will be created if the chip is placed
	 * in this particular column.
	 * @param col
	 * @param row
	 * @param com
	 * @return
	 */
	protected int analyse(int col, int row, int com) {
		return Math.max(com, countCombinations(col, row, Player.PLAYER2));
	}
	
	/**
	 * Returns the longest combination that will be created if the chip is
	 * placed in this particular column.
	 * @param col
	 * @param row
	 * @param p
	 * @return
	 */
	protected int countCombinations(int col, int row, int p) {
		int com = 0;
		com = Math.max(com, combination(col-1,	row,	-1,	0, p));
		com = Math.max(com, combination(col-1,	row+1,	-1,	1, p));
		com = Math.max(com, combination(col,	row+1,	0,	1, p));
		com = Math.max(com, combination(col+1,	row+1,	1,	1, p));
		com = Math.max(com, combination(col+1,	row,	1,	0, p));
		return com;
	}
	
	/**
	 * Calculates the combination length
	 * @param x column
	 * @param y row
	 * @param i offset on X
	 * @param j offset on Y
	 * @return combination length.
	 */
	protected int combination(int x, int y, int i, int j, int p) {
		if(x < 0 || x >= cols) return 0;
		if(y >= rows || y < 0) return 0;
		if(grid[x][y] == p) {
			return combination(x + i, y + j, i, j, p) + 1;
		}
		return 0;
	}
}
