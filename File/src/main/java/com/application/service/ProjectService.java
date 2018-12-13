package com.application.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.bean.CusProject;
import com.application.bean.PSetting;
import com.application.dao.ProjectDao;
import com.application.status.Status;

@Service
public class ProjectService implements IProjectService{
	@Autowired
	private ProjectDao projectDao;
	
	public Status createProject(CusProject cusProject, HttpServletRequest httpServletRequest) {
		return projectDao.createProject(cusProject, httpServletRequest);
	}

	public Status searchProject(int pid, HttpServletRequest httpServletRequest) {
		return projectDao.searchProject(pid, httpServletRequest);
	}

	public Status getSetting(int pid, HttpServletRequest httpServletRequest) {
		return projectDao.getSetting(pid, httpServletRequest);
	}

	public Status updateSetting(int pid, PSetting pSetting, HttpServletRequest httpServletRequest) {
		return projectDao.updateSetting(pid, pSetting, httpServletRequest);
	}

}
