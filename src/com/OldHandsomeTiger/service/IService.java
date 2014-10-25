package com.OldHandsomeTiger.service;

public interface IService {
	//开启保护，从忽略名单中移除APP
	public void AppProtectStart(String packname);
	
	//关闭保护，添加到忽略名单
	public void AppProtectStop(String packname);
	
}
