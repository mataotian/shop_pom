package com.qf.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "orders")
public class Order implements Serializable {
    @TableId(type = IdType.AUTO)
    private int id;
    private String orderid;
    private String person;
    private String address;
    private String phone;
    private String code;
    private BigDecimal allprice;
    private int uid;
    private int status;
    private Date createtime=new Date();
    @TableField(exist = false)
    private OrderDetails orderDetails;

}
