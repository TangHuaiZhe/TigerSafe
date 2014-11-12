package com.OldHandsomeTiger.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
/**
 * 网络状态工具类
 * @author tang
 *
 */
public class NetStatusUtil {

	/**
	 * 是否有网络连接
	 * @param context
	 * @return
	 */
	public static  boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 是否有wifi连接
	 * @param context
	 * @return
	 */
	public  static boolean isWifiConnected(Context context) {  
		    if (context != null) {  
		         ConnectivityManager mConnectivityManager = (ConnectivityManager) context  
		                .getSystemService(Context.CONNECTIVITY_SERVICE);  
		        NetworkInfo mWiFiNetworkInfo = mConnectivityManager  
		              .getNetworkInfo(ConnectivityManager.TYPE_WIFI);  
		        if (mWiFiNetworkInfo != null) {  
		              return mWiFiNetworkInfo.isAvailable();  
		        }  
		    }  
		    return false;  
		 }  

		 
		 /**
		  * 是否有移动网络连接
		  * @param context
		  * @return
		  */
		 public static boolean isMobileConnected(Context context) {  
		      if (context != null) {  
		        ConnectivityManager mConnectivityManager = (ConnectivityManager) context  
		                 .getSystemService(Context.CONNECTIVITY_SERVICE);  
		         NetworkInfo mMobileNetworkInfo = mConnectivityManager  
		                 .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);  
		         if (mMobileNetworkInfo != null) {  
		             return mMobileNetworkInfo.isAvailable();  
		       }  
		    }  
		   return false;  
		 }  

		 
		 public static int getConnectedType(Context context) {  
		   if (context != null) {  
		          ConnectivityManager mConnectivityManager = (ConnectivityManager) context  
		                 .getSystemService(Context.CONNECTIVITY_SERVICE);  
		      NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();  
		        if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {  
		            return mNetworkInfo.getType();  
		        }  
		     }  
		     return -1;  
		 }  


}
