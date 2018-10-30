package com.yx.itracker.Class;

import android.util.Log;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

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

//    public double getBuyValue() {
//        BigDecimal sum = new BigDecimal(0);
//        sum.setScale(2, RoundingMode.CEILING);
//        for(Order order : orders) {
//            BigDecimal qty = new BigDecimal(itm.getQuantity());
//            sum = sum.add(itm.getBuyPrice().multiply(qty));
//        }
//        return sum.doubleValue();
//    }

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
}

//getSellValue
//        BigDecimal sum = new BigDecimal(0);
//        sum.setScale(2, RoundingMode.CEILING);
//        BigDecimal result;
//        for(Order order : orders) {
//            BigDecimal qty = new BigDecimal(order.getQuantity());
//            sum = sum.add(order.getSellPrice().multiply(qty));
//            Log.d("aa", "GetSellVal:" + Integer.toString(sum.intValue()));
//        }
//return sum.doubleValue();