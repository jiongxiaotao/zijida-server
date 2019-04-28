
package tch.zijidaserver.service;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Repository;

/**
 * Created by ainstain on 2017/11/14.
 */
@Repository
public class MiniService {
    public static final String BK_STATUS = "BK_STATUS";
    public static final String BK_CODE = "BK_CODE";
    public static final String BK_DESC = "BK_DESC";

    public static final String SYS_TX_STATUS_SUCC = "00";
    public static final String SYS_TX_STATUS_NOSUCC = "01";
    public static final String SYS_RESP_CODE_SUCC = "000000000000";
    public static final String SYS_RESP_DESC_SUCC = "成功";

    public Map<String,Object> newSuccessResponseMap(){
        Map<String,Object> result = new HashMap<String,Object>();
        result.put(BK_STATUS, SYS_TX_STATUS_SUCC);
        result.put(BK_CODE, SYS_RESP_CODE_SUCC);
        result.put(BK_DESC, SYS_RESP_DESC_SUCC);
        return result;
    }


	public Map<String,Object> newErrorResponseMap(int errorCode,String errorMsg){
        Map<String,Object> result = new HashMap<String,Object>();
        result.put(BK_STATUS, SYS_TX_STATUS_NOSUCC);
        result.put(BK_CODE, String.valueOf(errorCode));//错误内容
        result.put(BK_DESC, errorMsg);//错误内容
        return result;
    }
}
