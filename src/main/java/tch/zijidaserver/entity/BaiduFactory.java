package tch.zijidaserver.entity;
import com.baidu.aip.face.AipFace;
public class BaiduFactory {
	private static AipFace aipFace;
	/**
    * 百度云 应用管理相关配置， 建议放在配置文件中
    */
	private static String appId = "15594124"; 
	private static String apiKey = "OEGrZkEZNOnFcosriMShEGOs";
	private static String secretKey = "IwAYN7BxE4o17fEw0FdNi0RMG1TLZyel"; 
	
	public static String getAppId() {
		return appId;
	}

	public static void setAppId(String appId) {
		BaiduFactory.appId = appId;
	}

	public static String getApiKey() {
		return apiKey;
	}

	public static void setApiKey(String apiKey) {
		BaiduFactory.apiKey = apiKey;
	}

	public static String getSecretKey() {
		return secretKey;
	}

	public static void setSecretKey(String secretKey) {
		BaiduFactory.secretKey = secretKey;
	}

	/**
    * 单例加载 
    * @return AipFace
    */
	public static AipFace getAipFace(){
		if(aipFace==null){
			synchronized (AipFace.class) {
				if(aipFace==null){
					aipFace = new AipFace(appId,apiKey,secretKey);
				}
			}
		}
		return aipFace;
	}
}
