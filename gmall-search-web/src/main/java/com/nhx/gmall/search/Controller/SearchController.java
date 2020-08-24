package com.nhx.gmall.search.Controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.nhx.gmall.bean.*;
import com.nhx.gmall.service.AttrService;
import com.nhx.gmall.service.SearchService;
import com.nhx.gmall.service.SpuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
public class SearchController {
    @Reference
    SearchService searchService;

    @Reference
    AttrService attrService;

    @RequestMapping("list.html")
    public String list(PmsSearchParam pmsSearchParam, ModelMap modelMap){

        //调用搜索服务
        List<PmsSearchSkuInfo> pmsSearchSkuInfoList = searchService.list(pmsSearchParam);
        modelMap.put("skuLsInfoList",pmsSearchSkuInfoList);

        //去重
        Set<Long> valueIdSet = new HashSet<>();
        for(PmsSearchSkuInfo pmsSearchSkuInfo:pmsSearchSkuInfoList){
            List<PmsSkuAttrValue> pmsSkuAttrValueList = new ArrayList<>();
            pmsSkuAttrValueList = pmsSearchSkuInfo.getSkuAttrValueList();
            for(PmsSkuAttrValue pmsSkuAttrValue:pmsSkuAttrValueList){
                Long valueId = pmsSkuAttrValue.getValueId();
                valueIdSet.add(valueId);
            }
        }
        //根据属性id,查询出属性列表
        List<PmsBaseAttrInfo> pmsBaseAttrInfoList = new ArrayList<>();
        if(valueIdSet!=null) {
            pmsBaseAttrInfoList = attrService.getAttrValueListByValueId(valueIdSet);
        }
        modelMap.put("attrList",pmsBaseAttrInfoList);

        //面包屑功能
        //对平台属性集合进一步处理，去掉当前筛选条件所在的属性组
        Long[] delValueIds = pmsSearchParam.getValueId();
        if(delValueIds!=null) {
            Iterator<PmsBaseAttrInfo> iterator = pmsBaseAttrInfoList.iterator();

            while (iterator.hasNext()) {
                PmsBaseAttrInfo pmsBaseAttrInfo = iterator.next();
                List<PmsBaseAttrValue> pmsBaseAttrValueList = pmsBaseAttrInfo.getAttrValueList();
                for (PmsBaseAttrValue pmsBaseAttrValue : pmsBaseAttrValueList) {
                    Long valueId = pmsBaseAttrValue.getId();
                    for (Long delValueId : delValueIds) {
                        if (delValueId.equals(valueId)) {
                            //删除已经被筛选的属性所在的组
                            iterator.remove();
                        }
                    }
                }
            }
        }

        //step 1.拆分urlParam
        String urlParam = getUrlParam(pmsSearchParam);
        modelMap.put("urlParam",urlParam);

        return "list";
    }

    public String getUrlParam(PmsSearchParam pmsSearchParam){
        String keyword = pmsSearchParam.getKeyword();
        Long catalog3Id = pmsSearchParam.getCatalog3Id();
        Long[] valueIds = pmsSearchParam.getValueId();

        String urlParam = "";
        if(keyword!=null&&StringUtils.isNotBlank(keyword)){
            if (StringUtils.isNotBlank(urlParam)) {
                urlParam = urlParam + "keyword=" + keyword;
            }
            else{
                urlParam = urlParam + "&keyword=" + keyword;
            }
        }

        if(catalog3Id!=null&&StringUtils.isNotBlank(catalog3Id.toString())){
            if (StringUtils.isNotBlank(urlParam)) {
                urlParam = urlParam + "catalog3Id=" + catalog3Id;
            }
            else{
                urlParam = urlParam + "&catalog3Id=" + catalog3Id;
            }
        }
        if(valueIds!=null) {
            for (Long valueId : valueIds) {
                if (StringUtils.isNotBlank(urlParam)) {
                    urlParam = urlParam + "valueId=" + keyword;
                } else {
                    urlParam = urlParam + "&valueId=" + valueId;
                }
            }
        }
        return urlParam;
    }

    @RequestMapping("index")
    public String index(){
        return "index";
    }
}
