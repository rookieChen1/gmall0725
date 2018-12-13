package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.BaseAttrInfo;
import com.atguigu.gmall.bean.SkuInfo;
import com.atguigu.gmall.bean.SpuImage;
import com.atguigu.gmall.bean.SpuSaleAttr;

import java.util.List;

public interface SkuService {

    public List<SkuInfo> getSkuInfoList(String spuId);

    public List<BaseAttrInfo> getAttrInfoList(String catalog3Id);

    public List<SpuSaleAttr> getSpuSaleAttrList(String spuId);

    public List<SpuImage> getSpuImageList(String spuId);

    public void saveSku(SkuInfo skuInfo);

    public SkuInfo getSkuInfo(String skuId);

    public List<SkuInfo> skuSaleAttrValueListBySpu(String spuId);

    public List<SkuInfo> getMySkuInfo(String catalog3Id);
}
