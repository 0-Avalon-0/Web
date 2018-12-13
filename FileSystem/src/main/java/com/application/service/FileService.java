package com.application.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.acceptbean.SimpleText;
import com.application.bean.FileText;
import com.application.bean.Menu;
import com.application.dao.FileDao;
import com.application.status.Status;

@Service
public class FileService implements IFileService{
	@Autowired
	private FileDao fileDao;
	
	public Status getFile(int pid, String filename, String path, HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) {
		return fileDao.getFile(pid, filename, path, httpServletRequest,httpServletResponse);
	}

	public Status createFile(int pid, String filename, String path, SimpleText simpleText, HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) {
		return fileDao.createFile(pid, filename, path, simpleText, httpServletRequest,httpServletResponse);
	}

	public Status changeFile(int pid, String filename, String path, FileText fileText,HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) {
		return fileDao.changeFile(pid, filename, path, fileText, httpServletRequest,httpServletResponse);
	}

	public Status deleteFile(int pid, String filename, String path, HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) {
		return fileDao.deleteFile(pid, filename, path, httpServletRequest,httpServletResponse);
	}

}
