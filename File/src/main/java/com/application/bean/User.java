package com.application.bean;

public class User {
	private String user_name;
	private String user_password;
	private int user_gender;
	private String user_email;
	
	public void setuser_name(String name) {
		this.user_name = name;
	}
	public void setuser_password(String password) {
		this.user_password = password;
	}
	public void setuser_gender(int gender) {
		this.user_gender = gender;
	}
	public void setuser_email(String email) {
		this.user_email = email;
	}
	
	public String getuser_name() {
		return this.user_name;
	}
	public String getuser_password() {
		return this.user_password;
	}
	public int getuser_gender() {
		return this.user_gender;
	}
	public String getuser_email() {
		return this.user_email;
	}
}
