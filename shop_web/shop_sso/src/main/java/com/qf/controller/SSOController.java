package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.qf.entity.Email;
import com.qf.entity.User;
import com.qf.service.ICarService;
import com.qf.service.IUserService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/sso")
public class SSOController {
    @Autowired
    private RedisTemplate redisTemplate;
    @Reference
    private IUserService userService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Reference
    private ICarService carService;
    @RequestMapping("/tologin")
    public String tologin(String badUrl, Model model){
        model.addAttribute("badUrl",badUrl);
        return "login";
    }
    @RequestMapping("/toregister")
    public String toregister(){
        return "register";
    }
    @RequestMapping("/register")
    public String register(User user,Model model){
        System.out.println("开始注册....");
        int i = userService.insert(user);
        if(i<=0){
            model.addAttribute("error","0");
            return "register";
        }
        Email email=new Email();
        email.setTitle("英雄联盟官网");
        email.setTo(user.getEmail());

        String uuid = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set("email_token_"+user.getUsername(),uuid);
        redisTemplate.expire("email_token_"+user.getUsername(),10,TimeUnit.MINUTES);
        String url="http://localhost:8084/sso/jihuo?username="+user.getUsername()+"&token="+uuid;
        email.setContent("英雄联盟激活链接地址：<a href='" + url + "'>" + url +"</a>");
        email.setCreatetime(new Date());
        rabbitTemplate.convertAndSend("email_queue",email);
        return "login";
    }
    @RequestMapping("/login")
    public String login(@CookieValue(name = "car_token",required = false)String carToken , String username, String password, Model model, HttpServletResponse response,String badUrl){

        User user = userService.toLogin(username, password);
        System.out.println("开始登录...."+user);
        if(user==null){
            model.addAttribute("error","0");
            return "login";
        }else if(user.getStatus()==0){
            model.addAttribute("error","1");
            String email=user.getEmail();
            int i = email.indexOf("@");
            String substring = email.substring(i + 1);
            String mail="http://mail."+substring;
            model.addAttribute("mail",mail);
            return "login";
        }

        System.out.println("传来的路径:"+badUrl);
        if(badUrl==null || badUrl.equals("")){
            badUrl="http://localhost:8081/";
        }

        String token= UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(token,user);
        redisTemplate.expire(token,2, TimeUnit.DAYS);

        Cookie cookie=new Cookie("login_token",token);
        cookie.setMaxAge(60*60*24*2);
        cookie.setPath("/");
        carService.mergeCar(carToken,user);
        response.addCookie(cookie);
        return "redirect:"+badUrl;
    }
    @RequestMapping("/islogin")
    @ResponseBody
    public String isLogin(@CookieValue(name = "login_token", required = false) String loginToken){

        //获取浏览器cookie中的login_token
        System.out.println("获得浏览器发送过来的请求：" + loginToken);

        //通过token去redis中验证是否登录
        User user = null;
        if(loginToken != null){
            user = (User) redisTemplate.opsForValue().get(loginToken);
        }

        return user == null ? "islogin(null)" : "islogin('" + JSON.toJSONString(user) + "')";
    }
    @RequestMapping("/zhuxiao")
    public String zhuxiao(@CookieValue(name = "login_token", required = false) String loginToken,HttpServletResponse response){
        redisTemplate.delete(loginToken);
        Cookie cookie=new Cookie("login_token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "login";
    }
    @RequestMapping("/jihuo")
    public String jihuo(String username,String token){
        String stoken = (String) redisTemplate.opsForValue().get("email_token_" + username);
        if(stoken==null || !stoken.equals(token)){
            return "jihuoshibai";
        }
        System.out.println("开始激活账号...");
        userService.jihuoUser(username);
        return "redirect:/sso/tologin";
    }
}
