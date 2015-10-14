package com.example.sqlite_application;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
public class SQLiteHelper extends SQLiteOpenHelper {

   private static String DB_PATH="/data/data/com.example.sqlite_application/databases/";
    //private static String DB_PATH;
    private static String DB_NAME = "leon";
    private static int VERSION =1;
    private SQLiteDatabase myDataBase;
    private final Context myContext;
    
    private static final String TABLE_RECORD = "People";
    private static final String COLUMN_NAME = "name";
    private static final String MY_NAME = "Leon";

    

    public SQLiteHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
        myContext = context;
        //DB_PATH=myContext.getDatabasePath(DB_NAME).getPath();
        try {
            createDatabase();
        }
        catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
    }


    public void createDatabase() throws IOException {
        boolean dbExist = checkDataBase();

        if (dbExist) {
            System.out.println("DB EXIST");
        }

        else {
            this.getReadableDatabase();
            this.close();
            copyDataBase();
        }
    }

    private void copyDataBase() throws IOException {
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;

        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            System.out.println("Database does't exist yet.");
        }

        if (checkDB != null) {
            checkDB.close();
        }

        return checkDB != null ? true : false;

    }

    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();

        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase arg0) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }
    
    
    public List<String> getAllRecord(String name) {
        List<String> studentList = new ArrayList<String>();

        //String leon = e_name.getText().toString();

      String selectQuery = "SELECT  * FROM " + TABLE_RECORD + " WHERE " + COLUMN_NAME + " = '" + name + "'";

       //String selectQuery = "SELECT  * FROM " + TABLE_RECORD + " WHERE " + COLUMN_NAME + " = '" +leon + "'";


      SQLiteDatabase database = getReadableDatabase();
       Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
             
                studentList.add("Id: " + cursor.getString(0)+ "\n"+  "Name: " + cursor.getString(1)+"\n"+ "Place: "+
                        cursor.getString(2) + "\n"+ "Age: "+ cursor.getString(3));

            } while (cursor.moveToNext());
        }
        database.close();
        return studentList;
    }

    

}
