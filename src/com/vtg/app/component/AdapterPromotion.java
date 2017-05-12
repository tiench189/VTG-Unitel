package com.vtg.app.component;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.LinearGradient;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vtg.unitel.R;
import com.vtg.app.model.ModelPromotion;

public class AdapterPromotion extends BaseAdapter {
	private Context mContext;
	private List<ModelPromotion> listPromotion;
	private LayoutInflater inflater;

	public AdapterPromotion(Context context, List<ModelPromotion> listPromotion) {
		this.mContext = context;
		this.listPromotion = listPromotion;
		inflater = ((Activity) context).getLayoutInflater();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listPromotion.size();
	}

	@Override
	public ModelPromotion getItem(int position) {
		// TODO Auto-generated method stub
		return listPromotion.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	private class Holder {
		TextView tvName;
		TextView tvDescription;
		TextView tvGuide;
		LinearLayout layoutActive;
		RelativeLayout btnActive;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		if (view == null) {
			view = inflater.inflate(R.layout.row_promotion, null);
			Holder holder = new Holder();
			holder.tvName = (TextView) view.findViewById(R.id.tv_name);
			holder.tvDescription = (TextView) view
					.findViewById(R.id.tv_description);
			holder.tvGuide = (TextView) view.findViewById(R.id.tv_guide);
			holder.layoutActive = (LinearLayout) view
					.findViewById(R.id.layout_active);
			holder.btnActive = (RelativeLayout) view
					.findViewById(R.id.btn_active);
			view.setTag(holder);
		}
		Holder holder = (Holder) view.getTag();
		ModelPromotion prom = getItem(position);
		holder.tvName.setText(prom.name);
		holder.tvDescription.setText(prom.description);
		holder.tvGuide.setText(prom.guide);
		if (prom.status == 0) {
			holder.layoutActive.setVisibility(View.GONE);
		} else {
			holder.layoutActive.setVisibility(View.VISIBLE);
		}
		holder.layoutActive.setVisibility(View.GONE);
		return view;
	}

}
