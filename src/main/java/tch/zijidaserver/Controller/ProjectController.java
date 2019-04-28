package tch.zijidaserver.Controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tch.zijidaserver.entity.Project;
import tch.zijidaserver.service.ProjectService;

import java.util.Map;

@RestController
public class ProjectController {
    @Autowired
    private ProjectService projectService;
    private Log log = LogFactory.getLog(ProjectController.class);
    @RequestMapping(value = "/getProjectList", method = RequestMethod.GET)
    public Map<String, Object> getProjectList(@RequestParam("loginCode") String loginCode,
                                                @RequestParam("statusList") String statusList)
    {
        log.info("[REQUEST]/queryProjectList?loginCode="+loginCode+"&statusList="+statusList);
        return projectService.queryProjectList(loginCode,statusList);
    }
    @RequestMapping(value = "/getProjectByInviteCode", method = RequestMethod.GET)
    public Map<String, Object> getProjectByInviteCode(@RequestParam("loginCode") String loginCode,
                                              @RequestParam("inviteCode") String inviteCode)
    {
        log.info("[REQUEST]/queryProjectList?loginCode="+loginCode+"&inviteCode="+inviteCode);
        return projectService.queryProjectByInviteCode(loginCode,inviteCode);
    }
    @RequestMapping(value="/addProject",method = {RequestMethod.GET,RequestMethod.POST})
    public Map<String, Object> addProject(@RequestParam("loginCode") String loginCode,
                                              @RequestBody Project project)
    {
        log.info("[REQUEST]/addProject?loginCode="+loginCode+" body="+project);
        return projectService.insertProject(loginCode,project);
    }
    @RequestMapping(value="/updateProject",method = {RequestMethod.GET,RequestMethod.POST})
    public Map<String, Object> updateProject(@RequestParam("loginCode") String loginCode,
                                          @RequestBody Project project)
    {
        log.info("[REQUEST]/updateProject?loginCode="+loginCode+" body="+project);
        return projectService.updateProject(loginCode,project);
    }
    @RequestMapping(value="/deleteProject",method = RequestMethod.GET)
    public Map<String, Object> deleteProject(@RequestParam("loginCode") String loginCode,
                                             @RequestParam("projectId") long projectId)
    {
        log.info("[REQUEST]/deleteProject?loginCode="+loginCode+" projectId="+projectId);
        return projectService.deleteProject(loginCode,projectId);
    }
}
