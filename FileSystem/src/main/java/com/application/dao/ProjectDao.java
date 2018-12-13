package com.application.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.application.acceptbean.AcceptPid;
import com.application.acceptbean.AcceptProject;
import com.application.bean.CusProject;
import com.application.bean.Member;
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
	
	
	public Status createProject(CusProject cusProject,HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) {
		Status status = new Status();
		String sql = "insert into project values(?,?,?,?,?)";
		
		HttpSession httpSession = httpServletRequest.getSession();
		
		//仅判断session有无user属性，有就默认已登录
		if(httpSession.getAttribute("user")!=null) {
			try {
				String countsql = "select count(pid) from project";
				int count = jdbcTemplate.queryForObject(countsql, Integer.class);
				//根据已有的工程数目生成pid
				int id = count;
				id++;
				
				String project_establisher = (String) httpSession.getAttribute("user");
				
				int statuscode = jdbcTemplate.update(sql,id,cusProject.getproject_property(),cusProject.getproject_pname(),cusProject.getproject_describe(),project_establisher);
				if(statuscode==0) {
					status.setCode(600);
					//insert返回值为0，失败		
					httpServletResponse.setStatus(600);
					status.setData("创建工程失败");
				}else {
					//默认创建者为member表内管理员权限
					String memsql = "insert into member values(?,?,?)";
					int memresult = jdbcTemplate.update(memsql,id,project_establisher,2);
					if(memresult>0) {
						status.setCode(201);
						httpServletResponse.setStatus(201);
						
						PSetting pSetting = new PSetting();
						pSetting.setproject_property(cusProject.getproject_property());
						pSetting.setproject_describe(cusProject.getproject_describe());
						pSetting.setproject_establisher(project_establisher);
						pSetting.setproject_pname(cusProject.getproject_pname());
						List<Member> origin = new ArrayList<>();
						Member member = new Member();
						member.setmembername(project_establisher);
						member.setproject_authority(2);
						origin.add(member);
						pSetting.setContent(origin);
						
						JSONObject jsonObject = JSONObject.fromObject(pSetting);
						status.setData(jsonObject.toString());		
					}else {
						httpServletResponse.setStatus(600);
						status.setData("赋予管理员权限失败");
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
		}else {
			status.setCode(401);
			httpServletResponse.setStatus(401);
			status.setData("用户未登录");
		}
		
		return status;
	}
	
	public Status deleteProject(int pid,HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) {
		Status status = new Status();
		HttpSession httpSession = httpServletRequest.getSession();
		//仅判断session有无user属性，有就默认已登录
		
		if(httpSession.getAttribute("user")!=null) {
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
						if(cusAuhthority==2) {
							isAble = true;
						}												
					}else {
						//获取权限时就有问题
						status = autStatus;
						httpServletResponse.setStatus(status.getCode());
					}
					//此处需要判断是否拥有权限
					if(isAble) {
						//删除项目
						String deletesql = "delete from project where pid = ?";
						int result = jdbcTemplate.update(deletesql,pid);
						if(result>0) {
							status.setCode(204);
							httpServletResponse.setStatus(204);
							AcceptPid acceptPid = new AcceptPid();
							acceptPid.setpid(pid);
							JSONObject jsonObject = JSONObject.fromObject(acceptPid);
							status.setData(jsonObject.toString());
						}else {
							status.setCode(600);
							httpServletResponse.setStatus(600);
							status.setData("删除工程失败");
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
	
	public Status searchProject(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) {
		Status status = new Status();
		String projectsql = "select * from project where project_establisher= ?";
		String membersql = "select * from member where pid = ?";
		HttpSession httpSession = httpServletRequest.getSession();
		//仅判断session有无user属性，有就默认已登录
		if(httpSession.getAttribute("user")!=null) {
			try {
				AcceptProject acceptProject = new AcceptProject();
				List<PSetting> pSettings = new ArrayList<PSetting>();
				List<Project> projects = jdbcTemplate.query(projectsql, new Object[] {(String)httpSession.getAttribute("user")},new BeanPropertyRowMapper(Project.class));
				//此处不存在404问题，因为可能用户没有创建工程
				//遍历所有project,将对应的member添加到每一个psetting
				Iterator projectIterator = projects.iterator();
				while(projectIterator.hasNext()) {
					PSetting addPsetting = new PSetting();
					Project currentProject = (Project)projectIterator.next();
					List<Member> members = jdbcTemplate.query(membersql, new Object[] {currentProject.getPid()},new BeanPropertyRowMapper(Member.class));
					addPsetting.setContent(members);
					addPsetting.setpid(currentProject.getPid());
					addPsetting.setproject_describe(currentProject.getproject_describe());
					addPsetting.setproject_establisher(currentProject.getproject_establisher());
					addPsetting.setproject_pname(currentProject.getproject_pname());
					addPsetting.setproject_property(currentProject.getproject_property());
					pSettings.add(addPsetting);
				}
				acceptProject.setprojects(pSettings);
				status.setCode(200);
				httpServletResponse.setStatus(200);
				JSONObject jsonObject = JSONObject.fromObject(acceptProject);
				status.setData(jsonObject.toString());
				
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
	
	//查询Member表和Menu表，并返回结果
	//Menu表返回第一层节点的目录，即所有父节点为root的节点
	public Status getSetting(int pid,HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) {
		Status status = new Status();
		
		PSetting psetting = new PSetting();
		String sql1 = "select membername, project_authority from member where pid = ?";//找成员配置
		String sql2 = "select * from project where pid = ?";//找项目配置
		
		HttpSession httpSession = httpServletRequest.getSession();
		//仅判断session有无user属性，有就默认已登录
		
		if(httpSession.getAttribute("user")!=null) {
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
						//先获取project设置
						List<Project> pList = jdbcTemplate.query(sql2, new Object[] {pid},new BeanPropertyRowMapper(Project.class));
						if(pList!=null&&pList.size()>0) {
							//psetting.setStatus(200);
							psetting.setpid(pid);
							psetting.setproject_describe(pList.get(0).getproject_describe());
							psetting.setproject_establisher(pList.get(0).getproject_establisher());
							psetting.setproject_pname(pList.get(0).getproject_pname());
							psetting.setproject_property(pList.get(0).getproject_property());
						}else {
							//没找到
							//psetting.setStatus(404);
							status.setCode(404);
							httpServletResponse.setStatus(404);
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
							httpServletResponse.setStatus(404);
							status.setData("工程不存在");
						}
						//psetting转换成json存入
						JSONObject jsonObject = JSONObject.fromObject(psetting);
						status.setCode(200);
						httpServletResponse.setStatus(200);
						status.setData(jsonObject.toString());	
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
	
	//更新Member表，并返回结果
	public Status updateSetting(int pid,PSetting pSetting,HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) {
		Status status= new Status();
		HttpSession httpSession = httpServletRequest.getSession();
		
		String projectUpdate = "update project set project_name = ?,project_establisher = ?, project_property = ? and project_describe = ? where pid = ?";
		String memberUpdate = "update member set membername = ? and project_authority = ? where pid = ?";
		List<Member> membersToChange = pSetting.getContent();//用户要改的成员信息
		
		if(httpSession.getAttribute("user")!=null) {
			
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
						String originProject = "select * from project where pid = ?";
						String originMember = "select * from member where pid = ?";
						List<Project> projectsinfo = jdbcTemplate.query(originProject,new Object[] {pid}, new BeanPropertyRowMapper(Project.class));
						List<Member> membersinfo = jdbcTemplate.query(originMember, new Object[] {pid}, new BeanPropertyRowMapper(Member.class));
						if(projectsinfo!=null && projectsinfo.size()>0) {
							Project currentProject = projectsinfo.get(0);
							//首先更新工程配置
							int presult = jdbcTemplate.update(projectUpdate,
									pSetting.getproject_pname()==""?currentProject.getproject_pname():pSetting.getproject_pname(),
									currentProject.getproject_establisher(),
									pSetting.getproject_property()==-1?currentProject.getproject_property():pSetting.getproject_property(),
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
											pid);
									if(mresult>0) {
										//member部分更改成功
										
										
									}else {
										membersuccess = false;
										status.setCode(404);
										httpServletResponse.setStatus(404);
										status.setData("工程不存在");
									}
									times--;
								}
								if(membersuccess) {
									//工程配置 和 成员配置都成功
									JSONObject jsonObject = JSONObject.fromObject(pSetting);
									status.setCode(201);
									httpServletResponse.setStatus(201);
									status.setData(jsonObject.toString());
								}
							}else {
								status.setCode(404);
								httpServletResponse.setStatus(404);
								status.setData("工程不存在");
							}						
						}else {
							status.setCode(404);
							httpServletResponse.setStatus(404);
							status.setData("工程不存在");
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

