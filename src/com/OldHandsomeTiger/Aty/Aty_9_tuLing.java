package com.OldHandsomeTiger.Aty;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.OldHandsomeTiger.net.AsyncNetRequest;
import com.OldHandsomeTiger.net.GetDataListener;
import com.OldHandsomeTiger.tigersafe.R;
import com.OldHandsomeTiger.util.Config;

public class Aty_9_tuLing extends Activity {
	private static final String TAG = "Aty_9_tuLing";
	private EditText  ed_info;
	private Button bt_submit;
	private TextView tv_content;
	 ProgressDialog pdDialog;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tuling);
		ed_info=(EditText) findViewById(R.id.ed_info);
		tv_content=(TextView) findViewById(R.id.tv_content);
		bt_submit=(Button) findViewById(R.id.bt_submit);
		
		final GetDataListener listener=new  GetDataListener() {
			@Override
			public String getData(String data) {
				Log.i("Result", "结果是"+data);
				String result = null;
				try {
					JSONObject jsonObject=new JSONObject(data);
					result=jsonObject.getString("text");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				pdDialog.dismiss();
				tv_content.setText(result);
				return result;
			}
		};
		
		bt_submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				 pdDialog=ProgressDialog.show(Aty_9_tuLing.this, "title", "老帅虎想问题中……");
				String content=ed_info.getText().toString();
				if(content==null){
					Toast.makeText(Aty_9_tuLing.this, "问题不能为空~", Toast.LENGTH_SHORT).show();
					return;
				}
				String url = null;
				try {
					/** 需要注意url的拼接，因为是get方式……不能有空格，也不要把之前的符号转化*/
					String urlInfo=URLEncoder.encode(content,"UTF-8");
					url = Config.urlString+"?"+"key=b24230c7a31a7b9b7e80822e68689b94&info="+urlInfo;
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				Log.i(TAG, "当前请求图灵机器人的url是:"+url);
				AsyncNetRequest asyncNetRequest=new AsyncNetRequest(url, listener);
				asyncNetRequest.execute();
			}
		});
		
	}
	
}
