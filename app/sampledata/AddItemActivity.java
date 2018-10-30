package com.yx.itracker;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.yx.itracker.Class.Item;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.yx.itracker.Class.Item;

public class AddItemActivity extends AppCompatActivity implements NewItemFragment.OnNewItemListener{
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

    ViewPager viewPager;
    ViewPageAdapter adapter;

    private String[] images = {
            "/mnt/sdcard/Pictures/healthy.jpg",
            "/mnt/sdcard/Pictures/aloe.jpg",
            "/mnt/sdcard/Pictures/86.jpg",
            "/mnt/sdcard/Pictures/5972016444_74d828bcb9_o.jpg"
    };

    public void onAddNewItem(String name, String id, String quantity, String sellPrice, String buyPrice, String notes) {
        Item newItem = new Item(new BigInteger(id),
                mItemImages,
                name,
                Integer.valueOf(quantity),
                new BigDecimal(sellPrice),
                new BigDecimal(buyPrice),
                notes);
        Log.d("aa", newItem.toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        Button btnUpload = findViewById(R.id.btnUpload);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openUploadImageIntent();
            }
        });

        mItemImages = new ArrayList<String>();

        viewPager = findViewById(R.id.viewpager);
        adapter = new ViewPageAdapter(AddItemActivity.this, mItemImages);
        viewPager.setAdapter(adapter);

        Log.d("aa", Integer.toString(adapter.getCount()));
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new NewItemFragment())
                    .commit();
        }

    }

    public void navigateTo(Fragment fragment, boolean addToBackstack) {
        FragmentTransaction transaction =
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, fragment);

        if (addToBackstack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
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
            Log.d("aa", mItemImages.toString());
        }
    }
}
