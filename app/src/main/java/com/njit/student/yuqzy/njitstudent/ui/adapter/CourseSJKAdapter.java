package com.njit.student.yuqzy.njitstudent.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.TextView;

import com.njit.student.yuqzy.njitstudent.R;
import com.njit.student.yuqzy.njitstudent.model.FormSJKCategory;

/**
 * Created by Administrator on 2017/1/27.
 */

public class CourseSJKAdapter extends BaseAdapter {
    private Context context;
    private FormSJKCategory category;

    public CourseSJKAdapter(Context context, FormSJKCategory category) {

        this.context = context;
        this.category=category;
    }

    @Override
    public int getCount() {
        return category.getSjkList().size();
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
            convertView = (View) vi.inflate(R.layout.item_course_sjk, parent, false);
            // binding view parts to view holder
            viewHolder.course_name = (TextView) convertView.findViewById(R.id.course_name);
            viewHolder.course_place = (TextView) convertView.findViewById(R.id.course_place);
            viewHolder.course_time = (TextView) convertView.findViewById(R.id.course_time);
            viewHolder.course_week = (TextView) convertView.findViewById(R.id.course_week);
            viewHolder.course_teacher = (TextView) convertView.findViewById(R.id.course_teacher);
            viewHolder.course_credit = (TextView) convertView.findViewById(R.id.course_credit);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.course_name.setText(category.getSjkList().get(position).getCourseName());
        viewHolder.course_place.setText(category.getSjkList().get(position).getStudyPlace());
        viewHolder.course_teacher.setText(category.getSjkList().get(position).getTeacher());
        viewHolder.course_week.setText(category.getSjkList().get(position).getTime());
        viewHolder.course_credit.setText(category.getSjkList().get(position).getCredit());
        viewHolder.course_time.setText(category.getSjkList().get(position).getStudyTime());
        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView course_name,course_place,course_time,course_week,course_teacher,course_credit;
    }
}
