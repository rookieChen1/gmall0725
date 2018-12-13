package com.atguigu.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.BaseAttrInfo;
import com.atguigu.gmall.bean.BaseAttrValue;
import com.atguigu.gmall.manage.mapper.AttrMapper;
import com.atguigu.gmall.manage.mapper.AttrValueMapper;
import com.atguigu.gmall.service.AttrService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class AttrServiceImpl implements AttrService{
    @Autowired
    AttrMapper attrMapper;
    @Autowired
    AttrValueMapper attrValueMapper;
    @Override
    public List<BaseAttrInfo> getAttrInfoList(String catalog3Id) {
        BaseAttrInfo baseAttrInfo = new BaseAttrInfo();
        baseAttrInfo.setCatalog3Id(catalog3Id);
        return attrMapper.select(baseAttrInfo);
    }

    @Override
    public void saveAttr(BaseAttrInfo baseAttrInfo) {
        String id = baseAttrInfo.getId();
        System.out.println(id);
        if("".equals(id)||id==null){
            baseAttrInfo.setId(null);
            attrMapper.insert(baseAttrInfo);
            BaseAttrInfo baseAttrInfo1 = attrMapper.selectOne(baseAttrInfo);
            List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
            if(attrValueList.size()>0) {
                for (BaseAttrValue baseAttrValue : attrValueList) {
                    //防止主键被赋上一个空字符串
                    baseAttrValue.setAttrId(baseAttrInfo1.getId());
                    baseAttrValue.setId(null);
                    attrValueMapper.insertSelective(baseAttrValue);
                }
            }
        }else{
            attrMapper.updateByPrimaryKey(baseAttrInfo);
            BaseAttrValue baseAttrValue = new BaseAttrValue();
            baseAttrValue.setAttrId(baseAttrInfo.getId());
            attrValueMapper.delete(baseAttrValue);
            List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
            if(attrValueList.size()>0) {
                for (BaseAttrValue attrValue : attrValueList) {
                        //防止主键被赋上一个空字符串
                        attrValue.setAttrId(baseAttrInfo.getId());
                        attrValue.setId(null);
                        attrValueMapper.insertSelective(attrValue);
                }
            }
        }
    }

    @Override
    public void delAttr(String id) {
        BaseAttrValue baseAttrValue = new BaseAttrValue();
        baseAttrValue.setAttrId(id);
        attrValueMapper.delete(baseAttrValue);
        attrMapper.deleteByPrimaryKey(id);
    }

}
