package com.application.dao;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.application.acceptbean.AcceptAllFiles;
import com.application.acceptbean.AcceptFileName;
import com.application.acceptbean.AcceptText;
import com.application.acceptbean.SimpleFile;
import com.application.bean.Menu;
import com.application.bean.Project;
import com.application.status.Status;

import net.sf.json.JSONObject;

@Repository
public class MenuDao implements Imenu{
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	AuthorityDao authorityDao;

	@Override
	public Status getFiles(String path, int pid, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		Status status = new Status();
		AcceptAllFiles acceptAllFiles = new AcceptAllFiles();
		acceptAllFiles.setpath(path);
		acceptAllFiles.setpid(pid);
		HttpSession httpSession = httpServletRequest.getSession();
		if(httpSession.getAttribute("user")!=null ) {
			try {
				//先判断项目是否存在
				String projectQuery = "select * from project where pid = ?";
				List<Project> projectsList = jdbcTemplate.query(projectQuery, new Object[] {pid},new BeanPropertyRowMapper(Project.class));
				if(projectsList!=null&&projectsList.size()>0) {
					int projectproperty = projectsList.get(0).getproject_property();
					
					boolean isAble = false;//是否有权限
					
					//除了查看文档以外都不需要分公私有项目
					Status autStatus = authorityDao.getAuthority(pid, (String)httpSession.getAttribute("user"));
					if(autStatus.getCode()==200) {
						//判断权限									
						int cusAuhthority = Integer.valueOf(autStatus.getData());
						if(cusAuhthority>=1) {
							isAble = true;
						}												
					}else {
						//获取权限时就有问题
						status = autStatus;
						httpServletResponse.setStatus(status.getCode());
					}
					//此处需要判断是否拥有权限
					if(isAble) {
						String sql = "select file_fname,file_property from menu where file_parentnode = ?";
						List<SimpleFile> simpleFiles = jdbcTemplate.query(sql, new Object[] {path},new BeanPropertyRowMapper(SimpleFile.class));
						if(simpleFiles!=null && simpleFiles.size()>=0) {
							acceptAllFiles.setfiles(simpleFiles);
							status.setCode(200);
							httpServletResponse.setStatus(200);
							JSONObject jsonObject = JSONObject.fromObject(acceptAllFiles);
							status.setData(jsonObject.toString());									
						}else {
							status.setCode(600);
							httpServletResponse.setStatus(600);
							status.setData("查询文件目录失败");							
						}					
					}		
				}else {
					status.setCode(404);
					httpServletResponse.setStatus(404);
					status.setData("工程不存在");
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
		}else {
			status.setCode(401);
			httpServletResponse.setStatus(401);
			status.setData("用户未登录");	
		}
		return status;
	}

	@Override
	public Status renameFile(String filename, String path, int pid, AcceptFileName acceptFileName,HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		Status status = new Status();
		
		StringBuffer filenode = new StringBuffer();
		filenode.append(path);
		filenode.append('\\');
		filenode.append(filename);
		
		
		HttpSession httpSession = httpServletRequest.getSession();
		if(httpSession.getAttribute("user")!=null ) {
			try {
				//先判断项目是否存在
				String projectQuery = "select * from project where pid = ?";
				List<Project> projectsList = jdbcTemplate.query(projectQuery, new Object[] {pid},new BeanPropertyRowMapper(Project.class));
				if(projectsList!=null&&projectsList.size()>0) {
					int projectproperty = projectsList.get(0).getproject_property();
					
					boolean isAble = false;//是否有权限
					
					//除了查看文档以外都不需要分公私有项目
					Status autStatus = authorityDao.getAuthority(pid, (String)httpSession.getAttribute("user"));
					if(autStatus.getCode()==200) {
						//判断权限									
						int cusAuhthority = Integer.valueOf(autStatus.getData());
						if(cusAuhthority>=1) {
							isAble = true;
						}												
					}else {
						//获取权限时就有问题
						status = autStatus;
						httpServletResponse.setStatus(status.getCode());
					}
					//此处需要判断是否拥有权限
					if(isAble) {
						String getMenu = "select * from menu where pid = ? and file_node = ?";
						String sql = "update menu set file_fname = ?, file_node = ?, file_parentnode = ?,file_property=? and file_text = ? where pid = ?";
						List<Menu> menus = jdbcTemplate.query(getMenu, new Object[] {pid,filenode.toString()},new BeanPropertyRowMapper(Menu.class));
						if(menus!=null && menus.size()>=0) {
							Menu currentMenu = menus.get(0);
							int result = jdbcTemplate.update(sql,filename,filenode.toString(),currentMenu.getfile_parentnode(),currentMenu.getfile_property(),pid);
							if(result>0) {
								status.setCode(201);
								httpServletResponse.setStatus(201);
								AcceptText acceptText = new AcceptText();
								JSONObject jsonObject = JSONObject.fromObject(acceptText);
								status.setData(jsonObject.toString());	
							}else {
								status.setCode(600);
								httpServletResponse.setStatus(600);
								status.setData("更改文件名失败");
							}									
						}else {
							status.setCode(600);
							httpServletResponse.setStatus(600);
							status.setData("查询文件目录失败");							
						}					
					}		
				}else {
					status.setCode(404);
					httpServletResponse.setStatus(404);
					status.setData("工程不存在");
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
		}else {
			status.setCode(401);
			httpServletResponse.setStatus(401);
			status.setData("用户未登录");	
		}
		
		return status;
	}

}
