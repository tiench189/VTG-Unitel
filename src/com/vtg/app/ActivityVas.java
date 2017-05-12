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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;

import com.vtg.app.component.AdapterVas;
import com.vtg.app.model.ModelVas;
import com.vtg.app.util.CommonDefine;
import com.vtg.app.util.JSONParser;
import com.vtg.unitel.R;

public class ActivityVas extends Activity implements CommonDefine {
	private SharedPreferences preferences;
	private Context mContext;

	private ListView lvVas;
	private List<ModelVas> listVas;
	private AdapterVas adapterVas;

	private ProgressDialog pDialog;
	private VasReceiver receiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		setContentView(R.layout.activity_vas);

		mContext = this;

		preferences = getSharedPreferences(MY_PACKAGE, Context.MODE_PRIVATE);

		receiver = new VasReceiver();
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

		lvVas = (ListView) findViewById(R.id.lv_vas);
		new AsyntaskGetVas().execute();

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

	private class AsyntaskGetVas extends AsyncTask<String, String, Boolean> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(mContext);
			pDialog.setCancelable(false);
			pDialog.setMessage(mContext.getString(R.string.message_loading));
			pDialog.show();
			listVas = new ArrayList<ModelVas>();
		}

		@Override
		protected Boolean doInBackground(String... arg0) {
			JSONParser jsonParser = new JSONParser();
			String strJson = jsonParser.getJSONFromUrl(MyService.VAS,
					CommonDefine.METHOD_GET, null);
			try {
				JSONObject jsonObj = new JSONObject(strJson);
				JSONArray jsonArr = jsonObj.getJSONArray("data");
				for (int i = 0; i < jsonArr.length(); i++) {
					ModelVas vas = new ModelVas();
					vas.id = jsonArr.getJSONObject(i).getString("id");
					vas.name = jsonArr.getJSONObject(i).getString("vas_name");
					vas.code = jsonArr.getJSONObject(i).getString("vas_code");
					vas.description = jsonArr.getJSONObject(i).getString(
							"description");
					vas.guide = jsonArr.getJSONObject(i).getString("guide");
					vas.status = jsonArr.getJSONObject(i).getInt("status");
					listVas.add(vas);
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
				adapterVas = new AdapterVas(mContext, listVas);
				lvVas.setAdapter(adapterVas);
			}
		}

	}

	private class VasReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Bundle mBundle = intent.getExtras();
		}

	}
}
