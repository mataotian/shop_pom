package com.qf.listen;

import com.qf.entity.Goods;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class Listen {
    @Autowired
    private SolrClient solrClient;
    @RabbitListener(queues = "goods1_queue")
    public void handleMsg(Goods goods){

        SolrInputDocument document = new SolrInputDocument();
        document.setField("id", goods.getId());
        document.setField("gname", goods.getGname());
        document.setField("ginfo", goods.getGinfo());
        document.setField("gprice", goods.getGprice().doubleValue());
        document.setField("gsave", goods.getGsave());
        document.setField("gimage", goods.getGimage());

        try {
            solrClient.add(document);
            solrClient.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
