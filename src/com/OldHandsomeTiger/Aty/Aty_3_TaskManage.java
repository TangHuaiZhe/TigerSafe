package com.OldHandsomeTiger.Aty;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.OldHandsomeTiger.adapter.TaskManagerAdapter;
import com.OldHandsomeTiger.domain.TaskInfo;
import com.OldHandsomeTiger.engine.TaskInfo_Service;
import com.OldHandsomeTiger.tigersafe.R;
import com.OldHandsomeTiger.util.Config;
import com.OldHandsomeTiger.util.TeleInfo;
import com.OldHandsomeTiger.util.TextFormater;

public class Aty_3_TaskManage extends Activity implements OnClickListener,
		OnItemClickListener, OnItemLongClickListener {

	private static final String TAG = "Aty_3_TaskManage";
	private TextView tv_task_count;
	private TextView tv_avail_memory;
	private Button bt_killTask;

	private ActivityManager activityManager;
	private ListView lv_task_manager;
	private TaskManagerAdapter adapter;
	private List<TaskInfo> taskInfos;
	private TaskInfo_Service taskInfo_Service;
	private LinearLayout ll_task_manager_loading;
	private List<TaskInfo> userTaskInfos;
	private List<TaskInfo> systemTaskInfos;

	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			if (msg.what == 101) {
				tv_task_count.setText("进程数：" + getPocessCount());
				//全部内存
				String totalMem= TeleInfo.getTotalMemory(Aty_3_TaskManage.this);
				tv_avail_memory.setText("可用内存：" + getAvailableMemory()+"/"+totalMem);
				ll_task_manager_loading.setVisibility(View.GONE);

				lv_task_manager.setAdapter(adapter);
				systemTaskInfos = adapter.getSystemTaskInfo();
				userTaskInfos = adapter.getUserTaskInfo();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// boolean flag = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.task_manager);
		activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		ll_task_manager_loading = (LinearLayout) findViewById(R.id.ll_task_manager_loading);
		ll_task_manager_loading.setVisibility(View.VISIBLE);
		// if (flag) {
		// getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
		// R.layout.task_manager_title);
		// }
		// 注释掉的代码没有问题，但是当前的主题下已经定义了一个TiTle，会出现冲突
		// 可以尝试修改主题下的Title解决此问题，留待后续。
		tv_task_count = (TextView) findViewById(R.id.tv_task_count);
		tv_avail_memory = (TextView) findViewById(R.id.tv_avail_memory);
		lv_task_manager = (ListView) findViewById(R.id.lv_task_manager);
		lv_task_manager.setOnItemClickListener(this);
		lv_task_manager.setOnItemLongClickListener(this);
		bt_killTask = (Button) findViewById(R.id.bt_killTask);
		bt_killTask.setOnClickListener(this);
		tv_task_count.setText("进程数：" + getPocessCount());
		Toast.makeText(Aty_3_TaskManage.this, "长按进程项获取进程详细信息", Toast.LENGTH_SHORT).show();
//		tv_avail_memory.setText("可用内存：" + getAvailableMemory());

		fillData();

	}

	private String getPocessCount() {
		List<RunningAppProcessInfo> runningAppProcessInfos = activityManager
				.getRunningAppProcesses();
		int count = runningAppProcessInfos.size();
		return count + "";
	}

	private String getAvailableMemory() {
		MemoryInfo MemoryoutInfo = new MemoryInfo();
		activityManager.getMemoryInfo(MemoryoutInfo);
		long size = MemoryoutInfo.availMem;
		return TextFormater.getDataSize(size);
	}

	private void fillData() {
		new Thread() {
			public void run() {
				taskInfo_Service = new TaskInfo_Service(Aty_3_TaskManage.this);
				taskInfos = new ArrayList<TaskInfo>();
				activityManager = (ActivityManager) Aty_3_TaskManage.this
						.getSystemService(Context.ACTIVITY_SERVICE);
				taskInfos = taskInfo_Service.getAllTaskInfos(activityManager
						.getRunningAppProcesses());
				adapter = new TaskManagerAdapter(Aty_3_TaskManage.this,
						taskInfos);
			
			
				
				handler.sendEmptyMessage(101);
			}
		}.start();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.bt_killTask:
			killProcess();

			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// 忽略两个TextView的点击事件！
		Object object = lv_task_manager.getItemAtPosition(position);
		Log.i(TAG, "点击的位置是：" + position);
		CheckBox cb_task_checked = (CheckBox) view
				.findViewById(R.id.cb_task_checked);
		if (object instanceof TaskInfo) {
			TaskInfo taskInfo = (TaskInfo) object;
			if (taskInfo.isIschecked()) {
				taskInfo.setIschecked(false);
				cb_task_checked.setChecked(false);
			} else {
				taskInfo.setIschecked(true);
				cb_task_checked.setChecked(true);
			}

		}

	}

	private void killProcess() {
		int a = 0;
		int memory = 0;
		// 想清楚为什么要用iterator遍历而不是for循环，因为涉及到对集合的读取同时还要进行修改
		// 并且只能使用iterator对象的remove()进行修改，直接修改集合就会ConcurrentModificationException
		// 如果用for each循环（只能读不能写，否则就是 ConcurrentModificationException异常）
		Iterator<TaskInfo> iterator = taskInfos.iterator();
		while (iterator.hasNext()) {
			TaskInfo taskInfo = iterator.next();
			if (taskInfo.isIschecked()) {
				String packageName = taskInfo.getPackname();
				activityManager.killBackgroundProcesses(packageName);

				android.os.Debug.MemoryInfo[] memoryInfos = activityManager
						.getProcessMemoryInfo(new int[] { taskInfo.getPid() });
				Log.i(TAG, "当前被kill的程序" + taskInfo.getAppname());
				Log.i(TAG,
						"当前被Kill的程序所占内存："
								+ memoryInfos[0].getTotalPrivateDirty());

				memory = memoryInfos[0].getTotalPrivateDirty() + memory;
				a++;
				iterator.remove();
				// memory=Integer.valueOf(taskInfo.getMemorysize())+memory;

			}

		}
		adapter = new TaskManagerAdapter(Aty_3_TaskManage.this, taskInfos);
		lv_task_manager.setAdapter(adapter);

		tv_task_count.setText("进程数：" + getPocessCount());
		tv_avail_memory.setText("剩余内存:" + getAvailableMemory());
		Toast.makeText(
				Aty_3_TaskManage.this,
				"杀死了" + a + "个进程，总计回收内存："
						+ TextFormater.getDataSize(memory * 1024),
				Toast.LENGTH_SHORT).show();

	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		
		Object object = lv_task_manager.getItemAtPosition(position);
		Log.i(TAG, "点击的位置是：" + position);
		if (object instanceof TaskInfo) {
			TaskInfo taskInfo = (TaskInfo) object;
			Intent intent=new Intent(Aty_3_TaskManage.this,Aty_3_AppPermissionDetail.class);
			intent.putExtra(Config.KEY_APP_NAME,taskInfo.getAppname());
			intent.putExtra(Config.KEY_PACKAGENAME, taskInfo.getPackname());
			startActivity(intent);
		}
		return false;
	}

}
