package com.qf.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Goods  implements Serializable {
    //一定要记得序列化 不然就是bug
    @TableId(type=IdType.AUTO)
    private int id;
    private String gname;
    private BigDecimal gprice;
    private int gsave;
    private String ginfo;
    private String gimage;
    private int status;
    private Date createtime=new Date();
    private int tid;
}
