package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.qf.entity.ShopCar;
import com.qf.entity.User;
import com.qf.service.ICarService;
import com.qf.shop_commons.IsLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/car")
public class CarController {

    @Reference
    private ICarService carService;
    @Autowired
    private RedisTemplate redisTemplate;
    @RequestMapping("/add")
    @IsLogin(mustLogin = false)
    public String addCar(@CookieValue(value = "login_token",required = false)String carToken,
                         ShopCar shopCar,
                         User user,
                         HttpServletResponse response){

        System.out.println("购物车Token"+carToken);
        if(carToken==null){
            carToken= UUID.randomUUID().toString();
            Cookie cookie=new Cookie("car_token",carToken);
            cookie.setMaxAge(60*60*24*10);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        System.out.println("添加购物车成功!!!!");
        carService.addCar(shopCar,carToken,user);

        return "addok";
    }
    @RequestMapping("/list")
    @IsLogin
    @ResponseBody
    public String queryCars(@CookieValue(value = "car_token",required = false)String carToken,
                            User user){
        List<ShopCar> cars = carService.queryCarList(carToken, user);
        //System.out.println(cars);
        return "showcars("+ JSON.toJSONString(cars)+")";
    }
    @RequestMapping("/carlist")
    @IsLogin
    public String queryCarList(@CookieValue(name = "car_token",required = false)String carToken, User user, Model model){
        List<ShopCar> cars = carService.queryCarList(carToken, user);
        model.addAttribute("cars",cars);
        //System.out.println("购物车:"+cars);
        BigDecimal priceall = BigDecimal.valueOf(0.0);
        for (ShopCar car : cars) {
            priceall = priceall.add(car.getAllprice());
        }
        //System.out.println("购物车总价"+priceall);
        model.addAttribute("priceall", priceall.doubleValue());

        return "carlist";
    }
}
