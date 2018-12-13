package com.application.dao;

import javax.servlet.http.HttpServletRequest;

import com.application.bean.CusProject;
import com.application.bean.PSetting;
import com.application.status.Status;

public interface Iproject {
	Status createProject(CusProject cusProject,HttpServletRequest httpServletRequest);//创建工程
	//Status deleteProject()
	Status searchProject(int pid,HttpServletRequest httpServletRequest);//查看用户的所有工程
	Status getSetting(int pid,HttpServletRequest httpServletRequest);//查看工程配置
	Status updateSetting(int pid,PSetting pSetting,HttpServletRequest httpServletRequest);//更新配置
}
