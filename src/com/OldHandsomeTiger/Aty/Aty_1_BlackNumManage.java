package com.OldHandsomeTiger.Aty;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.ContentProducer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.OldHandsomeTiger.db.dao.DAO_blackNum;
import com.OldHandsomeTiger.domain.Call;
import com.OldHandsomeTiger.tigersafe.R;

public class Aty_1_BlackNumManage extends Activity {
	private ListView lv_call_sms_safe;
	private Button bt_add_black_number;
	private DAO_blackNum dao;
	private List<String> numbers;
	private CallSmsAdapter adapter;
	private static String TAG = "Aty_1_BlackNumManage";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.call_sms_safe);
		dao = new DAO_blackNum(this);
		lv_call_sms_safe = (ListView) this.findViewById(R.id.lv_call_sms_safe);
		// 给listview注册上下文菜单
		registerForContextMenu(lv_call_sms_safe);
		bt_add_black_number = (Button) this
				.findViewById(R.id.bt_add_black_number);

		bt_add_black_number.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				AlertDialog.Builder builder = new Builder(
						Aty_1_BlackNumManage.this);
				builder.setTitle("如何选取黑名单号码？");
				CharSequence[] items = { "通话记录", "联系人", "手动输入" };
				android.content.DialogInterface.OnClickListener listener = new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:// 通话记录
							Intent intent = new Intent(
									Aty_1_BlackNumManage.this,
									Aty_CallLog.class);
							startActivityForResult(intent, 1);
							break;
						default:
							break;
						}
					}
				};
				builder.setItems(items, listener);
				builder.create().show();
			}
		});
		numbers = dao.findAll();
		adapter = new CallSmsAdapter();
		lv_call_sms_safe.setAdapter(adapter);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO 自动生成的方法存根
		super.onActivityResult(requestCode, resultCode, data);
		Toast.makeText(this, "会掉了", Toast.LENGTH_LONG).show();
	}

	/**
	 * ListView的上下文菜单注册之后在这里被定义
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_menu, menu);
	}

	/**
	 * ListView上下文菜单的点击事件
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		int id = (int) info.id;
		String number = numbers.get(id);
		switch (item.getItemId()) {
		case R.id.update_number:
			updataNumber(number);
			break;
		case R.id.delete_number:
			// 当前条目的id
			dao.delete(number);
			// 重新获取黑名单号码
			numbers = dao.findAll();
			// 通知listview更新界面
			adapter.notifyDataSetChanged();
			break;

		}
		return false;
	}

	/**
	 * 更新黑名单号码
	 * 
	 * @param number
	 */
	private void updataNumber(final String oldnumber) {
		AlertDialog.Builder builder = new Builder(Aty_1_BlackNumManage.this);
		builder.setTitle("更改黑名单号码");
		final EditText et = new EditText(Aty_1_BlackNumManage.this);
		et.setInputType(InputType.TYPE_CLASS_PHONE);
		builder.setView(et);
		builder.setPositiveButton("更改", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String newNumber = et.getText().toString().trim();
				if (TextUtils.isEmpty(newNumber)) {
					Toast.makeText(getApplicationContext(), "黑名单号码不能为空", 1)
							.show();
					return;
				} else {
					dao.update(oldnumber, newNumber);
					// todo: 通知listview更新数据
					// 缺点: 重新刷新整个listview
					// numbers = dao.getAllNumbers();
					// lv_call_sms_safe.setAdapter(new
					// ArrayAdapter<String>(CallSmsActivity.this,
					// R.layout.blacknumber_item, R.id.tv_blacknumber_item,
					// numbers));
					numbers = dao.findAll();
					// 让数据适配器通知listview更新数据
					adapter.notifyDataSetChanged();

				}

			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		builder.create().show();

	}

	// 不用arrayAdapter的原因是： adapter.notifyDataSetChanged();对arrayadapter适配器无效。
	private class CallSmsAdapter extends BaseAdapter {

		public int getCount() {
			// TODO Auto-generated method stub
			return numbers.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return numbers.get(position);
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(Aty_1_BlackNumManage.this,
					R.layout.blacknumber_item, null);
			TextView number = (TextView) view
					.findViewById(R.id.tv_blacknumber_item);
			number.setText(numbers.get(position));
			return view;
		}

	}

}
