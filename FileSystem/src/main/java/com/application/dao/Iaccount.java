package com.application.dao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.application.bean.PassInfo;
import com.application.bean.User;
import com.application.status.Status;

public interface Iaccount {
	Status signup(String name, PassInfo passInfo,HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse);//µÇÂ¼
	Status login(User user,HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse);//×¢²á
}
