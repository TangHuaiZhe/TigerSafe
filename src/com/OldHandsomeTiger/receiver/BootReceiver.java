package com.OldHandsomeTiger.receiver;

import com.OldHandsomeTiger.util.Config;

import android.R.anim;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Telephony;
import android.telephony.TelephonyManager;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class BootReceiver extends BroadcastReceiver {
	private static final String TAG = "BootReceiver";
	private SharedPreferences sp;
	private boolean isProtected;
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "检测到重启事件，准备检查Sim卡序列号是否一致");
		sp=context.getSharedPreferences(Config.KEY_CONFIG, Context.MODE_PRIVATE);
		isProtected=sp.getBoolean(Config.KEY_IS_PROTECTED, false);
		if(isProtected==true)
		{
			String SafeNum=sp.getString(Config.KEY_SAFE_NUMBER, "");
			if(SafeNum!="")
			{
				Log.i(TAG, "当前缓存的安全号码："+SafeNum);
				String SimSerial=sp.getString(Config.KEY_SIM_SERIAL, "");
				TelephonyManager tlm=(TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
				String currentSimSerial=tlm.getSimSerialNumber();
				if(currentSimSerial=="")
				{
					Log.i("BootReceiver", "可能在模拟器中，无法读取当前的SIM卡信息");
					return;
				}
				if(currentSimSerial!=SafeNum)
				{
					//Sim卡发生改变
					@SuppressWarnings("deprecation")
					android.telephony.SmsManager smsManager= android.telephony.SmsManager.getDefault();
					smsManager.sendTextMessage(SafeNum, null, "手机被盗啦……当前的SIm卡序列号是"+SimSerial, null, null);
					
					
					
				}
				
				
			}else {
				Log.w(TAG, "当前存储的电话号码为空");
			}
			
		}
		
		
		
	}

}
