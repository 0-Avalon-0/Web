package com.application.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	
	public Status createProject(CusProject cusProject, HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) {
		return projectDao.createProject(cusProject, httpServletRequest,httpServletResponse);
	}
	
	public Status deleteProject(int pid,HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) {
		return projectDao.deleteProject(pid, httpServletRequest, httpServletResponse);
	}

	public Status searchProject(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) {
		return projectDao.searchProject(httpServletRequest,httpServletResponse);
	}

	public Status getSetting(int pid, HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) {
		return projectDao.getSetting(pid, httpServletRequest,httpServletResponse);
	}

	public Status updateSetting(int pid, PSetting pSetting, HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) {
		return projectDao.updateSetting(pid, pSetting, httpServletRequest,httpServletResponse);
	}

}
