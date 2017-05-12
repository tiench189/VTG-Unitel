package com.vtg.app;

import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;

import com.vtg.app.util.CommonDefine;
import com.vtg.unitel.R;

public class ActivityMore extends Activity implements CommonDefine {
	private SharedPreferences preferences;
	private String locate = "";
	private Context mContext;

	private RelativeLayout boxAbout, boxProfile, boxSetting, boxShowrrom,
			boxLogout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		preferences = getSharedPreferences(MY_PACKAGE, Context.MODE_PRIVATE);
		mContext = this;
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
		setContentView(R.layout.activity_more);
		initView();
	}

	private void initView() {
		boxAbout = (RelativeLayout) findViewById(R.id.box_about);
		boxAbout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent iAbout = new Intent(mContext, ActivityAbout.class);
				startActivity(iAbout);
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_left);
			}
		});
		boxProfile = (RelativeLayout) findViewById(R.id.box_profile);
		boxProfile.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent iBalance = new Intent(mContext, ActivityProfiles.class);
				startActivity(iBalance);
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_left);
			}
		});
		boxShowrrom = (RelativeLayout) findViewById(R.id.box_showroom);
		boxShowrrom.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent iShowroom = new Intent(mContext, ActivityShowroom.class);
				startActivity(iShowroom);
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_left);
			}
		});
		boxSetting = (RelativeLayout) findViewById(R.id.box_setting);
		boxSetting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent iSetting = new Intent(mContext, ActivitySetting.class);
				startActivity(iSetting);
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_left);
			}
		});
		boxLogout = (RelativeLayout) findViewById(R.id.box_logout);
		boxLogout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setAction(Action.FILTER);
				intent.putExtra(Action.ACTION, Action.LOGOUT);
				sendBroadcast(intent);
			}
		});
	}

}
