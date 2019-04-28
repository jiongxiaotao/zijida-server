package tch.zijidaserver.Controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import tch.zijidaserver.service.Code2SessionService;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class Code2SessionController {
    @Autowired
    private Code2SessionService code2SessionService;
    private Log log = LogFactory.getLog(Code2SessionController.class);
    @RequestMapping(value = "/code2Session", method = RequestMethod.GET)
    public Map<String, Object> code2Session(@RequestParam("appId") String appId,
                                           @RequestParam("secret") String secret,
                                           @RequestParam("jsCode") String jsCode)
    {
        log.info("[REQUEST]/code2Session?appId="+appId);
        return code2SessionService.code2Session(appId,secret,jsCode);
    }

}
