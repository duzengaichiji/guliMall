package com.nhx.gmall.item.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.nhx.gmall.bean.PmsProductSaleAttr;
import com.nhx.gmall.bean.PmsSkuInfo;
import com.nhx.gmall.bean.PmsSkuSaleAttrValue;
import com.nhx.gmall.service.SkuService;
import com.nhx.gmall.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.spring.annotation.MapperScan;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class ItemController {

    @Reference
    SkuService skuService;
    @Reference
    SpuService spuService;

    @RequestMapping("{skuId}.html")
    public String item(@PathVariable String skuId, ModelMap modelMap, HttpServletRequest httpServletRequest){

        String requestAddr =  httpServletRequest.getRemoteAddr();//从请求中获取Ip地址
        //httpServletRequest.getHeader()//nginx负载均衡时使用这个获取IP
        System.out.println("request from:"+requestAddr+Thread.currentThread().getName());

        PmsSkuInfo pmsSkuInfo = skuService.getSkuById(skuId);
        //sku对象
        modelMap.put("skuInfo",pmsSkuInfo);

        //销售属性列表
        List<PmsProductSaleAttr> pmsProductSaleAttrList = spuService.spuScaleAttrListCheckBySku(pmsSkuInfo.getProductId(),skuId);
        modelMap.put("spuSaleAttrListCheckBySku",pmsProductSaleAttrList);

        //查询当前的spu的所有sku对应的哈希表集合
        HashMap<String,String> skuSaleAttrHash = new HashMap<>();
        List<PmsSkuInfo> pmsSkuInfoList = skuService.getSpuSaleAttrListCheckBySku(pmsSkuInfo.getProductId());
        for(PmsSkuInfo pmsSkuInfo1:pmsSkuInfoList){
            String key = "";
            String value = pmsSkuInfo1.getId().toString();

            List<PmsSkuSaleAttrValue> pmsSkuSaleAttrValueList = pmsSkuInfo1.getSkuSaleAttrValueList();
            for(PmsSkuSaleAttrValue pmsSkuSaleAttrValue:pmsSkuSaleAttrValueList){
                key+=pmsSkuSaleAttrValue.getSaleAttrValueId().toString()+"|";
            }
            skuSaleAttrHash.put(key,value);
        }

        //将sku销售属性hash表放在页面上
        String skuSaleHashJsonStr = JSON.toJSONString(skuSaleAttrHash);
        modelMap.put("skuSaleAttrHashJsonStr",skuSaleHashJsonStr);
        return "item";
    }

//    @RequestMapping("/index")
//    public String index(ModelMap modelMap){
//        List<String> stringList = new ArrayList<>();
//        for(int i=0;i<5;i++){
//            stringList.add("boy next "+i);
//        }
//        modelMap.put("stringList",stringList);
//        modelMap.put("hello","duzeng");
//        modelMap.put("check",1);
//        return "index";
//    }
//
//    @RequestMapping("/{skuId}.html")
//    public String item(@PathVariable String skuId){
//        return "item";
//    }
}
