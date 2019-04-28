/*
 * *
 *  * Copyright (c) 2016, China Construction Bank Co., Ltd. All rights reserved.
 *  * 中国建设银行版权所有.
 *  * <p/>
 *  * 审核人：
 *
 */

package tch.zijidaserver.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tch.zijidaserver.dao.Code2SessionDao;
import tch.zijidaserver.entity.UserSession;


@Component("miniCode2SessionService")
public class Code2SessionService {
	@Autowired
	private MiniService miniService;
	@Autowired
    private Code2SessionDao dao;
    private Log log=LogFactory.getLog(Code2SessionDao.class);
    public Map<String, Object> code2Session(String appId,String secret, String jsCode) {
    	Map<String,Object> result;
    	UserSession userSession=dao.code2Session(appId, secret, jsCode);
    	if(userSession.getErrcode()==0){
    		log.info("微信获取session成功："+userSession);
    		result = miniService.newSuccessResponseMap();
    		Map<String,Object> apiResult=new HashMap();
			apiResult.put("loginCode", userSession.getLogin_code());
			apiResult.put("loginStatus", userSession.getLogin_status());
    		//不反回userSession的open_id等信息
			apiResult.put("code", userSession.getCode());
    		apiResult.put("open_id", userSession.getOpenid());
    		apiResult.put("union_id", userSession.getUnionid());
    		
    		result.put("api_result", apiResult);
    	}
    	else{
    		log.info("微信获取session失败：errmsg="+userSession.getErrmsg());
    		result = miniService.newErrorResponseMap(userSession.getErrcode(),userSession.getErrmsg());
    		result.put("api_result", userSession);
    	}
        return result;
    }

}
