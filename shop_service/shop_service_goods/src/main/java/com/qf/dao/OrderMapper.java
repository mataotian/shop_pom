package com.qf.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qf.entity.Order;

import java.util.List;

public interface OrderMapper extends BaseMapper<Order> {
    List<Order> queryOrderByUid(int uid);
}
