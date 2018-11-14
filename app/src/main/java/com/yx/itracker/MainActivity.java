package com.yx.itracker;

import android.content.Context;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.yx.itracker.Class.Item;
import com.yx.itracker.Class.Order;

import java.io.File;
import java.io.FileWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NewItemFragment.OnAddNewItemListener,
        AddItemFragment.OnActivityInteractionListener,
        ItemListFragment.OnChangeFragListener,
        OrderListFragment.OnFragmentInteractionListener,
        AddOrderFragment.OnOrderActivityListener,
        GraphFragment.OnGraphDataInteractionListener,
        OptionsFragment.OnOptionsInteractionListener
{
//https://stackoverflow.com/questions/39491655/communication-between-nested-fragments-in-android
///https://github.com/wasabeef/awesome-android-ui
// http://littletandog.com/tutorials.html#enterNewSale

    final Fragment fragmentHome = new HomeFragment();
    final Fragment fragmentItemList = new ItemListFragment();
    final Fragment fragmentOrderList = new OrderListFragment();
    final Fragment fragmentGraph = new GraphFragment();
    final Fragment fragmentOptions = new OptionsFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragmentHome;

    private static final String BACK_STACK_ROOT_TAG = "root_fragment";

    private List<Item> itemList;
    private HashMap<Integer, Item> itemMap;
    private List<Order> orderList;

    public void writeFileOnInternalStorage(Context mcoContext, String sFileName, String sBody){
        File file = new File(getApplicationContext().getFilesDir(),"items.txt");
        if(!file.exists()){
            file.mkdir();
        }

        try {
            FileWriter out = new FileWriter(file, true);
            out.write(sBody + "\n");
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemMapSize() {
        return itemMap.size();
    }

    @Override
    public void newItemCreated(Item item) {
        Log.d("aa","Main Activity Recieved: " + item.printPretty());
        //writeFileOnInternalStorage(this, "items.txt", item.printPretty());
        fm.beginTransaction().hide(active).show(fragmentItemList).commit();
        //active = fragmentItemList;
//        itemList.add(item);
//        updateHomeItemUI(itemList);

        itemMap.put(itemMap.size(), item);
        updateHomeItemUI(itemMap);
    }

    @Override
    public void newOrderCreated(Order order) {
        Log.d("aa", "MainActivity- New Order Recieved:" + order.toString());
        orderList.add(order);
        updateHomeOrderUI(orderList);
        Item itm = order.getItem();
        int newQuantity = 0;
        if(order.getType().equals("SALE")) {
            newQuantity = itm.getQuantity() - order.getQuantity();
        } else {
            newQuantity = itm.getQuantity() + order.getQuantity();
        }
        itemMap.get(itm.getId()).setQuantity(newQuantity);
        ItemListFragment itemListFrag = (ItemListFragment) getSupportFragmentManager().findFragmentByTag("fragmentItemList");
        List<Item> newList = new ArrayList<>();
        for(Item item : itemMap.values()) {
            newList.add(item);
        }
        itemListFrag.updateItemList(newList);
        updateHomeItemUI(itemMap);

        //Update Graph data
        GraphFragment fragGraph = (GraphFragment) getSupportFragmentManager().findFragmentByTag("fragmentGraph");
        fragGraph.updateGraphData(orderList);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //setSupportActionBar(myToolbar);

        fm.beginTransaction().add(R.id.container, fragmentHome, "fragmentHome").addToBackStack(null).commit();
        fm.beginTransaction().add(R.id.container, fragmentItemList, "fragmentItemList").addToBackStack(null).hide(fragmentItemList).commit();
        fm.beginTransaction().add(R.id.container, fragmentOrderList, "fragmentOrderList").addToBackStack(null).hide(fragmentOrderList).commit();
        fm.beginTransaction().add(R.id.container, fragmentGraph, "fragmentGraph").addToBackStack(null).hide(fragmentGraph).commit();
        fm.beginTransaction().add(R.id.container, fragmentOptions, "fragmentOptions").addToBackStack(null).hide(fragmentOptions).commit();

        fm.beginTransaction().show(fragmentHome).commit();

        //itemList = new ArrayList<Item>();
        itemMap = new HashMap<>();
        orderList = new ArrayList<Order>();

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Log.d("backstack", "Back Stack Listener Count = " + getSupportFragmentManager().getBackStackEntryCount());
            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(

                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        Fragment fragment;
                        switch (item.getItemId()) {
                            case R.id.nav_home:
                                updateActiveFrag(fragmentHome);
                                break;
                            case R.id.nav_item_list:
                                Fragment fragAddItem = fm.findFragmentByTag("fragmentAddItem");
                                if (fragAddItem != null) {
                                    updateActiveFrag(fragAddItem);
                                } else {
                                    updateActiveFrag(fragmentItemList);
                                }
                                break;
                            case R.id.nav_transaction_list:
                                Fragment fragAddOrder = fm.findFragmentByTag("fragmentAddOrder");
                                if (fragAddOrder != null) {
                                    updateActiveFrag(fragAddOrder);
                                } else {
                                    updateActiveFrag(fragmentOrderList);
                                }
                                break;
                            case R.id.nav_graphs:
                                updateActiveFrag(fragmentGraph);
                                break;
                            case R.id.nav_settings:
                                updateActiveFrag(fragmentOptions);
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                });

    }

    public void changeTitle(Fragment fragment) {
        switch(fragment.getTag()) {
            case "fragmentHome":
                setTitle("iTracker - Home");
                break;
            case "fragmentItemList":
                setTitle("iTracker - Item List");
                break;
            case "fragmentAddItem":
                setTitle("iTracker - Add New Item");
                break;
            case "fragmentOrderList":
                setTitle("iTracker - Order List");
                break;
            case "fragmentAddOrder":
                setTitle("iTracker - Add New Order");
                break;
            case "fragmentGraph":
                setTitle("iTracker - Graphs");
                break;
            case "fragmentOptions":
                setTitle("iTracker - Options");
                break;
            default:
                setTitle("iTracker");
                break;
        }
    }

    //@Override
    public void updateHomeItemUI(HashMap<Integer, Item> itemMap){
        Log.d("aa","MainActivity: updateHomeItem called.");
        HomeFragment homeFrag = (HomeFragment) getSupportFragmentManager().findFragmentByTag("fragmentHome");
        homeFrag.updateItemUI(itemMap);
    }

    public void updateHomeOrderUI(List<Order> orderList){
        Log.d("aa","MainActivity: updateHomeOrder called.");
        HomeFragment homeFrag = (HomeFragment) getSupportFragmentManager().findFragmentByTag("fragmentHome");
        homeFrag.updateOrderUI(orderList);
    }

    @Override
    public void updateActiveFrag(Fragment fragment) {
        Log.d("bb", "Hiding active frag:" + active.toString());
        fm.beginTransaction().hide(active).show(fragment).commit();
        active = fragment;
        changeTitle(active);
    }

    private void replaceFragment (Fragment fragment, String tag) {
        //https://proandroiddev.com/mastering-the-bottom-navigation-with-the-new-navigation-architecture-component-cd6a71b266ae
        //https://stackoverflow.com/questions/42795611/fragmenttransaction-hide-show-doesnt-work-sometimes/46676381#46676381


//        String backStateName = fragment.getClass().getName();
//
//        FragmentManager manager = getSupportFragmentManager();
//        //boolean fragmentExists = manager.popBackStackImmediate (backStateName, 0);
//        Fragment fragmentPopped = manager.findFragmentByTag(backStateName);
//
//        if (fragmentPopped == null){ //fragment not in back stack, create it.
//            Log.d("aa", "Adding New Frag:" + backStateName);
//            FragmentTransaction ft = manager.beginTransaction();
//            ft.replace(R.id.container, fragment, backStateName);
//            ft.addToBackStack(null);
//            ft.commit();
//        } else {
//            FragmentTransaction ft = manager.beginTransaction();
//            ft.replace(R.id.container, fragmentPopped, backStateName);
//            ft.addToBackStack(null);
//            ft.commit();
//        }

        //String tag = fragment.getClass().getName();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        switch(tag) {
            case "a":
                HomeFragment fragHome = (HomeFragment) manager.findFragmentByTag(tag);
                if(fragHome == null) {
                    Log.d("aa", "home frag not found");
                    fragHome = new HomeFragment();
                    ft.replace(R.id.container, fragHome, tag);
                    ft.addToBackStack(null);
                    ft.commit();
                } else {
                    ft.replace(R.id.container, fragHome, tag);
                    ft.commit();
                }
                break;
            case "b":
                ItemListFragment fragItemList = (ItemListFragment) manager.findFragmentByTag(tag);
                if(fragItemList == null) {
                    Log.d("aa", "item frag not found");
                    fragItemList = new ItemListFragment();
                    ft.replace(R.id.container, fragItemList, tag);
                    ft.addToBackStack(null);
                    ft.commit();
                } else {
                    ft.replace(R.id.container, fragItemList, tag);
                    ft.commit();
                }

                break;
        }


//        //String tag = fragment.getClass().getName();
//        FragmentManager manager = getSupportFragmentManager();
//        FragmentTransaction ft = manager.beginTransaction();
//        Log.d("aa", "BS Count:" + Integer.toString(manager.getBackStackEntryCount()));
//        if (manager.findFragmentByTag(tag) == null) {
//            Log.d("aa", "Adding New Frag:" + tag + "|" + manager.findFragmentByTag(tag));
//            ft.replace(R.id.container, fragment, tag);
//            ft.addToBackStack(null);
//            ft.commit();
//        } else {
//            ft.show(manager.findFragmentByTag(tag)).commit();
//            Log.d("aa", "Frag Exists");
//            //ft.replace(R.id.container, fragment).commit();
//        }
    }

    @Override
    public HashMap<Integer, Item> getItemMap() {
        return itemMap;
    }

    @Override
    public List<Order> getOrderList() {
        return orderList;
    }
}
