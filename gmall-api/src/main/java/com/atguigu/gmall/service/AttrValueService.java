package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.BaseAttrValue;

import java.util.List;

public interface AttrValueService {

    public List<BaseAttrValue> getAttrValueList(String attrId);
}
