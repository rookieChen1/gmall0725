package com.atguigu.gmall.user.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.UserInfo;
import com.atguigu.gmall.user.mapper.UserInfoMapper;
import com.atguigu.gmall.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    UserInfoMapper userInfoMapper;
    @Override
    public List<UserInfo> getAll() {
        return userInfoMapper.selectAll();
    }

    @Override
    public UserInfo getOne(Integer id) {
        return userInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer saveUser(UserInfo userInfo) {
        return userInfoMapper.insert(userInfo);
    }

    @Override
    public Integer deleteUer(Integer id) {
        return userInfoMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Integer updateUser(UserInfo userInfo) {
        return userInfoMapper.updateByPrimaryKeySelective(userInfo);
    }
}
