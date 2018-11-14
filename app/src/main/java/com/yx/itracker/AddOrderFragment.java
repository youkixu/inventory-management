package com.yx.itracker;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.travijuu.numberpicker.library.NumberPicker;
import com.yx.itracker.Class.Item;
import com.yx.itracker.Class.Order;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;



public class AddOrderFragment extends Fragment {

    TextView tvProductName;
    NumberPicker npOrderQuantity;
    TextView etOrderPrice;
    RadioButton rdPurchase;
    RadioButton rdSale;

    ArrayList<String> productList;
    SpinnerDialog spinnerDialog;
    HashMap<Integer, Item> mapItems;
    Integer selectedItemPos;

    private OnOrderActivityListener mActivityListener;
    private OnOrderParentListener mParentListener;

    public interface OnOrderActivityListener {
        void newOrderCreated(Order order);
        void updateActiveFrag(Fragment fragment);
        HashMap<Integer, Item> getItemMap();
    }

    public interface OnOrderParentListener {
        void updateOrderList(Order order);
    }

    public AddOrderFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("aa", "AddOrderFrag init");
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        mapItems = new HashMap<>();

        Button btnAdd = view.findViewById(R.id.btnOrderAdd);
        Button btnCancel = view.findViewById(R.id.btnOrderCancel);
        Button btnSelectProduct = view.findViewById(R.id.btnOrderSelectProduct);
        tvProductName =(TextView) view.findViewById(R.id.tvOrderItemName);
        npOrderQuantity = view.findViewById(R.id.npOrderQuantity);
        etOrderPrice = view.findViewById(R.id.etOrderPrice);
        rdPurchase = view.findViewById(R.id.rbOrderPurchase);
        rdSale = view.findViewById(R.id.rbOrderSale);

        productList = new ArrayList<>();

        //spinnerDialog=new SpinnerDialog(getActivity(),items,"Select or Search City","Close Button Text");// With No Animation
        //spinnerDialog=new SpinnerDialog(getActivity(),productList,"Select or Search City",R.style.DialogAnimations_SmileWindow,"Close Button Text");// With 	Animation



        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateForm()) {
                    String type = rdPurchase.isChecked() ? "PURCHASE" : "SALE";
                    //    public Order(String name, int quantity, BigDecimal price, String type, Date transactionDate, Date dateAdded) {
                    Order newOrder = new Order(mapItems.get(selectedItemPos),
                            npOrderQuantity.getValue(),
                            new BigDecimal(etOrderPrice.getText().toString()),
                            type,
                            new Date());
                    mParentListener.updateOrderList(newOrder);
                    mActivityListener.newOrderCreated(newOrder);
                    closeFragment();
                }
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFragment();
            }
        });

        btnSelectProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapItems = mActivityListener.getItemMap();

                for (Item item : mapItems.values()) {
                    productList.add(item.getName());
                }

                spinnerDialog = new SpinnerDialog(getActivity(),productList,"Select or Search Item",R.style.DialogAnimations_SmileWindow,"[Close]");
                spinnerDialog.showSpinerDialog();
                spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
                    @Override
                    public void onClick(String item, int position) {
                        selectedItemPos = position;
                        tvProductName.setText(item);
                        etOrderPrice.setText(String.valueOf(mapItems.get(position).getSellPrice().doubleValue()));
                    }
                });

            }
        });

        return view;
    }

    public boolean validateForm() {
        StringBuilder msg = new StringBuilder();
        if(tvProductName.getText().toString().equals("None")) msg.append("Please select a product.\n");
        if(!(rdPurchase.isChecked() || rdSale.isChecked())) msg.append("Please select a type.\n");
        if(npOrderQuantity.getValue() == 0) msg.append("Please enter a quantity greater than 0.\n");
        if(etOrderPrice.getText().toString().isEmpty()) msg.append("Please enter a price.");
        if(msg.toString().isEmpty()) return true;

        DialogFragment dialog = new AlertDialogFragment();
        Bundle b = new Bundle();
        b.putString("title", "Error Adding Order");
        b.putString("msg", msg.toString());
        dialog.setArguments(b);
        dialog.show(getActivity().getSupportFragmentManager(), "fragmentAlertDialog");
        return false;
    }

    public void closeFragment() {
        getActivity().getSupportFragmentManager().popBackStack();
        hideKeyboardFrom(getContext());
        Fragment active = getActivity().getSupportFragmentManager().findFragmentByTag("fragmentOrderList");
        mActivityListener.updateActiveFrag(active);

    }

    public void hideKeyboardFrom(Context context) {
        View view = getActivity().getWindow().getDecorView();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnOrderActivityListener) {
            mActivityListener = (OnOrderActivityListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        try {
            mParentListener = (OnOrderParentListener) getActivity().getSupportFragmentManager().findFragmentByTag("fragmentOrderList");
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnParentFragInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivityListener = null;
        mParentListener = null;
    }

}
