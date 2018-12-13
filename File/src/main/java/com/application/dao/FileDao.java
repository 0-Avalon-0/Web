package com.application.dao;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.application.bean.FileText;
import com.application.bean.Menu;
import com.application.status.Status;

import net.sf.json.JSONObject;
import sun.print.resources.serviceui;

@Repository
public class FileDao implements IFile{
	@Autowired 
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private AuthorityDao authorityDao;
	
	public Status getFile(int pid, String filename, String path,HttpServletRequest httpServletRequest) {
		Status status = new Status();
		FileText fileText = new FileText();
		String sql = "select * from menu where pid = ? and file_node = ?";
		StringBuffer filenode = new StringBuffer();
		filenode.append(path);
		filenode.append('\\');
		filenode.append(filename);
		
		HttpSession httpSession = httpServletRequest.getSession();
		
		if(httpSession.getAttribute("user")!=null ) {
			try {
				//先获取权限
				Status autStatus = authorityDao.getAuthority(pid, (String)httpSession.getAttribute("user"));
				
				if(autStatus.getCode()==200) {
					//判断权限				
					//需要再研究权限问题							
					int cusAhthority = Integer.valueOf(autStatus.getData());
					boolean isAble = false;//是否有权限
					switch (cusAhthority) {
					case 1:
						
						break;

					default:
						break;
					}
					
					//此处需要判断是否拥有权限
					//if(isAble) else 403
					
					List<Menu> menuinfo = jdbcTemplate.query(sql, new Object[] {pid,filenode.toString()},new BeanPropertyRowMapper(Menu.class));
					if(menuinfo!=null && menuinfo.size()>0) {
						//提取file_text
						fileText.setfile_text(menuinfo.get(0).getfile_text());
						JSONObject jsonObject = JSONObject.fromObject(fileText);
						
						status.setCode(200);
						status.setData(jsonObject.toString());
					}else {
						status.setCode(404);
						status.setData("文档不存在");
					}						
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

	public Status createFile(int pid, String filename, String path,Menu menu,HttpServletRequest httpServletRequest) {
		Status status = new Status();
		String sql = "insert into menu values(?,?,?,?,?,?)";
		StringBuffer filenode = new StringBuffer();
		filenode.append(path);
		filenode.append('\\');
		filenode.append(filename);
		
		HttpSession httpSession = httpServletRequest.getSession();
		
		if(httpSession.getAttribute("user")!=null ) {
			try {
				//先获取权限
				Status autStatus = authorityDao.getAuthority(pid, (String)httpSession.getAttribute("user"));
				
				if(autStatus.getCode()==200) {
					//判断权限				
					//需要再研究权限问题							
					int cusAhthority = Integer.valueOf(autStatus.getData());
					boolean isAble = false;//是否有权限
					switch (cusAhthority) {
					case 1:
						
						break;

					default:
						break;
					}
					
					//此处需要判断是否拥有权限
					//if(isAble) else 403
					
					int result = jdbcTemplate.update(sql,pid,menu.getfile_fname(),
							menu.getfile_node(),
							menu.getfile_parentnode(),
							menu.getfile_property(),
							menu.getfile_text());
					if(result>0) {
						status.setCode(201);
						JSONObject jsonObject = JSONObject.fromObject(menu);
						status.setData(jsonObject.toString());
					}
					
					//异常处理重复文件名的错误
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

	public Status changeFile(int pid, String filename, String path,FileText fileText,HttpServletRequest httpServletRequest) {
		Status status = new Status();
		String sql = "update menu set file_text = ? where pid = ? and file_node = ?";
		StringBuffer filenode = new StringBuffer();
		filenode.append(path);
		filenode.append('\\');
		filenode.append(filename);
		
		HttpSession httpSession = httpServletRequest.getSession();
		
		if(httpSession.getAttribute("user")!=null ) {
			try {
				//先获取权限
				Status autStatus = authorityDao.getAuthority(pid, (String)httpSession.getAttribute("user"));
				
				if(autStatus.getCode()==200) {
					//判断权限				
					//需要再研究权限问题							
					int cusAhthority = Integer.valueOf(autStatus.getData());
					boolean isAble = false;//是否有权限
					switch (cusAhthority) {
					case 1:
						
						break;

					default:
						break;
					}
					
					//此处需要判断是否拥有权限
					//if(isAble) else 403
					
					int result = jdbcTemplate.update(sql,fileText.getfile_text(),pid,filenode.toString());
					if(result>0) {
						status.setCode(201);
						status.setData("修改成功");
					}else {
						status.setCode(404);
						status.setData("文档不存在");
					}
					
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

	public Status deleteFile(int pid, String filename, String path,HttpServletRequest httpServletRequest) {
		Status status = new Status();
		String sql = "delete from menu where pid = ? and file_node = ?";
		StringBuffer filenode = new StringBuffer();
		filenode.append(path);
		filenode.append('\\');
		filenode.append(filename);
		
		HttpSession httpSession = httpServletRequest.getSession();
		
		if(httpSession.getAttribute("user")!=null ) {
			try {
				//先获取权限
				Status autStatus = authorityDao.getAuthority(pid, (String)httpSession.getAttribute("user"));
				
				if(autStatus.getCode()==200) {
					//判断权限				
					//需要再研究权限问题							
					int cusAhthority = Integer.valueOf(autStatus.getData());
					boolean isAble = false;//是否有权限
					switch (cusAhthority) {
					case 1:
						
						break;

					default:
						break;
					}
					
					//此处需要判断是否拥有权限
					//if(isAble) else 403
					int result = jdbcTemplate.update(sql,pid,filenode.toString());
					if(result>0) {
						status.setCode(204);
						status.setData("删除成功");
					}else {
						status.setCode(404);
						status.setData("文档不存在");
					}			
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

}
