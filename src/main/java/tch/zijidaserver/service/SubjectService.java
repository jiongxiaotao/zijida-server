
package tch.zijidaserver.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tch.zijidaserver.dao.Code2SessionDao;
import tch.zijidaserver.dao.SubjectDao;
import tch.zijidaserver.dao.UserInfoDao;
import tch.zijidaserver.entity.Subject;
import tch.zijidaserver.entity.UserSession;

import java.util.List;
import java.util.Map;

@Service
public class SubjectService {

	@Autowired
	private MiniService miniService;
	@Autowired
	private Code2SessionDao code2SessionDao;
	@Autowired
	private UserInfoDao userInfoDao;
	@Autowired
	private SubjectDao subjectDao;
	private Log log = LogFactory.getLog(SubjectService.class);
	//查询评分项列表
	public Map<String, Object> querySubjectList(String loginCode,long projectId) {
		log.info("getSubject开始：loginCode=" + loginCode);
		Map<String, Object> result;
		List<Subject> subjectList;
		UserSession userSession=code2SessionDao.getSessionByLoginCode(loginCode);
		/****测试查询用户信息时跳过查cache*****/
		//UserSession userSession=code2SessionDao.getSessionByLoginCodeTest(loginCode);
		// 没获取到缓存，说明code失效了，让前段自己从登陆
		if(userSession != null){
			// 查询评分项详情
			log.info("查询评分项列表：projectId=" + projectId);
			//查所有
			subjectList=subjectDao.queryProjectSubjectList(projectId);
			if (subjectList != null) {
				result = miniService.newSuccessResponseMap();
				result.put("count", subjectList.size());
				result.put("subjectList", subjectList);
				int totalScore=0;
				for(Subject item:subjectList){
					totalScore+=item.getMax_score();
				}
				result.put("totalScore", totalScore);
			}
			else
				result = miniService.newErrorResponseMap(2101,"查询评分项列表失败!");
		}
		else {
			log.info("code已失效!logincode=" + loginCode);
			result = miniService.newErrorResponseMap(9001, "code已失效!");
		}
		return result;
	}

	//新增评分项
	public Map<String, Object> insertSubject(String loginCode,Subject subject) {
		Map<String, Object> result;
		log.info("addSubject：loginCode=" + loginCode);
		UserSession userSession= code2SessionDao.getSessionByLoginCode(loginCode);
		/****测试查询用户信息时跳过查cache*****/
		//UserSession userSession=code2SessionDao.getSessionByLoginCodeTest(loginCode);
		// 主要是判断缓存里还有没有
		if(userSession != null){
			long subjectId=subjectDao.insert(subject);
			if(subjectId!=-1) {
				result = miniService.newSuccessResponseMap();
				result.put("id", subjectId);
			}
			else
				result = miniService.newErrorResponseMap(2102, "保存评分项失败!");

		}
		// 没获取到openId，让前段自己从登陆
		else {
			log.info("获取openId失败：logincode=" + loginCode);
			result = miniService.newErrorResponseMap(9001, "code已失效!");

		}
		return result;
	}
	/*更新评分项 */
	public Map<String, Object> updateSubject(String loginCode,Subject subject) {
		Map<String, Object> result;
		log.info("updateSubject开始：loginCode=" + loginCode + ";subject="
				+subject );
		UserSession userSession= code2SessionDao.getSessionByLoginCode(loginCode);
		/****测试查询用户信息时跳过查cache*****/
		//UserSession userSession=code2SessionDao.getSessionByLoginCodeTest(loginCode);
		// 主要是判断缓存里还有没有
		if(userSession != null){
			Boolean ret=subjectDao.update(subject);
			if(ret)
				result = miniService.newSuccessResponseMap();
			else
				result = miniService.newErrorResponseMap(2004, "更新评分项失败!");

		}
		// 没获取到openId，让前段自己从登陆
		else {
			log.info("获取openId失败：logincode=" + loginCode);
			result = miniService.newErrorResponseMap(9001, "code已失效!");
		}
		return result;
	}
	/*删除评分项 */
	public Map<String, Object> deleteSubject(String loginCode,long subjectId) {
		Map<String, Object> result;
		log.info("deleteSubject开始：loginCode=" + loginCode + ";subjectId="
				+subjectId );
		UserSession userSession= code2SessionDao.getSessionByLoginCode(loginCode);
		/****测试查询用户信息时跳过查cache*****/
		//UserSession userSession=code2SessionDao.getSessionByLoginCodeTest(loginCode);
		// 主要是判断缓存里还有没有
		if(userSession != null){
			Boolean ret=subjectDao.delete(subjectId);
			if(ret)
				result = miniService.newSuccessResponseMap();
			else
				result = miniService.newErrorResponseMap(2006, "删除评分项失败!");

		}
		// 没获取到openId，让前段自己从登陆
		else {
			log.info("获取openId失败：logincode=" + loginCode);
			result = miniService.newErrorResponseMap(9001, "code已失效!");
		}
		return result;
	}
}
