package com.yx.itracker;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.yx.itracker.Class.ItemCard;

import java.util.List;

public class ItemListDiffCallback extends DiffUtil.Callback {
    private final List<ItemCard> mOldItems;
    private final List<ItemCard> mNewItems;

    public ItemListDiffCallback(List<ItemCard> oldItems, List<ItemCard> newItems) {
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
        final ItemCard oldItems = mOldItems.get(oldPostition);
        final ItemCard newItems = mNewItems.get(newPosition);
        Boolean bName = oldItems.getName().equals(newItems.getName());
        Boolean bQuantity = oldItems.getQuantity() == (newItems.getQuantity());
        Boolean bSellPrice = oldItems.getSellPrice() == (newItems.getSellPrice());
        Boolean bBuyPrice = oldItems.getBuyPrice() == (newItems.getBuyPrice());

        return bName && bQuantity && bSellPrice && bBuyPrice;
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
