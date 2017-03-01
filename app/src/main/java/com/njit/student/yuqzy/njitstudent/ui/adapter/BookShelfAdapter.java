package com.njit.student.yuqzy.njitstudent.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.njit.student.yuqzy.njitstudent.Event.BreakRulesEvent;
import com.njit.student.yuqzy.njitstudent.R;
import com.njit.student.yuqzy.njitstudent.model.BookShelfBean;
import com.njit.student.yuqzy.njitstudent.ui.info.library.BookDetailActivity;

import java.util.List;

/**
 * Created by Administrator on 2017/1/27.
 */

public class BookShelfAdapter extends BaseAdapter {
    private Context context;
    private List<BookShelfBean> content;

    public BookShelfAdapter(Context context, List<BookShelfBean> content) {

        this.context = context;
        this.content = content;
    }

    @Override
    public int getCount() {
        return content.size();
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
        View holderView = null;
        if (content.get(position).getType() == 0) {

            final ViewHolderShelf viewHolder;
            if (holderView == null) {
                viewHolder = new ViewHolderShelf();
                holderView = (View) vi.inflate(R.layout.item_shelf, parent, false);
                // binding view parts to view holder
                viewHolder.shelf_name = (TextView) holderView.findViewById(R.id.shelf_name);

                holderView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolderShelf) holderView.getTag();
            }
            convertView=holderView;
            viewHolder.shelf_name.setText(content.get(position).getTitle());

        } else if (content.get(position).getType() == 1) {

            final ViewHolderBook viewHolder;
            if (holderView == null) {
                viewHolder = new ViewHolderBook();
                holderView = (View) vi.inflate(R.layout.item_book_content, parent, false);
                // binding view parts to view holder
                viewHolder.book_name = (TextView) holderView.findViewById(R.id.book_name);
                viewHolder.book_authors = (TextView) holderView.findViewById(R.id.book_authors);
                viewHolder.book_publish_company = (TextView) holderView.findViewById(R.id.book_publish_company);
                viewHolder.book_publish_time = (TextView) holderView.findViewById(R.id.book_publish_time);
                viewHolder.book_id_suo_shu = (TextView) holderView.findViewById(R.id.book_id_suo_shu);
                viewHolder.shelf_book=(CardView)holderView.findViewById(R.id.shelf_book);
                holderView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolderBook) holderView.getTag();
            }
            convertView=holderView;
            viewHolder.book_name.setText(content.get(position).getItem().getName());
            viewHolder.book_authors.setText(content.get(position).getItem().getAuthors());
            viewHolder.book_publish_company.setText(content.get(position).getItem().getPublishCompany());
            viewHolder.book_publish_time.setText(content.get(position).getItem().getPublishTime());
            viewHolder.book_id_suo_shu.setText(content.get(position).getItem().getIdSuoShu());
            viewHolder.shelf_book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, BookDetailActivity.class);
                    intent.putExtra("url",content.get(position).getItem().getBookUrl());
                    intent.putExtra("name",content.get(position).getItem().getName());
                    intent.putExtra("other",content.get(position).getItem().getIdSuoShu());
                    context.startActivity(intent);
                }
            });
        }
        return convertView;
    }


    // View lookup cache
    private static class ViewHolderBook {
        TextView book_name, book_authors, book_publish_company, book_publish_time, book_id_suo_shu;
        CardView shelf_book;
    }

    private static class ViewHolderShelf {
        TextView shelf_name;
    }
}
