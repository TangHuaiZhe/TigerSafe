package com.OldHandsomeTiger.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.OldHandsomeTiger.db.dao.Dao_AppLock;
import com.OldHandsomeTiger.domain.AppInfo;
import com.OldHandsomeTiger.tigersafe.R;

public class AppLockAdapter extends BaseAdapter {

	private static final String TAG = "AppLockAdapter";
	private Context context;
	private List<AppInfo> appInfos;
	private static ImageView imageView;
	private static TextView AppName;
	private static TextView AppPackageName;
	private static ImageView Imv_isLock;

	public AppLockAdapter(Context context, List<AppInfo> appInfos) {
		this.context = context;
		this.appInfos = appInfos;
	}

	@Override
	public int getCount() {
		return appInfos.size();
	}

	@Override
	public AppInfo getItem(int position) {
		return appInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		if (convertView == null) {
			view = View.inflate(context, R.layout.lock_app_item, null);
		} else {
			view = convertView;
		}
		imageView = (ImageView) view.findViewById(R.id.iv_app_icon);
		AppName = (TextView) view.findViewById(R.id.tv_app_name);
		AppPackageName = (TextView) view.findViewById(R.id.tv_app_packname);
		Imv_isLock = (ImageView) view.findViewById(R.id.iv_app_lock_status);

		imageView.setImageDrawable(appInfos.get(position).getAppIcon());
		AppName.setText(appInfos.get(position).getAppName());
		AppPackageName.setText(appInfos.get(position).getAppPackage());

		if (appInfos.get(position).isLocked()) {
			Imv_isLock.setImageResource(R.drawable.lock);

		} else {
			Imv_isLock.setImageResource(R.drawable.unlock);
		}
		return view;
	}

}
