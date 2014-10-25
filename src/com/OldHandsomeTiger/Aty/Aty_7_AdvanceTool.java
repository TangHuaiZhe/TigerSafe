package com.OldHandsomeTiger.Aty;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.OldHandsomeTiger.engine.DownLoadApkThread;
import com.OldHandsomeTiger.engine.SmsInfo_Service;
import com.OldHandsomeTiger.service.Service_ShowCall_Location;
import com.OldHandsomeTiger.service.Service_BackupSms;
import com.OldHandsomeTiger.service.Service_RestoreSms;
import com.OldHandsomeTiger.tigersafe.R;
import com.OldHandsomeTiger.util.Config;

public class Aty_7_AdvanceTool extends Activity implements OnClickListener {

	protected static final int ERROR = 10;
	protected static final int SUCCESS = 11;
	private static final String TAG = "Aty_7_AdvanceTool";
	private TextView tv_query;
	private ProgressDialog pd;
	private TextView tv_atools_address;
	private TextView tv_atools_select_bg;
	private Intent serviceintent;
	private CheckBox cb_atools_address;
	private TextView tv_atools_change_location;

	private TextView tv_smsBackup;
	private TextView tv_smsRestore;
	private TextView tv_lockApp;
	private Button bt_clearBackupSms;
	private TextView tv_CommonNumQuery;

