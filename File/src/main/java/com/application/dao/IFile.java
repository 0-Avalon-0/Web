package com.application.dao;

import javax.servlet.http.HttpServletRequest;

import com.application.bean.FileText;
import com.application.bean.Menu;
import com.application.status.Status;


public interface IFile {
	Status getFile(int pid,String filename,String path,HttpServletRequest httpServletRequest);//此处path是父路径的完整路径
	Status createFile(int pid,String filename,String path,Menu menu,HttpServletRequest httpServletRequest);
	Status changeFile(int pid,String filename,String path,FileText fileText,HttpServletRequest httpServletRequest);
	Status deleteFile(int pid,String filename,String path,HttpServletRequest httpServletRequest);
}
