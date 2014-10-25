package com.OldHandsomeTiger.Aty;

import java.util.ArrayList;
import java.util.List;

import com.OldHandsomeTiger.adapter.AppInfoAdapter;
import com.OldHandsomeTiger.domain.AppInfo;
import com.OldHandsomeTiger.engine.AppInfo_Service;
import com.OldHandsomeTiger.tigersafe.R;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.PopupMenuCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class Aty_2_AppManage extends Activity implements OnItemClickListener,
		OnScrollListener, OnClickListener {

	private static final String TAG = "Aty_AppManage";
	protected static final int REFRESH_LISTVIEW = 99;
	private ListView lv_AppInfos;
	private List<AppInfo> appInfos;
	private List<AppInfo> userAppInfos;
	private static PopupWindow popupWindow;
	private LinearLayout llLayout;
	private LinearLayout llLayout_jiazai;
	private TextView Click_textView;

	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			if (msg.what == REFRESH_LISTVIEW) {
				lv_AppInfos.setVisibility(View.VISIBLE);
				llLayout_jiazai.setVisibility(View.INVISIBLE);
				lv_AppInfos.setAdapter(adapter);
			}

		}
	};
	private AppInfoAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_manager);
		lv_AppInfos = (ListView) findViewById(R.id.lv_app_manager);
		llLayout_jiazai = (LinearLayout) findViewById(R.id.ll_app_manager_loading);
		Click_textView = (TextView) findViewById(R.id.tv_app_manager_title);
		Click_textView.setOnClickListener(this);
		lv_AppInfos.setOnItemClickListener(this);
		lv_AppInfos.setOnScrollListener(this);

		new Thread() {
			@Override
			public void run() {
				super.run();
				// 获取所有的应用程序也是耗时操作啊……
				appInfos = new AppInfo_Service(Aty_2_AppManage.this)
						.getAppInfos();
				adapter = new AppInfoAdapter(Aty_2_AppManage.this, appInfos);
				handler.sendEmptyMessage(REFRESH_LISTVIEW);
			}

		}.start();

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// 先得到被点击的列表框所在位置，用来放置弹出窗口
		int[] location = new int[2];
		view.getLocationInWindow(location);
		int x = location[0] + 60;
		int y = location[1];
		if (popupWindow != null) {
			popupWindow.dismiss();
			popupWindow = null;
		}
		AppInfo appInfo = appInfos.get(position);

		ScaleAnimation scaleAnimation = new ScaleAnimation(0.1f, 1f, 0.1f, 1f);
		scaleAnimation.setDuration(500);

		View popView = view.inflate(Aty_2_AppManage.this, R.layout.popup_item,
				null);
		llLayout = (LinearLayout) popView.findViewById(R.id.ll_popup);
		LinearLayout ll_unistall = (LinearLayout) popView
				.findViewById(R.id.ll_uninstall);
		LinearLayout ll_start = (LinearLayout) popView
				.findViewById(R.id.ll_start);
		LinearLayout ll_share = (LinearLayout) popView
				.findViewById(R.id.ll_share);

		llLayout.setTag(position);

		ll_unistall.setOnClickListener(this);
		ll_start.setOnClickListener(this);
		ll_share.setOnClickListener(this);

		// TextView textView=new TextView(Aty_AppManage.this);
		// textView.setTextColor(Color.WHITE);
		// textView.setBackgroundColor(Color.GREEN);
		// textView.setTextSize(20);
		// textView.setGravity(Gravity.CENTER_HORIZONTAL);
		// textView.setTextDirection(textDirection);

		Drawable background = getResources().getDrawable(
				R.drawable.local_popup_bg);
		// textView.setText(appInfo.getAppName());

		popupWindow = new PopupWindow(popView, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		popupWindow.setBackgroundDrawable(background);
		popupWindow.showAtLocation(popView, Gravity.LEFT | Gravity.TOP, x, y);
		llLayout.startAnimation(scaleAnimation);// 开启动画效果
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (popupWindow != null) {
			popupWindow.dismiss();
			popupWindow = null;
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (popupWindow != null) {
			popupWindow.dismiss();
			popupWindow = null;
		}
	}

	@Override
	public void onClick(View v) {

		int positon = 0;
		// 点击切换所有程序和用户程序的TV时，不能执行下面的程序，否则报错……
		if (v.getId() != Click_textView.getId()) {
			positon = (int) llLayout.getTag();
		}

		AppInfo appInfo;
		String packageName;
		if (Click_textView.getText().toString().equals("所有程序")) {
			appInfo = appInfos.get(positon);
			packageName = appInfo.getAppPackage();
		} else {
			appInfo = userAppInfos.get(positon);
			packageName = appInfo.getAppPackage();
		}

		switch (v.getId()) {
		case R.id.ll_start:
			Log.i(TAG, "启动程序" + appInfos.get(positon).getAppName());
			try {
				PackageInfo packageInfo = getPackageManager().getPackageInfo(
						packageName,
						PackageManager.GET_UNINSTALLED_PACKAGES
								| PackageManager.GET_ACTIVITIES);
				ActivityInfo[] activityInfos = packageInfo.activities;// 获取这个包信息对象下的所有activity
				if (activityInfos.length > 0) {
					ActivityInfo mainActivityInfo = activityInfos[0];
					String mainActivity = mainActivityInfo.name;
					Intent intent = new Intent();
					intent.setClassName(packageName, mainActivity);// 用包名和类名唯一的确定一个意图
					startActivity(intent);
				} else {
					Toast.makeText(Aty_2_AppManage.this, "不能启动当前应用程序！",
							Toast.LENGTH_SHORT).show();
				}
			} catch (NameNotFoundException e) {
				e.printStackTrace();
				Toast.makeText(Aty_2_AppManage.this, "启动失败！",
						Toast.LENGTH_SHORT).show();
			}

			break;

		case R.id.ll_share:
			Log.i(TAG, "分享程序" + appInfos.get(positon).getAppName());

			Intent shareIntent = new Intent();
			shareIntent.setAction(Intent.ACTION_SEND);
			// shareIntent.putExtra("android.intent.extra.SUBJECT", "分享");
			shareIntent.setType("text/plain");
			// 需要指定意图的数据类型
			shareIntent.putExtra(Intent.EXTRA_SUBJECT, "分享");
			shareIntent.putExtra(Intent.EXTRA_TEXT,
					"推荐你使用一个程序" + appInfo.getAppName());
			shareIntent = Intent.createChooser(shareIntent, "分享");
			startActivity(shareIntent);
			break;

		case R.id.ll_uninstall:
			Log.i(TAG, "卸载程序" + appInfos.get(positon).getAppName());

			// 需求不能卸载系统的应用程序
			if (appInfo.isSystemApp()) {
				Toast.makeText(this, "系统应用不能被删除", 0).show();
			} else {
				String uristr = "package:" + packageName;
				Uri uri = Uri.parse(uristr);
				Intent deleteIntent = new Intent();
				deleteIntent.setAction(Intent.ACTION_DELETE);
				deleteIntent.setData(uri);
				startActivityForResult(deleteIntent, 0);
			}
			break;

		case R.id.tv_app_manager_title:
			String currentText = Click_textView.getText().toString();
			if (currentText.equals("所有程序")) {

				Click_textView.setText("用户程序");
				// 加载用户程序的Listview数据
				userAppInfos = getUserApps(appInfos);
				adapter.setAppInfos(userAppInfos);
				adapter.notifyDataSetChanged();

			} else {
				Click_textView.setText("所有程序");
				adapter.setAppInfos(appInfos);
				adapter.notifyDataSetChanged();
			}

			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 0) {
			super.onActivityResult(requestCode, resultCode, data);
			// 要求主界面刷新，为什么下面的代码不能动态更新？？
			// Log.i(TAG, "回调了1");
			// appInfos = new AppInfo_Service(this).getAppInfos();
			// adapter.notifyDataSetChanged();
			// Log.i(TAG, "回调了2");
			lv_AppInfos.setVisibility(View.INVISIBLE);
			llLayout_jiazai.setVisibility(View.VISIBLE);
			
			new Thread() {
				@Override
				public void run() {
					super.run();
					// 获取所有的应用程序也是耗时操作啊……
					if (Click_textView.getText().toString().equals("所有程序")) {
						appInfos = new AppInfo_Service(Aty_2_AppManage.this)
								.getAppInfos();
						adapter = new AppInfoAdapter(Aty_2_AppManage.this,
								appInfos);
						handler.sendEmptyMessage(REFRESH_LISTVIEW);
					} else {
						Log.i(TAG, "用户程序列表更新");
					
						appInfos = new AppInfo_Service(Aty_2_AppManage.this)
								.getAppInfos();
						userAppInfos=getUserApps(appInfos);
						adapter = new AppInfoAdapter(Aty_2_AppManage.this,
								userAppInfos);
						handler.sendEmptyMessage(REFRESH_LISTVIEW);

					}
				}

			}.start();

		}

	}

	/**
	 * 获取所有的用户安装的app
	 * 
	 * @param appinfos
	 * @return
	 */
	private List<AppInfo> getUserApps(List<AppInfo> appinfos) {
		List<AppInfo> userAppinfos = new ArrayList<AppInfo>();
		for (AppInfo appinfo : appinfos) {
			if (!appinfo.isSystemApp()) {
				userAppinfos.add(appinfo);
			}
		}
		return userAppinfos;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "Destroy……");
		if (popupWindow != null) {
			popupWindow.dismiss();
			popupWindow = null;
		}

	}

}
