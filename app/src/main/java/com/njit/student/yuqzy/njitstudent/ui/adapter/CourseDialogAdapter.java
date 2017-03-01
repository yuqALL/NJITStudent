package com.njit.student.yuqzy.njitstudent.ui.adapter;

import android.content.Context;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.njit.student.yuqzy.njitstudent.R;

/**
 * Created by Administrator on 2017/2/12.
 */

public class CourseDialogAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private String[] current;


    private String[] menu=new String[]{"当前账户","当前学期","当前周","课表类型","添加课程","实习课信息","调课信息","刷新课表","更换背景"};
    public CourseDialogAdapter(Context context,String[] current) {
        layoutInflater = LayoutInflater.from(context);
        this.current=current;
    }

    @Override
    public int getCount() {
        return menu.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View view = convertView;

        if (view == null) {
            view = layoutInflater.inflate(R.layout.course_dialog_list_adapter, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) view.findViewById(R.id.tv_dialog_value);
            viewHolder.currentvalue = (TextView) view.findViewById(R.id.tv_current_value);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.image_view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        //Context context = parent.getContext();
        viewHolder.name.setText(menu[position]);
        if(position<current.length) {
            viewHolder.currentvalue.setText(current[position]);
        }
        switch (position) {
            case 0:
                viewHolder.imageView.setImageResource(R.drawable.ic_user);
                break;
            case 1:
                viewHolder.imageView.setImageResource(R.drawable.ic_term);
                break;
            case 2:
                viewHolder.imageView.setImageResource(R.drawable.ic_week);
                break;
            case 3:
                viewHolder.imageView.setImageResource(R.drawable.ic_type);
                break;
            case 4:
                viewHolder.imageView.setImageResource(R.drawable.ic_add);
                break;
            case 5:
                viewHolder.imageView.setImageResource(R.drawable.ic_course_sxk);
                break;
            case 6:
                viewHolder.imageView.setImageResource(R.drawable.ic_change);
                break;
            case 7:
                viewHolder.imageView.setImageResource(R.drawable.ic_refresh);
                break;
            case 8:
                viewHolder.imageView.setImageResource(R.drawable.ic_change_bac);
                break;
            default:
                viewHolder.imageView.setImageResource(R.drawable.ic_more);
                break;
        }

        return view;
    }

    static class ViewHolder {
        TextView name;
        TextView currentvalue;
        ImageView imageView;
    }
}
