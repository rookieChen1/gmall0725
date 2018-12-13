package com.atguigu.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.BaseAttrInfo;
import com.atguigu.gmall.bean.SkuInfo;
import com.atguigu.gmall.bean.SpuImage;
import com.atguigu.gmall.bean.SpuSaleAttr;
import com.atguigu.gmall.service.SkuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class SkuController {

    @Reference
    SkuService skuService;

    @ResponseBody
    @RequestMapping("/skuInfoListBySpu")
    public List<SkuInfo> skuInfoListBySpu(String spuId){
        return skuService.getSkuInfoList(spuId);
    }

    @ResponseBody
    @RequestMapping("/attrInfoList")
    public List<BaseAttrInfo> getAttrInfoList(String catalog3Id){
        List<BaseAttrInfo> attrList=skuService.getAttrInfoList(catalog3Id);
        return attrList;
    }

    @ResponseBody
    @RequestMapping("/spuSaleAttrList")
    public List<SpuSaleAttr> getSpuSaleAttrList(String spuId){
        List<SpuSaleAttr> spuSaleAttrList = skuService.getSpuSaleAttrList(spuId);
        return spuSaleAttrList;
    }

    @ResponseBody
    @RequestMapping("/spuImageList")
    public List<SpuImage> getSpuImageList(String spuId){
        List<SpuImage> spuImageList=skuService.getSpuImageList(spuId);
        return spuImageList;
    }

    @ResponseBody
    @RequestMapping("/saveSku")
    public String saveSku(SkuInfo skuInfo){
        skuService.saveSku(skuInfo);
        return "success";
    }
}
