
package tch.zijidaserver.service;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tch.zijidaserver.dao.Code2SessionDao;
import tch.zijidaserver.dao.UserInfoDao;
import tch.zijidaserver.entity.UserInfo;
import tch.zijidaserver.entity.UserSession;

import com.alibaba.fastjson.JSONObject;
import tch.zijidaserver.utils.AESUtil;

@Service
public class UserInfoService {

	@Autowired
	private MiniService miniService;
	@Autowired
	private Code2SessionDao code2SessionDao;
	@Autowired
	private UserInfoDao userInfoDao;
	private Log log = LogFactory.getLog(UserInfoService.class);
	//查询用户信息
	public Map<String, Object> getUserInfo(String loginCode) {
		log.info("getUserInfo开始：loginCode=" + loginCode);
		Map<String, Object> result;
		UserSession userSession=code2SessionDao.getSessionByLoginCode(loginCode);
		// 没获取到缓存，说明code失效了，让前段自己从登陆
		if(userSession != null){
			String openId = userSession.getOpenid();
			String unionId = userSession.getUnionid();
			log.info("cache里获取union_id成功：" + unionId);
			// 登录信息中没有返回unionId，说明用户之前未关注公众号也没用过小程序，则用open_id +bc_status查询
			if (unionId == null || unionId.equals("")) {
				log.info("小程序用户，使用openId查询");
				// 小程序暂时status全是9
				UserInfo userInfo = userInfoDao.getByOpenIdStatus(openId, "9");
				result = miniService.newSuccessResponseMap();
				result.put("userInfo", userInfo);
			}
			// 用户之前关注过公众号，直接查
			else {
				log.info("使用unionId查询：unionId=" + unionId);
				UserInfo userInfo = userInfoDao.getByUnionId(unionId);
				result = miniService.newSuccessResponseMap();
				result.put("userInfo", userInfo);
			}
		}
		else {
			log.info("code已失效!logincode=" + loginCode);
			result = miniService.newErrorResponseMap(9001, "code已失效!");
		}
		return result;
	}

	/*
	 * 新增或更新用户信息，一个用户在公众号和小程序只有一条数据，
	 * 如果是公众号已新增过的用户，就不要小程序的open_id了，直接更新当前数据。bc_status按公众号的；
	 * 如果没有公众号，则bc_status=9（表示仅使用小程序未关注公众号）,保存小程序的open_id
	 * 如果先使用小程序，后关注公众号，则关注时将open_id和bc_status更新为公众号的
	 */
	public Map<String, Object> updateUserInfo(String loginCode,String encryptedData, String iv) {
		UserInfo userInfo = new UserInfo();
		Map<String, Object> result;
		log.info("updateUserInfo开始：loginCode=" + loginCode + ";encryptedData="
				+ encryptedData);
		UserSession userSession= code2SessionDao.getSessionByLoginCode(loginCode);
		// 主要是判断缓存里还有没有
		if(userSession != null){
			String openId =userSession.getOpenid();
			String secretKey = userSession.getSession_key();
			log.info("cache里获取openid成功：openId=" + openId);
			try {
				// 根据secretKey，iv解析encryptedData,获取用户信息
				AESUtil aes = new AESUtil();
				String decryptData = aes.decrypt(encryptedData, secretKey, iv);// 返回用户信息的json字符串
				Map<String, Object> userInfoData = (Map<String, Object>)JSONObject.parse(decryptData);// json转map对象
				String unionId = (String) userInfoData.get("unionId");
				// encryptedData里没拿到unionId，暂不可用
				if (unionId == null || unionId.equals("")) {
					result = miniService.newErrorResponseMap(9004,"获取客户unionId失败!");
				}
				// 拿到了unionId，入库时送进去。更新时不更新open_id和bc_status
				else {
					log.info("encryptedData里获取unionId成功：" + unionId);
					userInfo.setUnion_id( unionId);
					userInfo.setOpen_id( openId);// sql会判断如果是新增则为小程序新用户，送当前值，如果是更新，则有可能是公众号用户，这个字段无效
					userInfo.setStatus("9");// sql会判断如果是新增则为小程序新用户，送9，如果是更新，则有可能是公众号用户，这个字段无效
					userInfo.setCus_name((String)userInfoData.get("nickName"));
					userInfo.setProvince((String) userInfoData.get("province"));
					userInfo.setCity((String) userInfoData.get("city"));
					userInfo.setAvatarUrl((String) userInfoData.get("avatarUrl"));
					Boolean res=false;
					//查询库里有没有，有则更新，没有新增
					if(userInfoDao.getByUnionId(unionId)!=null){
						res=userInfoDao.update(userInfo);
					}
					else
						res=userInfoDao.insert(userInfo);

					if (res) { // 更新成功
						// 根据unionId查询一下并返回
						userInfo = userInfoDao.getByUnionId(unionId);
						if (userInfo != null) { // 查询刚才更新的成功
							result = miniService.newSuccessResponseMap();
							result.put("userInfo", userInfo);
						}
						
						else // 应该不会出现。。
							result = miniService.newErrorResponseMap(9002,
									"根据unionId获取用户信息失败!");
					}
					else {
						result = miniService.newErrorResponseMap(9006,
								"更新客户失败!");
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result = miniService.newErrorResponseMap(9005, "解密用户信息失败!");
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
