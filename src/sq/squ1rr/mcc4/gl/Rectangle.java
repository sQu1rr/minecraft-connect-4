/* COPYRIGHT (C) 2014 Aleksandr Belkin. All Rights Reserved. */
package sq.squ1rr.mcc4.gl;

/**
 * Same as android.Rect but upside down, best for OpenGL. Also stores the
 * coordinates the way that is more suitable for OpenGL calculations.
 * @author Aleksandr Belkin
 */
public class Rectangle {
	/** offset on X */
	private float offsetX;
	
	/** offset on Y */
	private float offsetY;
	
	/** rectangle width */
	private float width;
	
	/** rectangle height */
	private float height;
	
	/**
	 * Create empty rectangle
	 */
	Rectangle() {
		setSize(0f, 0f);
		setOffset(0f, 0f);
	}
	
	/**
	 * Creates rectangle with zero offset
	 * @param width
	 * @param height
	 */
	public Rectangle(float width, float height) {
		setSize(width, height);
		setOffset(0f, 0f);
	}
	
	/**
	 * Creates rectangle of given size on a given offset
	 * @param ox
	 * @param oy
	 * @param w
	 * @param h
	 */
	public Rectangle(float ox, float oy, float w, float h) {
		setSize(w, h);
		setOffset(ox, oy);
	}
	
	/**
	 * Copy constructor
	 * @param rect
	 */
	public Rectangle(Rectangle rect) {
		setSize(rect.getWidth(), rect.getHeight());
		setOffset(rect.getX(), rect.getY());
	}
	
	/**
	 * Returns offset on X
	 * @return
	 */
	public float getX() {
		return offsetX;
	}
	
	/**
	 * Returns offset on Y
	 * @return
	 */
	public float getY() {
		return offsetY;
	}
	
	/**
	 * Returns width
	 * @return
	 */
	public float getWidth() {
		return width;
	}
	
	/**
	 * Returns height
	 * @return
	 */
	public float getHeight() {
		return height;
	}
	
	/**
	 * Returns X coordinate of the left line
	 * @return
	 */
	public float getLeft() {
		return offsetX;
	}
	
	/**
	 * Returns X coordinate of the right line
	 * @return
	 */
	public float getRight() {
		return offsetX + width;
	}
	
	/**
	 * Returns Y coordinate of the top line
	 * @return
	 */
	public float getTop() {
		return offsetY + height;
	}
	
	/**
	 * Returns Y coordinate of the bottom line
	 * @return
	 */
	public float getBottom() {
		return offsetY;
	}
	
	/**
	 * Sets width and height of the rectangle
	 * @param _width
	 * @param _height
	 */
	public void setSize(float _width, float _height) {
		width = _width;
		height = _height;
	}
	
	/**
	 * Sets the width of the rectangle
	 * @param _width
	 */
	public void setWidth(float _width) {
		width = _width;
	}
	
	/**
	 * Sets the height of the rectangle
	 * @param _height
	 */
	public void setHeight(float _height) {
		height = _height;
	}
	
	/**
	 * Sets X and Y offset of the rectangle
	 * @param ox
	 * @param oy
	 */
	public void setOffset(float ox, float oy) {
		offsetX = ox;
		offsetY = oy;
	}
	
	/**
	 * Sets offset of the rectangle on X
	 * @param ox
	 */
	public void setX(float ox) {
		offsetX = ox;
	}
	
	/**
	 * Sets offset of the rectangle on Y
	 * @param oy
	 */
	public void setY(float oy) {
		offsetY = oy;
	}
	
	/**
	 * Shrinks rectangles by the factor {m} with centre in the centre of the
	 * rectangle.
	 * @param m
	 */
	public void shrink(float m) {
		float newWidth = width / m;
		float newHeight = height / m;
		
		offsetX += Math.abs(width - newWidth) / 2;
		offsetY += Math.abs(height - newHeight) / 2;
		
		width = newWidth;
		height = newHeight;
	}
	
	/**
	 * Move left line of the rectangle {ex} pixels left
	 * @param ex
	 */
	public void expandLeft(float ex) {
		width += ex;
		offsetX -= ex;
	}
	
	/**
	 * Move right line of the rectangle {ex} pixels right
	 * @param ex
	 */
	public void expandRight(float ex) {
		width += ex;
	}
	
	/**
	 * Move bottom line of the rectangle {ex} pixels down
	 * @param ex
	 */
	public void expandDown(float ex) {
		height += ex;
		offsetY -= ex;
	}
	
	/**
	 * Move top line of the rectangle {ex} pixels up
	 * @param ex
	 */
	public void expandUp(float ex) {
		height += ex;
	}
	
	/**
	 * Return true if (x, y) is within the rectangle
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean contains(float x, float y) {
		if(x < getLeft() || x > getRight()) return false;
		if(y < getBottom() || y > getTop()) return false;
		return true;
	}
}
