package com.OldHandsomeTiger.receiver;

import com.OldHandsomeTiger.db.dao.Dao_AppLock;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class AppProtectProvider extends ContentProvider {

	private Dao_AppLock dao_AppLock;
	
	private static Uri changeUri=Uri.parse("content://com.OldHandsomeTiger.AppProtectProvider");
			
	
	private static final int INSERT = 0;
	private static final int DELETE = 1;
	private static UriMatcher matcher;
	
	//初始化操作，匹配规则
	static{
		matcher=new UriMatcher(UriMatcher.NO_MATCH);
		matcher.addURI("com.OldHandsomeTiger.AppProtectProvider", "Insert", INSERT);
		matcher.addURI("com.OldHandsomeTiger.AppProtectProvider", "Delete", DELETE);
	}
	
	
	@Override
	public boolean onCreate() {
		dao_AppLock=new Dao_AppLock(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public String getType(Uri uri) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int result =matcher.match(uri);
		if(result==INSERT)
		{
			String packName=(String) values.get("packname");
			//通知Uri数据发生了改变
			getContext().getContentResolver().notifyChange(changeUri, null);
			dao_AppLock.add(packName);
		}else {
			throw new IllegalArgumentException("URI地址无法识别");
		}
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int result =matcher.match(uri);
		if(result==DELETE)
		{
			String packName=selectionArgs[0];
			getContext().getContentResolver().notifyChange(changeUri, null);
			dao_AppLock.delete(packName);
		}else {
			throw new IllegalArgumentException("URI地址无法识别");
		}
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO 自动生成的方法存根
		return 0;
	}

}
