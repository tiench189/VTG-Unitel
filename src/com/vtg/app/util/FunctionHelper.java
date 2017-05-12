package com.vtg.app.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.vtg.app.ActivityMain;
import com.vtg.app.model.ModelBalance;
import com.vtg.unitel.R;

@SuppressLint("SimpleDateFormat")
public class FunctionHelper implements CommonDefine {
    private static final String TAG = "FunctionHelper";

    public static String convertBalanceName(Context context, int balanceId) {
        String name = "";
        switch (balanceId) {
            case 2000:
                name = context.getResources().getString(R.string.acc2000);
                break;
            case 2001:
                name = context.getResources().getString(R.string.acc2001);
                break;
            case 2004:
                name = context.getResources().getString(R.string.acc2004);
                break;
            case 4200:
                name = context.getResources().getString(R.string.acc4200);
                break;
            case 4046:
                name = context.getResources().getString(R.string.acc4046);
                break;
            case 4500:
                name = context.getResources().getString(R.string.acc4500);
                break;
            case 4561:
                name = context.getResources().getString(R.string.acc4561);
                break;
            case 4560:
                name = context.getResources().getString(R.string.acc4560);
                break;
            case 2504:
                name = context.getResources().getString(R.string.acc2504);
                break;
            case 2500:
                name = context.getResources().getString(R.string.acc2500);
                break;

            default:
                break;
        }
        return name;
    }

    public static String getErrorMessage(Context context, int errCode) {
        String mess = context.getString(R.string.err_m1);
        switch (errCode) {
            case 0:
                mess = context.getString(R.string.err0);
                break;
            case 1000:
                mess = context.getString(R.string.err1000);
                break;
            case 2000:
                mess = context.getString(R.string.err2000);
                break;
            case 4000:
                mess = context.getString(R.string.err4000);
                break;
            case 1001:
                mess = context.getString(R.string.err1001);
                break;
            case 6000:
                mess = context.getString(R.string.err6000);
                break;
            case 7000:
                mess = context.getString(R.string.err7000);
                break;
            case 6001:
                mess = context.getString(R.string.err6001);
                break;
            case 6004:
                mess = context.getString(R.string.err6004);
                break;
            case 6002:
                mess = context.getString(R.string.err6002);
                break;
            case 6003:
                mess = context.getString(R.string.err6003);
                break;
            case 7001:
                mess = context.getString(R.string.err7001);
                break;
            case 2005:
                mess = context.getString(R.string.err2005);
                break;
            case 6005:
                mess = context.getString(R.string.err6005);
                break;
            case 8000:
                mess = context.getString(R.string.err8000);
                break;
            case 9001:
                mess = context.getString(R.string.err9001);
                break;
            case 9002:
                mess = context.getString(R.string.err9002);
                break;
            case 3000:
                mess = context.getString(R.string.err3000);
                break;
            case 2001:
                mess = context.getString(R.string.err2001);
                break;
            case 4001:
                mess = context.getString(R.string.err4001);
                break;
            case 5000:
                mess = context.getString(R.string.err5000);
                break;
            case 1002:
                mess = context.getString(R.string.err1002);
                break;
            case 2002:
                mess = context.getString(R.string.err2002);
                break;
            case 2003:
                mess = context.getString(R.string.err2003);
                break;
            case 2004:
                mess = context.getString(R.string.err2004);
                break;
            case 9000:
                mess = context.getString(R.string.err9000);
                break;
            case 8001:
                mess = context.getString(R.string.err8001);
                break;
            case 8002:
                mess = context.getString(R.string.err8002);
                break;
            case 6006:
                mess = context.getString(R.string.err6006);
                break;
            default:
                break;
        }
        return mess;
    }

    public static String formatCurrentTime() {
        String result = "";
        Calendar c = Calendar.getInstance();
        String dd = c.get(Calendar.DATE) < 10 ? "0" + c.get(Calendar.DATE) : ""
                + c.get(Calendar.DATE);
        String MM = (c.get(Calendar.MONTH) + 1) < 10 ? "0"
                + (c.get(Calendar.MONTH) + 1) : ""
                + (c.get(Calendar.MONTH) + 1);
        String yyyy = "" + c.get(Calendar.YEAR);
        String HH = c.get(Calendar.HOUR) < 10 ? "0" + c.get(Calendar.HOUR) : ""
                + c.get(Calendar.HOUR);
        String mm = c.get(Calendar.MINUTE) < 10 ? "0" + c.get(Calendar.MINUTE)
                : "" + c.get(Calendar.MINUTE);
        String ss = c.get(Calendar.SECOND) < 10 ? "0" + c.get(Calendar.SECOND)
                : "" + c.get(Calendar.SECOND);
        result = dd + MM + yyyy + HH + mm + ss;
        return result;
    }

