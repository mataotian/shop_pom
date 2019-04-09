package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.Goods;
import com.qf.service.IGoodsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Reference
    private IGoodsService goodsService;
    @RequestMapping("/list")
    public String getList(Model model){
        List<Goods> goods = goodsService.queryAll();
        model.addAttribute("goods",goods);
        return "goodslist";
    }
}
