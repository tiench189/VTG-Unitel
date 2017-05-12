package com.vtg.app.component;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vtg.app.ActivityMap;
import com.vtg.app.ActivityShowroom;
import com.vtg.app.ActivityShowroomImage;
import com.vtg.unitel.R;
import com.vtg.app.model.ModelShowroom;

public class AdapterShowroom extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private List<ModelShowroom> listShowroom;

	public AdapterShowroom(Context context, List<ModelShowroom> listShowroom) {
		super();
		this.context = context;
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

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	private class Holder {
		ImageView ivCover;
		TextView tvName;
		TextView tvDescription;
		TextView tvMap;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		if (view == null) {
			view = inflater.inflate(R.layout.row_showroom, null);
			Holder holder = new Holder();
			holder.ivCover = (ImageView) view.findViewById(R.id.iv_cover);
			holder.tvName = (TextView) view.findViewById(R.id.tv_name);
			holder.tvDescription = (TextView) view
					.findViewById(R.id.tv_description);
			holder.tvMap = (TextView) view.findViewById(R.id.tv_map);
			view.setTag(holder);
		}
		Holder holder = (Holder) view.getTag();
		final ModelShowroom sr = getItem(position);
		holder.tvName.setText(sr.name);
		holder.tvDescription.setText(sr.description);
		holder.tvMap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityShowroom.crShowroom = sr;
				Intent i = new Intent(context, ActivityMap.class);
				context.startActivity(i);
				((Activity) context).overridePendingTransition(
						R.anim.slide_in_left, R.anim.slide_out_left);
			}
		});
		try {
			Glide.with(context).load(sr.img).into(holder.ivCover);
		} catch (Exception e) {

		}
		return view;
	}
}
