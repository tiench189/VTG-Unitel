package com.vtg.app.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.vtg.app.ActivityFirst;
import com.vtg.app.ActivityMain.mProfile;
import com.vtg.app.model.ModelParam;
import com.vtg.app.model.ModelTag;
import com.vtg.app.util.CommonDefine;
import com.vtg.app.util.FunctionHelper;
import com.vtg.app.util.JSONParser;
import com.vtg.app.util.SOAPUtil;
import com.vtg.unitel.R;

public class DialogChangePass extends Dialog implements CommonDefine {

	private Context mContext;
	private RelativeLayout btnCancel, btnDone;
	private SharedPreferences preferences;
	private ProgressDialog pDialog;

	public EditText edtNumber;
	private EditText edtOldPassword, edtNewPassword;
	private String number, oldPass, newPass;

	public DialogChangePass(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_change_pass);
		this.mContext = context;
		preferences = context.getSharedPreferences(MY_PACKAGE,
				Context.MODE_PRIVATE);
		pDialog = new ProgressDialog(context);
		pDialog.setCancelable(false);
		initView();
	}

	private void initView() {
		edtNumber = (EditText) findViewById(R.id.edt_number);
		edtNumber.setText(FunctionHelper.getPhoneNumber(mContext));
		edtOldPassword = (EditText) findViewById(R.id.edt_old_password);
		edtNewPassword = (EditText) findViewById(R.id.edt_new_password);

		btnCancel = (RelativeLayout) findViewById(R.id.btn_cancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
		btnDone = (RelativeLayout) findViewById(R.id.btn_done);
		btnDone.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				number = edtNumber.getText().toString().trim();
				if (number.startsWith("0")) {
					number = number.substring(1);
				}
				oldPass = edtOldPassword.getText().toString().trim();
				newPass = edtNewPassword.getText().toString().trim();
				if (number.equals("") || oldPass.equals("")
						|| newPass.equals("")) {
					if (number.equals("")) {
						new DialogReport(mContext, mContext
								.getString(R.string.message_empty_number))
								.show();
						edtNumber.requestFocus();
					} else if (oldPass.equals("")) {
						new DialogReport(mContext, mContext
								.getString(R.string.message_empty_old_password))
								.show();
						edtOldPassword.requestFocus();
					} else if (newPass.equals("")) {
						new DialogReport(mContext, mContext
								.getString(R.string.message_empty_new_password))
								.show();
						edtNewPassword.requestFocus();
					}
				} else if (oldPass.equals(newPass)) {
					new DialogReport(mContext,
							"New password is equal to old pasword!").show();
					edtNewPassword.requestFocus();
				} else {
					number = FunctionHelper.formatPhoneNumber(number, mContext);
					new AsyncTaskChange().execute(number);
				}
			}
		});
	}

	private class AsyncTaskChange extends AsyncTask<String, String, Boolean> {
		String msg = "";
		String phone;
		int status;

		@Override
		protected Boolean doInBackground(String... params) {
			try {
				phone = params[0];
				JSONParser jParser = new JSONParser();
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("phone", phone);
				map.put("oldpassword", oldPass);
				map.put("newpassword", newPass);
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair(SIGNATURE,
						FunctionHelper.makeSignatureAPT(map)));
				nameValuePairs.add(new BasicNameValuePair("phone", phone));
				nameValuePairs.add(new BasicNameValuePair("oldpassword",
						oldPass));
				nameValuePairs.add(new BasicNameValuePair("newpassword",
						newPass));
				String json = jParser.getJSONFromUrl(MyService.CHANGE_PASS,
						METHOD_POST, nameValuePairs);
				Log.v("", "tiench change pass: " + json);
				JSONObject obj = new JSONObject(json);
				status = obj.getInt("status");
				if (status == 1) {
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
			pDialog.setMessage("Connecting...");
			pDialog.show();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if (result) {
				dismiss();
				new DialogReport(mContext, "Change password success!").show();
				ActivityFirst.edtNumber.setText(number);
			} else {
				new DialogReport(mContext, msg).show();
				if (status == 313) {
					edtOldPassword.requestFocus();
				}
			}
		}

	}
}
