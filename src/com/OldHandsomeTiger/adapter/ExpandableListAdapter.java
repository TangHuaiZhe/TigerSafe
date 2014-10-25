package com.OldHandsomeTiger.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private static final String TAG ="ExpandableListAdapter";
	private SQLiteDatabase db;
	private Context context;
	
	public ExpandableListAdapter(Context context,SQLiteDatabase db) {
		this.db=db;
		this.context=context;
		
	}
	
	
	
	//返回分组的数目
	@Override
	public int getGroupCount() {
		int count=0;
		if(db.isOpen()){
			 Cursor  cursor=db.rawQuery("select count (*)  from classlist", null);
			 if(cursor.moveToFirst()){
				count = cursor.getInt(0);
			 }
			 cursor.close();
//			 db.close();
		}
		return count;
	}

	//返回对应group处child的数目
	@Override
	public int getChildrenCount(int groupPosition) {
		int count=0;
		int index=groupPosition+1;
		String sql="select count(*) from table"+index;
		if(db.isOpen()){
			 Cursor  cursor=db.rawQuery(sql, null);
			 if(cursor.moveToFirst()){
				count = cursor.getInt(0);
			 }
			 cursor.close();
//			 db.close();
		}
		return count;
	}

	
	//返回groupPosition对应位置的对象
	@Override
	public Object getGroup(int groupPosition) {
		// TODO 自动生成的方法存根
		return null;
	}
	
	
	
//获取对应分组位置child位置的对象
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return null;
	}

	//组ID
	@Override
	public long getGroupId(int groupPosition) {
		// TODO 自动生成的方法存根
		return 0;
	}

	//childID
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		// TODO 自动生成的方法存根
		return false;
	}

	
	//分组信息的View对象
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		TextView  tView=new TextView(context);
		int GroupIndex=groupPosition+1;
		String sql="select name from classlist ";
		String name = "";
		if(db.isOpen()){
			//child位置的数据，选择查询
			 Cursor  cursor=db.rawQuery(sql+"  where   idx=?", new String[]{GroupIndex+""});
			 if(cursor.moveToFirst()){
				 name="                       "+cursor.getString(0);
//				 System.out.println(name+"Groupname^^^^^^^^^");
			 }
			 cursor.close();
//			 db.close();
		}
		tView.setText(name);
		return tView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		TextView  tView=new TextView(context);
		int GroupIndex=groupPosition+1;
		int ChildIndex=childPosition+1;
		//查找制定的表
		String sql="select  number, name from table"+GroupIndex+" ";
		StringBuilder sBuilder=new StringBuilder();
		if(db.isOpen()){
			//child位置的数据，选择查询
			 Cursor  cursor=db.rawQuery(sql+" where  _id=?", new String[]{ChildIndex+""});
			 if(cursor.moveToFirst()){
				sBuilder.append(cursor.getString(1));
				sBuilder.append(":");
				sBuilder.append(cursor.getString(0));
			 }
			 cursor.close();
//			 db.close();
		}
		tView.setText(sBuilder.toString());
		return tView;
	}

	
	//child是否接受点击事件
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		
		int GroupIndex=groupPosition+1;
		int ChildIndex=childPosition+1;
		//查找制定的表
		String sql="select  number  from table"+GroupIndex+" ";
		
		Cursor  cursor=db.rawQuery(sql+"  where _id =?", new String[]{ChildIndex+""});
		if(cursor.moveToFirst()){
			//使用Intent.ACTION_DIAL  弹出拨号界面！
			String telNum="tel:"+cursor.getString(0);
			Log.i(TAG, "点击的电话号码是："+telNum);
			Uri uri=Uri.parse(telNum);
			Intent intent=new Intent();
			intent.setAction(Intent.ACTION_DIAL);
			intent.setData(uri);
			context.startActivity(intent);
		}
		return true;
	}

}
