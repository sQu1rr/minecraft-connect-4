/* COPYRIGHT (C) 2014 Aleksandr Belkin. All Rights Reserved. */
package sq.squ1rr.mcc4.board;

import javax.microedition.khronos.opengles.GL10;

import sq.squ1rr.mcc4.GameActivity;
import sq.squ1rr.mcc4.R;
import sq.squ1rr.mcc4.Stats;
import sq.squ1rr.mcc4.ai.Ai;
import sq.squ1rr.mcc4.ai.EasyAi;
import sq.squ1rr.mcc4.ai.HardAi;
import sq.squ1rr.mcc4.ai.NormalAi;
import sq.squ1rr.mcc4.ai.PeacefulAi;
import sq.squ1rr.mcc4.board.BoardLogic.Outcome;
import sq.squ1rr.mcc4.gl.Rectangle;
import sq.squ1rr.mcc4.gl.Sprite;
import sq.squ1rr.mcc4.gl.Sprite.Flip;
import sq.squ1rr.mcc4.gl.Sprite.Mode;
import sq.squ1rr.mcc4.gl.SpriteBatch;
import sq.squ1rr.mcc4.rules.GameRules;
import sq.squ1rr.mcc4.rules.GameRules.Difficulty;
import sq.squ1rr.mcc4.rules.GameRules.FirstTurn;
import sq.squ1rr.mcc4.rules.GameRules.Opponent;
import sq.squ1rr.mcc4.rules.Player;
import sq.squ1rr.mcc4.util.GlobalConstants;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View.OnClickListener;

/**
 * All the game logic and UI
 * @author Aleksandr Belkin
 */
public class GameBoard {
    /** debug tag */
    private static final String TAG = GameBoard.class.getName();
    
    /*
     * Constants
     */
    
    /** number of columns */
    private static final int COLS = 7;
    
    /** number of rows */
    private static final int ROWS = 6;
    
    /** number of rows in the header */
    private static final int HEADER_ROWS = 3;
    
    /** percentage of the space used for paddings */
    private static final int PADDING_PC = 10;
    
    
    /*
     * Sprites
     */
    /** header sprites */
    SpriteBatch header;
    
    /**
     * board and background sprites
     * [row * COLS + col] = board sprites
     * other indices are background
     */
    SpriteBatch board;
    
    /** first player tokens */
    SpriteBatch tokens1;
    
    /** second player tokens */
    SpriteBatch tokens2;
    
    /** player #1 icon */
    Sprite player1;
    
    /** player #2 icon */
    Sprite player2;
    
    /** menu button */
    Sprite menu;
    
    /*
     * Grid
     */
    
    /** grid, contains 0 for empty cell or player ID */
    int grid[][] = new int[COLS][ROWS];
    
    /** free cells in every column */
    int free[] = new int[COLS];
    
    /*
     * Members
     */
    
    /** player ID for the current turn */
    private int playerTurn;
    
    /** if the game is finished */
    private boolean finished = true;
    
    /** current status */
    private Outcome outcome = Outcome.NOTHING;
    
    /** board rectangle */
    private Rectangle boardRect = null;
    
    /** parent context */
    private final GameActivity activity;
    
    /** game rules */
    private final GameRules rules;
    
    /** board logic (winning check) */
    private final BoardLogic logic = new BoardLogic(grid);
    
    /** dialogue */
    private final BoardDialogue dialogue;
    
    /** AI */
    private final Ai ai;
    
    /** main thread handler */
    private final Handler handler = new Handler();
    
    /** screen width */
    private int width = -1;
    
    /** screen height */
    private int height = -1;
    
    /** saved game */
    private final Bundle savedGame;
    
    /** whether all values are initialised */
    private boolean created = false;
    
    /** statistics */
    private final Stats stats;

