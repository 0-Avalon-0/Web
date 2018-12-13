package com.application.service;

import javax.servlet.http.HttpServletRequest;

import com.application.bean.User;
import com.application.status.Status;

public interface IPersonalDataService {
	Status getData(String name,HttpServletRequest httpServletRequest);
	Status changeData(String name,User user,HttpServletRequest httpServletRequest);
}
