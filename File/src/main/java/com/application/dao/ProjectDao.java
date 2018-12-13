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
		
		//仅判断session有无user属性，有就默认已登录
		if(httpSession.getAttribute("user")!=null) {
			try {
				String countsql = "select count(pid) from project";
				int count = jdbcTemplate.queryForObject(countsql, Integer.class);
				//根据已有的工程数目生成pid
				int id = count;
				id++;
				
				int statuscode = jdbcTemplate.update(sql,id,cusProject.getproject_name(),cusProject.getproject_describe(),cusProject.getproject_establisher());
				if(statuscode==0) {
					status.setCode(000);
					//insert返回值为0，失败			
					status.setData("创建工程失败");
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
	
	//返回信息包括
	public Status searchProject(int pid,HttpServletRequest httpServletRequest) {
		Status status = new Status();
		String sql = "select * from project where pname = ? and establisher= ?";
		
	
		
		return status;
	}
	
	//查询Member表和Menu表，并返回结果
	//Menu表返回第一层节点的目录，即所有父节点为root的节点
	public Status getSetting(int pid,HttpServletRequest httpServletRequests) {
		Status status = new Status();
		
		PSetting psetting = new PSetting();
		String sql1 = "select * from member where pid = ?";//找成员配置
		String sql2 = "select * from project where pid = ?";//找项目配置
		
		HttpSession httpSession = httpServletRequests.getSession();
		//仅判断session有无user属性，有就默认已登录
		
		if(httpSession.getAttribute("user")!=null) {
			try {
				//先获取权限
				//System.out.println((String)httpSession.getAttribute("user"));
				
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
					//if(isAble)
					
					//先获取project设置
					List<Project> pList = jdbcTemplate.query(sql2, new Object[] {pid},new BeanPropertyRowMapper(Project.class));
					if(pList!=null&&pList.size()>0) {
						//psetting.setStatus(200);
						psetting.setproject_describe(pList.get(0).getproject_describe());
						psetting.setproject_establisher(pList.get(0).getproject_establisher());
						psetting.setproject_name(pList.get(0).getproject_name());
						
					}else {
						//没找到
						//psetting.setStatus(404);
						status.setCode(404);
						status.setData("工程不存在");
					}
					
					//获取成员信息
					List<Member> memList = jdbcTemplate.query(sql1, new Object[] {pid},new BeanPropertyRowMapper(Member.class));
					if(memList!=null && memList.size()>0) {
						psetting.setContent(memList);
					}else {
						//没找到
						//psetting.setStatus(404);
						status.setCode(404);
						status.setData("工程不存在");
					}
					//psetting转换成json存入
					JSONObject jsonObject = JSONObject.fromObject(psetting);
					status.setCode(200);
					status.setData(jsonObject.toString());
					
				}else {
					//获取权限时就有问题
					status = autStatus;
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
	
	//更新Member表，并返回结果
	public Status updateSetting(int pid,PSetting pSetting,HttpServletRequest httpServletRequest) {
		Status status= new Status();
		HttpSession httpSession = httpServletRequest.getSession();
		
		String projectUpdate = "update project set project_name = ?,project_establisher = ? and project_describe = ? where pid = ?";
		String memberUpdate = "update member set membername = ?, authority = ? and description = ? where pid = ?";
		List<Member> membersToChange = pSetting.getContent();//用户要改的成员信息
		
		if(httpSession.getAttribute("user")!=null) {
			
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
					//if(isAble)
					
					String originProject = "select * from project where pid = ?";
					String originMember = "select * from member where pid = ?";
					List<Project> projectsinfo = jdbcTemplate.query(originProject,new Object[] {pid}, new BeanPropertyRowMapper(Project.class));
					List<Member> membersinfo = jdbcTemplate.query(originMember, new Object[] {pid}, new BeanPropertyRowMapper(Member.class));
					if(projectsinfo!=null && projectsinfo.size()>0) {
						Project currentProject = projectsinfo.get(0);
						//首先更新工程配置
						int presult = jdbcTemplate.update(projectUpdate,
								pSetting.getproject_name()==""?currentProject.getproject_name():pSetting.getproject_name(),
								pSetting.getproject_establisher()==""?currentProject.getproject_establisher():pSetting.getproject_establisher(),
										pSetting.getproject_describe()==""?currentProject.getproject_describe():pSetting.getproject_describe(),
												pid);
						if(presult>0) {
							//project部分更改成功
							int times = membersToChange.size();//记录更新次数
							Iterator memIterator = membersToChange.iterator();
							boolean membersuccess = true;//记录是否所有member记录都更新成功
							//具体操作，补全所有要更新的member数据，再全部上传
							while(times>0&&memIterator.hasNext()) {
								Member temp = (Member)memIterator.next();
								Member origin = new Member();//每个temp对应的源信息
								//遍历原memberinfo找源信息
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
									//member部分更改成功
									
									
								}else {
									membersuccess = false;
									status.setCode(404);
									status.setData("工程不存在");
								}
								times--;
							}
							if(membersuccess) {
								//工程配置 和 成员配置都成功
								JSONObject jsonObject = JSONObject.fromObject(pSetting);
								status.setCode(201);
								status.setData(jsonObject.toString());
							}
						}else {
							status.setCode(404);
							status.setData("工程不存在");
						}						
					}else {
						status.setCode(404);
						status.setData("工程不存在");
					}
				}else {
					//获取权限时就有问题
					status = autStatus;
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
