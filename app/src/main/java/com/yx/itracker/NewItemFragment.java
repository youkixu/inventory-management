package com.yx.itracker;


import android.app.Activity;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


//https://codelabs.developers.google.com/codelabs/mdc-101-java/#4
//https://www.dev2qa.com/android-pick-multiple-image-from-gallery-example/
//https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
public class NewItemFragment extends Fragment {
    OnNewItemListener mParentFragmentListener;
    OnAddNewItemListener mActivityListener;

    private TextInputEditText etName;
    private TextInputEditText etId;
    private TextInputEditText etQuantity;
    private TextInputEditText etSellPrice;
    private TextInputEditText etBuyPrice;
    private TextInputEditText etNotes;

    public NewItemFragment() {
        // Required empty public constructor
    }

    public interface OnNewItemListener {
        void onAddNewItem(String name, String id, String Quantity, String sellPrice, String buyPrice, String notes);
    }

    public interface OnAddNewItemListener {
        void updateActiveFrag(Fragment fragment);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mActivityListener = (OnAddNewItemListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnAddNewItemListener");
        }

        if (getParentFragment() instanceof OnNewItemListener) {
            mParentFragmentListener = (OnNewItemListener) getParentFragment();
        } else {
            throw new RuntimeException("The parent fragment must implement OnChildFragmentInteractionListener");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_new_item, container, false);
        etName = view.findViewById(R.id.etName);
        etId = view.findViewById(R.id.etId);
        etQuantity = view.findViewById(R.id.etQuantity);
        etSellPrice = view.findViewById(R.id.etSellPrice);
        etBuyPrice = view.findViewById(R.id.etBuyPrice);
        etNotes = view.findViewById(R.id.etNotes);
        Button btnAdd = view.findViewById(R.id.btnAdd);
        Button btnCancel = view.findViewById(R.id.btnCancel);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (validateForm(view)) {
                    mParentFragmentListener.onAddNewItem(
                            etName.getText().toString(),
                            etId.getText().toString(),
                            etQuantity.getText().toString(),
                            etSellPrice.getText().toString(),
                            etBuyPrice.getText().toString(),
                            etNotes.getText().toString());
                    closeFragment();
                }
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFragment();
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    public void closeFragment() {
        getActivity().getSupportFragmentManager().popBackStack();
        hideKeyboardFrom(getContext());
        Fragment active = getActivity().getSupportFragmentManager().findFragmentByTag("fragmentItemList");
        mActivityListener.updateActiveFrag(active);

    }

    public void hideKeyboardFrom(Context context) {
        View view = getActivity().getWindow().getDecorView();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public boolean validateForm(View view) {
        if (!(etName.getText().toString().isEmpty()
                && etId.getText().toString().isEmpty()
                && etQuantity.getText().toString().isEmpty()
                && etSellPrice.getText().toString().isEmpty()
                && etBuyPrice.getText().toString().isEmpty()
        )){
            return true;
        }

        TextInputLayout tilName = (TextInputLayout) view.findViewById(R.id.txtInputLayoutName);
        TextInputLayout tilId = (TextInputLayout) view.findViewById(R.id.txtInputLayoutId);
        TextInputLayout tilQuantity = (TextInputLayout) view.findViewById(R.id.txtInputLayoutQuantity);
        TextInputLayout tilSellPrice = (TextInputLayout) view.findViewById(R.id.txtInputLayoutSellPrice);
        TextInputLayout tilBuyPrice = (TextInputLayout) view.findViewById(R.id.txtInputLayoutBuyPrice);

        if (etName.getText().toString().isEmpty()) tilName.setError("Please enter a name.");
        if (etId.getText().toString().isEmpty()) tilId.setError("Please enter a id.");
        if (etQuantity.getText().toString().isEmpty()) tilQuantity.setError("Please enter a quantity.");
        if (etSellPrice.getText().toString().isEmpty()) tilSellPrice.setError("Please enter a sell price.");
        if (etBuyPrice.getText().toString().isEmpty()) tilBuyPrice.setError("Please enter a buy price.");

        return false;
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mActivityListener = null;
        mParentFragmentListener = null;
    }


}
