package com.OldHandsomeTiger.Aty;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.OldHandsomeTiger.adapter.AppLockAdapter;
import com.OldHandsomeTiger.db.dao.Dao_AppLock;
import com.OldHandsomeTiger.domain.AppInfo;
import com.OldHandsomeTiger.engine.AppInfo_Service;
import com.OldHandsomeTiger.service.Service_WatchDog;
import com.OldHandsomeTiger.tigersafe.R;
import com.OldHandsomeTiger.util.Config;

public class Aty_7_2_LockApp extends Activity implements OnItemClickListener, OnCheckedChangeListener {

	private static final String TAG = "Aty_7_2_LockApp";
	private static final String DELETE_URI = "content://com.OldHandsomeTiger.AppProtectProvider/Delete";
	private static final String INSERT_URI = "content://com.OldHandsomeTiger.AppProtectProvider/Insert";
	private ListView lv;
	private CheckBox	 cb_lockStatus;
	private List<com.OldHandsomeTiger.domain.AppInfo> appinfos;
	private List<AppInfo> LockedAppInfo;
	private AppInfo appInfo;
	private LinearLayout ll_app_manager_loading;
	private AppLockAdapter adapter;
	private AppInfo_Service appInfo_Service;
	private Dao_AppLock dao_AppLock;
	private SharedPreferences sp;
	private Editor editor;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			ll_app_manager_loading.setVisibility(View.INVISIBLE);
			adapter = new AppLockAdapter(Aty_7_2_LockApp.this, appinfos);
			lv.setAdapter(adapter);
		}
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_lock);
		sp=getSharedPreferences(Config.KEY_CONFIG, MODE_PRIVATE);
		 editor=sp.edit();
		lv = (ListView) findViewById(R.id.lv_app_lock);
		cb_lockStatus=(CheckBox) findViewById(R.id.cb_lockStatus);
		cb_lockStatus.setOnCheckedChangeListener(this);
		if(sp.getBoolean(Config.KEY_START_LOCK_SERVICE, false))
		{
			cb_lockStatus.setChecked(true);
		}else {
			cb_lockStatus.setChecked(false);
		}
		lv.setOnItemClickListener(this);
		ll_app_manager_loading = (LinearLayout) findViewById(R.id.ll_app_manager_loading);
		dao_AppLock = new Dao_AppLock(this);
		// 刷新界面
		appInfo_Service = new AppInfo_Service(this);
		initUI();

	}

	private void initUI() {
		ll_app_manager_loading.setVisibility(View.VISIBLE);
		new Thread() {
			@Override
			public void run() {
				appinfos = appInfo_Service.getAppInfos();
				// 判断应用是否在被锁数据库中,并给每个应用对象标记是否被锁
				Dao_AppLock dao_AppLock = new Dao_AppLock(Aty_7_2_LockApp.this);
				List<String> LocakedPackageNames = dao_AppLock.getAllApps();
				for (AppInfo appInfo : appinfos) {
					String PackAgeName = appInfo.getAppPackage();
					if (LocakedPackageNames.contains(PackAgeName)) {
						appInfo.setLocked(true);
						Log.i(TAG, "被锁的程序：" + appInfo.getAppName());
					} else {
						appInfo.setLocked(false);
					}
				}
				handler.sendEmptyMessage(0);
			}
		}.start();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		AppInfo appInfo = appinfos.get(position);
		ImageView imageView = (ImageView) view
				.findViewById(R.id.iv_app_lock_status);
		TranslateAnimation translateAnimation=new TranslateAnimation(0f,80f,0f,0f);
		translateAnimation.setDuration(500);
		view.startAnimation(translateAnimation);
		if(appInfo.isLocked()){
			appInfo.setLocked(false);
			imageView.setImageResource(R.drawable.unlock);
			String LockedPackname = appInfo.getAppPackage();
//			dao_AppLock.delete(LockedPackname);
			getContentResolver().delete(Uri.parse(DELETE_URI), null,new String[]{ LockedPackname});
		}else {
			appInfo.setLocked(true);
			imageView.setImageResource(R.drawable.lock);
			String LockedPackname = appInfo.getAppPackage();
			//			dao_AppLock.add(LockedPackname);
			ContentValues values=new ContentValues();
			values.put("packname", LockedPackname);
			getContentResolver().insert(Uri.parse(INSERT_URI), values);
		}
		
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		Intent service=new Intent(Aty_7_2_LockApp.this, Service_WatchDog.class);
		if(isChecked){
			startService(service);
			editor.putBoolean(Config.KEY_START_LOCK_SERVICE, true);
		}else {
			//->ondestroy->flag false->停止子线程
			stopService(service);
			editor.putBoolean(Config.KEY_START_LOCK_SERVICE, false);
		}
		editor.commit();
	}
}
