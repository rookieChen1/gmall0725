package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.BaseAttrInfo;

import java.util.List;

public interface AttrService {

    public List<BaseAttrInfo> getAttrInfoList(String catalog3Id);

    public void saveAttr(BaseAttrInfo baseAttrInfo);

    public void delAttr(String id);
}
