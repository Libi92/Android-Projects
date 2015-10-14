package com.toch.utils;



        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.DatabaseErrorHandler;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.util.Log;


public class DatabaseOperations extends SQLiteOpenHelper {
    public static final int database_version = 1;
    public static final String USER_NAME = "user_name";
    public static final String USER_PASS = "user_pass";
    public static final String DATABSE_NAME = "user_info";
    public static final String TABLE_NAME = "reg_info";






    public String CREATE_QUERY = "CREATE TABLE "+ TABLE_NAME+"("+ USER_NAME+" TEXT,"+ USER_PASS+" TEXT);";

    public DatabaseOperations(Context context) {
        super(context, DATABSE_NAME, null, database_version);
        Log.d("Database Operations","Database Created");
    }



    @Override
    public void onCreate(SQLiteDatabase sdb) {
        sdb.execSQL(CREATE_QUERY);
        Log.d("Database Operations","Table Created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0,int arg1,int arg2) {

    }
    public void putInformation(DatabaseOperations dop,String name,String pass)
    {
        SQLiteDatabase SQ = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(USER_NAME,name);
        cv.put(USER_PASS,pass);
        long k = SQ.insert(TABLE_NAME,null,cv);
        Log.d("Database Operations","One row inserted");
        SQ.close();

    }
    public Cursor getInformation(DatabaseOperations dop)
    {
        SQLiteDatabase SQ = dop.getReadableDatabase();
        String[] columns = {USER_NAME, USER_PASS};
        Cursor CR = SQ.query(TABLE_NAME,columns,null, null, null, null, null);
        return CR;

    }

    public void delete(String name){
        Log.d("DATABASE",name);
        SQLiteDatabase SQ = this.getWritableDatabase();
        name = name.trim();
        SQ.rawQuery("DELETE FROM " + TABLE_NAME + " WHERE " + USER_NAME + " = ?", new String[]{name});
        SQ.close();
    }

}


