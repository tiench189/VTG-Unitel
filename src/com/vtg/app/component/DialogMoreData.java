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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.vtg.app.model.ModelMoreData;
import com.vtg.app.model.ModelParam;
import com.vtg.app.model.ModelTag;
import com.vtg.app.util.CommonDefine;
import com.vtg.app.util.FunctionHelper;
import com.vtg.app.util.SOAPUtil;
import com.vtg.unitel.R;

public class DialogMoreData extends Dialog implements CommonDefine {

	private Context mContext;
//	private ModelSubData subData;
	private ListView lvMore;
	private AdapterModeData adapterMore;
	private RelativeLayout btnBuy, btnCancel;
	private SharedPreferences preferences;

	public DialogMoreData(Context context, List<ModelMoreData> listMoreData) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_more_data);

		this.mContext = context;
//		this.subData = subData;

		preferences = ((Activity) context).getSharedPreferences(MY_PACKAGE,
				Context.MODE_PRIVATE);

		lvMore = (ListView) findViewById(R.id.lv_more_data);
		adapterMore = new AdapterModeData(mContext, listMoreData);
		lvMore.setAdapter(adapterMore);
		// FunctionHelper.setListViewHeightBasedOnChildren(lvMore);

		btnBuy = (RelativeLayout) findViewById(R.id.btn_buy);
		btnBuy.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (adapterMore.pick == -1) {
					Toast.makeText(mContext, "Pick a package!",
							Toast.LENGTH_LONG).show();
				} else {
					new AsyncTaskBuyData().execute();
				}
			}
		});

		btnCancel = (RelativeLayout) findViewById(R.id.btn_cancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
	}

	private class AsyncTaskBuyData extends AsyncTask<String, String, Boolean> {
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
			params.add(new ModelParam("send_sms", "1"));
			params.add(new ModelParam("requestId", FunctionHelper
					.formatCurrentTime()));
			params.add(new ModelParam("command", adapterMore
					.getItem(adapterMore.pick).code));
			SOAPUtil soap = new SOAPUtil(WSCode.BUY_DATA, tags, params);
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
				mContext.sendBroadcast(intent);
			}
			new DialogReport(mContext, message).show();
			dismiss();
		}

	}
}
