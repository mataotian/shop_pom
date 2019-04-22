package com.qf.service;

import com.qf.entity.Address;

import java.util.List;

public interface IAddressService {

    int insert(Address address);

    List<Address> queryAddList();

    List<Address> queryByUid(int uid);

    Address queryById(int id);
}
