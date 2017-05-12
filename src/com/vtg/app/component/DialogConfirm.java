package com.vtg.app.component;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vtg.unitel.R;

public class DialogConfirm extends Dialog {
	public TextView tvMess;

	public RelativeLayout btnYes, btnNo;

	public TextView tvYes, tvNo;

	public DialogConfirm(Context context, String msg) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.dialog_confirm);
		setCanceledOnTouchOutside(false);
		tvMess = (TextView) findViewById(R.id.tvMess);

		tvYes = (TextView) findViewById(R.id.tv_yes);
		tvNo = (TextView) findViewById(R.id.tv_no);

		btnYes = (RelativeLayout) findViewById(R.id.btn_yes);
		btnNo = (RelativeLayout) findViewById(R.id.btn_no);

		tvMess.setText(msg);

		btnYes.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

		btnNo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}
}
