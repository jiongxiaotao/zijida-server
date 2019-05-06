
package tch.zijidaserver.service;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tch.zijidaserver.dao.Code2SessionDao;
import tch.zijidaserver.dao.ProjectDao;
import tch.zijidaserver.dao.ScoreDao;
import tch.zijidaserver.dao.UserInfoDao;
import tch.zijidaserver.entity.Project;
import tch.zijidaserver.entity.UserInfo;
import tch.zijidaserver.entity.UserSession;
import tch.zijidaserver.utils.AESUtil;

import java.util.List;
import java.util.Map;

@Service
public class ProjectService {

	@Autowired
	private MiniService miniService;
	@Autowired
	private Code2SessionDao code2SessionDao;
	@Autowired
	private UserInfoDao userInfoDao;
	@Autowired
	private ProjectDao projectDao;
	@Autowired
	private ScoreDao scoreDao;
	@Autowired
	private ProjectManageService projectManageService;
	private Log log = LogFactory.getLog(ProjectService.class);
	//查询项目列表
	public Map<String, Object> queryProjectList(String loginCode,String statusList) {
		log.info("getProject开始：loginCode=" + loginCode);
		Map<String, Object> result;
		List<Project> projectList;
		UserSession userSession=code2SessionDao.getSessionByLoginCode(loginCode);
		/****测试查询用户信息时跳过查cache*****/
		//UserSession userSession=code2SessionDao.getSessionByLoginCodeTest(loginCode);
		// 没获取到缓存，说明code失效了，让前段自己从登陆
		if(userSession != null){
			String unionId=userInfoDao.getByOpenIdStatus(userSession.getOpenid(), "9").getUnion_id();
			if(unionId!=null){
				// 查询项目详情
				log.info("查询项目列表：unionId="+unionId+",statusList=" + statusList);
				//查所有
				if(statusList.equals(""))
					projectList=projectDao.queryUserProjectList(unionId);
					//按statusList查
				else
					projectList= projectDao.queryUserProjectListByStatus(unionId,statusList);
				if (projectList != null) {
					result = miniService.newSuccessResponseMap();
					result.put("count", projectList.size());
					result.put("projectList", projectList);
				}
				else
					result = miniService.newErrorResponseMap(2001,"查询项目列表失败!");
			}
			else
				result = miniService.newErrorResponseMap(9003,"查询用户编号失败!");
		}
		else {
			log.info("code已失效!logincode=" + loginCode);
			result = miniService.newErrorResponseMap(9001, "code已失效!");
		}
		return result;
	}
	//根据邀请码查询项目
	public Map<String, Object> queryProjectByInviteCode(String loginCode,String inviteCode) {
		log.info("getProject开始：loginCode=" + loginCode);
		Map<String, Object> result;
		UserSession userSession=code2SessionDao.getSessionByLoginCode(loginCode);
		/****测试查询用户信息时跳过查cache*****/
		//UserSession userSession=code2SessionDao.getSessionByLoginCodeTest(loginCode);
		// 没获取到缓存，说明code失效了，让前段自己从登陆
		if(userSession != null){
			String unionId=userInfoDao.getByOpenIdStatus(userSession.getOpenid(), "9").getUnion_id();
			if(unionId!=null) {
				// 查询项目详情
				log.info("查询项目：inviteCode=" + inviteCode);
				Project project = projectDao.queryProjectByInviteCode(inviteCode);
				//未查到符合的项目
				if (project == null) {
					result = miniService.newErrorResponseMap(2010, "未查到符合的项目!");
				} else {
					//该项目还未发布
					if (project.getStatus().equals("0"))
						result = miniService.newErrorResponseMap(2011, "该项目还未发布!");
					else {
						//查询当前用户是否已对该项目进行过评分
						int doneFlag = scoreDao.queryUserProjectDone(project.getId(), unionId);
						if (doneFlag != -1) {
							result = miniService.newSuccessResponseMap();
							result.put("id", project.getId());
							result.put("name", project.getName());
							result.put("status", project.getStatus());
							result.put("done_amount", project.getDone_amount());
							result.put("amount", project.getAmount());
							result.put("curUserDone", doneFlag);
						} else
							result = miniService.newErrorResponseMap(2012, "获取用户是否已评分失败!");
					}
				}
			}
			else
				result = miniService.newErrorResponseMap(9003,"查询用户编号失败!");
		}
		else {
			log.info("code已失效!logincode=" + loginCode);
			result = miniService.newErrorResponseMap(9001, "code已失效!");
		}
		return result;
	}

