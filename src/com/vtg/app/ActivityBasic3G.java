package com.vtg.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.triggertrap.seekarc.SeekArc;
import com.vtg.app.ActivityMain.mProfile;
import com.vtg.app.component.AdapterBanner;
import com.vtg.app.component.DialogConfirm;
import com.vtg.app.component.DialogReport;
import com.vtg.app.model.ModelMoreData;
import com.vtg.app.model.ModelParam;
import com.vtg.app.model.ModelTag;
import com.vtg.app.util.CommonDefine;
import com.vtg.app.util.FunctionHelper;
import com.vtg.app.util.SOAPUtil;
import com.vtg.unitel.R;

public class ActivityBasic3G extends FragmentActivity implements CommonDefine,
        OnClickListener {

    private static final String TAG = "ActivityBasic3G";

    private ImageView ivBack;
    Configuration config;

    private SharedPreferences preferences;
    private Context mContext;

    private String locate = "";

    // Basic account
    private TextView tvDataValue;
    private RelativeLayout btnBuyData, btnDataDetail;

    private BasicDataReceiver receiver;

    private boolean getData = false;
    public static String data3g = "";
    public static String packCode = "";
    private ProgressDialog pDialog;

    private LinearLayout layoutMess;
    private RelativeLayout layoutDetail;
    private TextView tvInternetAmount;
    private SeekArc seekAmount;
    private TextView tvStatus, tvPlan, tvDataVolume, tvDataLeft, tvDataName;
    private TextView tvCancel, tvCancel1;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate(arg0);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        mContext = this;

        preferences = getSharedPreferences(MY_PACKAGE, Context.MODE_PRIVATE);

        receiver = new BasicDataReceiver();
        registerReceiver(receiver, new IntentFilter(Action.FILTER));
        pDialog = new ProgressDialog(mContext);
        pDialog.setCancelable(false);
        Log.v(TAG, "tiench onCreated");

    }

    // Banner
    private ViewPager pagerBanner;
    private AdapterBanner adapterBanner;

    // End Banner
    @SuppressWarnings("deprecation")
    private void initBanner() {
        pagerBanner = (ViewPager) findViewById(R.id.pager_banner);
        adapterBanner = new AdapterBanner(getSupportFragmentManager());
        pagerBanner.setAdapter(adapterBanner);

        changeBanner();
    }

    private void changeBanner() {
        try {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    int current = pagerBanner.getCurrentItem();
                    if (current < 3) {
                        current++;
                    } else {
                        current = 0;
                    }
                    pagerBanner.setCurrentItem(current);
                    changeBanner();
                }
            }, 10000);
        } catch (Exception e) {

        }
    }


    private void activityCreate() {
        setContentView(R.layout.activity_basic_3g);
        initView();
        initBanner();
    }

    private void initView() {
        pDialog = new ProgressDialog(mContext);
        layoutDetail = (RelativeLayout) findViewById(R.id.layout_detail);
        layoutMess = (LinearLayout) findViewById(R.id.layout_mess);
        tvInternetAmount = (TextView) findViewById(R.id.tv_internet_amount);
        seekAmount = (SeekArc) findViewById(R.id.seek_amount);
        seekAmount.setEnabled(false);
        tvStatus = (TextView) findViewById(R.id.tv_status);
        tvPlan = (TextView) findViewById(R.id.tv_plan);
        tvDataVolume = (TextView) findViewById(R.id.tv_data_volume);
        tvDataLeft = (TextView) findViewById(R.id.tv_data_left);
        tvDataName = (TextView) findViewById(R.id.tv_data_name);

        ivBack = (ImageView) findViewById(R.id.ivBack);
        ivBack.setOnClickListener(this);
        // Basic account
        tvDataValue = (TextView) findViewById(R.id.tv_data_value);
        btnBuyData = (RelativeLayout) findViewById(R.id.btn_buy_data);
        btnBuyData.setOnClickListener(this);

        RelativeLayout btnBuyData1 = (RelativeLayout) findViewById(R.id.btn_buy_data1);
        btnBuyData1.setOnClickListener(this);
        btnDataDetail = (RelativeLayout) findViewById(R.id.btn_data_detail);
        btnDataDetail.setOnClickListener(this);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(this);

        tvCancel1 = ((TextView) findViewById(R.id.tv_cancel1));
        tvCancel1.setOnClickListener(this);
//		if (!getData) {
//			new AsyncTaskCheck3G().execute();
//		} else {
        setBasicAccount();
//		}
    }

    private void setBasicAccount() {
        if (ActivityMain.myInternet.name.equals("") || ActivityMain.myInternet.dataVolume == 0) {
            layoutDetail.setVisibility(View.GONE);
            layoutMess.setVisibility(View.VISIBLE);
            tvDataValue.setText(ActivityMain.myInternet.mess);
            if (!ActivityMain.myInternet.avalable) {
                tvCancel.setVisibility(View.GONE);
                tvCancel1.setVisibility(View.GONE);
            }
        } else {
            layoutDetail.setVisibility(View.VISIBLE);
            layoutMess.setVisibility(View.GONE);
            tvDataName.setText(ActivityMain.myInternet.name);
            tvInternetAmount.setText(ActivityMain.myInternet.dataLeft + "MB");
            int progress = (int) (ActivityMain.myInternet.dataLeft * 100 / ActivityMain.myInternet.dataVolume);
            Log.d(TAG, "tiench data circle: " + ActivityMain.myInternet.dataLeft + "/" +
                    ActivityMain.myInternet.dataVolume + "=" + progress);
            seekAmount.setProgress(progress);
            tvStatus.setText(ActivityMain.myInternet.mess);
            tvPlan.setText("Plan fee: " + ActivityMain.myInternet.plan);
            tvDataVolume.setText("Data volume: " + ActivityMain.myInternet.dataVolume + "MB");
            tvDataLeft.setText("Data left: " + ActivityMain.myInternet.dataLeft + "MB");
            if (ActivityMain.myInternet.type.equals("3") || ActivityMain.myInternet.type.equals("4") || ActivityMain.myInternet.type.equals("6")) {
                tvCancel.setVisibility(View.GONE);
                tvCancel1.setVisibility(View.GONE);
            } else {
                tvCancel.setVisibility(View.VISIBLE);
                tvCancel1.setVisibility(View.VISIBLE);
            }
        }
    }

    private void actionBack() {
        try {
            unregisterReceiver(receiver);
        } catch (Exception e) {

        }
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        actionBack();
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
        if (!preferences.getString(PreferenceKey.LOCATE, "en").equals(locate)) {
            locate = preferences.getString(PreferenceKey.LOCATE, "en");
            setLanguage();
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_buy_data:
                Intent iSub = new Intent(mContext, ActivityBuyPackage.class);
                startActivity(iSub);
                overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_left);
                break;
            case R.id.btn_buy_data1:
                Intent iSub1 = new Intent(mContext, ActivityBuyPackage.class);
                startActivity(iSub1);
                overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_left);
                break;
            case R.id.btn_data_detail:
                Intent iDetail = new Intent(mContext, ActivityOther3G.class);
                startActivity(iDetail);
                overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_left);
                break;
            case R.id.ivBack:
                actionBack();
                break;
            case R.id.tv_cancel:
                final DialogConfirm cfDialog = new DialogConfirm(mContext,
                        "Do you want to cancel " + ActivityMain.myInternet.name + "?");
                cfDialog.btnYes.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        cfDialog.dismiss();
                        if (ActivityMain.myInternet.type.equals("2") ||
                                ActivityMain.myInternet.type.equals("4")) {
                            new AsyncTaskDeactive().execute(WSCode.REMOVE_4G);
                        } else {
                            new AsyncTaskDeactive().execute(WSCode.REMOVE_MI_NEW);
                        }
                    }
                });
                cfDialog.show();
                break;
            case R.id.tv_cancel1:
                final DialogConfirm cfDialog1 = new DialogConfirm(mContext,
                        "Do you want to cancel " + ActivityMain.myInternet.name + "?");
                cfDialog1.btnYes.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        cfDialog1.dismiss();
                        if (ActivityMain.myInternet.type.equals("2") ||
                                ActivityMain.myInternet.type.equals("4")) {
                            new AsyncTaskDeactive().execute(WSCode.REMOVE_4G);
                        } else if (ActivityMain.myInternet.type.equals("5") ||
                                ActivityMain.myInternet.type.equals("6")) {
                            new AsyncTaskDeactive().execute(WSCode.REMOVE_MI_NEW);
                        } else {
                            new AsyncTaskDeactive().execute(WSCode.REMOVE_MI);
                        }
                    }
                });
                cfDialog1.show();
                break;
            default:
                break;
        }
    }

    private class AsyncTaskCheck3G extends AsyncTask<String, String, Boolean> {
        String message = "";
        int dataErrCode;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... p) {
            // TODO Auto-generated method stub
            try {
                List<ModelTag> tags = new ArrayList<ModelTag>();
                tags.add(new ModelTag(SoapTag.USERNAME, USERNAME));
                tags.add(new ModelTag(SoapTag.PASSWORD, PASSWORD));
                tags.add(new ModelTag(SoapTag.RAWDATA, "?"));
                List<ModelParam> params = new ArrayList<ModelParam>();
                params.add(new ModelParam("msisdn", NUMBER_HEADER
                        + preferences.getString(PreferenceKey.PHONE_NUMBER, "")));
                params.add(new ModelParam("command", "CHECK"));
                params.add(new ModelParam("send_sms", "0"));
                params.add(new ModelParam("requestId", FunctionHelper
                        .formatCurrentTime()));
                SOAPUtil soap = new SOAPUtil(WSCode.CHECK_3G, tags, params);
                soap.makeSOAPRequest();
                if (soap.getError() == 0) {
                    if (soap.getValue(mXML.ERR_CODE).equals("0")) {
                        message = soap.getValue(mXML.DESCRIPTION);
                        dataErrCode = Integer.parseInt(soap
                                .getValue(mXML.ERR_CODE));
                        return true;
                    } else {
                        message = "3G is not available";
                        return false;
                    }

                } else {
                    message = "3G is not available";
                    return false;
                }
            } catch (Exception e) {
                message = getString(R.string.err_connect);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            pDialog.dismiss();
            getData = true;
            data3g = message;
            packCode = FunctionHelper.splitDataCode(message);
            Log.v(TAG, "tiench data pack: " + packCode);
            Intent intent = new Intent();
            intent.setAction(Action.FILTER);
            intent.putExtra(Action.ACTION, Action.DONE_RELOAD_DATA);
            sendBroadcast(intent);
        }
    }

    private class AsyncTaskDeactive extends AsyncTask<String, String, Boolean> {
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
                message = getString(R.string.err_connect);
                return false;
            }

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pDialog.setMessage(getString(R.string.message_loading));
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (result) {
                Intent intent = new Intent();
                intent.setAction(Action.FILTER);
                intent.putExtra(Action.ACTION, Action.RELOAD_DATA);
                sendBroadcast(intent);
            } else {
                pDialog.dismiss();
            }
            new DialogReport(mContext, message).show();
        }

    }

    private class BasicDataReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            Bundle mBundle = intent.getExtras();
            if (mBundle.getString(Action.ACTION)
                    .equals(Action.DONE_RELOAD_DATA)) {
                setBasicAccount();
                pDialog.dismiss();
            } else if (mBundle.getString(Action.ACTION).equals(
                    Action.RELOAD_DATA)) {
                setBasicAccount();
                pDialog.dismiss();
            }
        }

    }

}
