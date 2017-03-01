package com.njit.student.yuqzy.njitstudent.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.njit.student.yuqzy.njitstudent.R;
import com.njit.student.yuqzy.njitstudent.model.BookShelfBean;
import com.njit.student.yuqzy.njitstudent.model.UrlItem;
import com.njit.student.yuqzy.njitstudent.ui.info.library.BookDetailActivity;
import com.njit.student.yuqzy.njitstudent.utils.WebUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/1/27.
 */

public class UrlAdapter extends BaseAdapter {
    private Context context;
    private List<UrlItem> content;

    public UrlAdapter(Context context, List<UrlItem> content) {

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

            final ViewHolderTitle viewHolder;
            if (holderView == null) {
                viewHolder = new ViewHolderTitle();
                holderView = (View) vi.inflate(R.layout.item_shelf, parent, false);
                // binding view parts to view holder
                viewHolder.shelf_name = (TextView) holderView.findViewById(R.id.shelf_name);

                holderView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolderTitle) holderView.getTag();
            }
            convertView=holderView;
            viewHolder.shelf_name.setText(content.get(position).getTitle());

        } else if (content.get(position).getType() == 1) {

            final ViewHolderUrl viewHolder;
            if (holderView == null) {
                viewHolder = new ViewHolderUrl();
                holderView = (View) vi.inflate(R.layout.item_url, parent, false);
                // binding view parts to view holder
                viewHolder.url_name = (TextView) holderView.findViewById(R.id.url_name);
                viewHolder.url_card=(CardView)holderView.findViewById(R.id.url_card);
                holderView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolderUrl) holderView.getTag();
            }
            convertView=holderView;
            viewHolder.url_name.setText(content.get(position).getName());
            viewHolder.url_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WebUtils.openExternal(context,content.get(position).getUrl());
                }
            });
        }
        return convertView;
    }


    // View lookup cache
    private static class ViewHolderUrl{
        TextView url_name;
        CardView url_card;
    }

    private static class ViewHolderTitle {
        TextView shelf_name;
    }
}
