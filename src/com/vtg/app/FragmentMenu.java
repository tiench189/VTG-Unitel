package com.vtg.app;

import com.vtg.app.component.DialogConfirm;
import com.vtg.app.util.CommonDefine;
import com.vtg.unitel.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FragmentMenu extends Fragment implements OnClickListener,
		CommonDefine {

	private RelativeLayout layoutNotification, layoutProfile, layoutAbout,
			layoutTariff, layoutShowroom, layoutSetting, layoutHelp;
	private TextView tvNumber;

	private SharedPreferences preferences;
	private RelativeLayout btnLogout;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		preferences = getActivity().getSharedPreferences(MY_PACKAGE,
				Context.MODE_PRIVATE);
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_menu, null);
		initView(view);
		return view;
	}

	private void initView(View view) {
		layoutNotification = (RelativeLayout) view
				.findViewById(R.id.layout_notification);
		layoutNotification.setOnClickListener(this);
		layoutProfile = (RelativeLayout) view.findViewById(R.id.layout_profile);
		layoutProfile.setOnClickListener(this);
		layoutAbout = (RelativeLayout) view.findViewById(R.id.layout_about);
		layoutAbout.setOnClickListener(this);
		layoutTariff = (RelativeLayout) view.findViewById(R.id.layout_tariff);
		layoutTariff.setOnClickListener(this);
		layoutShowroom = (RelativeLayout) view
				.findViewById(R.id.layout_showroom);
		layoutShowroom.setOnClickListener(this);
		layoutSetting = (RelativeLayout) view.findViewById(R.id.layout_setting);
		layoutSetting.setOnClickListener(this);
		layoutHelp = (RelativeLayout) view.findViewById(R.id.layout_help);
		layoutHelp.setOnClickListener(this);

		tvNumber = (TextView) view.findViewById(R.id.tv_number);
		tvNumber.setText(preferences.getString(PreferenceKey.PHONE_NUMBER, ""));
		btnLogout = (RelativeLayout) view.findViewById(R.id.btn_logout);
		btnLogout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_notification:

			break;
		case R.id.layout_profile:
			Intent iProfiles = new Intent(getActivity(), ActivityProfiles.class);
			startActivity(iProfiles);
			getActivity().overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_left);
			break;
		case R.id.layout_about:
			Intent iAbout = new Intent(getActivity(), ActivityAbout.class);
			startActivity(iAbout);
			getActivity().overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_left);
			break;
		case R.id.layout_tariff:
			Intent iTariff = new Intent(getActivity(), ActivityTariff.class);
			startActivity(iTariff);
			getActivity().overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_left);
			break;
		case R.id.layout_showroom:
			Intent iShowroom = new Intent(getActivity(), ActivityShowroom.class);
			startActivity(iShowroom);
			getActivity().overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_left);
			break;
		case R.id.layout_setting:
			Intent iSetting = new Intent(getActivity(), ActivitySetting.class);
			startActivity(iSetting);
			getActivity().overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_left);
			break;
		case R.id.layout_help:

			break;
		case R.id.btn_logout:
			actionLogout();
			break;
		default:
			break;
		}
	}

	private void actionLogout() {
		DialogConfirm cfLogout = new DialogConfirm(getActivity(),
				getString(R.string.confirm_logout));
		cfLogout.btnYes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				preferences.edit().putString(PreferenceKey.PHONE_NUMBER, "")
						.commit();
				Intent intent = new Intent(getActivity(), ActivityFirst.class);
				getActivity().startActivity(intent);
				getActivity().finish();
			}
		});
		cfLogout.show();
	}

}
