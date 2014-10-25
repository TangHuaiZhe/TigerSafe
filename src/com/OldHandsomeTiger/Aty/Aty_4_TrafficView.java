package com.OldHandsomeTiger.Aty;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.OldHandsomeTiger.adapter.TrafficAdapter;
import com.OldHandsomeTiger.tigersafe.R;
import com.OldHandsomeTiger.util.TextFormater;

public class Aty_4_TrafficView   extends   Activity{

	private TextView tv_mobile_total;
	private TextView tv_wifi_total;
	private ListView lv_content;
	private String mobiletraffic;
	private String wifitraffic;
	private PackageManager pm;
	private  TrafficAdapter adapter;
	
	private Timer timer;
	private TimerTask task;
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			adapter.notifyDataSetChanged();
			setTotalDataInfo();
		}
	};
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		pm = getPackageManager();
		setContentView(R.layout.traffic_manager);
		tv_mobile_total = (TextView) this.findViewById(R.id.tv_mobile_total);
		tv_wifi_total = (TextView) this.findViewById(R.id.tv_wifi_total);
		lv_content = (ListView) this.findViewById(R.id.content);
		
		setTotalDataInfo();
		
		 adapter = new TrafficAdapter(Aty_4_TrafficView.this);
		 
		 //给ListView添加一个固定的头行信息
		   lv_content.addHeaderView(View.inflate(this, R.layout.traffic_title, null));
		   lv_content.setAdapter(adapter);
		
		
	}
	
	private void setTotalDataInfo() {
		long mobilerx = TrafficStats.getMobileRxBytes();
		long mobiletx = TrafficStats.getMobileTxBytes();
		
		long mobiletotal = mobilerx+mobiletx;
		
		mobiletraffic = (TextFormater.getDataSize(mobiletotal));
		tv_mobile_total.setText(mobiletraffic);
		
		 long totalrx = TrafficStats.getTotalRxBytes();
		 long toatltx = TrafficStats.getTotalTxBytes();
		 
		 long total = toatltx+ totalrx;
		 
		 
		 long wifitotal = total-mobiletotal;
		 
		 wifitraffic =  (TextFormater.getDataSize(wifitotal));
		 tv_wifi_total.setText(wifitraffic);
	}
	

	@Override
	protected void onStart() {
		timer = new Timer();
		task = new TimerTask() {
			@Override
			public void run() {
				// 发送一个消息给主线程,不要去NEW一个消息出来，避免内存溢出
				//obtain方法是从消息池中取出消息
				Message msg = Message.obtain();
				handler.sendMessage(msg);
			}
		};
		timer.schedule(task, 2000, 5000);
		super.onStart();
	}
	


	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		timer.cancel();
		timer =null;
		task = null;
	}

	
}
