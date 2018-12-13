package com.application;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.application.Application;

@SpringBootApplication
@RestController
@ServletComponentScan(basePackages = "com.application.status")
public class Application extends SpringBootServletInitializer{
	
	@RequestMapping(value = "/FileTest",method = RequestMethod.GET)
	public String test(HttpServletRequest httpServletRequest) {
		//return "1"+(String)httpServletRequest.getSession().getAttribute("user")+"1";
		return httpServletRequest.getSession().getAttribute("user")==null?"1":"2";
	}
	
	
	
	@Override
	   protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
	      return application.sources(Application.class);
	   }
	
	public static void main(String[] args) {
		
		SpringApplication.run(Application.class, args);
	}

}
