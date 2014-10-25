package com.OldHandsomeTiger.domain;

import android.graphics.drawable.Drawable;

public class TaskInfo {

	private String appname;
	private Drawable appicon;
	private int pid; // process id 进程的id
	private String memorysize;
	private boolean ischecked;
	private String packname;

	private boolean systemapp;
	
	
	
	public boolean isSystemapp() {
		return systemapp;
	}

	public void setSystemapp(boolean systemapp) {
		this.systemapp = systemapp;
	}

	public String getPackname() {
		return packname;
	}

	public void setPackname(String packname) {
		this.packname = packname;
	}

	public String getAppname() {
		return appname;
	}

	public void setAppname(String appname) {
		this.appname = appname;
	}

	public Drawable getAppicon() {
		return appicon;
	}

	public void setAppicon(Drawable appicon) {
		this.appicon = appicon;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getMemorysize() {
		return memorysize;
	}

	public void setMemorysize(String  memorysize) {
		this.memorysize = memorysize;
	}

	public boolean isIschecked() {
		return ischecked;
	}

	public void setIschecked(boolean ischecked) {
		this.ischecked = ischecked;
	}

}
