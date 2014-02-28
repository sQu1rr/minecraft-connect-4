/* COPYRIGHT (C) 2014 Aleksandr Belkin. All Rights Reserved. */
package sq.squ1rr.mcc4.ai;

/**
 * The most basic AI, just returns the random column (but the legal move)
 * @author Aleksandr Belkin
 */
public class PeacefulAi implements Ai {
    /** reference to the main grid */
    protected final int[][] grid;
    
    /**
     * Create AI
     * @param _grid
     */
    public PeacefulAi(int[][] _grid) {
        grid = _grid;
    }

    /*
     * (non-Javadoc)
     * @see ai.Ai#run()
     */
    @Override
    public int run() {
        // random column
        int col = (int)(Math.random() * grid.length);
        
        while(grid[col][0] != 0) {
            // increment it if not possible to put chip there
            col = (col + 1) % grid.length;
        }
        return col;
    }
}