    public static String formatDateTime(String oldTime) {
        String result = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        SimpleDateFormat outFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date d = sdf.parse(oldTime);
            result = outFormat.format(d);
        } catch (Exception ex) {
            ex.printStackTrace();
            result = oldTime;
        }
        return result;
    }

    public static int setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return 0;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
        return totalHeight;
    }

    public static String formatMoney(String oldValue) {
        String newValue = "";
        String[] split = oldValue.split("\\.");
        if (split.length <= 1) {
            return oldValue;
        } else {
            newValue += split[0];
            newValue += ".";
            if (split[1].length() > 2) {
                newValue += split[1].substring(0, 2);
            } else {
                newValue += split[1];
            }
        }
        return newValue;
    }

    public static String makeSignatureAPT(Map<String, String> hm) {
        String TOKEN = "6b2e85357c7581a9d7ef04304ca78080";
        Map<String, String> hmSorted = new TreeMap<String, String>(hm);
        String values = "";
        Set keys = hmSorted.keySet();
        Iterator itr = keys.iterator();

        String key;
        String value;

        while (itr.hasNext()) {
            key = (String) itr.next();
            value = (String) hmSorted.get(key);
            values += value;
        }
        values += TOKEN;
        Log.v("", "tiench sort: " + values);
        return getMD5OfString(values);
    }

    public static String getMD5OfString(String s) {

        final String MD5 = "MD5";
        String md5str = "";
        try {
            // Create MD5 Hash

            MessageDigest digest = java.security.MessageDigest.getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            md5str = hexString.toString();
            // System.out.println("md5str " + md5str);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return md5str;
    }

    public static String splitDataMess(String mess) {
        String result = "";
        try {
            String[] split = mess.split("\\.");
            result += split[0] + ". ";
//			result += split[1] + ".";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String splitDataCode(String mess) {
        Log.v(TAG, "tiench 3G: " + mess);
        String result = "NO_PACK";
        try {
            Log.v(TAG, "tiench split 3G: " + mess.split("\\s+").length);
            String[] split = mess.split("\\s+");
            Log.v(TAG, "tiench first: " + split[3]);
            result = split[3].substring(0, split[3].length() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int sub = 0;
        if (ActivityMain.mProfile.accountType == CREDIT
                && result.endsWith("XX")) {
            sub = 2;
        }
        result = result.substring(0, result.length() - sub);
        Log.v(TAG, "tiench 3G name: " + result);
        return result;
    }

    public static String getPhoneNumber(Context context) {
        try {
            TelephonyManager tMgr = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String mPhoneNumber = tMgr.getLine1Number();
            Log.v(TAG,
                    "tiench mobile: " + mPhoneNumber + "/"
                            + tMgr.getNetworkCountryIso() + "/"
                            + tMgr.getSimCountryIso());
            return mPhoneNumber;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String formatPhoneNumber(String old, Context context) {
        String rs = old;
        try {
            if (old.startsWith("0")) {
                rs = old.substring(1);
            } else if (old.startsWith("+")) {
                TelephonyManager tMgr = (TelephonyManager) context
                        .getSystemService(Context.TELEPHONY_SERVICE);
                if (tMgr.getNetworkCountryIso().equals("vn")) {
                    rs = old.substring(3);
                } else {
                    rs = old.substring(4);
                }
            } else if (old.startsWith(NUMBER_HEADER)) {
                rs = old.substring(NUMBER_HEADER.length());
            }
        } catch (Exception e) {

        }
        return rs;
    }

    public static String mapBalanceName(ModelBalance balance, Context context) {
        String id = balance.id;
        if (id.equals("1")) {
            return context.getString(R.string.bal1);
        } else if (id.equals("10")) {
            return context.getString(R.string.bal10);
        } else if (id.equals("14")) {
            return context.getString(R.string.bal14);
        } else if (id.equals("15")) {
            return context.getString(R.string.bal15);
        } else if (id.equals("8")) {
            return context.getString(R.string.bal8);
        } else if (id.equals("17")) {
            return context.getString(R.string.bal17);
        } else if (id.equals("13")) {
            return context.getString(R.string.bal13);
        } else if (id.equals("19")) {
            return context.getString(R.string.bal19);
        } else if (id.equals("12")) {
            return context.getString(R.string.bal12);
        } else if (id.equals("16")) {
            return context.getString(R.string.bal16);
        } else if (id.equals("26")) {
            return context.getString(R.string.bal26);
        } else if (id.equals("28")) {
            return context.getString(R.string.bal28);
        } else {
            return balance.name;
        }
    }

    public static String mapBalanceUnit(ModelBalance balance) {
        String id = balance.id;
        if (id.equals("1")) {
            return "KIP";
        } else if (id.equals("10")) {
            return "KIP";
        } else if (id.equals("14")) {
            return "KIP";
        } else if (id.equals("15")) {
            return "s";
        } else if (id.equals("8")) {
            return "s";
        } else if (id.equals("17")) {
            return "s";
        } else if (id.equals("13")) {
            return "KB";
        } else if (id.equals("19")) {
            return "KB";
        } else if (id.equals("12")) {
            return "SMS";
        } else if (id.equals("16")) {
            return "SMS";
        } else if (id.equals("26")) {
            return "KB";
        } else if (id.equals("28")) {
            return "KB";
        } else {
            return "KIP";
        }
    }

    public static String getVersionName(Context context) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pi = null;
        String versionName = "";
        try {
            pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public static void setGridViewHeightBasedOnChildren(GridView gridView,
                                                        int max_item_in_row, int screenWidth) {
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < (listAdapter.getCount() / max_item_in_row) + 1; i++) {
            try {
                View listItem = listAdapter.getView(i * max_item_in_row, null,
                        gridView);
                listItem.measure(0, 0);
                Log.v("tiench", "tiench item height: " + listItem.getMeasuredHeight() + "/" + listItem.getMeasuredWidth());
                totalHeight += screenWidth / 2;
            } catch (Exception e) {
                // e.printStackTrace();
            }
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        Log.v("tiench", "tiech total height" + totalHeight);
        gridView.setLayoutParams(params);
        gridView.requestLayout();
    }
}