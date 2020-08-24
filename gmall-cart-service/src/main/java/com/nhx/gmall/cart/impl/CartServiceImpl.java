package com.nhx.gmall.cart.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.nhx.gmall.bean.OmsCartItem;
import com.nhx.gmall.cart.mapper.OmsCartItemMapper;
import com.nhx.gmall.service.CartService;
import com.nhx.gmall.util.RedisUtil;
import jodd.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    OmsCartItemMapper omsCartItemMapper;

    @Override
    public OmsCartItem ifCartExistByUser(String memberId, String skuId) {
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(Long.valueOf(memberId));
        omsCartItem.setProductSkuId(Long.valueOf(skuId));
        OmsCartItem omsCartItem1 = omsCartItemMapper.selectOne(omsCartItem);
        return omsCartItem1;
    }

    @Override
    public void addCart(OmsCartItem omsCartItem) {
        if(StringUtils.isNoneBlank(omsCartItem.getMemberId().toString())){
            omsCartItemMapper.insertSelective(omsCartItem);
        }
    }

    @Override
    public void updateCart(OmsCartItem omsCartItemFromDB) {
        Example s = new Example(OmsCartItem.class);
        s.createCriteria().andEqualTo("id",omsCartItemFromDB.getId());
        omsCartItemMapper.updateByExampleSelective(omsCartItemFromDB,s);
    }

    @Override
    public void flushCartCache(String memberId) {
        OmsCartItem omsCartItem1 = new OmsCartItem();
        omsCartItem1.setMemberId(Long.valueOf(memberId));
        List<OmsCartItem> omsCartItems = omsCartItemMapper.select(omsCartItem1);
        //更新缓存数据
        Jedis jedis = redisUtil.getJedis();
        Map<String,String> map = new HashMap<>();
        for(OmsCartItem omsCartItem:omsCartItems) {
            map.put(omsCartItem.getProductSkuId().toString(),JSON.toJSONString(omsCartItem));
        }
        jedis.hmset("user:"+memberId+":cart",map);
        jedis.close();
    }

    @Override
    public List<OmsCartItem> cartList(String userId) {
        //查sql，redis缓存
        Jedis jedis = null;
        List<OmsCartItem> omsCartItems = new ArrayList<>();
        try{
            jedis = redisUtil.getJedis();

            List<String> hvals = jedis.hvals("user:"+userId+":cart");
            for(String hval:hvals){
                OmsCartItem omsCartItem = JSON.parseObject(hval,OmsCartItem.class);
                omsCartItems.add(omsCartItem);
            }
        }catch (Exception e){
            e.printStackTrace();
            //记录异常日志
            return null;
        }finally {
            if(jedis!=null) {
                jedis.close();
            }
        }
        return omsCartItems;
    }
}
