/* COPYRIGHT (C) 2014 Aleksandr Belkin. All Rights Reserved. */
package sq.squ1rr.mcc4.layout;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Allows to group views horizontally or vertically with margins and paddings.
 * @author Aleksandr Belkin
 */
public class McGroup extends LinearLayout {
	/** child layout parameters */
	private LayoutParams childParams = null;
	
	/**
	 * Create group
	 * @param context
	 */
	public McGroup(Context context) {
		super(context);
		setOrientation(LinearLayout.HORIZONTAL);
	}
	
	/**
	 * Sets child layout parameters, weight is default
	 * @param width
	 * @param height
	 */
	public void setChildParams(int width, int height) {
		childParams = new LayoutParams(width, height);
	}
	
	/**
	 * Sets child layout parameters including weight
	 * @param width
	 * @param height
	 * @param weight
	 */
	public void setChildParams(int width, int height, float weight) {
		childParams = new LayoutParams(width, height, weight);
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.view.ViewGroup#addView(android.view.View)
	 */
	@Override
	public void addView(View view) {
		// if no child parameters set use default
		if(childParams == null) {
			childParams = new LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT
			);
		}
		
		// if more than one child add margin
		if(getChildCount() > 0) {
			int spacing = McStyle.getMenuWidgetSpacing(getContext());
			if(getOrientation() == LinearLayout.HORIZONTAL) {
				childParams.setMargins(spacing, 0, 0, 0);
			} else childParams.setMargins(0, spacing, 0, 0);
		} else {
			childParams.setMargins(0, 0, 0, 0);
		}
		
		// add view
		view.setLayoutParams(childParams);
		super.addView(view);
	}
}
