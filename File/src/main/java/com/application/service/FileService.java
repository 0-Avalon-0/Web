package com.application.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.bean.FileText;
import com.application.bean.Menu;
import com.application.dao.FileDao;
import com.application.status.Status;

@Service
public class FileService implements IFileService{
	@Autowired
	private FileDao fileDao;
	
	public Status getFile(int pid, String filename, String path, HttpServletRequest httpServletRequest) {
		return fileDao.getFile(pid, filename, path, httpServletRequest);
	}

	public Status createFile(int pid, String filename, String path, Menu menu, HttpServletRequest httpServletRequest) {
		return fileDao.createFile(pid, filename, path, menu, httpServletRequest);
	}

	public Status changeFile(int pid, String filename, String path, FileText fileText,HttpServletRequest httpServletRequest) {
		return fileDao.changeFile(pid, filename, path, fileText, httpServletRequest);
	}

	public Status deleteFile(int pid, String filename, String path, HttpServletRequest httpServletRequest) {
		return fileDao.deleteFile(pid, filename, path, httpServletRequest);
	}

}
