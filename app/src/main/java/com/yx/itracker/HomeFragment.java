package com.yx.itracker;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yx.itracker.Class.Item;
import com.yx.itracker.Class.ItemCalculations;
import com.yx.itracker.Class.Order;
import com.yx.itracker.Class.OrderCalculations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("aa", "HomeFrag: Init");
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    public void updateItemUI(HashMap<Integer, Item> itemMap) {
        List<Item> items = new ArrayList<Item>();
        for (Item item : itemMap.values()) {
            items.add(item);
        }
        ItemCalculations itemCalculations = new ItemCalculations(items);
        View view = getView();
        TextView tvTotalItems = view.findViewById(R.id.tvTotalItems);
        TextView tvTotalQuantity = view.findViewById(R.id.tvTotalQuantity);
        TextView tvOutofStock = view.findViewById(R.id.tvOutofStock);
        TextView tvLowOnStock = view.findViewById(R.id.tvLowOnStock);

        tvTotalItems.setText(String.valueOf(itemCalculations.getSize()));
        tvTotalQuantity.setText(String.valueOf(itemCalculations.getTotalQuantity()));
        tvOutofStock.setText(String.valueOf(itemCalculations.getOutOfStockCount()));
        tvLowOnStock.setText(String.valueOf(itemCalculations.getLowOnStockCount()));

    }

    public void updateOrderUI(List<Order> orders) {
        OrderCalculations orderCalculations = new OrderCalculations(orders);
        View view = getView();
        TextView tvNumberOfOrders = view.findViewById(R.id.tvNumberOfOrders);
        TextView tvItemsSold = view.findViewById(R.id.tvItemsSold);
        TextView tvProfit = view.findViewById(R.id.tvProfit);
        TextView tvRevenue = view.findViewById(R.id.tvRevenue);

        tvNumberOfOrders.setText(String.valueOf(orderCalculations.getSize()));
        tvItemsSold.setText(String.valueOf(orderCalculations.getItemsSoldCount()));
        tvRevenue.setText(String.valueOf(orderCalculations.getTotalRevenue()));
        tvProfit.setText(String.valueOf(orderCalculations.getProfitValue()));


    }

}
