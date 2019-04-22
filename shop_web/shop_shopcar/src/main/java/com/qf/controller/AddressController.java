package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.Address;
import com.qf.entity.User;
import com.qf.service.IAddressService;
import com.qf.shop_commons.IsLogin;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/address")
public class AddressController {

    @Reference
    private IAddressService addressService;
    @IsLogin(mustLogin = true)
    @RequestMapping("/add")
    @ResponseBody
    public int add(Address address, User user){
        address.setUid(user.getId());
        int result = addressService.insert(address);
        return result;
    }
}
