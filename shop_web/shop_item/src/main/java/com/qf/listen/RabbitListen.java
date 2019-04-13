package com.qf.listen;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.Goods;
import com.qf.service.IGoodsService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

@Component
public class RabbitListen {
    @Autowired
    private Configuration configuration;
    @Reference
    private IGoodsService goodsService;
    @RabbitListener(queues = "goods2_queue")
    public void exchange(Goods goods){


        String[] images=goods.getGimage().split("\\|");

        try {

            Template template = configuration.getTemplate("goodsitem.ftl");
            Map<String,Object> map=new HashMap<>();
            map.put("goods",goods);

            map.put("images",images);
            String path=this.getClass().getResource("/static/page/").getPath()+goods.getId()+".html";
            template.process(map,new FileWriter(path));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
