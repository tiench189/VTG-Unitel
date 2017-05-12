package com.vtg.app.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vtg.app.ActivityBasic3G;
import com.vtg.app.model.ModelMoreData;
import com.vtg.app.model.ModelParam;
import com.vtg.app.model.ModelSubData;
import com.vtg.app.model.ModelTag;
import com.vtg.app.util.CommonDefine;
import com.vtg.app.util.FunctionHelper;
import com.vtg.app.util.JSONParser;
import com.vtg.app.util.SOAPUtil;
import com.vtg.unitel.R;

public class AdapterSubData2 extends BaseAdapter implements CommonDefine {

	private Context context;
	private List<ModelSubData> listSub;
	private LayoutInflater inflater;
	private SharedPreferences preferences;

	public AdapterSubData2(Context context, List<ModelSubData> listSub) {
		super();
		this.context = context;
		this.listSub = listSub;
		inflater = ((Activity) context).getLayoutInflater();
		preferences = ((Activity) context).getSharedPreferences(MY_PACKAGE,
				Context.MODE_PRIVATE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listSub.size();
	}

	@Override
	public ModelSubData getItem(int position) {
		// TODO Auto-generated method stub
		return listSub.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	private class Holder {
		private TextView tvName;
		private TextView tvDes;
		private TextView tvType;
		private LinearLayout layoutActive;
		private LinearLayout layoutDeactive;
		private RelativeLayout btnActive;
		private RelativeLayout btnDeactive;
		private RelativeLayout btnBuyMore;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			view = inflater.inflate(R.layout.row_sub_data2, null);
			Holder holder = new Holder();
			holder.tvName = (TextView) view.findViewById(R.id.tv_sub_name);
			holder.tvDes = (TextView) view.findViewById(R.id.tv_sub_des);
			holder.tvType = (TextView) view.findViewById(R.id.tv_type_name);
			holder.layoutActive = (LinearLayout) view
					.findViewById(R.id.layout_active);
			holder.layoutDeactive = (LinearLayout) view
					.findViewById(R.id.layout_deactive);
			holder.btnActive = (RelativeLayout) view
					.findViewById(R.id.btn_active);
			holder.btnDeactive = (RelativeLayout) view
					.findViewById(R.id.btn_deactive);
			holder.btnBuyMore = (RelativeLayout) view
					.findViewById(R.id.btn_buy_more);
			view.setTag(holder);
		}
		Holder holder = (Holder) view.getTag();
		final ModelSubData sub = getItem(position);
		holder.tvName.setText(sub.name);
		holder.tvDes.setText(sub.value);
		holder.tvType.setText(sub.type_name);
		if (ActivityBasic3G.packCode.equals(sub.name)) {
			holder.layoutDeactive.setVisibility(View.VISIBLE);
			holder.layoutActive.setVisibility(View.GONE);
			// if (sub.buyMore && !sub.type.equals("2") &&
			// !sub.type.equals("4")) {
			// holder.btnBuyMore.setVisibility(View.VISIBLE);
			// } else {
			holder.btnBuyMore.setVisibility(View.GONE);
			// }
		} else {
			holder.layoutDeactive.setVisibility(View.GONE);
			holder.layoutActive.setVisibility(View.VISIBLE);
		}
		holder.btnActive.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final DialogConfirm cfDialog = new DialogConfirm(context,
						"Do you want to register " + sub.name + "?");
				cfDialog.btnYes.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						cfDialog.dismiss();
						if (sub.type.equals("1")) {
							new AsyncTaskActive().execute(sub.code,
									WSCode.REGISTER_MI);
						} else if (sub.type.equals("2")) {
							new AsyncTaskActive().execute(sub.code,
									WSCode.REGISTER_4G);
						} else if (sub.type.equals("3")) {
							new AsyncTaskActive().execute(sub.code,
									WSCode.REGISTER_LT);
						} else if (sub.type.equals("4")) {
							new AsyncTaskActive().execute(sub.code,
									WSCode.REGISTER_LT4G);
						}
					}

				});
				cfDialog.show();
			}
		});
		holder.btnDeactive.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final DialogConfirm cfDialog = new DialogConfirm(context,
						"Do you want to remove " + sub.name + "?");
				cfDialog.btnYes.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						cfDialog.dismiss();
						if (sub.type.equals("1") || sub.type.equals("3")) {
							new AsyncTaskDeactive().execute(WSCode.REMOVE_MI);
						} else {
							new AsyncTaskDeactive().execute(WSCode.REMOVE_4G);
						}
					}
				});
				cfDialog.show();
			}
		});
		holder.btnBuyMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AsyncTaskMore().execute(sub.name);
			}
		});
		return view;
	}

	private class AsyncTaskMore extends AsyncTask<String, String, Boolean> {
		ProgressDialog pDialog;
		List<ModelMoreData> listMoreData;

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				listMoreData = new ArrayList<ModelMoreData>();
				String subName = params[0];
				JSONParser jParser = new JSONParser();
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("pack", subName);
				String url = MyService.GET_EXTEND_DATA + "?pack=" + subName
						+ "&signature=" + FunctionHelper.makeSignatureAPT(map);
				Log.v("", "tiench get extend: " + url);
				JSONObject obj = new JSONObject(jParser.getJSONFromUrl(url,
						METHOD_GET, null));
				if (obj.getInt("status") == 1) {
					JSONArray arr = obj.getJSONArray("data");
					for (int i = 0; i < arr.length(); i++) {
						JSONObject more = arr.getJSONObject(i);
						ModelMoreData data = new ModelMoreData();
						data.code = more.getString("code");
						data.price = more.getString("price");
						data.value = more.getString("data");
						listMoreData.add(data);
					}
					return true;
				} else {
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(context);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if (result) {
				new DialogMoreData(context, listMoreData).show();
			} else {
				new DialogReport(context,
						context.getString(R.string.err_connect)).show();
			}
		}

	}

	private class AsyncTaskActive extends AsyncTask<String, String, Boolean> {
		ProgressDialog pDialog;
		String message = "";

		@Override
		protected Boolean doInBackground(String... prs) {
			// TODO Auto-generated method stub
			try {
				String code = prs[0];
				// String code = "MI0";
				String ws = prs[1];
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
				params.add(new ModelParam("command", code));
				SOAPUtil soap = new SOAPUtil(ws, tags, params);
				soap.makeSOAPRequest();
				if (soap.getError() == 0) {
					int errCode = Integer
							.parseInt(soap.getValue(mXML.ERR_CODE));
					if (errCode == 0) {
						message = soap.getValue(mXML.DESCRIPTION);
						return true;
					} else {
						message = soap.getValue(mXML.DESCRIPTION);
						return false;
					}
				} else {
					message = "Fail";
					return false;
				}
			} catch (Exception e) {
				message = context.getString(R.string.err_connect);
				return false;
			}

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(context);
			pDialog.setMessage(context.getString(R.string.message_loading));
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
				context.sendBroadcast(intent);
			}
			new DialogReport(context, message).show();
		}

	}

	private class AsyncTaskDeactive extends AsyncTask<String, String, Boolean> {
		ProgressDialog pDialog;
		String message = "";

		@Override
		protected Boolean doInBackground(String... prs) {
			// TODO Auto-generated method stub
			try {
				String ws = prs[0];
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
				params.add(new ModelParam("command", "OFF"));
				SOAPUtil soap = new SOAPUtil(ws, tags, params);
				soap.makeSOAPRequest();
				if (soap.getError() == 0) {
					int errCode = Integer
							.parseInt(soap.getValue(mXML.ERR_CODE));
					if (errCode == 0) {
						message = soap.getValue(mXML.DESCRIPTION);
						return true;
					} else {
						message = soap.getValue(mXML.DESCRIPTION);
						return false;
					}
				} else {
					message = "Fail";
					return false;
				}
			} catch (Exception e) {
				message = context.getString(R.string.err_connect);
				return false;
			}

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(context);
			pDialog.setMessage(context.getString(R.string.message_loading));
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
				context.sendBroadcast(intent);
			}
			new DialogReport(context, message).show();
		}

	}
}
