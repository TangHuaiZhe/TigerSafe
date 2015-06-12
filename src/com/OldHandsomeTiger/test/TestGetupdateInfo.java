package com.OldHandsomeTiger.test;

import java.util.List;

import android.test.AndroidTestCase;

import com.OldHandsomeTiger.db.DB_blackNum;
import com.OldHandsomeTiger.db.dao.DAO_blackNum;
import com.OldHandsomeTiger.domain.ContactInfo;
import com.OldHandsomeTiger.domain.SmsInfo;
import com.OldHandsomeTiger.engine.SmsInfo_Service;

public class TestGetupdateInfo extends AndroidTestCase {

	public void testAdd() {
		DAO_blackNum dao_blackNum = new DAO_blackNum(getContext());
		long number = 1301782402;
		for (int i = 0; i < 100; i++) {
			number = number + i;
			dao_blackNum.add(number + "","tanghuaizhe"+i);
			System.out.println(number);
		}
	}

	public void testFinadAll() {
		DAO_blackNum dao_blackNum = new DAO_blackNum(getContext());
		List<ContactInfo> contactsList = dao_blackNum.findAll();
		// System.out.println(numbers.size());
		// assertEquals(100, numbers.size());

		for (ContactInfo ContactInfo : contactsList) {

			System.out.println(ContactInfo.getNumber());

		}

	}
	
	public void  testdelete() {
		DAO_blackNum dao_blackNum = new DAO_blackNum(getContext());
		String number=1301782402+"";
		assertEquals(true, dao_blackNum.find(number));
		dao_blackNum.delete(number);
		assertEquals(false, dao_blackNum.find(number));
	}
	
	public void  testupdate() {
		DAO_blackNum dao_blackNum = new DAO_blackNum(getContext());
		String number=1301782403+"";
		assertEquals(true, dao_blackNum.find(number));
		dao_blackNum.update(number, 110+"");
		assertEquals(false, dao_blackNum.find(number));
		assertEquals(true, dao_blackNum.find(110+""));
	}
	
	public void testSmsInfoService()
	{
		SmsInfo_Service smsInfo_Service=new SmsInfo_Service(getContext());
		List<SmsInfo>   smsInfos = smsInfo_Service.getSmsInfo();
		
		
	}
	

	
	
	

}
