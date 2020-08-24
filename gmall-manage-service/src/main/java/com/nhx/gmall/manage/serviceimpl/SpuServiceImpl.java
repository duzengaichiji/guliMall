package com.nhx.gmall.manage.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.nhx.gmall.bean.PmsProductImage;
import com.nhx.gmall.bean.PmsProductInfo;
import com.nhx.gmall.bean.PmsProductSaleAttr;
import com.nhx.gmall.bean.PmsProductSaleAttrValue;
import com.nhx.gmall.manage.mapper.PmsProductImageMapper;
import com.nhx.gmall.manage.mapper.PmsProductInfoMapper;
import com.nhx.gmall.manage.mapper.PmsProductSaleAttrMapper;
import com.nhx.gmall.manage.mapper.PmsProductSaleAttrValueMapper;
import com.nhx.gmall.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class SpuServiceImpl implements SpuService {
    @Autowired
    PmsProductInfoMapper pmsProductInfoMapper;
    @Autowired
    PmsProductSaleAttrMapper pmsProductSaleAttrMapper;
    @Autowired
    PmsProductSaleAttrValueMapper pmsProductSaleAttrValueMapper;
    @Autowired
    PmsProductImageMapper pmsProductImageMapper;

    @Override
    public List<PmsProductInfo> spuList(String catalog3Id) {
        PmsProductInfo pmsProductInfo = new PmsProductInfo();
        pmsProductInfo.setCatalog3Id(Long.valueOf(catalog3Id));
        return pmsProductInfoMapper.select(pmsProductInfo);
    }

    @Override
    public void saveSpuInfo(PmsProductInfo pmsProductInfo) {
        pmsProductInfoMapper.insertSelective(pmsProductInfo);
        return;
    }

    @Override
    public List<PmsProductSaleAttr> spuSaleAttrList(String spuId) {
        PmsProductSaleAttr pmsProductSaleAttr = new PmsProductSaleAttr();
        pmsProductSaleAttr.setProductId(Long.valueOf(spuId));
        List<PmsProductSaleAttr> pmsProductSaleAttrValueList = pmsProductSaleAttrMapper.select(pmsProductSaleAttr);
        for(PmsProductSaleAttr pmsProductSaleAttr1:pmsProductSaleAttrValueList){
            PmsProductSaleAttrValue pmsProductSaleAttrValue = new PmsProductSaleAttrValue();
            pmsProductSaleAttrValue.setProductId(Long.valueOf(spuId));
            pmsProductSaleAttrValue.setSaleAttrId(pmsProductSaleAttr1.getSaleAttrId());
            List<PmsProductSaleAttrValue> pmsProductSaleAttrValues = pmsProductSaleAttrValueMapper.select(pmsProductSaleAttrValue);
            pmsProductSaleAttr1.setSpuSaleAttrValueList(pmsProductSaleAttrValues);
        }
        return pmsProductSaleAttrValueList;
    }

    @Override
    public List<PmsProductImage> spuImageList(String spuId) {
        PmsProductImage pmsProductImage = new PmsProductImage();
        pmsProductImage.setProductId(Long.valueOf(spuId));
        List<PmsProductImage> pmsProductImageList = pmsProductImageMapper.select(pmsProductImage);
        return pmsProductImageList;
    }

    @Override
    public List<PmsProductSaleAttr> spuScaleAttrListCheckBySku(Long productId,String skuId) {
//        PmsProductSaleAttr pmsProductSaleAttr = new PmsProductSaleAttr();
//        pmsProductSaleAttr.setProductId(productId);
//        List<PmsProductSaleAttr> pmsProductSaleAttrList = pmsProductSaleAttrMapper.select(pmsProductSaleAttr);
//
//        for(PmsProductSaleAttr pmsProductSaleAttr1:pmsProductSaleAttrList){
//            Long scaleAttrId = pmsProductSaleAttr1.getSaleAttrId();
//
//            PmsProductSaleAttrValue pmsProductSaleAttrValue = new PmsProductSaleAttrValue();
//            pmsProductSaleAttrValue.setSaleAttrId(scaleAttrId);
//            pmsProductSaleAttrValue.setProductId(productId);
//            List<PmsProductSaleAttrValue> pmsProductSaleAttrValueList = pmsProductSaleAttrValueMapper.select(pmsProductSaleAttrValue);
//            pmsProductSaleAttr1.setSpuSaleAttrValueList(pmsProductSaleAttrValueList);
//        }
//        return pmsProductSaleAttrList;

        List<PmsProductSaleAttr> pmsProductSaleAttrList = pmsProductSaleAttrMapper.selectspuScaleAttrListCheckBySku(productId,skuId);
        return pmsProductSaleAttrList;
    }
}
