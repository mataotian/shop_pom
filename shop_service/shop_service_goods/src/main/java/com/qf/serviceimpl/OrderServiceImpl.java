package com.qf.serviceimpl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.dao.OrderMapper;
import com.qf.dao.OrderDetailsMapper;
import com.qf.entity.*;
import com.qf.service.IAddressService;
import com.qf.service.ICarService;
import com.qf.service.IOrderService;
import com.qf.shop_commons.IsLogin;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailsMapper orderDetailsMapper;
    @Reference
    private IAddressService addressService;
    @Reference
    private ICarService carService;


    @Override
    public String insert(int aid, User user) {
        Address address = addressService.queryById(aid);
        System.out.println(address);
        List<ShopCar> cars = carService.queryCarList(null, user);
        System.out.println(cars);
        BigDecimal priceall = BigDecimal.valueOf(0.0);
        for (ShopCar car : cars) {
            priceall = priceall.add(car.getAllprice());
        }
        String orderid= UUID.randomUUID().toString();
        Order order=new Order(
                0,
                orderid,
                address.getPerson(),
                address.getAddress(),
                address.getPhone(),
                address.getCode(),
                priceall,
                user.getId(),
                0,
                new Date(),
                null
        );
        System.out.println("添加订单成功!!!!");
        orderMapper.insert(order);
        for (ShopCar car : cars) {
            OrderDetails orderDetails=new OrderDetails();
            orderDetails.setGid(car.getGid());
            orderDetails.setGimage(car.getGoods().getGimage());
            orderDetails.setGname(car.getGoods().getGname());
            orderDetails.setGnumber(car.getGnumber());
            orderDetails.setGprice(car.getGoods().getGprice());
            orderDetails.setOid(order.getId());

            orderDetailsMapper.insert(orderDetails);
        }
        System.out.println("添加订单详情成功!!!!");

        carService.deleteCarsByUid(user.getId());
        return orderid;
    }

    @Override
    public Order queryById(int id) {
        return null;
    }

    @Override
    public Order queryByOrderId(String orderid) {
        //System.out.println(orderid);
        QueryWrapper<Order> wrapper=new QueryWrapper<>();
        wrapper.eq("orderid",orderid);
        Order order = orderMapper.selectOne(wrapper);
        //System.out.println(order);
        return order;
    }

    @Override
    public List<Order> queryOrderList(int uid) {

        return orderMapper.queryOrderByUid(uid);
    }

    @Override
    public int updateOrder(Order order) {
        return orderMapper.updateById(order);
    }
}
