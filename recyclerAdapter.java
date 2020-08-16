/*
Author: Abdirahman Hassan
Description:
           RecyclerView adapter that populates data into RecyclerView,updates data and listens for the user clicks and swiping gestures

 */
package com.example.inote.Adapter;

import android.content.ClipData;
import android.content.Context;
import android.os.SystemClock;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inote.Model.noteDatails;
import com.example.inote.R;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.Holder> implements onSwipeListner{
    private LayoutInflater mInfalter;
    private Realm mrealm;
    protected RealmResults<noteDatails> realmResults;
    private Context context;
    private noteOnclickListner noteClick;

    public recyclerAdapter(Context context, RealmResults<noteDatails> realmResults, Realm realm, noteOnclickListner noteClick){
        this.mrealm=realm;
        mInfalter=LayoutInflater.from(context);
        update(realmResults);
        this.context=context;
        this.noteClick=noteClick;


    }
    public void update(RealmResults<noteDatails> realmResults){
        this.realmResults=realmResults;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public recyclerAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= mInfalter.inflate(R.layout.card_rows,parent,false);

        Holder hold=new Holder(view,noteClick);
        return hold;
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerAdapter.Holder holder, int position) {
        noteDatails details=realmResults.get(position);
        holder.date.setText("Last edited "+DateUtils.getRelativeTimeSpanString(details.getLastEdited(),System.currentTimeMillis(),DateUtils.DAY_IN_MILLIS,0));

        holder.title.setText(details.getTitle());




    }
    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public int getItemCount() {
        return realmResults.size();
    }

    @Override
    public void onSwipeAction(int position,int direction) {
        mrealm.beginTransaction();
        if(direction== ItemTouchHelper.RIGHT) {

            realmResults.get(position).deleteFromRealm();
            mrealm.commitTransaction();

            Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show();
        }




    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView title,date;
        public ImageButton archivePlan,completion;
        noteOnclickListner noteClick;


        public Holder(@NonNull View itemView,noteOnclickListner noteClick) {
            super(itemView);
            title=itemView.findViewById(R.id.rowTitle);
            date=itemView.findViewById(R.id.rowdate);
            this.noteClick=noteClick;

            itemView.setOnClickListener((View.OnClickListener) this);



        }


        @Override
        public void onClick(View view) {
            noteClick.noteClick(getAdapterPosition());

        }
    }
    public interface noteOnclickListner{
        void noteClick(int postion);
    }
}
