package com.qf.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.dao.CarMapper;
import com.qf.dao.GoodsMapper;
import com.qf.entity.Goods;
import com.qf.entity.ShopCar;
import com.qf.entity.User;
import com.qf.service.ICarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.util.List;
@Service
public class CarServiceImpl implements ICarService {

    @Autowired
    private CarMapper carMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public void addCar(ShopCar shopCar, String carToken, User user) {

        Goods goods = goodsMapper.selectById(shopCar.getGid());
        BigDecimal number = BigDecimal.valueOf(shopCar.getGnumber());
        shopCar.setAllprice(goods.getGprice().multiply(number));

        if(user!=null){
            shopCar.setUid(user.getId());
            carMapper.insert(shopCar);
        }else if(carToken!=null){
            redisTemplate.opsForList().leftPush(carToken,shopCar);
        }
    }

    @Override
    public List<ShopCar> queryCarList(String carToken, User user) {
        List<ShopCar> cars=null;
        if(user!=null){
            QueryWrapper wrapper=new QueryWrapper();
            wrapper.eq("uid",user.getId());
            cars= carMapper.selectList(wrapper);
            //System.out.println("用户查询:"+cars);
            //return cars;
        }else if(carToken!=null){
            Long size = redisTemplate.opsForList().size(carToken);
            cars= redisTemplate.opsForList().range(carToken, 0, size);
        }

        if(cars!=null){
            for (ShopCar car : cars) {
                Goods goods = goodsMapper.selectById(car.getGid());
                car.setGoods(goods);
            }
        }
        return cars;
    }

    @Override
    public int mergeCar(String carToken, User user) {

        List<ShopCar> cars =null;

        if(carToken!=null){
            Long size = redisTemplate.opsForList().size(carToken);
            cars = redisTemplate.opsForList().range(carToken, 0, size);
        }
        if(cars==null){
            return 1;
        }
        for (ShopCar car : cars) {
            car.setUid(user.getId());
            carMapper.insert(car);
        }
        redisTemplate.delete(carToken);
        return 1;
    }

    @Override
    public int deleteCarsByUid(int uid) {
        QueryWrapper<ShopCar> wrapper=new QueryWrapper<>();
        wrapper.eq("uid",uid);
        carMapper.delete(wrapper);
        return 1;
    }
}
