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
				//�Ȼ�ȡȨ��
				Status autStatus = authorityDao.getAuthority(pid, (String)httpSession.getAttribute("user"));
				
				if(autStatus.getCode()==200) {
					//�ж�Ȩ��				
					//��Ҫ���о�Ȩ������							
					int cusAhthority = Integer.valueOf(autStatus.getData());
					boolean isAble = false;//�Ƿ���Ȩ��
					switch (cusAhthority) {
					case 1:
						
						break;

					default:
						break;
					}
					
					//�˴���Ҫ�ж��Ƿ�ӵ��Ȩ��
					//if(isAble) else 403
					
					List<Menu> menuinfo = jdbcTemplate.query(sql, new Object[] {pid,filenode.toString()},new BeanPropertyRowMapper(Menu.class));
					if(menuinfo!=null && menuinfo.size()>0) {
						//��ȡfile_text
						fileText.setfile_text(menuinfo.get(0).getfile_text());
						JSONObject jsonObject = JSONObject.fromObject(fileText);
						
						status.setCode(200);
						status.setData(jsonObject.toString());
					}else {
						status.setCode(404);
						status.setData("�ĵ�������");
					}						
				}
			}catch(Exception exception) {
				if(exception instanceof DataAccessResourceFailureException) {
					status.setCode(500);
					status.setData("���ݿ�����ʧ��");
				}else {
					exception.printStackTrace();
					
					status.setCode(000);
					status.setData("unknown error");
				}
			}
		}else {
			status.setCode(401);
			status.setData("�û�δ��¼");	
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
				//�Ȼ�ȡȨ��
				Status autStatus = authorityDao.getAuthority(pid, (String)httpSession.getAttribute("user"));
				
				if(autStatus.getCode()==200) {
					//�ж�Ȩ��				
					//��Ҫ���о�Ȩ������							
					int cusAhthority = Integer.valueOf(autStatus.getData());
					boolean isAble = false;//�Ƿ���Ȩ��
					switch (cusAhthority) {
					case 1:
						
						break;

					default:
						break;
					}
					
					//�˴���Ҫ�ж��Ƿ�ӵ��Ȩ��
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
					
					//�쳣�����ظ��ļ����Ĵ���
				}
			}catch(Exception exception) {
				if(exception instanceof DataAccessResourceFailureException) {
					status.setCode(500);
					status.setData("���ݿ�����ʧ��");
				}else if(exception instanceof DuplicateKeyException){
					//exception.printStackTrace();
					status.setCode(422);
					status.setData("�û����ظ�");
				}else {
					exception.printStackTrace();
					status.setCode(000);
					status.setData("unknown error");
				}
			}
		}else {
			status.setCode(401);
			status.setData("�û�δ��¼");	
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
				//�Ȼ�ȡȨ��
				Status autStatus = authorityDao.getAuthority(pid, (String)httpSession.getAttribute("user"));
				
				if(autStatus.getCode()==200) {
					//�ж�Ȩ��				
					//��Ҫ���о�Ȩ������							
					int cusAhthority = Integer.valueOf(autStatus.getData());
					boolean isAble = false;//�Ƿ���Ȩ��
					switch (cusAhthority) {
					case 1:
						
						break;

					default:
						break;
					}
					
					//�˴���Ҫ�ж��Ƿ�ӵ��Ȩ��
					//if(isAble) else 403
					
					int result = jdbcTemplate.update(sql,fileText.getfile_text(),pid,filenode.toString());
					if(result>0) {
						status.setCode(201);
						status.setData("�޸ĳɹ�");
					}else {
						status.setCode(404);
						status.setData("�ĵ�������");
					}
					
				}
			}catch(Exception exception) {
				if(exception instanceof DataAccessResourceFailureException) {
					status.setCode(500);
					status.setData("���ݿ�����ʧ��");
				}else {
					exception.printStackTrace();
					status.setCode(000);
					status.setData("unknown error");
				}
			}
		}else {
			status.setCode(401);
			status.setData("�û�δ��¼");	
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
				//�Ȼ�ȡȨ��
				Status autStatus = authorityDao.getAuthority(pid, (String)httpSession.getAttribute("user"));
				
				if(autStatus.getCode()==200) {
					//�ж�Ȩ��				
					//��Ҫ���о�Ȩ������							
					int cusAhthority = Integer.valueOf(autStatus.getData());
					boolean isAble = false;//�Ƿ���Ȩ��
					switch (cusAhthority) {
					case 1:
						
						break;

					default:
						break;
					}
					
					//�˴���Ҫ�ж��Ƿ�ӵ��Ȩ��
					//if(isAble) else 403
					int result = jdbcTemplate.update(sql,pid,filenode.toString());
					if(result>0) {
						status.setCode(204);
						status.setData("ɾ���ɹ�");
					}else {
						status.setCode(404);
						status.setData("�ĵ�������");
					}			
				}
			}catch(Exception exception) {
				if(exception instanceof DataAccessResourceFailureException) {
					status.setCode(500);
					status.setData("���ݿ�����ʧ��");
				}else {
					exception.printStackTrace();
					status.setCode(000);
					status.setData("unknown error");
				}
			}
		}else {
			status.setCode(401);
			status.setData("�û�δ��¼");	
		}
		
		return status;
	}

}
