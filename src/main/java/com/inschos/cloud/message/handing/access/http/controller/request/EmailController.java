package com.inschos.cloud.message.handing.access.http.controller.request;

import com.inschos.cloud.message.handing.access.http.controller.action.EmailAction;
import com.inschos.cloud.message.handing.access.http.controller.bean.ActionBean;
import com.inschos.cloud.message.handing.annotation.GetActionBeanAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/web/email")
public class EmailController {

    @Autowired
    private EmailAction emailAction;

    @GetActionBeanAnnotation(isCheckAccess = false)
    @RequestMapping("/send")
    @ResponseBody
    public String sendEmail(ActionBean actionBean){
        return emailAction.addEmailInfo(actionBean);
    }

    @GetActionBeanAnnotation(isCheckAccess = false)
    @RequestMapping("/status")
    @ResponseBody
    public String sendEmailStatus(ActionBean actionBean){
            return emailAction.sendEmailStatus(actionBean);
    }

}
