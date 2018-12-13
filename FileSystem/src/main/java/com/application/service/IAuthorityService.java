package com.application.service;

import javax.servlet.http.HttpServletResponse;

import com.application.acceptbean.RequestAuthority;
import com.application.status.Status;

public interface IAuthorityService {
	Status addAuthority(int pid,String membername,RequestAuthority requestAuthority,HttpServletResponse httpServletResponse);
}
