package com.example.todolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "NotesDatabase";


    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        create table
        String sqlQuery = "CREATE TABLE Note (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT, description TEXT)";
//        execeute sql
        sqLiteDatabase.execSQL(sqlQuery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//        when database change or upgraded this method will run
            String sqlQuery = "DROP TABLE IF EXISTS Note";
            sqLiteDatabase.execSQL(sqlQuery);

            onCreate(sqLiteDatabase);

    }
}
