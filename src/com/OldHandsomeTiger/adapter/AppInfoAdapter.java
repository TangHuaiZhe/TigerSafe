package com.OldHandsomeTiger.adapter;

import java.util.List;

import com.OldHandsomeTiger.domain.AppInfo;
import com.OldHandsomeTiger.tigersafe.R;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * APP信息Adapter
 * @author tang
 *
 */
public class AppInfoAdapter extends BaseAdapter {
	private static final String TAG = "AppInfoAdapter";
	private Context context;
	private List<AppInfo> appInfos;
	private static ImageView imageView;
	private static TextView textView;

	public AppInfoAdapter(Context context, List<AppInfo> appInfos) {
		this.context = context;
		this.appInfos = appInfos;
	}

	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return appInfos.size();
	}

	@Override
	public AppInfo getItem(int position) {
		// TODO 自动生成的方法存根
		return appInfos.get(position);
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
//			Log.i(TAG,"通过资源文件 创建view对象");
			view = View.inflate(context, R.layout.app_item, null);

		} else {
//			Log.i(TAG,"使用历史缓存view对象");
			view = convertView;
		}
		
		imageView = (ImageView) view.findViewById(R.id.iv_app_icon);
		textView = (TextView) view.findViewById(R.id.tv_app_name);
		imageView.setImageDrawable(appInfos.get(position).getAppIcon());
		textView.setText(appInfos.get(position).getAppName());

		return view;
	}

	
//此方法用以notifysetchanged方法之前，设置数据源，然后会调用getview
//	方法更新ListView数据
	public void setAppInfos(List<AppInfo> userAppInfos) {
		this.appInfos=userAppInfos;
	}

}
