package com.atguigu.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.BaseAttrValue;
import com.atguigu.gmall.manage.mapper.AttrValueMapper;
import com.atguigu.gmall.service.AttrValueService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Service
public class AttrValueServiceImpl implements AttrValueService {
    @Autowired
    AttrValueMapper attrValueMapper;
    @Override
    public List<BaseAttrValue> getAttrValueList(String attrId) {
        BaseAttrValue baseAttrValue = new BaseAttrValue();
        baseAttrValue.setAttrId(attrId);
        return attrValueMapper.select(baseAttrValue);
    }
}
