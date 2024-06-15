package com.example.whattodo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.core.content.ContextCompat;

import com.example.whattodo.Adapter.TodoAdapter;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback{
    private TodoAdapter adapter;

    public RecyclerItemTouchHelper(TodoAdapter adapter){
        super(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target ){
        return false;
    }

    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction){
        final int position = viewHolder.getAdapterPosition();
        if (direction == ItemTouchHelper.LEFT){
            AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext());
            builder.setTitle("Delete Task");
            builder.setMessage("Are you sure ??");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adapter.deleteItem(position);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adapter.notifyItemChanged(viewHolder.getAdapterPosition());
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        }
        else {
            adapter.editItem(position);
        }
    }
    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dx, float dy, int actionState, boolean isCurrentlyActive){
        super.onChildDraw(c, recyclerView, viewHolder, dx, dy, actionState, isCurrentlyActive);

        Drawable icon;
        ColorDrawable background;

        View itemView = viewHolder.itemView;
        int backgroundCornerOffSet =20;

        //Right swipe function listener
        if(dx>0){
            icon= ContextCompat.getDrawable(adapter.getContext(),R.drawable.baseline_edit_note_24);
            background = new ColorDrawable(ContextCompat.getColor(adapter.getContext(),R.color.colorPrimaryDark));
        }
        else{
            icon= ContextCompat.getDrawable(adapter.getContext(),R.drawable.baseline_delete);
            background = new ColorDrawable(Color.RED);
        }
        int iconMargin = (itemView.getHeight()- icon.getIntrinsicHeight())/2;
        int iconTop = itemView.getTop() + (itemView.getHeight()) - icon.getIntrinsicHeight() /2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();

        //left swipe listener function
        if (dx>0){
            int iconLeft = itemView.getLeft() + iconMargin;
            int iconRight = itemView.getLeft() + iconMargin +icon.getIntrinsicWidth();
            icon.setBounds(iconLeft,iconTop, iconRight,iconBottom);
            
            background.setBounds(itemView.getLeft(),itemView.getTop(), itemView.getLeft() + ((int)dx) + backgroundCornerOffSet, itemView.getBottom());
            
        } else if (dx<0) {
            int iconLeft = itemView.getLeft() - iconMargin - icon.getIntrinsicWidth();
            int iconRight = itemView.getLeft() - iconMargin;
            icon.setBounds(iconLeft,iconTop, iconRight,iconBottom);

            background.setBounds(itemView.getRight() +((int)dx) - backgroundCornerOffSet,itemView.getTop(),
                    itemView.getRight(), itemView.getBottom());
        }
        else {
            background.setBounds(0,0,0,0);
        }
        background.draw(c);
        icon.draw(c);
    }

}
