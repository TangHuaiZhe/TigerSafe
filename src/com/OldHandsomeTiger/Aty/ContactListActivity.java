package com.OldHandsomeTiger.Aty;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.OldHandsomeTiger.adapter.ContactInfoAdapter;
import com.OldHandsomeTiger.domain.ContactInfo;
import com.OldHandsomeTiger.engine.ContactInfo_Service;
import com.OldHandsomeTiger.tigersafe.R;

public class ContactListActivity extends Activity {

	private ListView lv_contact;
	
	private ContactInfo_Service service;
	
	private ContactInfoAdapter mAdater;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.contact_list);
		
		lv_contact = (ListView) findViewById(R.id.lv_contact);
		
		service = new ContactInfo_Service(this);
		List<ContactInfo> contacts = service.getContacts();
		
		mAdater = new ContactInfoAdapter(this,contacts);
		lv_contact.setAdapter(mAdater);
		
		lv_contact.setOnItemClickListener(new MyOnItemClickListener());
	}
	
	private final class MyOnItemClickListener implements OnItemClickListener{

		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			ContactInfo info = (ContactInfo) mAdater.getItem(position);
			String number = info.getNumber();
			Intent data = new Intent();
			data.putExtra("number", number);
			setResult(200, data);//往上一个activity返回数据
			finish();
		}
		
	}
}
