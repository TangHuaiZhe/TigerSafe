package com.OldHandsomeTiger.domain;

public class Call {

	public Call() {
		this.number="";
		this.name="";
		this.type=0;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	private String number;
	private String name;
	private int type;
}
