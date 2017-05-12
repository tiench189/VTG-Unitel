package com.vtg.app;

import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.vtg.app.util.CommonDefine;
import com.vtg.unitel.R;

public class ActivitySetting extends Activity implements CommonDefine {

	private SharedPreferences preferences;
	private Context mContext;

	private RelativeLayout layoutEN, layoutVI, layoutLO;
	private ImageView ivEN, ivVI, ivLO;

	private ImageView ivBack;

	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		mContext = this;

		preferences = getSharedPreferences(MY_PACKAGE, Context.MODE_PRIVATE);
	}

	private void activityCreate() {
		setContentView(R.layout.activity_setting);
		initView();
	}

	public void initView() {
		ivBack = (ImageView) findViewById(R.id.ivBack);
		ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				actionBack();
			}
		});
		layoutEN = (RelativeLayout) findViewById(R.id.layout_en);
		layoutVI = (RelativeLayout) findViewById(R.id.layout_vi);
		layoutLO = (RelativeLayout) findViewById(R.id.layout_lo);

		ivEN = (ImageView) findViewById(R.id.iv_en);
		ivVI = (ImageView) findViewById(R.id.iv_vi);
		ivLO = (ImageView) findViewById(R.id.iv_lo);

		if (preferences.getString(PreferenceKey.LOCATE, "en").equals("en")) {
			ivEN.setVisibility(View.VISIBLE);
			ivVI.setVisibility(View.INVISIBLE);
			ivLO.setVisibility(View.INVISIBLE);
		} else if (preferences.getString(PreferenceKey.LOCATE, "en").equals(
				"vi")) {
			ivEN.setVisibility(View.INVISIBLE);
			ivVI.setVisibility(View.VISIBLE);
			ivLO.setVisibility(View.INVISIBLE);
		} else {
			ivEN.setVisibility(View.INVISIBLE);
			ivVI.setVisibility(View.INVISIBLE);
			ivLO.setVisibility(View.VISIBLE);
		}

		layoutEN.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!preferences.getString(PreferenceKey.LOCATE, "en").equals(
						"en")) {
					preferences.edit().putString(PreferenceKey.LOCATE, "en")
							.commit();
					ivEN.setVisibility(View.VISIBLE);
					ivVI.setVisibility(View.INVISIBLE);
					ivLO.setVisibility(View.INVISIBLE);
					setLanguage();
				}
			}
		});

		layoutVI.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!preferences.getString(PreferenceKey.LOCATE, "en").equals(
						"vi")) {
					preferences.edit().putString(PreferenceKey.LOCATE, "vi")
							.commit();
					ivEN.setVisibility(View.INVISIBLE);
					ivVI.setVisibility(View.VISIBLE);
					ivLO.setVisibility(View.INVISIBLE);
					setLanguage();
				}
			}
		});
		
		layoutLO.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!preferences.getString(PreferenceKey.LOCATE, "en").equals(
						"lo")) {
					preferences.edit().putString(PreferenceKey.LOCATE, "lo")
							.commit();
					ivEN.setVisibility(View.INVISIBLE);
					ivVI.setVisibility(View.INVISIBLE);
					ivLO.setVisibility(View.VISIBLE);
					setLanguage();
				}
			}
		});
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

}
