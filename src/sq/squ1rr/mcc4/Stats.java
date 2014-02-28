/* COPYRIGHT (C) 2014 Aleksandr Belkin. All Rights Reserved. */
package sq.squ1rr.mcc4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Class saves and shows the statistics.
 * @author squ1rr
 */
public class Stats extends SQLiteOpenHelper {
    /*
     * Stats IDs
     */
    public static final int STATS_GAMES        = 0;
    public static final int STATS_PVPS        = 1;
    public static final int STATS_WINS        = 2;
    public static final int STATS_DEFEATS    = 3;
    public static final int STATS_TIME        = 4;
    public static final int STATS_DRAWS        = 5;
    
    /*
     * Database constants
     */
    /** database version */
    private static final int DATABASE_VERSION = 1;
     
    /** database name */
    private static final String DATABASE_NAME = "Stats";
 
    /** database table */
    private static final String TABLE_STATS = "stats";
    
    /*
     * Columns
     */
    private static final String KEY_ID = "id";
    private static final String KEY_VALUE = "value";
    
    /**
     * Create database
     * @param context
     */
    public Stats(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    /*
     * (non-Javadoc)
     * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_STATS + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_VALUE + " INTEGER)";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
 
    /*
     * (non-Javadoc)
     * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATS);
        onCreate(db);
    }
    
    /**
     * Insert a row in a table
     * @param id
     * @param value
     */
    private void insert(int id, int value) {
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put(KEY_ID, id);
        values.put(KEY_VALUE, value);
     
        db.insert(TABLE_STATS, null, values);
        db.close();
    }
    
    /**
     * Update a rew in a table
     * @param id
     * @param value
     * @return
     */
    private int update(int id, int value) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(KEY_VALUE, value);
     
        int result = db.update(TABLE_STATS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
        
        db.close();
        
        return result;
    }
    
    /**
     * Clear all info
     */
    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_STATS, null, null);
        db.close();
    }
    
    /**
     * Gets a row value if it is present, -1 otherwise
     * @param id
     * @return
     */
    public int get(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
     
        int result = -1;
        Cursor cursor = db.query(TABLE_STATS, new String[] { KEY_VALUE },
                KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if(cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            result = cursor.getInt(0);
        }
        
        if(cursor != null) cursor.close();

        db.close();
        return result;
    }
    
    /**
     * Updates a row if it's present (by adding value to it),
     * adds new row otherwise
     * @param id
     * @param value
     */
    public void add(int id, int value) {
        int old = get(id);
        if(old != -1) {
            value += old;
            update(id, value);
        } else {
            insert(id, value);
        }
    }
}
