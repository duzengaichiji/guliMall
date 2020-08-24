package com.nhx.gmall.service;

import com.nhx.gmall.bean.PmsProductImage;
import com.nhx.gmall.bean.PmsProductInfo;
import com.nhx.gmall.bean.PmsProductSaleAttr;

import java.util.List;

public interface SpuService {
    List<PmsProductInfo> spuList(String catalog3Id);

    void saveSpuInfo(PmsProductInfo pmsProductInfo);

    List<PmsProductSaleAttr> spuSaleAttrList(String spuId);

    List<PmsProductImage> spuImageList(String spuId);

    List<PmsProductSaleAttr> spuScaleAttrListCheckBySku(Long productId,String skuId);
}
