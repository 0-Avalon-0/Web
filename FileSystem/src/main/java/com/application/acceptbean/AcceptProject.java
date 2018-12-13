package com.application.acceptbean;

import java.util.List;

import com.application.bean.PSetting;

public class AcceptProject {
	private List<PSetting> projects;
	public void setprojects(List<PSetting> projects) {
		this.projects = projects;
	}
	public List<PSetting> getprojects(){
		return this.projects;
	}
}
