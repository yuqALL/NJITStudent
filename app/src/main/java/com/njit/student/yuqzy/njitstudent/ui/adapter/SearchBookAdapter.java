package com.njit.student.yuqzy.njitstudent.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.njit.student.yuqzy.njitstudent.R;
import com.njit.student.yuqzy.njitstudent.model.BookItem;
import com.njit.student.yuqzy.njitstudent.ui.info.library.BookDetailActivity;

import java.util.List;

public class SearchBookAdapter extends RecyclerView.Adapter<SearchBookAdapter.SearchViewHolder> {

    private List<BookItem> bookItems;
    private Context context;
    private LayoutInflater inflater;

    public SearchBookAdapter(Context context, List<BookItem> list) {
        this.context = context;
        this.bookItems = list;
        this.inflater = LayoutInflater.from(context);
    }

    public void setNewData(List<BookItem> data) {
        this.bookItems = data;
        notifyDataSetChanged();
    }

    public List<BookItem> getData() {
        return bookItems;
    }

    public void addData(int position, List<BookItem> data) {
        this.bookItems.addAll(position, data);
        this.notifyItemRangeInserted(position, data.size());
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_simple_search_books, parent, false);
        SearchViewHolder holder = new SearchViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final SearchViewHolder holder, int position) {
        final BookItem item = bookItems.get(position);
        holder.search_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("adapter", "" + item.getBookUrl());
                //WebUtils.openInternal(context, item.getBookUrl());
                Intent intent = new Intent(context, BookDetailActivity.class);
                intent.putExtra("url", item.getBookUrl());
                intent.putExtra("name", item.getName());
                intent.putExtra("other", item.getId_suoshu());
                context.startActivity(intent);
            }
        });
        holder.book_name.setText(item.getName());
        holder.book_author.setText(item.getAuthor());
        holder.book_type.setText(item.getBookType());
        holder.book_id_suo_shu.setText(item.getId_suoshu());
        holder.book_publish_company.setText(item.getPublishCompany());
        holder.book_publish_time.setText(item.getPublishTime());
        holder.book_num_total.setText(item.getNumTotal());
        holder.book_num_can_borrow.setText(item.getNumCanBorrow());

    }

    @Override
    public int getItemCount() {
        return bookItems == null ? 0 : bookItems.size();
    }

    @Override
    public long getItemId(int position) {
        return bookItems.get(position).hashCode();
    }

    class SearchViewHolder extends RecyclerView.ViewHolder {

        TextView book_name, book_author, book_type, book_id_suo_shu, book_publish_company, book_publish_time, book_num_total, book_num_can_borrow;
        CardView search_book;

        public SearchViewHolder(View view) {
            super(view);
            book_name = (TextView) view.findViewById(R.id.book_name);
            book_author = (TextView) view.findViewById(R.id.book_author);
            book_id_suo_shu = (TextView) view.findViewById(R.id.book_id_suo_shu);
            book_type = (TextView) view.findViewById(R.id.book_type);
            book_publish_company = (TextView) view.findViewById(R.id.book_publish_company);
            book_publish_time = (TextView) view.findViewById(R.id.book_publish_time);
            book_num_total = (TextView) view.findViewById(R.id.book_num_total);
            book_num_can_borrow = (TextView) view.findViewById(R.id.book_num_can_borrow);
            search_book = (CardView) view.findViewById(R.id.search_book);

        }

    }

}
