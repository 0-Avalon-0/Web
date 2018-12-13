package com.application.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.application.acceptbean.AcceptFileName;
import com.application.status.Status;

public interface IMenuService {
	Status getFiles(String path,int pid,HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse);
	Status renameFile(String filename,String path,int pid,AcceptFileName acceptFileName,HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse);
}
