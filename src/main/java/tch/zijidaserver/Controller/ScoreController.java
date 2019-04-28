package tch.zijidaserver.Controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tch.zijidaserver.entity.Score;
import tch.zijidaserver.service.ScoreService;

import java.util.List;
import java.util.Map;

@RestController
public class ScoreController {
    @Autowired
    private ScoreService scoreService;
    private Log log = LogFactory.getLog(ScoreController.class);
    @RequestMapping(value = "/getResult", method = RequestMethod.GET)
    public Map<String, Object> getResult(@RequestParam("loginCode") String loginCode,
                                         @RequestParam("projectId") long projectId)
    {
        log.info("[REQUEST]/getResult?loginCode="+loginCode+"projectId="+projectId);
        return scoreService.getResult(loginCode,projectId);
    }
    @RequestMapping(value="/submitProjectScore",method = {RequestMethod.GET,RequestMethod.POST})
    public Map<String, Object> submitProjectScore(@RequestParam("loginCode") String loginCode,
                                                  @RequestParam("projectId") long projectId,
                                                @RequestBody List<Score> scoreList)
    {
        log.info("[REQUEST]/submitProjectScore?loginCode="+loginCode+" projectId="+projectId);
        return scoreService.submitProjectScore(loginCode,projectId,scoreList);
    }

}
