package com.njit.student.yuqzy.njitstudent.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.njit.student.yuqzy.njitstudent.Event.CurrentReadEvent;
import com.njit.student.yuqzy.njitstudent.R;
import com.njit.student.yuqzy.njitstudent.ui.info.library.BookDetailActivity;

/**
 * Created by Administrator on 2017/1/27.
 */

public class CurrentReadAdapter extends BaseAdapter {
    private Context context;
    private CurrentReadEvent content;

    public CurrentReadAdapter(Context context, CurrentReadEvent content) {

        this.context = context;
        this.content = content;
    }

    @Override
    public int getCount() {
        return content.getCrItems().size();
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
            convertView = (View) vi.inflate(R.layout.item_current_read, parent, false);
            // binding view parts to view holder
            viewHolder.book_id = (TextView) convertView.findViewById(R.id.book_id);
            viewHolder.book_name = (TextView) convertView.findViewById(R.id.book_name);
            viewHolder.book_get_time = (TextView) convertView.findViewById(R.id.book_get_time);
            viewHolder.book_return_time = (TextView) convertView.findViewById(R.id.book_return_time);
            viewHolder.book_continue_times = (TextView) convertView.findViewById(R.id.book_continue_times);
            viewHolder.book_sto_place = (TextView) convertView.findViewById(R.id.book_sto_place);
            viewHolder.book_other = (TextView) convertView.findViewById(R.id.book_other);
            //viewHolder.book_continue = (Button) convertView.findViewById(R.id.book_continue);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.book_id.setText(content.getCrItems().get(position).getId());
        viewHolder.book_name.setText(content.getCrItems().get(position).getName());
        viewHolder.book_get_time.setText(content.getCrItems().get(position).getGetTime());
        viewHolder.book_return_time.setText(content.getCrItems().get(position).getReturnTime());
        viewHolder.book_continue_times.setText(content.getCrItems().get(position).getContinueTimes());
        viewHolder.book_sto_place.setText(content.getCrItems().get(position).getPlace());
        viewHolder.book_other.setText(content.getCrItems().get(position).getOtherThing());
//        viewHolder.book_continue.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context,"click "+position,Toast.LENGTH_SHORT).show();
//            }
//        });
        viewHolder.book_name.setTextColor(context.getResources().getColor(R.color.Color_DarkCyan));
        viewHolder.book_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BookDetailActivity.class);
                intent.putExtra("url", content.getCrItems().get(position).getBookUrl());
                intent.putExtra("name", content.getCrItems().get(position).getName());
                intent.putExtra("other", content.getCrItems().get(position).getPlace());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView book_id, book_name, book_get_time, book_return_time, book_continue_times, book_sto_place, book_other;
        //Button book_continue;
    }
}
