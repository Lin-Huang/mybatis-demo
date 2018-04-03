package com.lin.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.lin.domain.LearnResouce;
import com.lin.service.LearnService;
import com.lin.tools.ServletUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/learn")
public class LearnController {
    @Autowired
    private LearnService learnService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("")
    public String learn() {
        return "learn-resource";
    }

    @RequestMapping(value = "/queryLearnList",
            method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    @ResponseBody
    public void queryLearnList(HttpServletRequest request, HttpServletResponse response) {
        String page = request.getParameter("page");
        String size = request.getParameter("rows");
        String author = request.getParameter("author");
        String title = request.getParameter("title");

        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        params.put("size", size);
        params.put("author", author);
        params.put("title", title);
        List<LearnResouce> learnResouceList = learnService.queryLearnResourceList(params);
        PageInfo<LearnResouce> pageInfo = new PageInfo<>(learnResouceList);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rows", learnResouceList);
        jsonObject.put("total", pageInfo.getPages());
        jsonObject.put("records", pageInfo.getTotal());
        ServletUtil.createSuccessResponse(200, jsonObject, response);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public void addLearn(HttpServletRequest request, HttpServletResponse response) {
        String author = request.getParameter("author");
        String url = request.getParameter("url");
        String title = request.getParameter("title");

        JSONObject result = new JSONObject();
        if (StringUtils.isBlank(author)){
            result.put("message", "作者不能为空");
            result.put("flag", false);
            ServletUtil.createSuccessResponse(200, result, response);
            return;
        }
        if (StringUtils.isBlank(title)){
            result.put("message", "标题不能为空");
            result.put("flag", false);
            ServletUtil.createSuccessResponse(200, result, response);
            return;
        }
        if (StringUtils.isBlank(url)){
            result.put("message", "链接不能为空");
            result.put("flag", false);
            ServletUtil.createSuccessResponse(200, result, response);
            return;
        }

        LearnResouce learnResouce = new LearnResouce();
        learnResouce.setAuthor(author);
        learnResouce.setTitle(title);
        learnResouce.setUrl(url);
        int index = learnService.add(learnResouce);
        if (index > 0) {
            result.put("message", "教程信息添加成功");
            result.put("flag", true);
        }else {
            result.put("message", "教程信息添加失败");
            result.put("flag", false);
        }
        ServletUtil.createSuccessResponse(200, result, response);


    }
}
