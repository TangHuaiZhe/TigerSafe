package com.OldHandsomeTiger.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.OldHandsomeTiger.domain.TaskInfo;
import com.OldHandsomeTiger.tigersafe.R;

public class TaskManagerAdapter extends BaseAdapter {

	private List<TaskInfo> taskInfos;
	private Context context;
	private static TextView tv_app_name;
	private static TextView tv_app_memory_size;
	private static ImageView iv_app_icon;

	private List<TaskInfo> userTaskInfo;
	public List<TaskInfo> getUserTaskInfo() {
		return userTaskInfo;
	}

	public List<TaskInfo> getSystemTaskInfo() {
		return systemTaskInfo;
	}

	private List<TaskInfo> systemTaskInfo;

	public TaskManagerAdapter(Context context, List<TaskInfo> taskInfos) {
		this.context = context;
		this.taskInfos = taskInfos;

		userTaskInfo = new ArrayList<TaskInfo>();
		systemTaskInfo = new ArrayList<TaskInfo>();

		//分类用户和系统任务
		for (TaskInfo taskInfo : taskInfos)
			if (taskInfo.isSystemapp()) {
				systemTaskInfo.add(taskInfo);
			} else {
				userTaskInfo.add(taskInfo);
			}
	}

	@Override
	public int getCount() {
		return taskInfos.size()+2;
	}

	@Override
	public Object getItem(int position) {
		if(position==0)
		{
			return 0;
		}else if (position<=userTaskInfo.size()) {
			return userTaskInfo.get(position-1);
		}else if (position==userTaskInfo.size()+1) {
			return position;
		}else if (position<=taskInfos.size()+2) {
			return  systemTaskInfo.get((position)-userTaskInfo.size()-2);
		}else {
			return  position;
		}
	}

	@Override
	public long getItemId(int position) {
		if(position==0)
		{
			return 0;
		}else if (position<=userTaskInfo.size()) {
			return position-1;
		}else if (position==userTaskInfo.size()+1) {
			return position;
		}else if (position<=taskInfos.size()+2) {
			return  (position-userTaskInfo.size()-2);
		}else {
			return  position;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		if(position==0)
		{
			TextView tv_userApp=new TextView(context);
			tv_userApp.setTextColor(Color.WHITE);
			tv_userApp.setText("用户进程："+userTaskInfo.size()+"个");
			return tv_userApp;
		}else if (position<=userTaskInfo.size()) {
			int currentPositon=position-1;
			view = View.inflate(context, R.layout.task_manager_item, null);
			tv_app_name = (TextView) view.findViewById(R.id.tv_app_name);
			tv_app_memory_size = (TextView) view
					.findViewById(R.id.tv_app_memory_size);
			iv_app_icon = (ImageView) view.findViewById(R.id.iv_app_icon);
			TaskInfo taskInfo = userTaskInfo.get(currentPositon);
			tv_app_name.setText(taskInfo.getAppname());
			tv_app_memory_size.setText(taskInfo.getMemorysize());
			iv_app_icon.setImageDrawable(taskInfo.getAppicon());

			return view;		
		}else if (position==userTaskInfo.size()+1) {
			TextView tv_userApp=new TextView(context);
			tv_userApp.setTextColor(Color.WHITE);
			tv_userApp.setText("系统进程："+systemTaskInfo.size()+"个");
			return tv_userApp;
		}else if (position<=taskInfos.size()+2) {
			int currentPositon=position-userTaskInfo.size()-2;
			view = View.inflate(context, R.layout.task_manager_item, null);
			tv_app_name = (TextView) view.findViewById(R.id.tv_app_name);
			tv_app_memory_size = (TextView) view
					.findViewById(R.id.tv_app_memory_size);
			iv_app_icon = (ImageView) view.findViewById(R.id.iv_app_icon);

			TaskInfo taskInfo = systemTaskInfo.get(currentPositon);
			tv_app_name.setText(taskInfo.getAppname());
			tv_app_memory_size.setText(taskInfo.getMemorysize());
			iv_app_icon.setImageDrawable(taskInfo.getAppicon());

			return view;		
		}else {
			return  null;
		}
		
		
	
//		if (convertView == null) {
//			view = View.inflate(context, R.layout.task_manager_item, null);
//		} else {
//			view = convertView;
//		}
//
//		tv_app_name = (TextView) view.findViewById(R.id.tv_app_name);
//		tv_app_memory_size = (TextView) view
//				.findViewById(R.id.tv_app_memory_size);
//		iv_app_icon = (ImageView) view.findViewById(R.id.iv_app_icon);
//
//		TaskInfo taskInfo = taskInfos.get(position);
//		tv_app_name.setText(taskInfo.getAppname());
//		tv_app_memory_size.setText(taskInfo.getMemorysize());
//		iv_app_icon.setImageDrawable(taskInfo.getAppicon());
//
//		return view;
//	}
}
}