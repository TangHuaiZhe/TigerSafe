package com.OldHandsomeTiger.domain;

import android.graphics.drawable.Drawable;
/**
 * APP的信息服务类
 * @author tang
 *
 */
public class AppInfo {

	private String AppName;
	private Drawable AppIcon;
	private String AppPackage;
	private boolean isSystemApp;
	private boolean isLocked;

	
	
	public boolean isLocked() {
		return isLocked;
	}

	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}



	public String getAppName() {
		return AppName;
	}

	public void setAppName(String appName) {
		AppName = appName;
	}

	public Drawable getAppIcon() {
		return AppIcon;
	}

	public void setAppIcon(Drawable appIcon) {
		AppIcon = appIcon;
	}

	public String getAppPackage() {
		return AppPackage;
	}

	public void setAppPackage(String appPackage) {
		AppPackage = appPackage;
	}

	public boolean isSystemApp() {
		return isSystemApp;
	}

	public void setSystemApp(boolean isSystemApp) {
		this.isSystemApp = isSystemApp;
	}

}
