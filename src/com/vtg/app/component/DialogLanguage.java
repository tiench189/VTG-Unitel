package com.vtg.app.component;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.vtg.app.ActivityFirst;
import com.vtg.app.util.CommonDefine;
import com.vtg.unitel.R;

public class DialogLanguage extends Dialog implements CommonDefine {
	private Context mContext;
	private SharedPreferences preferences;
	private RadioGroup rg;
	private RelativeLayout btnStart, btnExit;

	public DialogLanguage(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_language);
		this.mContext = context;
		preferences = context.getSharedPreferences(MY_PACKAGE,
				Context.MODE_PRIVATE);
		setCancelable(false);

		rg = (RadioGroup) findViewById(R.id.rg);
		btnStart = (RelativeLayout) findViewById(R.id.btn_start);
		btnStart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				int pos = rg.getCheckedRadioButtonId();
				switch (pos) {
				case R.id.radio0:
					preferences.edit().putString(PreferenceKey.LOCATE, "en")
							.commit();
					break;
				case R.id.radio1:
					preferences.edit().putString(PreferenceKey.LOCATE, "vi")
							.commit();
					break;
				case R.id.radio2:
					preferences.edit().putString(PreferenceKey.LOCATE, "lo")
							.commit();
					break;

				default:
					break;
				}
				preferences.edit().putBoolean(PreferenceKey.FIRST_USE, false)
						.commit();
				Intent intent = new Intent(mContext, ActivityFirst.class);
				((Activity) mContext).startActivity(intent);
				((Activity) mContext).finish();
			}
		});
		
		btnExit = (RelativeLayout) findViewById(R.id.btn_exit);
		btnExit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((Activity) mContext).finish();
			}
		});
	}

}
