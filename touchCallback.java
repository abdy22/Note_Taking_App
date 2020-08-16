/*
Author: Abdirahman Hassan
Description: A class that changes visual Views in response to the user interaction such as swipes,long press and dragging



 */


package com.example.inote.Adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class touchCallback extends ItemTouchHelper.Callback {
    private onSwipeListner listner;
    private int direction;
    public touchCallback( onSwipeListner listner) {
        this.listner=listner;

    }
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0,ItemTouchHelper.RIGHT);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
            return true;
    }


    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        this.direction=direction;
        listner.onSwipeAction(viewHolder.getAdapterPosition(),direction);


    }
}
