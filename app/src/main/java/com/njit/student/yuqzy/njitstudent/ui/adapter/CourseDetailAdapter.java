package com.njit.student.yuqzy.njitstudent.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.njit.student.yuqzy.njitstudent.R;
import com.njit.student.yuqzy.njitstudent.model.Cell;


public class CourseDetailAdapter extends BaseAdapter {
    private Context context;
    private Cell cell;

    public CourseDetailAdapter(Context context, Cell cell) {

        this.context = context;
        this.cell = cell;
    }

    @Override
    public int getCount() {
        return cell.getCourseName().size();
    }

    @Override
    public Object getItem(int position) {
        return cell.getOriginText();
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
            convertView = (View) vi.inflate(R.layout.item_course_detail, parent, false);
            // binding view parts to view holder
            viewHolder.course_name = (TextView) convertView.findViewById(R.id.course_name);
            viewHolder.course_place = (TextView) convertView.findViewById(R.id.course_place);
            viewHolder.course_time = (TextView) convertView.findViewById(R.id.course_time);
            viewHolder.course_week = (TextView) convertView.findViewById(R.id.course_week);
            viewHolder.course_teacher = (TextView) convertView.findViewById(R.id.course_teacher);
            viewHolder.course_bixiu = (RadioButton) convertView.findViewById(R.id.course_bixiu);
            viewHolder.course_renxuan = (RadioButton) convertView.findViewById(R.id.course_renxuan);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.course_name.setText(cell.getCourseName().get(position));
        viewHolder.course_place.setText(cell.getPlaceName().get(position));

        viewHolder.course_teacher.setText(cell.getTeacher().get(position));

        if (cell.getX() > 7) {
            viewHolder.course_time.setText("周日 " + (cell.getY() - 1) + "-" + (cell.getY() - 2 + cell.getLength()));
        } else if (cell.getX() > 6) {
            viewHolder.course_time.setText("周六 " + (cell.getY() - 1) + "-" + (cell.getY() - 2 + cell.getLength()));
        } else if (cell.getX() > 5) {
            viewHolder.course_time.setText("周五 " + (cell.getY() - 1) + "-" + (cell.getY() - 2 + cell.getLength()));
        } else if (cell.getX() > 4) {
            viewHolder.course_time.setText("周四 " + (cell.getY() - 1) + "-" + (cell.getY() - 2 + cell.getLength()));
        } else if (cell.getX() > 3) {
            viewHolder.course_time.setText("周三 " + (cell.getY() - 1) + "-" + (cell.getY() - 2 + cell.getLength()));
        } else if (cell.getX() > 2) {
            viewHolder.course_time.setText("周二 " + (cell.getY() - 1) + "-" + (cell.getY() - 2 + cell.getLength()));
        } else if (cell.getX() > 1) {
            viewHolder.course_time.setText("周一 " + (cell.getY() - 1) + "-" + (cell.getY() - 2 + cell.getLength()));
        }


        viewHolder.course_week.setText(cell.getCourseWeek().get(position));

        if (cell.getCourseType().get(position).contains("必修")) {
            viewHolder.course_bixiu.setChecked(true);
            viewHolder.course_renxuan.setChecked(false);
        } else if (cell.getCourseType().get(position).contains("任选")) {
            viewHolder.course_bixiu.setChecked(false);
            viewHolder.course_renxuan.setChecked(true);
        }
        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView course_name, course_place, course_time, course_week, course_teacher;
        RadioButton course_bixiu, course_renxuan;
    }
}
