package com.qf.service;

import com.qf.entity.Goods;

import java.util.List;

public interface ISearchService {

    List<Goods> searchBySolr(String keyword);
    int insert(Goods goods);
}
