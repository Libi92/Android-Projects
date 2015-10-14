package com.toch.utils;


        import android.content.ContentValues;
        import android.content.Context;
        import android.content.SharedPreferences;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.util.Log;




public class DatabaseOperations2 extends SQLiteOpenHelper {


    static int DATABASE_VERSION = 1;
    static String DATABASE_NAME = "data";
    static String TABLE_NAME = "history";
    public static String HISTORY_COLUMN_LNG = "lng";
    public static String HISTORY_COLUMN_LAT = "lat";
    public static String HISTORY_COLUMN_ID = "_id";
    public static String HISTORY_COLUMN_NAME = "name";
    public static String HISTORY_COLUMN_LOCATION = "location";

    private Context mContext;




    public DatabaseOperations2(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d("DatabaseHelper", "Database2 constructor");
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        Log.d("DatabaseOP2", "onCreate");
        database.execSQL("CREATE TABLE " + TABLE_NAME +
                "( " + HISTORY_COLUMN_ID + " integer primary key, " + HISTORY_COLUMN_LNG + " text, " +
                HISTORY_COLUMN_LAT + " text, " + HISTORY_COLUMN_NAME + " text, " + HISTORY_COLUMN_LOCATION + " text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVer, int newVer) {
        Log.d("DatabaseHelper", "onUpgrade");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);

    }

    public void saveRecord(double lat, double lng, String name, String location) {
        Log.d("DatabaseHelper", "saveRecord");
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(HISTORY_COLUMN_LAT,lat);
        contentValues.put(HISTORY_COLUMN_LNG,lng);
        contentValues.put(HISTORY_COLUMN_NAME,name);
        contentValues.put(HISTORY_COLUMN_LOCATION,location);

        database.insert(TABLE_NAME, null, contentValues);



    }
    public void delete(String lat, String lng){
        SQLiteDatabase SQ = this.getWritableDatabase();
//        name = name.trim();
        lat = lat.trim();
        lng.trim();
        SQ.rawQuery("DELETE FROM " + TABLE_NAME + " WHERE " + HISTORY_COLUMN_LAT + " = ? AND " + HISTORY_COLUMN_LNG + " = ?", new String[]{lat, lng});
    }
    public Cursor getAllRecords() {

        SharedPreferences pref = mContext.getSharedPreferences("lin", 0);
        String a = pref.getString("KEY", null);          // getting String
        Log.d("test=====>", a);
        SQLiteDatabase databaseX = getReadableDatabase();

        a = a.trim();
        return databaseX.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + HISTORY_COLUMN_NAME + " = ?", new String[]{a});

    }
}



