package com.application.bean;

import java.util.List;

public class PSetting {
	private String project_pname;
	private String project_describe;
	private String project_establisher;
	private List<Member>content;

	public void setContent(List<Member>memberinfo) {
		this.content = memberinfo;
	}
	public void setproject_name(String pname ) {
		this.project_pname = pname;
	}
	public void setproject_establisher(String ename) {
		this.project_establisher = ename;
	}
	public void setproject_describe(String description) {
		this.project_describe = description;
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
	public List<Member> getContent() {
		return this.content;
	}
}
