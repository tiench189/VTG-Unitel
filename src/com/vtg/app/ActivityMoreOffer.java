package com.vtg.app;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;

import com.vtg.app.component.AdapterSubOffer;
import com.vtg.app.component.AdapterVas;
import com.vtg.app.model.ModelOffer;
import com.vtg.app.model.ModelVas;
import com.vtg.app.util.CommonDefine;
import com.vtg.app.util.JSONParser;
import com.vtg.app.util.CommonDefine.MyService;
import com.vtg.unitel.R;

public class ActivityMoreOffer extends Activity implements CommonDefine {
	private SharedPreferences preferences;
	private Context mContext;

	private ListView lvSubData;
	private List<ModelOffer> listSub;
	private AdapterSubOffer adapterSub;

	private ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		setContentView(R.layout.activity_more_offer);

		mContext = this;

		preferences = getSharedPreferences(MY_PACKAGE, Context.MODE_PRIVATE);

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

		lvSubData = (ListView) findViewById(R.id.lv_sub_data);
		// listSub = new ArrayList<ModelOffer>();
		// listSub.add(new ModelOffer("Package1", "Thông tin gói 1", true));
		// listSub.add(new ModelOffer("Package2", "Thông tin gói 2", false));
		// listSub.add(new ModelOffer("Package3", "Thông tin gói 3", false));
		// adapterSub = new AdapterSubOffer(mContext, listSub);
		// lvSubData.setAdapter(adapterSub);
		new AsyntaskGetOffer()
				.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}

	private void actionBack() {
		finish();
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		actionBack();
	}

	private class AsyntaskGetOffer extends AsyncTask<String, String, Boolean> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(mContext);
			pDialog.setCancelable(false);
			pDialog.setMessage(mContext.getString(R.string.message_loading));
			pDialog.show();
			listSub = new ArrayList<ModelOffer>();
		}

		@Override
		protected Boolean doInBackground(String... arg0) {
			JSONParser jsonParser = new JSONParser();
			String strJson = jsonParser.getJSONFromUrl(MyService.PROMOTION,
					CommonDefine.METHOD_GET, null);
			try {
				JSONObject jsonObj = new JSONObject(strJson);
				JSONArray jsonArr = jsonObj.getJSONArray("data");
				for (int i = 0; i < jsonArr.length(); i++) {
					ModelOffer offer = new ModelOffer();
					offer.name = jsonArr.getJSONObject(i).getString(
							"promote_name");
					offer.info = jsonArr.getJSONObject(i).getString(
							"description");
					listSub.add(offer);
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
				lvSubData.setAdapter(new AdapterSubOffer(mContext, listSub));
			}
		}

	}

}
