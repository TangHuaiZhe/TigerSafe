package com.OldHandsomeTiger.domain;

public class SmsInfo {
	
	private String address;
	private String date;
	private String type;  //1 接受  2发送
	private String body;
	private String id;
	
	public SmsInfo(String id,String address,String date,String type,String body) {
		this.address=address;
		this.date=date;
		this.type=type;
		this.body=body;
		this.id=id;
		
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public SmsInfo(){
		
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getData() {
		return date;
	}
	public void setData(String date) {
		this.date = date;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}

	
}
