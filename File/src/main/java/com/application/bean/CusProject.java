package com.application.bean;

public class CusProject {
	private String project_pname;
	private String project_describe;
	private String project_establisher;
	private int project_property;
	
	public void setproject_name(String pname ) {
		this.project_pname = pname;
	}
	public void setproject_establisher(String ename) {
		this.project_establisher = ename;
	}
	public void setproject_describe(String description) {
		this.project_describe = description;
	}
	public void setproject_property(int property) {
		this.project_property = property;
	}
	
	public String getproject_name() {
		return this.project_pname;
	}
	public String getproject_establisher() {
		return this.project_establisher;
	}
	public String getproject_describe() {
		return this.project_describe;
	}
	public int getproject_property() {
		return this.project_property;
	}
	
}
