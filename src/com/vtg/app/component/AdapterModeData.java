package com.vtg.app.component;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vtg.unitel.R;
import com.vtg.app.model.ModelMoreData;

public class AdapterModeData extends BaseAdapter {
	private List<ModelMoreData> listMore;
	private LayoutInflater inflater;
	public int pick = -1;

	public AdapterModeData(Context context, List<ModelMoreData> listMore) {
		this.listMore = listMore;
		inflater = ((Activity) context).getLayoutInflater();
		pick = -1;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listMore.size();
	}

	@Override
	public ModelMoreData getItem(int position) {
		// TODO Auto-generated method stub
		return listMore.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	private class Holder {
		TextView tvMore;
		RadioButton rbActive;
		RelativeLayout layoutMore;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		if (view == null) {
			view = inflater.inflate(R.layout.row_more_data, null);
			Holder holder = new Holder();
			holder.tvMore = (TextView) view.findViewById(R.id.tv_more_data);
			holder.rbActive = (RadioButton) view.findViewById(R.id.rb_active);
			holder.layoutMore = (RelativeLayout) view
					.findViewById(R.id.layout_more);
			view.setTag(holder);
		}
		final ModelMoreData more = getItem(position);
		Holder holder = (Holder) view.getTag();
		holder.tvMore.setText(more.value + "MB (" + more.price + " KIP)");
		holder.rbActive.setChecked(pick == position);
		holder.layoutMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (pick != position) {
					pick = position;
					notifyDataSetChanged();
				}
			}
		});
		return view;
	}
}
