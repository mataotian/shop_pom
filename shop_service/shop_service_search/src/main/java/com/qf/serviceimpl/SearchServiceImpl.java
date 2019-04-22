package com.qf.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.entity.Goods;
import com.qf.service.ISearchService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearchServiceImpl implements ISearchService {
    @Autowired
    private SolrClient solrClient;
    @Override
    public List<Goods> searchBySolr(String keyword) {
        System.out.println("搜索的关键字:"+keyword);
        SolrQuery solrQuery=new SolrQuery();
        if(keyword.equals("")){
            solrQuery.setQuery("*:*");
        }else {
            solrQuery.setQuery("gname:"+keyword+" || ginfo:"+keyword);
        }
        List<Goods> list=new ArrayList<>();
        try {
            QueryResponse result = solrClient.query(solrQuery);
            SolrDocumentList results = result.getResults();
            for (SolrDocument document:results) {
                Goods goods=new Goods();
                goods.setId(Integer.parseInt(document.get("id")+""));
                goods.setGname(document.get("gname")+"");
                goods.setGinfo(document.get("ginfo")+"");
                goods.setGimage(document.get("gimage")+"");
                goods.setGprice(BigDecimal.valueOf(Double.parseDouble(document.get("gprice")+"")));
                goods.setGsave(Integer.parseInt(document.get("gsave")+""));
                list.add(goods);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public int insert(Goods goods){
        SolrInputDocument document=new SolrInputDocument();
        document.setField("id",goods.getId());
        document.setField("gname",goods.getGname());
        document.setField("gimage",goods.getGimage());
        document.setField("ginfo",goods.getGinfo());
        document.setField("gsave",goods.getGsave());
        document.setField("gprice",goods.getGprice().doubleValue());

        try {
            solrClient.add(document);
            solrClient.commit();
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
