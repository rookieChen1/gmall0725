package com.atguigu.gmall.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.UserInfo;
import com.atguigu.gmall.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserInfoController {

    @Reference
    UserInfoService userInfoService;

    @RequestMapping("/user/list")
    public List<UserInfo> getUserList() {
        return userInfoService.getAll();
    }

    @RequestMapping("/user/get/{id}")
    public UserInfo getUser(@PathVariable("id") Integer id){
        return userInfoService.getOne(id);
    }

    @RequestMapping("/user/add")
    public Integer saveUser(@RequestBody UserInfo userInfo){
        return userInfoService.saveUser(userInfo);
    }

    @RequestMapping("/user/del/{id}")
    public Integer delteUer(@PathVariable("id") Integer id){
        return userInfoService.deleteUer(id);
    }

    @RequestMapping("/user/update/")
    public Integer updateUser(@RequestBody UserInfo userInfo){
        return userInfoService.updateUser(userInfo);
    }
}