    /**
     * Create the game board and initialise the rules
     * @param _context
     * @param _rules
     */
    public GameBoard(GameActivity _context, Bundle _savedGame, GameRules _rules) {
        activity = _context;
        rules = _rules;
        savedGame = _savedGame;
        
        // statistics
        stats = new Stats(activity);
        
        // create dialogue
        dialogue = new BoardDialogue(_context);
        dialogue.setAi(rules.getRule(GameRules.OPPONENT) == Opponent.AI);
        
        // create AI if needed
        if(rules.getRule(GameRules.OPPONENT) == Opponent.AI) {
            switch(rules.getRule(GameRules.DIFFICULTY)) {
            case Difficulty.PEACEFUL:    ai = new PeacefulAi(grid);    break;
            case Difficulty.EASY:        ai = new EasyAi(grid);        break;
            case Difficulty.NORMAL:      ai = new NormalAi(grid);      break;
            case Difficulty.HARD:        ai = new HardAi(grid);        break;
            default:                     ai = null;                    break;
            }
        } else ai = null;
    }
    
    /**
     * Create the textures and the board UI
     * @param gl
     * @param _width
     * @param _height
     */
    public void onCreate(GL10 gl, int _width, int _height) {
        // set size
        width = _width;
        height = _height;
        
        // load textures
        loadTextures(gl);
        
        // initialise UI
        initialise();
        
        // playable from now on
        created = true;

        // if it is a computer turn, go ahead with it
        if(playerTurn == FirstTurn.PLAYER2 && ai != null) aiTurn();
    }
    
    /**
     * Loads textures
     * @param gl
     */
    private void loadTextures(GL10 gl) {
        Texture.PLAYER2.setResource(
            ai == null ? R.drawable.player : R.drawable.creeper
        );
        Texture.TOKEN1.setResource(rules.getRule(GameRules.TOKEN));
        Texture.TOKEN2.setResource(rules.getRule(GameRules.TOKEN2));
        
        Texture.initialise(gl, activity);
    }
    
    /**
     * Initialise the board UI
     */
    public void initialise() {
        initialise(false);
    }
    
    /**
     * Initialise the board UI
     * @param force force initialise
     */
    public void initialise(boolean force) {
        if(finished || force) {
            // clear all sprites
            tokens1 = new SpriteBatch(Texture.TOKEN1.get());
            tokens2 = new SpriteBatch(Texture.TOKEN2.get());
            
            // unfinish the game
            finished = false;
            outcome = Outcome.NOTHING;
            
            // set up the first turn
            playerTurn = rules.getRule(GameRules.FIRST_TURN);
            
            // create cells if needed
            if(board == null) createBoard(); 
            
            // update cell colour
            for(int i = 0; i < ROWS * COLS; ++i) {
                board.get(i).setColour(
                    Mode.DARK, 1f - (int)(Math.random() * 10) / 20f
                );
            }
            
            // null the grid and free counter for every column
            for(int i = 0; i < COLS; ++i) {
                for(int j = 0; j < ROWS; ++j) {
                    grid[i][j] = 0;
                }
                free[i] = ROWS;
            }
            
            if(savedGame != null) loadGame();
        } else displayDialogue();
    }
    
    /**
     * Load the game from a saved bundle
     */
    private void loadGame() {
        // load finished state
        finished = savedGame.getBoolean("finished");
        
        // load outcome
        int out = savedGame.getInt("outcome");
        if(out == 1) outcome = Outcome.DRAW;
        if(out == 2) outcome = Outcome.P1_WINS;
        if(out == 3) outcome = Outcome.P2_WINS;
        
        // load the grid
        for(int i = 0; i < grid.length; ++i) {
            grid[i] = savedGame.getIntArray("grid" + i);
        }
        
        // load the turn
        playerTurn = savedGame.getInt("turn");
        
        // put the tokens where they should be
        for(int i = 0; i < COLS; ++i) {
            free[i] = ROWS;
            for(int j = 0; j < ROWS; ++j) {                
                if(grid[i][j] != 0) {
                    putToken(i, j, grid[i][j]);
                    free[i]--;
                }
            }
        }
        
        // display dialogue (pause the game, or finish if it is the saved state)
        displayDialogue();
    }
    
