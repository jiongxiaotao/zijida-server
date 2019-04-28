package tch.zijidaserver.Controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tch.zijidaserver.service.UserInfoService;

import java.util.Map;

@RestController
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;
    private Log log = LogFactory.getLog(UserInfoController.class);
    @RequestMapping(value = "/miniQueryUserInfo", method = RequestMethod.GET)
    public Map<String, Object> miniQueryUserInfo(@RequestParam("loginCode") String loginCode)
    {
        log.info("[REQUEST]/miniQueryUserInfo?loginCode="+loginCode);
        return userInfoService.getUserInfo(loginCode);
    }
    @RequestMapping(value="/miniSaveUserInfo",method = {RequestMethod.GET,RequestMethod.POST})
    public Map<String, Object> miniSaveUserInfo(@RequestParam("loginCode") String loginCode,
                                              @RequestBody JSONObject jsonObject)
    {
        log.info("[REQUEST]/miniSaveUserInfo?loginCode="+loginCode+" body="+jsonObject);
        String encryptedData=jsonObject.get("encryptedData").toString();    // 微信用户信息加密报文
        String iv=jsonObject.get("iv").toString(); //微信用户信息加密偏移量
        return userInfoService.updateUserInfo(loginCode,encryptedData,iv);
    }
}
