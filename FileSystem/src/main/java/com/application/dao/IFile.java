package com.application.dao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.application.acceptbean.SimpleText;
import com.application.bean.FileText;
import com.application.bean.Menu;
import com.application.status.Status;

public interface IFile {
	Status getFile(int pid,String filename,String path,HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse);//此处path是父路径的完整路径
	Status createFile(int pid,String filename,String path,SimpleText simpleText,HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse);
	Status changeFile(int pid,String filename,String path,FileText fileText,HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse);
	Status deleteFile(int pid,String filename,String path,HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse);
}
