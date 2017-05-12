package com.vtg.app.component;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vtg.unitel.R;
import com.vtg.app.model.ModelShowroom;

public class AdapterShowroomSpinner extends BaseAdapter {
	private LayoutInflater inflater;
	List<ModelShowroom> listShowroom;

	public AdapterShowroomSpinner(Context context,
			List<ModelShowroom> listShowroom) {
		this.listShowroom = listShowroom;
		inflater = ((Activity) context).getLayoutInflater();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listShowroom.size();
	}

	@Override
	public ModelShowroom getItem(int position) {
		// TODO Auto-generated method stub
		return listShowroom.get(position);
	}

	private class Holder {
		TextView tv;
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
			view = inflater.inflate(R.layout.row_basic_text, null);
			Holder holder = new Holder();
			holder.tv = (TextView) view.findViewById(R.id.tv);
			view.setTag(holder);
		}

		Holder holder = (Holder) view.getTag();
		holder.tv.setText(getItem(position).name);
		return view;
	}
}
