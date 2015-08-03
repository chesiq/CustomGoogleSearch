package ua.antonk.googlecustomsearch.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

import ua.antonk.googlecustomsearch.database.entities.Item;

/**
 * Created by Anton on 02.08.2015.
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper{

    private static String DB_NAME = "search_results";
    private static int DB_VERSION = 1;

    public static final String COLUMN_LINK = "link";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_THUMBNAIL = "thumbnail";

    private static final String TABLE_FAVORITES = "favorites";

    private static DatabaseOpenHelper sInstance;

    public static synchronized DatabaseOpenHelper getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new DatabaseOpenHelper(context.getApplicationContext());
        }
        return sInstance;
    }


    private DatabaseOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql =     "CREATE TABLE "+ TABLE_FAVORITES + " ( "
                + COLUMN_LINK + " TEXT PRIMARY KEY NOT NULL , "
                + COLUMN_TITLE + "  TEXT NOT NULL ,"
                + COLUMN_IMAGE + "  TEXT ,"
                + COLUMN_THUMBNAIL + "  TEXT ) ";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //no impl
    }

    public Cursor getData(int startIndex, int count){
        int endIndex = startIndex + count;

        return getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_FAVORITES +
                " LIMIT " + startIndex + ", " + endIndex, null);
    }

    public void insertData(List<Item> items){

        SQLiteDatabase db = this.getWritableDatabase();

        for(Item item : items){
            ContentValues values = new ContentValues();
            values.put(COLUMN_LINK, item.getLink());
            values.put(COLUMN_TITLE, item.getTitle());
            values.put(COLUMN_IMAGE, item.getImagePath());
            values.put(COLUMN_THUMBNAIL, item.getThumbPath());
            try {
                db.insert(TABLE_FAVORITES, null, values);
            }catch (SQLiteException e){
                e.printStackTrace();
            }
        }
    }
}
