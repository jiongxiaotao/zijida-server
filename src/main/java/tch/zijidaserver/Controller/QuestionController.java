package tch.zijidaserver.Controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tch.zijidaserver.entity.Question;
import tch.zijidaserver.service.QuestionService;

import java.util.Map;

@RestController
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    private Log log = LogFactory.getLog(QuestionController.class);
    @RequestMapping(value = "/getQuestionList", method = RequestMethod.GET)
    public Map<String, Object> getQuestionList(@RequestParam("loginCode") String loginCode,
                                                @RequestParam("projectId") long projectId)
    {
        log.info("[REQUEST]/getQuestionList?loginCode="+loginCode+"projectId="+projectId);
        return questionService.queryQuestionList(loginCode,projectId);
    }
    @RequestMapping(value="/addQuestion",method = {RequestMethod.GET,RequestMethod.POST})
    public Map<String, Object> addQuestion(@RequestParam("loginCode") String loginCode,
                                              @RequestBody Question question)
    {
        log.info("[REQUEST]/addQuestion?loginCode="+loginCode+" body="+question);
        return questionService.insertQuestion(loginCode,question);
    }
    @RequestMapping(value="/updateQuestion",method = {RequestMethod.GET,RequestMethod.POST})
    public Map<String, Object> updateQuestion(@RequestParam("loginCode") String loginCode,
                                          @RequestBody Question question)
    {
        log.info("[REQUEST]/updateQuestion?loginCode="+loginCode+" body="+question);
        return questionService.updateQuestion(loginCode,question);
    }
    //将问卷中的一个问题移动到另一个位置
    @RequestMapping(value="/moveQuestion",method = {RequestMethod.GET})
    public Map<String, Object> moveQuestion(@RequestParam("loginCode") String loginCode,
                                            @RequestParam("questionId") long questionId,
                                            @RequestParam("destNumber") int destNumber)
    {
        log.info("[REQUEST]/updateQuestion?loginCode="+loginCode+" questionId="+questionId+" destNumber="+destNumber);
        return questionService.updateQuestionNumbers(loginCode,questionId,destNumber);
    }
    @RequestMapping(value="/deleteQuestion",method = RequestMethod.GET)
    public Map<String, Object> deleteQuestion(@RequestParam("loginCode") String loginCode,
                                             @RequestParam("questionId") long questionId)
    {
        log.info("[REQUEST]/deleteQuestion?loginCode="+loginCode+" questionId="+questionId);
        return questionService.deleteQuestion(loginCode,questionId);
    }
}
