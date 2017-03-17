package com.njit.student.yuqzy.njitstudent.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.njit.student.yuqzy.njitstudent.R;

import java.util.List;

/**
 * Created by Administrator on 2017/1/27.
 */

public class BookDetailAdapter extends BaseAdapter {
    private Context context;
    private List<String> detail;

    public BookDetailAdapter(Context context, List<String> detail) {

        this.context = context;
        this.detail = detail;
    }

    @Override
    public int getCount() {
        return detail.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
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
            convertView = (View) vi.inflate(R.layout.item_book_detail, parent, false);
            // binding view parts to view holder
            viewHolder.item_value = (TextView) convertView.findViewById(R.id.item_value);
            viewHolder.img_book = (ImageView) convertView.findViewById(R.id.img_book);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String content = detail.get(position);
        if (content.contains("<image>")) {
            viewHolder.item_value.setVisibility(View.GONE);
            viewHolder.img_book.setVisibility(View.VISIBLE);
            Glide.with(context).load(content.replaceAll("<image>", "").trim()).skipMemoryCache(true).fitCenter().into(viewHolder.img_book);
        } else {
            viewHolder.item_value.setVisibility(View.VISIBLE);
            viewHolder.img_book.setVisibility(View.GONE);
            viewHolder.item_value.setText(content);
        }
        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView item_value;
        ImageView img_book;
    }
}
