/* COPYRIGHT (C) 2014 Aleksandr Belkin. All Rights Reserved. */
package sq.squ1rr.mcc4.gl;

import javax.microedition.khronos.opengles.GL10;

import sq.squ1rr.mcc4.layout.McStyle;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.opengl.GLUtils;

/**
 * OpenGL ES 1.0 dynamic font rendering system.
 * It loads actual font generates a font map (texture), and allows rendering of
 * text strings.
 * 
 * Note: based one following tutorial:
 * fractiousg.blogspot.co.uk/2012/04/rendering-text-in-opengl-on-android.html
 * 
 * @author Aleksandr Belkin
 */
public class SpriteString {
    /*
     * Character constants
     */
    /** first usable character */
    private final static int CHAR_START = ' '; // #32
    
    /** last usable character */
    private final static int CHAR_END = '~'; // #126
    
    /** character used for unknown symbols (must be in usable range) */
    private final static int CHAR_UNKNOWN = '?'; // #63
    
    /** total count of characters */
    private final static int CHAR_COUNT = (CHAR_END - CHAR_START + 1);

    /*
     * Members
     */
    /** GL instance */
    private final GL10 gl;
    
    /** batch renderer */
    private SpriteBatch batch;
    
    /** activity context */
    private final Context context;

    /** font actual height */
    private float fontHeight = 0;
    
    /** font height above baseline */
    private float fontAscent = 0;
    
    /** font height below baseline */
    private float fontDescent = 0;

    /** texture ID */
    private int textureId = -1;
    
    /** texture size */
    private int textureSize = 0;

    /** widths of every character */
    private final float[] charWidths = new float[CHAR_COUNT];
    
    /** character cell width (of widest character) */
    private int cellWidth = 0;
    
    /** character cell height */
    private int cellHeight = 0;
    
    /** X offset on texture for a letter */
    private final int[] offsetX = new int[CHAR_COUNT];
    
    /** Y offset on texture for a letter */
    private final int[] offsetY = new int[CHAR_COUNT];


    /**
     * Create new GL Text
     * @param _gl
     * @param _context
     */
    public SpriteString(GL10 _gl, Context _context) {
        gl = _gl;
        context = _context;
    }

    /**
     * Get length of a string in pixels
     * @param text
     * @return
     */
    public int getLength(String text) {
        int len = 0;
        for(char c : text.toCharArray()) len += getCharWidth(c);
        return len;
    }

    /**
     * Returns character width in pixels
     * @param chr
     * @return
     */
    public int getCharWidth(char c)  {
        int index = c - CHAR_START;
        if(index < 0 || index >= CHAR_COUNT) {
            index = CHAR_UNKNOWN - CHAR_START;
        }
        return Math.round(charWidths[index]);
    }

    /**
     * Return font ascent in pixels
     * @return
     */
    public float getAscent()  {
        return fontAscent;
    }
    
    /**
     * Return font descent in pixels
     * @return
     */
    public float getDescent()  {
        return fontDescent;
    }

    /**
     * Return font height in pixels
     * @return
     */
    public float getHeight() {
        return fontHeight;
    }

    /**
     * This will create a texture for the Minecraftia font with a specified
     * font size
     * @param size in px
     */
    public void load(int size) {
        // font paint
        Paint paint = new Paint();
        paint.setTextSize(size);
        paint.setTypeface(McStyle.getFont(context));
        
        // get font metrics
        getFontMetrics(paint);
        
        // shadow offset
        int shadowOffset = initialiseShadow(size);

        // list all characters
        char[] chars = new char[CHAR_COUNT];
        for(int i = 0; i < CHAR_COUNT; ++i) chars[i] = (char)(CHAR_START + i);
        
        // set cell sizes
        cellWidth = (int)Math.round(maxWidthChar(chars, paint));
        cellHeight = (int)Math.round(fontHeight);
        
        calculateTextureSize();

        // generate a new texture
        int[] textureIds = new int[1];
        gl.glGenTextures(1, textureIds, 0);
        textureId = textureIds[0];
        
        // create an empty bitmap (alpha only)
        Bitmap bitmap = Bitmap.createBitmap(
            textureSize, textureSize, Bitmap.Config.ARGB_8888
        );
        Canvas canvas = new Canvas(bitmap);
        bitmap.eraseColor(Color.TRANSPARENT);
    
        // build the font map
        buildFontMap(chars, canvas, paint, shadowOffset);
        
        // create batch
        batch = new SpriteBatch(textureId);

        initialiseFilters();
        
        // load the generated bitmap onto the texture
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        
        // clean up
        gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
        bitmap.recycle();
    }
    
