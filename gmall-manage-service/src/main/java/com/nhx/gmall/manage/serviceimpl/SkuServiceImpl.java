package com.nhx.gmall.manage.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.nhx.gmall.bean.PmsSkuAttrValue;
import com.nhx.gmall.bean.PmsSkuImage;
import com.nhx.gmall.bean.PmsSkuInfo;
import com.nhx.gmall.bean.PmsSkuSaleAttrValue;
import com.nhx.gmall.manage.mapper.PmsSkuAttrValueMapper;
import com.nhx.gmall.manage.mapper.PmsSkuImageMapper;
import com.nhx.gmall.manage.mapper.PmsSkuInfoMapper;
import com.nhx.gmall.manage.mapper.PmsSkuSaleAttrValueMapper;
import com.nhx.gmall.service.SkuService;
import com.nhx.gmall.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.UUID;

@Service
public class SkuServiceImpl implements SkuService {
    @Autowired
    PmsSkuInfoMapper pmsSkuInfoMapper;
    @Autowired
    PmsSkuAttrValueMapper pmsSkuAttrValueMapper;
    @Autowired
    PmsSkuImageMapper pmsSkuImageMapper;
    @Autowired
    PmsSkuSaleAttrValueMapper pmsSkuSaleAttrValueMapper;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public String saveSkuInfo(PmsSkuInfo pmsSkuInfo) {
        pmsSkuInfoMapper.insertSelective(pmsSkuInfo);

        Long skuId = pmsSkuInfo.getId();
        List<PmsSkuAttrValue> pmsSkuAttrValueList = pmsSkuInfo.getSkuAttrValueList();
        for(PmsSkuAttrValue pmsSkuAttrValue:pmsSkuAttrValueList){
            pmsSkuAttrValue.setSkuId(skuId);
            pmsSkuAttrValueMapper.insertSelective(pmsSkuAttrValue);
        }

        List<PmsSkuSaleAttrValue> pmsSkuSaleAttrValueList = pmsSkuInfo.getSkuSaleAttrValueList();
        for(PmsSkuSaleAttrValue pmsSkuSaleAttrValue:pmsSkuSaleAttrValueList){
            pmsSkuSaleAttrValue.setSkuId(skuId);
            pmsSkuSaleAttrValueMapper.insertSelective(pmsSkuSaleAttrValue);
        }

        List<PmsSkuImage> pmsSkuImageList = pmsSkuInfo.getSkuImageList();
        for(PmsSkuImage pmsSkuImage:pmsSkuImageList){
            pmsSkuImage.setSkuId(skuId);
            pmsSkuImageMapper.insertSelective(pmsSkuImage);
        }
        return "success";
    }

    //从数据库中查询
    public PmsSkuInfo getSkuByIdFromDB(String skuId){
        //sku商品对象
        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
        pmsSkuInfo.setId(Long.valueOf(skuId));
        PmsSkuInfo pmsSkuInfores = pmsSkuInfoMapper.selectOne(pmsSkuInfo);
        //sku图片集合
        PmsSkuImage pmsSkuImage = new PmsSkuImage();
        pmsSkuImage.setSkuId(Long.valueOf(skuId));
        List<PmsSkuImage> pmsSkuImageList = pmsSkuImageMapper.select(pmsSkuImage);
        pmsSkuInfores.setSkuImageList(pmsSkuImageList);
        return pmsSkuInfores;
    }

    @Override
    public PmsSkuInfo getSkuById(String skuId) {
        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();

        //链接缓存
        Jedis jedis = redisUtil.getJedis();

        //查询缓存
        String skuKey = "sku:"+skuId+":info";
        String skuJson = jedis.get(skuKey);

        if(StringUtils.isNotBlank(skuJson)){
            pmsSkuInfo = JSON.parseObject(skuJson, PmsSkuInfo.class);
        }
        else{
            //2. 先设置分布式锁,防止缓存击穿
            String token = UUID.randomUUID().toString();
            ///方法一
            ///方法二，用redisson进行分布式锁
            String res = jedis.set("sku:"+skuId+":lock",token,"nx","px",10000);
            if(StringUtils.isNotBlank(res)&&res.equals("OK")){
                //设置成功，在过期时间内可以访问sql
                //如果缓存中没有，调用查询DB的方法
                pmsSkuInfo = getSkuByIdFromDB(skuId);
                //mysql查询结果同步至缓存
                if(pmsSkuInfo!=null){
                    //这个策略可以自己定义
                    jedis.set("sku:"+skuId+":info",JSON.toJSONString(pmsSkuInfo));
                }
                else {
                    //1. 为了防止缓存穿透，null或者空字符串设置给redis
                    //如果数据库中也没有该字段
                    //防止不断进行 空值 访问的请求 给缓存和数据库造成压力（缓存穿透）
                    jedis.setex("sku:"+skuId+":info",60*3,JSON.toJSONString(""));
                }
                //释放分布式锁
                String lockToken = jedis.get("sku:"+skuId+":lock");
                //String script = "if redis.call('get',KEYS[1])==ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
                //防止当前本线程的锁过期，导致删到别人的锁，所以进行校验
                if(StringUtils.isNotBlank(lockToken)&&(lockToken.equals(token))) {
                    //jedis.eval("lua");//执行lua脚本。。读取value的同时删除key
                    jedis.del("sku:" + skuId + ":lock");
                }
            }else{
                //数据被设置锁，设置失败,自旋（在该线程休眠几秒后，重新访问该方法）
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return getSkuById(skuId);//同一线程下继续访问该方法
                //getSkuById(skuId);//放在这里会变成新的线程，成为孤儿线程
            }

        }

        jedis.close();
        return pmsSkuInfo;
    }

    @Override
    public List<PmsSkuInfo> getSpuSaleAttrListCheckBySku(Long productId) {
        List<PmsSkuInfo> pmsSkuInfoList = pmsSkuInfoMapper.selectSpuSaleAttrListCheckBySku(productId);
        return pmsSkuInfoList;
    }

    @Override
    public List<PmsSkuInfo> getAllSku(String catalog3Id) {
        List<PmsSkuInfo> pmsSkuInfoList = pmsSkuInfoMapper.selectAll();
        for(PmsSkuInfo pmsSkuInfo:pmsSkuInfoList){
            Long skuId =  pmsSkuInfo.getId();

            PmsSkuAttrValue pmsSkuAttrValue = new PmsSkuAttrValue();
            pmsSkuAttrValue.setSkuId(skuId);
            List<PmsSkuAttrValue> select = pmsSkuAttrValueMapper.select(pmsSkuAttrValue);
            pmsSkuInfo.setSkuAttrValueList(select);
        }
        return pmsSkuInfoList;
    }
}
