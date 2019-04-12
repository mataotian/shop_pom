package com.qf.serviceimpl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.qf.dao.GoodsMapper;
import com.qf.entity.Goods;
import com.qf.service.IGoodsService;
import com.qf.service.ISearchService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Service
public class GoodsSeviceImpl implements IGoodsService {
    @Autowired
    private GoodsMapper goodsMapper;
    @Reference
    private ISearchService searchService;
    @Override
    public List<Goods> queryAll() {
        return goodsMapper.selectList(null);
    }

    @Override
    public int insert(Goods goods) {
        int result = goodsMapper.insert(goods);
        searchService.insert(goods);
        return result;
    }

    @Override
    public Goods queryById(int gid) {
        return goodsMapper.selectById(gid);
    }
}
