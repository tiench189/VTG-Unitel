package com.vtg.app;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.vtg.app.component.AdapterSubData;
import com.vtg.app.component.AdapterTimeData;
import com.vtg.app.model.ModelSubData;
import com.vtg.app.model.ModelTimeData;
import com.vtg.app.util.CommonDefine;
import com.vtg.app.util.JSONParser;
import com.vtg.unitel.R;

public class ActivityBuyPackage extends Activity implements CommonDefine {
    private SharedPreferences preferences;
    private Context mContext;

    //	private GridView gvSubData;
//	private List<ModelSubData> listSub;
//	private AdapterSubData adapterSub;
    private ListView lvData;
    private List<ModelTimeData> listData;
    private AdapterTimeData adapterData;

    private ProgressDialog pDialog;
    private BasicDataReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        setContentView(R.layout.activity_buy_data);

        mContext = this;

        preferences = getSharedPreferences(MY_PACKAGE, Context.MODE_PRIVATE);

        receiver = new BasicDataReceiver();
        registerReceiver(receiver, new IntentFilter(Action.FILTER));

        initView();
    }

    private void initView() {
        ImageView ivBack = (ImageView) findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                actionBack();
            }
        });

//		gvSubData = (GridView) findViewById(R.id.gv_sub_data);
        lvData = (ListView) findViewById(R.id.lv_data);
        new AsyntaskGetSubData().execute();

    }

    private void actionBack() {
        try {
            unregisterReceiver(receiver);
        } catch (Exception e) {
        }
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        actionBack();
    }

    private class AsyntaskGetSubData extends AsyncTask<String, String, Boolean> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pDialog = new ProgressDialog(mContext);
            pDialog.setCancelable(false);
            pDialog.setMessage(mContext.getString(R.string.message_loading));
            pDialog.show();
//			listSub = new ArrayList<ModelSubData>();
            listData = new ArrayList<ModelTimeData>();
        }

        @Override
        protected Boolean doInBackground(String... arg0) {
            JSONParser jsonParser = new JSONParser();
            String strJson = jsonParser.getJSONFromUrl(MyService.GET_TIME_DATA,
                    CommonDefine.METHOD_GET, null);
            Log.v("JSON List data", "tiench: " + strJson);
            try {
                JSONObject jsonObj = new JSONObject(strJson);
                JSONArray jsonArr = jsonObj.getJSONArray("data");
                for (int k = 0; k < jsonArr.length(); k++) {
                    ModelTimeData timeData = new ModelTimeData();
                    timeData.name = jsonArr.getJSONObject(k).getString("type_time_name");
                    JSONArray dataArr = jsonArr.getJSONObject(k).getJSONArray("data");
                    for (int i = 0; i < dataArr.length(); i++) {
                        ModelSubData sub = new ModelSubData();
                        sub.name = dataArr.getJSONObject(i).getString("pack_name");
                        sub.code = dataArr.getJSONObject(i).getString("code").trim();
                        sub.value = dataArr.getJSONObject(i).getString("description");
                        sub.price = dataArr.getJSONObject(i).getString("price");
                        sub.type = dataArr.getJSONObject(i).getString("type").trim();
//                        sub.type_name = dataArr.getJSONObject(i).getString("type_name");
                        sub.buyMore = dataArr.getJSONObject(i).getInt("extent") == 1;
                        sub.data = dataArr.getJSONObject(i).getString("data");
                        sub.time = dataArr.getJSONObject(i).getString("time_unit");
                        timeData.listSubs.add(sub);
                    }
                    if (timeData.listSubs.size() > 0) {
                        listData.add(timeData);
                    }
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            if (result) {
                adapterData = new AdapterTimeData(mContext, listData);
                lvData.setAdapter(adapterData);
            }
        }

    }

    private class BasicDataReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            Bundle mBundle = intent.getExtras();
            if (mBundle.getString(Action.ACTION)
                    .equals(Action.DONE_RELOAD_DATA)) {
                try {
                    adapterData.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
