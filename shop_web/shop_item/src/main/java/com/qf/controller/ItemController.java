package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.Goods;
import com.qf.service.IGoodsService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/item")
public class ItemController {

    @Autowired
    private Configuration configuration;
    @Reference
    private IGoodsService goodsService;
    @RequestMapping("/createHtml")
    public String createHtml(int gid, HttpServletRequest request){
        Goods goods = goodsService.queryById(gid);
        String[] images=goods.getGimage().split("\\|");
        String path=this.getClass().getResource("/static/page/").getPath()+goods.getId()+".html";
        try {
            FileWriter fileWriter = new FileWriter(path);
            Template template = configuration.getTemplate("goodsitem.ftl");
            Map<String,Object> map=new HashMap<>();
            map.put("goods",goods);
            map.put("images",images);
            map.put("context",request.getContextPath());
            template.process(map,new FileWriter(path));
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }



        return "";
    }
}
