package com.application.acceptbean;

public class SimpleFile {
	private String file_fname;
	private int file_property;
	public void setfile_fname(String name) {
		this.file_fname = name;
	}
	public void setfile_property(int property) {
		this.file_property = property;
	}
	
	public String getfile_fname() {
		return this.file_fname;
	}
	public int getfile_property() {
		return this.file_property;
	}
}
