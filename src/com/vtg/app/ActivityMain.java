package com.vtg.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.NodeList;

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
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.triggertrap.seekarc.SeekArc;
import com.vtg.app.component.AdapterBanner;
import com.vtg.app.component.DialogReport;
import com.vtg.app.model.ModelBalance;
import com.vtg.app.model.ModelBanner;
import com.vtg.app.model.ModelDebit;
import com.vtg.app.model.ModelInternet;
import com.vtg.app.model.ModelMoreData;
import com.vtg.app.model.ModelParam;
import com.vtg.app.model.ModelTag;
import com.vtg.app.util.CommonDefine;
import com.vtg.app.util.FunctionHelper;
import com.vtg.app.util.JSONParser;
import com.vtg.app.util.NetworkUtil;
import com.vtg.app.util.SOAPUtil;
import com.vtg.app.util.XMLParser;
import com.vtg.unitel.R;

public class ActivityMain extends FragmentActivity implements CommonDefine,
        OnClickListener {

    private static final String TAG = "ActivityMain";

    // profile
    public static class mProfile {
        public static int accountType = 0;
        public static String userUsing = "";
        public static String startTime = "";
        public static String address = "";
        public static String birthDay = "";
    }

    // End profile

    private ImageView ivMenu;
    private SlidingMenu menu;
    Configuration config;

    private SharedPreferences preferences;
    private Context mContext;

    private String locate = "";

    private RelativeLayout layoutSplash;
    private boolean showSplash = true;
    private boolean getBalance = false;
    private boolean get3gData = false;
    private boolean getProfile = false;

    // Banner
//    private ViewPager pagerBanner;
//    private AdapterBanner adapterBanner;
    // End Banner

    private TextView tvUsername, tvIsdn, tvAccType;
    private RelativeLayout bannerProfile;

    public static ModelBalance basicAcc;
    public static ModelDebit debitAcc;
    public static ModelInternet myInternet;
    public static List<ModelBalance> listBalances;
    // Basic account
    private TextView tvBalanceStatus;
    private RelativeLayout layoutBalance;
    private ProgressBar pbBalance;
    private ImageView ivBasicBalance;
    private TextView labelBalance;
    // 3G Data
    private TextView tvDataStatus;
    private RelativeLayout layoutData;
    private ProgressBar pbData;
    private RelativeLayout layoutInternetAmount;
    private TextView tvInternetAmount;
    private SeekArc seekAmount;
    // Offer
    private RelativeLayout layoutOffer;
    private TextView tvOffer;
    private ProgressBar pbOffer;
    // 4g
    private RelativeLayout layout4G;
    // VAS
    private RelativeLayout layoutVas;
    // transfer
    private RelativeLayout layoutTransfer;

    public static String data3g = "";
    public static int dataErrCode = -1;

    private MainReceiver receiver;

    public static List<ModelBanner> listBanner;
    private boolean loadBanner = false;

    private RelativeLayout boxInternet;
    private RelativeLayout boxBalance;
    private RelativeLayout boxOffer;
    private RelativeLayout boxService;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate(arg0);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        mContext = this;

        preferences = getSharedPreferences(MY_PACKAGE, Context.MODE_PRIVATE);

        receiver = new MainReceiver();
        registerReceiver(receiver, new IntentFilter(Action.FILTER));
        Log.v(TAG, "tiench onCreated");
        if (NetworkUtil.availableConnect(mContext)) {
            new AsyntaskBanner().execute();
        } else {
            DialogReport noConnect = new DialogReport(mContext,
                    "Internet not found!");
            noConnect.btnOK.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    finish();
                }
            });
            noConnect.show();
        }
    }

    private class AsyntaskBanner extends AsyncTask<String, String, Boolean> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            listBanner = new ArrayList<ModelBanner>();
        }

        @Override
        protected Boolean doInBackground(String... arg0) {
            JSONParser jsonParser = new JSONParser();
            String strJson = jsonParser.getJSONFromUrl(MyService.BANNER,
                    CommonDefine.METHOD_GET, null);
            try {
                JSONObject jsonObj = new JSONObject(strJson);
                JSONArray jsonArr = jsonObj.getJSONArray("data");
                for (int i = 0; i < jsonArr.length(); i++) {
                    ModelBanner banner = new ModelBanner();
                    banner.id = jsonArr.getJSONObject(i).getString("id");
                    banner.image = jsonArr.getJSONObject(i).getString("image");
                    banner.action = jsonArr.getJSONObject(i)
                            .getString("action");
                    banner.status = jsonArr.getJSONObject(i)
                            .getString("status");
                    listBanner.add(banner);
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                loadBanner = true;
//                initBanner();
            }
            myInternet = new ModelInternet();
            new AsyncTaskSubInfo().execute();
        }

    }

    @SuppressWarnings("deprecation")
