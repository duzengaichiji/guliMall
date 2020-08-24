package com.nhx.gmall.service;

import com.nhx.gmall.bean.PmsBaseAttrInfo;
import com.nhx.gmall.bean.PmsBaseAttrValue;
import com.nhx.gmall.bean.PmsBaseSaleAttr;

import java.util.List;
import java.util.Set;

public interface AttrService {
    List<PmsBaseAttrInfo> attrInfoList(String catalog3Id);

    String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo);

    List<PmsBaseAttrValue> getAttrValueList(String attrId);

    List<PmsBaseSaleAttr> baseSaleAttrList();

    List<PmsBaseAttrInfo> getAttrValueListByValueId(Set<Long> valueIdSet);
}
