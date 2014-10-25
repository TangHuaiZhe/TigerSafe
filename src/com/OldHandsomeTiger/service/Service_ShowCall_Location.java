package com.OldHandsomeTiger.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.OldHandsomeTiger.engine.NumberAddress_Service;
import com.OldHandsomeTiger.tigersafe.R;
import com.OldHandsomeTiger.util.Config;

public class Service_ShowCall_Location extends Service {

	public static final String TAG = "Srvc_Address";
	private PhoneStateListener MyPhoneStateListener;
	private TelephonyManager TelephonyManager;
	private WindowManager windowmanager;
	private View view;
	private SharedPreferences sp;
	private TextView tv;


	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		sp = getSharedPreferences(Config.KEY_CONFIG, Context.MODE_PRIVATE);
		windowmanager = (WindowManager) this.getSystemService(WINDOW_SERVICE);
		MyPhoneStateListener = new MyPhoneStateListener();
		TelephonyManager = (android.telephony.TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		TelephonyManager.listen(MyPhoneStateListener,
				PhoneStateListener.LISTEN_CALL_STATE);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		TelephonyManager.listen(MyPhoneStateListener,
				PhoneStateListener.LISTEN_NONE);
		MyPhoneStateListener = null;

	}

	class MyPhoneStateListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);

			switch (state) {
			case 0:
				Log.i(TAG, "进入无电话状态");
				if (view != null) {
					windowmanager.removeView(view);
					view = null;
				}
				break;

			case 1:// 有电话呼入，未接通
				view.setVisibility(View.VISIBLE);
				Log.i(TAG, "进入电话呼入未接通状态");
				String address = NumberAddress_Service
						.getAddress(incomingNumber);
				showLoaction(address);

				break;

			case 2:// 接通状态，至少有一个接通电话TelephonyManager.CALL_STATE_OFFHOOK:
				Log.i(TAG, "进入电话接通状态");
				if (view != null) {
					windowmanager.removeView(view);
					view = null;
				}

				break;
			default:
				break;
			}
		}

		/**
		 * 在窗体上显示出来位置信息
		 * 
		 * @param address
		 */
		public void showLoaction(String address) {
			view = View.inflate(getApplicationContext(),
					R.layout.show_location, null);
			tv=(TextView) view.findViewById(R.id.tv_location);
			WindowManager.LayoutParams params = new LayoutParams();
			params.height = WindowManager.LayoutParams.WRAP_CONTENT;
			params.width = WindowManager.LayoutParams.WRAP_CONTENT;
			params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
					| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
					| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
			params.format = PixelFormat.TRANSLUCENT;
			params.type = WindowManager.LayoutParams.TYPE_TOAST;
			params.setTitle("Toast");
			params.gravity = Gravity.START | Gravity.TOP;
			params.x=sp.getInt(Config.KEY_LAST_X, 0);
			params.y=sp.getInt(Config.KEY_LAST_Y, 0);
			
			
			
			
//			LinearLayout ll = (LinearLayout) view
//					.findViewById(R.id.ll_location);
//
//			int backgroundid = sp.getInt("background", 0);
//			if (backgroundid == 0) {
//				ll.setBackgroundResource(R.drawable.call_locate_gray);
//			} else if (backgroundid == 1) {
//				ll.setBackgroundResource(R.drawable.call_locate_orange);
//			} else {
//				ll.setBackgroundResource(R.drawable.call_locate_green);
//			}

			final TextView tv = (TextView) view.findViewById(R.id.tv_location);
			tv.setText(address);
			windowmanager.addView(view, params);
		
		}
	}
}