    /**
     * Create playing board UI
     */
    private void createBoard() {
        // determine cell size
        int cellWidth = (int)((width - width * (float)PADDING_PC / 100) / COLS);
        int celldHeight = height / (ROWS + HEADER_ROWS);
        int cellSize = Math.min(cellWidth, celldHeight);
        
        // create board rect
        boardRect = new Rectangle(0, 0, cellSize * COLS, cellSize * ROWS);
        
        // set offsets
        float offsetX = (width - boardRect.getWidth()) / 2;
        float offsetY = (height - cellSize * HEADER_ROWS - boardRect.getHeight());
        boardRect.setOffset(offsetX, offsetY);
        
        float x = boardRect.getLeft();
        float y = boardRect.getTop() - cellSize;
        
        // create board
        Texture boardTexture = Texture.DIRT;
        board = new SpriteBatch(boardTexture.get());
        for(int j = 0; j < 6; ++j) {
            for(int i = 0; i < 7; ++i) {
                Sprite sprite = new Sprite(
                    new Rectangle(x, y, cellSize, cellSize)
                );
                sprite.setTextureTile(
                    boardTexture.getWidth(), boardTexture.getHeight(), x, y
                );
                board.add(sprite);         
                x += cellSize;
            }
            y -= cellSize;
            x = boardRect.getLeft();
        }
        
        createBackground(cellSize);
    }
    
    /**
     * Create header and background
     * @param cellSize
     */
    private void createBackground(int cellSize) {
        // header rect
        Rectangle headerRect = new Rectangle(
            boardRect.getX(), boardRect.getY() + boardRect.getHeight(),
            boardRect.getWidth(), cellSize * HEADER_ROWS
        );
        
        // expand header rect
        while(headerRect.getLeft() > 0) {
            headerRect.expandLeft(cellSize);
        }
        while(headerRect.getRight() < (float)width) {
            headerRect.expandRight(cellSize);
        }
        
        // background rect
        Rectangle backgroundRect = new Rectangle(headerRect);
        
        // expand it
        while(backgroundRect.getBottom() > 0) {
            backgroundRect.expandDown(cellSize);
        }
        
        populateBackground(headerRect, backgroundRect, cellSize);
        createHeaderUI(headerRect);
    }
    
    /**
     * Populate background with cells
     * @param headerRect
     * @param bgRect
     * @param cellSize
     */
    private void populateBackground(Rectangle headerRect,
                                    Rectangle bgRect, int cellSize) {
        // used textures
        Texture headerTexture = Texture.GRASS;
        Texture backgroundTexture = Texture.DIRT;
        
        // create batches
        header = new SpriteBatch(headerTexture.get());
        
        // calculate columns and rows
        int headerCols = (int)Math.round(headerRect.getWidth() / cellSize);
        int backgroundRows = (int)Math.round(bgRect.getHeight() / cellSize);
        
        // randomly generate header
        int[] heights = new int[headerCols];
        
        for(int i = 0; i < heights.length; ++i) {
            int height = (int)(Math.random() * 5) + 1;
            if(height == 5) height = 3; // 3 cells height
            else if(height > 1) height = 2; // 2 cells height
            // else 1 cell height
            heights[i] = height;
        }
        
        // calculate how many columns were added from each side of the screen
        int addCol = (headerCols - COLS) / 2;
        
        // create the UI
        Texture texture;
        for(int col = 0; col < headerCols; ++col) {
            float x = headerRect.getLeft() + cellSize * col;
            float y = headerRect.getTop() - cellSize;
            
            for(int row = 0; row < backgroundRows; ++row) {
                if(row < heights[col]) texture = headerTexture;
                else texture = backgroundTexture;
                
                if(row == HEADER_ROWS && col >= addCol &&
                   col < headerCols - addCol) {
                    row += ROWS;
                    y -= cellSize * ROWS;
                }
                
                Sprite sprite = new Sprite(
                    new Rectangle(x, y, cellSize, cellSize)
                );
                sprite.setTextureTile(
                    texture.getWidth(), texture.getHeight(),
                    x, y
                );
                
                if(texture == backgroundTexture) {
                    float colour = .3f - (int)(Math.random() * 4) / 20f;
                    sprite.setColour(Mode.DARK, colour);
                    board.add(sprite);
                } else {
                    float add = (int)(Math.random() * 11) / 100f;
                    sprite.setColour(Mode.BRIGHT, add);
                    header.add(sprite);
                }
                
                y -= cellSize;
            }
        }
    }
    
