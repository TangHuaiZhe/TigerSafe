package com.OldHandsomeTiger.util;

public class Config {

	public static final String Server_addtress = "http://192.168.0.100:8088/";
	// 如果不加http，测试的时候会报protocol not found!
	public static final String UpdateInfo_address = "update.xml";
	public static final String UpdateAPK_address = "TigerSafe.apk";
	public static final String UpdateAPK_NAME = "TigerSafe.apk";
	public static final String UPDATE_ADDRESS_DB = "address.db";
	public static final String KEY_CONFIG="config";

	public static final String KEY_PASSWORD_MD5 = "password";
	public static final String KEY_SIM_SERIAL="sim_serial";
	public static final String KEY_IS_SETUP="is_setup";
	public static final String KEY_IS_PROTECTED="is_protected";
	public static final String KEY_SAFE_NUMBER="safe_number";
	public static final String KEY_LOCATIN="location";
	public static final String COMMAND_LOCATION="#*location*#";
	public static final String COMMAND_DELETE="#*delete*#";
	public static final String COMMAND_LOCKSCREEN="#*lockscreen*#";
	public static final String COMMAND_ALARM="#*alarm*#";
	
	
	
	public static final String KEY_LAST_X="lastX";
	public static final String KEY_LAST_Y="lastY";
	
	public static final String KEY_DISPLAY_LOCATION="display_location";
	public static final String KEY_START_LOCK_SERVICE = "start lock service";
	public static final String KEY_KILL_PROCESS = "killprocess";
	public static final String KEY_PACKAGENAME = "packageName";
	public static final String KEY_APP_NAME = "AppName";
	
	public static final String urlString="http://www.tuling123.com/openapi/api";
	public static final String KEY_AUTOREAD = "auto_read";
}
