package com.application.dao;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.application.bean.User;
import com.application.status.Status;

import net.sf.json.JSONObject;


@Repository
public class PersonaldataDao implements Ipersonaldata{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public Status getData(String name,HttpServletRequest httpServletRequest) {
		Status status = new Status();
		String sql = "select * from user where user_name = ?";
			
		HttpSession httpSession = httpServletRequest.getSession();
		
		//检查是否登录
		if(httpSession.getAttribute("user")!=null && ((String)httpSession.getAttribute("user")).equals(name)) {
			try {
				List<User> list = jdbcTemplate.query(sql,new Object[]{name},new BeanPropertyRowMapper(User.class));
				if(list!=null && list.size()>0) {
					User userinfo = list.get(0);
					JSONObject jsonObject = JSONObject.fromObject(userinfo);
					status.setCode(200);
					status.setData(jsonObject.toString());
				}else {
					status.setCode(404);
					status.setData("用户信息不存在");
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
		}else {
			status.setCode(401);
			status.setData("用户未登录");
		}	
		return status;
	}

	public Status changeData(String name, User user,HttpServletRequest httpServletRequest) {
		Status status = new Status();
		String sql = "update user set user_password = ?, user_gender = ?, user_email = ? where user_name = ?";
			
		HttpSession httpSession = httpServletRequest.getSession();
		
		//检查是否登录 
		
		if(httpSession.getAttribute("user")!=null && ((String)httpSession.getAttribute("user")).equals(name)) {
			try {
				User originUser = new User();
				String findusersql = "select * from user where user_name = ?";
				List<User> originUserList = jdbcTemplate.query(findusersql,new Object[] {name}, new BeanPropertyRowMapper(User.class));
				//先找原来用户的信息
				//原因是更改时不变的信息不传，需要服务器找
				if(originUserList!=null && originUserList.size()>0) {
					originUser = originUserList.get(0);
					
					//此处若gender不修改则要求客户端传入gender数据为-1
					//此处若其余数据不修改则要求客户端传入""空字符串
					int result= jdbcTemplate.update(sql,user.getuser_password()==""?originUser.getuser_password():user.getuser_password(),
							user.getuser_gender()==-1?originUser.getuser_gender():user.getuser_gender(),
									user.getuser_email()==""?originUser.getuser_email():user.getuser_email(),
											user.getuser_name()==""?originUser.getuser_name():user.getuser_name());
					if(result>0) {
						JSONObject jsonObject = JSONObject.fromObject(user);
						status.setCode(201);
						status.setData(jsonObject.toString());
					}else {
						status.setCode(404);
						status.setData("用户信息不存在");
					}	
				}else {
					status.setCode(404);
					status.setData("用户信息不存在");
				}
				
						
			}catch(Exception exception) {
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
		}else {
			status.setCode(401);
			status.setData("用户未登录");
		}	
		return status;
	}
	
}
