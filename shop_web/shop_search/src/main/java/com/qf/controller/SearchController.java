package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.Goods;
import com.qf.service.ISearchService;
import org.apache.solr.client.solrj.SolrClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/search")
public class SearchController {
    @Reference
    private ISearchService searchService;
    @Autowired
    private SolrClient solrClient;
    @RequestMapping("/searchByKeyword")
    public String searchByKeyword(String keyword, Model model){
        System.out.println("搜索的关键字为:"+keyword);
        List<Goods> goods = searchService.searchBySolr(keyword);
        System.out.println(goods);
        model.addAttribute("goods",goods);
        return "searchlist";
    }

}
