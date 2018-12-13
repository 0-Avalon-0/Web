package com.application.dao;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.application.acceptbean.AcceptAuthority;
import com.application.acceptbean.RequestAuthority;
import com.application.status.Status;

import net.sf.json.JSONObject;

@Repository
public class AuthorityDao implements Iauthority{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public Status getAuthority(int pid,String membername) {
		Status status = new Status();
		
		String sql = "select authority from member where pid = ? and membername = ?";
		try {
			List<Integer> list= jdbcTemplate.query(sql, new Object[] {pid,membername},new BeanPropertyRowMapper(Integer.class));
			if(list!=null && list.size()>0) {
				status.setCode(200);
				status.setData(list.get(0).toString());
			}else {
				status.setCode(403);
				status.setData("用户权限不足");
			}
		}catch(Exception e){
			if(e instanceof DataAccessResourceFailureException) {
				status.setCode(500);
				status.setData("数据库连接失败");
			}else {
				//e.printStackTrace();
				
				status.setCode(600);
				status.setData(e.getMessage());
			}
		}
		
		return status;
	}

	@Override
	public Status addAuthority(int pid, String membername, RequestAuthority requestAuthority,HttpServletResponse httpServletResponse) {
		Status status = new Status();
		String sql = "select project_authority from member where pid = ? and membername = ?";
		String addsql = "insert into member values(?,?,?)";
		try {
			List<Integer> authority = jdbcTemplate.query(sql, new Object[] {pid,membername},new BeanPropertyRowMapper(Integer.class));
			boolean isAble = false;
			if(authority!=null&&authority.size()>0) {
				int canManage = authority.get(0);
				if(canManage==2) {
					isAble = true;
				}else {
					status.setCode(403);
					httpServletResponse.setStatus(403);
					status.setData("用户权限不足");
				}
			}else {
				status.setCode(403);
				httpServletResponse.setStatus(403);
				status.setData("用户权限不足");
			}
			if(isAble) {
				int result = jdbcTemplate.update(addsql,pid,membername,requestAuthority.getauthority());
				if(result>0) {
					status.setCode(201);
					httpServletResponse.setStatus(201);
					AcceptAuthority acceptAuthority = new AcceptAuthority();
					acceptAuthority.setpid(pid);
					acceptAuthority.setname(membername);
					acceptAuthority.setauthority(requestAuthority.getauthority());
					JSONObject jsonObject = JSONObject.fromObject(acceptAuthority);
					
					status.setData(jsonObject.toString());
				}else {
					status.setCode(600);
					httpServletResponse.setStatus(600);
					status.setData("更新成员权限失败");
				}
			}
		}catch(Exception exception) {
			if(exception instanceof DataAccessResourceFailureException) {
				status.setCode(500);
				httpServletResponse.setStatus(500);
				status.setData("数据库连接失败");
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

