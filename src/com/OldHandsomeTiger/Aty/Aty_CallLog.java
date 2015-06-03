package com.OldHandsomeTiger.Aty;

import java.util.ArrayList;

import com.OldHandsomeTiger.domain.Call;
import com.OldHandsomeTiger.tigersafe.R;
import com.OldHandsomeTiger.tigersafe.R.id;
import com.OldHandsomeTiger.tigersafe.R.layout;
import com.OldHandsomeTiger.tigersafe.R.menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class Aty_CallLog extends Activity {

	private ListView callLogListView;
	private static String TAG="Aty_CallLog";
	private ArrayList<Call> callLogList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.call_log_list_view);
		//读取通话记录
		//to do  线程中执行，hanlder执行刷新界面，读10个数据貌似没必要
		callLogList = ReadCallLog();
		callLogListView = (ListView) this.findViewById(R.id.callLogListView);
		CallLogAdapter callLogAdapter = new CallLogAdapter(callLogList);
		callLogListView.setAdapter(callLogAdapter);
		
		
		callLogListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				AlertDialog.Builder builder = new Builder(Aty_CallLog.this);
				builder.setTitle("确认添加到黑名单中？");
				Call callLog=callLogList.get(position);
				String message="姓名:  "+callLog.getName()+"\n"+"号码:  "+callLog.getNumber();
				builder.setMessage(message);
				
				
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 号码插入数据库
						
					}
				});
				
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO 自动生成的方法存根
						
					}
				});
				
				
				
				
				builder.create().show();
			}
		});
	}


	
	private class CallLogAdapter extends BaseAdapter {

		private ArrayList<Call> callLogList = null;

		public CallLogAdapter(ArrayList<Call> callLogList) {
			this.callLogList = callLogList;
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return callLogList.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return callLogList.get(position);
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolderForCallLogListView viewHolder = null;
			if (null == convertView) {
				viewHolder = new ViewHolderForCallLogListView();
				convertView = View.inflate(Aty_CallLog.this,
						R.layout.call_log_item, null);
				viewHolder.name = (TextView) convertView
						.findViewById(R.id.tv_callLogItem_name);
				viewHolder.number = (TextView) convertView
						.findViewById(R.id.tv_callLogItem_number);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolderForCallLogListView) convertView
						.getTag();
			}
			viewHolder.name.setText(callLogList.get(position).getName());
			viewHolder.number.setText(callLogList.get(position).getNumber());
			// viewHolder的典型用法：
			// ViewHolder viewHolder = null;
			// if (null == convertView)
			// {
			// viewHolder = new ViewHolder();
			// LayoutInflater mInflater = LayoutInflater.from(mContext);
			// convertView = mInflater.inflate(R.layout.item_marker_item, null);
			//
			// viewHolder.name = (TextView) convertView.findViewById(R.id.name);
			// viewHolder.description = (TextView) convertView
			// .findViewById(R.id.description);
			// viewHolder.createTime = (TextView) convertView
			// .findViewById(R.id.createTime);
			//
			// convertView.setTag(viewHolder);
			// }
			// else
			// {
			// viewHolder = (ViewHolder) convertView.getTag();
			// }
			return convertView;
		}

	}

	private static class ViewHolderForCallLogListView {
		TextView name;
		TextView number;
	}

	private ArrayList<Call> ReadCallLog() {
		// TODO 自动生成的方法存根
		ContentResolver cr = Aty_CallLog.this.getContentResolver();

		ArrayList<Call> callLogList = new ArrayList<>();
		final Cursor cursor = cr.query(CallLog.Calls.CONTENT_URI, new String[] {
				CallLog.Calls.NUMBER, CallLog.Calls.CACHED_NAME,
				CallLog.Calls.TYPE, CallLog.Calls.DATE }, null, null,
				CallLog.Calls.DEFAULT_SORT_ORDER + " LIMIT 10");

		while (cursor.moveToNext()) {
			Call callLog = new Call();// 循环内new，否则导致list内所有对象都是最后一个修改的对象……
			String number = cursor.getString(0);
			String name = cursor.getString(1);
			int type = cursor.getInt(2);
			String date = cursor.getString(3);
			// Log.i(TAG, "start--------------");
//			 Log.i(TAG, "the number is "+number);
//			 Log.i(TAG, "the name is "+name);
			// Log.i(TAG, "the type is "+type);
			// Log.i(TAG, "the call date is "+date);
			// Log.i(TAG, "end--------------");
			if (number != null) {
				callLog.setNumber(number);
			} else {
				callLog.setNumber("未知号码");
			}
			if (name != null) {
				callLog.setName(name);
			} else {
				callLog.setName("陌生人");
			}
			callLog.setType(type);
			// Log.i(TAG, "the call number is "+callLog.getNumber());
			// Log.i(TAG, "the call name is "+callLog.getName());
			// Log.i(TAG, "the call type is "+callLog.getType());
			callLogList.add(callLog);
			// System.out.println("the size is "+callLogList.size());
		}
		cursor.close();
		// for (Call call : callLogList) {
		// System.out.println("the name is "+call.getName());
		// System.out.println("the number is"+call.getNumber());
		// System.out.println("the type is "+call.getType());
		return callLogList;
		// }
	}
}
