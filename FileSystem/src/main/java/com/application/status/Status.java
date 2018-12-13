package com.application.status;

public class Status {
	private int code;
	private String data;
	public void setCode(int statuscode) {
		this.code = statuscode;
	}
	public void setData(String statusinfo) {
		this.data = statusinfo;
	}
	public int getCode() {
		return this.code;
	}
	public String getData() {
		return this.data;
	}
}

