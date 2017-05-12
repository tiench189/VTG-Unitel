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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import com.vtg.app.component.AdapterProvince;
import com.vtg.app.component.AdapterShowroom;
import com.vtg.app.component.AdapterShowroomSpinner;
import com.vtg.app.model.ModelProvince;
import com.vtg.app.model.ModelShowroom;
import com.vtg.app.util.CommonDefine;
import com.vtg.app.util.JSONParser;
import com.vtg.unitel.R;

public class ActivityShowroom extends Activity implements CommonDefine {

	private SharedPreferences preferences;
	private Context mContext;

	private Spinner spProvince, spShowrrom;
	private ListView lvShowrrom;
	private List<ModelProvince> listProvince;
	private List<ModelShowroom> listShowroom;

	private ProgressDialog pDialog;
	
	public static ModelShowroom crShowroom;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		setContentView(R.layout.activity_showroom);

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

		spProvince = (Spinner) findViewById(R.id.spProvine);
		spProvince.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				spShowrrom.setAdapter(new AdapterShowroomSpinner(mContext,
						listProvince.get(position).showrooms));
				lvShowrrom.setAdapter(new AdapterShowroom(mContext,
						listProvince.get(position).showrooms));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		spShowrrom = (Spinner) findViewById(R.id.spShowroom);
		lvShowrrom = (ListView) findViewById(R.id.lvShowroom);
		new AsyntaskGetProvince().execute();
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

	private class AsyntaskGetProvince extends
			AsyncTask<String, String, Boolean> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(mContext);
			pDialog.setCancelable(false);
			pDialog.setMessage(mContext.getString(R.string.message_loading));
			pDialog.show();
			listProvince = new ArrayList<ModelProvince>();
			listProvince.add(new ModelProvince("0", "Province"));
		}

		@Override
		protected Boolean doInBackground(String... arg0) {
			JSONParser jsonParser = new JSONParser();
			String strJson = jsonParser.getJSONFromUrl(MyService.SHOWROOM,
					CommonDefine.METHOD_GET, null);
			try {
				JSONObject jsonObj = new JSONObject(strJson);
				JSONArray jsonArr = jsonObj.getJSONArray("data");
				Log.v("", "tiench showrrom: " + jsonArr.toString());
				for (int i = 0; i < jsonArr.length(); i++) {
					ModelProvince province = new ModelProvince();
					province.id = jsonArr.getJSONObject(i).getString(
							"province_id");
					province.name = jsonArr.getJSONObject(i).getString(
							"province_name");
					List<ModelShowroom> showrooms = new ArrayList<ModelShowroom>();
					JSONArray arrSR = jsonArr.getJSONObject(i).getJSONArray(
							"list_showroom");
					for (int k = 0; k < arrSR.length(); k++) {
						ModelShowroom sr = new ModelShowroom();
						sr.id = arrSR.getJSONObject(k).getString("id");
						sr.name = arrSR.getJSONObject(k).getString(
								"showroom_name");
						sr.description = arrSR.getJSONObject(k).getString(
								"description");
						sr.img = arrSR.getJSONObject(k).getString("image");
						sr.latitude = Double.parseDouble(arrSR.getJSONObject(k).getString("lat"));
						sr.longitude = Double.parseDouble(arrSR.getJSONObject(k).getString("long"));
						showrooms.add(sr);
					}
					province.showrooms = showrooms;
					listProvince.add(province);
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
			spProvince.setAdapter(new AdapterProvince(mContext, listProvince));
		}

	}

}
