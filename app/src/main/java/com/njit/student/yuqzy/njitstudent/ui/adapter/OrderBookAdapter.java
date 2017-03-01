package com.njit.student.yuqzy.njitstudent.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.njit.student.yuqzy.njitstudent.Event.OrderBookEvent;
import com.njit.student.yuqzy.njitstudent.Event.PreReadEvent;
import com.njit.student.yuqzy.njitstudent.R;
import com.njit.student.yuqzy.njitstudent.ui.info.library.BookDetailActivity;
import com.njit.student.yuqzy.njitstudent.utils.WebUtils;

/**
 * Created by Administrator on 2017/1/27.
 */

public class OrderBookAdapter extends BaseAdapter {
    private Context context;
    private OrderBookEvent content;

    public OrderBookAdapter(Context context, OrderBookEvent content) {

        this.context = context;
        this.content=content;
    }

    @Override
    public int getCount() {
        return content.getOrderItems().size();
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
            convertView = (View) vi.inflate(R.layout.item_order_book, parent, false);
            // binding view parts to view holder
            viewHolder.book_id = (TextView) convertView.findViewById(R.id.book_id);
            viewHolder.book_name_author = (TextView) convertView.findViewById(R.id.book_name_author);
            viewHolder.book_sto_place = (TextView) convertView.findViewById(R.id.book_sto_place);
            viewHolder.book_order_time = (TextView) convertView.findViewById(R.id.book_order_time);
            viewHolder.book_abort_time = (TextView) convertView.findViewById(R.id.book_abort_time);
            viewHolder.book_get_place = (TextView) convertView.findViewById(R.id.book_get_place);
            viewHolder.book_state = (TextView) convertView.findViewById(R.id.book_state);
            viewHolder.order_book=(CardView)convertView.findViewById(R.id.order_book);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.book_id.setText(content.getOrderItems().get(position).getId());
        viewHolder.book_name_author.setText(content.getOrderItems().get(position).getName());
        viewHolder.book_sto_place.setText(content.getOrderItems().get(position).getPlace());
        viewHolder.book_order_time.setText(content.getOrderItems().get(position).getOrderTime());
        viewHolder.book_abort_time.setText(content.getOrderItems().get(position).getAbortTime());
        viewHolder.book_get_place.setText(content.getOrderItems().get(position).getGetBookPlace());
        viewHolder.book_state.setText(content.getOrderItems().get(position).getState());
        viewHolder.order_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //WebUtils.openInternal(context,content.getOrderItems().get(position).getBookUrl());
                Intent intent=new Intent(context, BookDetailActivity.class);
                intent.putExtra("url",content.getOrderItems().get(position).getBookUrl());
                intent.putExtra("name",content.getOrderItems().get(position).getName());
                intent.putExtra("other",content.getOrderItems().get(position).getPlace());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView book_id,book_name_author,book_sto_place,book_order_time,book_abort_time,book_get_place,book_state;
        CardView order_book;
    }
}
