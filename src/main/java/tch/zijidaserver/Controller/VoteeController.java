package tch.zijidaserver.Controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tch.zijidaserver.entity.Votee;
import tch.zijidaserver.service.VoteeService;

import java.util.Map;

@RestController
public class VoteeController {
    @Autowired
    private VoteeService voteeService;
    private Log log = LogFactory.getLog(VoteeController.class);
    @RequestMapping(value = "/getVoteeList", method = RequestMethod.GET)
    public Map<String, Object> getVoteeList(@RequestParam("loginCode") String loginCode,
                                                @RequestParam("projectId") long projectId)
    {
        log.info("[REQUEST]/getVoteeList?loginCode="+loginCode+"projectId="+projectId);
        return voteeService.queryVoteeList(loginCode,projectId);
    }
    @RequestMapping(value="/addVotee",method = {RequestMethod.GET,RequestMethod.POST})
    public Map<String, Object> addVotee(@RequestParam("loginCode") String loginCode,
                                              @RequestBody Votee votee)
    {
        log.info("[REQUEST]/addVotee?loginCode="+loginCode+" body="+votee);
        return voteeService.insertVotee(loginCode,votee);
    }
    @RequestMapping(value="/updateVotee",method = {RequestMethod.GET,RequestMethod.POST})
    public Map<String, Object> updateVotee(@RequestParam("loginCode") String loginCode,
                                          @RequestBody Votee votee)
    {
        log.info("[REQUEST]/updateVotee?loginCode="+loginCode+" body="+votee);
        return voteeService.updateVotee(loginCode,votee);
    }
    @RequestMapping(value="/deleteVotee",method = RequestMethod.GET)
    public Map<String, Object> deleteVotee(@RequestParam("loginCode") String loginCode,
                                             @RequestParam("voteeId") long voteeId)
    {
        log.info("[REQUEST]/deleteVotee?loginCode="+loginCode+" voteeId="+voteeId);
        return voteeService.deleteVotee(loginCode,voteeId);
    }
}
