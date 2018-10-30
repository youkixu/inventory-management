package com.yx.itracker;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yx.itracker.Class.Item;
import com.yx.itracker.Class.ItemCard;
import com.yx.itracker.Class.Order;
import com.yx.itracker.Class.OrderCard;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderListFragment extends Fragment implements AddOrderFragment.OnOrderParentListener{
    private RecyclerView mRecyclerView;
    private OrderCardListAdapter mListAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Order> orderList;
    private List<OrderCard> orderCardList;
    private OnFragmentInteractionListener mActivityListener;
    private FragmentManager fm;
    private Fragment fragmentAddOrder;

    public interface OnFragmentInteractionListener {
        void updateActiveFrag(Fragment fragment);
    }

    public OrderListFragment() {
        // Required empty public constructor
    }


    @Override
    public void updateOrderList(Order order){
        orderList.add(order);
        orderCardList.add(createCard(order));
        mListAdapter.updateItemCards(orderCardList);

        Log.d("aa", "OrderListFrag recieved");
    }

    public OrderCard createCard(Order order) {
        return new OrderCard(order.getItem().getId(), order.getItem().getName(), order.getQuantity(), order.getPrice(), order.getType(), order.getDateAdded());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);
        Log.d("aa","OrderListFrag Initi");

        mRecyclerView = view.findViewById(R.id.rvOrderList);

        orderList = new ArrayList<Order>();
        orderCardList = new ArrayList<OrderCard>();

        ArrayList<OrderCard> orderCardList = new ArrayList<OrderCard>();
        mListAdapter = new OrderCardListAdapter(orderCardList, getContext());
        mRecyclerView.setAdapter(mListAdapter);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

//        //DUMMY DATA
//        List<String> tempItemImages = new ArrayList<String>();
//        tempItemImages.add("android.resource://com.yx.itracker/drawable/noimage");
//        Item itm = new Item(0, new BigInteger("909933"), tempItemImages, "Apple", 20, new BigDecimal(110),new BigDecimal(100), "AA" );
//        Order o1 = new Order(itm, 5, new BigDecimal(130), "SALE", new Date());
//        Order o2 = new Order(itm, 2, new BigDecimal(150), "SALE", new Date());
//        updateOrderList(o1);
//        updateOrderList(o2);
//        ///

        fm = getActivity().getSupportFragmentManager();

        FloatingActionButton fab = view.findViewById(R.id.fab_addNewOrder);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentAddOrder = new AddOrderFragment();
                fm.beginTransaction().add(R.id.container,fragmentAddOrder, "fragmentAddOrder").addToBackStack(null).hide(fragmentAddOrder).commit();
                Fragment fragmentActive = getActivity().getSupportFragmentManager().findFragmentByTag("fragmentOrderList");
                fm.beginTransaction().hide(fragmentActive).show(fragmentAddOrder).commit();
                mActivityListener.updateActiveFrag(fragmentAddOrder);
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mActivityListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivityListener = null;
    }


}
