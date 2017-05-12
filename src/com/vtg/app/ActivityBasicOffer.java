package com.vtg.app;

import java.util.Locale;

import android.content.Context;
import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vtg.app.component.AdapterBanner;
import com.vtg.app.util.CommonDefine;
import com.vtg.unitel.R;

public class ActivityBasicOffer extends FragmentActivity implements
		CommonDefine, OnClickListener {

	private static final String TAG = "ActivityBasicOffer";

	Configuration config;

	private SharedPreferences preferences;
	private Context mContext;

	private String locate = "";

	// Basic account
	private TextView tvBasicValue;
	private RelativeLayout btnOfferInfo;

	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		mContext = this;

		preferences = getSharedPreferences(MY_PACKAGE, Context.MODE_PRIVATE);

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
		}catch (Exception e){

		}
	}

	private void activityCreate() {
		setContentView(R.layout.activity_basic_offer);
		initView();
		initBanner();
	}

	private void initView() {
		ImageView ivBack = (ImageView) findViewById(R.id.ivBack);
		ivBack.setOnClickListener(this);
		// Basic account
		tvBasicValue = (TextView) findViewById(R.id.tv_basic_value);
		btnOfferInfo = (RelativeLayout) findViewById(R.id.btn_offer_info);
		btnOfferInfo.setOnClickListener(this);
	}

	private void actionBack() {
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
		case R.id.btn_offer_info:
			Intent iDetail = new Intent(mContext, ActivityMoreOffer.class);
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

}
