package com.qf.service;

import com.qf.entity.Order;
import com.qf.entity.OrderDetails;
import com.qf.entity.User;

import java.util.List;

public interface IOrderService {
    String insert(int aid, User user);
    Order queryById(int id);
    Order queryByOrderId(String orderid);
    List<Order> queryOrderList(int uid);
    int updateOrder(Order order);
}
