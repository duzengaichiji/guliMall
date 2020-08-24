package com.nhx.gmall.service;

import com.nhx.gmall.bean.PmsSearchParam;
import com.nhx.gmall.bean.PmsSearchSkuInfo;

import java.util.List;

public interface SearchService {
    List<PmsSearchSkuInfo> list(PmsSearchParam pmsSearchParam);
}
