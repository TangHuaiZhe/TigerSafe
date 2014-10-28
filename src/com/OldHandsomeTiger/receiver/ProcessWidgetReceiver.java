package com.OldHandsomeTiger.receiver;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import com.OldHandsomeTiger.service.Service_UpdateWidget;
/**
 * 杀死进程的小部件
 * @author tang
 *
 */
public class ProcessWidgetReceiver extends AppWidgetProvider {
	
	Intent intent;

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		intent=new Intent(context,Service_UpdateWidget.class);
		context.startService(intent);
		System.out.println("onUpdate^^^^^^^^^^^");
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
		intent = new Intent(context,Service_UpdateWidget.class);
		context.stopService(intent);
		System.out.println("onDelete^^^^^^^^^^^");
	}

	@Override
	public void onEnabled(Context context) {
		// TODO 自动生成的方法存根
		super.onEnabled(context);
		System.out.println("onEnable^^^^^^^^^^^");
	}

	@Override
	public void onDisabled(Context context) {
		// TODO 自动生成的方法存根
		System.out.println("onDisable^^^^^^^^^^");
		super.onDisabled(context);
	}
	
}
