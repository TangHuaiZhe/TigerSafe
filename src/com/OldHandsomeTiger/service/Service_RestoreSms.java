package com.OldHandsomeTiger.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import com.OldHandsomeTiger.Aty.Aty_7_AdvanceTool;
import com.OldHandsomeTiger.domain.SmsInfo;

public class Service_RestoreSms extends Service {

	protected static final String TAG = "Service_RestoreSms";

	@Override
	public IBinder onBind(Intent intent) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		/*
		 * 读取备份的XML文件 解析XML文件 写入到Sms中
		 */
		new Thread() {
			public void run() {
				try {
					ProgressDialog pdDialog = null;
					File file = new File("/sdcard/smsBackup.xml");
					FileInputStream fiStream = new FileInputStream(file);
					ContentValues values = null;// 这种数据类型用来存储被ContentResolver处理的一组数据

					XmlPullParser pullParser = Xml.newPullParser();
					pullParser.setInput(fiStream, "UTF-8"); // 为Pull解释器设置要解析的XML数据
					int event = pullParser.getEventType();
					while (event != XmlPullParser.END_DOCUMENT) {
						switch (event) {
						case XmlPullParser.START_TAG:
							if ("sms".equals(pullParser.getName())) {
								Log.i(TAG, "new sms");
								values = new ContentValues();
							} else if ("address".equals(pullParser.getName())) {
								Log.i(TAG, "address");
								values.put("address", pullParser.nextText());
							} else if ("_id".equals(pullParser.getName())) {
								values.put("_id", pullParser.nextText());
							} else if ("date".equals(pullParser.getName())) {
								Log.i(TAG, "date");
								values.put("date", pullParser.nextText());
							} else if ("type".equals(pullParser.getName())) {
								Log.i(TAG, "type");
								values.put("type", pullParser.nextText());
							} else if ("body".equals(pullParser.getName())) {
								Log.i(TAG, "body");
								values.put("body", pullParser.nextText());
							}
							break;
						case XmlPullParser.END_TAG:
							if ("sms".equals(pullParser.getName())) {
								Log.i(TAG, "End sms");
								ContentResolver resolver =getContentResolver();
								Uri uri = Uri.parse("content://sms/");
//								Log.i(TAG, (String) values.get("body"));
								resolver.insert(uri, values);
								values = null;
							}
							break;
						}
						event = pullParser.next();
						Log.i(TAG, "Next");
					}

					Looper.prepare();
					Toast.makeText(getApplicationContext(), "还原联系人完成！",
							Toast.LENGTH_SHORT).show();
					Looper.loop();

				} catch (XmlPullParserException | IOException e) {
					e.printStackTrace();
					Looper.prepare();
					Toast.makeText(getApplicationContext(),
							"还原备份出错……" + e.toString(), Toast.LENGTH_LONG).show();
					Looper.loop();
				}
			}
		}.start();

	}
}
