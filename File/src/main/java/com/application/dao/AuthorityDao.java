package com.application.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.application.status.Status;

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
				status.setCode(404);
				status.setData("用户不存在");
			}
		}catch(Exception e){
			if(e instanceof DataAccessResourceFailureException) {
				status.setCode(500);
				status.setData("数据库连接失败");
			}else {
				//e.printStackTrace();
				
				status.setCode(000);
				status.setData("unknown error");
			}
		}
		
		return status;
	}

}
