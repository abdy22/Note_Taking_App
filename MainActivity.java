/*
Author: Abdirahman Hassan
Description: Displays the notes list to the user, Lets user sort notes, add notes and save any changes to Realm database.
 */





package com.example.inote.Controller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.inote.Adapter.recyclerAdapter;
import com.example.inote.Adapter.touchCallback;
import com.example.inote.Model.noteDatails;
import com.example.inote.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements recyclerAdapter.noteOnclickListner {
    private FloatingActionButton addPNewPlan;
    private FloatingActionButton menuChoices;
    private TextView welcomeMessage;
    private touchCallback callback;
    private ItemTouchHelper helper;
    private List<View> notEmpty= Collections.emptyList();
    private List<View> Empty= Collections.emptyList();
    private View emptyLayout;
    private TextView notifier,TopNotifier,numOfNotes;



    private RecyclerView recyclerView;
    private ArrayList<String> data;
    private Realm mRealm;
    private RealmResults<noteDatails> results;
    private noteDatails details;
    private recyclerAdapter adapter;
    private ImageView addFromEmptyView;

    private String planTitle;
    private Long planDate;





    private static final int NEW_NOTE_REQUEST=01;
    private static final int VIEW_AND_EDIT_NOTE_REQUEST=02;

    private RealmChangeListener listener=new RealmChangeListener() {
        @SuppressLint("RestrictedApi")
        @Override
        public void onChange(Object o) {
            adapter.update(results);
            if(adapter.getItemCount()==0){
                recyclerView.setVisibility(View.GONE);
                notifier.setVisibility(View.VISIBLE);
                TopNotifier.setVisibility(View.GONE);
                numOfNotes.setVisibility(View.GONE);
                addFromEmptyView.setVisibility(View.VISIBLE);
                menuChoices.setVisibility(View.GONE);
                addPNewPlan.setVisibility(View.GONE);

            }
            else {
                recyclerView.setVisibility(View.VISIBLE);
                TopNotifier.setVisibility(View.VISIBLE);
                numOfNotes.setVisibility(View.VISIBLE);
                notifier.setVisibility(View.GONE);
                addFromEmptyView.setVisibility(View.GONE);
                menuChoices.setVisibility(View.VISIBLE);
                addPNewPlan.setVisibility(View.VISIBLE);
                if(results.size()==1)
                  numOfNotes.setText(results.size()+" note");
                else
                    numOfNotes.setText(results.size()+" notes");


            }


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addPNewPlan=findViewById(R.id.floatingAdd);
        menuChoices=findViewById(R.id.menu_items);
        recyclerView = findViewById(R.id.recycler);
        mRealm = Realm.getDefaultInstance();
        results = mRealm.where(noteDatails.class).findAllAsync();//returns a list of notes in the Realm database
        adapter = new recyclerAdapter(this, results, mRealm, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        notifier=findViewById(R.id.notifier);
        addFromEmptyView = findViewById(R.id.addFromEmptyRV);
        TopNotifier=findViewById(R.id.TopNotifier);
        numOfNotes=findViewById(R.id.numOfNotes);


        CollapsingToolbarLayout collap=findViewById(R.id.collapsing);
        collap.setTitle("To do");
        collap.setMinimumHeight(100);
        Log.d("Main", "onCreate: is called");
    }

    @Override
    protected void onStart() {
        super.onStart();
        //emptyLayout=findViewById(R.id.emptyRecycler);
        Log.d("Main", "onStart: is called");




        callback = new touchCallback(adapter);
        helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);
        results.addChangeListener(listener);



    }

    @Override
    protected void onResume() {
        super.onResume();
        results = mRealm.where(noteDatails.class).findAllAsync();
        results.addChangeListener(listener);
        Log.d("Main", "onResume: is called");



    }

    private List<View> getEmptyViewList(View ...view){
        Empty= Arrays.asList(view);
        return Empty;
    }
    private List<View> getNotEmptyViewList(View ...view){
        notEmpty= Arrays.asList(view);
        return notEmpty;
    }
    @Override
    protected void onStop() {
        super.onStop();
        results.removeChangeListener(listener);
        Log.d("Main", "onStop: is called");

    }
    public void addNewNote(View view){
        Intent intent=new Intent(this,addNote.class);
        startActivityForResult(intent,NEW_NOTE_REQUEST);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(NEW_NOTE_REQUEST==requestCode)
            if(resultCode==RESULT_OK){
                String what=data.getStringExtra("what");
                long date=data.getLongExtra("date",Calendar.getInstance().getTimeInMillis());
                String Title=data.getStringExtra("title");




                details=new noteDatails(date,Calendar.getInstance().getTimeInMillis(),Title,what);

                mRealm.beginTransaction();
                mRealm.copyToRealm(details);

                mRealm.commitTransaction();
                results.addChangeListener(listener);
                //Log.d("Main", "onActivityResult: request data "+requestCode);

            }

        if(VIEW_AND_EDIT_NOTE_REQUEST==requestCode)
            if(resultCode==RESULT_OK){
                int pos=data.getIntExtra("pos",-1);
                String title=data.getStringExtra("title");
                String what=data.getStringExtra("what");
                Long date=data.getLongExtra("date", SystemClock.elapsedRealtime());
                if(pos>-1){
                    mRealm.beginTransaction();
                    noteDatails details=results.get(pos);
                    details.setTitle(title);
                    details.setWhat(what);
                    details.setLastEdited(date);
                    mRealm.commitTransaction();
                    adapter.notifyItemChanged(pos);
                    results.addChangeListener(listener);
                    Log.d("Main", "onActivityResult: what data "+details.getWhat());
                }

            }
    }


    @Override
    public void noteClick(int postion) {
        noteDatails details=results.get(postion);
        Intent viewAndEdit=new Intent(this,addNote.class);
        viewAndEdit.putExtra("what",details.getWhat());
        viewAndEdit.putExtra("title",details.getTitle());
        viewAndEdit.putExtra("pos",postion);
        viewAndEdit.putExtra("edit",true);
        startActivityForResult(viewAndEdit,VIEW_AND_EDIT_NOTE_REQUEST);

    }

    public void popMenu(View view) {
        PopupMenu pop=new PopupMenu(this,view);
        pop.inflate(R.menu.selection);
        pop.show();
        pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.SORT:
                        return true;
                    case R.id.delete_All:
                        mRealm.beginTransaction();
                        mRealm.deleteAll();
                        mRealm.commitTransaction();

                        return true;

                    case R.id.Last_Edited:

                        results=mRealm.where(noteDatails.class).sort("lastEdited").findAllAsync();
                        results.addChangeListener(listener);
                        return true;

                    case R.id.recentlyCreated:

                        results=mRealm.where(noteDatails.class).sort("whenCreated").findAllAsync();
                        results.addChangeListener(listener);
                        return true;


                    default:
                        return false;

                }
            }
        });
    }
}