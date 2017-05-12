package com.vtg.app.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.vtg.app.ActivityFirst;
import com.vtg.app.ActivityMain.mProfile;
import com.vtg.app.model.ModelParam;
import com.vtg.app.model.ModelTag;
import com.vtg.app.util.CommonDefine;
import com.vtg.app.util.FunctionHelper;
import com.vtg.app.util.JSONParser;
import com.vtg.app.util.SOAPUtil;
import com.vtg.unitel.R;

public class DialogRegister extends Dialog implements CommonDefine {

    private Context mContext;
    private RelativeLayout btnCancel, btnDone;
    private SharedPreferences preferences;
    private ProgressDialog pDialog;

    private EditText edtNumber;
    // private EditText edtPassword, edtRePassword;
    private String number;

    public DialogRegister(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_register);
        this.mContext = context;
        preferences = context.getSharedPreferences(MY_PACKAGE,
                Context.MODE_PRIVATE);
        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
        initView();
    }

    private void initView() {
        edtNumber = (EditText) findViewById(R.id.edt_number);
        edtNumber.setText(FunctionHelper.getPhoneNumber(mContext));
        // edtPassword = (EditText) findViewById(R.id.edt_password);
        // edtRePassword = (EditText) findViewById(R.id.edt_re_password);

        btnCancel = (RelativeLayout) findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dismiss();
            }
        });
        btnDone = (RelativeLayout) findViewById(R.id.btn_done);
        btnDone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                number = edtNumber.getText().toString().trim();
                if (number.startsWith("0")) {
                    number = number.substring(1);
                }
                // password = edtPassword.getText().toString().trim();
                // String repassword =
                // edtRePassword.getText().toString().trim();
                if (number.equals("")) {
                    Toast.makeText(mContext, "Enter mobile number ...",
                            Toast.LENGTH_LONG).show();
                } else {
                    number = FunctionHelper.formatPhoneNumber(number, mContext);
                    new AsyncTaskSubInfo().execute(number);
                }
            }
        });
    }

    private class AsyncTaskSubInfo extends AsyncTask<String, String, Boolean> {
        String message = "";

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pDialog.setMessage("checking...");
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... p) {
            // TODO Auto-generated method stub
            try {
                String phone = p[0];
                List<ModelTag> tags = new ArrayList<ModelTag>();
                tags.add(new ModelTag(SoapTag.USERNAME, USERNAME));
                tags.add(new ModelTag(SoapTag.PASSWORD, PASSWORD));
                tags.add(new ModelTag(SoapTag.RAWDATA, "?"));
                List<ModelParam> params = new ArrayList<ModelParam>();
                params.add(new ModelParam("isdn", phone));
                SOAPUtil soap = new SOAPUtil(WSCode.GET_SUB_INFO, tags, params);
                soap.makeSOAPRequest();
                if (soap.getError() == 0) {
                    mProfile.accountType = Integer.parseInt(soap
                            .getValue(mXML.SUB_TYPE));
                    mProfile.startTime = "";
                    mProfile.userUsing = "";
                    return true;
                } else {
                    message = soap.getValue(mXML.DESCRIPTION);
                    return false;
                }
            } catch (Exception e) {
                message = mContext.getString(R.string.err_connect);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (result) {
                new AsyncTaskRegis().execute(number);
            } else {
                pDialog.dismiss();
                DialogReport dlErr = new DialogReport(mContext, message);
                dlErr.show();
                dlErr.btnOK.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        dismiss();
                    }
                });
            }
        }

    }

    private class AsyncTaskRegis extends AsyncTask<String, String, Boolean> {
        String msg = "";
        String phone;
        int status;

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                phone = params[0];
                JSONParser jParser = new JSONParser();
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("phone", phone);
//				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//				nameValuePairs.add(new BasicNameValuePair(SIGNATURE,
//						FunctionHelper.makeSignatureAPT(map)));
//				nameValuePairs.add(new BasicNameValuePair("phone", phone));
//				// nameValuePairs.add(new BasicNameValuePair("password", pass));
//				String json = jParser.getJSONFromUrl(MyService.REGIS,
//						METHOD_POST, nameValuePairs);
                String url = MyService.REGIS + "?phone=" + phone + "&" + SIGNATURE + "=" + FunctionHelper.makeSignatureAPT(map);
                String json = jParser.getJSONFromUrl(url,
                        METHOD_GET, null);
                Log.v("", "tiench regis url: " + url);
                Log.v("", "tiench regis res: " + json);
                JSONObject obj = new JSONObject(json);
                status = obj.getInt("status");
                if (status == 1) {
                    return true;
                } else {
                    msg = obj.getString("msg");
                    if (status == 304) {
                        msg = mContext.getString(R.string.number_exist);
                    }
                    return false;
                }
            } catch (Exception e) {
                msg = "Connection fail!";
                return false;
            }
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pDialog.setMessage("Registing...");
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            pDialog.dismiss();
            if (result) {
                dismiss();
                new DialogReport(mContext,
                        "Register success! We send a sms with your password to number "
                                + phone + ".").show();
                ActivityFirst.edtNumber.setText(number);
            } else {
                if (status == 304) {
                    dismiss();
                }
                new DialogReport(mContext, msg).show();
            }
        }

    }
}
