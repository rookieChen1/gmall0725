package com.atguigu.gmall.manage.service.impl;

import org.apache.commons.lang3.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.*;
import com.atguigu.gmall.manage.mapper.*;
import com.atguigu.gmall.service.SkuService;
import com.atguigu.gmall.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.List;

@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    SkuInfoMapper skuInfoMapper;
    @Autowired
    BaseAttrInfoMapper baseAttrInfoMapper;
    @Autowired
    BaseAttrValueMapper baseAttrValueMapper;
    @Autowired
    SpuSaleAttrMapper spuSaleAttrMapper;
    @Autowired
    SpuSaleAttrValueMapper spuSaleAttrValueMapper;
    @Autowired
    SpuImageMapper spuImageMapper;
    @Autowired
    SkuImageMapper skuImageMapper;
    @Autowired
    SkuAttrValueMapper skuAttrValueMapper;
    @Autowired
    SkuSaleAttrValueMapper skuSaleAttrValueMapper;
    @Autowired
    RedisUtil redisUtil;
    @Override
    public List<SkuInfo> getSkuInfoList(String spuId) {
        SkuInfo skuInfo = new SkuInfo();
        skuInfo.setSpuId(spuId);
        return skuInfoMapper.select(skuInfo);
    }

    @Override
    public List<BaseAttrInfo> getAttrInfoList(String catalog3Id) {
        BaseAttrInfo baseAttrInfo = new BaseAttrInfo();
        baseAttrInfo.setCatalog3Id(catalog3Id);
        List<BaseAttrInfo> baseAttrInfos = baseAttrInfoMapper.select(baseAttrInfo);
        for (BaseAttrInfo attrInfo : baseAttrInfos) {
            String attrInfoId = attrInfo.getId();
            BaseAttrValue baseAttrValue = new BaseAttrValue();
            baseAttrValue.setAttrId(attrInfoId);
            attrInfo.setAttrValueList(baseAttrValueMapper.select(baseAttrValue));
        }
        return baseAttrInfos;
    }

    @Override
    public List<SpuSaleAttr> getSpuSaleAttrList(String spuId) {
        SpuSaleAttr spuSaleAttr = new SpuSaleAttr();
        spuSaleAttr.setSpuId(spuId);
        List<SpuSaleAttr> spuSaleAttrs = spuSaleAttrMapper.select(spuSaleAttr);
        for (SpuSaleAttr saleAttr : spuSaleAttrs) {
            String saleAttrId = saleAttr.getSaleAttrId();
            SpuSaleAttrValue spuSaleAttrValue = new SpuSaleAttrValue();
            spuSaleAttrValue.setSpuId(spuId);
            spuSaleAttrValue.setSaleAttrId(saleAttrId);
            saleAttr.setSpuSaleAttrValueList(spuSaleAttrValueMapper.select(spuSaleAttrValue));
        }
        return spuSaleAttrs;
    }

    @Override
    public List<SpuImage> getSpuImageList(String spuId) {
        SpuImage spuImage = new SpuImage();
        spuImage.setSpuId(spuId);
        return spuImageMapper.select(spuImage);
    }

    @Override
    public void saveSku(SkuInfo skuInfo) {
        //保存sku信息
        skuInfoMapper.insert(skuInfo);
        String skuId = skuInfo.getId();
        //保存sku图片信息
        List<SkuImage> skuImageList = skuInfo.getSkuImageList();
        for (SkuImage skuImage : skuImageList) {
            skuImage.setSkuId(skuId);
            skuImageMapper.insert(skuImage);
        }
        //保存sku平台属性
        List<SkuAttrValue> skuAttrValueList = skuInfo.getSkuAttrValueList();
        for (SkuAttrValue skuAttrValue : skuAttrValueList) {
            skuAttrValue.setSkuId(skuId);
            skuAttrValueMapper.insert(skuAttrValue);
        }
        //保存sku销售属性
        List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
        for (SkuSaleAttrValue skuSaleAttrValue : skuSaleAttrValueList) {
            skuSaleAttrValue.setSkuId(skuId);
            skuSaleAttrValueMapper.insert(skuSaleAttrValue);
        }
    }

    //从缓存中获取skuInfo
    @Override
    public SkuInfo getSkuInfo(String skuId) {
        //获取redis连接
        Jedis jedis = redisUtil.getJedis();
        SkuInfo skuInfo;
        //查询缓存
        String cacheJson = jedis.get("sku:" + skuId + ":info");
        System.err.println(Thread.currentThread().getName()+"号线程，进入程序,skuId："+skuId);
        if(StringUtils.isBlank(cacheJson)){
            System.err.println(Thread.currentThread().getName()+"号线程，发现缓存中没有数据，申请分布缓存锁");
            // 分布式缓存锁服务器取锁
            String OK = jedis.set("sku:"+skuId+":lock","1","nx","px",10000);
            if(StringUtils.isNotBlank(OK)){
                System.err.println(Thread.currentThread().getName()+"号线程，申请成功，访问db");
                //未查到缓存，查询数据库
                skuInfo = getSkuInfoFromDB(skuId);
                if(skuInfo!=null){
                    // 将数据库的信息同步到缓存
                    System.err.println(Thread.currentThread().getName()+"号线程，从db中得到数据，放入缓存");
                    jedis.set("sku:" + skuId + ":info",JSON.toJSONString(skuInfo));
                    System.err.println(Thread.currentThread().getName()+"号线程，将锁释放。。。。。");
                    jedis.del("sku:"+skuId+":lock");
                }else{
                    // 在缓存中加入有超时时间的空值
                }
            }else {
                System.err.println(Thread.currentThread().getName()+"号线程，申请失败，开始自旋");
                //自旋
                return getSkuInfo(skuId);
            }

        }else{
            System.err.println(Thread.currentThread().getName()+"号线程，成功获得缓存数据。。。。");
            skuInfo = JSON.parseObject(cacheJson, SkuInfo.class);
        }
        jedis.close();
        return skuInfo;
    }

    //从数据库获取skuInfo
    public SkuInfo getSkuInfoFromDB(String skuId){

        SkuInfo skuInfo = skuInfoMapper.selectByPrimaryKey(skuId);
        SkuImage skuImage = new SkuImage();
        skuImage.setSkuId(skuId);
        skuInfo.setSkuImageList(skuImageMapper.select(skuImage));
        SkuAttrValue skuAttrValue = new SkuAttrValue();
        skuAttrValue.setSkuId(skuId);
        skuInfo.setSkuAttrValueList(skuAttrValueMapper.select(skuAttrValue));
        SkuSaleAttrValue skuSaleAttrValue = new SkuSaleAttrValue();
        skuSaleAttrValue.setSkuId(skuId);
        skuInfo.setSkuSaleAttrValueList(skuSaleAttrValueMapper.select(skuSaleAttrValue));

        return skuInfo;
    }

    @Override
    public List<SkuInfo> skuSaleAttrValueListBySpu(String spuId) {
        List<SkuInfo> skuInfos=skuSaleAttrValueMapper.selectSkuSaleAttrValueListBySpu(spuId);
        return skuInfos;
    }

    @Override
    public List<SkuInfo> getMySkuInfo(String catalog3Id) {
        SkuInfo skuInfo = new SkuInfo();
        skuInfo.setCatalog3Id(catalog3Id);
        List<SkuInfo> skuInfos = skuInfoMapper.select(skuInfo);
        for (SkuInfo info : skuInfos) {
            SkuAttrValue skuAttrValue = new SkuAttrValue();
            skuAttrValue.setSkuId(info.getId());
            List<SkuAttrValue> skuAttrValues = skuAttrValueMapper.select(skuAttrValue);
            info.setSkuAttrValueList(skuAttrValues);
        }

        return skuInfos;
    }
}
