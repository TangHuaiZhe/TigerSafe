package com.OldHandsomeTiger.db.dao;

import java.util.ArrayList;
import java.util.List;

import com.OldHandsomeTiger.db.DB_blackNum;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

public class DAO_blackNum {
	private static final String TAG = "DAO_blackNum";
	private Context context;
	private DB_blackNum db_blackNum;

	public DAO_blackNum(Context context) {
		this.context = context;
		this.db_blackNum = new DB_blackNum(context, "blacknumber.db", null, 1);
	}

	/**
	 * 
	 * 查找
	 * 
	 * @param number
	 * @return
	 */

	public boolean find(String number) {
		boolean result = false;
		SQLiteDatabase db = db_blackNum.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery(
					"select  number from  blackNum where number=?",
					new String[] { number });
			while (cursor.moveToNext()) {
				return true;
			}
			cursor.close();
			db.close();
		}
		return result;
	}

	/**
	 * 添加操作
	 * 
	 * 
	 */
	public void add(String number) {
		if (find(number)) {
		Log.i(TAG, "已经存在相同号码，无需添加");
			return;
		}
		SQLiteDatabase db = db_blackNum.getWritableDatabase();
		if (db.isOpen()) {
			db.execSQL("insert into blackNum (number) values (?)",
					new Object[] { number });
			db.close();
		}
	}

	public void delete(String number) {
		if (!find(number)) {
			return;
		}
		SQLiteDatabase db = db_blackNum.getWritableDatabase();
		if (db.isOpen()) {
			db.execSQL("delete from blackNum where number= ?",
					new Object[] { number });
			db.close();
		}
	}

	/**
	 * 
	 * 更新操作
	 * 
	 * @param OldNumber
	 * @param NewNumber
	 */

	public void update(String OldNumber, String NewNumber) {
		if (!find(OldNumber)) {
			return;
		}
		SQLiteDatabase db = db_blackNum.getWritableDatabase();
		if (db.isOpen()) {
			db.execSQL("update  blackNum  set number =? where number= ?",
					new Object[] { NewNumber, OldNumber });
			db.close();
		}
	}

	public List<String> findAll() {

		SQLiteDatabase db = db_blackNum.getWritableDatabase();
		List<String> numbers = new ArrayList<>();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select number from blackNum", null);

			while(cursor.moveToNext()) {
				String number = cursor.getString(0);
				System.out.println(number);
				numbers.add(number);

			}

			db.close();
		}
		return numbers;
	}

}
