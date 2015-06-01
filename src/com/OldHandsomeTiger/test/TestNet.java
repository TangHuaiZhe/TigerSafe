package com.OldHandsomeTiger.test;

import com.OldHandsomeTiger.net.AsyncNetRequest;
import com.OldHandsomeTiger.net.GetDataListener;
import com.OldHandsomeTiger.util.Config;

import android.test.AndroidTestCase;
import android.util.Log;

public class TestNet extends AndroidTestCase {
	
	public void TestAsyncNet(){
		
		String url=Config.urlString+"?"+"key=b24230c7a31a7b9b7e80822e68689b94&info=梁朝伟是谁？";
		GetDataListener listener=new  GetDataListener() {
			@Override
			public String getData(String data) {
				Log.i("Result", "结果是"+data);
				return data;
			}
		};
		AsyncNetRequest asyncNetRequest=new AsyncNetRequest(url, listener);
		asyncNetRequest.execute();
	}
}
