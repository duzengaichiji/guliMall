package com.nhx.gmall.manage.mapper;

import com.nhx.gmall.bean.PmsProductSaleAttr;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface PmsProductSaleAttrMapper extends Mapper<PmsProductSaleAttr> {
    List<PmsProductSaleAttr> selectspuScaleAttrListCheckBySku(@Param("productId") Long productId, @Param("skuId") String skuId);
}
