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

import com.application.acceptbean.AcceptFile;
import com.application.acceptbean.AcceptText;
import com.application.acceptbean.SimpleText;
import com.application.bean.FileText;
import com.application.bean.Menu;
import com.application.bean.Project;
import com.application.status.Status;

import net.sf.json.JSONObject;

@Repository
public class FileDao implements IFile{
	@Autowired 
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private AuthorityDao authorityDao;
	
	public Status getFile(int pid, String filename, String path,HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) {
		Status status = new Status();
		AcceptFile acceptFile = new AcceptFile();
		String sql = "select * from menu where pid = ? and file_node = ?";
		StringBuffer filenode = new StringBuffer();
		filenode.append(path);
		filenode.append('\\');
		filenode.append(filename);
		
		HttpSession httpSession = httpServletRequest.getSession();
		
		if(httpSession.getAttribute("user")!=null ) {
			try {
				//���ж���Ŀ��˽��
				String projectQuery = "select * from project where pid = ?";
				List<Project> projectsList = jdbcTemplate.query(projectQuery, new Object[] {pid},new BeanPropertyRowMapper(Project.class));
				if(projectsList!=null&&projectsList.size()>0) {
					int projectproperty = projectsList.get(0).getproject_property();
					
					boolean isAble = false;//�Ƿ���Ȩ��
					if(projectproperty==0) {
						//������Ŀ�������˿���ֱ�Ӳ鿴�ĵ�
						isAble = true;
					}else {
						//˽����Ŀ
						//�ٻ�ȡȨ��
						Status autStatus = authorityDao.getAuthority(pid, (String)httpSession.getAttribute("user"));
						if(autStatus.getCode()==200) {
							//�ж�Ȩ��									
							int cusAuhthority = Integer.valueOf(autStatus.getData());
							if(cusAuhthority>=0) {
								isAble = true;
							}												
						}else {
							//��ȡȨ��ʱ��������
							status = autStatus;
							httpServletResponse.setStatus(status.getCode());
						}
					}
					//�˴���Ҫ�ж��Ƿ�ӵ��Ȩ��
					if(isAble) {
						List<Menu> menuinfo = jdbcTemplate.query(sql, new Object[] {pid,filenode.toString()},new BeanPropertyRowMapper(Menu.class));
						if(menuinfo!=null && menuinfo.size()>0) {
							//��ȡfile_text
							acceptFile.setfile_text(menuinfo.get(0).getfile_text());
							acceptFile.setfname(menuinfo.get(0).getfile_fname());
							acceptFile.setpid(menuinfo.get(0).getPid());
							acceptFile.setpath(path);
							JSONObject jsonObject = JSONObject.fromObject(acceptFile);
							
							status.setCode(200);
							httpServletResponse.setStatus(200);
							status.setData(jsonObject.toString());
						}else {
							status.setCode(404);
							httpServletResponse.setStatus(404);
							status.setData("�ĵ�������");
						}		
					}else {
						status.setCode(403);
						httpServletResponse.setStatus(403);
						status.setData("�û�Ȩ�޲���");
					}	
				}else {
					status.setCode(404);
					httpServletResponse.setStatus(404);
					status.setData("���̲�����");
				}		
			}catch(Exception exception) {
				if(exception instanceof DataAccessResourceFailureException) {
					status.setCode(500);
					httpServletResponse.setStatus(500);
					status.setData("���ݿ�����ʧ��");
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
			status.setData("�û�δ��¼");	
		}
		
		return status;
	}

	public Status createFile(int pid, String filename, String path,SimpleText simpleText,HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) {
		Status status = new Status();
		String sql = "insert into menu values(?,?,?,?,?,?)";
		StringBuffer filenode = new StringBuffer();
		filenode.append(path);
		filenode.append('\\');
		filenode.append(filename);
		
		Menu menu = new Menu();
		menu.setfile_fname(filename);
		menu.setfile_node(filenode.toString());
		menu.setfile_parentnode(path);
		menu.setfile_property(simpleText.getfile_property());
		menu.setfile_text(simpleText.getfile_text());
		menu.setPid(pid);
		
		HttpSession httpSession = httpServletRequest.getSession();
		
		if(httpSession.getAttribute("user")!=null ) {
			try {
				//���ж���Ŀ�Ƿ����
				String projectQuery = "select * from project where pid = ?";
				List<Project> projectsList = jdbcTemplate.query(projectQuery, new Object[] {pid},new BeanPropertyRowMapper(Project.class));
				if(projectsList!=null&&projectsList.size()>0) {
					int projectproperty = projectsList.get(0).getproject_property();
					
					boolean isAble = false;//�Ƿ���Ȩ��
					
					//���˲鿴�ĵ����ⶼ����Ҫ�ֹ�˽����Ŀ
					Status autStatus = authorityDao.getAuthority(pid, (String)httpSession.getAttribute("user"));
					if(autStatus.getCode()==200) {
						//�ж�Ȩ��									
						int cusAuhthority = Integer.valueOf(autStatus.getData());
						if(cusAuhthority>=1) {
							isAble = true;
						}												
					}else {
						//��ȡȨ��ʱ��������
						status = autStatus;
						httpServletResponse.setStatus(status.getCode());
					}
					//�˴���Ҫ�ж��Ƿ�ӵ��Ȩ��
					if(isAble) {
						int result = jdbcTemplate.update(sql,pid,menu.getfile_fname(),
								menu.getfile_node(),
								menu.getfile_parentnode(),
								menu.getfile_property(),
								menu.getfile_text());
						if(result>0) {
							status.setCode(201);
							httpServletResponse.setStatus(201);
							JSONObject jsonObject = JSONObject.fromObject(menu);
							status.setData(jsonObject.toString());
						}
						
						//�쳣�����ظ��ļ����Ĵ���
					}
				}else {
					status.setCode(404);
					httpServletResponse.setStatus(404);
					status.setData("���̲�����");
				}					
			}catch(Exception exception) {
				if(exception instanceof DataAccessResourceFailureException) {
					status.setCode(500);
					httpServletResponse.setStatus(500);
					status.setData("���ݿ�����ʧ��");
				}else if(exception instanceof DuplicateKeyException){
					//exception.printStackTrace();
					status.setCode(422);
					httpServletResponse.setStatus(422);
					status.setData("�ļ����ظ�");
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
			status.setData("�û�δ��¼");	
		}
		
		return status;
	}

	public Status changeFile(int pid, String filename, String path,FileText fileText,HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) {
		Status status = new Status();
		String sql = "update menu set file_text = ? where pid = ? and file_node = ?";
		StringBuffer filenode = new StringBuffer();
		filenode.append(path);
		filenode.append('\\');
		filenode.append(filename);
		
		HttpSession httpSession = httpServletRequest.getSession();
		
		if(httpSession.getAttribute("user")!=null ) {
			try {
				//���ж���Ŀ�Ƿ����
				String projectQuery = "select * from project where pid = ?";
				List<Project> projectsList = jdbcTemplate.query(projectQuery, new Object[] {pid},new BeanPropertyRowMapper(Project.class));
				if(projectsList!=null&&projectsList.size()>0) {
					int projectproperty = projectsList.get(0).getproject_property();
					
					boolean isAble = false;//�Ƿ���Ȩ��
					
					//���˲鿴�ĵ����ⶼ����Ҫ�ֹ�˽����Ŀ
					Status autStatus = authorityDao.getAuthority(pid, (String)httpSession.getAttribute("user"));
					if(autStatus.getCode()==200) {
						//�ж�Ȩ��									
						int cusAuhthority = Integer.valueOf(autStatus.getData());
						if(cusAuhthority>=1) {
							isAble = true;
						}												
					}else {
						//��ȡȨ��ʱ��������
						status = autStatus;
						httpServletResponse.setStatus(status.getCode());
					}
					if(isAble) {
						int result = jdbcTemplate.update(sql,fileText.getfile_text(),pid,filenode.toString());
						if(result>0) {
							status.setCode(201);
							httpServletResponse.setStatus(201);
							AcceptText acceptText = new AcceptText();
							acceptText.setfname(filename);
							acceptText.setpath(path);
							acceptText.setpid(pid);
							JSONObject jsonObject = JSONObject.fromObject(acceptText);
							
							status.setData(jsonObject.toString());
						}else {
							status.setCode(404);
							httpServletResponse.setStatus(404);
							status.setData("�ĵ�������");
						}
					}
				}else {
					status.setCode(404);
					httpServletResponse.setStatus(404);
					status.setData("���̲�����");
				}	
			}catch(Exception exception) {
				if(exception instanceof DataAccessResourceFailureException) {
					status.setCode(500);
					httpServletResponse.setStatus(500);
					status.setData("���ݿ�����ʧ��");
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
			status.setData("�û�δ��¼");	
		}
		
		return status;
	}

	public Status deleteFile(int pid, String filename, String path,HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) {
		Status status = new Status();
		String sql = "delete from menu where pid = ? and file_node = ?";
		StringBuffer filenode = new StringBuffer();
		filenode.append(path);
		filenode.append('\\');
		filenode.append(filename);
		
		HttpSession httpSession = httpServletRequest.getSession();
		
		if(httpSession.getAttribute("user")!=null ) {
			try {
				//���ж���Ŀ�Ƿ����
				String projectQuery = "select * from project where pid = ?";
				List<Project> projectsList = jdbcTemplate.query(projectQuery, new Object[] {pid},new BeanPropertyRowMapper(Project.class));
				if(projectsList!=null&&projectsList.size()>0) {
					int projectproperty = projectsList.get(0).getproject_property();
					
					boolean isAble = false;//�Ƿ���Ȩ��
					
					//���˲鿴�ĵ����ⶼ����Ҫ�ֹ�˽����Ŀ
					Status autStatus = authorityDao.getAuthority(pid, (String)httpSession.getAttribute("user"));
					if(autStatus.getCode()==200) {
						//�ж�Ȩ��									
						int cusAuhthority = Integer.valueOf(autStatus.getData());
						if(cusAuhthority>=1) {
							isAble = true;
						}												
					}else {
						//��ȡȨ��ʱ��������
						status = autStatus;
						httpServletResponse.setStatus(status.getCode());
					}
					
					if(isAble) {
						int result = jdbcTemplate.update(sql,pid,filenode.toString());
						if(result>0) {
							status.setCode(204);
							httpServletResponse.setStatus(204);
							AcceptText acceptText = new AcceptText();
							acceptText.setfname(filename);
							acceptText.setpath(path);
							acceptText.setpid(pid);
							JSONObject jsonObject = JSONObject.fromObject(acceptText);
							
							status.setData(jsonObject.toString());
						}else {
							status.setCode(404);
							httpServletResponse.setStatus(404);
							status.setData("�ĵ�������");
						}		
					}
				}else {
					status.setCode(404);
					httpServletResponse.setStatus(404);
					status.setData("���̲�����");
				}
			}catch(Exception exception) {
				if(exception instanceof DataAccessResourceFailureException) {
					status.setCode(500);
					httpServletResponse.setStatus(500);
					status.setData("���ݿ�����ʧ��");
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
			status.setData("�û�δ��¼");	
		}
		
		return status;
	}

}