    /**
     * Extract font metrics
     * @param paint
     */
    private void getFontMetrics(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        fontHeight = (float)Math.ceil(Math.abs(fm.bottom) + Math.abs(fm.top));
        fontAscent = (float)Math.ceil(Math.abs(fm.ascent));
        fontDescent = (float)Math.ceil(Math.abs(fm.descent));
    }
    
    /**
     * Size the shadow
     * @param size
     * @return shadow offset (x and y)
     */
    private int initialiseShadow(int size) {
        int shadowOffset = size / 12;
        if(shadowOffset == 0) shadowOffset = 1;
        
        fontHeight += shadowOffset;
        fontDescent += shadowOffset;
        
        return shadowOffset;
    }
    
    /**
     * Calculates width for all the characters and saves into charWidths.
     * Returns widest char width.
     * @param chars
     * @param paint
     * @return
     */
    private float maxWidthChar(char[] chars, Paint paint) {
        paint.getTextWidths(chars, 0, CHAR_COUNT, charWidths);
        
        float max = 0f;
        for(float val : charWidths) if(max < val) max = val;
                
        return max;
    }
    
    /**
     * Calculate texture size based on cell height and width
     */
    private void calculateTextureSize() {
        int maxSize = cellWidth > cellHeight ? cellWidth : cellHeight;

        // set up texture size (fixed on CHAR_COUNT, so don't change it)
        if(maxSize <= 24) textureSize = 256;
        else if(maxSize <= 40) textureSize = 512;
        else if(maxSize <= 80) textureSize = 1024;
        else textureSize = 2048;
    }
    
    /**
     * Initialise GL filters for texture
     */
    private void initialiseFilters() {
        // setup filters for texture
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
        
        // resize filters
        gl.glTexParameterx(
            GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST
        );
        gl.glTexParameterx(
            GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR
        ); 
        
        // UVW Wrapping
        gl.glTexParameterx(
            GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE
        );
        gl.glTexParameterx(
            GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE
        );
    }
    
    /**
     * Build font map and assign texture areas
     * @param chars
     * @param canvas
     * @param paint
     */
    private void buildFontMap(char[] chars, Canvas canvas, Paint paint,
                              int shadowOffset) {
        int x = 0;
        int y = (int)(cellHeight - fontDescent);
        int yUp = 0;
        
        for(int i = 0; i < chars.length; ++i) {
            // draw character on a map
            paint.setColor(McStyle.TEXT_SHADOW_COLOUR);
            canvas.drawText(
                chars, i, 1, x + shadowOffset, y + shadowOffset, paint
            );
            
            // draw character on a map
            paint.setColor(Color.WHITE);
            canvas.drawText(chars, i, 1, x, y, paint);
            
            // create texture region
            offsetX[i] = x;
            offsetY[i] = yUp;
            
            // move one cell right
            x += cellWidth;
            
            // if out of boundaries, move down
            if(x + cellWidth > textureSize) {
                x = 0;
                y += cellHeight;
                yUp += cellHeight;
            }
        }
    }

    /**
     * Draw text on given coordinates
     * @param text
     * @param x
     * @param y
     */
    public void draw(String text, float x, float y)  {        
        int len = text.length();
        
        batch.clear();
        
        for(int i = 0; i < len; i++)  {
            int c = text.charAt(i) - CHAR_START;
            if(c < 0 || c >= CHAR_COUNT) c = CHAR_UNKNOWN - CHAR_START;
            
            Sprite sprite = new Sprite(
                new Rectangle(x, y, cellWidth, cellHeight)
            );
            sprite.setTextureTile(textureSize, textureSize, offsetX[c], offsetY[c]);
            batch.add(sprite);
            
            x += charWidths[c];
        }
        
        batch.draw(gl);
    }
}