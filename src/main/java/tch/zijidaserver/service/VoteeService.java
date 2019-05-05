
package tch.zijidaserver.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tch.zijidaserver.dao.Code2SessionDao;
import tch.zijidaserver.dao.VoteeDao;
import tch.zijidaserver.entity.Votee;
import tch.zijidaserver.entity.UserSession;

import java.util.List;
import java.util.Map;

@Service
public class VoteeService {

	@Autowired
	private MiniService miniService;
	@Autowired
	private Code2SessionDao code2SessionDao;
	@Autowired
	private VoteeDao voteeDao;
	private Log log = LogFactory.getLog(VoteeService.class);
	//查询被评人列表
	public Map<String, Object> queryVoteeList(String loginCode,long projectId) {
		log.info("getVotee开始：loginCode=" + loginCode);
		Map<String, Object> result;
		List<Votee> voteeList;
		UserSession userSession=code2SessionDao.getSessionByLoginCode(loginCode);
		/****测试查询用户信息时跳过查cache*****/
		//UserSession userSession=code2SessionDao.getSessionByLoginCodeTest(loginCode);
		// 没获取到缓存，说明code失效了，让前段自己从登陆
		if(userSession != null){
			// 查询被评人详情
			log.info("查询被评人列表：projectId=" + projectId);
			//查所有
			voteeList=voteeDao.queryProjectVoteeList(projectId);
			if (voteeList != null) {
				result = miniService.newSuccessResponseMap();
				result.put("count", voteeList.size());
				result.put("voteeList", voteeList);
			}
			else
				result = miniService.newErrorResponseMap(2201,"查询被评人列表失败!");
		}
		else {
			log.info("code已失效!logincode=" + loginCode);
			result = miniService.newErrorResponseMap(9001, "code已失效!");
		}
		return result;
	}

	//新增被评人
	public Map<String, Object> insertVotee(String loginCode,Votee votee) {
		Map<String, Object> result;
		log.info("addVotee：loginCode=" + loginCode);
		UserSession userSession= code2SessionDao.getSessionByLoginCode(loginCode);
		/****测试查询用户信息时跳过查cache*****/
		//UserSession userSession=code2SessionDao.getSessionByLoginCodeTest(loginCode);
		// 主要是判断缓存里还有没有
		if(userSession != null){
			Boolean ret=voteeDao.insert(votee);
			if(ret)
				result = miniService.newSuccessResponseMap();
			else
				result = miniService.newErrorResponseMap(2202, "保存被评人失败!");

		}
		// 没获取到openId，让前段自己从登陆
		else {
			log.info("获取openId失败：logincode=" + loginCode);
			result = miniService.newErrorResponseMap(9001, "code已失效!");

		}
		return result;
	}
	/*更新被评人 */
	public Map<String, Object> updateVotee(String loginCode,Votee votee) {
		Map<String, Object> result;
		log.info("updateVotee开始：loginCode=" + loginCode + ";votee="
				+votee );
		UserSession userSession= code2SessionDao.getSessionByLoginCode(loginCode);
		/****测试查询用户信息时跳过查cache*****/
		//UserSession userSession=code2SessionDao.getSessionByLoginCodeTest(loginCode);
		// 主要是判断缓存里还有没有
		if(userSession != null){
			//更新前：查询是否当前用户的被评人
			Boolean ret=voteeDao.update(votee);
			if(ret)
				result = miniService.newSuccessResponseMap();
			else
				result = miniService.newErrorResponseMap(2204, "更新被评人失败!");

		}
		// 没获取到openId，让前段自己从登陆
		else {
			log.info("获取openId失败：logincode=" + loginCode);
			result = miniService.newErrorResponseMap(9001, "code已失效!");
		}
		return result;
	}
	/*删除被评人 */
	public Map<String, Object> deleteVotee(String loginCode,long voteeId) {
		Map<String, Object> result;
		log.info("deleteVotee开始：loginCode=" + loginCode + ";voteeId="
				+voteeId );
		UserSession userSession= code2SessionDao.getSessionByLoginCode(loginCode);
		/****测试查询用户信息时跳过查cache*****/
		//UserSession userSession=code2SessionDao.getSessionByLoginCodeTest(loginCode);
		// 主要是判断缓存里还有没有
		if(userSession != null){
			Boolean ret=voteeDao.delete(voteeId);
			if(ret)
				result = miniService.newSuccessResponseMap();
			else
				result = miniService.newErrorResponseMap(2206, "删除被评人失败!");

		}
		// 没获取到openId，让前段自己从登陆
		else {
			log.info("获取openId失败：logincode=" + loginCode);
			result = miniService.newErrorResponseMap(9001, "code已失效!");
		}
		return result;
	}
}
