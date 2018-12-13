package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.UserInfo;

import java.util.List;

public interface UserInfoService {

    public List<UserInfo> getAll();

    UserInfo getOne(Integer id);

    Integer saveUser(UserInfo userInfo);

    Integer deleteUer(Integer id);

    Integer updateUser(UserInfo userInfo);
}
