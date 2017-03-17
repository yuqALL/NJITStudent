package com.njit.student.yuqzy.njitstudent.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.njit.student.yuqzy.njitstudent.Event.PreReadEvent;
import com.njit.student.yuqzy.njitstudent.R;
import com.njit.student.yuqzy.njitstudent.ui.info.library.BookDetailActivity;

/**
 * Created by Administrator on 2017/1/27.
 */

public class PreReadAdapter extends BaseAdapter {
    private Context context;
    private PreReadEvent content;

    public PreReadAdapter(Context context, PreReadEvent content) {

        this.context = context;
        this.content = content;
    }

    @Override
    public int getCount() {
        return content.getPreItems().size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater vi = LayoutInflater.from(context);
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = (View) vi.inflate(R.layout.item_pre_read, parent, false);
            // binding view parts to view holder
            viewHolder.book_id = (TextView) convertView.findViewById(R.id.book_id);
            viewHolder.book_name = (TextView) convertView.findViewById(R.id.book_name);
            viewHolder.book_authors = (TextView) convertView.findViewById(R.id.book_authors);
            viewHolder.book_get_time = (TextView) convertView.findViewById(R.id.book_get_time);
            viewHolder.book_return_time = (TextView) convertView.findViewById(R.id.book_return_time);
            viewHolder.book_sto_place = (TextView) convertView.findViewById(R.id.book_sto_place);
            viewHolder.pre_read = (CardView) convertView.findViewById(R.id.pre_read);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.book_id.setText(content.getPreItems().get(position).getId());
        viewHolder.book_name.setText(content.getPreItems().get(position).getName());
        viewHolder.book_get_time.setText(content.getPreItems().get(position).getReadTime());
        viewHolder.book_return_time.setText(content.getPreItems().get(position).getReturnTime());
        viewHolder.book_sto_place.setText(content.getPreItems().get(position).getPlace());
        viewHolder.book_authors.setText(content.getPreItems().get(position).getAuthor());
        viewHolder.pre_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BookDetailActivity.class);
                intent.putExtra("url", content.getPreItems().get(position).getBookUrl());
                intent.putExtra("name", content.getPreItems().get(position).getName());
                intent.putExtra("other", content.getPreItems().get(position).getPlace());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView book_id, book_name, book_get_time, book_return_time, book_authors, book_sto_place;
        CardView pre_read;
    }
}
