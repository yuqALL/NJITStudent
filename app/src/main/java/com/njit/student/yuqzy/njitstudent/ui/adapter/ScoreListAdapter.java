package com.njit.student.yuqzy.njitstudent.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.njit.student.yuqzy.njitstudent.R;
import com.njit.student.yuqzy.njitstudent.database.ScoreData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class ScoreListAdapter extends ArrayAdapter<ScoreData> {

    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private Context context;

    public ScoreListAdapter(Context context, List<ScoreData> objects) {
        super(context, 0, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get item for selected view
        ScoreData item = getItem(position);
        // if cell is exists - reuse it, if not - create the new one from resource
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            convertView = (View) vi.inflate(R.layout.score_item, parent, false);
            // binding view parts to view holder
            viewHolder.imgScoreIcon = (ImageView) convertView.findViewById(R.id.img_score_img);
            viewHolder.tvScoreName = (TextView) convertView.findViewById(R.id.tv_score_name);
            viewHolder.tvScoreUsual = (TextView) convertView.findViewById(R.id.tv_score_usual);
            viewHolder.tvScore = (TextView) convertView.findViewById(R.id.tv_score);
            viewHolder.expand_img_score = (ImageView) convertView.findViewById(R.id.expand_img_score);
            //viewHolder.downHeadimg = (ImageView) convertView.findViewById(R.id.down_head_img);

            viewHolder.lv_detail_content = (ListView) convertView.findViewById(R.id.lv_detail_content);
            convertView.setTag(viewHolder);
        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }

        int score = 0;
        String s=item.getScore().trim();
        try {
            score = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if (score >= 90||"优秀".equals(s)) {
            //Glide.with(context).load(R.drawable.ic_above_90).skipMemoryCache(true).into(viewHolder.imgScoreIcon);
            viewHolder.imgScoreIcon.setImageResource(R.drawable.ic_above_90);
        } else if (score >= 60||"良好".equals(s)) {
            //Glide.with(context).load(R.drawable.ic_heart).skipMemoryCache(true).into(viewHolder.imgScoreIcon);
            viewHolder.imgScoreIcon.setImageResource(R.drawable.ic_heart);
        } else {
            //Glide.with(context).load(R.drawable.ic_heart_break).skipMemoryCache(true).into(viewHolder.imgScoreIcon);
            viewHolder.imgScoreIcon.setImageResource(R.drawable.ic_heart_break);
        }
        viewHolder.tvScoreName.setText(item.getCourseName());
        viewHolder.tvScoreUsual.setText(item.getUsualScore());
        viewHolder.tvScore.setText(item.getScore());
        ScoreDetailListAdapter adapter = new ScoreDetailListAdapter(context, item);
        viewHolder.lv_detail_content.setAdapter(adapter);
        GETHeight.setListViewHeightBasedOnChildren(viewHolder.lv_detail_content);
        return convertView;
    }

    // simple methods for register cell state changes
    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }

    // View lookup cache
    private static class ViewHolder {
        ImageView imgScoreIcon;
        TextView tvScoreName;
        TextView tvScoreUsual;
        TextView tvScore;
        ImageView expand_img_score;
        ListView lv_detail_content;
    }

}

class ScoreDetailListAdapter extends BaseAdapter {

    private ScoreData detail;
    private Context context;
    private List<String> key;
    private List<String> value;

    public ScoreDetailListAdapter(Context context, ScoreData detail) {
        this.detail = detail;
        this.context = context;
        key = new ArrayList<String>();
        value = new ArrayList<String>();
        initDate();
    }

    private void initDate() {
        key.add("学年");
        value.add(detail.getSchoolYear());
        key.add("学期");
        value.add(detail.getSchoolTerm());
        key.add("课程代码");
        value.add(detail.getCourseCode());
        key.add("课程名称");
        value.add(detail.getCourseName());
        key.add("课程性质");
        value.add(detail.getCourseNature());
        key.add("课程归属");
        value.add(detail.getCourseBelong());
        key.add("学分");
        value.add(detail.getCredit());
        key.add("平时成绩");
        value.add(detail.getUsualScore());
        key.add("期中成绩");
        value.add(detail.getMidtermScore());
        key.add("期末成绩");
        value.add(detail.getFinalScore());
        key.add("实验成绩");
        value.add(detail.getExperimentScore());
        key.add("成绩");
        value.add(detail.getScore());
        key.add("补考成绩");
        value.add(detail.getMake_upScore());
        key.add("是否重修");
        value.add(detail.getRebuild());
        key.add("开课学院");
        value.add(detail.getCollegeBelong());
        key.add("备注");
        value.add(detail.getComment());
        key.add("补考备注");
        value.add(detail.getMake_upComment());
    }

    @Override
    public int getCount() {
        return key == null ? 0 : key.size();
    }

    @Override
    public String getItem(int position) {
        return key == null ? "" : key.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater vi = LayoutInflater.from(context);
        final ViewHolder viewHolder;
        final String urlKey = key.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = (View) vi.inflate(R.layout.item_score_detail_content, parent, false);
            // binding view parts to view holder
            viewHolder.item_detail_name = (TextView) convertView.findViewById(R.id.item_detial_name);
            viewHolder.item_detail_value = (TextView) convertView.findViewById(R.id.item_detail_value);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.item_detail_name.setText(key.get(position));
        viewHolder.item_detail_value.setText(value.get(position));
        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView item_detail_name;
        TextView item_detail_value;

    }


}

class GETHeight {

    public static void setListViewHeightBasedOnChildren(ListView listview) {

        ListAdapter listAdapter = listview.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listview);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listview.getLayoutParams();
        params.height = totalHeight + (listview.getDividerHeight() * (listAdapter.getCount() - 1)) + 20;
        listview.setLayoutParams(params);
    }
}
