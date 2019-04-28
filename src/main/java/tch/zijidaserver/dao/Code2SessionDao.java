package tch.zijidaserver.dao;

import com.alibaba.fastjson.JSON;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import tch.zijidaserver.cache.RedisUtils;
import tch.zijidaserver.entity.UserSession;
import tch.zijidaserver.utils.HttpClientUtils;
import tch.zijidaserver.utils.Json2BeanResponseHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class Code2SessionDao {
    @Value("${mini.weixinapi.host}")
    private String weixinhost;
    @Value("${mini.weixinapi.code2session}")
    private String weixinUrl;
    private Log log= LogFactory.getLog(Code2SessionDao.class);
    @Autowired
    private JdbcTemplate jdbc;
    @Autowired
    private RedisUtils redisUtils;
    //向微信发请求
    public UserSession code2Session(String appId, String secret, String jsCode) {
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("appid", appId);
            params.put("secret", secret);
            params.put("js_code", jsCode);
            params.put("grant_type", "authorization_code");
            UserSession userSession= HttpClientUtils.get(weixinhost+weixinUrl,params,new Json2BeanResponseHandler<UserSession>(weixinhost+weixinUrl,UserSession.class));
            //微信调用成功
            if(userSession.getErrcode()==0){
                //自定义logincode
                userSession.setCode(jsCode);//返回前段送的code
                userSession.setLogin_code(this.genLoginCode(userSession.getCode(),userSession.getOpenid(),userSession.getSession_key()));
                userSession.setLogin_status(1);//返回已登录状态

                //存入redis
                log.info("新增session信息："+userSession.getLogin_code());
                String redisKey = "zijida_"+userSession.getLogin_code();
                redisUtils.set(redisKey, userSession,7200);
            }
            return userSession;
        }catch (Exception e){
            log.error(e);
            return null;
        }

    }
    //根据前段送的logincode，获取当前用户的缓存数据
    public UserSession getSessionByLoginCode(String loginCode){
        try {
            String redisKey = "zijida_"+loginCode;
            if (redisUtils.checkKeyExists(redisKey)) {
                UserSession userSession = redisUtils.get(redisKey,UserSession.class);
                log.info("从缓存中取出loginCode="+loginCode+"的数据:"+ JSON.toJSONString(userSession));
                return userSession;
            }
            else
                return null;
        }catch (Exception e){
            log.error(e);
            return null;
        }
    }
    //测试使用，返回固定userSesion
    public UserSession getSessionByLoginCodeTest(String loginCode){
        UserSession userSession=new UserSession();
        userSession.setOpenid("ozrIJ4zlTDmRgwmEbbXFwiuQvEuU");  //我的openId
        userSession.setUnionid("olvd61APeNKPDY73LZZBjUqJ5Wgw"); //我的unionId
        return userSession;
    }
    //根据code生成我自己的loginCode(目前只是简单的code+openId+sessionKey)
    public String genLoginCode(String code,String openId,String sessionKey){
        return code+"|"+openId+"|"+sessionKey;
    }
}

