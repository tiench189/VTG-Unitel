package com.vtg.app;

import java.util.ArrayList;
import java.util.List;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.vtg.app.component.AdapterBanner;
import com.vtg.app.component.AdapterMap;
import com.vtg.app.model.ModelMap;
import com.vtg.app.util.CommonDefine;
import com.vtg.app.util.FunctionHelper;
import com.vtg.unitel.R;

public class ActivityProfiles extends FragmentActivity implements CommonDefine {
    private SharedPreferences preferences;
    private Context mContext;

    private ImageView ivBack;
    private ListView lvProfile;
    private List<ModelMap> listMap;

    private MyReveiver receiver;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate(arg0);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        mContext = this;

        preferences = getSharedPreferences(MY_PACKAGE, Context.MODE_PRIVATE);
        receiver = new MyReveiver();
        registerReceiver(receiver, new IntentFilter(Action.FILTER));
    }

    private void activityCreate() {
        setContentView(R.layout.activity_profile);
        initView();
        initBanner();
    }

    // Banner
    private ViewPager pagerBanner;
    private AdapterBanner adapterBanner;

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

    public void initView() {
        ivBack = (ImageView) findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                actionBack();
            }
        });
        lvProfile = (ListView) findViewById(R.id.lv_profile);
        initProfile();

    }

    private void initProfile() {
        // initProfile
        listMap = new ArrayList<ModelMap>();
        listMap.add(new ModelMap(getString(R.string.number), preferences
                .getString(PreferenceKey.PHONE_NUMBER, "")));
        listMap.add(new ModelMap(getString(R.string.account_name),
                ActivityMain.mProfile.userUsing));
        if (ActivityMain.mProfile.accountType == DEBIT) {
            listMap.add(new ModelMap(getString(R.string.account_type),
                    getString(R.string.prepaid)));
        } else {
            listMap.add(new ModelMap(getString(R.string.account_type),
                    getString(R.string.postpaid)));
        }
        listMap.add(new ModelMap(getString(R.string.start_datetime),
                ActivityMain.mProfile.startTime));
        listMap.add(new ModelMap(getString(R.string.address),
                ActivityMain.mProfile.address));
        listMap.add(new ModelMap(getString(R.string.birthdate),
                ActivityMain.mProfile.birthDay));
        lvProfile.setAdapter(new AdapterMap(mContext, listMap));
    }

    private void actionBack() {
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
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

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        actionBack();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        try {
            unregisterReceiver(receiver);
        } catch (Exception e) {

        }
    }

    private class MyReveiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if (intent.getExtras().getString(Action.ACTION)
                    .equals(Action.LOAD_PROFILE)) {
                initProfile();
            }
        }

    }
}
