package com.OldHandsomeTiger.service;

import java.util.ArrayList;
import java.util.List;

import com.OldHandsomeTiger.Aty.Aty_7_2_LockScreen;
import com.OldHandsomeTiger.db.dao.Dao_AppLock;
import com.OldHandsomeTiger.domain.AppInfo;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.KeyguardManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Loader.ForceLoadContentObserver;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class Service_WatchDog extends Service {

	protected static final String TAG = "Service_WatchDog";
	private static final String uriString = "content://com.OldHandsomeTiger.AppProtectProvider";
	private List<AppInfo> appInfos;
	private Dao_AppLock Dao_AppLock;
	private ActivityManager manager;
	private boolean flag;
	private Intent lockappintent;
	private List<String> StopAppProtect_PackNames;
	private Mybinder mybinder;
	List<String> LockedPackNames;

	// 返回的mybinder对象，会传递给绑定服务的Ibinder对象
	@Override
	public IBinder onBind(Intent intent) {
		return mybinder;
	}

	private class Mybinder extends Binder implements IService {

		// 开启保护，从忽略名单中移除APP
		@Override
		public void AppProtectStart(String packname) {
			if (StopAppProtect_PackNames.contains(packname)) {
				StopAppProtect_PackNames.remove(packname);
			}
		}

		// 关闭保护，添加到忽略名单
		@Override
		public void AppProtectStop(String packname) {
			StopAppProtect_PackNames.add(packname);
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();

		getContentResolver().registerContentObserver(Uri.parse(uriString),
				true, new myContentObserver(new Handler()));

		final KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
		
		mybinder = new Mybinder();
		flag = true;
		appInfos = new ArrayList<AppInfo>();
		StopAppProtect_PackNames = new ArrayList<String>();
		lockappintent = new Intent(this, Aty_7_2_LockScreen.class);
		// 服务是不存在任务栈的 要在服务里面开启activity的话 必须添加这样一个flag
		lockappintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// 得到数据库中，所有被锁定的APP的包名
		Dao_AppLock = new Dao_AppLock(this);
		LockedPackNames = Dao_AppLock.getAllApps();

		manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

		new Thread() {
			public void run() {
				while (flag) {
					
					if(keyguardManager.inKeyguardRestrictedInputMode()){
						StopAppProtect_PackNames.clear();
						Log.i(TAG, "进入锁屏……清空忽略名单");
					}
					
					
					
					// 得到最近启动的APP的包名
					List<RunningTaskInfo> RunningTaskInfos = manager
							.getRunningTasks(1);
					RunningTaskInfo RunningTaskInfo = RunningTaskInfos.get(0);
					String packageName = RunningTaskInfo.topActivity
							.getPackageName();
					Log.i(TAG, "当前运行的APP" + packageName);
					// 判断是否在被锁定名单内
					if (LockedPackNames.contains(packageName)) {
						// 判断是否在忽略名单内
						if (StopAppProtect_PackNames.contains(packageName)) {

							try {
								sleep(500);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							continue;
						}

						Log.i(TAG, "需要锁定" + packageName);
						// todo 弹出来一个锁定的界面 让用户输入密码，传递当前需要锁定的packageName
						lockappintent.putExtra("packname", packageName);
						startActivity(lockappintent);
					} else {
						// 放行

					}

					try {
						sleep(500);
					} catch (InterruptedException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
				}
			}

		}.start();

	}

	private class myContentObserver extends ContentObserver {

		public myContentObserver(Handler handler) {
			super(handler);
		}

		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			LockedPackNames = Dao_AppLock.getAllApps();
			Log.i(TAG, "onchange------------------------------------------");
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		flag = false;
	}

}
