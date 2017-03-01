package com.njit.student.yuqzy.njitstudent.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.njit.student.yuqzy.njitstudent.R;

/**
 * Created by Administrator on 2017/1/27.
 */

public class HelpImageAdapter extends BaseAdapter {
    private int[] imageRes = {R.drawable.about_1,R.drawable.about_2,R.drawable.about_3,R.drawable.about_5};
    private Context context;

    public HelpImageAdapter(Context context) {

        this.context = context;

    }

    @Override
    public int getCount() {
        return imageRes.length-1;
    }

    @Override
    public Object getItem(int position) {
        return imageRes[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater vi = LayoutInflater.from(context);
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = (View) vi.inflate(R.layout.help_item, parent, false);
            // binding view parts to view holder
            viewHolder.help_img_item = (ImageView) convertView.findViewById(R.id.help_img_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Glide.with(context).load(imageRes[position]).skipMemoryCache(true).fitCenter().into(viewHolder.help_img_item);
        Log.e("help img","load "+position+" "+imageRes[position]);
        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        ImageView help_img_item;
    }
}
