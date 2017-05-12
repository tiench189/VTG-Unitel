package com.vtg.app;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;

import com.vtg.app.component.AdapterBanner;
import com.vtg.app.component.AdapterPromotion;
import com.vtg.app.model.ModelPromotion;
import com.vtg.app.util.CommonDefine;
import com.vtg.app.util.JSONParser;
import com.vtg.unitel.R;

public class ActivityAbout extends FragmentActivity implements CommonDefine,
		OnClickListener {

	private static final String TAG = "ActivityBasicAccount";

	private ImageView ivBack;

	private SharedPreferences preferences;
	private Context mContext;
	private WebView wv;

	private ProgressDialog pDialog;
	String about = "";

	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		setContentView(R.layout.activity_about);
		mContext = this;

		preferences = getSharedPreferences(MY_PACKAGE, Context.MODE_PRIVATE);
		initView();
		new AsyntaskAbout().execute();
	}

	private void initView() {
		ImageView ivBack = (ImageView) findViewById(R.id.ivBack);
		ivBack.setOnClickListener(this);
		wv = (WebView) findViewById(R.id.wv);
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ivBack:
			actionBack();
			break;
		default:
			break;
		}
	}

	private class AsyntaskAbout extends AsyncTask<String, String, Boolean> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(mContext);
			pDialog.setCancelable(false);
			pDialog.setMessage(mContext.getString(R.string.message_loading));
			pDialog.show();
		}

		@Override
		protected Boolean doInBackground(String... arg0) {
			JSONParser jsonParser = new JSONParser();
			String strJson = jsonParser.getJSONFromUrl(MyService.ABOUT,
					CommonDefine.METHOD_GET, null);
			try {
				JSONObject jsonObj = new JSONObject(strJson);
				about = jsonObj.getString("data");
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
				wv.getSettings().setJavaScriptEnabled(true);
				wv.loadDataWithBaseURL("", about, "text/html", "UTF-8", "");
			}
		}

	}

}
