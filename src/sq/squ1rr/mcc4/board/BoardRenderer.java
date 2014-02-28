/* COPYRIGHT (C) 2014 Aleksandr Belkin. All Rights Reserved. */
package sq.squ1rr.mcc4.board;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import sq.squ1rr.mcc4.gl.SpriteString;
import sq.squ1rr.mcc4.layout.McStyle;
import sq.squ1rr.mcc4.util.GlobalConstants;
import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;
 
/**
 * Renders the board on a view.
 * @author Aleksandr Belkin
 */
public class BoardRenderer implements Renderer {
	/** debugging tag */
	private static final String TAG = BoardRenderer.class.getName();
	
	/** parent context */
	private final Context context;
	
	/** game board */
	private final GameBoard game;
    
	/*
	 * Size
	 */
    private int width = -1;
    private int height = -1;
    
    /*
     * FPS Counter
     */
    private boolean countFps = false;
    private long lastTime;
    private int fps = 0;
    private int frames = 0;
    
    /** FPS counter text */
    private SpriteString glText;
    
    /** if surfaced is fully initialised (not the game though) */
    private boolean surfaceCreated = false;
    
    /** is it paused */
    private boolean pause = false;
 
    /**
     * Creates new renderer
     * @param _context
     * @param _game
     */
    public BoardRenderer(Context _context, GameBoard _game) {
    	context = _context;
    	game = _game;

    	// set last time to current time
    	lastTime = System.currentTimeMillis();
    }
 
    /*
     * (non-Javadoc)
     * @see android.opengl.GLSurfaceView.Renderer#onSurfaceCreated(javax.microedition.khronos.opengles.GL10, javax.microedition.khronos.egl.EGLConfig)
     */
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        if(GlobalConstants.DEBUG) {
            Log.i(TAG, "Surface created");
        }
        
        // set up random (black in this case) clear colour
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        
        // we will use 2D textures
        gl.glEnable(GL10.GL_TEXTURE_2D);
        
        // FPS counter text
        glText = new SpriteString(gl, context);
    	glText.load((int)McStyle.getTextSize(context));
        
        surfaceCreated = true;
        width = -1;
        height = -1;
    }
 
    /*
     * (non-Javadoc)
     * @see android.opengl.GLSurfaceView.Renderer#onSurfaceChanged(javax.microedition.khronos.opengles.GL10, int, int)
     */
    @Override
    public void onSurfaceChanged(GL10 gl, int _width, int _height) {
        if(!surfaceCreated && width == _width && height == _height) {
            if(GlobalConstants.DEBUG) {
                Log.i(TAG, "Surface changed but already handled");
            }
            return;
        }
 
        width = _width;
        height = _height;
        
        // set up new size
        gl.glViewport(0, 0, width, height);

        // setup orthographic projection
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrthof(0, width, 0, height, 1.0f, -1.0f);
        
        // handle this event in game class
        game.onCreate(gl, width, height);
        
        // open up for size change events
        surfaceCreated = false;
    }
 
    /*
     * (non-Javadoc)
     * @see android.opengl.GLSurfaceView.Renderer#onDrawFrame(javax.microedition.khronos.opengles.GL10)
     */
    @Override
    public void onDrawFrame(GL10 gl) { 
    	if(pause) return; // shouldn't be executed anyway, but lets be safe
    	
    	// clear the screen
    	gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
    	// draw the game
		game.draw(gl);		
    	
		// show the FPS
        if(countFps) {
            frames++;
            long currentTime = System.currentTimeMillis();
            if(currentTime - lastTime >= 1000) {
            	fps = frames;
            	frames = 0;
                lastTime = currentTime;
            }
    		
            gl.glEnable(GL10.GL_BLEND);
    		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
    		
    		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
    		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    		
    		gl.glFrontFace(GL10.GL_CW);

            glText.draw("" + fps, 8, getHeight() - glText.getHeight() - 8);
            
            gl.glDisable(GL10.GL_BLEND);
        }
    }
 
    /**
     * Returns the current FPS
     * @return
     */
    public int getFPS() {
        return fps;
    }
    
    /**
     * Sets the pause boolean to true
     */
    public void onPause() {
    	pause = true;
    }
    
    /**
     * Sets the pause boolean to false
     */
    public void onResume() {
    	pause = false;
    }
    
    /**
     * Returns current viewport width
     * @return
     */
    public int getWidth() {
    	return width;
    }
    
    /**
     * Returns current viewport height
     * @return
     */
    public int getHeight() {
    	return height;
    }
    
    /**
     * Change state of FPS counter
     * @param _countFps
     */
    public void setCountFps(boolean _countFps) {
    	countFps = _countFps;
    }
}