package com.vtg.app.component;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vtg.app.ActivityMain;
import com.vtg.unitel.R;
import com.vtg.app.model.ModelOffer;
import com.vtg.app.model.ModelParam;
import com.vtg.app.model.ModelSubData;
import com.vtg.app.model.ModelTag;
import com.vtg.app.util.CommonDefine;
import com.vtg.app.util.FunctionHelper;
import com.vtg.app.util.SOAPUtil;

public class AdapterSubOffer extends BaseAdapter implements CommonDefine {

	private Context context;
	private List<ModelOffer> listSub;
	private LayoutInflater inflater;
	private SharedPreferences preferences;

	public AdapterSubOffer(Context context, List<ModelOffer> listSub) {
		super();
		this.context = context;
		this.listSub = listSub;
		inflater = ((Activity) context).getLayoutInflater();
		preferences = ((Activity) context).getSharedPreferences(MY_PACKAGE,
				Context.MODE_PRIVATE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listSub.size();
	}

	@Override
	public ModelOffer getItem(int position) {
		// TODO Auto-generated method stub
		return listSub.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	private class Holder {
		private TextView tvName;
		private TextView tvDes;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			view = inflater.inflate(R.layout.row_sub_offer, null);
			Holder holder = new Holder();
			holder.tvName = (TextView) view.findViewById(R.id.tv_name);
			holder.tvDes = (TextView) view.findViewById(R.id.tv_description);
			view.setTag(holder);
		}
		Holder holder = (Holder) view.getTag();
		final ModelOffer sub = getItem(position);
		holder.tvName.setText(sub.name);
		holder.tvDes.setText(sub.info);
		return view;
	}

}
