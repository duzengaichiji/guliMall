package com.nhx.gmall.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.nhx.gmall.bean.OmsCartItem;
import com.nhx.gmall.bean.PmsSkuInfo;
import com.nhx.gmall.service.CartService;
import com.nhx.gmall.service.SkuService;
import com.nhx.gmall.util.CookieUtil;
import jodd.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class CartController {
    @Reference
    SkuService skuService;
    @Reference
    CartService cartService;

    @RequestMapping("cartList")
    public String cartList(HttpServletRequest request, HttpServletResponse response, HttpSession session,ModelMap modelMap){
        List<OmsCartItem> omsCartItems = new ArrayList<>();
        String userId = "1";//"";
        if(StringUtil.isNotBlank(userId)){
            //已经登录，查询db
            omsCartItems = cartService.cartList(userId);
        }else{
            //没有登录，查询cookie
            String cartListCookie = CookieUtil.getCookieValue(request,"cartListCookie",true);
            if(StringUtil.isNotBlank(cartListCookie)){
                omsCartItems = JSON.parseArray(cartListCookie,OmsCartItem.class);
            }
        }
        modelMap.put("cartList",omsCartItems);
        return "cartList";
    }

    @RequestMapping("addToCart")
    public String addToCart(String skuId, long quantity, HttpServletRequest request, HttpServletResponse response){
        //调用商品服务查询商品信息
        PmsSkuInfo pmsSkuInfo = skuService.getSkuById(skuId);
        System.out.println(pmsSkuInfo);

        //将商品信息封装成购物车信息
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setCreateDate(new Date());
        omsCartItem.setDeleteStatus(0);
        omsCartItem.setModifyDate(new Date());
        omsCartItem.setPrice(pmsSkuInfo.getPrice());
        omsCartItem.setProductAttr("");
        omsCartItem.setProductBrand("");
        omsCartItem.setProductCategoryId(pmsSkuInfo.getCatalog3Id());
        omsCartItem.setProductId(pmsSkuInfo.getProductId());
        omsCartItem.setProductSkuId(Long.valueOf(skuId));
        omsCartItem.setProductName(pmsSkuInfo.getSkuName());
        omsCartItem.setProductPic(pmsSkuInfo.getSkuDefaultImg());
        omsCartItem.setProductSkuCode("1111");
        omsCartItem.setQuantity(quantity);
        //判断用户登录
        String memberId = "1";


        if(StringUtils.isNoneBlank(memberId)){
            //用户已经登录,数据写入db
            //从DB中查询用户购物车数据
            OmsCartItem omsCartItemFromDB = cartService.ifCartExistByUser(memberId,skuId);
            if(omsCartItemFromDB==null){
                //如果DB中没有
                omsCartItem.setMemberId(Long.valueOf(memberId));
                omsCartItem.setQuantity(quantity);
                cartService.addCart(omsCartItem);
            }else{
                //该商品在用户购物车中
                omsCartItemFromDB.setQuantity(omsCartItem.getQuantity()+omsCartItemFromDB.getQuantity());
                cartService.updateCart(omsCartItemFromDB);
            }

            //同步redis缓存
            cartService.flushCartCache(memberId);

        }else{
            //用户没有登录，数据仅写入cookie
            //原有的cookie
            List<OmsCartItem> omsCartItemList = new ArrayList<>();

            String cartListCookie = CookieUtil.getCookieValue(request,"cartListCookie",true);
            if(StringUtil.isNotBlank(cartListCookie)) {
                //如果该
                omsCartItemList = JSON.parseArray(cartListCookie, OmsCartItem.class);
                //是否已经存在
                boolean exist = if_cart_exist(omsCartItemList,omsCartItem);
                if(exist){
                    //已经存在，更新原有信息
                    for(OmsCartItem omsCartItem1:omsCartItemList){
                        if(omsCartItem1.getProductSkuId()==omsCartItem.getProductSkuId()){
                            omsCartItem1.setQuantity(omsCartItem1.getQuantity()+omsCartItem.getQuantity());
                            //omsCartItem1.setPrice(omsCartItem1.getPrice().add(omsCartItem.getPrice()));
                        }
                    }
                }else {
                    //不存在的商品cookie,添加
                    omsCartItemList.add(omsCartItem);
                }
            }else{
                //原本没有cookie
                omsCartItemList.add(omsCartItem);
            }
            //更新cookie
            CookieUtil.setCookie(request, response, "cartListCookie", JSON.toJSONString(omsCartItemList), 60 * 60 * 72, true);
        }
        return "redirect:/success.html";
    }

    private boolean if_cart_exist(List<OmsCartItem> omsCartItemList1, OmsCartItem omsCartItem) {
        boolean exist = false;
        for(OmsCartItem omsCartItem1:omsCartItemList1){
            Long productSkuId = omsCartItem1.getProductSkuId();
            if(productSkuId==omsCartItem1.getProductSkuId()){
                exist = true;
            }
        }
        return exist;
    }
}
