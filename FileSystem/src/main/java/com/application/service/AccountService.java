package com.application.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.bean.PassInfo;
import com.application.bean.User;
import com.application.dao.AccountDao;
import com.application.status.Status;

@Service
public class AccountService implements IAccountService{
	@Autowired
	AccountDao accountDao;
	
	public Status signup(String name,PassInfo passInfo,HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) {
		return accountDao.signup(name,passInfo,httpServletRequest,httpServletResponse);	
	}

	public Status login(User user,HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) {
		return accountDao.login(user,httpServletRequest,httpServletResponse);
	}


}
