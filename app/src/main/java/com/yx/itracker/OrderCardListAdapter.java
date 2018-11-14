package com.yx.itracker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yx.itracker.Class.OrderCard;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// Sorting
//https://android.jlelse.eu/smart-way-to-update-recyclerview-using-diffutil-345941a160e0
// https://medium.com/@Pang_Yao/android-fragment-use-recyclerview-cardview-4bc10beac446
//https://willowtreeapps.com/ideas/android-fundamentals-working-with-the-recyclerview-adapter-and-viewholder-pattern/
public class OrderCardListAdapter extends RecyclerView.Adapter<OrderCardListAdapter.ViewHolder>
{
    private ArrayList<OrderCard> dataList;
    private Context mContext;

    public OrderCardListAdapter(ArrayList<OrderCard> data, Context context)
    {
        this.dataList = data;
        this.mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvOrderCardName;
        TextView tvOrderCardDate;
        TextView tvOrderCardType;
        //TextView tvOrderCardPrice;
        TextView tvOrderCardQuantity;
        TextView tvOrderCardValue;


        public ViewHolder(View itemView)
        {
            super(itemView);
            this.tvOrderCardName = itemView.findViewById(R.id.tvOrderCardName);
            this.tvOrderCardDate = itemView.findViewById(R.id.tvOrderCardDate);
            this.tvOrderCardType = itemView.findViewById(R.id.tvOrderCardType);
            //this.tvOrderCardPrice = itemView.findViewById(R.id.tvOrderCardPrice);
            this.tvOrderCardQuantity = itemView.findViewById(R.id.tvOrderCardQuantity);
            this.tvOrderCardValue = itemView.findViewById(R.id.tvOrderCardValue);

        }


    }

    @NonNull
    @Override
    public OrderCardListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_order, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        viewHolder.tvOrderCardName.setText(dataList.get(position).getName());
        viewHolder.tvOrderCardDate.setText(dataList.get(position).getDateAddedFormatted());
        viewHolder.tvOrderCardType.setText(dataList.get(position).getType());
        //viewHolder.tvOrderCardPrice.setText("Price: " + Double.toString((dataList.get(position).getPrice()).doubleValue()));
        viewHolder.tvOrderCardQuantity.setText(mContext.getString(R.string.order_card_quantity, dataList.get(position).getQuantity()));
        Double value = dataList.get(position).getPrice().multiply(new BigDecimal(dataList.get(position).getQuantity())).setScale(2, BigDecimal.ROUND_UP).doubleValue();
        Log.d("aa", Double.toString(value));
        viewHolder.tvOrderCardValue.setText(mContext.getString(R.string.order_card_value, value));
    }

    public void updateItemCards(List<OrderCard> orderCards) {
        final OrderListDiffCallback diffCallback = new OrderListDiffCallback(this.dataList, orderCards);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.dataList.clear();
        this.dataList.addAll(orderCards);
        diffResult.dispatchUpdatesTo(this);
    }

    @Override
    public int getItemCount()
    {
        return dataList.size();
    }



}