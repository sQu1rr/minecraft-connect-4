/* COPYRIGHT (C) 2014 Aleksandr Belkin. All Rights Reserved. */
package sq.squ1rr.mcc4.layout;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * Minecraft text font. TextView with different font.
 * @author Aleksandr Belkin
 */
public class McText extends TextView {
	/**
	 * Creates Minecraft styled text view
	 * @param context
	 */
	public McText(Context context) {
		super(context);
		
		float den = getResources().getDisplayMetrics().density;
		
		// set font
		setTypeface(McStyle.getFont(context));
		
		// set text colour
		setTextColor(McStyle.TEXT_COLOUR_CONTENT);
		
		// set text shadow
		setShadowLayer(1f, den, den, Color.BLACK);

		// set text size
		setTextSize(TypedValue.COMPLEX_UNIT_DIP, McStyle.TEXT_SIZE);
	}
}
