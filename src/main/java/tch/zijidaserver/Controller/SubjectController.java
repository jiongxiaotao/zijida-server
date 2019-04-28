package tch.zijidaserver.Controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tch.zijidaserver.entity.Subject;
import tch.zijidaserver.service.SubjectService;

import java.util.Map;

@RestController
public class SubjectController {
    @Autowired
    private SubjectService subjectService;
    private Log log = LogFactory.getLog(SubjectController.class);
    @RequestMapping(value = "/getSubjectList", method = RequestMethod.GET)
    public Map<String, Object> getSubjectList(@RequestParam("loginCode") String loginCode,
                                                @RequestParam("projectId") long projectId)
    {
        log.info("[REQUEST]/getSubjectList?loginCode="+loginCode+"projectId="+projectId);
        return subjectService.querySubjectList(loginCode,projectId);
    }
    @RequestMapping(value="/addSubject",method = {RequestMethod.GET,RequestMethod.POST})
    public Map<String, Object> addSubject(@RequestParam("loginCode") String loginCode,
                                              @RequestBody Subject subject)
    {
        log.info("[REQUEST]/addSubject?loginCode="+loginCode+" body="+subject);
        return subjectService.insertSubject(loginCode,subject);
    }
    @RequestMapping(value="/updateSubject",method = {RequestMethod.GET,RequestMethod.POST})
    public Map<String, Object> updateSubject(@RequestParam("loginCode") String loginCode,
                                          @RequestBody Subject subject)
    {
        log.info("[REQUEST]/updateSubject?loginCode="+loginCode+" body="+subject);
        return subjectService.updateSubject(loginCode,subject);
    }
    @RequestMapping(value="/deleteSubject",method = RequestMethod.GET)
    public Map<String, Object> deleteSubject(@RequestParam("loginCode") String loginCode,
                                             @RequestParam("subjectId") long subjectId)
    {
        log.info("[REQUEST]/deleteSubject?loginCode="+loginCode+" subjectId="+subjectId);
        return subjectService.deleteSubject(loginCode,subjectId);
    }
}
