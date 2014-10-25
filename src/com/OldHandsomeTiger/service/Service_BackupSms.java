package com.OldHandsomeTiger.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.xmlpull.v1.XmlSerializer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import com.OldHandsomeTiger.domain.SmsInfo;
import com.OldHandsomeTiger.engine.SmsInfo_Service;

public class Service_BackupSms extends Service {

	protected static final String TAG = "Service_BackupBlackNum";
	private SmsInfo_Service smsInfo_Service;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public void onCreate() {
		Log.i(TAG, "onCreate");
		super.onCreate();
		smsInfo_Service = new SmsInfo_Service(this);
		new Thread() {
			public void run() {
				try {
					Log.i(TAG, "开启备份短信的服务中的子线程……");
					List<SmsInfo> smsinfos = smsInfo_Service.getSmsInfo();
					File file = new File("/sdcard/smsBackup.xml");
					OutputStream out = new FileOutputStream(file);

					XmlSerializer serializer = Xml.newSerializer();
					serializer.setOutput(out, "UTF-8");
					serializer.startDocument("UTF-8", true);
					serializer.startTag(null, "smss");
					
					//总计有多少条短信要备份，还原的时候可以用来显示精度条
					serializer.startTag(null, "count");
					serializer.text(smsinfos.size()+"");
					serializer.endTag(null, "count");
					
					
					for (SmsInfo smsInfo : smsinfos) {
						serializer.startTag(null, "sms");

						serializer.startTag(null, "id");
						serializer.text(smsInfo.getId());
						serializer.endTag(null, "id");

						serializer.startTag(null, "address");
						serializer.text(smsInfo.getAddress());
						serializer.endTag(null, "address");
						
						
						serializer.startTag(null, "date");
						serializer.text(smsInfo.getData());
						serializer.endTag(null, "date");

						serializer.startTag(null, "type");
						serializer.text(smsInfo.getType());
						serializer.endTag(null, "type");

						serializer.startTag(null, "body");
						serializer.text(smsInfo.getBody());
						serializer.endTag(null, "body");

						serializer.endTag(null, "sms");
					}
					serializer.endTag(null, "smss");
					serializer.endDocument();
					out.flush();// 缓冲区的数据写出去
					out.close();

					// 子线程中显示Toast
					Looper.prepare();
					Toast.makeText(getApplicationContext(), "短消息备份完成",
							Toast.LENGTH_SHORT).show();
					Looper.loop();

				} catch (IllegalArgumentException | IllegalStateException
						| IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();

					// 子线程中显示Toast
					Looper.prepare();
					Toast.makeText(getApplicationContext(),
							"短消息备份失败" + e.toString(), Toast.LENGTH_SHORT)
							.show();
					Looper.loop();
				}

			}

		}.start();
	}

}
