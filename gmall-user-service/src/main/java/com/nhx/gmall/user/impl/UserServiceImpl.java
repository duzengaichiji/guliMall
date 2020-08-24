package com.nhx.gmall.user.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.nhx.gmall.bean.UmsMember;
import com.nhx.gmall.bean.UmsMemberReceiveAddress;
import com.nhx.gmall.service.UserService;
import com.nhx.gmall.user.mapper.UmsMemberReceiveAddressMapper;
import com.nhx.gmall.user.mapper.UmsUserMpper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UmsUserMpper userMpper;
    @Autowired
    UmsMemberReceiveAddressMapper umsMemberReceiveAddressMapper;


    @Override
    public UmsMember selectUser(String memberId) {
        UmsMember umsMember = userMpper.selectByPrimaryKey(memberId);
        return umsMember;
    }

    @Override
    public String updateUser(String memberId,UmsMember umsMember) {
        umsMember.setId(memberId);
        userMpper.updateByPrimaryKey(umsMember);
        return null;
    }

    @Override
    public String deleteUser(String memberId) {
        userMpper.deleteByPrimaryKey(memberId);
        return null;
    }

    @Override
    public String addUser(UmsMember umsMember) {
        userMpper.insert(umsMember);
        return null;
    }

    //    for test
    @Override
    public List<UmsMember> getAllUser() {
        //List<UmsMember> userMemberList = userMpper.selectAllUser();
        List<UmsMember> userMemberList = userMpper.selectAll();
        return userMemberList;
    }

    @Override
    public List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String memberId) {
        //根据外键查询
        UmsMemberReceiveAddress umsMemberReceiveAddress = new UmsMemberReceiveAddress();
        umsMemberReceiveAddress.setMemberId(memberId);
        List<UmsMemberReceiveAddress> userMemberReceiveAddressList = umsMemberReceiveAddressMapper.select(umsMemberReceiveAddress);

        //用example查询，只有生成的sql语句上由一定区别
//        Example example = new Example(UmsMemberReceiveAddress.class);
//        example.createCriteria().andEqualTo("memberId",memberId);
//        umsMemberReceiveAddressMapper.selectByExample(example);
        return userMemberReceiveAddressList;
    }
}
