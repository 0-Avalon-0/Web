package com.application.dao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.application.bean.User;
import com.application.status.Status;

public interface Ipersonaldata {
	Status getData(String name,HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse);
	Status changeData(String name,User user,HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse);
}
