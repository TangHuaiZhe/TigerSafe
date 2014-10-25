package com.OldHandsomeTiger.Aty;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.CheckBox;

import com.OldHandsomeTiger.tigersafe.R;

public class Aty_0_Config4 extends Activity implements OnClickListener{

	private SharedPreferences sp;
	private CheckBox cb_setup4_start_protect;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		
		
		setContentView(R.layout.setup4config);
		cb_setup4_start_protect = (CheckBox) findViewById(R.id.cb_setup4_start_protect);
		sp = getSharedPreferences(com.OldHandsomeTiger.util.Config.KEY_CONFIG, Context.MODE_PRIVATE);
		
		cb_setup4_start_protect.setOnClickListener(this);
		boolean isprotected = sp.getBoolean(com.OldHandsomeTiger.util.Config.KEY_IS_PROTECTED, false);
		if(isprotected){
			cb_setup4_start_protect.setChecked(true);
			cb_setup4_start_protect.setText("防盗保护已经开启");
		}else{
			cb_setup4_start_protect.setChecked(false);
			cb_setup4_start_protect.setText("防盗保护没有开启");
		}
	}
	@Override
	protected void onDestroy() {
		// TODO 自动生成的方法存
		System.out.println("设置完成，is——setup的值：");
		System.out.println(sp.getBoolean(com.OldHandsomeTiger.util.Config.KEY_IS_SETUP, false));
		
		System.out.println("设置完成，is——protected的值：");
		System.out.println(sp.getBoolean(com.OldHandsomeTiger.util.Config.KEY_IS_PROTECTED, false));
		super.onDestroy();
		
		
	}
	
	public void pre(View v){
		Intent intent = new Intent(this,Aty_0_Config3.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_enter, R.anim.tran_exit);
	}
	
	public void next(View v){
		//判断防盗保护是否已经开启
		boolean isprotected = sp.getBoolean(com.OldHandsomeTiger.util.Config.KEY_IS_PROTECTED, false);
		if(isprotected){
			Editor editor = sp.edit();
			editor.putBoolean(com.OldHandsomeTiger.util.Config.KEY_IS_SETUP, true);//设置向导完成
			editor.commit();
			startActivity(new Intent(Aty_0_Config4.this,LostProtectedSettingActivity.class));
			finish();
		}else{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("强烈建议");
			builder.setMessage("手机防盗极大的保护了手机的安全，请勾选开启防盗");
			builder.setPositiveButton("开启", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					cb_setup4_start_protect.setChecked(true);
					cb_setup4_start_protect.setText("防盗保护已经开启");
					Editor editor = sp.edit();
					editor.putBoolean(com.OldHandsomeTiger.util.Config.KEY_IS_PROTECTED, true);
					editor.putBoolean(com.OldHandsomeTiger.util.Config.KEY_IS_SETUP, true);//设置向导完成
					editor.commit();
					startActivity(new Intent(Aty_0_Config4.this,LostProtectedSettingActivity.class));
					finish();
				}
			});
			builder.setNegativeButton("放弃", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Editor editor = sp.edit();
					editor.putBoolean(com.OldHandsomeTiger.util.Config.KEY_IS_SETUP, true);//设置向导完成
					editor.commit();
					finish();
				}
			});
			AlertDialog dialog = builder.create();
			dialog.show();
		}
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.cb_setup4_start_protect:
			boolean isprotected = sp.getBoolean(com.OldHandsomeTiger.util.Config.KEY_IS_PROTECTED, false);
			if(isprotected){
				cb_setup4_start_protect.setChecked(false);
				cb_setup4_start_protect.setText("防盗保护没有开启");
				Editor editor = sp.edit();
				editor.putBoolean(com.OldHandsomeTiger.util.Config.KEY_IS_PROTECTED, false);
				editor.commit();
			}else{
				cb_setup4_start_protect.setChecked(true);
				cb_setup4_start_protect.setText("防盗保护已经开启");
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