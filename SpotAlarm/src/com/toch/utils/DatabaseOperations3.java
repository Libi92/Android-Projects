package com.toch.utils;



        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.DatabaseErrorHandler;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.util.Log;


public class DatabaseOperations3 extends SQLiteOpenHelper {
    static final int database_version = 1;
    static final String USER_NAME = "user_name";
    static final String USER_PHONE = "user_ph";
    static final String USER_SELECT = "user_select";
    static final String DATABSE_NAME = "phone_info.db";
    static final String TABLE_NAME = "ph_info";




    public DatabaseOperations3(Context context) {
        super(context, DATABSE_NAME, null, database_version);
        Log.d("Database Operations","Database Created");

    }

    @Override
    public void onCreate(SQLiteDatabase sdb) {
        Log.d("Database Operations3","Table Created");
        sdb.execSQL("CREATE TABLE " + TABLE_NAME +
                "( "  + USER_NAME + " text, " +
                USER_PHONE + " text, " + USER_SELECT + " text)");

    }




    public void onUpgrade(SQLiteDatabase sq,int arg1,int arg2) {
        Log.d("DatabaseHelper", "onUpgrade");
        sq.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sq);

    }

    public void delete(String name){
        SQLiteDatabase SQ = this.getWritableDatabase();
        name = name.trim();
        SQ.rawQuery("DELETE FROM " + TABLE_NAME + " WHERE " + USER_NAME + " = ?", new String[]{name});
    }


    public void putInformation(DatabaseOperations3 dop,String name,String ph,String isc)
    {
        SQLiteDatabase SQ = dop.getWritableDatabase();
        name = name.trim();
        SQ.rawQuery("DELETE FROM " + TABLE_NAME + " WHERE " + USER_NAME + " = ?", new String[]{name});
        ContentValues cv = new ContentValues();
        cv.put(USER_NAME,name);
        cv.put(USER_PHONE,ph);
        cv.put(USER_SELECT,isc);
        Log.d(name,ph);
        SQ.insert(TABLE_NAME,null,cv);
    }
    public Cursor getInformation(DatabaseOperations3 dop)
    {
        Log.d("DatabaseHelper", "getinfo");

        SQLiteDatabase SQ = dop.getReadableDatabase();
        String[] columns = {USER_NAME, USER_PHONE,USER_SELECT};
        Cursor CR = SQ.query(TABLE_NAME,columns,null, null, null, null, null);
        return CR;

    }
}


