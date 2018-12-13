package com.atguigu.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.BaseAttrInfo;
import com.atguigu.gmall.service.AttrService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class AttrController {
    @Reference
    AttrService attrService;

    @RequestMapping("/attrListPage")
    public String toAttrListPage(){
        return "attrListPage";
    }

    @ResponseBody
    @RequestMapping("/getAttrList")
    public List<BaseAttrInfo> getAttrInfoList(String catalog3Id){
       return attrService.getAttrInfoList(catalog3Id);
    }

    @RequestMapping(value = "saveAttrInfo",method = RequestMethod.POST)
    @ResponseBody
    public String saveAttrInfo(BaseAttrInfo baseAttrInfo){
        attrService.saveAttr(baseAttrInfo);
        return "success";
    }

    @RequestMapping(value = "delAttr",method = RequestMethod.POST)
    @ResponseBody
    public String delAttrInfo(String id){
        attrService.delAttr(id);
        return "success";
    }
}
