package com.vtg.app;

import java.util.Locale;

import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.vtg.app.ActivityMain.mProfile;
import com.vtg.app.component.DialogConfirm;
import com.vtg.app.util.CommonDefine;
import com.vtg.app.util.CommonDefine.PreferenceKey;
import com.vtg.app.util.CommonDefine.TabIndex;
import com.vtg.unitel.R;

public class ActivityHome extends TabActivity implements CommonDefine {

    private TabHost tabHost;
    private SharedPreferences preferences;
    private String locate = "";
    private MyReceiver receiver;

    private ImageView ivLogo;
    private ImageView ivNoti;
    private RelativeLayout layoutBack;

    private RelativeLayout tabProfile;
    private RelativeLayout tabInternet;
    private RelativeLayout tabOffer;
    private RelativeLayout tabService;
    private RelativeLayout tabMore;

    private Context mContext;

    private SwipeRefreshLayout swipeContainer;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        preferences = getSharedPreferences(MY_PACKAGE, Context.MODE_PRIVATE);
        mContext = this;
        receiver = new MyReceiver();
        registerReceiver(receiver, new IntentFilter(Action.FILTER));
        setLanguage();
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
//        if (!preferences.getString(PreferenceKey.LOCATE, "en").equals(locate)) {
//            locate = preferences.getString(PreferenceKey.LOCATE, "en");
//            setLanguage();
//        }
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

    private void activityCreate() {
        Log.v("", "tiench create activity home");
        setContentView(R.layout.activity_home);
        initTab();
        initView();
    }

    private void initView() {
        ivLogo = (ImageView) findViewById(R.id.iv_logo);
        ivLogo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                changeTab(TabIndex.HOME);
            }
        });

        ivNoti = (ImageView) findViewById(R.id.iv_noti);
        ivNoti.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

        tabProfile = (RelativeLayout) findViewById(R.id.tab_profile);
        tabProfile.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mProfile.accountType == DEBIT) {
                    changeTab(TabIndex.DEBIT);
                } else if (mProfile.accountType == CREDIT) {
                    changeTab(TabIndex.CREDIT);
                }

            }
        });
        tabInternet = (RelativeLayout) findViewById(R.id.tab_internet);
        tabInternet.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                changeTab(TabIndex.DATA);
            }
        });
        tabOffer = (RelativeLayout) findViewById(R.id.tab_offer);
        tabOffer.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                changeTab(TabIndex.OFFER);
            }
        });
        tabService = (RelativeLayout) findViewById(R.id.tab_services);
        tabService.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                changeTab(TabIndex.SERVICE);
            }
        });
        tabMore = (RelativeLayout) findViewById(R.id.tab_more);
        tabMore.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                changeTab(TabIndex.MORE);
            }
        });

        //layout back
        layoutBack = (RelativeLayout) findViewById(R.id.layout_back);
        layoutBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTab(TabIndex.HOME);
            }
        });

        // refresh view
//        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
//        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                Intent intent = new Intent();
//                intent.setAction(Action.FILTER);
//                intent.putExtra(Action.ACTION, Action.RELOAD_DATA);
//                sendBroadcast(intent);
//            }
//        });

    }

    private void changeTab(int index) {
        if (index == TabIndex.HOME) {
            ivLogo.setVisibility(View.VISIBLE);
            layoutBack.setVisibility(View.GONE);
        } else {
            ivLogo.setVisibility(View.GONE);
            layoutBack.setVisibility(View.VISIBLE);
        }
        if (tabHost.getCurrentTab() != index) {
            tabHost.setCurrentTab(index);
        }
    }

    private void initTab() {
        tabHost = getTabHost();
        // Tab for main
        TabSpec mainSpec = tabHost.newTabSpec("Home");
        mainSpec.setIndicator("Home",
                getResources().getDrawable(R.drawable.unitel));
        Intent mainIntent = new Intent(this, ActivityMain.class);
        mainSpec.setContent(mainIntent);
        // Tab for Account Debit
        TabSpec profileSpec = tabHost.newTabSpec("Profile");
        profileSpec.setIndicator("Profile",
                getResources().getDrawable(R.drawable.menu_profile));
        Intent profileIntent = new Intent(this, ActivityBasicAccount.class);
        profileSpec.setContent(profileIntent);
        // Tab for Mobile Internet
        TabSpec dataSpec = tabHost.newTabSpec("Mobile Internet");
        dataSpec.setIndicator("Profile",
                getResources().getDrawable(R.drawable.menu_mobile_internet));
        Intent dataIntent = new Intent(this, ActivityBasic3G.class);
        dataSpec.setContent(dataIntent);
        // Tab for Offer
        TabSpec offerSpec = tabHost.newTabSpec("Offer");
        offerSpec.setIndicator("Profile",
                getResources().getDrawable(R.drawable.menu_mobile_internet));
        Intent offerIntent = new Intent(this, ActivityMoreOffer.class);
        offerSpec.setContent(offerIntent);
        // Tab for Service
        TabSpec serviceSpec = tabHost.newTabSpec("Service");
        serviceSpec.setIndicator("Profile",
                getResources().getDrawable(R.drawable.menu_mobile_internet));
        Intent serviceIntent = new Intent(this, ActivityVas.class);
        serviceSpec.setContent(serviceIntent);

        TabSpec creditSpec = tabHost.newTabSpec("Profile");
        creditSpec.setIndicator("Profile",
                getResources().getDrawable(R.drawable.menu_profile));
        Intent creditIntent = new Intent(this, ActivityBasicDebit.class);
        creditSpec.setContent(creditIntent);

        TabSpec moreSpec = tabHost.newTabSpec("Profile");
        moreSpec.setIndicator("Profile",
                getResources().getDrawable(R.drawable.menu_profile));
        Intent moreIntent = new Intent(this, ActivityMore.class);
        moreSpec.setContent(moreIntent);

        tabHost.addTab(mainSpec);
        tabHost.addTab(profileSpec);
        tabHost.addTab(dataSpec);
        tabHost.addTab(offerSpec);
        tabHost.addTab(serviceSpec);
        tabHost.addTab(creditSpec);
        tabHost.addTab(moreSpec);

        tabHost.setCurrentTab(0);
    }

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getExtras().get(Action.ACTION).equals(Action.CHANGE_TAB)) {
                changeTab(intent.getExtras().getInt("index"));
            } else if (intent.getExtras().get(Action.ACTION)
                    .equals(Action.LOGOUT)) {
                actionLogout();
            }
        }

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

    private void actionLogout() {
        DialogConfirm cfLogout = new DialogConfirm(mContext,
                getString(R.string.confirm_logout));
        cfLogout.btnYes.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                preferences.edit().putString(PreferenceKey.PHONE_NUMBER, "")
                        .commit();
                Intent intent = new Intent(mContext, ActivityFirst.class);
                startActivity(intent);
                finish();
            }
        });
        cfLogout.show();
    }

}
