package com.nhx.gmall.user.mapper;

import com.nhx.gmall.bean.UmsMember;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

//该类单表的增删改查都由通用mapper类生成
public interface UmsUserMpper extends Mapper<UmsMember> {
    List<UmsMember> selectAllUser();
}
