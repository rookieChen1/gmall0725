package com.atguigu.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.*;
import com.atguigu.gmall.manage.mapper.*;
import com.atguigu.gmall.service.SpuInfoService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

@Service
public class SpuInfoServiceImpl implements SpuInfoService {
    @Autowired
    SpuInfoMapper spuInfoMapper;
    @Autowired
    BaseSaleAttrMapper saleAttrMapper;
    @Autowired
    SpuImageMapper spuImageMapper;
    @Autowired
    SpuSaleAttrMapper spuSaleAttrMapper;
    @Autowired
    SpuSaleAttrValueMapper spuSaleAttrValueMapper;
    @Autowired
    SkuInfoMapper skuInfoMapper;

    @Override
    public List<SpuInfo> getSpuList(String catalog3Id) {
        SpuInfo spuInfo = new SpuInfo();
        spuInfo.setCatalog3Id(catalog3Id);
        return spuInfoMapper.select(spuInfo);
    }

    @Override
    public List<BaseSaleAttr> getSaleAttrList() {
        return saleAttrMapper.selectAll();
    }

    @Override
    public void saveSpu(SpuInfo spuInfo) {
        //保存spu信息
        spuInfoMapper.insert(spuInfo);
        String spuInfoId = spuInfo.getId();
        //保存属性列表
        List<SpuSaleAttr> spuSaleAttrList = spuInfo.getSpuSaleAttrList();
        if(spuSaleAttrList!=null){
            for (SpuSaleAttr spuSaleAttr : spuSaleAttrList) {
                spuSaleAttr.setSpuId(spuInfoId);
                spuSaleAttrMapper.insert(spuSaleAttr);
                String saleAttrId = spuSaleAttr.getSaleAttrId();
                //保存属性值
                List<SpuSaleAttrValue> spuSaleAttrValueList = spuSaleAttr.getSpuSaleAttrValueList();
                if(spuSaleAttrValueList!=null){
                    for (SpuSaleAttrValue spuSaleAttrValue : spuSaleAttrValueList) {
                        spuSaleAttrValue.setSaleAttrId(saleAttrId);
                        spuSaleAttrValue.setSpuId(spuInfoId);
                        spuSaleAttrValueMapper.insert(spuSaleAttrValue);
                    }
                }
            }
        }
        //保存图片列表
        List<SpuImage> spuImageList = spuInfo.getSpuImageList();
        if(spuImageList!=null){
            for (SpuImage spuImage : spuImageList) {
                spuImage.setSpuId(spuInfoId);
                spuImageMapper.insert(spuImage);
            }
        }
    }

    @Override
    public SpuInfo getSpuInfo(String skuId) {
        String spuId = skuInfoMapper.selectByPrimaryKey(skuId).getSpuId();
        SpuInfo spuInfo = spuInfoMapper.selectByPrimaryKey(spuId);
        SpuSaleAttr spuSaleAttr = new SpuSaleAttr();
        spuSaleAttr.setSpuId(spuId);
        spuInfo.setSpuSaleAttrList(spuSaleAttrMapper.select(spuSaleAttr));
        List<SpuSaleAttr> spuSaleAttrList = spuInfo.getSpuSaleAttrList();
        for (SpuSaleAttr saleAttr : spuSaleAttrList) {
            SpuSaleAttrValue spuSaleAttrValue = new SpuSaleAttrValue();
            spuSaleAttrValue.setSpuId(spuId);
            spuSaleAttrValue.setSaleAttrId(saleAttr.getSaleAttrId());
            saleAttr.setSpuSaleAttrValueList(spuSaleAttrValueMapper.select(spuSaleAttrValue));
        }
        return spuInfo;
    }

    //查询spu属性列表
    @Override
    public List<SpuSaleAttr> getSaleAttrList(String skuId) {
        String spuId = skuInfoMapper.selectByPrimaryKey(skuId).getSpuId();
        HashMap<String, Object> map = new HashMap<>();
        map.put("skuId",skuId);
        map.put("spuId",spuId);
        return spuSaleAttrMapper.selectSpuSaleAttrListBySpuId(map);
    }
}
