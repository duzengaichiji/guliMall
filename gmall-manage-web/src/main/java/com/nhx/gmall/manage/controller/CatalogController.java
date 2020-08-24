package com.nhx.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.nhx.gmall.bean.PmsBaseCatalog1;
import com.nhx.gmall.bean.PmsBaseCatalog2;
import com.nhx.gmall.bean.PmsBaseCatalog3;
import com.nhx.gmall.service.CatalogService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

//前后端分离要加上允许跨域注解,否则前后端不同地址，不同端口，会导致请求被拦截
//可以F12在浏览器中观察请求头，相应头的变化
@Controller
@CrossOrigin
public class CatalogController {
    @Reference
    CatalogService catalogService;

    @RequestMapping("getCatalog1")
    @ResponseBody
    public List<PmsBaseCatalog1> getCatalog1(){
        return catalogService.getCatalog1();
    }

    @RequestMapping("getCatalog2")
    @ResponseBody
    public List<PmsBaseCatalog2> getCatalog2(String catalog1Id){
        return catalogService.getCatalog2(catalog1Id);
    }

    @RequestMapping("getCatalog3")
    @ResponseBody
    public List<PmsBaseCatalog3> getCatalog3(String catalog2Id){
        return catalogService.getCatalog3(catalog2Id);
    }
}