    /**
     * Create menu and player icons
     * @param headerRect
     */
    public void createHeaderUI(Rectangle headerRect) {
        // player #1
        Rectangle p1 = new Rectangle(headerRect);
            p1.shrink(1.2f);
            p1.setX(0f - Texture.PLAYER1.getWidth() / 5);
            p1.setWidth(p1.getHeight());
        player1 = new Sprite(p1);
        player1.setTextureId(Texture.PLAYER1.get());
        
        // player #2
        Rectangle p2 = new Rectangle(p1);
            p2.setX(width - p2.getWidth() + Texture.PLAYER2.getWidth() / 9);
        player2 = new Sprite(p2);
        player2.setTextureId(Texture.PLAYER2.get());
        player2.flip(Flip.HORIZONTALLY);
        
        // menu
        float menuSize = width / 5;
        Rectangle menuRect = new Rectangle(
            (width - menuSize) / 2, height - menuSize, menuSize, menuSize
        );
        menu = new Sprite(menuRect);
        menu.setTextureId(Texture.MENU.get());
    }
    
    /**
     * Dismiss the dialogue
     */
    public void onDestroy() {
        dialogue.dismiss();
    }    
    
    /**
     * Create token and darken the current cell
     * @param col
     * @param row
     * @param player
     */
    public void putToken(int col, int row, int player) {
        // get sprite
        Sprite sprite = board.get(row * COLS + col);
        
        // make it darker
        sprite.setColour(Mode.DARK, 0.35f);
        
        // create token from the current sprite
        Rectangle tokenRect = new Rectangle(sprite.getRect());
            tokenRect.shrink(1.5f);
        Sprite token = new Sprite(tokenRect);
        
        if(player == Player.PLAYER1) tokens1.add(token);
        else tokens2.add(token);
    }
    
    /**
     * Export info to a bundle
     * @param bundle
     * @return
     */
    public Bundle exportTo(Bundle bundle) {
        // export grid
        for(int i = 0; i < grid.length; ++i) {
            bundle.putIntArray("grid" + i, grid[i]);
        }
        
        // convert enum to int
        int out = 0;
        if(outcome == Outcome.DRAW) out = 1;
        if(outcome == Outcome.P1_WINS) out = 2;
        if(outcome == Outcome.P2_WINS) out = 3;
        
        // export other info
        bundle.putInt("turn", playerTurn);
        bundle.putBoolean("finished", finished);
        bundle.putInt("outcome", out);
        
        return bundle;
    }
    
    public void draw(GL10 gl) {
        bind(gl);
        
        // draw header
        header.draw(gl);
        
        // draw board (and background)
        board.draw(gl);
        
        // draw tokens of a player #1
        tokens1.draw(gl);
        
        // draw tokens of a player #2
        tokens2.draw(gl);

        // draw icons
        player1.draw(gl);
        player2.draw(gl);
        
        // draw menu
        menu.draw(gl);
        
        unbind(gl);
    }
    
    /**
     * Enable appropriate flags
     * @param gl
     */
    private void bind(GL10 gl) {
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        
        gl.glFrontFace(GL10.GL_CW);
    }
    
    /**
     * Disable flags
     * @param gl
     */
    private void unbind(GL10 gl) {
        gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
        
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisable(GL10.GL_BLEND);
    }
    
