
package tch.zijidaserver.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tch.zijidaserver.dao.ChoiseDao;
import tch.zijidaserver.dao.Code2SessionDao;
import tch.zijidaserver.dao.ChoiseDao;
import tch.zijidaserver.dao.UserInfoDao;
import tch.zijidaserver.entity.Choise;
import tch.zijidaserver.entity.Choise;
import tch.zijidaserver.entity.UserSession;

import java.util.List;
import java.util.Map;

@Service
public class ChoiseService {

	@Autowired
	private MiniService miniService;
	@Autowired
	private Code2SessionDao code2SessionDao;
	@Autowired
	private UserInfoDao userInfoDao;
	@Autowired
	private ChoiseDao choiseDao;
	private Log log = LogFactory.getLog(ChoiseService.class);
	//查询选项列表
	public Map<String, Object> queryChoiseList(String loginCode,long questionId) {
		log.info("getChoise开始：loginCode=" + loginCode);
		Map<String, Object> result;
		List<Choise> choiseList;
		UserSession userSession=code2SessionDao.getSessionByLoginCode(loginCode);
		// 没获取到缓存，说明code失效了，让前段自己从登陆
		if(userSession != null){
			// 查询选项详情
			log.info("查询选项列表：questionId=" + questionId);
			//查所有
			choiseList=choiseDao.queryQuestionChoiseList(questionId);
			if (choiseList != null) {
				result = miniService.newSuccessResponseMap();
				result.put("count", choiseList.size());
				result.put("choiseList", choiseList);
			}
			else
				result = miniService.newErrorResponseMap(3101,"查询选项列表失败!");
		}
		else {
			log.info("code已失效!logincode=" + loginCode);
			result = miniService.newErrorResponseMap(9001, "code已失效!");
		}
		return result;
	}

	//新增选项
	public Map<String, Object> insertChoise(String loginCode,Choise choise) {
		Map<String, Object> result;
		log.info("addChoise：loginCode=" + loginCode);
		UserSession userSession= code2SessionDao.getSessionByLoginCode(loginCode);
		// 主要是判断缓存里还有没有
		if(userSession != null){
			long choiseId=choiseDao.insert(choise);
			if(choiseId!=-1) {
				result = miniService.newSuccessResponseMap();
				result.put("id", choiseId);
			}
			else
				result = miniService.newErrorResponseMap(3102, "保存选项失败!");
		}
		// 没获取到openId，让前段自己从登陆
		else {
			log.info("获取openId失败：logincode=" + loginCode);
			result = miniService.newErrorResponseMap(9001, "code已失效!");
		}
		return result;
	}
	/*更新选项 */
	public Map<String, Object> updateChoise(String loginCode,Choise choise) {
		Map<String, Object> result;
		log.info("updateChoise开始：loginCode=" + loginCode + ";choise="
				+choise );
		UserSession userSession= code2SessionDao.getSessionByLoginCode(loginCode);
		// 主要是判断缓存里还有没有
		if(userSession != null){
			Boolean ret=choiseDao.update(choise);
			if(ret) {
				result = miniService.newSuccessResponseMap();
			}
			else
				result = miniService.newErrorResponseMap(3103, "更新选项失败!");

		}
		// 没获取到openId，让前段自己从登陆
		else {
			log.info("获取openId失败：logincode=" + loginCode);
			result = miniService.newErrorResponseMap(9001, "code已失效!");
		}
		return result;
	}
	/*删除选项 */
	public Map<String, Object> deleteChoise(String loginCode,long choiseId) {
		Map<String, Object> result;
		log.info("deleteChoise开始：loginCode=" + loginCode + ";choiseId="
				+choiseId );
		UserSession userSession= code2SessionDao.getSessionByLoginCode(loginCode);
		// 主要是判断缓存里还有没有
		if(userSession != null){
			Boolean ret=choiseDao.delete(choiseId);
			if(ret)
				result = miniService.newSuccessResponseMap();
			else
				result = miniService.newErrorResponseMap(2006, "删除选项失败!");

		}
		// 没获取到openId，让前段自己从登陆
		else {
			log.info("获取openId失败：logincode=" + loginCode);
			result = miniService.newErrorResponseMap(9001, "code已失效!");
		}
		return result;
	}

	//批量新增选项
	public Boolean insertChoiseList(List<Choise> choiseList) {
		Map<String, Object> result;
		log.info("insertChoiseList");
		return true;
	}
}
