package com.atguigu.gmall.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.UserAddress;
import com.atguigu.gmall.service.UserAddressService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserAddressController {
    @Reference
    UserAddressService userAddressService;

    @RequestMapping("/userAddress/list")
    public List<UserAddress> getUserAddressList() {
        return userAddressService.getAll();
    }

    @RequestMapping("/userAddress/get/{id}")
    public UserAddress getUserAddress(@PathVariable("id") Integer id){
        return userAddressService.getOne(id);
    }

    @RequestMapping("/userAddress/add")
    public Integer addUserAddress(@RequestBody UserAddress userAddress){
        return userAddressService.saveUserAddress(userAddress);
    }

    @RequestMapping("/userAddress/del/{id}")
    public Integer delteUerAddress(@PathVariable("id") Integer id){
        return userAddressService.deleteUerAddress(id);
    }

    @RequestMapping("/userAddress/update/")
    public Integer updateUserAddress(@RequestBody UserAddress userAddress){
        return userAddressService.updateUserAddress(userAddress);
    }

}
