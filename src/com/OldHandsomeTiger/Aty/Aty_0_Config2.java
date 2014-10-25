package com.OldHandsomeTiger.Aty;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.OldHandsomeTiger.tigersafe.R;
import com.OldHandsomeTiger.util.Config;

public class Aty_0_Config2 extends Activity {
	protected static final String TAG = "Aty_0_Config2";
	ImageView imv_bindSim;
	private TelephonyManager tm;
	private SharedPreferences sp;
	Button bt_pre;
	Button bt_next;
	String sim_serial;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup2config);
		imv_bindSim = (ImageView) findViewById(R.id.iv_bind_sim);

		tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		sp = getSharedPreferences(Config.KEY_CONFIG, Context.MODE_PRIVATE);
		sim_serial = sp.getString(com.OldHandsomeTiger.util.Config.KEY_SIM_SERIAL, "");
		System.out.println("__________"+sim_serial);
		if ("".equals(sim_serial)) {
			imv_bindSim.setImageResource(R.drawable.switch_off_normal);
		} else {
			imv_bindSim.setImageResource(R.drawable.switch_on_normal);
		}
		imv_bindSim.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(sim_serial.equals(""))
				{
					sim_serial = tm.getSimSerialNumber();
					Log.i(TAG, "本机的Sim卡序列号是："+sim_serial);
					Editor editor = sp.edit();
					editor.putString(Config.KEY_SIM_SERIAL, sim_serial);
					editor.commit();
					Log.i(TAG, "成功绑定本机Sim卡序列号");
					imv_bindSim.setImageResource(R.drawable.switch_on_normal);
				}else {
					sim_serial ="";
					Editor editor = sp.edit();
					editor.putString(Config.KEY_SIM_SERIAL, "");
					editor.commit();
					Log.i(TAG, "解除绑定本机Sim卡序列号");
					imv_bindSim.setImageResource(R.drawable.switch_off_normal);
				}
				
			}
		});

	}

	public void pre(View v) {
		Intent intent = new Intent(this, Aty_0_Config1.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_enter, R.anim.tran_exit);
	}

	public void next(View v) {
		Intent intent = new Intent(this, Aty_0_Config3.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.alpha_enter, R.anim.alpha_exit);
	}

}
