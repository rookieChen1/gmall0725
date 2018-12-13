package com.atguigu.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.BaseSaleAttr;
import com.atguigu.gmall.bean.SpuInfo;
import com.atguigu.gmall.bean.SpuSaleAttr;
import com.atguigu.gmall.manage.util.GmallUploadUtil;
import com.atguigu.gmall.service.SpuInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class SpuController {
    @Reference
    SpuInfoService spuInfoService;

    @RequestMapping("/spuListPage")
    public String toSpuListPage(){
        return "spuListPage";
    }

    //获取spu列表
    @ResponseBody
    @RequestMapping("/getSpuList")
    public List<SpuInfo> getSpuList(String catalog3Id){
        return spuInfoService.getSpuList(catalog3Id);
    }

    //获取销售属性列表
    @ResponseBody
    @RequestMapping("/baseSaleAttrList")
    public List<BaseSaleAttr> getSaleAttrList(){
        return spuInfoService.getSaleAttrList();
    }

    //保存spu
    @ResponseBody
    @RequestMapping("/saveSpu")
    public String saveSpu(SpuInfo spuInfo){
        spuInfoService.saveSpu(spuInfo);
        return "success";
    }

    //上传图片
    @ResponseBody
    @RequestMapping("/fileUpload")
    public String fileUpload(@RequestParam("file") MultipartFile multipartFile){
        String imgUrl = GmallUploadUtil.uploadImage(multipartFile);
        return imgUrl;
    }
}
