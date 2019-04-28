package tch.zijidaserver.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import com.baidu.aip.face.AipFace;
import org.springframework.stereotype.Service;
import tch.zijidaserver.entity.BaiduFactory;

@Service
public class BaiduFRService {

	@Autowired
	private MiniService miniService;
	private AipFace client= BaiduFactory.getAipFace();
	//
	public Map<String,Object> videoSessioncode() {
		Map<String,Object> result;
	    // 传入可选参数调用接口
	    HashMap<String, String> options = new HashMap<String, String>();
	    options.put("appid", BaiduFactory.getAppId());
	    
	    // 语音校验码接口
		Map<String,Object> apiResult= client.videoSessioncode(options).toMap();
		if(((String)apiResult.get("error_code")).equals("0")){
			result = miniService.newSuccessResponseMap();
			result.put("api_result", apiResult);
		}
		else{
			result = miniService.newErrorResponseMap(9101,"获取百度语音校验码失败！"+
					(String)apiResult.get("error_msg"));
		}
		return result;
	}
	//视频活体检测。client-api对象；sessionId-上一步语音校验的会话id；video：视频文件名
	public Map<String,Object> videoFaceliveness(String sessionId,String video) {
		Map<String,Object> result;
	    // 传入可选参数调用接口
	    HashMap<String, String> options = new HashMap<String, String>();
	    Map<String,Object> apiResult= client.videoFaceliveness(sessionId, video, options).toMap();
	    if((Integer)apiResult.get("error_code")==0){
	    	Map<String,Object> faceRes=(Map<String,Object>)apiResult.get("result");
	    	Double score=(Double)faceRes.get("score");	//检测分数
	    	//目前使用误拒率1e-2的
	    	Double threshold=((Map<String,Double>)faceRes.get("thresholds")).get("frr_1e-2");
	    	//活体图片列表
	    	List<Map<String,Object>> picList=(List<Map<String,Object>>)faceRes.get("pic_list");
	    	//取最后一张
	    	String pic=(String)picList.get(picList.size()-1).get("pic");
	    	result = miniService.newSuccessResponseMap();
	    	result.put("score", score);
	    	result.put("threshold", threshold);
	    	result.put("pic", pic);
	    	result.put("result", (score>threshold)?1:0);
	    }
	    else{
	    	result = miniService.newErrorResponseMap(9101,"百度视频活体检测失败！"+(Integer)apiResult.get("error_code")+
	    			(String)apiResult.get("error_msg"));
	    }
		return result;
	}
}
