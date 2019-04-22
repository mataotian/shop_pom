package com.qf.service;

import com.qf.entity.ShopCar;
import com.qf.entity.User;

import java.util.List;

public interface ICarService {
    void addCar(ShopCar shopCar, String carToken, User user);
    List<ShopCar> queryCarList(String carToken,User user);
    int mergeCar(String carToken,User user);
    int deleteCarsByUid(int uid);
}
