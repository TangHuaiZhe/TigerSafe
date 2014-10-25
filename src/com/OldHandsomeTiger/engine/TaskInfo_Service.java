package com.OldHandsomeTiger.engine;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.OldHandsomeTiger.domain.TaskInfo;
import com.OldHandsomeTiger.tigersafe.R;
import com.OldHandsomeTiger.util.TextFormater;

public class TaskInfo_Service {

	private static final String TAG = "TaskInfo_Service";
	private ActivityManager activityManager;
	private TaskInfo taskInfo;
	private List<TaskInfo> taskInfos;
	private Context context;
	private PackageManager packageManager;// 通过包名获得ApplicationInfo的必由之路……
	private ApplicationInfo appInfo;// 可以得到很多信息

	public TaskInfo_Service(Context context) {
		this.context = context;
		packageManager = context.getPackageManager();
		activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
	}

	public List<TaskInfo> getAllTaskInfos(
			List<RunningAppProcessInfo> runningAppProcessInfos) {

		taskInfos = new ArrayList<TaskInfo>();
		for (RunningAppProcessInfo Info : runningAppProcessInfos) {

			Log.i(TAG, "进程名：" + Info.processName);
			taskInfo = new TaskInfo();
			int id = Info.pid;
			taskInfo.setPid(id);

			String packName = Info.processName;
			taskInfo.setPackname(packName);

			appInfo = null;
			try {
				appInfo = packageManager.getPackageInfo(packName, 0).applicationInfo;

				if (filterApp(appInfo)) {
					taskInfo.setSystemapp(false);
					Log.i(TAG, "第三方程序");

				} else {
					taskInfo.setSystemapp(true);
					Log.i(TAG, "系统程序");

				}

				Drawable AppIcon = appInfo.loadIcon(packageManager);
				taskInfo.setAppicon(AppIcon);

				String appName = (String) appInfo.loadLabel(packageManager);
				taskInfo.setAppname(appName);

				android.os.Debug.MemoryInfo[] memoryInfo = activityManager
						.getProcessMemoryInfo(new int[] { id });
				int AppMemoryInfo = memoryInfo[0].getTotalPrivateDirty();// 返回当前应用程序占用的内存
																			// 单位KB
				String MemoryInfo = TextFormater.getKBDataSize(AppMemoryInfo);
				taskInfo.setMemorysize(MemoryInfo);

				taskInfos.add(taskInfo);
				taskInfo = null;
			} catch (NameNotFoundException e) {
				// 会有一个名字为System的进程无法按照包名得到包信息类
				e.printStackTrace();
				taskInfo = new TaskInfo();
				String packname = Info.processName;
				taskInfo.setPackname(packname);
				taskInfo.setAppname(packname);
				Drawable appicon = context.getResources().getDrawable(
						R.drawable.ic_launcher_android);
				taskInfo.setAppicon(appicon);
				int pid = Info.pid;
				taskInfo.setPid(pid);
				taskInfo.setSystemapp(true);
				android.os.Debug.MemoryInfo[] memoryinfos = activityManager
						.getProcessMemoryInfo(new int[] { pid });
				int memorysize = memoryinfos[0].getTotalPrivateDirty();
				String MemoryInfo = TextFormater.getKBDataSize(memorysize);
				taskInfo.setMemorysize(MemoryInfo);
				taskInfos.add(taskInfo);
				taskInfo = null;
			}

		}

		return taskInfos;
	}

	public boolean filterApp(ApplicationInfo info) {
		if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
			return true;
		} else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
			return true;
		}
		return false;
	}
}
