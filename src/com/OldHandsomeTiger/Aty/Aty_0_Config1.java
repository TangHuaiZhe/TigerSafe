package com.OldHandsomeTiger.Aty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.OldHandsomeTiger.tigersafe.R;

public class Aty_0_Config1  extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup1config);
		ImageView imageView=(ImageView) findViewById(R.id.Setup1ImageView);
		imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Toast.makeText(Aty_0_Config1.this, "请你不要抚摸本帅~我很淫荡~", Toast.LENGTH_SHORT).show();
				
			}
		});
		
		Button bt=(Button)findViewById(R.id.Setup1next);
				bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intent=new Intent(Aty_0_Config1.this, Aty_0_Config2.class);
				startActivity(intent);
				finish();
				
				
			}
		});
		
		
		
		
		
	}
}
