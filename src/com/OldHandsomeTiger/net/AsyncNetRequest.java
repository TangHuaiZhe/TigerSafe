package com.OldHandsomeTiger.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

public class AsyncNetRequest   extends AsyncTask<String, Void, String>{


	private String url;
	private InputStream is;
	private HttpClient httpClient;
	private HttpGet httpGet;
	private HttpResponse HttpResponse;
	private HttpEntity HttpEntity;
	private GetDataListener listener;
	private StringBuilder sbBuilder;
	
	public AsyncNetRequest(String url,GetDataListener listener) {
		this.url=url;
		this.listener=listener;
	}
	
	
	@Override
	protected String doInBackground(String... params) {
		try {
		System.out.println("请求的url"+url);
		httpClient=new DefaultHttpClient();
		httpGet=new HttpGet(url);
//		httpGet=new HttpGet(URLEncoder.encode(url,"UTF-8"));
		HttpResponse=httpClient.execute(httpGet);
		HttpEntity=HttpResponse.getEntity();
		is=HttpEntity.getContent();
		
		BufferedReader  bfw=new BufferedReader(new InputStreamReader(is));
		String line;
		sbBuilder=new StringBuilder();
		while((line=bfw.readLine())!=null){
			sbBuilder.append(line);
			line=null;
		}
		System.out.println("异步任务输出"+sbBuilder);
		is.close();
		bfw.close();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sbBuilder.toString();
	}
	
	
	@Override
	protected void onPostExecute(String result) {
		listener.getData(result);
		super.onPostExecute(result);
	}

}
