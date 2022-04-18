package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class NoteHandler extends DatabaseHelper{
    public NoteHandler(Context context) {
        super(context);
    }

//    CRUD Operation in Database Methods will be here

    public boolean create(Note note){
//        create data into table

        ContentValues values = new ContentValues();
        values.put("title", note.getTitle());
        values.put("description", note.getDescription());

        SQLiteDatabase db = this.getWritableDatabase();
        boolean isSuccessfull = db.insert("Note",null,values) > 0;
//        null ka matlab hai ki row empty bhi ho sakti hai table ke ander
//        if it returns value 0 or -1 , it means data did not inserted into table
//        if number is higher than zero that means data inserted successfully
        db.close();
        return isSuccessfull;

//        thats all for INSERTING DATA INTO TABLE IN DATABASE
    }



    public ArrayList<Note> readNotes(){
        ArrayList<Note> notes = new ArrayList<>();

        String sqlQuery = "SELECT * FROM Note ORDER BY id ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sqlQuery,null);

        if (cursor.moveToFirst()){
            do {
//                fetching data from table
                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("id")));
            String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
            String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));

            Note note = new Note(title, description);
            note.setId(id);
            notes.add(note);
            }while (cursor.moveToNext());

            cursor.close();
            db.close();
        }
        return notes;
    }

    public Note readSingleNote(int id){
        Note note = null;
        String sqlQuery = "SELECT * FROM Note WHERE id = "+id;

        SQLiteDatabase db = this.getWritableDatabase();
       Cursor cursor = db.rawQuery(sqlQuery,null);

       if (cursor.moveToFirst()){
           int noteid = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("id")));
           String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
           String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));

           note = new Note(title, description);
           note.setId(noteid);
       }
        cursor.close();
       db.close();
       return note;
    }

    public boolean update(Note note){
        ContentValues values= new ContentValues();
        values.put("title", note.getTitle());
        values.put("description", note.getDescription());
        SQLiteDatabase db = this.getWritableDatabase();

        boolean isSuccessfull =  db.update("Note", values,"id='"+note.getId()+"'",null)>0;
        db.close();
        return isSuccessfull;
    }

    public  boolean delete(int id){
        boolean isDeleted;
        SQLiteDatabase db = this.getWritableDatabase();
        isDeleted = db.delete("Note", "id='"+id+"'",null)>0;
        db.close();
        return isDeleted;
    }

//    CRUD Method completed

}
