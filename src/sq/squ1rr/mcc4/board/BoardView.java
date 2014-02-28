/* COPYRIGHT (C) 2014 Aleksandr Belkin. All Rights Reserved. */
package sq.squ1rr.mcc4.board;

import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

/**
 * Displays OpenGL windows on which renderer renders the game itself.
 * @author Aleksandr Belkin
 */
@SuppressLint("ViewConstructor") // we don't use layout editor
public class BoardView extends GLSurfaceView {
    /** renderer */
    private final BoardRenderer renderer;
    
    /** game board, where the game is happening */
    private final GameBoard game;
    
    /**
     * Construct the view and the renderer
     * @param context
     * @param _game
     */
    public BoardView(Context context, GameBoard _game) {
        super(context);
        
        game = _game;
        
        renderer = new BoardRenderer(context, game);
        setRenderer(renderer);
        
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }
    
    /*
     * (non-Javadoc)
     * @see android.opengl.GLSurfaceView#onPause()
     */
    @Override
    public void onPause() {
        renderer.onPause();
        super.onPause();
    }
    
    /*
     * (non-Javadoc)
     * @see android.opengl.GLSurfaceView#onResume()
     */
    @Override
    public void onResume() {
        super.onResume();
        renderer.onResume();
    }
    
    /*
     * (non-Javadoc)
     * @see android.view.View#onTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if(e.getAction() == MotionEvent.ACTION_DOWN) {
            return game.onTouch(e.getX(), e.getY()); // just pass it there
        }
        
        return false;
    }
    
    /**
     * Sets whether FPS is needs to be counted
     * @param countFps
     */
    public void setCountFps(boolean countFps) {
        renderer.setCountFps(countFps);
    }
}
