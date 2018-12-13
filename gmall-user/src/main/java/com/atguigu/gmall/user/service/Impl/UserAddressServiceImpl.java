package com.atguigu.gmall.user.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.UserAddress;
import com.atguigu.gmall.service.UserAddressService;
import com.atguigu.gmall.user.mapper.UserAddressMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class UserAddressServiceImpl implements UserAddressService{

    @Autowired
    UserAddressMapper userAddressMapper;
    @Override
    public List<UserAddress> getAll() {
        return userAddressMapper.selectAll();
    }

    @Override
    public UserAddress getOne(Integer id) {
        return userAddressMapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer saveUserAddress(UserAddress userAddress) {
        return userAddressMapper.insert(userAddress);
    }

    @Override
    public Integer deleteUerAddress(Integer id) {
        return userAddressMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Integer updateUserAddress(UserAddress userAddress) {
        return userAddressMapper.updateByPrimaryKeySelective(userAddress);
    }
}
