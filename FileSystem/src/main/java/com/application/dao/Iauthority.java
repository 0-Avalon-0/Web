package com.application.dao;

import javax.servlet.http.HttpServletResponse;

import com.application.acceptbean.RequestAuthority;
import com.application.status.Status;

public interface Iauthority {
	Status getAuthority(int pid,String membername);
	Status addAuthority(int pid,String membername,RequestAuthority requestAuthority,HttpServletResponse httpServletResponse);
}
