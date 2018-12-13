package com.application.dao;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.application.bean.PassInfo;
import com.application.bean.User;
import com.application.status.Status;

import net.sf.json.JSONObject;


@Repository
public class AccountDao implements Iaccount{
	@Autowired 	
	JdbcTemplate jdbcTemplate;
	
	public Status signup(String name,PassInfo passInfo,HttpServletRequest httpServletRequest) {
		Status status = new Status();

		HttpSession httpSession = httpServletRequest.getSession();
		String username = (String)httpSession.getAttribute("user");
		
		if(username!=null&&username.equals(name)) {
			status.setCode(200);
			status.setData("");
		}else {
			String sql = "select * from user where user_name = ? and user_password = ?";
			
//			String sql2 = "select count(user_name) from user";
			
//			System.out.println("name"+name);
//			
//			System.out.println("pass"+passInfo.getuser_password());
			try {
				List<User> list = jdbcTemplate.query(sql, new Object[] {name,passInfo.getuser_password()},new BeanPropertyRowMapper(User.class));
				
//				int count = jdbcTemplate.queryForObject(sql2, Integer.class);
//				System.out.println("count"+count);
				
				if(list!=null && list.size()>0) {
					status.setCode(200);
					//sessionid可以不存储
					//status.setData(httpSession.getId().toString());
					httpSession.setAttribute("user", name);
					
				}else {
					status.setCode(404);
					status.setData("No such user");
				}
			}catch(Exception exception) {
				if(exception instanceof DataAccessResourceFailureException) {
					status.setCode(500);
					status.setData("数据库连接失败");
				}else {
					exception.printStackTrace();
					
					status.setCode(000);
					status.setData("unknown error");
				}
			}
		}
		
		return status;
	}

	public Status login(User user,HttpServletRequest httpServletRequest) {
		Status status = new Status();
		try {
			int result = jdbcTemplate.update(("insert into user values (?,?,?,?)"),user.getuser_name(),user.getuser_password(),user.getuser_gender(),user.getuser_email());
			status.setCode(201);
			JSONObject jsonObject = JSONObject.fromObject(user);
			status.setData(jsonObject.toString());
			
			HttpSession httpSession = httpServletRequest.getSession();
			httpSession.setAttribute("user", user.getuser_name());
			
		}catch(Exception exception){
			if(exception instanceof DataAccessResourceFailureException) {
				status.setCode(500);
				status.setData("数据库连接失败");
			}else if(exception instanceof DuplicateKeyException){
				//exception.printStackTrace();
				status.setCode(422);
				status.setData("用户名重复");
			}else {
				exception.printStackTrace();
				
				status.setCode(000);
				status.setData("unknown error");
			}
		}
		
		return status;
	}

}
