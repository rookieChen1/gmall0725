package com.atguigu.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.BaseAttrInfo;
import com.atguigu.gmall.manage.mapper.BaseAttrInfoMapper;
import com.atguigu.gmall.manage.mapper.BaseAttrValueMapper;
import com.atguigu.gmall.service.BaseAttrService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

@Service
public class BaseAttrServiceImpl implements BaseAttrService {
    @Autowired
    BaseAttrValueMapper baseAttrValueMapper;
    @Override
    public List<BaseAttrInfo> getAttrListByValueIds(Set<String> valueIds) {
        String valueIdStr = StringUtils.join(valueIds, ",");
        List<BaseAttrInfo> baseAttrInfos=baseAttrValueMapper.getAttrListByValueIds(valueIdStr);
        return baseAttrInfos;
    }
}
