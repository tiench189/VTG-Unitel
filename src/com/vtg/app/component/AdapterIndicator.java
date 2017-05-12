package com.vtg.app.component;

import com.vtg.unitel.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class AdapterIndicator extends BaseAdapter {

	private int count;
	private int pos = 0;
	private LayoutInflater inflater;

	public AdapterIndicator(Context context, int count) {
		this.count = count;
		inflater = ((Activity) context).getLayoutInflater();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return count;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	private class Holder {
		ImageView ivIndicator;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			view = inflater.inflate(R.layout.row_indicator, null);
			Holder holder = new Holder();
			holder.ivIndicator = (ImageView) view
					.findViewById(R.id.iv_indicator);
			view.setTag(holder);
		}

		Holder holder = (Holder) view.getTag();
		if (position == pos) {
			holder.ivIndicator.setImageResource(R.drawable.highlight_circle);
		} else {
			holder.ivIndicator.setImageResource(R.drawable.normal_circle);
		}
		return view;
	}

}
