package com.atguigu.gmall.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.CartInfo;
import com.atguigu.gmall.bean.SkuInfo;
import com.atguigu.gmall.service.CartService;
import com.atguigu.gmall.service.SkuService;
import com.atguigu.gmall.util.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CartController {
    @Reference
    SkuService skuService;
    @Reference
    CartService cartService;

    @RequestMapping("/addToCart")
    public String addSkuInfo(HttpServletResponse response, HttpServletRequest request, String skuId , int num){
        //向购物车添加逻辑
        SkuInfo skuInfo = skuService.getSkuInfo(skuId);
        CartInfo cartInfo = new CartInfo();
        cartInfo.setCartPrice(skuInfo.getPrice());
        cartInfo.setSkuNum(num);
        cartInfo.setSkuPrice(skuInfo.getPrice());
        cartInfo.setIsChecked("1");
        cartInfo.setSkuName(skuInfo.getSkuName());
        cartInfo.setSkuId(skuId);
        cartInfo.setImgUrl(skuInfo.getSkuDefaultImg());

        List<CartInfo> cartInfoList= new ArrayList<>();
        String userId = "2";
        if(StringUtils.isNotBlank(userId)){
            //已经登录
            cartInfo.setUserId(userId);
            //检测购物车中是否有该商品
            CartInfo cartInfoDb =cartService.selectCartExists(cartInfo);
            if(cartInfoDb==null){
                //新增
                cartService.addCart(cartInfo);
            }else {
                //更新
                cartInfoDb.setSkuNum(cartInfoDb.getSkuNum()+num);
                cartInfoDb.setCartPrice(cartInfoDb.getSkuPrice().multiply(new BigDecimal(cartInfoDb.getSkuNum())));

                cartService.updateCart(cartInfoDb);
            }
            cartService.flushCache(userId);
        }else{
            //未登录
            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
            if(StringUtils.isBlank(cartListCookie)){
                //cookie为空新增
                cartInfoList.add(cartInfo);
            }else {
                // cookie中的购物车集合
                cartInfoList = JSON.parseArray(cartListCookie, CartInfo.class);
                //检测cookie购物车集合中是否有当前cartInfo
                Boolean b=if_new_cart(cartInfoList,cartInfo);

                if(!b){
                    //新增
                    cartInfoList.add(cartInfo);
                }else {
                    //更新
                    for (CartInfo info : cartInfoList) {
                        if(info.getSkuId().equals(cartInfo.getSkuId())){
                            info.setSkuNum(info.getSkuNum()+num);
                            info.setCartPrice(info.getSkuPrice().multiply(new BigDecimal(info.getSkuNum())));
                        }
                    }
                }
            }
            CookieUtil.setCookie(request,response,"cartListCookie",JSON.toJSONString(cartInfoList),60*60*24,true);
        }
        return "redirect:/cartAddSuccess?skuId="+skuId+"&num="+num;
    }

    //集合中有返回true，没有返回false
    private Boolean if_new_cart(List<CartInfo> cartInfoList, CartInfo cartInfo){

        for (CartInfo cart : cartInfoList) {
            if(cart.getSkuId().equals(cartInfo.getSkuId())){
                return true;
            }
        }

        return false;
    }

    @RequestMapping("cartAddSuccess")
    public String cartAddSuccess(String skuId, Model model,int num){
        SkuInfo skuInfo = skuService.getSkuInfo(skuId);
        model.addAttribute("skuInfo",skuInfo);
        model.addAttribute("skuNum",num);
        return "success";
    }

    @RequestMapping("cartList")
    public String cartList(HttpServletRequest request, ModelMap map){
        //判断用户是否登录
        String userId="2";
        List<CartInfo> cartInfoList=new ArrayList<>();

        if(StringUtils.isBlank(userId)){
            //未登录从Cookie中取数据
            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
            if(StringUtils.isNotBlank(cartListCookie)){
                cartInfoList=JSON.parseArray(cartListCookie,CartInfo.class);
            }
        }else {
            //登陆从缓存中取数据
            cartInfoList = cartService.getCartListFromCache(userId);
        }

        map.put("cartList",cartInfoList);
        BigDecimal totalPrice = getCartSum(cartInfoList);
        map.put("totalPrice",totalPrice);
        return "cartList";
    }

    private BigDecimal getCartSum(List<CartInfo> cartInfoList){
        BigDecimal bigDecimal = new BigDecimal("0");
        if(cartInfoList!=null){
            for (CartInfo cartInfo : cartInfoList) {
                if(cartInfo.getIsChecked().equals("1")){
                    bigDecimal= bigDecimal.add(cartInfo.getCartPrice());
                }
            }
        }
        System.out.println(bigDecimal);
        return  bigDecimal;
    }
}
