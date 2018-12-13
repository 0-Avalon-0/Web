package com.application.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.application.bean.PassInfo;
import com.application.bean.User;
import com.application.service.IAccountService;
import com.application.status.Status;

@RestController
@RequestMapping(value = "/accounts")
@CrossOrigin(origins = "*",maxAge = 3600)
public class AccountController {
	@Autowired
	IAccountService AccountService;
	
//	@RequestMapping("/")
//	public String test() {
//		return "Test!";
//	}
	
//	@RequestMapping(value = "/{name}",method=RequestMethod.GET)
//	public Status Signup(@PathVariable("name")String name,@RequestParam(value ="user_password") String password) {
//		PassInfo passInfo = new PassInfo();
//		passInfo.setPassword();
//		SignupInfo signupInfo = new SignupInfo();
//		signupInfo.setName(name);
//		signupInfo.setPassword(password);
//		return AccountService.signup(signupInfo);
//	}
	
	@RequestMapping(value = "/{name}",method=RequestMethod.POST)
	public Status Signup(@PathVariable("name")String name,@RequestBody PassInfo password,HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) {
		return AccountService.signup(name,password,httpServletRequest,httpServletResponse);
	}

	
	@RequestMapping(method=RequestMethod.POST)
	public Status Login(@RequestBody User user,HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) {
		return AccountService.login(user,httpServletRequest,httpServletResponse);
	}
	
	@RequestMapping(value = "/test",method = RequestMethod.POST)
	public String Test(HttpServletRequest httpServletRequest) {
		HttpSession httpSession = httpServletRequest.getSession();
		httpSession.setAttribute("user", "lsh");
		return (String)httpSession.getAttribute("user");
	}

}