	private SharedPreferences sp;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ERROR:
				Toast.makeText(getApplicationContext(), "下载数据库失败", 0).show();
				break;
			case SUCCESS:
				Toast.makeText(getApplicationContext(), "下载数据库成功", 0).show();
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.atools);
		tv_atools_change_location = (TextView) this
				.findViewById(R.id.tv_atools_change_location);
		tv_atools_change_location.setOnClickListener(this);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		final Editor editor = sp.edit();
		tv_atools_select_bg = (TextView) this
				.findViewById(R.id.tv_atools_select_bg);
		tv_atools_select_bg.setOnClickListener(this);
		tv_query = (TextView) this.findViewById(R.id.tv_atools_query);
		cb_atools_address = (CheckBox) this
				.findViewById(R.id.cb_atools_address);
		serviceintent = new Intent(this, Service_ShowCall_Location.class);
		tv_atools_address = (TextView) this
				.findViewById(R.id.tv_atools_address);
		tv_CommonNumQuery = (TextView) findViewById(R.id.tv_CommonNumQuery);
		tv_CommonNumQuery.setOnClickListener(this);
		tv_smsBackup = (TextView) findViewById(R.id.tv_smsBackup);
		tv_smsBackup.setOnClickListener(this);// 如果不注册事件监听器，怎么可能触发单击事件
		tv_smsRestore = (TextView) findViewById(R.id.tv_smsRestore);
		tv_smsRestore.setOnClickListener(this);

		bt_clearBackupSms = (Button) findViewById(R.id.bt_clearSmsBackup);
		bt_clearBackupSms.setOnClickListener(this);
		tv_lockApp = (TextView) findViewById(R.id.tv_lockApp);
		tv_lockApp.setOnClickListener(this);
		boolean display_location = sp.getBoolean(Config.KEY_DISPLAY_LOCATION,
				false);
		if (display_location) {
			cb_atools_address.setChecked(true);
			startService(serviceintent);
			tv_atools_address.setTextColor(Color.WHITE);
			tv_atools_address.setText("号码归属地服务已经开启");
		} else {
			cb_atools_address.setChecked(false);
			tv_atools_address.setTextColor(Color.RED);
			tv_atools_address.setText("号码归属地服务未开启");
			if (isServiceRunning(this, "Srvc_Address")) {
				stopService(serviceintent);
			}
		}

		cb_atools_address
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							startService(serviceintent);
							tv_atools_address.setTextColor(Color.WHITE);
							tv_atools_address.setText("号码归属地服务已经开启");

							editor.putBoolean(Config.KEY_DISPLAY_LOCATION, true);
							editor.commit();

						} else {
							stopService(serviceintent);
							tv_atools_address.setTextColor(Color.RED);
							tv_atools_address.setText("号码归属地服务未开启");

							editor.putBoolean(Config.KEY_DISPLAY_LOCATION,
									false);
							editor.commit();

						}

					}
				});

		tv_query.setOnClickListener(this);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_atools_query:
			// 判断来电归属地数据库是否存在
			if (isDBexist()) {
				Intent intent = new Intent(this, Aty_7_1_QueryNumber.class);
				startActivity(intent);
			} else {
				// 提示用户下载数据库
				pd = new ProgressDialog(this);
				pd.setMessage("正在下载数据库");
				pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				pd.show();
				// 下载数据库
				new Thread() {
					@Override
					public void run() {
						String Urlpath = Config.Server_addtress
								+ Config.UPDATE_ADDRESS_DB;
						String filepath = "/sdcard/address.db";
						try {
							new DownLoadApkThread().DownLoadFile(Urlpath, pd,
									Aty_7_AdvanceTool.this);
							pd.dismiss();
							Message msg = new Message();
							msg.what = SUCCESS;
							handler.sendMessage(msg);
						} catch (Exception e) {
							e.printStackTrace();
							pd.dismiss();
							Message msg = new Message();
							msg.what = ERROR;
							handler.sendMessage(msg);
						}
					}
				}.start();

			}

			break;
		case R.id.tv_atools_select_bg:
			AlertDialog.Builder builder = new Builder(this);
			builder.setTitle("归属地提示显示风格");
			String[] items = new String[] { "半透明", "活力橙", "苹果绿" };
			builder.setSingleChoiceItems(items, 0,
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							Editor editor = sp.edit();
							editor.putInt("background", which);
							editor.commit();
						}
					});

			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {

						}
					});
			builder.create().show();
			break;

		case R.id.tv_atools_change_location:
			// 更改来电归属地的显示位置

			Intent intent = new Intent(this, Aty_7_1_EditShowLocation.class);
			startActivity(intent);

			break;
		case R.id.tv_smsBackup:
			// 备份短信
			// 因为是非常耗时的操作，需要使用服务来新开一条线程来做
			// 如果直接新开一个线程，容易被Kill
			Intent intent2 = new Intent(this, Service_BackupSms.class);
			startService(intent2);
			break;
		case R.id.tv_smsRestore:
			// 读取备份的xml文件
			// 解析文件里面的信息
			// 插入到短信应用里面
			// final ProgressDialog pd = new ProgressDialog(this);
			// pd.setCancelable(false);
			// pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			// pd.setMessage("正在还原短信");
			// pd.show();
			// final SmsInfo_Service smsInfo_Service = new
			// SmsInfo_Service(this);
			// new Thread(){
			// @Override
			// public void run() {
			// try {
			// smsInfo_Service.restoreSms("/sdcard/smsBackup.xml",pd);
			// pd.dismiss();
			// Looper.prepare();
			// Toast.makeText(getApplicationContext(), "还原成功", 0).show();
			// Looper.loop();
			// } catch (Exception e) {
			// e.printStackTrace();
			// pd.dismiss();
			// Looper.prepare();
			// Toast.makeText(getApplicationContext(), "还原失败", 0).show();
			// Looper.loop();
			// }
			// }
			// }.start();
			Toast.makeText(this, "准备恢复备份的短信", Toast.LENGTH_SHORT).show();
			Intent intent3 = new Intent(this, Service_RestoreSms.class);
			startService(intent3);
			break;

		case R.id.bt_clearSmsBackup:

			AlertDialog.Builder Clear_builder = new AlertDialog.Builder(this);
			Clear_builder.setMessage("确定删除备份的短消息吗？此操作不可恢复");
			Clear_builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							File backupFile = new File("/sdcard/smsBackup.xml");
							if (backupFile.exists()) {
								if (backupFile.delete()) {
									Toast.makeText(Aty_7_AdvanceTool.this,
											"删除成功", Toast.LENGTH_SHORT).show();
								} else {

									Toast.makeText(Aty_7_AdvanceTool.this,
											"删除失败……", Toast.LENGTH_SHORT)
											.show();
								}
							} else {
								Toast.makeText(Aty_7_AdvanceTool.this,
										"备份文件不存在", Toast.LENGTH_SHORT).show();
							}
						}
					});
			Clear_builder.setNegativeButton("取消", null);
			Clear_builder.create().show();
			break;

		case R.id.tv_lockApp:
			Intent intent4 = new Intent(Aty_7_AdvanceTool.this,
					Aty_7_2_LockApp.class);
			startActivity(intent4);
			break;

		case R.id.tv_CommonNumQuery:
			Intent intent5 = new Intent(Aty_7_AdvanceTool.this,
					Aty_7_CommonNumQuery.class);
			startActivity(intent5);
			break;
		}

	}

	/**
	 * 判断数据库是否存在
	 * 
	 * @return
	 */
	public boolean isDBexist() {
		File file = new File("/sdcard/address.db");
		return file.exists();

	}

	
	
	//isServiceRunning
	public static boolean isServiceRunning(Context mContext, String className) {

		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager
				.getRunningServices(30);

		if (serviceList.size() <= 0) {
			return false;
		}

		for (int i = 0; i < serviceList.size(); i++) {
			Log.i(TAG, serviceList.get(i).service.getClassName());
			if (serviceList.get(i).service.getClassName().equals(className) == true) {
				isRunning = true;
				break;
			}
		}
		return isRunning;
	}

}
