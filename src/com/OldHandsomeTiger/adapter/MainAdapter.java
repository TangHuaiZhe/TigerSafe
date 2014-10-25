package com.OldHandsomeTiger.adapter;

import com.OldHandsomeTiger.tigersafe.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MainAdapter extends BaseAdapter {
	private static final String TAG = "MainUIAdapter";
	private Context context;
	private LayoutInflater inflater;
	private static ImageView iv_icon;
	private static TextView tv_name;
	private SharedPreferences sp;
	private static String[] names = { "手机防盗", "通讯卫士", "软件管理", "任务管理", "流量管理",
			"手机杀毒", "系统优化", "高级工具", "设置中心","硬件信息" };
	private static int[] icons = { R.drawable.icon1, R.drawable.icon2,
			R.drawable.icon3, R.drawable.icon4, R.drawable.icon5,
			R.drawable.icon6, R.drawable.icon7, R.drawable.icon8,
			R.drawable.icon9 ,R.drawable.icon10};

	public MainAdapter(Context context) {
		super();
		this.context = context;
		inflater = LayoutInflater.from(context);
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
	}

	@Override
	public int getCount() {
		return names.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = inflater.inflate(R.layout.main_ui_item, null);
		ImageView imageView = (ImageView) view.findViewById(R.id.iv_main_icon);
		TextView textView = (TextView) view.findViewById(R.id.tv_main_name);

		imageView.setImageResource(icons[position]);
		textView.setText(names[position]);

		return view;

	}

}
