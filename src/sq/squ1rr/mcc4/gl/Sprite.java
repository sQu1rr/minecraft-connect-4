/* COPYRIGHT (C) 2014 Aleksandr Belkin. All Rights Reserved. */
package sq.squ1rr.mcc4.gl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Represents a simple rectangular sprite, that provides functionality to
 * set a texture or make the sprite brighter/darker
 * @author Aleksandr Belkin
 */
public class Sprite {
	/**
	 * Colour Mode
	 * @author squ1rr
	 */
	public enum Mode {
		NORMAL, DARK, BRIGHT
	}
	
	/** default vertices */
	private float vertices[] = {
			-1.0f,	-1.0f,	0.0f,
			-1.0f,	1.0f,	0.0f,
			1.0f,	-1.0f,	0.0f,
			1.0f,	1.0f,	0.0f
	};

	/** default (stretched) texture */
	private float texture[] = {    		
			0.0f, 1.0f,
			0.0f, 0.0f,
			1.0f, 1.0f,
			1.0f, 0.0f
	};
	
	/** vertex buffer */
	private FloatBuffer vertexBuffer;
	
	/** texture buffer */
	private FloatBuffer textureBuffer;
	
	/** sprite rect */
	private Rectangle rect;
	
	/** the ID of the texture used, 0 if not used */
	private int textureId = 0;
	
	/** brighter/darker mode */
	private int mode = GL10.GL_REPLACE;
	
	/** colour used in with a mode */
	private float colour = 1f;
	
	/**
	 * Create a new sprite (stretched)
	 */
	public Sprite() {
		setTextureStretched();
	}
	
	/**
	 * Create a new sprite from a rect (stretched)
	 * @param rect
	 */
	public Sprite(Rectangle rect) {
		setRect(rect);
		setTextureStretched();
	}
	
	/**
	 * Set sprite rect
	 * @param _rect
	 */
	public void setRect(Rectangle _rect) {
		rect = _rect;
		
		vertices[0] = vertices[3] = rect.getLeft();
		vertices[6] = vertices[9] = rect.getRight();
		vertices[1] = vertices[7] = rect.getBottom();
		vertices[4] = vertices[10] = rect.getTop();
		
		initialiseVertices();
	}
	
	/**
	 * Set texture mode tile
	 * @param _width
	 * @param _height
	 */
	public void setTextureTile(int _width, int _height) {
		setTextureTile(_width, _height, 0, 0);
	}
	
	/**
	 * Set texture mode tile with custom offset
	 * @param _width
	 * @param _height
	 * @param x
	 * @param y
	 */
	public void setTextureTile(int _width, int _height, float x, float y) {
		// calculate UV coordinates
		float u = rect.getWidth() / _width;
		float v = rect.getHeight() / _height;
		
		// calculate offset
		x /= _width;
		y /= _height;
		
		// apply coordinates
		texture[0] = texture[2] = x;
		texture[3] = texture[7] = y;
		texture[4] = texture[6] = u + x;
		texture[1] = texture[5] = v + y;
		
		initialiseTexture();
	}
	
	/**
	 * Sets texture stretched
	 */
	public void setTextureStretched() {
		texture[0] = texture[2] = texture[3] = texture[7] = 0f;
		texture[1] = texture[4] = texture[5] = texture[6] = 1f;
		
		initialiseTexture();
	}
	
	/**
	 * Sets texture ID
	 * @param id
	 */
	public void setTextureId(int id) {
		textureId = id;
	}
	
	/**
	 * Set colour mode only
	 * @param _mode
	 */
	public void setColour(Mode _mode) {
		switch(_mode) {
		case NORMAL: mode = GL10.GL_REPLACE; break;
		case DARK: mode = GL10.GL_MODULATE; break;
		case BRIGHT: mode = GL10.GL_ADD; break;
		}
	}
	
	/**
	 * Set colour for colour mode only (colour is actually a shade black..white)
	 * @param _colour
	 */
	public void setColour(float _colour) {
		colour = _colour;
	}
	
	/**
	 * Set colour mode and colour (colour is actually a shade black..white)
	 * @param mode
	 * @param colour
	 */
	public void setColour(Mode mode, float colour) {
		setColour(mode);
		setColour(colour);
	}
	
	/**
	 * Returns the sprite rect (if it is changed the sprite won't move)
	 * unless setRect is explicitly called
	 * @return
	 */
	public Rectangle getRect() {
		return rect;
	}
	
	/**
	 * Initialise the vertices, create a buffer
	 */
	private void initialiseVertices() {
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * 4); 
		byteBuffer.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuffer.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
	}
	
	/**
	 * Initialise texture UV map, create a buffer
	 */
	private void initialiseTexture() {
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(texture.length * 4);
		byteBuffer.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuffer.asFloatBuffer();
		textureBuffer.put(texture);
		textureBuffer.position(0);
	}
	
	/**
	 * Draw this particular sprite, assumes that the following flags are ON:
	 * GL_TEXTURE_2D
	 * GL_BLEND
	 * GL_SRC_ALPHA (GL_ONE_MINUS_SRC_ALPHA)
	 * GL_VERTEX_ARRAY
	 * GL_TEXTURE_COORD_ARRAY
	 * GL_CW
	 * and if texture is not set, it should be bind beforehand
	 * @param gl
	 */
	public void draw(GL10 gl) {
		// bind texture if given
		if(textureId != 0) gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
		
		// set up buffers
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
		
		// apply colour mode
		gl.glTexEnvx(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, mode);
		gl.glColor4f(colour, colour, colour, 1f);		

		// draw
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 3);
	}
}
