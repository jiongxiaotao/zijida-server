/*
 * *
 *  * Copyright (c) 2016, China Construction Bank Co., Ltd. All rights reserved.
 *  * 中国建设银行版权所有.
 *  * <p/>
 *  * 审核人：
 *
 */

package tch.zijidaserver.service;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import tch.zijidaserver.dao.FRSignDao;

/**
 * Created by ainstain on 2017/11/14.
 */
@Service
public class FRSignService {
	@Autowired
	private MiniService miniService;
    @Autowired
    private FRSignDao signDao;
    private Log log=LogFactory.getLog(FRSignDao.class);
    
    private long appId=1258590214;
    private String secretId="AKIDh9QfeNNEtIH0Wn9PfZGjgPtJT5RZvhaE";
    private String secretKey="WH6g6GDwPnw3tToul8PUbT5Zx7MM7pIz";
    private String bucket="tencentyun";
    private long expired=2592000;
    
    public Map<String, Object> sign() {
    	log.info("获取人脸识别签名开始");
    	Map<String,Object> result;
    	String signStr;
		try {
			signStr = signDao.sign(appId,secretId,secretKey,bucket,expired);
			result = miniService.newSuccessResponseMap();
			result.put("signStr", signStr); 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = miniService.newErrorResponseMap(9101,"获取人脸识别签名失败!");
		}
    	return result;
    }
}
