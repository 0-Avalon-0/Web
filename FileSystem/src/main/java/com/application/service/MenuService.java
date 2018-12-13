package com.application.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.acceptbean.AcceptFileName;
import com.application.dao.MenuDao;
import com.application.status.Status;

@Service
public class MenuService implements IMenuService{
	@Autowired
	MenuDao menudao;
	
	@Override
	public Status getFiles(String path, int pid, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		return menudao.getFiles(path, pid, httpServletRequest, httpServletResponse);
	}

	@Override
	public Status renameFile(String filename, String path, int pid, AcceptFileName acceptFileName,
			HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		return menudao.renameFile(filename, path, pid, acceptFileName, httpServletRequest, httpServletResponse);
	}

}
