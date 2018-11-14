package com.yx.itracker.Class;

import android.util.Log;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class OrderCalculations {
    List<Order> orders;

    public OrderCalculations(List<Order> orders) {
        this.orders = orders;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public int getSize() { return orders.size(); }

    public int getItemsSoldCount() {
        int count = 0;
        for(Order order : orders) {
            if (order.getType() .equals("SALE"))
                count += order.getQuantity();
        }
        return count;
    }

    public double getTotalRevenue() {
        BigDecimal sum = new BigDecimal(0);
        BigDecimal profit;
        sum.setScale(2, RoundingMode.CEILING);
        for(Order order : orders) {
            if(order.getType().equals("SALE")) {
                profit = order.getPrice().multiply(new BigDecimal(order.getQuantity()));
                sum = sum.add(profit);
            }
        }
        return sum.doubleValue();
    }

    public double getProfitValue() {
        BigDecimal sum = new BigDecimal(0);
        sum.setScale(2, RoundingMode.CEILING);
        for(Order order : orders) {
            BigDecimal cost = order.getPrice().subtract(order.getItem().getBuyPrice());
            BigDecimal profit = cost.multiply(new BigDecimal(order.getQuantity()));
            sum = sum.add(profit);
        }
        return sum.doubleValue();
    }

    public HashMap<Long, Float> getTimeProfitMap() {
        HashMap<Long, Float> map = new LinkedHashMap<>(); //Hashmap that is ordered

        BigDecimal sum = new BigDecimal(0).setScale(2, RoundingMode.CEILING);
        for(Order order : orders) {
            BigDecimal cost = order.getPrice().subtract(order.getItem().getBuyPrice());
            BigDecimal profit = cost.multiply(new BigDecimal(order.getQuantity()));
            sum = sum.add(profit);
            long unixTsTimeAdded = order.getDateAdded().getTime() / 1000;
            map.put((unixTsTimeAdded), sum.floatValue());
        }
        return map;
    }
}
