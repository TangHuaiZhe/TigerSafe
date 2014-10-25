package com.OldHandsomeTiger.domain;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

/*
 *更新信息类
 * 服务器返回
 */
public class UpdateInfo {
	private String version;
	private String description;
	private String apkUrl;

	public UpdateInfo(String vesion, String description, String apkUrl) {
		this.version = vesion;
		this.description = description;
		this.apkUrl = apkUrl;
	}

	public UpdateInfo() {
		super();
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getApkUrl() {
		return apkUrl;
	}

	public void setApkUrl(String apkUrl) {
		this.apkUrl = apkUrl;
	}

	public static  UpdateInfo getUpdateInfo() throws IOException {
		String path = com.OldHandsomeTiger.util.Config.Server_addtress
				+ com.OldHandsomeTiger.util.Config.UpdateInfo_address;
		URL updateUrl = new URL(path);
		HttpURLConnection connection = (HttpURLConnection) updateUrl
				.openConnection();
		connection.setConnectTimeout(2000);
		connection.setRequestMethod("GET");
		InputStream is = connection.getInputStream();
		// BufferedReader bf=new BufferedReader(new InputStreamReader(is));
		// StringBuilder sb=new StringBuilder();
		// String line;
		// while((line=bf.readLine())!=null)
		// {
		// sb.append(line);
		// System.out.println(sb);
		// }

		UpdateInfo updateInfo = null;
		try {
			//通过输入流解析XML数据，返回updateInfo对象
			updateInfo = ReadUpdateInfo(is);
			System.out.println(updateInfo.getVersion());
			System.out.println(updateInfo.getDescription());
			System.out.println(updateInfo.getApkUrl());
			
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return updateInfo;
	}

	private static UpdateInfo ReadUpdateInfo(InputStream is)
			throws XmlPullParserException, IOException {
		XmlPullParser parser = Xml.newPullParser();
		UpdateInfo info = new UpdateInfo();
		parser.setInput(is, "utf-8");
		// 定位到XML文件的开头
		int type = parser.getEventType();
		while (type != XmlPullParser.END_DOCUMENT) {
			switch (type) {
			case XmlPullParser.START_TAG:
				if ("version".equals(parser.getName())) {
					String version = parser.nextText();
					info.setVersion(version);
				} else if ("description".equals(parser.getName())) {
					String description = parser.nextText();
					info.setDescription(description);
				} else if ("apkurl".equals(parser.getName())) {
					String apkurl = parser.nextText();
					info.setApkUrl(apkurl);
				}

				break;
			}

			type = parser.next();
		}
		return info;
	}

}
