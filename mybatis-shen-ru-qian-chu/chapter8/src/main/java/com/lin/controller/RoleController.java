package com.lin.controller;

import com.lin.entity.RoleBean;
import com.lin.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RoleController {
    @Autowired
    private RoleService roleService;

    @RequestMapping("/role/getRole")
    @ResponseBody
    public RoleBean getRole(@RequestParam("id") int id) {
        long start = System.currentTimeMillis();
        RoleBean role = roleService.getRole(id);
        long end = System.currentTimeMillis();
        System.err.println(end - start);
        return role;
    }
}
