package com.application.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.application.bean.FileText;
import com.application.bean.Menu;
import com.application.service.IFileService;
import com.application.status.Status;

@RestController
@RequestMapping(value = "/filemanagers")
public class FileController {
	@Autowired
	private IFileService fileService;
	
	@RequestMapping(value = "/{fname}/file/{path}", method = RequestMethod.GET)
	public Status getFile(int pid, String filename, String path, HttpServletRequest httpServletRequest) {
		return fileService.getFile(pid, filename, path, httpServletRequest);
	}
	
	
	public Status createFile(int pid, String filename, String path, @RequestBody Menu menu, HttpServletRequest httpServletRequest) {
		return fileService.createFile(pid, filename, path, menu, httpServletRequest);
	}

	public Status changeFile(int pid, String filename, String path, @RequestBody FileText fileText,HttpServletRequest httpServletRequest) {
		return fileService.changeFile(pid, filename, path, fileText, httpServletRequest);
	}

	public Status deleteFile(int pid, String filename, String path, HttpServletRequest httpServletRequest) {
		return fileService.deleteFile(pid, filename, path, httpServletRequest);
	}
	
}
