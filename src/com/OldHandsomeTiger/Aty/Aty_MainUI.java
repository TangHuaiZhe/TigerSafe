package com.OldHandsomeTiger.Aty;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.OldHandsomeTiger.adapter.MainAdapter;
import com.OldHandsomeTiger.receiver.LockScreenReceiver;
import com.OldHandsomeTiger.tigersafe.R;

public class Aty_MainUI extends Activity {
	GridView gv;
	MainAdapter mainAdapte;
	int endCount;//用于两次返回才推出程序的计数器
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_ui);
		gv = (GridView) findViewById(R.id.gv_main);
		mainAdapte = new MainAdapter(this);
		gv.setAdapter(mainAdapte);
		
		endCount = 0;
		
		//因为bug的原因。不能再XML中定义这个监听锁屏事件，所以只能放在这里注册……
		//好蛋疼的感觉……
		//哪里去回收？unregisterReceiver
		IntentFilter filter=new IntentFilter();
		filter.addAction("android.intent.action.SCREEN_OFF");
		filter.setPriority(1000);
		/**注册锁屏广播*/
		LockScreenReceiver receiver=new LockScreenReceiver();
		registerReceiver(receiver,filter);

		
		
		
		
		gv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent;
				
				switch (position) {
				case 0:
					// 进入手机防盗界面
					intent=new Intent(Aty_MainUI.this, Aty_0_TelProLost.class);
					startActivity(intent);
					break;
					
				case 1:
					// 进入手机防盗界面
					intent=new Intent(Aty_MainUI.this, Aty_1_BlackNumManage.class);
					startActivity(intent);
					
					break;
				case 2:
					// 进入手机防盗界面
					intent=new Intent(Aty_MainUI.this, Aty_2_AppManage.class);
					startActivity(intent);
					
					break;
				case 3:
					// 进入手机防盗界面
					intent=new Intent(Aty_MainUI.this, Aty_3_TaskManage.class);
					startActivity(intent);
					
					break;
					
					
				case 4:
					// 进入手机防盗界面
					intent=new Intent(Aty_MainUI.this, Aty_4_TrafficView.class);
					startActivity(intent);
					
					break;
					
				case  7:
					intent=new Intent(Aty_MainUI.this, Aty_7_AdvanceTool.class);
					startActivity(intent);
					break;
					
				case 8:
					intent=new Intent(Aty_MainUI.this, Aty_8_SettingCenter.class);
					startActivity(intent);
					break;
					
				case 9:
					intent=new Intent(Aty_MainUI.this,Aty_9_TeleInfo.class);
					startActivity(intent);
					break;
				case 10:
					intent=new Intent(Aty_MainUI.this,Aty_9_tuLing.class);
					startActivity(intent);
					break;
				default:
					break;
				}
			}
		});
	}

	@Override
	public void finish() {
		endCount++;
		if (endCount == 1) {
			Toast.makeText(this, "再按一次返回键退出应用程序", Toast.LENGTH_SHORT).show();
		} else if (endCount >= 2) {
			super.finish(); // 不要直接finish。。会递归调用的小伙子……
		}
	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// 实现连续两次退出按钮才会让程序退出
		endCount = 0;// 拦截触摸事件，侦测到屏幕触摸事件，把之前记录的返回事件计算清零。
		return super.dispatchTouchEvent(ev);
	}
}
