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

import com.application.bean.User;
import com.application.service.PersonalDataService;
import com.application.status.Status;

@RestController
@RequestMapping(value = "/datamanagers")
@CrossOrigin(origins = "*",maxAge = 3600)
public class PersonalDataController {
	@Autowired
	private PersonalDataService personalDataService;
	
	@RequestMapping(value = "/{name}",method = RequestMethod.GET)
	public Status getData(@PathVariable("name") String name,HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) {
		return personalDataService.getData(name, httpServletRequest,httpServletResponse);
	}
	
	@RequestMapping(value = "/{name}", method = RequestMethod.PATCH)
	public Status changeData(@PathVariable("name") String name,@RequestBody User user,HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) {
		return personalDataService.changeData(name, user, httpServletRequest,httpServletResponse);
	}
	
}

