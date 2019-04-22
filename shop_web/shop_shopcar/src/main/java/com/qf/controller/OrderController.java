package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.Address;
import com.qf.entity.Order;
import com.qf.entity.ShopCar;
import com.qf.entity.User;
import com.qf.service.IAddressService;
import com.qf.service.ICarService;
import com.qf.service.IOrderService;
import com.qf.shop_commons.IsLogin;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Reference
    private ICarService carService;
    @Reference
    private IAddressService addressService;
    @Reference
    private IOrderService orderService;
    @IsLogin
    @RequestMapping("/add")
    public String add(User user, Model model){
        List<ShopCar> cars = carService.queryCarList(null, user);
        //System.out.println("用户咋的了:"+user);
        List<Address> addresses = addressService.queryByUid(user.getId());
        //System.out.println("地址也没有吗"+addresses);
        BigDecimal priceall = BigDecimal.valueOf(0.0);
        for (ShopCar car : cars) {
            priceall = priceall.add(car.getAllprice());
        }
        model.addAttribute("cars",cars);
        model.addAttribute("addresses",addresses);
        model.addAttribute("priceall",priceall);
        return "order";
    }
    @RequestMapping("/orderadd")
    @IsLogin
    @ResponseBody
    public String insert(int aid,User user){
        String orderid = orderService.insert(aid, user);
        return orderid;
    }
    @IsLogin(mustLogin = true)
    @RequestMapping("/orderlist")
    public String queryOrderList(User user,Model model){
        System.out.println("后台还行...");
        List<Order> orders = orderService.queryOrderList(user.getId());
        System.out.println(orders);
        model.addAttribute("orders",orders);
        return "orderdetails";
    }
}
