package com.OldHandsomeTiger.test;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.test.AndroidTestCase;

import com.OldHandsomeTiger.domain.TaskInfo;
import com.OldHandsomeTiger.engine.TaskInfo_Service;

public class TestTaskInfo_service extends AndroidTestCase {

//	public void testTaskInfo(){
//		TaskInfo_Service taskInfo_Service=new TaskInfo_Service(getContext());
//		List<TaskInfo> taskInfos=new ArrayList<TaskInfo>();	
//		
//		ActivityManager	activityManager = (ActivityManager) getContext()
//				.getSystemService(Context.ACTIVITY_SERVICE);
//		
//		
//		taskInfos=	taskInfo_Service.getAllTaskInfos(activityManager.getRunningAppProcesses());
//		System.out.println("进程数："+taskInfos.size());
//		for(TaskInfo taskInfo:taskInfos)
//		{
//			System.out.println("PID："+taskInfo.getPid());
//			System.out.println("AppName："+taskInfo.getAppname());
//			System.out.println("内存占用："+taskInfo.getMemorysize());
//		}
//	}
	
	String ss="我是一只小小鸟，想要飞啊";
	public void testString(){
		String s2=ss.substring(1);
		System.out.println(s2);
		
	}
	
}
