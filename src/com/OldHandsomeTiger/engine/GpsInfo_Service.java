package com.OldHandsomeTiger.engine;

import com.OldHandsomeTiger.util.Config;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class GpsInfo_Service {
	protected static final String TAG = "GpsInfoService";
	private static GpsInfo_Service gpsInfoService;
	private Context context;
	private SharedPreferences sp;
	private static LocationListener locationListener;

	private GpsInfo_Service(Context context) {
		this.context = context;
	}

	public static synchronized GpsInfo_Service getGpsInfoService(Context context) {
		if (gpsInfoService == null) {
			gpsInfoService = new GpsInfo_Service(context);
		}
		return gpsInfoService;
	}

	public String getLocation() {
		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		String bestGpsProvider = getProvider(locationManager);

		locationManager.requestLocationUpdates(bestGpsProvider, 30000, 5,
				getListener());
		sp=context.getSharedPreferences(Config.KEY_CONFIG, Context.MODE_PRIVATE);
		return sp.getString(Config.KEY_LOCATIN, "");

	}

	/**
	 * LocationListener的get方法，为了实现单例模式
	 * 
	 * @return
	 */

	private LocationListener getListener() {
		if (locationListener == null) {
			/**
			 * locationListener的构造方法
			 * 
			 */
			locationListener = new LocationListener() {

				@Override
				public void onStatusChanged(String provider, int status,
						Bundle extras) {

				}

				@Override
				public void onProviderEnabled(String provider) {
				}

				@Override
				public void onProviderDisabled(String provider) {
				}

				@Override
				public void onLocationChanged(Location location) {
					String latitude = String.valueOf(location.getLatitude());
					String Longitude = String.valueOf(location.getLongitude());

					sp = context.getSharedPreferences(
							com.OldHandsomeTiger.util.Config.KEY_CONFIG,
							Context.MODE_PRIVATE);
					Editor editor = sp.edit();
					editor.putString(Config.KEY_LOCATIN, "Latidutu:" + latitude
							+ "###" + "Longitude:" + Longitude);
					Log.i(TAG, latitude+  "__"+Longitude);
					editor.commit();
				}
			};
		}
		return locationListener;
	}

	/**
	 * 
	 * @param locationManager
	 * @return 最好的位置提供者
	 */
	private String getProvider(LocationManager locationManager) {
		// 创建位置提供者的“标准”，也就是过滤器
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setPowerRequirement(Criteria.POWER_HIGH);
		// 返回
		return locationManager.getBestProvider(criteria, false);

	}

}
