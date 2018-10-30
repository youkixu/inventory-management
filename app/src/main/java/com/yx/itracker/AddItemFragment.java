package com.yx.itracker;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.yx.itracker.Class.Item;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddItemFragment extends Fragment implements NewItemFragment.OnNewItemListener {
    // Log tag that is used to distinguish log info.
    private final static String TAG_BROWSE_PICTURE = "BROWSE_PICTURE";
    // Used when request action Intent.ACTION_GET_CONTENT
    private final static int REQUEST_CODE_BROWSE_PICTURE = 1;
    // Used when request read external storage permission.
    private final static int REQUEST_PERMISSION_READ_EXTERNAL = 2;
    // Save user selected image uri list.
    private List<String> mItemImages = null;
    // Currently displayed user selected image index in userSelectedImageUriList.
    private int currentDisplayedUserSelectImageIndex = 0;

    private OnActivityInteractionListener mActivityListener;
    private OnParentFragInteractionListener mParentListener;

    ViewPager viewPager;
    ViewPageAdapter adapter;

    public interface OnParentFragInteractionListener {
        void addToItemList(Item itm);
    }

    public interface OnActivityInteractionListener {
        void newItemCreated(Item item);
        int getItemMapSize();
    }

    public AddItemFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnActivityInteractionListener) {
            mActivityListener = (OnActivityInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        try {
            mParentListener = (OnParentFragInteractionListener) getActivity().getSupportFragmentManager().findFragmentByTag("fragmentItemList");
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnParentFragInteractionListener");
        }

        //Log.d("aab", getParentFragment())
//        if (getParentFragment() instanceof OnParentFragInteractionListener) {
//            mParentListener = (OnParentFragInteractionListener) getParentFragment();
//        } else {
//            throw new RuntimeException("The parent fragment must implement OnChildFragmentInteractionListener");
//        }
    }

    @Override
    public void onAddNewItem(String name, String id, String quantity, String sellPrice, String buyPrice, String notes) {
        List<String> itemImages = new ArrayList<String>(mItemImages);
        if(mItemImages.isEmpty()) {
            itemImages.add("android.resource://com.yx.itracker/drawable/noimage");
        }
        Item newItem = new Item(mActivityListener.getItemMapSize(),
                new BigInteger(id),
                itemImages,
                name,
                Integer.valueOf(quantity),
                new BigDecimal(sellPrice),
                new BigDecimal(buyPrice),
                notes);
        Log.d("aa", "AddItemFrag Img Count: " + itemImages.toString());
        mActivityListener.newItemCreated(newItem);
        mParentListener.addToItemList(newItem);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_add_item, container, false);

        Button btnUpload = view.findViewById(R.id.btnUpload);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openUploadImageIntent();
            }
        });

        mItemImages = new ArrayList<String>();
        viewPager = view.findViewById(R.id.viewpager);
        adapter = new ViewPageAdapter(getActivity(), mItemImages);
        viewPager.setAdapter(adapter);
        return view;
    }

    public void openUploadImageIntent() {
        Intent openAlbumIntent = new Intent();

        // Only show images in the content chooser.
        // If you want to select all type data then openAlbumIntent.setType("*/*");
        // Must set type for the intent, otherwise there will throw android.content.ActivityNotFoundException: No Activity found to handle Intent { act=android.intent.action.GET_CONTENT }
        openAlbumIntent.setType("image/*");

        // Set action, this action will invoke android os browse content app.
        openAlbumIntent.setAction(Intent.ACTION_GET_CONTENT);

        // Start the activity.
        startActivityForResult(openAlbumIntent, REQUEST_CODE_BROWSE_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_BROWSE_PICTURE && resultCode == RESULT_OK && null != data) {
            String fileUri = data.getData().toString();
            mItemImages.add(fileUri);
            adapter.notifyDataSetChanged();
            updateImageCount();
        }
    }

    public void updateImageCount() {
        TextView tvImageCount = getActivity().findViewById(R.id.tvImageCount);
        tvImageCount.setText("Image Count: " + Integer.toString(adapter.getCount()));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Fragment childFragment = new NewItemFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.add_item_fragment, childFragment, "newItem").addToBackStack(null).commit();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mParentListener = null;
        mActivityListener = null;
    }

}
