package com.application.bean;

import java.util.List;

public class PSetting {
	private int pid;
	private String project_pname;
	private String project_describe;
	private String project_establisher;
	private int project_property;
	private List<Member>content;

	public void setContent(List<Member>memberinfo) {
		this.content = memberinfo;
	}
	public void setproject_pname(String pname ) {
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
	public void setpid(int pid) {
		this.pid = pid;
	}
	
	
	public String getproject_pname() {
		return this.project_pname;
	}
	public String getproject_establisher() {
		return this.project_establisher;
	}
	public String getproject_describe() {
		return this.project_describe;
	}
	public List<Member> getContent() {
		return this.content;
	}
	public int getproject_property() {
		return this.project_property;
	}
	public int getpid() {
		return this.pid;
	}
}
