package com.OldHandsomeTiger.Aty;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.OldHandsomeTiger.tigersafe.R;

public class Aty_7_CommonNumQuery extends Activity {

	private SQLiteDatabase db;
	private ExpandableListView eplv_commonNumQuery;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_num_query);
		eplv_commonNumQuery = (ExpandableListView) findViewById(R.id.eplv_commonNumQuery);

		AssetManager assetManager = getAssets();
		copyDbFromAssets(assetManager);
		db=SQLiteDatabase.openDatabase("/sdcard/commonnum.db", null, SQLiteDatabase.OPEN_READONLY);
		ExpandableListAdapter  adapter=new com.OldHandsomeTiger.adapter.ExpandableListAdapter(Aty_7_CommonNumQuery.this,db);
		 eplv_commonNumQuery.setAdapter(adapter);

	}
	
	@Override
	protected void onDestroy() {
		// TODO 自动生成的方法存根
		super.onDestroy();
		db.close();
	}

	private void copyDbFromAssets(AssetManager assetManager) {
		
		File file = new File("/sdcard/commonnum.db");
		if(file.exists()){
			return;
		}
		try {
			InputStream is = assetManager.open("commonnum.db");
			file = new File("/sdcard/commonnum.db");
			FileOutputStream fos = new FileOutputStream(file);
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				fos.write(buffer);
			}
			fos.flush();
			fos.close();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
