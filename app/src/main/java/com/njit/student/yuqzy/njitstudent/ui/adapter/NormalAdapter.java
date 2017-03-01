package com.njit.student.yuqzy.njitstudent.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.njit.student.yuqzy.njitstudent.R;
import com.njit.student.yuqzy.njitstudent.model.NormalItem;
import com.njit.student.yuqzy.njitstudent.ui.info.news.NewsDetailActivity;
import com.njit.student.yuqzy.njitstudent.utils.WebUtils;

import java.util.List;

public class NormalAdapter extends RecyclerView.Adapter<NormalAdapter.NormalViewHolder> {

    private List<NormalItem> normalItems;
    private Context context;
    private LayoutInflater inflater;

    public NormalAdapter(Context context, List<NormalItem> list) {
        this.context = context;
        this.normalItems = list;
        this.inflater = LayoutInflater.from(context);
    }

    public void setNewData(List<NormalItem> data) {
        this.normalItems = data;
        notifyDataSetChanged();
    }

    public List<NormalItem> getData() {
        return normalItems;
    }

    public void addData(int position, List<NormalItem> data) {
        this.normalItems.addAll(position, data);
        this.notifyItemRangeInserted(position, data.size());
    }

    @Override
    public NormalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_edu_notification_list, parent, false);
        NormalViewHolder holder = new NormalViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final NormalViewHolder holder, int position) {
        final NormalItem item = normalItems.get(position);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("adapter",""+item.getUrl());
               // WebUtils.openInternal(context, item.getUrl());
                Intent intent=new Intent(context, NewsDetailActivity.class);
                intent.putExtra("host",item.getUrl());
                intent.putExtra("title",item.getName());
                intent.putExtra("time",item.getUpdateTime());
                context.startActivity(intent);
            }
        });
        holder.tv_name.setText(String.format("%s. %s", position + 1, item.getName()));
        holder.tv_info.setText(item.getUpdateTime() + " • NJIT");
    }

    @Override
    public int getItemCount() {
        return normalItems == null ? 0 : normalItems.size();
    }

    @Override
    public long getItemId(int position) {
        return normalItems.get(position).hashCode();
    }

    class NormalViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name;
        TextView tv_info;
        View rootView;

        public NormalViewHolder(View view) {
            super(view);
            rootView = view;
            tv_name = (TextView) view.findViewById(R.id.tv_notification_name);
            tv_info = (TextView) view.findViewById(R.id.tv_notification_info);
        }

    }

}
