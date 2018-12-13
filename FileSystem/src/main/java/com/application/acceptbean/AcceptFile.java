package com.application.acceptbean;

public class AcceptFile {
	private String file_text;
	private String fname;
	private String path;
	private int pid;
	
	public void setfile_text(String text) {
		this.file_text = text;
	}
	public void setfname(String name) {
		this.fname = name;
	}
	public void setpath(String path) {
		this.path = path;
	}
	public void setpid(int pid) {
		this.pid = pid;
	}
	
	public String getfile_text() {
		return this.file_text;
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
