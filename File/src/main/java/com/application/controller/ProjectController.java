package com.application.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(value = "/projectsetting")
public class ProjectController {
	@Autowired 
	private IProjectService projectService;
	
	@RequestMapping(method = RequestMethod.POST)
	public Status createProject(@RequestBody CusProject cusProject, HttpServletRequest httpServletRequest) {
		return projectService.createProject(cusProject, httpServletRequest);
	}

//	@RequestMapping(value = "/{pid}",method = RequestMethod.GET)
//	public Status searchProject(@PathVariable("pid") int pid, HttpServletRequest httpServletRequest) {
//		return projectService.searchProject(pid, httpServletRequest);
//	}

	@RequestMapping(value = "/{pid}",method = RequestMethod.GET)
	public Status getSetting(@PathVariable("pid") int pid, HttpServletRequest httpServletRequest) {
		return projectService.getSetting(pid, httpServletRequest);
	}

	@RequestMapping(value = "/{pid}",method = RequestMethod.PATCH)
	public Status updateSetting(@PathVariable("pid") int pid, @RequestBody PSetting pSetting, HttpServletRequest httpServletRequest) {
		return projectService.updateSetting(pid, pSetting, httpServletRequest);
	}
}
