
package tch.zijidaserver.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tch.zijidaserver.dao.ChoiseDao;
import tch.zijidaserver.dao.Code2SessionDao;
import tch.zijidaserver.dao.QuestionDao;
import tch.zijidaserver.dao.UserInfoDao;
import tch.zijidaserver.entity.Choise;
import tch.zijidaserver.entity.Question;
import tch.zijidaserver.entity.UserSession;

import java.awt.*;
import java.util.List;
import java.util.Map;

@Service
public class QuestionService {

	@Autowired
	private MiniService miniService;
	@Autowired
	private Code2SessionDao code2SessionDao;
	@Autowired
	private UserInfoDao userInfoDao;
	@Autowired
	private QuestionDao questionDao;
	@Autowired
	private ChoiseDao choiseDao;
	private Log log = LogFactory.getLog(QuestionService.class);
	//查询题目列表
	public Map<String, Object> queryQuestionList(String loginCode,long projectId) {
		log.info("getQuestion开始：loginCode=" + loginCode);
		Map<String, Object> result;
		List<Question> questionList;
		//UserSession userSession=code2SessionDao.getSessionByLoginCode(loginCode);
		/****测试查询用户信息时跳过查cache*****/
		UserSession userSession=code2SessionDao.getSessionByLoginCodeTest(loginCode);
		// 没获取到缓存，说明code失效了，让前段自己从登陆
		if(userSession != null){
			// 查询题目详情
			log.info("查询题目列表：projectId=" + projectId);
			//查所有
			questionList=questionDao.queryProjectQuestionList(projectId);
			if (questionList != null) {
				for(Question item:questionList){
					//如果是选择题，则查询所有选项
					if(item.getType().equals("radio")||item.getType().equals("checkbox"))
						item.setChoiseList(choiseDao.queryQuestionChoiseList(item.getId()));
				}
				result = miniService.newSuccessResponseMap();
				result.put("count", questionList.size());
				result.put("questionList", questionList);
			}
			else
				result = miniService.newErrorResponseMap(3001,"查询题目列表失败!");
		}
		else {
			log.info("code已失效!logincode=" + loginCode);
			result = miniService.newErrorResponseMap(9001, "code已失效!");
		}
		return result;
	}

	//新增题目，如果是选择题，还要遍历新增选项
	public Map<String, Object> insertQuestion(String loginCode,Question question) {
		Map<String, Object> result;
		log.info("addQuestion：loginCode=" + loginCode);
		//UserSession userSession= code2SessionDao.getSessionByLoginCode(loginCode);
		/****测试查询用户信息时跳过查cache*****/
		UserSession userSession=code2SessionDao.getSessionByLoginCodeTest(loginCode);
		// 主要是判断缓存里还有没有
		if(userSession != null){
			long questionId=questionDao.insert(question);
			if(questionId!=-1) {
				//选择题要加选项
				if(question.getType().equals("radio")||question.getType().equals("checkbox")){
					List<Choise> choiseList=question.getChoiseList();
					long flag=1;
					for(Choise item:choiseList){
						item.setQuestion_id(questionId);
						flag=choiseDao.insert(item);
						if(flag==-1)
							break;
					}
					//全部插入成功
					if(flag!=-1){
						result = miniService.newSuccessResponseMap();
						result.put("id", questionId);
					}
					else{
						result = miniService.newErrorResponseMap(3002, "保存题目选项失败!");
					}
				}
				//非选择题，返回成功
				else{
					result = miniService.newSuccessResponseMap();
					result.put("id", questionId);
				}

			}
			else
				result = miniService.newErrorResponseMap(3003, "保存题目失败!");

		}
		// 没获取到openId，让前段自己从登陆
		else {
			log.info("获取openId失败：logincode=" + loginCode);
			result = miniService.newErrorResponseMap(9001, "code已失效!");

		}
		return result;
	}
	/*更新题目 */
	public Map<String, Object> updateQuestion(String loginCode,Question question) {
		Map<String, Object> result;
		log.info("updateQuestion开始：loginCode=" + loginCode + ";question="
				+question );
		//UserSession userSession= code2SessionDao.getSessionByLoginCode(loginCode);
		/****测试查询用户信息时跳过查cache*****/
		UserSession userSession=code2SessionDao.getSessionByLoginCodeTest(loginCode);
		// 主要是判断缓存里还有没有
		if(userSession != null){
			Boolean ret=questionDao.update(question);
			if(ret) {
				//选择题选项，新增每个没有id的新选项；遍历原有选项，对比当前所有选项，若原有选项有则修改，没有则删除
				if(question.getType().equals("radio")||question.getType().equals("checkbox")) {
					//原有选项列表
					List<Choise> oldChoiseList=choiseDao.queryQuestionChoiseList(question.getId());

					Boolean updateRight=true;//更新是否报错
					for(Choise oldItem:oldChoiseList){
						Boolean deleteflag=true;//是否需要删除
						for (Choise item : question.getChoiseList()) {
							//修改该项目
							if(item.getId()==oldItem.getId()) {
								deleteflag=false;//原来有，修改了，不删除
								updateRight = choiseDao.update(item);
								break;
							}
						}
						//遍历当前没有原选项，删掉原选项
						if (deleteflag) {
							updateRight=choiseDao.delete(oldItem.getId());
						}
						//更新报错了，跳出循环，交易返回
						if(!updateRight)
							break;
					}

					//没有id的就是新增
					long insertRight = 1;
					for (Choise item : question.getChoiseList()) {
						if(item.getId()==0) { //之前没有id，直接新增
							insertRight = choiseDao.insert(item);
							if (insertRight == -1) //插入报错
								break;
						}
					}
					//插入，更新，删除都成功
					if (updateRight && insertRight!=-1) {
						result = miniService.newSuccessResponseMap();
					} else {
						result = miniService.newErrorResponseMap(3003, "更新题目选项失败!");
					}
				}
				else
					result = miniService.newSuccessResponseMap();
			}
			else
				result = miniService.newErrorResponseMap(3004, "更新题目失败!");

		}
		// 没获取到openId，让前段自己从登陆
		else {
			log.info("获取openId失败：logincode=" + loginCode);
			result = miniService.newErrorResponseMap(9001, "code已失效!");
		}
		return result;
	}
	/*删除题目 */
	public Map<String, Object> deleteQuestion(String loginCode,long questionId) {
		Map<String, Object> result;
		log.info("deleteQuestion开始：loginCode=" + loginCode + ";questionId="
				+questionId );
		//UserSession userSession= code2SessionDao.getSessionByLoginCode(loginCode);
		/****测试查询用户信息时跳过查cache*****/
		UserSession userSession=code2SessionDao.getSessionByLoginCodeTest(loginCode);
		// 主要是判断缓存里还有没有
		if(userSession != null){
			Boolean ret=questionDao.delete(questionId);
			if(ret)
				result = miniService.newSuccessResponseMap();
			else
				result = miniService.newErrorResponseMap(2006, "删除题目失败!");

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
