package com.njit.student.yuqzy.njitstudent.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.njit.student.yuqzy.njitstudent.R;
import com.njit.student.yuqzy.njitstudent.model.FormSJKCategory;
import com.njit.student.yuqzy.njitstudent.model.FormTTBCategory;

/**
 * Created by Administrator on 2017/1/27.
 */

public class CourseTTBAdapter extends BaseAdapter {
    private Context context;
    private FormTTBCategory category;

    public CourseTTBAdapter(Context context, FormTTBCategory category) {

        this.context = context;
        this.category=category;
    }

    @Override
    public int getCount() {
        return category.getTtbList().size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater vi = LayoutInflater.from(context);
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = (View) vi.inflate(R.layout.item_course_ttb, parent, false);
            // binding view parts to view holder
            viewHolder.course_name = (TextView) convertView.findViewById(R.id.course_name);
            viewHolder.course_id = (TextView) convertView.findViewById(R.id.course_id);
            viewHolder.course_pre_info = (TextView) convertView.findViewById(R.id.course_pre_info);
            viewHolder.course_now_info = (TextView) convertView.findViewById(R.id.course_now_info);
            viewHolder.course_post_time = (TextView) convertView.findViewById(R.id.course_post_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.course_name.setText(category.getTtbList().get(position).getCourseName());
        viewHolder.course_id.setText(category.getTtbList().get(position).getId());
        viewHolder.course_pre_info.setText(category.getTtbList().get(position).getPreStudyState().replaceAll("/","\n"));
        viewHolder.course_now_info.setText(category.getTtbList().get(position).getNowStudyState().replaceAll("/","\n"));
        viewHolder.course_post_time.setText(category.getTtbList().get(position).getPostTime());
        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView course_name,course_id,course_pre_info,course_now_info,course_post_time;
    }
}
