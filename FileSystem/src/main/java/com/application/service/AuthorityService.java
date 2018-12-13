package com.application.service;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.acceptbean.RequestAuthority;
import com.application.dao.AuthorityDao;
import com.application.status.Status;

@Service
public class AuthorityService implements IAuthorityService{
	@Autowired
	private AuthorityDao authoritydao;
	
	@Override
	public Status addAuthority(int pid, String membername, RequestAuthority requestAuthority, HttpServletResponse httpServletResponse) {
		return authoritydao.addAuthority(pid, membername, requestAuthority, httpServletResponse);
	}


}
