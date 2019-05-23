package tch.zijidaserver.Controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tch.zijidaserver.entity.Choise;
import tch.zijidaserver.service.ChoiseService;

import java.util.Map;

@RestController
public class ChoiseController {
    @Autowired
    private ChoiseService choiseService;
    private Log log = LogFactory.getLog(ChoiseController.class);
    @RequestMapping(value = "/getChoiseList", method = RequestMethod.GET)
    public Map<String, Object> getChoiseList(@RequestParam("loginCode") String loginCode,
                                                @RequestParam("questionId") long questionId)
    {
        log.info("[REQUEST]/getChoiseList?loginCode="+loginCode+"questionId="+questionId);
        return choiseService.queryChoiseList(loginCode,questionId);
    }
    @RequestMapping(value="/addChoise",method = {RequestMethod.GET,RequestMethod.POST})
    public Map<String, Object> addChoise(@RequestParam("loginCode") String loginCode,
                                              @RequestBody Choise choise)
    {
        log.info("[REQUEST]/addChoise?loginCode="+loginCode+" body="+choise);
        return choiseService.insertChoise(loginCode,choise);
    }
    @RequestMapping(value="/updateChoise",method = {RequestMethod.GET,RequestMethod.POST})
    public Map<String, Object> updateChoise(@RequestParam("loginCode") String loginCode,
                                          @RequestBody Choise choise)
    {
        log.info("[REQUEST]/updateChoise?loginCode="+loginCode+" body="+choise);
        return choiseService.updateChoise(loginCode,choise);
    }
    
    @RequestMapping(value="/deleteChoise",method = RequestMethod.GET)
    public Map<String, Object> deleteChoise(@RequestParam("loginCode") String loginCode,
                                             @RequestParam("choiseId") long choiseId)
    {
        log.info("[REQUEST]/deleteChoise?loginCode="+loginCode+" choiseId="+choiseId);
        return choiseService.deleteChoise(loginCode,choiseId);
    }
}
