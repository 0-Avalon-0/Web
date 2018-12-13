package com.application.dao;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.application.acceptbean.AcceptName;
import com.application.bean.PassInfo;
import com.application.bean.User;
import com.application.status.Status;

import net.sf.json.JSONObject;

@Repository
public class AccountDao implements Iaccount{
	@Autowired 	
	JdbcTemplate jdbcTemplate;
	
	public Status signup(String name,PassInfo passInfo,HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) {
		Status status = new Status();

		HttpSession httpSession = httpServletRequest.getSession();
		String username = (String)httpSession.getAttribute("user");
		
		if(username!=null&&username.equals(name)) {
			status.setCode(200);
			AcceptName acceptname = new AcceptName();
			acceptname.setname(name);
			JSONObject jsonObject = JSONObject.fromObject(acceptname);
			status.setData(jsonObject.toString());
			
			httpServletResponse.setStatus(200);
		}else {
			String sql = "select * from user where user_name = ? and user_password = ?";
			
//			System.out.println("pass"+passInfo.getuser_password());
			try {
				List<User> list = jdbcTemplate.query(sql, new Object[] {name,passInfo.getuser_password()},new BeanPropertyRowMapper(User.class));
				
//				int count = jdbcTemplate.queryForObject(sql2, Integer.class);
//				System.out.println("count"+count);
				
				if(list!=null && list.size()>0) {
					status.setCode(200);
					
					httpServletResponse.setStatus(200);
					//sessionid可以不存储
					//status.setData(httpSession.getId().toString());
					httpSession.setAttribute("user", name);
					
				}else {
					status.setCode(404);
					
					httpServletResponse.setStatus(404);
					status.setData("No such user");
				}
			}catch(Exception exception) {
				if(exception instanceof DataAccessResourceFailureException) {
					status.setCode(500);
					
					httpServletResponse.setStatus(500);
					status.setData("数据库连接失败");
				}else {			
					status.setCode(600);
					httpServletResponse.setStatus(600);
					status.setData(exception.getMessage());
				}
			}
		}
		
		return status;
	}

	public Status login(User user,HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) {
		Status status = new Status();
		try {
			int result = jdbcTemplate.update(("insert into user values (?,?,?,?)"),user.getuser_name(),user.getuser_password(),user.getuser_gender(),user.getuser_email());
			status.setCode(201);
			
			httpServletResponse.setStatus(201);
			JSONObject jsonObject = JSONObject.fromObject(user);
			status.setData(jsonObject.toString());
			
			HttpSession httpSession = httpServletRequest.getSession();
			httpSession.setAttribute("user", user.getuser_name());
			
		}catch(Exception exception){
			if(exception instanceof DataAccessResourceFailureException) {
				status.setCode(500);
				httpServletResponse.setStatus(500);
				status.setData("数据库连接失败");
			}else if(exception instanceof DuplicateKeyException){
				//exception.printStackTrace();
				status.setCode(422);
				httpServletResponse.setStatus(422);
				status.setData("用户名重复");
			}else {
				exception.printStackTrace();
				
				status.setCode(600);
				httpServletResponse.setStatus(600);
				status.setData(exception.getMessage());
			}
		}
		
		return status;
	}

}

