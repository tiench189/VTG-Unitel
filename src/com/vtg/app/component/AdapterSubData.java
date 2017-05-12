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
import com.vtg.app.ActivityMain;
import com.vtg.app.model.ModelMoreData;
import com.vtg.app.model.ModelParam;
import com.vtg.app.model.ModelSubData;
import com.vtg.app.model.ModelTag;
import com.vtg.app.util.CommonDefine;
import com.vtg.app.util.FunctionHelper;
import com.vtg.app.util.JSONParser;
import com.vtg.app.util.SOAPUtil;
import com.vtg.unitel.R;

public class AdapterSubData extends BaseAdapter implements CommonDefine {

    private Context context;
    private List<ModelSubData> listSub;
    private LayoutInflater inflater;
    private SharedPreferences preferences;

    public AdapterSubData(Context context, List<ModelSubData> listSub) {
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
        TextView tvName;
        TextView tvTime;
        TextView tvPrice;
        TextView tvData;
        RelativeLayout btnRegister;
        TextView tvAction;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.row_sub_data, null);
            Holder holder = new Holder();
            holder.tvName = (TextView) view.findViewById(R.id.tv_sub_name);
            holder.tvTime = (TextView) view.findViewById(R.id.tv_time);
            holder.tvData = (TextView) view.findViewById(R.id.tv_data);
            holder.tvPrice = (TextView) view.findViewById(R.id.tv_price);
            holder.btnRegister = (RelativeLayout) view
                    .findViewById(R.id.btn_register);
            holder.tvAction = (TextView) view.findViewById(R.id.tv_action);
            view.setTag(holder);
        }
        Holder holder = (Holder) view.getTag();
        final ModelSubData data = listSub.get(position);
        holder.tvName.setText(data.name);
        holder.tvTime.setText(data.time);
        try {
            int d = Integer.parseInt(data.data);
            holder.tvData.setText(data.data + "MB");
        } catch (Exception e) {
            holder.tvData.setText(data.data);
        }

        if (!data.price.equals("0")) {
            holder.tvPrice.setText(data.price + " kip");
        } else {
            holder.tvPrice.setText("Free");
        }

        if (data.code.equals(ActivityMain.myInternet.packageCode)) {
            if (!data.type.equals("3") && !data.type.equals("4") && !data.type.equals("6")) {
                holder.tvAction.setText(context.getString(R.string.cancel));
            } else {
                holder.tvAction.setText(context.getString(R.string.register));
            }
        } else {
            holder.tvAction.setText(context.getString(R.string.register));
        }
        holder.btnRegister.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (data.code.equals(ActivityMain.myInternet.packageCode) && !data.type.equals("3") && !data.type.equals("4") && !data.type.equals("6")) {
                    final DialogConfirm cfDialog = new DialogConfirm(context,
                            "Do you want to remove " + data.name + "?");
                    cfDialog.btnYes.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            cfDialog.dismiss();
                            if (data.type.equals("2") || data.type.equals("4")) {
                                new AsyncTaskDeactive().execute(WSCode.REMOVE_4G);
                            } else {
                                new AsyncTaskDeactive().execute(WSCode.REMOVE_MI_NEW);
                            }
                        }
                    });
                    cfDialog.show();
                } else {
                    if (data.type.equals("2")) {
                        new AsyncTaskActive().execute(data.code,
                                WSCode.REGISTER_4G);
                    } else if (data.type.equals("3")) {
                        new AsyncTaskActive().execute(data.code,
                                WSCode.REGISTER_LT);
                    } else if (data.type.equals("4")) {
                        new AsyncTaskActive().execute(data.code,
                                WSCode.REGISTER_LT4G);
                    } else if (data.type.equals("5")) {
                        new AsyncTaskActive().execute(data.code,
                                WSCode.REGISTER_MI_NEW);
                    } else if (data.type.equals("6")) {
                        new AsyncTaskActive().execute(data.code,
                                WSCode.REGISTER_LT_NEW);
                    } else {
                        new AsyncTaskActive().execute(data.code,
                                WSCode.REGISTER_MI);
                    }
                }
            }
        });
        return view;
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
                e.printStackTrace();
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
