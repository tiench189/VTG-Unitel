package com.vtg.app;

import java.util.Locale;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vtg.app.component.AdapterBalanceInfo;
import com.vtg.app.component.AdapterBanner;
import com.vtg.app.component.DialogConfirm;
import com.vtg.app.component.DialogUpPay;
import com.vtg.app.util.CommonDefine;
import com.vtg.app.util.FunctionHelper;
import com.vtg.unitel.R;

public class ActivityBasicAccount extends FragmentActivity implements
        CommonDefine, OnClickListener {

    private static final String TAG = "ActivityBasicAccount";

    private ImageView ivBack;
    Configuration config;

    private SharedPreferences preferences;
    private Context mContext;

    private String locate = "";

    // Basic account
    private TextView tvBasicValue;
    private RelativeLayout btnBasicUpPay, btnBasicDetail;
    private RelativeLayout layoutBasicAccount;

    private BasicAccountReceiver receiver;

    private ListView lvBalance;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate(arg0);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        mContext = this;

        preferences = getSharedPreferences(MY_PACKAGE, Context.MODE_PRIVATE);

        receiver = new BasicAccountReceiver();
        registerReceiver(receiver, new IntentFilter(Action.FILTER));

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
                    try {
                        int current = pagerBanner.getCurrentItem();
                        if (current < 3) {
                            current++;
                        } else {
                            current = 0;
                        }
                        pagerBanner.setCurrentItem(current);
                        changeBanner();
                    } catch (Exception e) {

                    }
                }
            }, 10000);
        } catch (Exception e) {

        }
    }

    private void activityCreate() {
        setContentView(R.layout.activity_basic_account);
        initView();
        initBanner();
    }

    private void initView() {
        ImageView ivBack = (ImageView) findViewById(R.id.ivBack);
        ivBack.setOnClickListener(this);
        // Basic account
        tvBasicValue = (TextView) findViewById(R.id.tv_basic_value);
        btnBasicUpPay = (RelativeLayout) findViewById(R.id.btn_basic_up_pay);
        btnBasicUpPay.setOnClickListener(this);
        btnBasicDetail = (RelativeLayout) findViewById(R.id.btn_basic_detail);
        btnBasicDetail.setOnClickListener(this);
        layoutBasicAccount = (RelativeLayout) findViewById(R.id.layout_basic_account);
        lvBalance = (ListView) findViewById(R.id.lv_balance);
        setBasicAccount();
    }

    private void setBasicAccount() {
        tvBasicValue.setText(ActivityMain.basicAcc.value + " KIP");
        lvBalance.setAdapter(new AdapterBalanceInfo(mContext,
                ActivityMain.listBalances));
        FunctionHelper.setListViewHeightBasedOnChildren(lvBalance);
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
            case R.id.btn_basic_up_pay:
                showConfirmUpPay();
                break;
            case R.id.btn_basic_detail:
                Intent iDetail = new Intent(mContext, ActivityBalanceInfo.class);
                startActivity(iDetail);
                overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_left);
                break;
            case R.id.ivBack:
                actionBack();
                break;
            default:
                break;
        }
    }

    private void showConfirmUpPay() {
        final DialogConfirm cfUpPay = new DialogConfirm(mContext,
                getString(R.string.confirm_up_pay));
        cfUpPay.tvYes.setText(getString(R.string.you));
        cfUpPay.tvNo.setText(getString(R.string.anyone));
        cfUpPay.btnYes.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                cfUpPay.dismiss();
                showDialogUpPay(true);
            }
        });
        cfUpPay.btnNo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                cfUpPay.dismiss();
                showDialogUpPay(false);
            }
        });
        cfUpPay.show();
    }

    private void showDialogUpPay(boolean isMe) {
        DialogUpPay dlUpPay = new DialogUpPay(mContext,
                ActivityMain.mProfile.accountType == DEBIT);
        if (isMe) {
            dlUpPay.edtNumber.setText(preferences.getString(
                    PreferenceKey.PHONE_NUMBER, ""));
            dlUpPay.edtNumber.setVisibility(View.GONE);
        }
        dlUpPay.show();
    }

    private class BasicAccountReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            Bundle mBundle = intent.getExtras();
            if (mBundle.getString(Action.ACTION).equals(
                    Action.DONE_RELOAD_ACCOUNT)) {
                setBasicAccount();
            }
        }

    }

}
