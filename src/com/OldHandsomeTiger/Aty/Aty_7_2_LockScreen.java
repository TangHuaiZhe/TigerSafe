package com.OldHandsomeTiger.Aty;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.OldHandsomeTiger.service.IService;
import com.OldHandsomeTiger.service.Service_WatchDog;
import com.OldHandsomeTiger.tigersafe.R;
import com.OldHandsomeTiger.util.SecurityUtil;


/**
 * 由看门狗服务开启此activity
 * 用户屏蔽被锁定程序的界面
 * 如果密码输入正确，不再显示此界面，通过绑定服务(bindService)，调用服务的方法实现
 * 
 * 想过直接finish掉这个Acitivity  ，但是由于看门狗服务（while true）的存在，仍然会调用起这个Activity
 * 所以只能通过这个Activity去和看门狗服务交互，才能避免这个情况的发生
 * 而如果想和一个服务交互，只能是通过bindservice的方式
 * 所以才会搞的这么复杂……
 * 
 */
public class Aty_7_2_LockScreen extends Activity {
	public static final String TAG = "Aty_7_2_LockScreen";
	private ImageView iv_app_lock_pwd_icon;
	private TextView tv_app_lock_pwd_name;
	private EditText et_app_lock_pwd;
	private SharedPreferences sp;
	private String realpwd;
	private IService iService;
	private MyConn myconn;
	private String packname;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_lock_pwd);
		myconn = new MyConn();
		Intent intent = new Intent(this,Service_WatchDog.class);
		bindService(intent, myconn, BIND_AUTO_CREATE);
		sp =getSharedPreferences(com.OldHandsomeTiger.util.Config.KEY_CONFIG, MODE_PRIVATE);
		realpwd = sp.getString(com.OldHandsomeTiger.util.Config.KEY_PASSWORD_MD5, "");
		//得到需要锁定的packageName，由看门狗服务传递过来
		//通过packageName获取APP信息，完成界面初始化
		//如果用户密码输入正确，通知看门狗将此APP加入忽略名单
		packname = getIntent().getStringExtra("packname");
		iv_app_lock_pwd_icon = (ImageView) this
				.findViewById(R.id.iv_app_lock_pwd_icon);
		tv_app_lock_pwd_name = (TextView) this
				.findViewById(R.id.tv_app_lock_pwd_name);
		et_app_lock_pwd = (EditText) this.findViewById(R.id.et_app_lock_pwd);
		// 完成界面的初始化 
		ApplicationInfo appinfo;
		try {
			appinfo = getPackageManager().getPackageInfo(packname, 0).applicationInfo;
			Drawable appicon = appinfo.loadIcon(getPackageManager());
			String appname = appinfo.loadLabel(getPackageManager()).toString();
			iv_app_lock_pwd_icon.setImageDrawable(appicon);
			tv_app_lock_pwd_name.setText(appname);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 确定按钮对应的点击事件
	 */
	public void confirm(View view) {
		//得到用户输入的密码
		String password = et_app_lock_pwd.getText().toString().trim();
		if(TextUtils.isEmpty(password)){
			Toast.makeText(this, "密码不能为空", 1).show();
			return ;
		}else{
			if(SecurityUtil.strToMD5(password).equals(realpwd)){
				
				// 通知看门狗 临时的取消对这个程序的保护
				iService.AppProtectStop(packname);
				Log.i(TAG, "临时取消保护:"+packname);
				finish();
			}else{
				Toast.makeText(this, "密码错误", 1).show();
				return ;
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(event.getKeyCode()==KeyEvent.KEYCODE_BACK){
			return true; // 阻止按键事件继续向下分发
		}
		return super.onKeyDown(keyCode, event);
	}
	
	//定义一个类实现ServiceConnection接口，用于bindservice方法的参数
	private class MyConn implements ServiceConnection{

		public void onServiceConnected(ComponentName name, IBinder service) {
			//IBinder对象service，就是看门狗服务返回的mybinder对象，继承Ibinder实现Iservice接口。
			iService = (IService)service;
			Log.i(TAG, "onServiceConnected------------------------------------");
			
		}

		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			
		}
	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unbindService(myconn);
	}
}
