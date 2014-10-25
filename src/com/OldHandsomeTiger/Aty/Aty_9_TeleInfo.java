package com.OldHandsomeTiger.Aty;

import java.util.List;

import com.OldHandsomeTiger.adapter.TeleInfoAdapter;
import com.OldHandsomeTiger.tigersafe.R;
import com.OldHandsomeTiger.util.TeleInfo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Aty_9_TeleInfo extends Activity {
	private ListView  lv_TeleInfo;
	private List<String> PhoneInfos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tele_info_list);
		lv_TeleInfo=(ListView) findViewById(R.id.lv_TeleInfo);
		PhoneInfos=TeleInfo.getPhoneInfo(Aty_9_TeleInfo.this, PhoneInfos);
		ListAdapter adapter=new TeleInfoAdapter(Aty_9_TeleInfo.this, PhoneInfos);
		lv_TeleInfo.setAdapter(adapter);
	}
}
