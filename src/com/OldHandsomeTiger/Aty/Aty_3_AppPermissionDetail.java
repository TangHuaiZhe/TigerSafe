package com.OldHandsomeTiger.Aty;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import com.OldHandsomeTiger.tigersafe.R;
import com.OldHandsomeTiger.util.Config;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ScrollView;
import android.widget.TextView;

public class Aty_3_AppPermissionDetail extends Activity {
	private TextView tv_app_detail_name;
	private TextView tv_app_detail_packname;
	private ScrollView sv_app_detail;
	private String packName;
	private String AppName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.app_detail);
		tv_app_detail_name=(TextView) findViewById(R.id.tv_app_detail_name);
		tv_app_detail_packname=(TextView) findViewById(R.id.tv_app_detail_packname);
		sv_app_detail=(ScrollView) findViewById(R.id.sv_app_detail);
		packName=getIntent().getStringExtra(Config.KEY_PACKAGENAME);
		AppName=getIntent().getStringExtra(Config.KEY_APP_NAME);
		tv_app_detail_packname.setText(packName);
		tv_app_detail_name.setText(AppName);
//		System.out.println("__________当前点击的应用程序是："+AppName);
		
		
		
		
		
		//通过反射得到被隐藏的类，构造器，方法。并构造对象执行方法
		try {
			//首先，你得知道需要调用的全路径类名
			Class clazz = getClass().getClassLoader().loadClass(
					"android.widget.AppSecurityPermissions");
			//其次，你的知道需要得到哪个构造方法。如果不确定，先得到全部构造方法，遍历一下。
			Constructor constructor=clazz.getConstructor(new Class[]{Context.class,String.class});
			//然后，你就可以拿着这个构造方法去new Instance了
			Object object=constructor.newInstance(new Object[]{this,packName});
			//再然后，你就可以拿着这个对象去得到方法，当然你得知道方法的名字和参数，不知道的话，遍历吧
			Method method=clazz.getMethod("getPermissionsView", new Class[]{});
			//最后，执行方法吧，传入的是之前构造的对象，还有参数
			View view=(View) method.invoke(object, new Object[]{});
			//于是，一切水到渠成……
			sv_app_detail.addView(view);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

}
