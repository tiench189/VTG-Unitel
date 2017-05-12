package com.vtg.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vtg.app.component.DialogChangePass;
import com.vtg.app.component.DialogConfirm;
import com.vtg.app.component.DialogRegister;
import com.vtg.app.component.DialogReport;
import com.vtg.app.util.CommonDefine;
import com.vtg.app.util.FunctionHelper;
import com.vtg.app.util.JSONParser;
import com.vtg.app.util.CommonDefine.MyService;
import com.vtg.unitel.R;

public class ActivityFirst extends Activity implements CommonDefine {
	private SharedPreferences preferences;
	private Context mContext;

	public static EditText edtNumber, edtPassword;

	private RelativeLayout btnLogin, btnRegister;
	private CheckBox cbSave;
	private String number;
	private ProgressDialog pDialog;

	private TextView tvForget, tvChangePass;

	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		mContext = this;

		pDialog = new ProgressDialog(mContext);
		pDialog.setCancelable(false);

		preferences = getSharedPreferences(MY_PACKAGE, Context.MODE_PRIVATE);
		// if (preferences.getString(PreferenceKey.PHONE_NUMBER, "") == "") {
		// preferences.edit()
		// .putString(PreferenceKey.PHONE_NUMBER, "975775775")
		// .commit();
		// }
		// preferences.edit().putString(PreferenceKey.PHONE_NUMBER,
		// "2095000470")
		// .commit();
		// preferences.edit()
		// .putBoolean(PreferenceKey.SAVE_LOGIN, true)
		// .commit();
		// Intent intent = new Intent(mContext, ActivityMain.class);
		// startActivity(intent);
		// finish();
	}

	private void activityCreate() {
		setContentView(R.layout.activity_first);
		initView();
	}

	private void initView() {
		edtNumber = (EditText) findViewById(R.id.edt_number);
		edtPassword = (EditText) findViewById(R.id.edt_password);
		edtNumber.setText(FunctionHelper.formatPhoneNumber(
				FunctionHelper.getPhoneNumber(mContext), mContext)); // trả sau
		// 0979065992

		btnLogin = (RelativeLayout) findViewById(R.id.btn_login);
		btnRegister = (RelativeLayout) findViewById(R.id.btn_register);

		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				number = edtNumber.getText().toString().trim();
				number = FunctionHelper.formatPhoneNumber(number, mContext);
				String password = edtPassword.getText().toString().trim();
				if (number.equals("")) {
					Toast.makeText(mContext,
							getString(R.string.message_empty_number),
							Toast.LENGTH_LONG).show();
				} else if (password.equals("")) {
					Toast.makeText(mContext,
							getString(R.string.message_empty_password),
							Toast.LENGTH_LONG).show();
				} else {
					new AsyncTaskLogin().execute(number, password);
				}

			}
		});

		btnRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DialogRegister dlRegister = new DialogRegister(mContext);
				dlRegister.show();
			}
		});

		tvForget = (TextView) findViewById(R.id.tv_forget);
		tvForget.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				number = edtNumber.getText().toString().trim();
				number = FunctionHelper.formatPhoneNumber(number, mContext);
				if (!number.equals("")) {
					final DialogConfirm cf = new DialogConfirm(mContext,
							"Do you want reset password");
					cf.btnYes.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							cf.dismiss();
							new AsyncTaskReset().execute(number);
						}
					});
					cf.show();
				} else {
					new DialogReport(mContext, "Empty"
							+ ", " + getString(R.string.hint_number)).show();
				}
			}
		});

		tvChangePass = (TextView) findViewById(R.id.tv_change_pass);
		tvChangePass.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DialogChangePass dlChange = new DialogChangePass(mContext);
				dlChange.edtNumber.setText(edtNumber.getText().toString()
						.trim());
				dlChange.show();
			}
		});

		cbSave = (CheckBox) findViewById(R.id.cb_save);
	}

	private class AsyncTaskReset extends AsyncTask<String, String, Boolean> {
		String msg = "";
		String phone;

		@Override
		protected Boolean doInBackground(String... params) {
			try {
				phone = params[0];
				JSONParser jParser = new JSONParser();
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("phone", phone);
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair(SIGNATURE,
						FunctionHelper.makeSignatureAPT(map)));
				nameValuePairs.add(new BasicNameValuePair("phone", phone));
				// nameValuePairs.add(new BasicNameValuePair("password", pass));
				String json = jParser.getJSONFromUrl(MyService.RESET,
						METHOD_POST, nameValuePairs);
				Log.v("", "tiench reset: " + json);
				JSONObject obj = new JSONObject(json);
				if (obj.getInt("status") == 1) {
					return true;
				} else {
					msg = obj.getString("msg");
					return false;
				}
			} catch (Exception e) {
				return false;
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog.setMessage("Send request ...");
			pDialog.show();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if (result) {
				new DialogReport(mContext,
						"Your password had reset! We send a sms with your new password to number "
								+ phone + ".").show();
				ActivityFirst.edtNumber.setText(number);
			} else {
				new DialogReport(mContext, msg).show();
			}
		}

	}

	private class AsyncTaskLogin extends AsyncTask<String, String, Boolean> {
		String msg = "";

		@Override
		protected Boolean doInBackground(String... params) {
			try {
				String phone = params[0];
				String pass = params[1];
				Log.v("", "tiench: " + phone + "/" + pass);
				JSONParser jParser = new JSONParser();
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("phone", phone);
				map.put("password", pass);
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair(SIGNATURE,
						FunctionHelper.makeSignatureAPT(map)));
				nameValuePairs.add(new BasicNameValuePair("phone", phone));
				nameValuePairs.add(new BasicNameValuePair("password", pass));
				Log.v("",
						"tiench param login: "
								+ FunctionHelper.makeSignatureAPT(map));
				String json = jParser.getJSONFromUrl(MyService.LOGIN,
						METHOD_POST, nameValuePairs);
				Log.v("", "tiench login: " + json);
				JSONObject obj = new JSONObject(json);
				if (obj.getInt("status") == 1) {
					return true;
				} else {
					msg = obj.getString("msg");
					return false;
				}
			} catch (Exception e) {
				msg = "Connection fail!";
				return false;
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog.setMessage("Login...");
			pDialog.show();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if (result) {
				preferences.edit()
						.putString(PreferenceKey.PHONE_NUMBER, number).commit();
				preferences
						.edit()
						.putBoolean(PreferenceKey.SAVE_LOGIN,
								cbSave.isChecked()).commit();
				Intent intent = new Intent(mContext, ActivityHome.class);
				startActivity(intent);
				finish();
			} else {
				new DialogReport(mContext, msg).show();
			}
		}

	}

	public void setLanguage() {
		Locale locale = new Locale(preferences.getString(PreferenceKey.LOCATE,
				"en"));
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getBaseContext().getResources().updateConfiguration(config,
				getBaseContext().getResources().getDisplayMetrics());
		activityCreate();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		setLanguage();
	}

}
