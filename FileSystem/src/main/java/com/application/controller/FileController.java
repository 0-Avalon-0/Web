package com.application.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.application.acceptbean.SimpleText;
import com.application.bean.FileText;
import com.application.bean.Menu;
import com.application.service.IFileService;
import com.application.status.Status;

@RestController
@RequestMapping(value = "/filemanagers")
@CrossOrigin(origins = "*",maxAge = 3600)
public class FileController {
	@Autowired
	private IFileService fileService;
	
	@RequestMapping(value = "/{fname}/file/{path}/project/{pid}", method = RequestMethod.GET)
	public Status getFile(@PathVariable("pid") int pid, @PathVariable("fname") String filename, @PathVariable("path") String path, HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) {
		return fileService.getFile(pid, filename, path, httpServletRequest,httpServletResponse);
	}
	
	@RequestMapping(value = "/{fname}/file/{path}/project/{pid}", method = RequestMethod.POST)
	public Status createFile(int pid, String filename, String path, @RequestBody SimpleText simpleText, HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) {
		return fileService.createFile(pid, filename, path, simpleText, httpServletRequest,httpServletResponse);
	}
	
	@RequestMapping(value = "/{fname}/file/{path}/project/{pid}", method = RequestMethod.PUT)
	public Status changeFile(int pid, String filename, String path, @RequestBody FileText fileText,HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) {
		return fileService.changeFile(pid, filename, path, fileText, httpServletRequest,httpServletResponse);
	}
	
	@RequestMapping(value = "/{fname}/file/{path}/project/{pid}", method = RequestMethod.DELETE)
	public Status deleteFile(int pid, String filename, String path, HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) {
		return fileService.deleteFile(pid, filename, path, httpServletRequest,httpServletResponse);
	}
	
}
