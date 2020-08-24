package com.nhx.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.nhx.gmall.bean.PmsSkuInfo;
import com.nhx.gmall.service.SkuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@CrossOrigin
public class SkuController {
    @Reference
    SkuService skuService;

    @RequestMapping("saveSkuInfo")
    @ResponseBody
    public String saveSkuInfo(@RequestBody PmsSkuInfo pmsSkuInfo){
        //将spuId封装到productId，这是由于bean属性名和前端不对应
        pmsSkuInfo.setProductId(Long.valueOf(pmsSkuInfo.getSpuId()));
        return skuService.saveSkuInfo(pmsSkuInfo);
    }
}
