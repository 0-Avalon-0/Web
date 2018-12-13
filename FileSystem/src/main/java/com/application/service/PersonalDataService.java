package com.application.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.bean.User;
import com.application.dao.PersonaldataDao;
import com.application.status.Status;

@Service
public class PersonalDataService implements IPersonalDataService{
	@Autowired
	private PersonaldataDao personaldataDao;
	
	public Status getData(String name, HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) {
		return personaldataDao.getData(name, httpServletRequest,httpServletResponse);
	}

	public Status changeData(String name, User user, HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) {
		return personaldataDao.changeData(name, user, httpServletRequest,httpServletResponse);
	}

}
