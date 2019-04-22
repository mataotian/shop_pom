package com.qf.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.dao.AddressMapper;
import com.qf.entity.Address;
import com.qf.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Service
public class AddressServiceImpl implements IAddressService {
    @Autowired
    private AddressMapper addressMapper;
    @Override
    public int insert(Address address) {
        return addressMapper.insertAddress(address);
    }

    @Override
    public List<Address> queryAddList() {
        return addressMapper.selectList(null);
    }

    @Override
    public List<Address> queryByUid(int uid) {
        QueryWrapper<Address> wrapper=new QueryWrapper<>();
        wrapper.eq("uid",uid);
        List<Address> addresses = addressMapper.selectList(wrapper);
        return addresses;
    }

    @Override
    public Address queryById(int id) {
        return addressMapper.selectById(id);
    }
}
