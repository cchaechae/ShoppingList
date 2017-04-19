package hu.ait.shoppinglist.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import hu.ait.shoppinglist.MainActivity;
import hu.ait.shoppinglist.R;
import hu.ait.shoppinglist.data.Item;
import io.realm.internal.Collection;

/**
 * Created by chaelimseo on 4/11/17.
 */

public class ShoppingAdapter extends RecyclerView.Adapter<ShoppingAdapter.ViewHolder> {


    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView ivIcon;
        public TextView tvName;
        public TextView tvDescribe;
        public TextView tvPrice;
        public CheckBox boxBuy;
        public Button btEdit;

        public ViewHolder(View itemView){
            super(itemView);
            ivIcon = (ImageView) itemView.findViewById(R.id.ivIcon);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvDescribe = (TextView) itemView.findViewById(R.id.tvDescribe);
            boxBuy = (CheckBox) itemView.findViewById(R.id.boxBuy);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            btEdit = (Button) itemView.findViewById(R.id.btEdit);
        }
    }

    private List<Item> itemList;
    private Context context;
    private int lastPosition = -1;

    public ShoppingAdapter(List<Item> itemList, Context context){
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.tvName.setText(itemList.get(position).getItemName());
        holder.tvDescribe.setText(itemList.get(position).getDescription());
        holder.tvPrice.setText(itemList.get(position).getItemPrice());
        holder.ivIcon.setImageResource(itemList.get(position).getItemType().getId());
        holder.btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).showEditItemActivity(
                        itemList.get(holder.getAdapterPosition()).getItemID(),
                        holder.getAdapterPosition());
            }
        });

        holder.boxBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((CheckBox) v).isChecked()) {

                    ((MainActivity) context).purchaseItem(itemList.get(position));
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void addItem(Item item){
        itemList.add(item);
        notifyDataSetChanged();
    }

    public void updateItem(int i, Item item){
        itemList.set(i, item);
        notifyItemChanged(i);
    }

    public void swapPlaces(int oldPosition, int newPosition) {
        if (oldPosition < newPosition) {
            for (int i = oldPosition; i < newPosition; i++) {
                Collections.swap(itemList, i, i + 1);
            }
        } else {
            for (int i = oldPosition; i > newPosition; i--) {
                Collections.swap(itemList, i, i - 1);
            }
        }
        notifyItemMoved(oldPosition, newPosition);
    }

    public Item getItem(int i){
        return itemList.get(i);
    }

    public void removeItem(int index) {
        ((MainActivity)context).deleteItem(getItem(index));
        itemList.remove(index);
        notifyItemRemoved(index);
    }

    public void removePurchased(){

        Iterator<Item> iter = itemList.iterator();
        while(iter.hasNext()) {

            Item blah = iter.next();

            if (blah.isPurchased()) {
                ((MainActivity)context).deleteItem(blah);
                iter.remove();
            }
        }

        notifyDataSetChanged();
    }

    public void removeAll(){

        Iterator<Item> iter = itemList.iterator();
        while(iter.hasNext()) {

            Item blah = iter.next();
            ((MainActivity)context).deleteItem(blah);
            iter.remove();
        }

        notifyDataSetChanged();

    }

}


