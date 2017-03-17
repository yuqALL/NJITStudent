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
import com.njit.student.yuqzy.njitstudent.ui.info.library.BookDetailActivity;

/**
 * Created by Administrator on 2017/1/27.
 */

public class BreakRulesAdapter extends BaseAdapter {
    private Context context;
    private BreakRulesEvent content;

    public BreakRulesAdapter(Context context, BreakRulesEvent content) {

        this.context = context;
        this.content = content;
    }

    @Override
    public int getCount() {
        return content.getDebtItems().size();
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
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = (View) vi.inflate(R.layout.break_rules_book, parent, false);
            // binding view parts to view holder
            viewHolder.book_id_tiao_ma = (TextView) convertView.findViewById(R.id.book_id_tiao_ma);
            viewHolder.book_id_suo_shu = (TextView) convertView.findViewById(R.id.book_id_suo_shu);
            viewHolder.book_name = (TextView) convertView.findViewById(R.id.book_name);
            viewHolder.book_authors = (TextView) convertView.findViewById(R.id.book_authors);
            viewHolder.book_read_time = (TextView) convertView.findViewById(R.id.book_read_time);
            viewHolder.book_return_time = (TextView) convertView.findViewById(R.id.book_return_time);
            viewHolder.book_sto_place = (TextView) convertView.findViewById(R.id.book_sto_place);
            viewHolder.book_should_pay = (TextView) convertView.findViewById(R.id.book_should_pay);
            viewHolder.book_actual_pay = (TextView) convertView.findViewById(R.id.book_actual_pay);
            viewHolder.book_state = (TextView) convertView.findViewById(R.id.book_state);
            viewHolder.break_book = (CardView) convertView.findViewById(R.id.break_book);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.book_id_tiao_ma.setText(content.getDebtItems().get(position).getId_tiaoma());
        viewHolder.book_id_suo_shu.setText(content.getDebtItems().get(position).getId_suoshu());
        viewHolder.book_name.setText(content.getDebtItems().get(position).getName());
        viewHolder.book_authors.setText(content.getDebtItems().get(position).getAuthors());
        viewHolder.book_read_time.setText(content.getDebtItems().get(position).getReadTime());
        viewHolder.book_return_time.setText(content.getDebtItems().get(position).getReturnTime());
        viewHolder.book_sto_place.setText(content.getDebtItems().get(position).getPlace());
        viewHolder.book_should_pay.setText(content.getDebtItems().get(position).getShould_pay());
        viewHolder.book_actual_pay.setText(content.getDebtItems().get(position).getActual_pay());
        viewHolder.book_state.setText(content.getDebtItems().get(position).getState());
        viewHolder.break_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BookDetailActivity.class);
                intent.putExtra("url", content.getDebtItems().get(position).getBookurl());
                intent.putExtra("name", content.getDebtItems().get(position).getName());
                intent.putExtra("other", content.getDebtItems().get(position).getPlace());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView book_id_tiao_ma, book_id_suo_shu, book_name, book_authors, book_read_time, book_return_time, book_sto_place, book_should_pay, book_actual_pay, book_state;
        CardView break_book;
    }
}
