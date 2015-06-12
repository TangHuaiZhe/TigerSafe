package com.OldHandsomeTiger.db;
/*
 * 创建黑名单号码数据库
 * 
 */
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * 黑名单号码数据库 类
 * @author tang
 *
 */
public class DB_blackNum extends SQLiteOpenHelper {
	final String CREATE_TABLE_SQL="create table blackNum(_id integer primary key autoincrement," +
			"number nvarchar(20)," +
			"name nvarchar(30))";

	
	/**
	 * 构造方法
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	public DB_blackNum(Context context, String name, CursorFactory factory,
			int version) {
		super(context, "blacknumber.db", null, 2);
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_SQL);
	}

	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		to do ,升级之后数据的迁移！
	}

}
