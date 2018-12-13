package com.application.bean;

public class Member {
	private String membername;
	private int project_authority;
	private String project_describe;
	
	public void setmembername(String membername) {
		this.membername = membername;
	}
	public void setproject_authority(int authority) {
		this.project_authority = authority;
	}
	public void setproject_describe(String describe) {
		this.project_describe = describe;
	}

	public String getmembername() {
		return this.membername;
	}
	public int getproject_authority() {
		return this.project_authority;
	}
	public String getproject_describe() {
		return this.project_describe;
	}

}
