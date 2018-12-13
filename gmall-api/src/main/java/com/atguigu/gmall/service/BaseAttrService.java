package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.BaseAttrInfo;

import java.util.List;
import java.util.Set;

public interface BaseAttrService {

   public List<BaseAttrInfo> getAttrListByValueIds(Set<String> valueIds);
}
