package com.qf.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "order_details")
public class OrderDetails implements Serializable {

    private int id;
    private int gid;
    private String gname;
    private String gimage;
    private BigDecimal gprice;
    private int gnumber;
    private int oid;
}
