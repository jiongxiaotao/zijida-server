
package tch.zijidaserver.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tch.zijidaserver.dao.Code2SessionDao;
import tch.zijidaserver.dao.MessageDao;
import tch.zijidaserver.dao.UserInfoDao;
import tch.zijidaserver.entity.Message;
import tch.zijidaserver.entity.UserSession;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class MessageService {
	@Value("${filePath.message}")
	String messageFilePath;
	@Autowired
	private MiniService miniService;
	@Autowired
	private Code2SessionDao code2SessionDao;
	@Autowired
	private UserInfoDao userInfoDao;
	@Autowired
	private MessageDao messageDao;
	private Log log = LogFactory.getLog(MessageService.class);

	//新增留言
	public Map<String, Object> insertMessage(String loginCode, MultipartFile files,int duration) {
		Map<String, Object> result = miniService.newSuccessResponseMap();
		Message message=new Message();
		log.info("addMessage：loginCode=" + loginCode);
		UserSession userSession= code2SessionDao.getSessionByLoginCode(loginCode);
		// 主要是判断缓存里还有没有
		if(userSession != null){
			//根据openid在数据库获取union_id,session里一开始并不会保存union_id
			String unionId=userInfoDao.getByOpenIdStatus(userSession.getOpenid(), "9").getUnion_id();
			if(unionId!=null) {
				//上传文件
                Date date = new Date();
                //按日期建立目录
				String uploadPath = messageFilePath+(new SimpleDateFormat("yyyyMMdd")).format(date);
				// 如果目录不存在就创建
				File uploadDir = new File(uploadPath);
				if (!uploadDir.exists()) {
					uploadDir.mkdir();
				}
				// 获取文件的 名称.扩展名
				String oldName = files.getOriginalFilename();
				String extensionName = "";
				// 获取原来的扩展名
				if ((oldName != null) && (oldName.length() > 0)) {
					int dot = oldName.lastIndexOf('.');
					if ((dot > -1) && (dot < (oldName.length() - 1))) {
						extensionName = oldName.substring(dot);
					}
				}
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				// 构建文件名称
				String fileName = "voicemessage_"+sdf.format(date)+(int)(Math.random()*10)+ extensionName;
				// 获取
				String[] fileType = { ".CD", ".WAVE", ".AIFF", ".AU", ".MPEG", ".MP3",
						".MPEG-4", ".MIDI", ".WMA", ".RealAudio", ".VQF", ".OggVorbis",
						".AMR" };
				List<String> fileTypeList = Arrays.asList(fileType);
				int fileTypeOnCount = 0;
				for (String item : fileTypeList) {
					if (item.equalsIgnoreCase(extensionName)) {
						// -----如果是音频文件的话
						// 构建文件路径
						String path = uploadPath + File.separator + fileName;
						File desFile = new File(path);

						try {
							files.transferTo(desFile);
							log.info("save file success:"+path);
							result.put("filepath",path);
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						fileTypeOnCount++;
					}
				}
				// 不是音频文件
				if (fileTypeOnCount == fileTypeList.size()) {
					result = miniService.newErrorResponseMap(3002, "上传文件格式错误!");
				}

				//保存数据库
				message.setUser_id(unionId);
				message.setName("");
				message.setDuration(duration);
				long messageId = messageDao.insert(message);
				if (messageId != -1) {
					result.put("id", messageId);
				} else
					result = miniService.newErrorResponseMap(3002, "保存留言失败!");

			}
			else
				result = miniService.newErrorResponseMap(9003,"查询用户编号失败!");
		}
		// 没获取到openId，让前段自己从登陆
		else {
			log.info("获取openId失败：logincode=" + loginCode);
			result = miniService.newErrorResponseMap(9001, "code已失效!");

		}
		return result;
	}

}
