/* COPYRIGHT (C) 2014 Aleksandr Belkin. All Rights Reserved. */
package sq.squ1rr.mcc4.layout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ToggleButton;

/**
 * Toggle button that switches on or off
 * @author Aleksandr Belkin
 */
public class McToggler extends ToggleButton {
	/** stroke paint */
	private Paint stroke = new Paint();
	
	/** fill paint */
	private Paint fill = new Paint();
	
	/** text paint */
	private Paint text = new Paint();
	
	/** background image */
	private BitmapDrawable background = null;
	
	/** user click listener */
	private OnClickListener clickListener = null;
	
	/** text displayed on a selected button */
	private String label = null;
	
	/** draw a red border around it */
	private boolean superSelected = false;
	
	/** cannot be unchecked by user */
	private boolean uncheckable = false;
	
	/**
	 * Create toggler
	 * @param context
	 */
	public McToggler(Context context) {
		super(context);
		initialise();
	}
	
	/**
	 * Initialise members
	 */
	public void initialise() {
		int size = McStyle.getTogglerSize(getContext());
		
		// remove default style
		this.setTextOn(null);
		this.setTextOff(null);
		this.setBackgroundColor(Color.TRANSPARENT);
		
		// set size
		setWidth(size);
		setHeight(size);
		
		// stroke
		stroke.setStyle(Style.STROKE);
		
		// fill
		fill.setStyle(Style.FILL);
		
		// text
		text.setTextAlign(Align.RIGHT);
		text.setTypeface(McStyle.getFont(getContext()));
		text.setTextSize(McStyle.getTextSize(getContext()));
		
		// set listener
		super.setOnClickListener(listener);
	}
	
	/**
	 * Sets image to use on the button
	 * @param imageId
	 */
	public void setImage(int imageId) {
		Bitmap backgroundImage = BitmapFactory.decodeResource(
				getResources(), imageId
		);
		background = new BitmapDrawable(getResources(), backgroundImage);
	}
	
	/**
	 * Sets button to be uncheckable (only by user)
	 * @param state
	 */
	public void setUncheckable(boolean state) {
		uncheckable = state;
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.view.View#setOnClickListener(android.view.View.OnClickListener)
	 */
	@Override
	public void setOnClickListener(OnClickListener _clickListener) {
		clickListener = _clickListener;
	}
	
	/**
	 * Sets label that will be displayed on the selected button
	 * @param str
	 */
	public void setLabel(String str) {
		label = str;
		invalidate();
	}
	
	/**
	 * Draw red border around selected button
	 * @param state
	 */
	public void setSuperSelect(boolean state) {
		superSelected = state;
		invalidate();
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.widget.ToggleButton#setChecked(boolean)
	 */
	@Override
	public void setChecked(boolean state) {
		super.setChecked(state);
		if(uncheckable && !state) {
			setEnabled(true);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.widget.CompoundButton#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		// padding between selection border and image
		int padding = McStyle.getTogglerPadding(getContext());
		
		// unit size
		int borderWidth = McStyle.getTogglerUnit(getContext());
		int halfWidth = borderWidth / 2;
		
		if(isChecked()) {
			// selection
			fill.setColor(McStyle.TOGGLER_COLOUR_SELECTION);
			canvas.drawPaint(fill);
			
			// border
			if(!superSelected) stroke.setColor(McStyle.TOGGLER_COLOUR_BORDER); 
			else stroke.setColor(McStyle.TOGGLER_COLOUR_SUPER_BORDER);
			stroke.setStrokeWidth(borderWidth);
			canvas.drawRect(0, 0, this.getWidth(), getHeight(), stroke);
		}
		
		// inside
		fill.setColor(McStyle.TOGGLER_COLOUR_INSIDE);
		canvas.drawRect(
				padding,
				padding,
				this.getWidth() - padding,
				this.getHeight() - padding,
				fill
		);
		
		stroke.setStrokeWidth(halfWidth);
		int offset = padding + halfWidth / 2;
		
		// highlights
		stroke.setColor(McStyle.TOGGLER_COLOUR_HIGHLIGHT);
		
		// right highlight
		canvas.drawLine(
				this.getWidth() - offset,
				padding,
				this.getWidth() - offset,
				this.getHeight() - padding,
				stroke
		);
		
		// bottom highlight
		canvas.drawLine(
				padding,
				this.getHeight() - offset,
				this.getWidth() - padding,
				this.getHeight() - offset,
				stroke
		);
		
		// darkens
		stroke.setColor(McStyle.TOGGLER_COLOUR_DARKEN);
		
		// left darken
		canvas.drawLine(
				offset,
				padding,
				offset,
				this.getHeight() - padding,
				stroke
		);
		
		// top darken
		canvas.drawLine(
				padding + halfWidth,
				offset,
				this.getWidth() - padding,
				offset,
				stroke
		);
		
		int imagePadding = padding + borderWidth + halfWidth;
		
		// background
		if(background != null) {
			background.setBounds(
					imagePadding,
					imagePadding,
					this.getWidth() - imagePadding,
					this.getHeight() - imagePadding
			);
			background.draw(canvas);
		}
		
		// label
		if(isChecked() && label != null) {
			int xPos = this.getWidth() - imagePadding;
		    int yPos = (int)(this.getHeight() - imagePadding);
		    
		    // shadow
		    text.setColor(McStyle.TEXT_SHADOW_COLOUR);
		    canvas.drawText(label, xPos + halfWidth, yPos + halfWidth, text);
		    
		    // normal
		    text.setColor(McStyle.TEXT_COLOUR_TITLE);
			canvas.drawText(label, xPos, yPos, text);
		}
	}
	
	/**
	 * Invalidates the view whenever the state is changed
	 */
	private OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			McToggler.this.invalidate();
			
			if(uncheckable) {
				setEnabled(!isChecked());
			}
	
			if(clickListener != null) {
				clickListener.onClick(McToggler.this);
			}
			
			if(!isChecked()) setChecked(true);
		}
	};
}
