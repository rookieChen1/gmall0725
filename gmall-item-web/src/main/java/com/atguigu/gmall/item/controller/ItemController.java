package com.atguigu.gmall.item.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.SkuInfo;
import com.atguigu.gmall.bean.SkuSaleAttrValue;
import com.atguigu.gmall.bean.SpuInfo;
import com.atguigu.gmall.bean.SpuSaleAttr;
import com.atguigu.gmall.service.SkuService;
import com.atguigu.gmall.service.SpuInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ItemController {
    @Reference
    SkuService skuService;
    @Reference
    SpuInfoService spuInfoService;
    @RequestMapping("/{skuId}.html")
    public String toItemPage(@PathVariable("skuId") String skuId,Model model){
        SkuInfo skuInfo = skuService.getSkuInfo(skuId);
        model.addAttribute("skuInfo",skuInfo);
        String spuId = skuInfo.getSpuId();
        //第一种方式
        //SpuInfo spuInfo=spuInfoService.getSpuInfo(skuId);
        //model.addAttribute("spuSaleAttrListCheckBySku",spuInfo.getSpuSaleAttrList());
        //第二种方式
        List<SpuSaleAttr> spuSaleAttrs=spuInfoService.getSaleAttrList(skuId);
        model.addAttribute("spuSaleAttrListCheckBySku",spuSaleAttrs);

        // 根据spuId制作页面销售属性的hash表
        // 销售属性组合：skuId
        List<SkuInfo> skuInfos=skuService.skuSaleAttrValueListBySpu(spuId);
        Map<String, String> stringStringHashMap = new HashMap<>();
        for (SkuInfo info : skuInfos) {
            String skuSaleAttrValueIdsKey = "";
            List<SkuSaleAttrValue> skuSaleAttrValueList = info.getSkuSaleAttrValueList();
            for (SkuSaleAttrValue skuSaleAttrValue : skuSaleAttrValueList) {
                skuSaleAttrValueIdsKey=skuSaleAttrValueIdsKey+"|"+skuSaleAttrValue.getSaleAttrValueId();
            }
            String skuIdValue = info.getId();
            stringStringHashMap.put(skuSaleAttrValueIdsKey,skuIdValue);
        }

        String s = JSON.toJSONString(stringStringHashMap);
        model.addAttribute("valuesSkuJson",s);
        return "item";
    }
}
