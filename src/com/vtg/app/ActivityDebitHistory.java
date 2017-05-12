package com.vtg.app;

import java.util.ArrayList;
import java.util.List;

import com.vtg.app.component.DialogReport;
import com.vtg.app.model.ModelParam;
import com.vtg.app.model.ModelTag;
import com.vtg.app.util.CommonDefine;
import com.vtg.app.util.FunctionHelper;
import com.vtg.app.util.SOAPUtil;
import com.vtg.app.util.CommonDefine.Action;
import com.vtg.app.util.CommonDefine.PreferenceKey;
import com.vtg.app.util.CommonDefine.SoapTag;
import com.vtg.app.util.CommonDefine.WSCode;
import com.vtg.app.util.CommonDefine.mXML;
import com.vtg.unitel.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;

public class ActivityDebitHistory extends Activity implements CommonDefine {
	private SharedPreferences preferences;
	private Context mContext;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		setContentView(R.layout.activity_debit_history);

		mContext = this;

		preferences = getSharedPreferences(MY_PACKAGE, Context.MODE_PRIVATE);
		new AsyncTaskDebitHistory().execute();
	}

	private class AsyncTaskDebitHistory extends
			AsyncTask<String, String, Boolean> {
		ProgressDialog pDialog;
		String message = "";

		@Override
		protected Boolean doInBackground(String... prs) {
			// TODO Auto-generated method stub
			List<ModelTag> tags = new ArrayList<ModelTag>();
			tags.add(new ModelTag(SoapTag.USERNAME, USERNAME));
			tags.add(new ModelTag(SoapTag.PASSWORD, PASSWORD));
			tags.add(new ModelTag(SoapTag.RAWDATA, "?"));
			List<ModelParam> params = new ArrayList<ModelParam>();
			params.add(new ModelParam("msisdn", NUMBER_HEADER
					+ preferences.getString(PreferenceKey.PHONE_NUMBER, "")));
			params.add(new ModelParam("send_sms", "0"));
			params.add(new ModelParam("requestId", FunctionHelper
					.formatCurrentTime()));
			params.add(new ModelParam("command", "OFF"));
			SOAPUtil soap = new SOAPUtil(WSCode.GET_DEBIT_INFO_BUILD, tags,
					params);
			soap.makeSOAPRequest();
			if (soap.getError() == 0) {
				int errCode = Integer.parseInt(soap.getValue(mXML.ERR_CODE));
				if (errCode == 0) {
					message = soap.getValue(mXML.MESSAGE);
					return true;
				} else {
					message = soap.getValue(mXML.MESSAGE);
					return false;
				}
			} else {
				message = "Fail";
				return false;
			}

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(mContext);
			pDialog.setMessage(mContext.getString(R.string.message_loading));
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if (result) {
				Intent intent = new Intent();
				intent.setAction(Action.FILTER);
				intent.putExtra(Action.ACTION, Action.RELOAD_DATA);
				sendBroadcast(intent);
			}
			new DialogReport(mContext, message).show();
		}

	}
}
