package com.nhx.gmall.service;

import com.nhx.gmall.bean.PmsSkuInfo;
import org.springframework.stereotype.Service;

import java.util.List;

public interface SkuService {

    String saveSkuInfo(PmsSkuInfo pmsSkuInfo);

    PmsSkuInfo getSkuById(String skuId);

    List<PmsSkuInfo> getSpuSaleAttrListCheckBySku(Long productId);

    List<PmsSkuInfo> getAllSku(String catalog3Id);
}
