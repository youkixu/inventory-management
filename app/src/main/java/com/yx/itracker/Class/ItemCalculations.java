package com.yx.itracker.Class;

import android.util.Log;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class ItemCalculations {
    List<Item> items;

    public ItemCalculations(List<Item> items) {
        this.items = items;
    }

    public List<Item> getItems() {
        return items;
    }

    public int getSize() {
        return items.size();
    }

    public int getTotalQuantity() {
        int total = 0;
        for(Item item : items) {
            total += item.getQuantity();
        }
        return total;
    }

    public int getLowOnStockCount() {
        int total = 0;
        for(Item item : items) {
            if(item.getQuantity() < 5)
                total++;
        }
        return total;
    }

    public int getOutOfStockCount() {
        int total = 0;
        for(Item item : items) {
            if(item.getQuantity() == 0)
                total++;
        }
        return total;
    }

    public BigDecimal getItemBuyPriceById(int id) {
        for(Item item : items) {
            if (item.getId() == id) {
                return item.getBuyPrice();
            }
        }
        Log.d("errors", "GetItemBuyPriceId - cannot find one");
        return new BigDecimal(0);
    }
}
