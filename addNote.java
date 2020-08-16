/*
Author: Abdirahman Hassan
Description: An Activity  that lets user add note



 */

package com.example.inote.Controller;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;

import com.example.inote.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.Calendar;

public class addNote extends AppCompatActivity {
    private static final int EXTRACTION_REQUEST_CODE = 001;
    private EditText noteTitle,noteText;
    private ImageView add,cancel;
    private boolean isEdit=false;
    FloatingActionButton extractActivity;
    private  static final int NEW_NOTE_REQUEST=01;
    private static final int VIEW_AND_EDIT_NOTE_REQUEST=02;
    String what,Title;
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        noteText=findViewById(R.id.noteText);
        noteTitle=findViewById(R.id.noteTitleText);
        add=findViewById(R.id.addNote);
        extractActivity=findViewById(R.id.extractTextActivity);
        extractActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent extractionIntent=new Intent(addNote.this,extractTextActivity.class);
                startActivityForResult(extractionIntent,EXTRACTION_REQUEST_CODE);
            }
        });


        Bundle extras=getIntent().getExtras();
        try {
            what=  extras.getString("what");
            Title=(String)extras.getString("title");
            pos=extras.getInt("pos");
            boolean toEdit=extras.getBoolean("edit");
            if(what!=null&&Title!=null&&toEdit==true){
                noteTitle.setText(Title);
                noteText.setText(what);
                isEdit=toEdit;
            }

        }catch (Exception e){

        }


        cancel=findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent= new Intent();
                setResult(RESULT_CANCELED,returnIntent);
                finish();

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(EXTRACTION_REQUEST_CODE==requestCode)
            if(resultCode==RESULT_OK){
                if(!noteText.getText().toString().isEmpty()){
                    String temp=noteText.getText().toString();
                    String extracted=data.getStringExtra("X");
                    temp+=extracted;
                    noteText.setText(temp);
                }
                else{
                    String extracted=data.getStringExtra("X");
                    Log.d("Add Note", "onActivityResult: "+extracted);
                    noteText.setText(extracted);
                }

            }


    }

    public void saveNote(View view){
        if(noteText.getText().toString().isEmpty()) {
            noteText.setError("Please enter the Note text");
            noteText.requestFocus();
        }
        else{
            if(isEdit==false) {

                if (noteTitle.getText().toString().isEmpty() || noteTitle.getText().toString() == "")
                    noteTitle.setText("No title");


                    Long date;
                    Calendar calendar = Calendar.getInstance();
                    date = calendar.getTimeInMillis();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("what", noteText.getText().toString());
                    returnIntent.putExtra("title", noteTitle.getText().toString());
                    returnIntent.putExtra("date", date);


                    setResult(RESULT_OK, returnIntent);
                    finish();
            }
            else if(isEdit==true){
                Long date;
                Calendar calendar = Calendar.getInstance();
                date = calendar.getTimeInMillis();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("what", noteText.getText().toString());
                returnIntent.putExtra("title", noteTitle.getText().toString());
                returnIntent.putExtra("pos", pos);
                returnIntent.putExtra("date", date);
                setResult(RESULT_OK, returnIntent);
                finish();


            }


        }

    }
}