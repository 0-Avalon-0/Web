package com.application.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.application.bean.CusProject;
import com.application.bean.Member;
import com.application.bean.Menu;
import com.application.bean.PSetting;
import com.application.bean.Project;
import com.application.status.Status;

import net.sf.json.JSONObject;

@Repository
public class ProjectDao implements Iproject{
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	AuthorityDao authorityDao;
	
	
	public Status createProject(CusProject cusProject,HttpServletRequest httpServletRequest) {
		Status status = new Status();
		String sql = "insert into project values(?,?,?,?)";
		
		HttpSession httpSession = httpServletRequest.getSession();
		
		//���ж�session����user���ԣ��о�Ĭ���ѵ�¼
		if(httpSession.getAttribute("user")!=null) {
			try {
				String countsql = "select count(pid) from project";
				int count = jdbcTemplate.queryForObject(countsql, Integer.class);
				//�������еĹ�����Ŀ����pid
				int id = count;
				id++;
				
				int statuscode = jdbcTemplate.update(sql,id,cusProject.getproject_name(),cusProject.getproject_describe(),cusProject.getproject_establisher());
				if(statuscode==0) {
					status.setCode(000);
					//insert����ֵΪ0��ʧ��			
					status.setData("��������ʧ��");
				}else {
					status.setCode(201);
					Project project = new Project();
					project.setpid(id);
					project.setproject_describe(cusProject.getproject_describe());
					project.setproject_establisher(cusProject.getproject_establisher());
					project.setproject_name(cusProject.getproject_name());
					JSONObject jsonObject = JSONObject.fromObject(project);
					status.setData(jsonObject.toString());		
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
	
	//������Ϣ����
	public Status searchProject(int pid,HttpServletRequest httpServletRequest) {
		Status status = new Status();
		String sql = "select * from project where pname = ? and establisher= ?";
		
	
		
		return status;
	}
	
	//��ѯMember���Menu�������ؽ��
	//Menu���ص�һ��ڵ��Ŀ¼�������и��ڵ�Ϊroot�Ľڵ�
	public Status getSetting(int pid,HttpServletRequest httpServletRequests) {
		Status status = new Status();
		
		PSetting psetting = new PSetting();
		String sql1 = "select * from member where pid = ?";//�ҳ�Ա����
		String sql2 = "select * from project where pid = ?";//����Ŀ����
		
		HttpSession httpSession = httpServletRequests.getSession();
		//���ж�session����user���ԣ��о�Ĭ���ѵ�¼
		
		if(httpSession.getAttribute("user")!=null) {
			try {
				//�Ȼ�ȡȨ��
				//System.out.println((String)httpSession.getAttribute("user"));
				
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
					//if(isAble)
					
					//�Ȼ�ȡproject����
					List<Project> pList = jdbcTemplate.query(sql2, new Object[] {pid},new BeanPropertyRowMapper(Project.class));
					if(pList!=null&&pList.size()>0) {
						//psetting.setStatus(200);
						psetting.setproject_describe(pList.get(0).getproject_describe());
						psetting.setproject_establisher(pList.get(0).getproject_establisher());
						psetting.setproject_name(pList.get(0).getproject_name());
						
					}else {
						//û�ҵ�
						//psetting.setStatus(404);
						status.setCode(404);
						status.setData("���̲�����");
					}
					
					//��ȡ��Ա��Ϣ
					List<Member> memList = jdbcTemplate.query(sql1, new Object[] {pid},new BeanPropertyRowMapper(Member.class));
					if(memList!=null && memList.size()>0) {
						psetting.setContent(memList);
					}else {
						//û�ҵ�
						//psetting.setStatus(404);
						status.setCode(404);
						status.setData("���̲�����");
					}
					//psettingת����json����
					JSONObject jsonObject = JSONObject.fromObject(psetting);
					status.setCode(200);
					status.setData(jsonObject.toString());
					
				}else {
					//��ȡȨ��ʱ��������
					status = autStatus;
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
	
	//����Member�������ؽ��
	public Status updateSetting(int pid,PSetting pSetting,HttpServletRequest httpServletRequest) {
		Status status= new Status();
		HttpSession httpSession = httpServletRequest.getSession();
		
		String projectUpdate = "update project set project_name = ?,project_establisher = ? and project_describe = ? where pid = ?";
		String memberUpdate = "update member set membername = ?, authority = ? and description = ? where pid = ?";
		List<Member> membersToChange = pSetting.getContent();//�û�Ҫ�ĵĳ�Ա��Ϣ
		
		if(httpSession.getAttribute("user")!=null) {
			
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
					//if(isAble)
					
					String originProject = "select * from project where pid = ?";
					String originMember = "select * from member where pid = ?";
					List<Project> projectsinfo = jdbcTemplate.query(originProject,new Object[] {pid}, new BeanPropertyRowMapper(Project.class));
					List<Member> membersinfo = jdbcTemplate.query(originMember, new Object[] {pid}, new BeanPropertyRowMapper(Member.class));
					if(projectsinfo!=null && projectsinfo.size()>0) {
						Project currentProject = projectsinfo.get(0);
						//���ȸ��¹�������
						int presult = jdbcTemplate.update(projectUpdate,
								pSetting.getproject_name()==""?currentProject.getproject_name():pSetting.getproject_name(),
								pSetting.getproject_establisher()==""?currentProject.getproject_establisher():pSetting.getproject_establisher(),
										pSetting.getproject_describe()==""?currentProject.getproject_describe():pSetting.getproject_describe(),
												pid);
						if(presult>0) {
							//project���ָ��ĳɹ�
							int times = membersToChange.size();//��¼���´���
							Iterator memIterator = membersToChange.iterator();
							boolean membersuccess = true;//��¼�Ƿ�����member��¼�����³ɹ�
							//�����������ȫ����Ҫ���µ�member���ݣ���ȫ���ϴ�
							while(times>0&&memIterator.hasNext()) {
								Member temp = (Member)memIterator.next();
								Member origin = new Member();//ÿ��temp��Ӧ��Դ��Ϣ
								//����ԭmemberinfo��Դ��Ϣ
								Iterator originmemIterator = membersinfo.iterator();
								while(originmemIterator.hasNext()){
									Member oMember = (Member)originmemIterator.next();
									if(oMember.getmembername() == temp.getmembername()) {
										origin = oMember;
										break;
									}
								}
								int mresult = jdbcTemplate.update(memberUpdate,
										temp.getmembername()==""?origin.getmembername():temp.getmembername(),
										temp.getproject_authority()==-1?origin.getproject_authority():temp.getproject_authority(),
										temp.getproject_describe()==""?origin.getproject_describe():temp.getproject_describe(),
										pid);
								if(mresult>0) {
									//member���ָ��ĳɹ�
									
									
								}else {
									membersuccess = false;
									status.setCode(404);
									status.setData("���̲�����");
								}
								times--;
							}
							if(membersuccess) {
								//�������� �� ��Ա���ö��ɹ�
								JSONObject jsonObject = JSONObject.fromObject(pSetting);
								status.setCode(201);
								status.setData(jsonObject.toString());
							}
						}else {
							status.setCode(404);
							status.setData("���̲�����");
						}						
					}else {
						status.setCode(404);
						status.setData("���̲�����");
					}
				}else {
					//��ȡȨ��ʱ��������
					status = autStatus;
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
