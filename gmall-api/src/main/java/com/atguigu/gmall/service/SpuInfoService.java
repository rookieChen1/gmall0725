package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.BaseSaleAttr;
import com.atguigu.gmall.bean.SpuInfo;
import com.atguigu.gmall.bean.SpuSaleAttr;

import java.util.List;

public interface SpuInfoService {

    public List<SpuInfo> getSpuList(String catalog3Id);

    public List<BaseSaleAttr> getSaleAttrList();

    public void saveSpu(SpuInfo spuInfo);

    public SpuInfo getSpuInfo(String skuId);

    public List<SpuSaleAttr> getSaleAttrList(String skuId);
}
