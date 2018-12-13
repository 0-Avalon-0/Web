package com.application.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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
public class PersonalDataController {
	@Autowired
	private PersonalDataService personalDataService;
	
	@RequestMapping(value = "/{name}",method = RequestMethod.GET)
	public Status getData(@PathVariable("name") String name,HttpServletRequest httpServletRequest) {
		return personalDataService.getData(name, httpServletRequest);
	}
	
	@RequestMapping(value = "/{name}", method = RequestMethod.PATCH)
	public Status changeData(@PathVariable("name") String name,@RequestBody User user,HttpServletRequest httpServletRequest) {
		return personalDataService.changeData(name, user, httpServletRequest);
	}
	
}
