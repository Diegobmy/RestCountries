package com.moyses.diego.restcountres.CountriesList.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;

import com.moyses.diego.restcountres.R;

public class CountriesSwipeCallback extends ItemTouchHelper.SimpleCallback {

    private Context context;
    private CountriesListAdapter countriesListAdapter;

    //controls if the view is locked beyond the anchor
    private boolean lockButton;
    private final int deleteButtonAnchor = 300;

    public CountriesSwipeCallback(Context context, CountriesListAdapter countriesListAdapter) {
        super(0, ItemTouchHelper.LEFT);
        this.context = context;
        this.countriesListAdapter = countriesListAdapter;
    }

    public void attachToRecyclerView(RecyclerView recyclerView) {
        //attach the callback to the recycler view using ItemTouchHelper
        new ItemTouchHelper(this).attachToRecyclerView(recyclerView);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        countriesListAdapter.removeItem(viewHolder.getAdapterPosition());
        lockButton = false;
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

            View itemView = viewHolder.itemView;

            // if the view is locked make its minimal x position be -300
            if (lockButton) {
                dX = Math.min(dX, -deleteButtonAnchor);
            }

            // if the item is dragged to the left, draw the delete button
            if (dX < 0) {
                drawDeleteButton(c, dX, itemView);
            }

            //sets a touch listener for the item
            setTouch(recyclerView, dX, viewHolder.getAdapterPosition(), itemView);
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    private void drawDeleteButton(Canvas c, float dX, View itemView) {
        int height = itemView.getBottom() - itemView.getTop();
        int width = height / 4;

        //draws the red background
        Paint p = new Paint();
        p.setColor(ContextCompat.getColor(context, R.color.delete_color));
        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
        c.drawRect(background, p);

        // draws the bomb icon for the delete button
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_delete);
        if (drawable != null) {
            Rect drawableBound = new Rect(itemView.getRight() - 3 * width, itemView.getTop() + width, itemView.getRight() - width, itemView.getBottom() - width);
            drawable.setBounds(drawableBound);
            drawable.draw(c);
        }
    }

    private void setTouch(View view, float dX, int position, View itemView) {
        view.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getAction() == MotionEvent.ACTION_UP && lockButton) {
                    view.performClick();
                    if (touchIsOnDelete(itemView, v, event)) {
                        //if the touch is inside remove the item from adapter
                        countriesListAdapter.removeItem(position);
                    }
                    //sets the lockButton as false and resets the item view
                    lockButton = false;
                    countriesListAdapter.notifyItemChanged(position);
                }
                // if the x position is beyond the anchor, sets lockButton as true
                if (Math.abs(dX) > deleteButtonAnchor) {
                    lockButton = true;
                }
            }
            return false;
        });
    }

    private boolean touchIsOnDelete(View itemView, View v, MotionEvent event) {
        // returns true if the touch event was inside the delete button
        return event.getX() >= v.getWidth() - deleteButtonAnchor && event.getY() > itemView.getTop() && event.getY() < itemView.getBottom();
    }
}