	//新增项目
	public Map<String, Object> insertProject(String loginCode,Project project) {
		Map<String, Object> result;
		log.info("addProject：loginCode=" + loginCode);
		UserSession userSession= code2SessionDao.getSessionByLoginCode(loginCode);
		/****测试查询用户信息时跳过查cache*****/
		//UserSession userSession=code2SessionDao.getSessionByLoginCodeTest(loginCode);
		// 主要是判断缓存里还有没有
		if(userSession != null){
			//根据openid在数据库获取union_id,session里一开始并不会保存union_id
			String unionId=userInfoDao.getByOpenIdStatus(userSession.getOpenid(), "9").getUnion_id();
			if(unionId!=null) {
				project.setUser_id(unionId);
				//新增前：查询用户当前是否还能新增项目
				Map<String, Integer> manageRet = projectManageService.getUserProjectManage(project.getUser_id());
				if (manageRet.get("curCount") < manageRet.get("maxCount")) {
					//新增前：查询invite_code是否被占用
					Project hasProj = projectDao.queryProjectByInviteCode(project.getInvite_code());
					if (hasProj == null) {
						long projectId = projectDao.insert(project);
						if (projectId != -1) {
							result = miniService.newSuccessResponseMap();
							result.put("id", projectId);
						} else
							result = miniService.newErrorResponseMap(2002, "保存项目失败!");
					} else
						result = miniService.newErrorResponseMap(2003, "邀请码已被占用，请修改!");
				} else {
					result = miniService.newErrorResponseMap(2004, "您的免费项目数量已达上限!");
				}
			}
			else
				result = miniService.newErrorResponseMap(9003,"查询用户编号失败!");
		}
		// 没获取到openId，让前段自己从登陆
		else {
			log.info("获取openId失败：logincode=" + loginCode);
			result = miniService.newErrorResponseMap(9001, "code已失效!");

		}
		return result;
	}
	/*更新项目 */
	public Map<String, Object> updateProject(String loginCode,Project project) {
		Map<String, Object> result;
		log.info("updateProject开始：loginCode=" + loginCode + ";project="
				+project );
		UserSession userSession= code2SessionDao.getSessionByLoginCode(loginCode);
		/****测试查询用户信息时跳过查cache*****/
		//UserSession userSession=code2SessionDao.getSessionByLoginCodeTest(loginCode);
		// 主要是判断缓存里还有没有
		if(userSession != null){
			//根据openid在数据库获取union_id,session里一开始并不会保存union_id
			String unionId=userInfoDao.getByOpenIdStatus(userSession.getOpenid(), "9").getUnion_id();
			if(unionId!=null) {
				project.setUser_id(unionId);
				//更新前：查询是否当前用户的项目
				Project curProject = projectDao.queryProjectById(project.getId());
				if (curProject.getUser_id().equals(project.getUser_id())) {
					Boolean ret = projectDao.update(project);
					if (ret)
						result = miniService.newSuccessResponseMap();
					else
						result = miniService.newErrorResponseMap(2005, "更新项目失败!");
				} else
					result = miniService.newErrorResponseMap(2006, "您无权修改本项目!");
			}
			else
				result = miniService.newErrorResponseMap(9003,"查询用户编号失败!");
		}
		// 没获取到openId，让前段自己从登陆
		else {
			log.info("获取openId失败：logincode=" + loginCode);
			result = miniService.newErrorResponseMap(9001, "code已失效!");
		}
		return result;
	}
	/*删除项目 */
	public Map<String, Object> deleteProject(String loginCode,long projectId) {
		Map<String, Object> result;
		log.info("deleteProject开始：loginCode=" + loginCode + ";projectId="
				+projectId );
		UserSession userSession= code2SessionDao.getSessionByLoginCode(loginCode);
		/****测试查询用户信息时跳过查cache*****/
		//UserSession userSession=code2SessionDao.getSessionByLoginCodeTest(loginCode);
		// 主要是判断缓存里还有没有
		if(userSession != null){
			//根据openid在数据库获取union_id,session里一开始并不会保存union_id
			String unionId=userInfoDao.getByOpenIdStatus(userSession.getOpenid(), "9").getUnion_id();
			if(unionId!=null) {
				//删除前：查询是否当前用户的项目
				Project curProject = projectDao.queryProjectById(projectId);
				if (curProject.getUser_id().equals(unionId)) {
					//未发布或已发布未开放评分才可以删除
					if (curProject.getStatus().equals("0") || curProject.getStatus().equals("1")) {
						Boolean ret = projectDao.delete(projectId);
						if (ret)
							result = miniService.newSuccessResponseMap();
						else
							result = miniService.newErrorResponseMap(2007, "删除项目失败!");
					} else
						result = miniService.newErrorResponseMap(2008, "未发布或已发布未开放评分才可以删除!");
				} else
					result = miniService.newErrorResponseMap(2009, "您无权删除本项目!");
			}
			else
				result = miniService.newErrorResponseMap(9003,"查询用户编号失败!");
		}
		// 没获取到openId，让前段自己从登陆
		else {
			log.info("获取openId失败：logincode=" + loginCode);
			result = miniService.newErrorResponseMap(9001, "code已失效!");
		}
		return result;
	}
}
