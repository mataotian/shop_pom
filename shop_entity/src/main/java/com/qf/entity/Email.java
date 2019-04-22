package com.qf.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Email implements Serializable {

    private String title;
    //发送方
    private String from;
    //接收方
    private String to;
    private String content;
    private Date createtime;
    private File file;

}
