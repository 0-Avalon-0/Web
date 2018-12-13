package com.application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.application.bean.CusProject;
import com.application.bean.Member;
import com.application.bean.Menu;
import com.application.bean.PSetting;
import com.application.bean.User;

import net.sf.json.JSONObject;

@SpringBootApplication
@RestController

public class Application {
	@Autowired
	JdbcTemplate JdbcTemplate;
	
	@RequestMapping(value = "/FileTest",method = RequestMethod.GET)
	public String test(HttpServletRequest httpServletRequest) {
		//return "1"+(String)httpServletRequest.getSession().getAttribute("user")+"1";
		return (String)httpServletRequest.getSession().getAttribute("user");
	}
	
	@RequestMapping(value = "/test/{pid}",method = RequestMethod.GET)
	public String test(@PathVariable("pid") int pid){
		String sql1 = "select membername, project_authority,project_describe from member where pid = ?";//’“≥…‘±≈‰÷√
		List<Member> members = JdbcTemplate.query(sql1, new Object[] {pid},new BeanPropertyRowMapper(Member.class));
		JSONObject jsonObject = JSONObject.fromObject(members.get(0));
		
		return jsonObject.toString();
	}
	
	@RequestMapping(value = "/test",method = RequestMethod.GET)
	public String test(@RequestBody User user){
		return user.getuser_name();
	}
	
	
	

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SpringApplication.run(Application.class, args);
	}

}