    /**
     * Put token into a column
     * @param column
     */
    private void selectColumn(int column) {
        if(free[column] == 0 || finished) {
            if(GlobalConstants.DEBUG) {
                Log.e(TAG, "full column or game is finished");
            }
            return;
        }
        
        // decrement free space in this column
        free[column]--;
        
        // put token
        putToken(column, free[column], playerTurn);
        
        // set who put the token
        grid[column][free[column]] = playerTurn;
        
        // switch player
        playerTurn = playerTurn == Player.PLAYER1
                ? Player.PLAYER2 : Player.PLAYER1;
        
        // check if someone has won
        runLogic();
        
        // AI move if needed
        if(playerTurn == Player.PLAYER2 && ai != null) aiTurn();
    }
    
    /**
     * Convert coordinates into column number and put token in there
     * @param x
     */
    public void selectColumnAt(float x) {
        float cellSize = boardRect.getWidth() / COLS;
        selectColumn((int)((x - boardRect.getLeft()) / cellSize));
    }
    
    /**
     * AI move
     */
    private void aiTurn() {
        if(finished) return;
        handler.postDelayed(aiLogic, GlobalConstants.AI_DELAY);
    }
    
    /**
     * Returns true if AI is thinking at the moment
     * @return
     */
    private boolean isAiTurn() {
        return ai != null && playerTurn == FirstTurn.PLAYER2;
    }
    
    /**
     * Check if someone has won
     */
    private void runLogic() {
        outcome = logic.run();

        if(outcome != Outcome.NOTHING) {
            stats.add(Stats.STATS_GAMES, 1);
            stats.add(Stats.STATS_TIME, activity.getTimePlayed());
            
            if(ai != null) {
                if(outcome == Outcome.DRAW) {
                    stats.add(Stats.STATS_DRAWS, 1);
                }
                if(outcome == Outcome.P1_WINS) {
                    stats.add(Stats.STATS_WINS, 1);
                }
                if(outcome == Outcome.P2_WINS) {
                    stats.add(Stats.STATS_DEFEATS, 1);
                }
            } else {
                stats.add(Stats.STATS_PVPS, 1);
            }
            
            finished = true;
            if(outcome != Outcome.DRAW) {
                for(Sprite s : logic.getWinSprites(board)) {
                    s.setColour(Mode.BRIGHT, 0.5f);
                }
            }
            displayDialogue(GlobalConstants.WIN_DELAY);
        }
    }
    
    /**
     * Displays the in-game dialogue
     */
    public void displayDialogue() {
        displayDialogue(0);
    }
    
    /**
     * Displays the in-game dialogue with delay
     * @param delay
     */
    public void displayDialogue(int delay) {
        handler.postDelayed(runDisplayDialogue, delay); // always on a main thread
    }
    
    /**
     * Sets the listener for some of the dialogue buttons
     * @param listener
     */
    public void setDialogueListener(OnClickListener listener) {
        dialogue.setOnClickListener(listener);
    }
    
    
    /**
     * Hides the dialogue
     */
    public void hideDialogue() {
        dialogue.hide();
    }
    
    /**
     * Handles on touch event
     * @param x
     * @param y
     * @return
     */
    public boolean onTouch(float x, float y) {
        if(!created) return true; // ignore
        
        if(boardRect.contains(x, (float)height - y)) {
            if(!isAiTurn()) selectColumnAt(x);
        } else if(menu.getRect().contains(x, (float)height - y)) {
            displayDialogue();
        } else return false; // don't care about other stuff
        return true;
    }
    
    /**
     * Runs AI after a delay
     */
    private Runnable aiLogic = new Runnable(){
        @Override
        public void run() {
            synchronized(this) {
                try {
                    wait(GlobalConstants.AI_DELAY);
                } catch(InterruptedException e) {
                    if(GlobalConstants.DEBUG) {
                        Log.e(TAG, "Can't wait", e);
                    }
                }
            }
            selectColumn(ai.run());
        }
    };
    
    /**
     * Display dialogue runnable
     */
    private Runnable runDisplayDialogue = new Runnable() {
        @Override
        public void run() {
            dialogue.setOutcome(outcome);
            dialogue.show();
        }
    };
}
