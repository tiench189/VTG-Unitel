package com.vtg.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import com.vtg.unitel.R;

public class ActivityShowroomImage extends Activity {

	private ImageView ivShowroom;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_showroom_image);

		ivShowroom = (ImageView) findViewById(R.id.iv_showroom);
	}

}
