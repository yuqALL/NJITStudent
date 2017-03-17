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
import com.njit.student.yuqzy.njitstudent.database.BookDetailRealm;
import com.njit.student.yuqzy.njitstudent.ui.info.library.BookDetailActivity;

import java.util.List;

public class LikeBooksAdapter extends RecyclerView.Adapter<LikeBooksAdapter.NormalViewHolder> {

    private List<BookDetailRealm> bookDetailRealms;
    private Context context;
    private LayoutInflater inflater;

    public LikeBooksAdapter(Context context, List<BookDetailRealm> list) {
        this.context = context;
        this.bookDetailRealms = list;
        this.inflater = LayoutInflater.from(context);
    }

    public void setNewData(List<BookDetailRealm> data) {
        this.bookDetailRealms = data;
        notifyDataSetChanged();
    }

    public List<BookDetailRealm> getData() {
        return bookDetailRealms;
    }

    public void addData(int position, List<BookDetailRealm> data) {
        this.bookDetailRealms.addAll(position, data);
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
        final BookDetailRealm item = bookDetailRealms.get(position);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("adapter", "" + item.getUrl());
                // WebUtils.openInternal(context, item.getUrl());
                Intent intent = new Intent(context, BookDetailActivity.class);
                intent.putExtra("url", item.getUrl());
                context.startActivity(intent);
            }
        });
        holder.tv_name.setText(String.format("%s. %s", position + 1, item.getName()));
        holder.tv_info.setText(item.getOther() + " • NJIT");
    }

    @Override
    public int getItemCount() {
        return bookDetailRealms == null ? 0 : bookDetailRealms.size();
    }

    @Override
    public long getItemId(int position) {
        return bookDetailRealms.get(position).hashCode();
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
