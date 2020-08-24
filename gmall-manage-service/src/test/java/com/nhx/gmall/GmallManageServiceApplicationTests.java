package com.nhx.gmall;

import com.nhx.gmall.manage.mapper.PmsBaseAttrInfoMapper;
import com.nhx.gmall.util.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;

@SpringBootTest
class GmallManageServiceApplicationTests {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    PmsBaseAttrInfoMapper pmsBaseAttrInfoMapper;

    @Test
    void contextLoads() {
//        Jedis jedis = redisUtil.getJedis();
//        System.out.println(jedis);
        System.out.println(pmsBaseAttrInfoMapper.getClass());
    }

}
