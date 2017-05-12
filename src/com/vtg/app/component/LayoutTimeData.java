package com.vtg.app.component;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vtg.app.model.ModelTimeData;
import com.vtg.app.util.FunctionHelper;
import com.vtg.unitel.R;

/**
 * Created by Windows 10 Gamer on 10/12/2016.
 */
public class LayoutTimeData extends LinearLayout {

    private Context mContext;
    private ModelTimeData data;
    private View vi;
    TextView tvName;
    GridView gvData;
    private AdapterSubData adapterSub;

    public LayoutTimeData(Context context, ModelTimeData data) {
        super(context);
        this.mContext = context;
        this.data = data;
        adapterSub = new AdapterSubData(context, data.listSubs);
        init();
    }

    public void init() {
        try {
            if (vi == null) {
                // inflate layout
                LayoutInflater mLayoutInflater = (LayoutInflater) mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                vi = mLayoutInflater.inflate(R.layout.layout_time_data, null);
                tvName = (TextView) vi.findViewById(R.id.tv_name);
                tvName.setText(data.name);
                gvData = (GridView) vi.findViewById(R.id.gv_sub_data);
                gvData.setAdapter(adapterSub);
                Display display = ((Activity)mContext).getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;
                Log.v("tiench", "tiench screen width: " + width);
                FunctionHelper.setGridViewHeightBasedOnChildren(gvData, 2, width);
                addView(vi, new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void notifyChange() {
        try {
            adapterSub.notifyDataSetChanged();
        } catch (Exception e) {

        }
    }
}
