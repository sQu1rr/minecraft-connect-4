/* COPYRIGHT (C) 2014 Aleksandr Belkin. All Rights Reserved. */
package sq.squ1rr.mcc4.layout;

import android.content.Context;
import android.view.View;

/**
 * Minecraft Select Button.
 * Acts like a list in a button, selects next element when the button is
 * clicked.
 * @author Aleksandr Belkin
 */
public class McSelector extends McButton {
    /** labels to show */
    private String[] labels = null;
    
    /** IDs assigned to labels */
    private int[] ids = null;
    
    /** selected element */
    private int selected = 0;
    
    /** click listener */
    private OnClickListener clickListener = null;
    
    /**
     * Create Button
     * @param context
     */
    public McSelector(Context context) {
        super(context);
        super.setOnClickListener(changeLabel);
    }

    /**
     * Returns index of the selected label
     * @return
     */
    public int getSelectedIndex() {
        return selected;
    }
    
    /**
     * Returns selected ID
     * @return
     */
    public int getSelectedId() {
        return ids[selected];
    }
    
    /**
     * Sets new labels
     * @param _labels
     */
    public void setLabels(String[] _labels) {
        labels = _labels;
        invalidate();
    }
    
    /**
     * Sets IDs associated with labels
     * @param _ids
     */
    public void setIds(int[] _ids) {
        ids = _ids;
    }
    
    /**
     * Sets new labels from string resources and assigns IDs
     */
    public void setLabels(int[] stringIds) {
        String[] labels = new String[stringIds.length];
        for(int i = 0; i < stringIds.length; ++i) {
            labels[i] = getContext().getString(stringIds[i]);
        }
        ids = stringIds;
        setLabels(labels);
    }
    
    /**
     * Selects the label by index
     * @param index
     */
    public void select(int index) {
        selected = index;
        invalidate();
    }
    
    /**
     * Selects the label based on ID
     * @param id
     */
    public void selectId(int id) {
        selected = 0;
        for(int i : ids) {
            if(i == id) break;
            selected++;
        }
        invalidate();
    }
    
    /*
     * (non-Javadoc)
     * @see android.view.View#invalidate()
     */
    @Override
    public void invalidate() {
        if(labels != null && getText() != labels[selected]) {
            setText(labels[selected]);
        }
        super.invalidate();
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
     * Increments selection index, calls user listener
     */
    private OnClickListener changeLabel = new OnClickListener() {
        @Override
        public void onClick(View view) {
            selected = (selected + 1) % labels.length;
            if(clickListener != null) clickListener.onClick(view);
            invalidate();
        }
    };
}
