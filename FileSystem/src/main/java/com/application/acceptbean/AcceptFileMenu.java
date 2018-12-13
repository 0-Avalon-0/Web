package com.application.acceptbean;

public class AcceptFileMenu {
	private String file_text;
	private String file_fname;
	private String path;
	private int file_property;
	private int pid;
	
	public void setfile_text(String text) {
		this.file_text = text;
	}
	public void setfile_fname(String name) {
		this.file_fname = name;
	}
	public void setpath(String path) {
		this.path = path;
	}
	public void setpid(int pid) {
		this.pid = pid;
	}
	public void setfile_property(int property) {
		this.file_property = property;
	}
	
	public String getfile_text() {
		return this.file_text;
	}
	public String getfile_fname() {
		return this.file_fname;
	}
	public String getpath() {
		return this.path;
	}
	public int getpid() {
		return this.pid;
	}
	public int getfile_property() {
		return this.file_property;
	}
}
