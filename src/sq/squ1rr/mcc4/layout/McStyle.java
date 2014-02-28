/* COPYRIGHT (C) 2014 Aleksandr Belkin. All Rights Reserved. */
package sq.squ1rr.mcc4.layout;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;

/**
 * Minecraft Styles
 * Computed values are stored for later use.
 * @author Aleksandr Belkin
 */
public class McStyle {
    
    /*
     * LAYOUT
     */
    
    /** widget spacing in DIP */
    public static final int MENU_WIDGET_SPACING = 8;
    
    /** padding between menu area and page border */
    public static final int MENU_PADDING = 8;
    
    /** spacing between banner and menu area */
    public static final int MENU_BANNER_SPACING = 48;
    
    /*
     * TEXT
     */
    
    /** size of the text in DIP */
    public static final int TEXT_SIZE = 16;
    
    /** line #1 of game menu (when again AI) */
    public static final int TEXT_SIZE_GAME_LINE1_AI = 56;
    
    /** line #1 of game menu (when again other player) */
    public static final int TEXT_SIZE_GAME_LINE1 = 48;
    
    /** line #2 of game menu */
    public static final int TEXT_SIZE_GAME_LINE2 = 56;
    
    /** colour of the text in the title */
    public static final int TEXT_COLOUR_TITLE = Color.WHITE;
    
    /** colour of the text on the content pane */
    public static final int TEXT_COLOUR_CONTENT = 0xFF999999;
    
    /** colour of the text on the button */
    public static final int TEXT_COLOUR_BUTTON = 0xFFEEEEEE;
    
    /** colour of the text shadow */
    public static final int TEXT_SHADOW_COLOUR = 0xFF222222;
    
    /** colour of the selected text */
    public static final int TEXT_COLOUR_SELECTED = 0xFFFFFFA0;
    
    /*
     * BUTTON
     */
    
    /** buttons smallest line width in DIP. Must be MORE than 1 */
    public static final int BUTTON_UNIT = 2;
    
    /** colour of a border of button */
    public static final int BUTTON_COLOUR_BORDER = Color.BLACK;
    
    /** colour of highlighted spots on button */
    public static final int BUTTON_COLOUR_HIGHLIGHT = 0x85FFFFFF;
    
    /** colour of darken lines on button */
    public static final int BUTTON_COLOUR_DARKEN = 0x35000000;
    
    /** overlay colour of the disabled button */
    public static final int BUTTON_OVERLAY_DISABLED = 0x85000000;
    
    /** overlay colour of the selected button */
    public static final int BUTTON_OVERLAY_SELECTED = 0x556588BF;
    
    /*
     * TOGGLER
     */
    
    /** togglers smallest line width in DIP. Must be MORE than 3 */
    public static final int TOGGLER_UNIT = 4;
    
    /** toggle button size: vertical and horizontal */
    public static final int TOGGLER_SIZE = 64;
    
    /** toggle button padding between selection border and image */
    public static final int TOGGLER_PADDING = 8;
    
    /** colour of a border of toggler */
    public static final int TOGGLER_COLOUR_BORDER = 0xFF808080;
    
    /** colour of a border of toggler in super state */
    public static final int TOGGLER_COLOUR_SUPER_BORDER = Color.YELLOW;
    
    /** colour of a selection of toggler */
    public static final int TOGGLER_COLOUR_SELECTION = Color.BLACK;
    
    /** colour of a selection of toggler */
    public static final int TOGGLER_COLOUR_INSIDE = 0xFF8B8B8B;
    
    /** colour of a selection of toggler */
    public static final int TOGGLER_COLOUR_HIGHLIGHT = Color.WHITE;
    
    /** colour of a selection of toggler */
    public static final int TOGGLER_COLOUR_DARKEN = 0x80000000;
    
    /*
     * FONT
     */
    
    /** Minecraft font */
    private static Typeface font = null;
    
    /** Text size in pixels */
    private static float textSize = 0;
    
    /** Button unit in pixels */
    private static int buttonUnit = 0;
    
    /** Toggler unit in pixels */
    private static int togglerUnit = 0;
    
    /** Toggler size in pixels */
    private static int togglerSize = 0;
    
    /** Toggler padding in pixels */
    private static int togglerPadding = 0;
    
    /** Widget spacing in pixels */
    private static int menuWidgetSpacing = 0;
    
    /** Menu padding in pixels */
    private static int menuPadding = 0;
    
    /** Banner spacing in pixels */
    private static int menuBannerSpacing = 0;
    
    /**
     * Returns a Minecraftia font, creates it if needed
     * @param context
     * @return Minecraftia font
     */
    public static Typeface getFont(Context context) {
        if(font == null) {
            font = Typeface.createFromAsset(
                context.getAssets(), "Minecraftia.ttf"
            );
        }
        return font;
    }
    
    private static float convertDpiToPx(Context context, int dip) {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dip,
            context.getResources().getDisplayMetrics()
        );
    }
    
    /**
     * Returns a standard text size in pixels
     * @param context
     * @return
     */
    public static float getTextSize(Context context) {
        if(textSize == 0) {
            textSize = convertDpiToPx(context, TEXT_SIZE);
        }
        return textSize;
    }
    
    /**
     * Returns a button unit in pixels
     * @param context
     * @return
     */
    public static int getButtonUnit(Context context) {
        if(buttonUnit == 0) {
            // must divide by 2
            buttonUnit = ((int)convertDpiToPx(context, BUTTON_UNIT) / 2);
            if(buttonUnit < 1) buttonUnit = 1;
            buttonUnit *= 2; // must be at least 2
        }
        return buttonUnit;
    }
    
    /**
     * Returns a toggler unit in pixels
     * @param context
     * @return
     */
    public static int getTogglerUnit(Context context) {
        if(togglerUnit == 0) {
            // must divide by 4
            togglerUnit = ((int)convertDpiToPx(context, TOGGLER_UNIT) / 4);
            if(togglerUnit < 1) togglerUnit = 1;
            togglerUnit *= 4; // must be at least 4
        }
        return togglerUnit;
    }
    
    /**
     * Returns a toggler size in pixels
     * @param context
     * @return
     */
    public static int getTogglerSize(Context context) {
        if(togglerSize == 0) {
            togglerSize = (int)convertDpiToPx(context, TOGGLER_SIZE);
        }
        return togglerSize;
    }
    
    /**
     * Returns a toggler padding in pixels
     * @param context
     * @return
     */
    public static int getTogglerPadding(Context context) {
        if(togglerPadding == 0) {
            togglerPadding = (int)convertDpiToPx(context, TOGGLER_PADDING);
        }
        return togglerPadding;
    }
    
    /**
     * Returns a menu widget spacing in pixels
     * @param context
     * @return
     */
    public static int getMenuWidgetSpacing(Context context) {
        if(menuWidgetSpacing == 0) {
            menuWidgetSpacing = (int)convertDpiToPx(context, TOGGLER_PADDING);
        }
        return menuWidgetSpacing;
    }
    
    /**
     * Returns a menu padding in pixels
     * @param context
     * @return
     */
    public static int getMenuPadding(Context context) {
        if(menuPadding == 0) {
            menuPadding = (int)convertDpiToPx(context, MENU_PADDING);
        }
        return menuPadding;
    }
    
    /**
     * Returns a menu banner spacing in pixels
     * @param ctx
     * @return
     */
    public static int getMenuBannerSpacing(Context ctx) {
        if(menuBannerSpacing == 0) {
            menuBannerSpacing = (int)convertDpiToPx(ctx, MENU_BANNER_SPACING);
        }
        return menuBannerSpacing;
    }
}
