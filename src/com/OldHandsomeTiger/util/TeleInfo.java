package com.OldHandsomeTiger.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;

public class TeleInfo {

	/**
	 * 获取手机信息
	 */
	public static List<String> getPhoneInfo(Context context,
			List<String> PhoneInfos) {
		PhoneInfos = new ArrayList<String>();
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String TeleBrand = "手机品牌："+android.os.Build.BRAND;// 手机品牌
		String TeleModel = "手机型号："+android.os.Build.MODEL; // 手机型号
		String TeleSDK = "SDK："+android.os.Build.VERSION.SDK_INT; // 手机型号
		String TeleDeviceID = "设备ID："+tm.getDeviceId();
		String TeleImsi ="IMSI：" +tm.getSubscriberId();// the unique subscriber ID
		String TeleNumer = "手机号码："+tm.getLine1Number(); // 手机号码
		String TeleserviceName ="运营商："+ tm.getSimOperatorName(); // 运营商
		// tvPhoneInfo.setText("品牌: " + mtyb + "\n" + "型号: " + mtype + "\n" +
		// "版本: Android " + android.os.Build.VERSION.RELEASE + "\n" + "IMEI: " +
		// imei
		// + "\n" + "IMSI: " + imsi + "\n" + "手机号码: " + numer + "\n" + "运营商: " +
		// serviceName + "\n");
		PhoneInfos.add(TeleBrand);
		PhoneInfos.add(TeleModel);
		PhoneInfos.add(TeleSDK);
		PhoneInfos.add(TeleDeviceID);
		PhoneInfos.add(TeleImsi);
		PhoneInfos.add(TeleNumer);
		PhoneInfos.add(TeleserviceName);
		return PhoneInfos;

	}

	/**
	 * 获取手机内存大小，
	 * 通过读取/proc/meminfo下的信息实现
	 * @return
	 */
	public static String getTotalMemory(Context context) {
		String str1 = "/proc/meminfo";// 系统内存信息文件
		String str2;
		String[] arrayOfString;
		long initial_memory = 0;
		try {
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(
					localFileReader, 8192);
			str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小
			// 格式是这样的：MemTotal: 1031128 kB
			// 使用空字符串作为关键字，分解字符串！可以得到3个字符串……
			arrayOfString = str2.split("\\s+");
			for (String num : arrayOfString) {
				Log.i(str2, num + "\t");
			}
			initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
			localBufferedReader.close();
		} catch (IOException e) {
		}
		return Formatter.formatFileSize(context, initial_memory);// Byte转换为KB或者MB，内存大小规格化
	}

}
