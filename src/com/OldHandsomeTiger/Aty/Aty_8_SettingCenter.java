package com.OldHandsomeTiger.Aty;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.OldHandsomeTiger.tigersafe.R;
import com.OldHandsomeTiger.util.Config;

public class Aty_8_SettingCenter extends Activity implements OnClickListener, OnCheckedChangeListener {

	private CheckBox Cb_killProcess;
	private SharedPreferences sp;
	private Editor editor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty8_setting);
		Cb_killProcess=(CheckBox) findViewById(R.id.Cb_killProcess);
		Cb_killProcess.setOnCheckedChangeListener(this);
		sp=getSharedPreferences(Config.KEY_CONFIG, MODE_PRIVATE);
		editor=sp.edit();
		boolean AutoKillOpened=sp.getBoolean(Config.KEY_KILL_PROCESS, false);
		if(AutoKillOpened){
			Cb_killProcess.setChecked(true);
		}else {
			Cb_killProcess.setChecked(false);
		}
		
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO 自动生成的方法存根
		if(isChecked){
			editor.putBoolean(Config.KEY_KILL_PROCESS, true);
			
		}else {
			editor.putBoolean(Config.KEY_KILL_PROCESS, false);
		}
		editor.commit();
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		
	}


}
