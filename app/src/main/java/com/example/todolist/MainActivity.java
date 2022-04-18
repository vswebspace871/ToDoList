package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ImageButton imageButton;
    ArrayList<Note> notes;
    RecyclerView recyclerView;
    NoteAdapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler);
        imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View viewInput = inflater.inflate(R.layout.note_input,null,false);
                EditText edtTitle = viewInput.findViewById(R.id.edt_title);
                EditText edtDescription = viewInput.findViewById(R.id.edt_description);

                new AlertDialog.Builder(MainActivity.this)
                        .setView(viewInput)
                        .setTitle("Add Note")
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String title = edtTitle.getText().toString();
                                String description = edtDescription.getText().toString();

                                Note note = new Note(title,description);
                                boolean isInserted = new NoteHandler(MainActivity.this).create(note);
                                if (isInserted){
                                    Toast.makeText(MainActivity.this, "Note Saved", Toast.LENGTH_SHORT).show();
                                    loadNotes();
                                }else {
                                    Toast.makeText(MainActivity.this, "Note Failed to Save", Toast.LENGTH_SHORT).show();
                                }
                                dialogInterface.cancel();

                            }
                        }).show();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

//        Swipe karke Note ko delete karne ke liye ye COde lagega

        ItemTouchHelper.SimpleCallback itemTouchCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT| ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            new NoteHandler(MainActivity.this).delete(notes.get(viewHolder.getAdapterPosition()).getId());
//            arraylist me se note ko remove krne ke liye
            notes.remove(viewHolder.getAdapterPosition());
            noteAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

//        Swipe karke Note ko delete karne ke liye ye COde lagega

        loadNotes();

    }



//    read notes
    public ArrayList<Note> readNotes(){
        ArrayList<Note> notes = new NoteHandler(this).readNotes();

        return notes;
    }

//    inflate notes in recycler view
    public void loadNotes(){
        notes = readNotes();

        noteAdapter = new NoteAdapter(notes, this, new NoteAdapter.ItemClicked() {
            @Override
            public void onClick(int position, View view) {
                editNote(notes.get(position).getId(),view);
            }
        });
        recyclerView.setAdapter(noteAdapter);
    }

    private void editNote(int noteId, View view){
        NoteHandler noteHandler = new NoteHandler(this);
        Note note = noteHandler.readSingleNote(noteId);
        Intent intent = new Intent(this,EditNote.class);
        intent.putExtra("title",note.getTitle());
        intent.putExtra("description", note.getDescription());
        intent.putExtra("id", note.getId());
//for animation
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this,view, ViewCompat.getTransitionName(view));

        startActivityForResult(intent,1,optionsCompat.toBundle());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1){
            loadNotes();
        }
    }
}