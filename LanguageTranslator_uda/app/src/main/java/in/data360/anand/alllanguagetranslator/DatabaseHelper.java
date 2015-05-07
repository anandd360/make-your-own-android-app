package in.data360.anand.alllanguagetranslator;

/**
 * Created by ANAND on 4/7/2015.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Name
    public static String DATABASE_NAME = "trans_db";

    // Current version of database
    private static final int DATABASE_VERSION = 1;

    // Name of table
    private static final String TABLE_TRANS = "translator";

    // All Keys used in table
    private static final String KEY_ID = "id";
    private static final String KEY_INPUT = "input";
    private static final String KEY_OUTPUT = "output";

    public static String TAG = "tag";

    // Translator Table Create Query
    /**
     * CREATE TABLE translator ( id INTEGER PRIMARY KEY AUTOINCREMENT, name
     * TEXT,phone_number TEXT);
     */

    private static final String CREATE_TABLE_TRANS = "CREATE TABLE "
            + TABLE_TRANS + "(" + KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_INPUT + " TEXT,"
            + KEY_OUTPUT+ " TEXT );";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This method is called by system if the database is accessed but not yet
     * created.
     */

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_TRANS); // create  table

    }

    /**
     * This method is called when any modifications in database are done like
     * version is updated or database schema is changed
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_TRANS); // drop table if exists

        onCreate(db);
    }



    public long addDetail(FavModel trans) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Creating content values
        ContentValues values = new ContentValues();
        values.put(KEY_INPUT, trans.input);
        values.put(KEY_OUTPUT, trans.output);

        // insert row in translator table

        long insert = db.insert(TABLE_TRANS, null, values);

        return insert;
    }

    /**
     * This method is used to update particular record entry
     *
     */
    public int updateEntry(FavModel trans) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Creating content values
        ContentValues values = new ContentValues();
        values.put(KEY_INPUT, trans.input);
        values.put(KEY_OUTPUT, trans.output);

        // update row in table base on .is value
        return db.update(TABLE_TRANS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(trans.id) });
    }

    /**
     * Used to delete particular entry
     *
     * @param id
     */
    public void deleteEntry(long id) {

        // delete row from table based on id
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRANS, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }

    /**
     * Used to get particular record details
     *
     * @param id
     * @return
     */

    public FavModel getTrans(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        // SELECT * FROM table WHERE id = ?;
        String selectQuery = "SELECT  * FROM " + TABLE_TRANS + " WHERE "
                + KEY_ID + " = " + id;
        Log.d(TAG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        FavModel trans = new FavModel();
        trans.id = c.getInt(c.getColumnIndex(KEY_ID));
        trans.output = c.getString(c.getColumnIndex(KEY_OUTPUT));
        trans.input = c.getString(c.getColumnIndex(KEY_INPUT));

        return trans;
    }

    /**
     * Used to get detail of entire database and save in array list of data type
     * FavModel
     *
     * @return
     */
    public List<FavModel> getAllList() {
        List<FavModel> transArrayList = new ArrayList<FavModel>();

        String selectQuery = "SELECT  * FROM " + TABLE_TRANS;
        Log.d(TAG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                FavModel trans = new FavModel();
                trans.id = c.getInt(c.getColumnIndex(KEY_ID));
                trans.output = c.getString(c
                        .getColumnIndex(KEY_OUTPUT));
                trans.input = c.getString(c.getColumnIndex(KEY_INPUT));

                // adding to list
                transArrayList.add(trans);
            } while (c.moveToNext());
        }

        return transArrayList;
    }
}
