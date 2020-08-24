package com.nhx.gmall.manage.mapper;

import com.nhx.gmall.bean.PmsSkuInfo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface PmsSkuInfoMapper extends Mapper<PmsSkuInfo> {
    List<PmsSkuInfo> selectSpuSaleAttrListCheckBySku(Long productId);
}
