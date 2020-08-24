package com.nhx.gmall.manage.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.nhx.gmall.bean.PmsBaseCatalog1;
import com.nhx.gmall.bean.PmsBaseCatalog2;
import com.nhx.gmall.bean.PmsBaseCatalog3;
import com.nhx.gmall.manage.mapper.PmsBaseCatalog1Mapper;
import com.nhx.gmall.manage.mapper.PmsBaseCatalog2Mapper;
import com.nhx.gmall.manage.mapper.PmsBaseCatalog3Mapper;
import com.nhx.gmall.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class CatalogServiceImpl implements CatalogService {
    @Autowired
    PmsBaseCatalog1Mapper pmsBaseCatalog1Mapper;
    @Autowired
    PmsBaseCatalog2Mapper pmsBaseCatalog2Mapper;
    @Autowired
    PmsBaseCatalog3Mapper pmsBaseCatalog3Mapper;

    @Override
    public List<PmsBaseCatalog1> getCatalog1() {
        return pmsBaseCatalog1Mapper.selectAll();
    }

    @Override
    public List<PmsBaseCatalog2> getCatalog2(String catalog1id) {
        PmsBaseCatalog2 pmsBaseCatalog2 = new PmsBaseCatalog2();
        pmsBaseCatalog2.setCatalog1Id(Integer.valueOf(catalog1id));
        List<PmsBaseCatalog2> pmsBaseCatalog2List = pmsBaseCatalog2Mapper.select(pmsBaseCatalog2);
        return pmsBaseCatalog2List;
    }

    @Override
    public List<PmsBaseCatalog3> getCatalog3(String catalog2id) {
        PmsBaseCatalog3 pmsBaseCatalog3 = new PmsBaseCatalog3();
        pmsBaseCatalog3.setCatalog2Id(Long.valueOf(catalog2id));
        List<PmsBaseCatalog3> pmsBaseCatalog3List = pmsBaseCatalog3Mapper.select(pmsBaseCatalog3);
        return pmsBaseCatalog3List;
    }
}
