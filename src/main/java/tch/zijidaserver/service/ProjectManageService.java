
package tch.zijidaserver.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tch.zijidaserver.dao.Code2SessionDao;
import tch.zijidaserver.dao.ProjectDao;
import tch.zijidaserver.dao.UserInfoDao;
import tch.zijidaserver.entity.Project;
import tch.zijidaserver.entity.UserSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProjectManageService {

	@Autowired
	private MiniService miniService;
	@Autowired
	private Code2SessionDao code2SessionDao;
	@Autowired
	private UserInfoDao userInfoDao;
	@Autowired
	private ProjectDao projectDao;
	private Log log = LogFactory.getLog(ProjectManageService.class);
	//查询用户最大项目数和当前项目数
	public Map<String, Object> getProjectManage(String loginCode) {
		log.info("getProjectManage开始：loginCode=" + loginCode);
		Map<String, Object> result;
		UserSession userSession=code2SessionDao.getSessionByLoginCode(loginCode);
		/****测试查询用户信息时跳过查cache*****/
		//UserSession userSession=code2SessionDao.getSessionByLoginCodeTest(loginCode);
		// 没获取到缓存，说明code失效了，让前段自己从登陆
		if(userSession != null){
			String unionId=userSession.getUnionid();
			// 查库，项目管理情况
			Map<String, Integer> ret=getUserProjectManage(unionId);
			result = miniService.newSuccessResponseMap();
			result.put("curCount", ret.get("curCount"));
			result.put("maxCount", ret.get("maxCount"));
			if (ret.get("curCount")<ret.get("maxCount")) {
				result.put("canAdd", true);
			}
			else
				result.put("canAdd", false);
		}
		else {
			log.info("code已失效!logincode=" + loginCode);
			result = miniService.newErrorResponseMap(9001, "code已失效!");
		}
		return result;
	}
	//从库里查询用户最大项目数和当前项目书
	public Map<String, Integer> getUserProjectManage(String unionId) {
		Map<String, Integer> ret=new HashMap<String, Integer>();
		int maxCount=userInfoDao.getUserMaxProjectCount(unionId);
		int curCount=0;//当前是用的项目个数，默认0
		List<Project> list=projectDao.queryUserProjectList(unionId);
		if(list!=null)
			curCount=(list.size());
		log.info("查询用户最大项目数和当前项目数：unionId="+unionId+",maxCount="+maxCount+",curCount="+curCount);
		ret.put("maxCount", maxCount);
		ret.put("curCount",curCount);
		return ret;
	}
}
