package com.qf.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.dao.UserMapper;
import com.qf.entity.User;
import com.qf.service.IUserService;
import com.qf.shop_commons.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UserSeviceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public int insert(User user) {
        user.setPassword(MD5Utils.md5(user.getPassword()));
        return userMapper.insert(user);
    }

    @Override
    public User toLogin(String username, String password) {
        QueryWrapper<User> wrapper=new QueryWrapper<>();

        wrapper.eq("username",username);
        wrapper.eq("password",MD5Utils.md5(password));
        User user = userMapper.selectOne(wrapper);
        return user;
    }

    @Override
    public void jihuoUser(String username) {
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("username",username);
        User user = userMapper.selectOne(wrapper);
        user.setStatus(1);
        userMapper.updateById(user);
    }
}
