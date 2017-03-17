package com.njit.student.yuqzy.njitstudent.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.njit.student.yuqzy.njitstudent.R;

import java.util.List;

/**
 * Created by Administrator on 2017/2/13.
 */

public class PersonInfoAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private List<String> name, value;

    public PersonInfoAdapter(Context context, List<String> name, List<String> value) {
        layoutInflater = LayoutInflater.from(context);
        this.name = name;
        this.value = value;
    }


    @Override
    public int getCount() {
        return name.size() > value.size() ? value.size() : name.size();
    }

    @Override
    public Object getItem(int position) {
        return value.get(position);
    }

    @Override
    public long getItemId(int position) {
        return name.size() > value.size() ? value.size() : name.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View view = convertView;

        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_normal_info, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) view.findViewById(R.id.tv_info_name);
            viewHolder.value = (TextView) view.findViewById(R.id.tv_info_value);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.name.setText(name.get(position));
        viewHolder.value.setText(value.get(position));

        return view;
    }

    static class ViewHolder {
        TextView name;
        TextView value;
    }
}
