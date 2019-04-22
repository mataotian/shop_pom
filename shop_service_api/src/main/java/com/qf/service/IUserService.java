package com.qf.service;

import com.qf.entity.User;

public interface IUserService {
    int insert(User user);
    User toLogin(String username,String password);
    void jihuoUser(String username);
}
