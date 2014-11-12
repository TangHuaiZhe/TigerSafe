package com.OldHandsomeTiger.test;

import android.test.AndroidTestCase;

import com.OldHandsomeTiger.util.GetHardwareInfo;

public class testHardwareinfo extends AndroidTestCase {
	
	
	public void testHardware(){
		String name=GetHardwareInfo.getCpuName();
		System.out.println("cpuName is "+name);
		
		int count=GetHardwareInfo.getNumCores();
		System.out.println("cpuCoreNum is "+count);
		
		String minFre=GetHardwareInfo.getMinCpuFreq();
		System.out.println("cpuCoreNum is "+minFre);
		
		long RAM=GetHardwareInfo.getRamMemory();
		System.out.println("RAM大小是："+RAM);
		
//		String size=GetHardwareInfo.getScreenResolution(getContext());
//		System.out.println("屏幕大小是："+size);
	}
	

	
	

}
