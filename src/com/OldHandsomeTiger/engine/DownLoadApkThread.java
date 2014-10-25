package com.OldHandsomeTiger.engine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.OldHandsomeTiger.Aty.Aty_Entry;
import com.OldHandsomeTiger.util.Config;

//下载新版本的APK
public class DownLoadApkThread extends Thread {
	ProgressDialog pd;
	Context context;
	String UrlPath;

	public DownLoadApkThread() {
		super();
	}

	public DownLoadApkThread(String UrlPath, ProgressDialog pd, Context context) {
		this.pd = pd;
		this.UrlPath = UrlPath;
		this.context = context;
	}

	@Override
	public void run() {
		super.run();
		try {
			DownLoadFile(UrlPath, pd, context);
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

		File apkFile = new File("/sdcard/TigerSafe.apk");
		install(apkFile);

	}

	public void install(File file) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		// 获取APK文件的MIME类型
		context.startActivity(intent);
		((Activity) context).finish();
	}

	// 抽象为一个工具类，方便其他场合复用……也方面下载完毕之后，可以执行安装，做成线程就悲剧鸟
	public void DownLoadFile(String UrlPath, ProgressDialog pd, Context context)
			throws IOException, InterruptedException {
		URL url = new URL(UrlPath);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		InputStream is = connection.getInputStream();
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			System.out.println(Environment.getExternalStorageState());
			int subStringStart = UrlPath.lastIndexOf("/")+1;
			String FileName = UrlPath.substring(subStringStart);
			System.out.println("要下载的文件名字是：" + FileName);

			
			FileOutputStream fos = new FileOutputStream("/sdcard/" + FileName);

			byte[] buff = new byte[2048];
			int total = connection.getContentLength();
			pd.setMax(100);
			System.out.println("新版本文件总大小" + total);
			int hasread;
			float sum = 0;
			while ((hasread = is.read(buff)) > 0) {
				fos.write(buff, 0, hasread);
				sum = sum + hasread;
				System.out.println("Sum:" + sum);
				float percent = sum / total * 100f;
				if (total != 0) {
					System.out.println("已经读取了" + percent + "%");
				}
				sleep(5);
				pd.setProgress((int) percent);

			}
			fos.flush();
			is.close();
			pd.dismiss();

		}
	}
}
