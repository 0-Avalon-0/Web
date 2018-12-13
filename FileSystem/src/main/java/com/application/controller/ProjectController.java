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

import com.application.bean.CusProject;
import com.application.bean.PSetting;
import com.application.service.IProjectService;
import com.application.status.Status;

@RestController
@RequestMapping(value = "/projectsettings")
@CrossOrigin(origins = "*",maxAge = 3600)
public class ProjectController {
	@Autowired 
	private IProjectService projectService;
	
	@RequestMapping(method = RequestMethod.POST)
	public Status createProject(@RequestBody CusProject cusProject, HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) {
		return projectService.createProject(cusProject, httpServletRequest,httpServletResponse);
	}
	
	@RequestMapping(value = "/{pid}",method = RequestMethod.DELETE)
	public Status deleteProject(@PathVariable("pid") int pid,HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) {
		return projectService.deleteProject(pid, httpServletRequest, httpServletResponse);
	}

	@RequestMapping(value = "/user",method = RequestMethod.GET)
	public Status searchProject(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) {
		return projectService.searchProject(httpServletRequest,httpServletResponse);
	}

	@RequestMapping(value = "/{pid}",method = RequestMethod.GET)
	public Status getSetting(@PathVariable("pid") int pid, HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) {
		return projectService.getSetting(pid, httpServletRequest,httpServletResponse);
	}

	@RequestMapping(value = "/{pid}",method = RequestMethod.PATCH)
	public Status updateSetting(@PathVariable("pid") int pid, @RequestBody PSetting pSetting, HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) {
		return projectService.updateSetting(pid, pSetting, httpServletRequest,httpServletResponse);
	}
}
