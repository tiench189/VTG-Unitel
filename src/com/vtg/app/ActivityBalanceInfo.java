package com.vtg.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.w3c.dom.NodeList;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
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

import com.vtg.app.component.AdapterBalanceInfo;
import com.vtg.app.component.AdapterBanner;
import com.vtg.app.model.ModelBalance;
import com.vtg.app.model.ModelParam;
import com.vtg.app.model.ModelTag;
import com.vtg.app.util.CommonDefine;
import com.vtg.app.util.FunctionHelper;
import com.vtg.app.util.SOAPUtil;
import com.vtg.app.util.XMLParser;
import com.vtg.unitel.R;

public class ActivityBalanceInfo extends FragmentActivity implements
        CommonDefine {
    private SharedPreferences preferences;
    private Context mContext;

    private AdapterBalanceInfo adapterBalance;
    private List<ModelBalance> listBalances;
    private ImageView ivBack;
    private ListView lvBalance;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate(arg0);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        mContext = this;

        preferences = getSharedPreferences(MY_PACKAGE, Context.MODE_PRIVATE);
        listBalances = new ArrayList<ModelBalance>();
    }

    private void activityCreate() {
        setContentView(R.layout.activity_balance_info);
        initView();
        initBanner();
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

    public void initView() {
        ivBack = (ImageView) findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                actionBack();
            }
        });

        lvBalance = (ListView) findViewById(R.id.lv_balance);

        new AsyncTaskBalanceInfo()
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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

    private class AsyncTaskBalanceInfo extends
            AsyncTask<String, String, String> {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            listBalances = new ArrayList<ModelBalance>();
            pDialog = new ProgressDialog(mContext);
            pDialog.setCancelable(false);
            pDialog.setMessage(mContext.getString(R.string.message_loading));
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... p) {
            // TODO Auto-generated method stub
            try {
                List<ModelTag> tags = new ArrayList<ModelTag>();
                tags.add(new ModelTag(SoapTag.USERNAME, USERNAME));
                tags.add(new ModelTag(SoapTag.PASSWORD, PASSWORD));
                tags.add(new ModelTag(SoapTag.RAWDATA, "?"));
                List<ModelParam> params = new ArrayList<ModelParam>();
                params.add(new ModelParam("msisdn", NUMBER_HEADER
                        + preferences.getString(PreferenceKey.PHONE_NUMBER, "")));
//			params.add(new ModelParam("listId", "2001,2004,4200,4046"));
                params.add(new ModelParam("listId", "10,14,13,18,26"));
                SOAPUtil soap = new SOAPUtil(WSCode.GET_SUB_ACC, tags, params);
                soap.makeSOAPRequest();
                NodeList listId = soap.mDoc.getElementsByTagName("balanceId");
                NodeList listExpire = soap.mDoc
                        .getElementsByTagName("balanceExpire");
                NodeList listName = soap.mDoc.getElementsByTagName("balanceName");
                NodeList listValue = soap.mDoc.getElementsByTagName("balanceValue");
                for (int i = 0; i < 10; i++) {
                    if (!XMLParser.getElementValue(listName.item(i)).trim()
                            .equals("")) {
                        ModelBalance balance = new ModelBalance(
                                XMLParser.getElementValue(listExpire.item(i)),
                                XMLParser.getElementValue(listId.item(i)),
                                XMLParser.getElementValue(listName.item(i)),
                                FunctionHelper.formatMoney(XMLParser
                                        .getElementValue(listValue.item(i))));
                        listBalances.add(balance);
                    }
                }
            } catch (Exception e) {
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            pDialog.dismiss();
            adapterBalance = new AdapterBalanceInfo(mContext, listBalances);
            lvBalance.setAdapter(adapterBalance);
        }

    }
}
