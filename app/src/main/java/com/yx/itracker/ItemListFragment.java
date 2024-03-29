package com.yx.itracker;


import android.content.Context;
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
import android.widget.Button;

import com.yx.itracker.Class.Item;
import com.yx.itracker.Class.ItemCard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;



public class ItemListFragment extends Fragment implements AddItemFragment.OnParentFragInteractionListener{
    private RecyclerView mRecyclerView;
    private ItemCardListAdapter mListAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Item> itemList;
    private List<ItemCard> mItemCards;


    private OnChangeFragListener mParentListener;
    public interface OnChangeFragListener {
        //void updateHome(List<Item> itemList);
        void updateActiveFrag(Fragment fragment);
    }

    public ItemListFragment() {
        // Required empty public constructor
    }

    public void updateItemList(List<Item> newList) {
        Log.d("aa", "ItemListFrag: UpdateItemList called. " + newList.toString());
        itemList = newList;
        mListAdapter.updateItemCards(generateItemCards(itemList));
    }
    @Override
    public void addToItemList(Item itm) {
        itemList.add(itm);
        mListAdapter.updateItemCards(generateItemCards(itemList));
        //mParentListener.updateHome(itemList);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnChangeFragListener) {
            mParentListener = (OnChangeFragListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public List<ItemCard> generateItemCards(List<Item> data) {
        List<ItemCard> result = new ArrayList<ItemCard>();
        for (Item itm : data) {
            result.add(new ItemCard(itm.getId(), itm.getProductId(), itm.getFirstImage(), itm.getName(), itm.getQuantity(), itm.getSellPrice(), itm.getBuyPrice()));
        }
        return result;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("aad","ItemListFragment Initi");
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        mRecyclerView = view.findViewById(R.id.rvItemList);

        itemList = new ArrayList<Item>();

        ArrayList<ItemCard> itemCardList = new ArrayList<ItemCard>();

        mListAdapter = new ItemCardListAdapter(itemCardList);
        mRecyclerView.setAdapter(mListAdapter);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

//        ///Dummy Data
//        List<String> tempItemImages = new ArrayList<String>();
//        tempItemImages.add("android.resource://com.yx.itracker/drawable/noimage");
//        itemList.add(new Item(0, new BigInteger("909933"), tempItemImages, "Apple", 20, new BigDecimal(110),new BigDecimal(100), "AA" ));
//        mListAdapter.updateItemCards(generateItemCards(itemList));
//        ///

        FloatingActionButton fab = view.findViewById(R.id.fab_addNewItem);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragmentAddItem = new AddItemFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                //FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                Fragment fragmentActive = getActivity().getSupportFragmentManager().findFragmentByTag("fragmentItemList");
                transaction.add(R.id.container,fragmentAddItem, "fragmentAddItem").addToBackStack(null);
                transaction.hide(fragmentActive).show(fragmentAddItem).commit();
                mParentListener.updateActiveFrag(fragmentAddItem);
                //transaction.replace(R.id.container, childFragment, "addItem").addToBackStack(null).commit();
            }
        });


        //readFromInternalStorage();

        return view;
    }


    public void readFromInternalStorage(){
        File file = new File(getActivity().getApplicationContext().getFilesDir(), "items.txt");

        if(!file.exists()){
            return;
        }

        List<String> result = new ArrayList<String>();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(file));
            String line = in.readLine();
            while(line != null) {
                result.add(line);
                line = in.readLine();
            }
        } catch (Exception e) { }
        Log.d("test2", "Items from Internal Storage:" + result.toString());
        //return result;
    }
}
