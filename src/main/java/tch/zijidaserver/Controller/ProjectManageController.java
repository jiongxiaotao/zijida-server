package tch.zijidaserver.Controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tch.zijidaserver.entity.Project;
import tch.zijidaserver.service.ProjectManageService;
import tch.zijidaserver.service.ProjectService;

import java.util.Map;

@RestController
public class ProjectManageController {
    @Autowired
    private ProjectManageService projectManageService;
    private Log log = LogFactory.getLog(ProjectManageController.class);

    @RequestMapping(value = "/getProjectManage", method = RequestMethod.GET)
    public Map<String, Object> getProjectManage(@RequestParam("loginCode") String loginCode)
    {
        log.info("[REQUEST]/queryProjectList?loginCode="+loginCode);
        return projectManageService.getProjectManage(loginCode);
    }

}
