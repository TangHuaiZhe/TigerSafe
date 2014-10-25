package com.OldHandsomeTiger.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.nfc.cardemulation.OffHostApduService;
import android.util.Log;

import com.OldHandsomeTiger.domain.AppInfo;

public class AppInfo_Service {
	private static final String TAG="AppInfo_Service";
	private List<AppInfo> appInfos;
	private List<AppInfo> LockedAppInfos;
	private Context context;
	private PackageManager packageManager;

	public AppInfo_Service(Context context) {
		this.context = context;
		packageManager = context.getPackageManager();
	}

	public List<AppInfo> getAppInfos() {
		appInfos = new ArrayList<>();
		AppInfo appInfo;
		List<PackageInfo> PackageInfos = packageManager
				.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
		for (PackageInfo packageInfo : PackageInfos) {
			ApplicationInfo applicationInfo = packageInfo.applicationInfo;
			Drawable drawable = applicationInfo.loadIcon(packageManager);
			appInfo = new AppInfo();
			appInfo.setAppPackage(packageInfo.packageName);
			// appInfo.setAppIcon(applicationInfo.icon);
			appInfo.setAppIcon(drawable);
//			appInfo.setAppName(applicationInfo.name);  这里不行
//			因为在XML中没有定义这个熟属性，应用的名字也是定义在 android:label="@string/app_name"
			String appname = applicationInfo.loadLabel(packageManager).toString();
			appInfo.setAppName(appname);
			Log.i(TAG, appname);

			boolean isSystemApp = filterApp(applicationInfo);
			if (isSystemApp) {
				appInfo.setSystemApp(false);

			} else {
				appInfo.setSystemApp(true);
			}

			// appInfo.setAppName(packageInfo);

			appInfos.add(appInfo);
		}

		return appInfos;
	}
//	
//	public List<AppInfo> getLockedAppInfos() {
//		LockedAppInfos = new ArrayList<>();
//		AppInfo appInfo;
//		List<PackageInfo> PackageInfos = packageManager
//				.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
//		for (PackageInfo packageInfo : PackageInfos) {
//			ApplicationInfo applicationInfo = packageInfo.applicationInfo;
//			Drawable drawable = applicationInfo.loadIcon(packageManager);
//			appInfo = new AppInfo();
//			appInfo.setAppPackage(packageInfo.packageName);
//			// appInfo.setAppIcon(applicationInfo.icon);
//			appInfo.setAppIcon(drawable);
////			appInfo.setAppName(applicationInfo.name);  这里不行
////			因为在XML中没有定义这个熟属性，应用的名字也是定义在 android:label="@string/app_name"
//			String appname = applicationInfo.loadLabel(packageManager).toString();
//			appInfo.setAppName(appname);
//			Log.i(TAG, appname);
//
//
//			// appInfo.setAppName(packageInfo);
//			if(appInfo.isLocked()){
//				LockedAppInfos.add(appInfo);
//			}
//		}
//
//		return appInfos;
//	}

	/**
	 * 判断某个应用程序是 不是三方的应用程序
	 * 
	 * @param info
	 * @return  true为第三方应用程序！
	 * 
	 */
	public boolean filterApp(ApplicationInfo info) {
		if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
			return true;
		} else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
			return true;
		}
		return false;
	}

}
