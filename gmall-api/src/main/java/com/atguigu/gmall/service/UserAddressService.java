package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.UserAddress;

import java.util.List;

public interface UserAddressService {

    public List<UserAddress> getAll();

    UserAddress getOne(Integer id);

    Integer saveUserAddress(UserAddress userAddress);

    Integer deleteUerAddress(Integer id);

    Integer updateUserAddress(UserAddress userAddress);
}
