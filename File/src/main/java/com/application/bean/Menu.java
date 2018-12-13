package com.application.bean;

public class Menu {
	private int pid;
	private String file_fname;
	private String file_node;
	private String file_parentnode;
	private int file_property;
	private String file_text;
	
	public void setPid(int pid) {
		this.pid = pid;
	}
	public void setfile_fname(String name) {
		this.file_fname = name;
	}
	public void setfile_node(String node) {
		this.file_node = node;
	}
	public void setfile_parentnode(String pnode) {
		this.file_parentnode = pnode;
	}
	public void setfile_property(int property) {
		this.file_property = property;
	}
	public void setfile_text(String text) {
		this.file_text = text;
	}
	
	
	public int getPid() {
		return this.pid;
	}
	public String getfile_fname() {
		return this.file_fname;
	}
	public String getfile_node() {
		return this.file_node;
	}
	public String getfile_parentnode() {
		return this.file_parentnode;
	}
	public int getfile_property() {
		return this.file_property;
	}
	public String getfile_text() {
		return this.file_text;
	}
	
	
}
