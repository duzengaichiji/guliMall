package com.nhx.gmall.user.controller;
import com.alibaba.dubbo.config.annotation.Reference;
import com.nhx.gmall.bean.UmsMember;
import com.nhx.gmall.bean.UmsMemberReceiveAddress;
import com.nhx.gmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class UserController {
    @Reference
    UserService userService;

    @RequestMapping("selectUser")
    @ResponseBody
    public UmsMember selectUser(String memberId){
        UmsMember umsMemberList = userService.selectUser(memberId);
        return umsMemberList;
    }

    @RequestMapping("updateUser")
    @ResponseBody
    public String updateUser(String memberId){
        userService.updateUser(memberId,new UmsMember());
        return "update";
    }

    @RequestMapping("deleteUser")
    @ResponseBody
    public String deleteUser(String memberId){
        userService.deleteUser(memberId);
        return "delete";
    }

    @RequestMapping("addUser")
    @ResponseBody
    public String addUser(){
        userService.addUser(new UmsMember());
        return "add";
    }

//  tests
    @RequestMapping("getAllUser")
    @ResponseBody
    public List<UmsMember> getAllUser(String memberId){
        List<UmsMember> userMember = userService.getAllUser();
        return userMember;
    }

    @RequestMapping("getReceiveAddresByMemberId")
    @ResponseBody
    public List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String memberId){
        List<UmsMemberReceiveAddress> userMemReceiveAddressList= userService.getReceiveAddressByMemberId(memberId);
        return userMemReceiveAddressList;
    }
}
