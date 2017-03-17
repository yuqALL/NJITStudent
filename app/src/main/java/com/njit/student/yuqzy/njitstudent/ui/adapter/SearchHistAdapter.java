package com.njit.student.yuqzy.njitstudent.ui.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.njit.student.yuqzy.njitstudent.Event.SearchHistEvent;
import com.njit.student.yuqzy.njitstudent.R;
import com.njit.student.yuqzy.njitstudent.utils.WebUtils;

/**
 * Created by Administrator on 2017/1/27.
 */

public class SearchHistAdapter extends BaseAdapter {
    private Context context;
    private SearchHistEvent content;

    public SearchHistAdapter(Context context, SearchHistEvent content) {

        this.context = context;
        this.content = content;
    }

    @Override
    public int getCount() {
        return content.getSearchItems().size();
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
            convertView = (View) vi.inflate(R.layout.item_search_history, parent, false);
            // binding view parts to view holder
            viewHolder.search_content = (TextView) convertView.findViewById(R.id.search_content);
            viewHolder.search_time = (TextView) convertView.findViewById(R.id.search_time);
            viewHolder.search_history = (CardView) convertView.findViewById(R.id.search_history);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.search_content.setText(content.getSearchItems().get(position).getContent());
        viewHolder.search_time.setText(content.getSearchItems().get(position).getTime());
        viewHolder.search_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebUtils.openExternal(context, content.getSearchItems().get(position).getUrl());

            }
        });

        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView search_content, search_time;
        CardView search_history;
    }
}
