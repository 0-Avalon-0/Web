package com.application.dao;

import javax.servlet.http.HttpServletRequest;

import com.application.bean.CusProject;
import com.application.bean.PSetting;
import com.application.status.Status;

public interface Iproject {
	Status createProject(CusProject cusProject,HttpServletRequest httpServletRequest);//��������
	//Status deleteProject()
	Status searchProject(int pid,HttpServletRequest httpServletRequest);//�鿴�û������й���
	Status getSetting(int pid,HttpServletRequest httpServletRequest);//�鿴��������
	Status updateSetting(int pid,PSetting pSetting,HttpServletRequest httpServletRequest);//��������
}
