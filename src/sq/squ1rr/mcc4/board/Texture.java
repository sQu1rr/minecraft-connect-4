/* COPYRIGHT (C) 2014 Aleksandr Belkin. All Rights Reserved. */
package sq.squ1rr.mcc4.board;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import sq.squ1rr.mcc4.R;

/**
 * Initialises textures and saves some info about them.
 * @author Aleksandr Belkin
 */
public enum Texture {
	MENU	(R.drawable.menu),			// menu button
	DIRT	(R.drawable.dirt_dark_hd),	// dirt texture
	GRASS	(R.drawable.grass_hd),		// grass texture
	PLAYER1	(R.drawable.player),		// player sprite
	PLAYER2	(-1),						// opponent sprite (defined later)
	TOKEN1	(-1),						// player #1 token (defined later)
	TOKEN2	(-1);						// player #2 token (defined later)
	
	/** hold resource ID for the texture */
	private int resourceId;
	
	private int textureId;
	
	private int width;
	private int height;
	
	/**
	 * Constructor
	 * @param _resourceId
	 */
	Texture(int _resourceId) {
		resourceId = _resourceId;
	}
	
	/**
	 * Set custom resource ID
	 * @param _resourceId
	 */
	public void setResource(int _resourceId) {
		resourceId = _resourceId;
	}
	
	/**
	 * Returns resource ID
	 * @return
	 */
	public int getResource() {
		return resourceId;
	}
	
	/**
	 * Loads texture from resources to OpenGL texture
	 * @param gl
	 * @param context
	 * @param _textureId
	 */
	public void load(GL10 gl, Context context, int _textureId) {
		textureId = _textureId;
		
		// bind texture
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
		
		// create nearest filtered texture
		gl.glTexParameterf(
			GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST
		);
		gl.glTexParameterf(
			GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR
		);
		
		// load bitmap
		Bitmap bitmap = BitmapFactory.decodeResource(
			context.getResources(), resourceId
		);
		
		// create two-dimensional texture image from our bitmap 
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		
		width = bitmap.getWidth();
		height = bitmap.getHeight();
		
		// Clean up
		bitmap.recycle();
	}
	
	/**
	 * Sets texture ID
	 * @param _textureId
	 */
	public void set(int _textureId) {
		textureId = _textureId;
	}
	
	/**
	 * Returns texture ID
	 * @return
	 */
	public int get() {
		return textureId;
	}
	
	/**
	 * Returns height of the texture
	 * @return
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Returns width of the texture
	 * @return
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Initialises all the textures provided that all of them do have
	 * resources associated
	 * @param gl
	 * @param context
	 */
	public static void initialise(GL10 gl, Context context) {
		int[] textures = new int[Texture.values().length];
		
		Texture[] values = Texture.values();
		
		// generate one texture pointer
		gl.glGenTextures(values.length, textures, 0);
		
		for(int i = 0; i < values.length; ++i) {
			values[i].load(gl, context, textures[i]);
		}
	}
}