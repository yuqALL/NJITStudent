package com.njit.student.yuqzy.njitstudent.ui.adapter;

import android.content.Context;
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

public class ScoresDialogAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private String[] current;


    // 平均学分绩点反映学习的整体质量，是获得学位授予资格的重要条件之一，其计算方法为：
//    平均学分绩点=Σ（课程学分×课程成绩绩点）/Σ课程学分（四舍五入保留两位小数）
//    纳入平均学分绩点计算的课程含公共基础课、专业基础课、专业课、专业限选课、专业任选课以及实践教学环节。
//    公共基础课、专业基础课、专业课、专业限选课以及实践教学环节类课程全部计入平均学分绩点。
//    专业任选课按毕业要求的最低任选学分要求，取获得成绩最高的课程计算平均学分绩点。
//    公共选修课不计入平均学分绩点。
//    正考取得的百分制成绩绩点对照表
//
//            序号
//    成绩分数段
//            对应绩点
//
//            95≤X≤100
//            5
//
//            90≤X＜95
//            4.5
//
//            85≤X＜90
//            4
//
//            80≤X＜85
//            3.5
//
//            75≤X＜80
//            3
//
//            70≤X＜75
//            2.5
//
//            65≤X＜70
//            2
//
//            60≤X＜65
//            1
//
//    X＜60
//            0
//    正考取得的等级制成绩绩点对照表
//
//            序号
//    成绩
//            对应的绩点
//
//    优秀
//    4.5
//
//    良好
//    3.5
//
//    中等
//    2.5
//
//    及格
//    1.5
//
//    不及格
//    0
//    重考、重修取得的百分制成绩绩点对照表
//
//            序号
//    成绩分数段
//            对应绩点
//
//            95≤X≤100
//            4.5
//
//            90≤X＜95
//            4
//
//            85≤X＜90
//            3.5
//
//            80≤X＜85
//            3
//
//            75≤X＜80
//            2.5
//
//            70≤X＜75
//            2
//
//            65≤X＜70
//            1.5
//
//            60≤X＜65
//            1
//
//    X＜60
//            0
//    重考、重修取得的等级制成绩绩点对照表
//
//            序号
//    成绩
//            对应的绩点
//
//    优秀
//    4
//
//    良好
//    3
//
//    中等
//    2
//            4
//    及格
//    1
//
//    不及格
//    0
    private String[] menu = new String[]{"加权平均分", "平均学分绩点", "总成绩平均分", "期末成绩平均分"};

    public ScoresDialogAdapter(Context context, String[] current) {
        layoutInflater = LayoutInflater.from(context);
        this.current = current;
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
            view = layoutInflater.inflate(R.layout.score_dialog_list_adapter, parent, false);
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
        if (position < current.length) {
            viewHolder.currentvalue.setText(current[position]);
        }
        switch (position) {
            case 0:
                viewHolder.imageView.setImageResource(R.drawable.ic_score);
                break;
            case 1:
                viewHolder.imageView.setImageResource(R.drawable.ic_sto);
                break;
            case 2:
                viewHolder.imageView.setImageResource(R.drawable.ic_average);
                break;
            case 3:
                viewHolder.imageView.setImageResource(R.drawable.ic_average);
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
