package com.yx.itracker;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.yx.itracker.Class.ItemCard;
import com.yx.itracker.Class.Order;
import com.yx.itracker.Class.OrderCard;

import java.util.List;

public class OrderListDiffCallback extends DiffUtil.Callback {
    private final List<OrderCard> mOldItems;
    private final List<OrderCard> mNewItems;

    public OrderListDiffCallback(List<OrderCard> oldItems, List<OrderCard> newItems) {
        this.mOldItems = oldItems;
        this.mNewItems = newItems;
    }

    @Override
    public int getOldListSize() {
        return mOldItems.size();
    }

    @Override
    public int getNewListSize() {
        return mNewItems.size();
    }

    @Override
    public boolean areItemsTheSame(int oldPostition, int newPosition) {
        return mOldItems.get(oldPostition).getId() == mNewItems.get(newPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldPostition, int newPosition) {
        final OrderCard oldItems = mOldItems.get(oldPostition);
        final OrderCard newItems = mNewItems.get(newPosition);
        Boolean bName = oldItems.getName().equals(newItems.getName());
        Boolean bQuantity = oldItems.getQuantity() == (newItems.getQuantity());
        Boolean bPrice = oldItems.getPrice().equals(newItems.getPrice());
        Boolean bType = oldItems.getType().equals(newItems.getType());
        return bName && bQuantity && bPrice && bType;
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
