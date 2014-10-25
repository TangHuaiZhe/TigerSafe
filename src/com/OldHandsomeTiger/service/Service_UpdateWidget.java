package com.OldHandsomeTiger.service;

import java.util.Timer;
import java.util.TimerTask;

import com.OldHandsomeTiger.receiver.LockScreenReceiver;
import com.OldHandsomeTiger.tigersafe.R;
import com.OldHandsomeTiger.util.TaskUtil;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.widget.RemoteViews;

public class Service_UpdateWidget extends Service {

	private Timer timer;
	private TimerTask task;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	

	@Override
	public void onCreate() {
		super.onCreate();
		final AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(getApplicationContext());
		timer = new Timer();
		task = new TimerTask() {

			@Override
			public void run() {
				// 四大组件就是这么获取的
				ComponentName componentName = new ComponentName(
						"com.OldHandsomeTiger.tigersafe",
						"com.OldHandsomeTiger.receiver.ProcessWidgetReceiver");
				RemoteViews views = new RemoteViews(
						"com.OldHandsomeTiger.tigersafe",
						R.layout.process_widget);
				views.setTextViewText(
						R.id.process_count,
						"进程数目"
								+ TaskUtil
										.getProcessCount(getApplicationContext()));
				views.setTextViewText(
						R.id.process_memory,
						"进程数目"
								+ TaskUtil
										.getMemeorySize(getApplicationContext()));
				
				views.setTextColor(R.id.process_count, Color.BLACK);
				views.setTextColor(R.id.process_memory, Color.BLACK);
				
				//使用pendingIntent
				Intent intent = new Intent(Service_UpdateWidget.this,
						LockScreenReceiver.class);
				PendingIntent pendingIntent = PendingIntent.getBroadcast(
						getApplicationContext(), 0, intent, 0);
				views.setOnClickPendingIntent(R.id.btn_clear, pendingIntent);
				// 原理是跨进程通讯,需要在远程桌面进程中去显示这个widget
				appWidgetManager.updateAppWidget(componentName, views);

			}
		};

		 timer.scheduleAtFixedRate(task, 1000, 2000);
	}

	@Override
	public void onDestroy() {
		timer.cancel();
		timer=null;
		task=null;
		super.onDestroy();
	}

}
