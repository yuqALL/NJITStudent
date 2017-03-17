package com.njit.student.yuqzy.njitstudent.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.njit.student.yuqzy.njitstudent.Event.HotRecEvent;
import com.njit.student.yuqzy.njitstudent.R;
import com.njit.student.yuqzy.njitstudent.ui.info.library.BookDetailActivity;

/**
 * Created by Administrator on 2017/1/27.
 */

public class HotBooksAdapter extends BaseAdapter {
    private Context context;
    private HotRecEvent content;

    public HotBooksAdapter(Context context, HotRecEvent content) {

        this.context = context;
        this.content = content;
    }

    @Override
    public int getCount() {
        return content.getHotItems().size();
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
            convertView = (View) vi.inflate(R.layout.item_hot_books, parent, false);
            // binding view parts to view holder
            viewHolder.book_name = (TextView) convertView.findViewById(R.id.book_name);
            viewHolder.book_author = (TextView) convertView.findViewById(R.id.book_author);
            viewHolder.book_publish_info = (TextView) convertView.findViewById(R.id.book_publish_info);
            viewHolder.book_id_suo_shu = (TextView) convertView.findViewById(R.id.book_id_suo_shu);
            viewHolder.book_num = (TextView) convertView.findViewById(R.id.book_num);
            viewHolder.book_read_times = (TextView) convertView.findViewById(R.id.book_read_times);
            viewHolder.book_read = (TextView) convertView.findViewById(R.id.book_read);
            viewHolder.hot_book = (CardView) convertView.findViewById(R.id.hot_book);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.book_name.setText(content.getHotItems().get(position).getName());
        viewHolder.book_author.setText(content.getHotItems().get(position).getAuthor());
        viewHolder.book_publish_info.setText(content.getHotItems().get(position).getPublishInfo());
        viewHolder.book_id_suo_shu.setText(content.getHotItems().get(position).getIdSuoShu());
        viewHolder.book_num.setText(content.getHotItems().get(position).getNum());
        viewHolder.book_read_times.setText(content.getHotItems().get(position).getReadTimes());
        viewHolder.book_read.setText(content.getHotItems().get(position).getRead());
        viewHolder.hot_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //WebUtils.openInternal(context,content.getHotItems().get(position).getBookUrl());
                Intent intent = new Intent(context, BookDetailActivity.class);
                intent.putExtra("url", content.getHotItems().get(position).getBookUrl());
                intent.putExtra("name", content.getHotItems().get(position).getName());
                intent.putExtra("other", content.getHotItems().get(position).getIdSuoShu());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView book_name, book_author, book_publish_info, book_id_suo_shu, book_num, book_read_times, book_read;
        CardView hot_book;
    }
}
