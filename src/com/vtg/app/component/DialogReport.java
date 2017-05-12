package com.vtg.app.component;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vtg.unitel.R;

/**
 * define dialog confirm
 * 
 * @author Tien
 * 
 */
public class DialogReport extends Dialog {

	public TextView tvMess;

	public RelativeLayout btnOK;

	public DialogReport(Context context, String msg) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.dialog_report);
		setCanceledOnTouchOutside(false);
		tvMess = (TextView) findViewById(R.id.tvMess);

		btnOK = (RelativeLayout) findViewById(R.id.btn_ok);

		tvMess.setText(msg);

		btnOK.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}

}
