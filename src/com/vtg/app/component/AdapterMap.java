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
import com.vtg.app.model.ModelMap;

public class AdapterMap extends BaseAdapter {

	private LayoutInflater inflater;
	private List<ModelMap> listMap;

	public AdapterMap(Context context, List<ModelMap> listMap) {
		this.listMap = listMap;
		inflater = ((Activity) context).getLayoutInflater();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listMap.size();
	}

	@Override
	public ModelMap getItem(int position) {
		// TODO Auto-generated method stub
		return listMap.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	private class Holder {
		TextView tvKey;
		TextView tvValue;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		if (view == null) {
			view = inflater.inflate(R.layout.row_profile, null);
			Holder holder = new Holder();
			holder.tvKey = (TextView) view.findViewById(R.id.tv_key);
			holder.tvValue = (TextView) view.findViewById(R.id.tv_value);
			view.setTag(holder);
		}
		Holder holder = (Holder) view.getTag();
		ModelMap map = getItem(position);
		holder.tvKey.setText(map.key);
		holder.tvValue.setText(map.value);
		return view;
	}

}
