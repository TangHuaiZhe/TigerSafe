package com.OldHandsomeTiger.Aty;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.OldHandsomeTiger.tigersafe.R;
import com.OldHandsomeTiger.util.Config;

public class LostProtectedSettingActivity extends Activity implements OnClickListener{

	private static final int MENU_CHANGE_NAME_ID = 0;
	private TextView tv_lost_protected_setting_safe_number;
	private CheckBox cb_lost_protected_setting_protecting;
	private TextView tv_lost_protected_setting_reset;
	private SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences(Config.KEY_CONFIG, Context.MODE_PRIVATE);
		setContentView(R.layout.lost_protected_setting);
		
		tv_lost_protected_setting_safe_number = (TextView) findViewById(R.id.tv_lost_protected_setting_safe_number);
		String safe_number = sp.getString("safe_number", "");
		tv_lost_protected_setting_safe_number.setText(safe_number);
		
		cb_lost_protected_setting_protecting = (CheckBox) findViewById(R.id.cb_lost_protected_setting_protecting);
		boolean isprotected = sp.getBoolean(com.OldHandsomeTiger.util.Config.KEY_IS_PROTECTED, false);
		if(isprotected){
			cb_lost_protected_setting_protecting.setChecked(true);
			cb_lost_protected_setting_protecting.setText("防盗保护已经开启");
		}else{
			cb_lost_protected_setting_protecting.setChecked(false);
			cb_lost_protected_setting_protecting.setText("防盗保护没有开启");
		}
		cb_lost_protected_setting_protecting.setOnClickListener(this);
		
		tv_lost_protected_setting_reset = (TextView) findViewById(R.id.tv_lost_protected_setting_reset);
		tv_lost_protected_setting_reset.setOnClickListener(this);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.tv_lost_protected_setting_reset:
			Intent intent = new Intent(this,Aty_0_Config1.class);
			startActivity(intent);
			finish();
			break;
		case R.id.cb_lost_protected_setting_protecting:
			boolean isprotected = sp.getBoolean(com.OldHandsomeTiger.util.Config.KEY_IS_PROTECTED, false);
			if(isprotected){
				cb_lost_protected_setting_protecting.setChecked(false);
				cb_lost_protected_setting_protecting.setText("防盗保护没有开启");
				Editor editor = sp.edit();
				editor.putBoolean(com.OldHandsomeTiger.util.Config.KEY_IS_PROTECTED, false);
				editor.commit();
			}else{
				cb_lost_protected_setting_protecting.setChecked(true);
				cb_lost_protected_setting_protecting.setText("防盗保护已经开启");
				Editor editor = sp.edit();
				editor.putBoolean(com.OldHandsomeTiger.util.Config.KEY_IS_PROTECTED, true);
				editor.commit();
			}
			break;
		default:
			break;
		}
	}
	
}
	
