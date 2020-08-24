package com.nhx.gmall.service;

import com.nhx.gmall.bean.UmsMember;
import com.nhx.gmall.bean.UmsMemberReceiveAddress;

import java.util.List;

public interface UserService {

    UmsMember selectUser(String memberId);
    String updateUser(String memberId,UmsMember umsMember);
    String deleteUser(String memberId);
    String addUser(UmsMember umsMember);
    //test
    List<UmsMember> getAllUser();
    List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String memberId);
}
