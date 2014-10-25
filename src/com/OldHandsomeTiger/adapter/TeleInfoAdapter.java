package com.OldHandsomeTiger.adapter;

import java.util.List;
import java.util.zip.Inflater;

import com.OldHandsomeTiger.tigersafe.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TeleInfoAdapter extends BaseAdapter {
	private Context context;
	private List<String> teleInfos;

	public TeleInfoAdapter(Context context, List<String> teleInfos) {
		// TODO 自动生成的构造函数存根
		this.context = context;
		this.teleInfos = teleInfos;
	}

	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return teleInfos.size();
	}

	@Override
	public String getItem(int position) {
		// TODO 自动生成的方法存根
		return teleInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		if (convertView == null) {
			view = View.inflate(context, R.layout.tele_info_item, null);
		} else {
			view = convertView;
		}
		TextView tv_info=(TextView) view.findViewById(R.id.tele_info_item);
		tv_info.setText(teleInfos.get(position));
		
		return view;
	}

}
