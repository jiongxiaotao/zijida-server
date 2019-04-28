/*
 * *
 *  * Copyright (c) 2016, China Construction Bank Co., Ltd. All rights reserved.
 *  * 中国建设银行版权所有.
 *  * <p/>
 *  * 审核人：
 *
 */

package tch.zijidaserver.entity;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UserSession extends MiniBean implements Serializable {

    private String code;
    private String openid;
    private String session_key;
    private String unionid;
    private String login_code;
    private int login_status;
    public UserSession() {

    }
    //code+openID+sessionKey，生成我自己的loginCode
    public UserSession(String code,String openid,String sessionKey,String loginCode) {
    	this.code=code;
    	this.openid=openid;
    	this.session_key=sessionKey;
    	//对用户的openID和sessionkey做映射
    	Log log=LogFactory.getLog(UserSession.class);
    	log.info("usersession=="+this.code+this.openid+this.session_key);
        
    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}



	public String getSession_key() {
		return session_key;
	}

	public void setSession_key(String session_key) {
		this.session_key = session_key;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public String getLogin_code() {
		return login_code;
	}

	public void setLogin_code(String login_code) {
		this.login_code = login_code;
	}

	public int getLogin_status() {
		return login_status;
	}

	public void setLogin_status(int login_status) {
		this.login_status = login_status;
	}

	@Override
	public String toString() {
		return "UserSession{" +
				"code='" + code + '\'' +
				", openid='" + openid + '\'' +
				", session_key='" + session_key + '\'' +
				", unionid='" + unionid + '\'' +
				", login_code='" + login_code + '\'' +
				", login_status=" + login_status +
				", errcode=" + errcode +
				", errmsg='" + errmsg + '\'' +
				'}';
	}
}
