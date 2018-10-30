package com.yx.itracker;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ViewPageAdapter extends PagerAdapter{
    Context context;
    List<String> images;
    LayoutInflater inflater;

    public ViewPageAdapter(Context context, List<String> images) {
        this.context = context;
        this.images = images;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view==o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //inflater = (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.viewpager_item, container, false);
        ImageView ivItem = itemView.findViewById(R.id.ivItem);
        //ivItem.setImageResource(images[position]);
//        DisplayMetrics dis = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dis);
//        int height = dis.heightPixels;
//        int width = dis.widthPixels;
//        image.setMaxHeight(height);
//        image.setMaxWidth(width);
        if (!images.isEmpty()) {
            try {
                Picasso.get()
                        .load(images.get(position))
                        .placeholder(R.mipmap.ic_launcher_round)
                        .error(R.drawable.noimage)
                        .into(ivItem);
            } catch (Exception e) {
                Log.d("error","Error with loading image");
            }
        }

        container.addView(itemView);
        return itemView;

    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
