package com.application.acceptbean;

public class AcceptText {
	private String fname;
	private String path;
	private int pid;
	
	public void setfname(String name) {
		this.fname = name;
	}
	public void setpath(String path) {
		this.path = path;
	}
	public void setpid(int pid) {
		this.pid = pid;
	}
	
	public String getfname() {
		return this.fname;
	}
	public String getpath() {
		return this.path;
	}
	public int getpid() {
		return this.pid;
	}
}
