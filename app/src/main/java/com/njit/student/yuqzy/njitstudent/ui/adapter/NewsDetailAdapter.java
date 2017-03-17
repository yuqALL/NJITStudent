package com.njit.student.yuqzy.njitstudent.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.njit.student.yuqzy.njitstudent.R;
import com.njit.student.yuqzy.njitstudent.database.NewsDetailItem;
import com.njit.student.yuqzy.njitstudent.ui.info.news.NewsDetailActivity;
import com.njit.student.yuqzy.njitstudent.utils.WebUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/2/14.
 */

public class NewsDetailAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private Context context;
    List<NewsDetailItem> data;

    public NewsDetailAdapter(Context context, List<NewsDetailItem> data) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return data == null ? null : data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View view = convertView;

        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_news_detail, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imgName = (TextView) view.findViewById(R.id.item_news_img_name);
            viewHolder.content = (TextView) view.findViewById(R.id.item_news_content);
            viewHolder.link = (TextView) view.findViewById(R.id.item_news_link);
            viewHolder.title = (TextView) view.findViewById(R.id.tv_news_title);
            viewHolder.info = (TextView) view.findViewById(R.id.tv_news_info);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.item_news_img);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        NewsDetailItem item = data.get(position);
        Log.e("item adapter", item.toString());
        switch (item.getType()) {
            case 1:
                viewHolder.content.setVisibility(View.VISIBLE);
                viewHolder.imageView.setVisibility(View.GONE);
                viewHolder.imgName.setVisibility(View.GONE);
                viewHolder.link.setVisibility(View.GONE);
                viewHolder.title.setVisibility(View.GONE);
                viewHolder.info.setVisibility(View.GONE);
                viewHolder.content.setText("\u3000\u3000" + item.getValue());
                break;
            case 2:
                viewHolder.content.setVisibility(View.GONE);
                viewHolder.imgName.setVisibility(View.VISIBLE);
                viewHolder.imageView.setVisibility(View.VISIBLE);
                viewHolder.link.setVisibility(View.GONE);
                viewHolder.title.setVisibility(View.GONE);
                viewHolder.info.setVisibility(View.GONE);
                viewHolder.imgName.setText(item.getValue());
                Glide.with(context).load(item.getLink()).skipMemoryCache(true).fitCenter().into(viewHolder.imageView);
                break;
            case 3:
                viewHolder.content.setVisibility(View.GONE);
                viewHolder.imageView.setVisibility(View.GONE);
                viewHolder.imgName.setVisibility(View.GONE);
                viewHolder.link.setVisibility(View.VISIBLE);
                viewHolder.title.setVisibility(View.GONE);
                viewHolder.info.setVisibility(View.GONE);
                final String text = item.getValue();
                final String link = item.getLink();
                viewHolder.link.setText(text);
                if (!link.contains("download.jsp?")) {
                    viewHolder.link.setCompoundDrawables(null, null, null, null);
                }
                viewHolder.link.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //text.contains("上一篇")||text.contains("下一篇")
                        if (!link.contains("download.jsp?")) {
                            Intent intent = new Intent(context, NewsDetailActivity.class);
                            intent.putExtra("host", link);
                            context.startActivity(intent);
                        } else {
                            WebUtils.openExternal(context, link);
                        }
                    }
                });
                break;
            case 4:
                viewHolder.content.setVisibility(View.GONE);
                viewHolder.imageView.setVisibility(View.GONE);
                viewHolder.imgName.setVisibility(View.GONE);
                viewHolder.link.setVisibility(View.GONE);
                viewHolder.title.setVisibility(View.VISIBLE);
                viewHolder.info.setVisibility(View.GONE);
                viewHolder.title.setText(item.getValue());
                break;
            case 5:
                viewHolder.content.setVisibility(View.GONE);
                viewHolder.imageView.setVisibility(View.GONE);
                viewHolder.imgName.setVisibility(View.GONE);
                viewHolder.link.setVisibility(View.GONE);
                viewHolder.title.setVisibility(View.GONE);
                viewHolder.info.setVisibility(View.VISIBLE);
                viewHolder.info.setText(item.getValue());
                break;
        }

        return view;
    }

    static class ViewHolder {
        TextView imgName, content, link, title, info;
        ImageView imageView;
    }
}
