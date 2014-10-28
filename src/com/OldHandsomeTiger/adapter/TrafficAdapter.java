package com.OldHandsomeTiger.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.OldHandsomeTiger.Aty.Aty_4_TrafficView;
import com.OldHandsomeTiger.tigersafe.R;
import com.OldHandsomeTiger.util.ImageUtil;
import com.OldHandsomeTiger.util.TextFormater;
/**
 * 流量信息Adapter
 * @author tang
 *
 */

public class TrafficAdapter extends BaseAdapter {
	List<ResolveInfo> resovleInfos;
	Context context;
	PackageManager pm ;

	public TrafficAdapter(Context context) {
		super();
		this.context = context;
		pm = context.getPackageManager();
		Intent intent = new Intent();
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.LAUNCHER");
		
		//过滤intent，只含有上述定义的intent才可以，这样也可能导致一些APP找不到……
		//后续试验之
		resovleInfos = pm.queryIntentActivities(intent,
				PackageManager.MATCH_DEFAULT_ONLY);

	}

	public int getCount() {
		// TODO Auto-generated method stub
		return resovleInfos.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			view = View.inflate(context.getApplicationContext(),
					R.layout.traffic_item, null);
		} else {
			view = convertView;
		}
		ViewHolder holder = new ViewHolder();
		holder.iv = (ImageView) view.findViewById(R.id.iv_traffic_icon);
		holder.tv_name = (TextView) view.findViewById(R.id.tv_traffic_name);
		holder.tv_tx = (TextView) view.findViewById(R.id.tv_traffic_tx);
		holder.tv_rx = (TextView) view.findViewById(R.id.tv_traffic_rx);
		ResolveInfo info = resovleInfos.get(position);

		String appname = info.loadLabel(pm).toString();
		holder.tv_name.setText(appname);

		Drawable appicon = info.loadIcon(pm);
		Bitmap resizeicon = ImageUtil.getResizedBitmap(
				(BitmapDrawable) appicon, context);
		holder.iv.setImageBitmap(resizeicon);
		String packname = info.activityInfo.packageName;
		try {
			PackageInfo packinfo = pm.getPackageInfo(packname, 0);
			int uid = packinfo.applicationInfo.uid;
//			关键类TrafficStats，关键方法getUidRxBytes(uid)
			holder.tv_rx.setText(TextFormater.getDataSize(TrafficStats
					.getUidRxBytes(uid)));
			holder.tv_tx.setText(TextFormater.getDataSize(TrafficStats
					.getUidTxBytes(uid)));

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return view;
	}

	static class ViewHolder {
		ImageView iv;
		TextView tv_name;
		TextView tv_tx;
		TextView tv_rx;

	}
}