//    private void initBanner() {
//        pagerBanner = (ViewPager) findViewById(R.id.pager_banner);
//        adapterBanner = new AdapterBanner(getSupportFragmentManager());
//        pagerBanner.setAdapter(adapterBanner);
//
//        changeBanner();
//    }
//
//    private void changeBanner() {
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                int current = pagerBanner.getCurrentItem();
//                if (current < 3) {
//                    current++;
//                } else {
//                    current = 0;
//                }
//                pagerBanner.setCurrentItem(current);
//                changeBanner();
//            }
//        }, 10000);
//    }

    private void activityCreate() {
        setContentView(R.layout.activity_main);
        initView();
        initMenu();
    }

    private void initMenu() {
        ivMenu = (ImageView) findViewById(R.id.ivMenu);
        ivMenu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (menu.isMenuShowing()) {
                    menu.showContent();
                } else {
                    menu.showMenu();
                }
            }
        });

        menu = new SlidingMenu(this);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.menu_frame);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.menu_frame, new FragmentMenu()).commit();
    }

    private void initView() {
        tvUsername = (TextView) findViewById(R.id.tv_username);
        tvIsdn = (TextView) findViewById(R.id.tv_isdn);
        tvAccType = (TextView) findViewById(R.id.tv_acc_type);
        if (getProfile) {
            setProfile();
        }
        bannerProfile = (RelativeLayout) findViewById(R.id.banner_profile);
        bannerProfile.setOnClickListener(this);

        layoutSplash = (RelativeLayout) findViewById(R.id.layout_splash);
        if (showSplash) {
            layoutSplash.setVisibility(View.VISIBLE);
            showSplash = false;
        }
        // offer
        layoutOffer = (RelativeLayout) findViewById(R.id.layout_offer);
        layoutOffer.setOnClickListener(this);
        tvOffer = (TextView) findViewById(R.id.tvOfferStatus);
        pbOffer = (ProgressBar) findViewById(R.id.pb_offer);
        // Basic account
        layoutBalance = (RelativeLayout) findViewById(R.id.layout_balance);
        layoutBalance.setOnClickListener(this);
        tvBalanceStatus = (TextView) findViewById(R.id.tv_basic_balance);
        ivBasicBalance = (ImageView) findViewById(R.id.iv_box_balance);
        labelBalance = (TextView) findViewById(R.id.label_balance);
        if (getBalance) {
            setBasicAccount();
        }
        pbBalance = (ProgressBar) findViewById(R.id.pb_balance);
        // 3g data
        layoutData = (RelativeLayout) findViewById(R.id.layout_data);
        layoutData.setOnClickListener(this);
        tvDataStatus = (TextView) findViewById(R.id.tv_mobile_internet);
        pbData = (ProgressBar) findViewById(R.id.pb_data);
        // 4G
        layout4G = (RelativeLayout) findViewById(R.id.layout_4g);
        layout4G.setOnClickListener(this);
        // Vas
        layoutVas = (RelativeLayout) findViewById(R.id.layout_vas);
        layoutVas.setOnClickListener(this);
        // Transfer
        layoutTransfer = (RelativeLayout) findViewById(R.id.layout_tranfer);
        layoutTransfer.setOnClickListener(this);

        boxInternet = (RelativeLayout) findViewById(R.id.box_internet);
        boxInternet.setOnClickListener(this);

        boxBalance = (RelativeLayout) findViewById(R.id.box_balance);
        boxBalance.setOnClickListener(this);

        boxOffer = (RelativeLayout) findViewById(R.id.box_offer);
        boxOffer.setOnClickListener(this);

        boxService = (RelativeLayout) findViewById(R.id.box_service);
        boxService.setOnClickListener(this);

        layoutInternetAmount = (RelativeLayout) findViewById(R.id.layout_internet_amount);
        tvInternetAmount = (TextView) findViewById(R.id.tv_internet_amount);
        seekAmount = (SeekArc) findViewById(R.id.seek_amount);
        seekAmount.setEnabled(false);
        seekAmount.setMax(100);
        if (get3gData) {
            set3gData();
        }
        // refresh view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent intent = new Intent();
                intent.setAction(Action.FILTER);
                intent.putExtra(Action.ACTION, Action.RELOAD_DATA);
                sendBroadcast(intent);
            }
        });
    }

    private void setBasicAccount() {
        if (mProfile.accountType == DEBIT) {
            ivBasicBalance.setImageResource(R.drawable.home_box_balance);
            labelBalance.setText("Basic banlance");
            tvBalanceStatus.setText(basicAcc.value + " KIP");
        } else if (mProfile.accountType == CREDIT) {
            ivBasicBalance.setImageResource(R.drawable.home_box_debit);
            labelBalance.setText("Current hotcharge");
            tvBalanceStatus.setText(debitAcc.charge + " KIP");
        }
        pbBalance.setVisibility(View.GONE);
    }

    private void set3gData() {
        if (myInternet.name.equals("") || myInternet.dataVolume == 0) {
            tvDataStatus.setText(data3g);
            tvDataStatus.setVisibility(View.VISIBLE);
            layoutInternetAmount.setVisibility(View.GONE);
        } else {
            Log.v("", "tiench set 3G data " + myInternet.dataLeft + "/" + myInternet.dataVolume);
            tvDataStatus.setVisibility(View.GONE);
            layoutInternetAmount.setVisibility(View.VISIBLE);
            tvInternetAmount.setText(myInternet.dataLeft + "MB");
            seekAmount.setProgress((int) (myInternet.dataLeft * 100 / myInternet.dataVolume));
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        if (menu.isMenuShowing()) {
            menu.showContent();
        } else {
            finish();
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

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        if (!preferences.getString(PreferenceKey.LOCATE, "en").equals(locate)) {
            locate = preferences.getString(PreferenceKey.LOCATE, "en");
            setLanguage();
        }
    }

    private class AsyncTaskSubInfo extends AsyncTask<String, String, Boolean> {
        String message = "";

        @Override
        protected Boolean doInBackground(String... p) {
            // TODO Auto-generated method stub
            try {
                List<ModelTag> tags = new ArrayList<ModelTag>();
                tags.add(new ModelTag(SoapTag.USERNAME, USERNAME));
                tags.add(new ModelTag(SoapTag.PASSWORD, PASSWORD));
                tags.add(new ModelTag(SoapTag.RAWDATA, "?"));
                List<ModelParam> params = new ArrayList<ModelParam>();
                params.add(new ModelParam("isdn", preferences.getString(
                        PreferenceKey.PHONE_NUMBER, "")));
                SOAPUtil soap = new SOAPUtil(WSCode.GET_SUB_INFO, tags, params);
                soap.makeSOAPRequest();
                if (soap.getError() == 0) {
                    mProfile.accountType = Integer.parseInt(soap
                            .getValue(mXML.SUB_TYPE));
                    mProfile.startTime = soap.getValue(mXML.START_DATE_TIME);
                    mProfile.userUsing = soap.getValue(mXML.USER_USING);
                    mProfile.address = soap.getValue(mXML.ADDRESS);
                    return true;
                } else {
                    message = soap.getValue(mXML.DESCRIPTION);
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
            if (result) {
                getProfile = true;
                setProfile();
                layoutSplash.setVisibility(View.GONE);
                if (mProfile.accountType == DEBIT) {
                    new AsyncTaskAccInfo().execute();
                } else if (mProfile.accountType == CREDIT) {
                    new AsyncTaskDebitInfo().execute();
                    layoutTransfer.setVisibility(View.GONE);
                }
                Intent intent = new Intent();
                intent.setAction(Action.FILTER);
                intent.putExtra(Action.ACTION, Action.LOAD_PROFILE);
                sendBroadcast(intent);
            } else {
                DialogReport dlErr = new DialogReport(mContext, message);
                dlErr.show();
                dlErr.btnOK.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        finish();
                    }
                });
            }
        }

    }

    private void setProfile() {
        tvUsername.setText(mProfile.userUsing);
        tvIsdn.setText(preferences.getString(
                PreferenceKey.PHONE_NUMBER, ""));
        if (mProfile.accountType == DEBIT) {
            tvAccType.setText(getString(R.string.prepaid));
        } else {
            tvAccType.setText(getString(R.string.postpaid));
        }
    }

    private class AsyncTaskAccInfo extends AsyncTask<String, String, Boolean> {
        String message = "";
        List<String> ignoID;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            // layoutBasicAccount.setVisibility(View.INVISIBLE);
            pbBalance.setVisibility(View.VISIBLE);
            listBalances = new ArrayList<ModelBalance>();
            ignoID = new ArrayList<String>();
            ignoID.add("26");
            ignoID.add("12");
            ignoID.add("16");
            ignoID.add("27");
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
                // params.add(new ModelParam("listId", "2000"));
                SOAPUtil soap = new SOAPUtil(WSCode.GET_SUB_ACC, tags, params);
                soap.makeSOAPRequest();
                if (soap.getError() == 0) {
                    NodeList listId = soap.mDoc
                            .getElementsByTagName("balanceId");
                    NodeList listExpire = soap.mDoc
                            .getElementsByTagName("balanceExpire");
                    NodeList listName = soap.mDoc
                            .getElementsByTagName("balanceName");
                    NodeList listValue = soap.mDoc
                            .getElementsByTagName("balanceValue");
                    if (listId.getLength() > 0) {
                        for (int i = 0; i < listId.getLength(); i++) {
                            Log.v("", "tiench id balance: " + XMLParser.getElementValue(listId.item(i)));
                            if (XMLParser.getElementValue(listId.item(i))
                                    .equals("1")) {
                                basicAcc = new ModelBalance(
                                        XMLParser.getElementValue(listExpire
                                                .item(i)),
                                        XMLParser.getElementValue(listId
                                                .item(i)),
                                        XMLParser.getElementValue(listName
                                                .item(i)),
                                        FunctionHelper.formatMoney(XMLParser
                                                .getElementValue(listValue
                                                        .item(i))));
                            } else if (XMLParser.getElementValue(listId.item(i))
                                    .equals("13")) {
                                try {
                                    Log.v("", "tiench data left: " + Integer.parseInt(XMLParser.getElementValue(listValue
                                            .item(i))));
                                    myInternet.dataLeft = (int) (Integer.parseInt(XMLParser.getElementValue(listValue
                                            .item(i))) / 1024);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (!ignoID.contains(XMLParser.getElementValue(listId.item(i)))) {
                                ModelBalance ba = new ModelBalance();
                                ba.id = XMLParser.getElementValue(listId
                                        .item(i));
                                ba.name = XMLParser.getElementValue(listName
                                        .item(i));
                                ba.expire = XMLParser
                                        .getElementValue(listExpire.item(i));
                                ba.value = FunctionHelper.formatMoney(XMLParser
                                        .getElementValue(listValue.item(i)));
                                listBalances.add(ba);
                            }
                        }
                    }
                    return true;
                } else {
                    message = FunctionHelper.getErrorMessage(mContext,
                            soap.getError());
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
            if (result) {
                getBalance = true;
                Intent intent = new Intent();
                intent.setAction(Action.FILTER);
                intent.putExtra(Action.ACTION, Action.DONE_RELOAD_ACCOUNT);
                sendBroadcast(intent);
            } else {
                basicAcc = new ModelBalance("", "", "", "");
            }
            setBasicAccount();
            new AsyncTaskCheck3G()
                    .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

    }

    private class AsyncTaskCheck3G extends AsyncTask<String, String, Boolean> {
        String message = "";

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pbData.setVisibility(View.VISIBLE);
            // layoutData.setVisibility(View.INVISIBLE);
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
            data3g = FunctionHelper.splitDataMess(message);
            myInternet.avalable = result;
            myInternet.mess = data3g;
            if (result) {
                myInternet.name = FunctionHelper.splitDataCode(message);
                new AsyncTaskDetal3G().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                myInternet.packageCode = "";
                myInternet.name = "";
                get3gData = true;
                set3gData();
                Intent intent = new Intent();
                intent.setAction(Action.FILTER);
                intent.putExtra(Action.ACTION, Action.DONE_RELOAD_DATA);
                sendBroadcast(intent);
            }
        }
    }

    private class AsyncTaskDetal3G extends AsyncTask<String, String, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... p) {
            try {
                JSONParser jParser = new JSONParser();
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("name", myInternet.name);
                String url = MyService.DETAIL_3G + "?name=" + myInternet.name
                        + "&signature=" + FunctionHelper.makeSignatureAPT(map);
                Log.v("", "tiench get detail: " + url);
                JSONObject obj = new JSONObject(jParser.getJSONFromUrl(url,
                        METHOD_GET, null));
                if (obj.getInt("status") == 1) {
                    JSONObject data = obj.getJSONObject("data");
                    myInternet.dataVolume = Integer.parseInt(data.getString("data").replaceAll(",", "").replace(".", ""));
                    myInternet.plan = data.getString("description");
                    myInternet.packageCode = data.getString("code");
                    myInternet.type = data.getString("type");
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            get3gData = true;
            set3gData();
            Intent intent = new Intent();
            intent.setAction(Action.FILTER);
            intent.putExtra(Action.ACTION, Action.DONE_RELOAD_DATA);
            sendBroadcast(intent);
        }
    }

    private class AsyncTaskDebitInfo extends AsyncTask<String, String, Boolean> {
        String message = "";

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pbBalance.setVisibility(View.VISIBLE);
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
                params.add(new ModelParam("isdn", preferences.getString(
                        PreferenceKey.PHONE_NUMBER, "")));
                SOAPUtil soap = new SOAPUtil(WSCode.GET_DEBIT_INFO, tags,
                        params);
                soap.makeSOAPRequest();
                if (soap.getError() == 0) {
                    if (soap.getValue(mXML.RESPONSE_CODE).equals("0")) {
                        String v = soap.getValue(mXML.VALUE).trim();
                        Log.v(TAG, "tiench value: " + v);
                        String[] value = v.split("\\|");
                        Log.d(TAG, "tiench value: " + value.length);
                        debitAcc = new ModelDebit(
                                FunctionHelper.formatMoney(value[0]),
                                FunctionHelper.formatMoney(value[1]),
                                FunctionHelper.formatMoney(value[2]));
                        return true;
                    } else {
                        message = soap.getValue(mXML.DESCRIPTION);
                        return false;
                    }
                } else {
                    message = soap.getValue(mXML.DESCRIPTION);
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
            if (result) {
                getBalance = true;
                Intent intent = new Intent();
                intent.setAction(Action.FILTER);
                intent.putExtra(Action.ACTION, Action.DONE_RELOAD_ACCOUNT);
                sendBroadcast(intent);
            }
            setBasicAccount();
            new AsyncTaskCheck3G()
                    .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.layout_balance:
                if (getBalance) {
                    if (mProfile.accountType == DEBIT) {
                        // Intent iDetail = new Intent(mContext,
                        // ActivityBasicAccount.class);
                        // startActivity(iDetail);
                        // overridePendingTransition(R.anim.slide_in_left,
                        // R.anim.slide_out_left);
                        changeTab(TabIndex.DEBIT);
                    } else if (mProfile.accountType == CREDIT) {
                        // Intent iDetail = new Intent(mContext,
                        // ActivityBasicDebit.class);
                        // startActivity(iDetail);
                        // overridePendingTransition(R.anim.slide_in_left,
                        // R.anim.slide_out_left);
                        changeTab(TabIndex.CREDIT);
                    }
                }
                break;
            case R.id.layout_data:
                if (get3gData) {
                    Intent i3g = new Intent(mContext, ActivityBasic3G.class);
                    startActivity(i3g);
                    overridePendingTransition(R.anim.slide_in_left,
                            R.anim.slide_out_left);
                }
                break;
            case R.id.layout_offer:
                Intent iOffer = new Intent(mContext, ActivityPromotion.class);
                startActivity(iOffer);
                overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_left);
                break;
            case R.id.layout_4g:
                // Intent i4g = new Intent(mContext, ActivityBasic4G.class);
                // startActivity(i4g);
                // overridePendingTransition(R.anim.slide_in_left,
                // R.anim.slide_out_left);
                break;
            case R.id.layout_vas:
                Intent iVas = new Intent(mContext, ActivityVas.class);
                startActivity(iVas);
                overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_left);
                break;
            case R.id.layout_tranfer:
                if (getBalance) {
                    if (mProfile.accountType == DEBIT) {
                        Intent iDetail = new Intent(mContext,
                                ActivityBasicTranfer.class);
                        startActivity(iDetail);
                        overridePendingTransition(R.anim.slide_in_left,
                                R.anim.slide_out_left);
                    } else if (mProfile.accountType == CREDIT) {
                        Intent iDetail = new Intent(mContext,
                                ActivityBasicDebit.class);
                        startActivity(iDetail);
                        overridePendingTransition(R.anim.slide_in_left,
                                R.anim.slide_out_left);
                    }
                }
                break;
            case R.id.box_internet:
                changeTab(TabIndex.DATA);
                break;
            case R.id.box_offer:
                changeTab(TabIndex.OFFER);
                break;
            case R.id.box_service:
                changeTab(TabIndex.SERVICE);
                break;
            case R.id.box_balance:
                if (getBalance) {
                    if (mProfile.accountType == DEBIT) {
//					Intent iDetail = new Intent(mContext,
//							ActivityBasicAccount.class);
//					startActivity(iDetail);
//					overridePendingTransition(R.anim.slide_in_left,
//							R.anim.slide_out_left);
                        changeTab(TabIndex.DEBIT);
                    } else if (mProfile.accountType == CREDIT) {
//					Intent iDetail = new Intent(mContext,
//							ActivityBasicDebit.class);
//					startActivity(iDetail);
//					overridePendingTransition(R.anim.slide_in_left,
//							R.anim.slide_out_left);
                        changeTab(TabIndex.CREDIT);
                    }
                }
                break;
            case R.id.banner_profile:
                if (getProfile) {
//                    Intent iBalance = new Intent(mContext, ActivityProfiles.class);
//                    startActivity(iBalance);
//                    overridePendingTransition(R.anim.slide_in_left,
//                            R.anim.slide_out_left);
                }
            default:
                break;
        }
    }

    private void changeTab(int index) {
        Intent intent = new Intent();
        intent.setAction(Action.FILTER);
        intent.putExtra(Action.ACTION, Action.CHANGE_TAB);
        intent.putExtra("index", index);
        sendBroadcast(intent);
    }

    private class MainReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            Bundle mBundle = intent.getExtras();
            if (mBundle.getString(Action.ACTION).equals(Action.RELOAD_ACCOUNT)) {
                if (mProfile.accountType == DEBIT) {
                    new AsyncTaskAccInfo().execute();
                } else if (mProfile.accountType == CREDIT) {
                    new AsyncTaskDebitInfo().execute();
                }
            } else if (mBundle.getString(Action.ACTION).equals(
                    Action.RELOAD_DATA)) {
                if (mProfile.accountType == DEBIT) {
                    new AsyncTaskAccInfo().execute();
                } else if (mProfile.accountType == CREDIT) {
                    new AsyncTaskDebitInfo().execute();
                }
            } else if (mBundle.getString(Action.ACTION)
                    .equals(Action.DONE_RELOAD_DATA)) {
                try {
                    swipeContainer.setRefreshing(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
