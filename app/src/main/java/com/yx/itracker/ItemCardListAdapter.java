package com.yx.itracker;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yx.itracker.Class.ItemCard;

import java.util.ArrayList;
import java.util.List;

// Sorting
//https://android.jlelse.eu/smart-way-to-update-recyclerview-using-diffutil-345941a160e0
// https://medium.com/@Pang_Yao/android-fragment-use-recyclerview-cardview-4bc10beac446
//https://willowtreeapps.com/ideas/android-fundamentals-working-with-the-recyclerview-adapter-and-viewholder-pattern/
public class ItemCardListAdapter extends RecyclerView.Adapter<ItemCardListAdapter.ViewHolder>
{
    private ArrayList<ItemCard> dataList;

    public ItemCardListAdapter(ArrayList<ItemCard> data)
    {
        this.dataList = data;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvItemCardName;
        TextView tvItemCardQuantity;
        TextView tvItemCardSellPrice;
        TextView tvItemCardBuyPrice;
        TextView tvItemCardStockSellValue;
        ImageView ivItemCardItem;

        public ViewHolder(View itemView)
        {
            super(itemView);
            this.tvItemCardName = itemView.findViewById(R.id.tvOrderCardName);
            this.tvItemCardQuantity = itemView.findViewById(R.id.tvItemCardQuantity);
            this.tvItemCardSellPrice = itemView.findViewById(R.id.tvItemCardSellPrice);
            this.tvItemCardBuyPrice = itemView.findViewById(R.id.tvItemCardBuyPrice);
            this.tvItemCardStockSellValue = itemView.findViewById(R.id.tvItemCardStockSellValue);
            this.ivItemCardItem = itemView.findViewById(R.id.ivItemCardItem);
        }

    }

    @NonNull
    @Override
    public ItemCardListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        viewHolder.tvItemCardName.setText(dataList.get(position).getName());
        viewHolder.tvItemCardQuantity.setText(Integer.toString(dataList.get(position).getQuantity()));
        viewHolder.tvItemCardSellPrice.setText(dataList.get(position).getSellPrice().toString());
        viewHolder.tvItemCardBuyPrice.setText(dataList.get(position).getBuyPrice().toString());
        viewHolder.tvItemCardStockSellValue.setText(dataList.get(position).getSellValue().toString());
        try {
            //Log.d("aab", "Loading Image");
            Picasso.get()
                    .load(dataList.get(position).getFirstImage())
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.drawable.baseline_error_black_48)
                    .into(viewHolder.ivItemCardItem);
        } catch (Exception e) {
            Log.d("error","Error with loading image");
        }
//        viewHolder.itemView
//        viewHolder.tvQuantity.setText(Integer.toString(dataList.get(position).getQuantity()));
//
//        viewHolder.itemView.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                Log.d("aa", "Card Clicked:" + viewHolder.tvName.getText());
//            }
//        });

    }

    public void updateItemCards(List<ItemCard> itemCards) {
        final ItemListDiffCallback diffCallback = new ItemListDiffCallback(this.dataList, itemCards);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.dataList.clear();
        this.dataList.addAll(itemCards);
        diffResult.dispatchUpdatesTo(this);
    }

    @Override
    public int getItemCount()
    {
        return dataList.size();
    }



}