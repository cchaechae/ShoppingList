package hu.ait.shoppinglist.touch;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import hu.ait.shoppinglist.adapter.ShoppingAdapter;

/**
 * Created by chaelimseo on 4/11/17.
 */

public class ShoppingListTouchHelper extends ItemTouchHelper.Callback{

    private ShoppingAdapter itemTouchHelperAdapter;

    public ShoppingListTouchHelper(ShoppingAdapter itemTouchHelperAdapter) {
        this.itemTouchHelperAdapter = itemTouchHelperAdapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView,
                          RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        itemTouchHelperAdapter.swapPlaces(viewHolder.getAdapterPosition(),
                target.getAdapterPosition());

        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        itemTouchHelperAdapter.removeItem(viewHolder.getAdapterPosition());
    }

}
