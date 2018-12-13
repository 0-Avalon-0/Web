package com.application.dao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.application.bean.CusProject;
import com.application.bean.PSetting;
import com.application.status.Status;

public interface Iproject {
	Status createProject(CusProject cusProject,HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse);//创建工程
	Status deleteProject(int pid,HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse);
	Status searchProject(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse);//查看用户的所有工程
	Status getSetting(int pid,HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse);//查看工程配置
	Status updateSetting(int pid,PSetting pSetting,HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse);//更新配置
}
