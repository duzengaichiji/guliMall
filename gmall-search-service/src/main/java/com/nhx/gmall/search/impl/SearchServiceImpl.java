package com.nhx.gmall.search.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.nhx.gmall.bean.PmsSearchParam;
import com.nhx.gmall.bean.PmsSearchSkuInfo;
import com.nhx.gmall.bean.PmsSkuAttrValue;
import com.nhx.gmall.service.SearchService;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {
    @Override
    public List<PmsSearchSkuInfo> list(PmsSearchParam pmsSearchParam) {
        //链接es数据库
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(new HttpClientConfig
                .Builder("http://192.168.50.70:9210")
                .multiThreaded(true)
                .build()
        );
        JestClient jestClient = factory.getObject();
        //提取搜索条件
        Long[] valueList = pmsSearchParam.getValueId();
        String keyword = pmsSearchParam.getKeyword();
        Long catalog3Id = pmsSearchParam.getCatalog3Id();
        //构建搜索dsl
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        //filter
        if (catalog3Id!=null&&StringUtils.isNotBlank(catalog3Id.toString())) {
            TermQueryBuilder termQueryBuilder = new TermQueryBuilder("catalog3Id",catalog3Id);
            boolQueryBuilder.filter(termQueryBuilder);
        }
        if(valueList!=null){
            for(Long valueId:valueList){
                TermQueryBuilder termQueryBuilder = new TermQueryBuilder("skuAttrValueList.valueId",valueId);
                boolQueryBuilder.filter(termQueryBuilder);
            }
        }
        if(StringUtils.isNotBlank(keyword)){
            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("skuName",keyword);
            boolQueryBuilder.must(matchQueryBuilder);
        }
        searchSourceBuilder.query(boolQueryBuilder);
        //from
        searchSourceBuilder.from(0);
        //size
        searchSourceBuilder.size(20);
        Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex("gmallpms").addType("PmsSkuInfo").build();
        SearchResult searchResult = null;
        try {
            searchResult = jestClient.execute(search);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<PmsSearchSkuInfo> pmsSearchSkuInfoList = new ArrayList<>();

        List<SearchResult.Hit<PmsSearchSkuInfo,Void>> list =  searchResult.getHits(PmsSearchSkuInfo.class);
        for(SearchResult.Hit<PmsSearchSkuInfo,Void> hit:list){
            PmsSearchSkuInfo source = hit.source;
            pmsSearchSkuInfoList.add(source);
        }

        return pmsSearchSkuInfoList;
    }
}
