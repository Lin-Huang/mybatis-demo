package com.lin.controller;

import com.github.pagehelper.PageInfo;
import com.lin.domain.LearnResource;
import com.lin.model.QueryLearnListReq;
import com.lin.service.LearnService;
import com.lin.util.AjaxObject;
import com.lin.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "/learn")
public class LearnController extends AbstractController {
    @Autowired
    private LearnService learnService;

    @RequestMapping("")
    public String learn(Model model){
        model.addAttribute("ctx", getContextPath()+"/");
        return "learn-resource";
    }

    @RequestMapping(value = "/queryLearnList", method = RequestMethod.POST)
    @ResponseBody
    public AjaxObject queryLearnList(Page<QueryLearnListReq> page) {
        List<LearnResource> resourceList = learnService.queryLearnResourceList(page);
        PageInfo<LearnResource> pageInfo = new PageInfo<>(resourceList);
        return AjaxObject.ok().put("page", pageInfo);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public AjaxObject addLearn(@RequestBody LearnResource learnResource) {
        learnService.save(learnResource);
        return AjaxObject.ok();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public AjaxObject updateLearn(LearnResource learnResource) {
        learnService.updateNotNull(learnResource);
        return AjaxObject.ok();
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxObject deleteLearn(@RequestBody Long ids[]){
        learnService.deleteBatch(ids);
        return AjaxObject.ok();
    }
}
