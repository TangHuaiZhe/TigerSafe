package com.OldHandsomeTiger.Aty;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.OldHandsomeTiger.domain.UpdateInfo;
import com.OldHandsomeTiger.engine.DownLoadApkThread;
import com.OldHandsomeTiger.tigersafe.R;
import com.OldHandsomeTiger.util.Config;

public class Aty_Entry extends Activity {
	private UpdateInfo info;
	public static final String TAG = "Aty_Entry";
	private ProgressDialog downloadProgressDialog;
	Aty_Entry aty_Entry;

	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {

			super.handleMessage(msg);
			if (msg.what == 0x123) {
				// 确定存在新版本
				AlertDialog.Builder builder = new Builder(Aty_Entry.this);
				builder.setTitle("升级提醒");
				builder.setMessage(info.getDescription());
				builder.setCancelable(false); // 让用户不能通过后退按钮取消对话框

				builder.setPositiveButton("更新", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Log.i(TAG, "点击了确定更新按钮");
						// 下载客户端方法
						downloadProgressDialog.show();
						Toast.makeText(Aty_Entry.this, "开始自动更新……",
								Toast.LENGTH_SHORT).show();
						String DownLoadPath= Config.Server_addtress + Config.UpdateAPK_address;
						//开始下载线程
						new DownLoadApkThread(DownLoadPath, downloadProgressDialog, aty_Entry)
								.start();
						

					}

				});
				builder.setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						downloadProgressDialog.dismiss();
						Log.i(TAG, "点击了取消更新按钮");
						loadMain();
						finish();
					}
				}).create().show();
				

			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		aty_Entry = this;
		// 取消标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.first);
		LinearLayout first = (LinearLayout) findViewById(R.id.ll_first);
		downloadProgressDialog = new ProgressDialog(this);
		downloadProgressDialog
				.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		// 全屏显示
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// 透明的动画效果
		AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
		aa.setDuration(2000);
		first.startAnimation(aa);
		// 新线程判断是否需要自动更新
		new Thread() {
			public void run() {
				try {
					// isNeedUPdate方法涉及 网络操作，必须放在新线程，否则报NetworkOnMainException
					if (isNeedUPdate()) {
						handler.sendEmptyMessage(0x123);
					} else {
						try {
							sleep(500);
						} catch (InterruptedException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}
						loadMain();
						finish();

					}
				} catch (NameNotFoundException e) {
					loadMain();
					finish();
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
					loadMain();
					finish();
				}
			}
		}.start();

	}

	private boolean isNeedUPdate() throws IOException, NameNotFoundException {
		info = UpdateInfo.getUpdateInfo();
		System.out.println("_______________");
		System.out.println(info.getVersion());
		System.out.println(equals(getVersionName()));
		System.out.println(info.getVersion().equals(getVersionName()));
		System.out.println("_______________");
		// 字符串的比较，序列相等的判断用equals方法，==方法是判断是否同一个引用的……
		if (!(info.getVersion().equals(getVersionName()))) {
			return true;
		}
		return false;
	}

	private void loadMain() {
		Intent intent = new Intent();
		intent.setClass(Aty_Entry.this, Aty_MainUI.class);
		startActivity(intent);
	}



	private String getVersionName() throws NameNotFoundException {
		PackageManager packageManager = getPackageManager();
		PackageInfo info = packageManager.getPackageInfo(getPackageName(), 0);
		System.out.println("当前手机内的版本号是：" + info.versionName);
		return info.versionName;
	}

}
