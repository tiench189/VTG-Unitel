package com.vtg.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.BuildConfig;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.vtg.app.component.DialogConfirm;
import com.vtg.app.component.DialogReport;
import com.vtg.app.util.CommonDefine;
import com.vtg.app.util.FunctionHelper;
import com.vtg.app.util.JSONParser;
import com.vtg.unitel.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class ActivitySplash extends Activity implements CommonDefine {

    private SharedPreferences preferences;
    private Context mContext;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        setContentView(R.layout.activity_splash);
        mContext = this;

        preferences = getSharedPreferences(MY_PACKAGE, Context.MODE_PRIVATE);

        pDialog = new ProgressDialog(mContext);
        pDialog.setCancelable(false);
        new AsyncTaskVersion().execute();
    }

    private class AsyncTaskVersion extends AsyncTask<String, String, Boolean> {
        String newVer = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String currentVersion = FunctionHelper.getVersionName(mContext);
            try {
                JSONParser jsonParser = new JSONParser();
                String strJson = jsonParser.getJSONFromUrl(MyService.VERSION,
                        CommonDefine.METHOD_GET, null);
                JSONObject jsonObj = new JSONObject(strJson);
                newVer = jsonObj.getString("data").trim();
                Log.v("tiench", "tiench version: " + currentVersion + "/" + newVer + "/" + currentVersion.compareTo(newVer));
                return currentVersion.compareTo(newVer) >= 0;
            } catch (Exception e) {
                return true;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            pDialog.dismiss();
            if (aBoolean) {
                // if (preferences.getBooledan(PreferenceKey.FIRST_USE, true)) {
                // new DialogLanguage(mContext).show();
                // } else {
                //Trả sau: 2091999268
//		preferences.edit().putString(PreferenceKey.PHONE_NUMBER, "2095577779")
//				.commit();
                if (preferences.getString(PreferenceKey.PHONE_NUMBER, "").equals("")
                        || !preferences.getBoolean(PreferenceKey.SAVE_LOGIN, true)) {
                    Intent intent = new Intent(mContext, ActivityFirst.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(mContext, ActivityHome.class);
                    startActivity(intent);
                    finish();
                }
                // }
            } else {
                DialogConfirm dlUpdate = new DialogConfirm(mContext, "New version available. Do you want to update?");
                dlUpdate.btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                        finish();
                    }
                });
                dlUpdate.btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (preferences.getString(PreferenceKey.PHONE_NUMBER, "").equals("")
                                || !preferences.getBoolean(PreferenceKey.SAVE_LOGIN, true)) {
                            Intent intent = new Intent(mContext, ActivityFirst.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(mContext, ActivityHome.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
                dlUpdate.show();

            }
        }
    }
}
