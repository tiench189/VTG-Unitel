package com.vtg.app.component;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vtg.app.model.ModelParam;
import com.vtg.app.model.ModelTag;
import com.vtg.app.util.CommonDefine;
import com.vtg.app.util.FunctionHelper;
import com.vtg.app.util.SOAPUtil;
import com.vtg.unitel.R;

public class DialogUpPay extends Dialog implements CommonDefine {
	private static final String TAG = "DialogUpPay";
	private Context mContext;

	public EditText edtNumber, edtPin;
	public RelativeLayout btnDone, btnCancel;
	public TextView tvTitle;

	SharedPreferences preferences;
	private boolean isDebit = false;

	public DialogUpPay(Context context, boolean isDebit) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_up_pay);
		this.mContext = context;
		this.isDebit = isDebit;
		preferences = ((Activity) context).getSharedPreferences(MY_PACKAGE,
				Context.MODE_PRIVATE);
		initView();
	}

	private void initView() {
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		edtNumber = (EditText) findViewById(R.id.edt_number);
		edtPin = (EditText) findViewById(R.id.edt_pin);

		btnDone = (RelativeLayout) findViewById(R.id.btn_done);
		btnCancel = (RelativeLayout) findViewById(R.id.btn_cancel);

		btnDone.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				submitPin();
			}
		});

		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}

	private void submitPin() {
		String pin = edtPin.getText().toString().trim();
		String number = edtNumber.getText().toString().trim();
		if (number.equals("")) {
			Toast.makeText(mContext,
					mContext.getString(R.string.message_empty_number),
					Toast.LENGTH_LONG).show();
		} else if (pin.equals("")) {

			Toast.makeText(mContext,
					mContext.getString(R.string.message_empty_pin),
					Toast.LENGTH_LONG).show();
		} else {
			dismiss();
			number = FunctionHelper.formatPhoneNumber(number, mContext);
			new AsyncTaskSubInfo().executeOnExecutor(
					AsyncTask.THREAD_POOL_EXECUTOR, number, pin);
		}
	}

	private class AsyncTaskSubInfo extends AsyncTask<String, String, Boolean> {
		String message = "";
		String number, pin;

		@Override
		protected Boolean doInBackground(String... p) {
			// TODO Auto-generated method stub
			try {
				number = p[0];
				pin = p[1];
				List<ModelTag> tags = new ArrayList<ModelTag>();
				tags.add(new ModelTag(SoapTag.USERNAME, USERNAME));
				tags.add(new ModelTag(SoapTag.PASSWORD, PASSWORD));
				tags.add(new ModelTag(SoapTag.RAWDATA, "?"));
				List<ModelParam> params = new ArrayList<ModelParam>();
				params.add(new ModelParam("isdn", number));
				SOAPUtil soap = new SOAPUtil(WSCode.GET_SUB_INFO, tags, params);
				soap.makeSOAPRequest();
				if (soap.getError() == 0) {
					int accType = Integer
							.parseInt(soap.getValue(mXML.SUB_TYPE));
					isDebit = accType == DEBIT;
					return true;
				} else {
					message = soap.getValue(mXML.DESCRIPTION);
					return false;
				}
			} catch (Exception e) {
				message = mContext.getString(R.string.err_connect);
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result) {
				new AsyncTaskPay().executeOnExecutor(
						AsyncTask.THREAD_POOL_EXECUTOR, number, pin);
			} else {
				DialogReport dlErr = new DialogReport(mContext, message);
				dlErr.show();
			}
		}

	}

	private class AsyncTaskPay extends AsyncTask<String, String, SOAPUtil> {
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(mContext);
			pDialog.setMessage(mContext.getString(R.string.message_checking));
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected SOAPUtil doInBackground(String... p) {
			// TODO Auto-generated method stub
			List<ModelTag> tags = new ArrayList<ModelTag>();
			tags.add(new ModelTag(SoapTag.USERNAME, USERNAME));
			tags.add(new ModelTag(SoapTag.PASSWORD, PASSWORD));
			tags.add(new ModelTag(SoapTag.RAWDATA, "?"));
			List<ModelParam> params = new ArrayList<ModelParam>();
			SOAPUtil soap;
			if (isDebit) {
				params.add(new ModelParam("msisdn", NUMBER_HEADER + p[0]));
				params.add(new ModelParam("calling", NUMBER_HEADER + p[0]));
				params.add(new ModelParam("pin", p[1]));
				params.add(new ModelParam("serviceId", "1"));
				params.add(new ModelParam("sendSms", "1"));
				soap = new SOAPUtil(WSCode.TOP_UP_PAY_BY_CARD, tags, params);
			} else {
				params.add(new ModelParam("isdn", p[0]));
				params.add(new ModelParam("calling", p[0]));
				params.add(new ModelParam("pin", p[1]));
				soap = new SOAPUtil(WSCode.PAY_BY_CARD, tags, params);
			}
			soap.makeSOAPRequest();
			return soap;
		}

		@Override
		protected void onPostExecute(SOAPUtil result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			int errCode = result.getError();
			int responseCode = Integer
					.parseInt(result.getValue("responseCode"));
			if (errCode == 0) {
				if (responseCode == 0) {
					dismiss();
					Toast.makeText(mContext,
							mContext.getString(R.string.message_success),
							Toast.LENGTH_LONG).show();
					Intent intent = new Intent();
					intent.setAction(Action.FILTER);
					intent.putExtra(Action.ACTION, Action.RELOAD_ACCOUNT);
					mContext.sendBroadcast(intent);
				} else {
					String message = result.getValue(mXML.DESCRIPTION);
					if (responseCode == 5) {
						message = "Mobile number is not activate";
					} else if (responseCode == 2 || responseCode == 6011
							|| responseCode == 6012) {
						message = "Card pin is not valid";
					}
					new DialogReport(mContext, message).show();
				}
			} else {
				new DialogReport(mContext, FunctionHelper.getErrorMessage(
						mContext, errCode)).show();
			}
		}
	}
}
