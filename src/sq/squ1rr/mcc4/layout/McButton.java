/* COPYRIGHT (C) 2014 Aleksandr Belkin. All Rights Reserved. */
package sq.squ1rr.mcc4.layout;

import sq.squ1rr.mcc4.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.widget.Button;

/**
 * Base class for Minecraft-looking buttons.
 * @author Aleksandr Belkin
 */
public class McButton extends Button {
	/** background image */
	private static Bitmap backgroundImage = null;
	
	/** stroke paint */
	private Paint stroke = new Paint();;
	
	/** fill paint */
	private Paint fill = new Paint();;
	
	/** text font and style */
	private Paint text = new Paint();;
	
	/** background image */
	private BitmapDrawable background = null;

	/**
	 * Initialise button and its members
	 * @param context
	 */
	public McButton(Context context) {
		super(context);
		initialise();
	}
	
	/**
	 * Initialise members (both static and local)
	 * @param context
	 */
	private void initialise() {
		// stroke paint
		stroke.setStyle(Style.STROKE);
		stroke.setStrokeWidth(McStyle.getButtonUnit(getContext()));
		
		// fill paint
		fill.setStyle(Style.FILL);

		// text paint
		text.setTextAlign(Align.CENTER);
		text.setTypeface(McStyle.getFont(getContext()));
		text.setTextSize(McStyle.getTextSize(getContext()));
		
		// initialise background
		if(backgroundImage == null) {
			backgroundImage = BitmapFactory.decodeResource(
					getResources(), R.drawable.button_background
			);
		}
		background = new BitmapDrawable(getResources(), backgroundImage);
		background.setTileModeXY(
				Shader.TileMode.REPEAT,
				Shader.TileMode.REPEAT
		);
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.widget.TextView#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		int borderWidth = McStyle.getButtonUnit(getContext());
		int halfBorder = borderWidth / 2;
		int textOffset = 0; // on enabled button text should be a bit upper
		
		// draw button background
		background.setBounds(0, 0, this.getWidth(), this.getHeight());
		background.draw(canvas);
		
		// border
		stroke.setColor(McStyle.BUTTON_COLOUR_BORDER);
		canvas.drawRect(
				halfBorder,
				halfBorder,
				this.getWidth() - halfBorder,
				this.getHeight() - halfBorder, 
				stroke
		);
		
		if(isEnabled()) {
			int borderOffset = borderWidth + (halfBorder);
			textOffset = borderOffset;
			
			// highlight
			stroke.setColor(McStyle.BUTTON_COLOUR_HIGHLIGHT);
			
			// draw left highlight
			canvas.drawLine(
					borderOffset, borderWidth,
					borderOffset, this.getHeight() - borderWidth,
					stroke
			);
			
			// draw upper highlight
			canvas.drawLine(
					borderWidth * 2,				borderOffset,
					this.getWidth() - borderWidth,	borderOffset,
					stroke
			);
			
			// darken
			stroke.setColor(McStyle.BUTTON_COLOUR_DARKEN);
			fill.setColor(McStyle.BUTTON_COLOUR_DARKEN);
			
			// draw right darken
			canvas.drawLine(
					this.getWidth() - borderOffset,
					borderWidth,
					this.getWidth() - borderOffset,
					this.getHeight() - borderWidth,
					stroke
			);
			
			// draw bottom darken			
			canvas.drawRect(
					borderWidth,
					this.getHeight() - borderWidth * 4,
					this.getWidth() - borderWidth * 2,
					this.getHeight() - borderWidth,
					fill
			);
			
			if(isPressed()) {
				fill.setColor(McStyle.BUTTON_OVERLAY_SELECTED);
				canvas.drawPaint(fill);
			}
		} else { // if disabled
			fill.setColor(McStyle.BUTTON_OVERLAY_DISABLED);
			canvas.drawPaint(fill);
		}
		
		// draw text
	    int xPos = this.getWidth() / 2;
	    int yPos = (int)(this.getHeight() / 2 - (
	    		(text.descent() + text.ascent()) / 2)) - textOffset;
	    
	    String label = getText().toString();
	    
	    // shadow
	    text.setColor(McStyle.TEXT_SHADOW_COLOUR);
	    canvas.drawText(label, xPos + borderWidth, yPos + borderWidth, text);
	    
	    // normal
	    if(isPressed()) text.setColor(McStyle.TEXT_COLOUR_SELECTED);
	    else text.setColor(McStyle.TEXT_COLOUR_BUTTON);
	    canvas.drawText(label, xPos, yPos, text);
	}

}
