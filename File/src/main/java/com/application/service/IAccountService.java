package com.application.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.application.bean.PassInfo;
import com.application.bean.User;
import com.application.status.Status;

public interface IAccountService {
	Status signup(String name,PassInfo passInfo,HttpServletRequest httpServletRequest);
	Status login(User user,HttpServletRequest httpServletRequest);

}
