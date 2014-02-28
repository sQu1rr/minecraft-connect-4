/* COPYRIGHT (C) 2014 Aleksandr Belkin. All Rights Reserved. */
package sq.squ1rr.mcc4.board;

import java.util.ArrayList;

import sq.squ1rr.mcc4.gl.Sprite;
import sq.squ1rr.mcc4.gl.SpriteBatch;
import sq.squ1rr.mcc4.rules.GameRules.FirstTurn;

/**
 * Decides if one of the players has won. Doesn't support calculating of the
 * definite winnings that are yet to come.
 * @author Aleksandr Belkin
 */
public class BoardLogic {
    /** Possible outcomes */
    public enum Outcome {
        NOTHING, DRAW, P1_WINS, P2_WINS;
    }
    
    /** tokens we need to win */
    private static final int WIN = 4;
    
    /*
     * Array constants (directions: top, left, top-left, top-right, total of 4)
     */
    private static final int T        = 0;
    private static final int L        = 1;
    private static final int TL       = 2;
    private static final int TR       = 3;
    private static final int SIDES    = 4;
    
    /** Reference to a main grid */
    private final int[][] grid;
    
    /** Calculations are happening on this grid [x][y][direction] */
    private final int[][][] calc;
    
    /** number of columns in the grid */
    private final int cols;
    
    /** number of rows in the grid */
    private final int rows;
    
    /** winner combination X */
    private int winX;
    
    /** winner combination Y */
    private int winY;
    
    /** winner combination direction */
    private int winD;
    
    /**
     * Initialise members
     * @param _grid
     */
    public BoardLogic(int[][] _grid) {
        grid = _grid;
        cols = _grid.length;
        rows = _grid[0].length;
        
        calc = new int[cols][rows][SIDES];
    }
    
    /**
     * Run the logic calculation
     * @return
     */
    public Outcome run() {
        // set draw to true for now
        boolean draw = true;
        
        for(int j = 0; j < rows; ++j) {
            for(int i = 0; i < cols; ++i) {
                // save player id
                int p = grid[i][j];
                
                if(p == 0) {
                    // empty cell means board is not full
                    if(draw) draw = false;
                    continue;
                }
                
                for(int t = 0; t < SIDES; ++t) {
                    // cell is not empty, so base value of it is 1
                    calc[i][j][t] = 1;
                }
                
                // check upper cells
                if(j > 0) {
                    if(grid[i][j-1] == p) {
                        calc[i][j][T] = Math.max(
                            calc[i][j][T], calc[i][j-1][T] + 1
                        );
                    }
                    if(i > 0 && grid[i-1][j-1] == p) {
                        calc[i][j][TL] = Math.max(
                            calc[i][j][TL], calc[i-1][j-1][TL] + 1
                        );
                    }
                    if(i < cols - 1 && grid[i+1][j-1] == p) {
                        calc[i][j][TR] = Math.max(
                            calc[i][j][TR], calc[i+1][j-1][TR] + 1
                        );
                    }
                }
                
                // check left cell
                if(i > 0 && grid[i-1][j] == p) {
                    calc[i][j][L] = Math.max(
                        calc[i][j][L], calc[i-1][j][L] + 1
                    );
                }
                
                // see if someone has won, terminate if so
                for(int t = 0; t < SIDES; ++t) {
                    if(calc[i][j][t] == WIN) {
                        winX = i;
                        winY = j;
                        winD = t;
                        
                        if(p == FirstTurn.PLAYER1) return Outcome.P1_WINS;
                        else return Outcome.P2_WINS;
                    }
                }
            }
        }
        
        // nobody won, return draw if it is, nothing if it's not
        return draw ? Outcome.DRAW : Outcome.NOTHING;
    }
    
    /**
     * Returns sprites of a winning combination
     * @param board
     * @return
     */
    public ArrayList<Sprite> getWinSprites(SpriteBatch board) {
        ArrayList<Sprite> combination = new ArrayList<Sprite>();
        
        // offsets
        int ox = 0;
        int oy = 0;
        
        if(winD == L) ox = -1;
        else {
            oy = -1;
            if(winD == TL) ox = -1;
            else if(winD == TR) ox = 1;
        }
        
        int x = winX;
        int y = winY;
        
        for(int i = 0; i < WIN; ++i, x += ox, y += oy) {
            combination.add(board.get(y * cols + x));
        }
        
        return combination;
    }
}
