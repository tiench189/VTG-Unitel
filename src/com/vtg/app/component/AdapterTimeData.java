package com.vtg.app.component;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vtg.app.ActivityMain;
import com.vtg.app.model.ModelSubData;
import com.vtg.app.model.ModelTimeData;
import com.vtg.app.util.CommonDefine;
import com.vtg.app.util.FunctionHelper;
import com.vtg.unitel.R;

import java.util.List;

/**
 * Created by Windows 10 Gamer on 10/12/2016.
 */
public class AdapterTimeData extends BaseAdapter {

    private Context context;
    private List<ModelTimeData> listTimeData;
    private LayoutInflater inflater;

    private int width;

    public AdapterTimeData(Context context, List<ModelTimeData> listTimeData) {
        this.context = context;
        this.listTimeData = listTimeData;
        inflater = ((Activity) context).getLayoutInflater();
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listTimeData.size();
    }

    @Override
    public ModelTimeData getItem(int position) {
        // TODO Auto-generated method stub
        return listTimeData.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    private class Holder {
        TextView tvName;
        GridView gvData;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.row_time_data, null);
            Holder holder = new Holder();
            holder.tvName = (TextView) view.findViewById(R.id.tv_name);
            holder.gvData = (GridView) view.findViewById(R.id.gv_sub_data);
            view.setTag(holder);
        }
        Holder holder = (Holder) view.getTag();
        final ModelTimeData timeData = getItem(position);
        holder.tvName.setText(timeData.name);
        holder.gvData.setAdapter(new AdapterSubData(context, timeData.listSubs));
        FunctionHelper.setGridViewHeightBasedOnChildren(holder.gvData, 2, width);
        return view;
    }
}
