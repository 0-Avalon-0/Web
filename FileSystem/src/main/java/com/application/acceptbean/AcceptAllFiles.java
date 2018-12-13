package com.application.acceptbean;

import java.util.List;

public class AcceptAllFiles {
	private String path;
	private int pid;
	private List<SimpleFile> files;
	
	public void setpath(String path) {
		this.path = path;
	}
	public void setpid(int pid) {
		this.pid = pid;
	}
	public void setfiles(List<SimpleFile> files) {
		this.files = files;
	}
	
	public String getpath() {
		return this.path;
	}
	public int getpid() {
		return this.pid;
	}
	public List<SimpleFile> getfiles(){
		return this.files;
	}
}
