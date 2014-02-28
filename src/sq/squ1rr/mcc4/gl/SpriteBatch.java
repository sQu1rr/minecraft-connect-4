/* COPYRIGHT (C) 2014 Aleksandr Belkin. All Rights Reserved. */
package sq.squ1rr.mcc4.gl;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

/**
 * Draws the sprites with a single texture
 * @author Aleksandr Belkin
 */
public class SpriteBatch extends ArrayList<Sprite> {
	/** serialisation ID */
	private static final long serialVersionUID = 1L;
	
	/** texture with which the sprites are drawn */
	private final int textureId;
	
	/**
	 * Creates a batch that uses a particular texture
	 * @param _textureId
	 */
	public SpriteBatch(int _textureId) {
		textureId = _textureId;
	}
	
	/**
	 * Draws every element of the batch
	 * @param gl
	 */
	public void draw(GL10 gl) {
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
		synchronized(this) { // thread safe
			for(Sprite s : this) s.draw(gl);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.util.ArrayList#get(int)
	 */
	@Override
	public Sprite get(int index) {
		synchronized(this) { // thread safe
			return super.get(index);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.util.ArrayList#add(java.lang.Object)
	 */
	@Override
	public boolean add(Sprite sprite) {
		synchronized(this) { // thread safe
			return super.add(sprite);
		}
	}
}
