package com.vtg.app.component;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vtg.unitel.R;
import com.vtg.app.model.ModelBalance;
import com.vtg.app.util.FunctionHelper;

public class AdapterBalanceInfo extends BaseAdapter {

    private LayoutInflater inflater;
    private List<ModelBalance> listBalances;
    private Context context;

    public AdapterBalanceInfo(Context context, List<ModelBalance> listBalances) {
        this.listBalances = listBalances;
        inflater = ((Activity) context).getLayoutInflater();
        this.context = context;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listBalances.size();
    }

    @Override
    public ModelBalance getItem(int position) {
        // TODO Auto-generated method stub
        return listBalances.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    private class Holder {
        RelativeLayout layoutBalance;
        TextView tvName;
        TextView tvValue;
        TextView tvExpire;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.row_balance_info, null);
            Holder holder = new Holder();
            holder.layoutBalance = (RelativeLayout) view
                    .findViewById(R.id.layout_balance);
            holder.tvName = (TextView) view.findViewById(R.id.tv_balance_name);
            holder.tvValue = (TextView) view
                    .findViewById(R.id.tv_balance_value);
            holder.tvExpire = (TextView) view
                    .findViewById(R.id.tv_balance_expire);
            view.setTag(holder);
        }
        ModelBalance balance = getItem(position);
        Holder holder = (Holder) view.getTag();
        // Log.i("", "tiench id: " + balance.id);
        try {
            holder.tvName.setText(FunctionHelper.mapBalanceName(balance, context));
        } catch (Exception e) {
            e.printStackTrace();
            holder.tvName.setText("");
        }
        holder.tvValue.setText(balance.value + FunctionHelper.mapBalanceUnit(balance));
        holder.tvExpire.setText(FunctionHelper.formatDateTime(balance.expire));
        // if (position % 2 == 0) {
        // holder.layoutBalance.setBackgroundColor(Color.WHITE);
        // } else {
        // holder.layoutBalance
        // .setBackgroundColor(Color.parseColor("#F6F6F6"));
        // }
        return view;
    }
}
