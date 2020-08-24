package com.nhx.gmall.search;

import com.alibaba.dubbo.config.annotation.Reference;
import com.nhx.gmall.bean.PmsSearchSkuInfo;
import com.nhx.gmall.bean.PmsSkuInfo;
import com.nhx.gmall.service.SkuService;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class GmallSearchServiceApplicationTests {
    @Reference
    SkuService skuService;

    @Test
    public void contextLoads() throws IOException {
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(new HttpClientConfig
                .Builder("http://192.168.50.70:9210")
                .multiThreaded(true)
                .build()
        );
        JestClient jestClient = factory.getObject();

        List<PmsSearchSkuInfo> pmsSearchSkuInfoList = new ArrayList<>();

        //jest的dsl工具
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //bool
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        //filter
        TermQueryBuilder termQueryBuilder = new TermQueryBuilder("","");
        boolQueryBuilder.filter();
        //must
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("","");
        boolQueryBuilder.must();
        //query
        searchSourceBuilder.query(boolQueryBuilder);

        //from
        searchSourceBuilder.from();
        //size
        searchSourceBuilder.size();
        //hightlight
        searchSourceBuilder.highlighter();

        String searchString = null;

        Search search = new Search.Builder(searchString).addIndex("gmallpms").addType("PmsSkuInfo").build();
        SearchResult searchResult = jestClient.execute(search);
        List<SearchResult.Hit<PmsSearchSkuInfo,Void>> list =  searchResult.getHits(PmsSearchSkuInfo.class);
        for(SearchResult.Hit<PmsSearchSkuInfo,Void> hit:list){
            PmsSearchSkuInfo source = hit.source;
            pmsSearchSkuInfoList.add(source);
        }
    }

    @Test
    public void put() throws Exception{
        //jest自动装配失败。。手动创建
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(new HttpClientConfig
                .Builder("http://192.168.50.70:9210")
                .multiThreaded(true)
                .build()
        );
        JestClient jestClient = factory.getObject();
        //查询mysql数据
        List<PmsSkuInfo> pmsSkuInfoList = new ArrayList<>();
        pmsSkuInfoList = skuService.getAllSku("61");

        List<PmsSearchSkuInfo> pmsSearchSkuInfoList = new ArrayList<>();
        for(PmsSkuInfo pmsSkuInfo:pmsSkuInfoList){
            PmsSearchSkuInfo pmsSearchSkuInfo = new PmsSearchSkuInfo();
            BeanUtils.copyProperties(pmsSkuInfo,pmsSearchSkuInfo);
            pmsSearchSkuInfoList.add(pmsSearchSkuInfo);
        }
        //导入es数据库
        for(PmsSearchSkuInfo pmsSearchSkuInfo:pmsSearchSkuInfoList) {
            Index put = new Index.Builder(pmsSearchSkuInfo).index("gmallpms").type("PmsSkuInfo").id(pmsSearchSkuInfo.getId().toString()).build();

            jestClient.execute(put);
        }
    }

}
