package tch.zijidaserver.Controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tch.zijidaserver.entity.Message;
import tch.zijidaserver.service.MessageService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class MessageController {
    @Autowired
    private MessageService messageService;
    private Log log = LogFactory.getLog(MessageController.class);
    @RequestMapping(value="/uploadVoiceMessage",produces = "application/json",method = {RequestMethod.GET,RequestMethod.POST})
    public Map<String, Object> addMessage(HttpServletRequest request,
                                          @RequestParam("voiceMessage") MultipartFile files,
                                          int voiceDuration, String loginCode)
    {
        log.info("[REQUEST]/addMessage?loginCode="+loginCode+" body="+files+voiceDuration);
        return messageService.insertMessage(loginCode,files,voiceDuration);
    }

}
