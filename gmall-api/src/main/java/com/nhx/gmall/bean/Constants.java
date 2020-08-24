package com.nhx.gmall.bean;

import tk.mybatis.mapper.util.StringUtil;

import java.math.BigDecimal;
import java.util.List;

//常量类
public class Constants {

    private Constants() {
    }

    //redis：sku+skuid+info
    public static final String sku = "sku:";
    public static final String info = ":info";
    public static final String lock = ":lock";
    //es库索引
    public static final String index = "gmall";
    //es 表名
    public static final String type = "PmsSkuInfo";
    public static final String id = "id";
    public static final String price = "price";
    public static final String skuDesc = "skuDesc";
    public static final String skuName = "skuName";
    public static final String catalog3Id = "catalog3Id";
    public static final String skuDefaultImg = "skuDefaultImg";
    public static final String hotScore = "hotScore";
    public static final String productId = "productId";
    public static final String skuAttrValueList_valueId = "skuAttrValueList.valueId";
    public static final String skuAttrValueList_attrId = "skuAttrValueList.attrId";

    //购物车
    public static final String user = "user:";
    public static final String cart = ":cart";

    public static final String password = "password";
    public static final String token = ":token";
    //统一认证
    public static final String key = "gmall-wzy";
    public static final String memberId = "memberId";
    public static final String nickname = "nickname";

    public static final String ip = "127.0.0.1";

    public static final String tradeCode = ":tradeCode";
}
