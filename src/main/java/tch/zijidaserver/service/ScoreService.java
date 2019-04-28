
package tch.zijidaserver.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tch.zijidaserver.dao.*;
import tch.zijidaserver.entity.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScoreService {

	@Autowired
	private MiniService miniService;
	@Autowired
	private Code2SessionDao code2SessionDao;
	@Autowired
	private SubjectDao subjectDao;
	@Autowired
	private VoteeDao voteeDao;
	@Autowired
	private ScoreDao scoreDao;
	@Autowired
	private ProjectDao projectDao;

	private Log log = LogFactory.getLog(ScoreService.class);
	//查询评分项列表
	public Map<String, Object> getResult(String loginCode,long projectId) {
		log.info("getScore开始：loginCode=" + loginCode);
		Map<String, Object> result;
		List<Subject> subjectList;
		List<Map<String, Object>> voteeOderList;
		List<Map<String, Object>> voteeResultList=new ArrayList<>();	//每个被评人的评分结果情况
		UserSession userSession=code2SessionDao.getSessionByLoginCode(loginCode);
		/****测试查询用户信息时跳过查cache*****/
		//UserSession userSession=code2SessionDao.getSessionByLoginCodeTest(loginCode);
		// 没获取到缓存，说明code失效了，让前段自己从登陆
		if(userSession != null){
			//查项目信息
			try {
				Project project=projectDao.queryProjectById(projectId);
				//查询本项目的评分项列表，主要拿到满分总分
				subjectList=subjectDao.queryProjectSubjectList(projectId);
				int totalScore=0;
				for(Subject item:subjectList){
					totalScore+=item.getMax_score();
				}
				//查询本项目的被评人排名
				voteeOderList=scoreDao.queryVoteeScoreOrder(projectId);
				if(voteeOderList!=null){
					//遍历被评人，查询每个被评人的各项平均分
					int no=1;	//排名
					Boolean voteeResultFlag=true;
					for(Map<String,Object> voteeItem:voteeOderList){
						Map<String,Object> voteeResult=new HashMap<>();
						voteeResult.put("no", no);	//被评人排名
						no+=1;	//排名自增
						voteeResult.put("voteeId", voteeItem.get("votee_id"));	//被评人编号
						voteeResult.put("name", voteeItem.get("name"));	//被评人姓名
						List<Map<String,Object>> subjectResultList=scoreDao.querySubjectScoreListByVoteeId((long)voteeItem.get("votee_id"));
						if(subjectResultList!=null){
							double voteeScore=0.0;
							for(Map<String,Object> scoreItem:subjectResultList){
								voteeScore+=((BigDecimal)scoreItem.get("score")).doubleValue();
							}
							voteeResult.put("score", voteeScore);	//被评人总分
							voteeResult.put("subjects", subjectResultList);

							//加入被评人结果列表
							voteeResultList.add(voteeResult);
						}
						else{
							voteeResultFlag=false;
							break;
						}
					}
					if(!voteeResultFlag){
						result = miniService.newErrorResponseMap(2301,"查询被评人得分详情失败!");
					}
					else{
						result = miniService.newSuccessResponseMap();
						result.put("projectId", projectId);//已收集评分的个数
						result.put("doneAmount", project.getDone_amount());//已收集评分的个数
						result.put("amount", project.getAmount());//计划评分个数
						result.put("totalScore", totalScore); //满分分值
						result.put("voteeCount", voteeOderList.size()); //被评人个数
						result.put("results", voteeResultList);	//结果列表
					}
				}
				else{
					result = miniService.newErrorResponseMap(2302,"查询被评人排名失败!");
				}
			}
			catch (Exception e){
				log.error(e);
				result = miniService.newErrorResponseMap(2303,"查询结果失败!");
			}
		}
		else {
			log.info("code已失效!logincode=" + loginCode);
			result = miniService.newErrorResponseMap(9001, "code已失效!");
		}
		return result;
	}

	//提交评分
	public Map<String, Object> submitProjectScore(String loginCode,long projectId,List<Score> scoreList) {
		Map<String, Object> result;
		Project project;
		UserSession userSession= code2SessionDao.getSessionByLoginCode(loginCode);
		/****测试查询用户信息时跳过查cache*****/
		//UserSession userSession=code2SessionDao.getSessionByLoginCodeTest(loginCode);
		// 主要是判断缓存里还有没有
		if(userSession != null){
			//检查当前用户是否已评过分
			int hasPR=scoreDao.queryUserProjectDone(projectId, userSession.getUnionid());
			//查询sql报错
			if(hasPR==-1){
				result = miniService.newErrorResponseMap(2304, "查询用户是否对项目打分失败!");
			}
			else if(hasPR>0)
				result = miniService.newErrorResponseMap(2305, "该用户已完成对本项目打分!");
			else{
				//判断项目是否已结束
				project=projectDao.queryProjectById(projectId);
				if(project.getStatus().equals("9")){
					result = miniService.newErrorResponseMap(2306, "本项目已终止评分!");
				}
				//项目还未开放评分
				else if(!project.getStatus().equals("2"))
					result = miniService.newErrorResponseMap(2309, "本项目还未开放评分!");
				else{
					//遍历每个打分
					boolean flag=true;
					for(Score item:scoreList){
						item.setUser_id(userSession.getUnionid());
						//入库
						flag=scoreDao.insert(item);
					}
					//依次插入，有错误时将插入的内容全部清空
					if(!flag){
						scoreDao.deleteUserProjectScore(projectId,userSession.getUnionid());
						result = miniService.newErrorResponseMap(2307, "对评分项打分时发生错误!");
					}
					//全部插入成功
					else {
						//project表的已完成评分数+1
						projectDao.increaseDoneAmount(projectId);
						//判断新插入后，项目收集个数是否到达期望个数
						project=projectDao.queryProjectById(projectId);
						if(project.getDone_amount()==project.getAmount()){
							project.setStatus("9");
							Boolean ret=projectDao.update(project);
							if(ret){
								result = miniService.newSuccessResponseMap();
							}
							else{
								result = miniService.newErrorResponseMap(2308, "更新项目状态失败!");
							}
						}
						else
							result = miniService.newSuccessResponseMap();
					}
				}
			}
		}
		// 没获取到openId，让前段自己从登陆
		else {
			log.info("获取openId失败：logincode=" + loginCode);
			result = miniService.newErrorResponseMap(9001, "code已失效!");
		}
		return result;
	}


}
