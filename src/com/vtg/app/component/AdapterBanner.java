package com.vtg.app.component;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.vtg.app.ActivityMain;

public class AdapterBanner extends FragmentPagerAdapter {

	public AdapterBanner(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int arg0) {
		Fragment mFragment = new FragmentBanner();
		Bundle mBundle = new Bundle();
		mBundle.putInt("page", arg0);
		mFragment.setArguments(mBundle);
		return mFragment;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return ActivityMain.listBanner.size();
	}

}
