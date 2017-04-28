package com.slidenote.www.slidenotev2.View.Adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.slidenote.www.slidenotev2.R;

import java.util.List;
import java.util.Locale;

/**
 * Created by Cieo233 on 4/10/2017.
 */

public class ItemTouchHelperCallBack extends ItemTouchHelper.Callback {

    private static final String TAG = "ItemTouchHelperCallBack";
    private RecyclerView.ViewHolder choosedTarget;
    private OnNoteMergeListener onNoteMergeListener;
    private RecyclerView.ViewHolder draggedItem;

    public ItemTouchHelperCallBack(OnNoteMergeListener onNoteMergeListener) {
        this.onNoteMergeListener = onNoteMergeListener;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (viewHolder.getAdapterPosition() == 0) {
            return 0;
        }
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {

            int dragFlag = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlag = 0;
            return makeMovementFlags(dragFlag, swipeFlag);
        }
        return 0;
    }


    @Override
    public RecyclerView.ViewHolder chooseDropTarget(RecyclerView.ViewHolder selected, List<RecyclerView.ViewHolder> dropTargets, int curX, int curY) {
        RecyclerView.ViewHolder winner = super.chooseDropTarget(selected, dropTargets, curX, curY);
        if (choosedTarget != null){
            choosedTarget.itemView.setBackgroundResource(R.color.subColor1);
        }
        if (winner == null || winner.getAdapterPosition() == 0){
            choosedTarget = null;
            return null;
        } else {
            choosedTarget = winner;
            choosedTarget.itemView.setBackgroundResource(R.color.mainColor1);
            return winner;
        }
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG){
            draggedItem = viewHolder;
        }
        if (actionState == ItemTouchHelper.ACTION_STATE_IDLE && choosedTarget != null){
            choosedTarget.itemView.setBackgroundResource(R.color.subColor1);
            draggedItem.itemView.setVisibility(View.GONE);
            onNoteMergeListener.onNoteMerge(draggedItem.getAdapterPosition(), choosedTarget.getAdapterPosition());
            choosedTarget = null;
            draggedItem = null;
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }

    public interface OnNoteMergeListener{
        void onNoteMerge(int srcPosition, int targetPosition);
    }
}
