package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.qf.entity.Order;
import com.qf.service.IOrderService;
import com.qf.shop_commons.IsLogin;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/pay")
public class PayController {

    @Reference
    private IOrderService orderService;
    @RequestMapping("/alipay")
    @IsLogin(mustLogin = true)
    public void pay(String orderid, HttpServletResponse response) throws Exception {
        System.out.println("支付宝付款..."+orderid);
        Order order = orderService.queryByOrderId(orderid);
        AlipayClient alipayClient = new DefaultAlipayClient( "https://openapi.alipaydev.com/gateway.do",
                "2016100100637785",
                "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCR045++s5y45VLTMq3CKOcPSh4Da9oSCxp9MMrhX33KeOTPfosQfnTeAaZwSJeRf/42GCxwrluI2/dcLE3W4N8Nu/MQfELAAPCHu5pbxbcawT+XK9s7gOsL5ELqv2vYyfVf04Ecv4gWp2GcSRZCK6Tm3StvXemZNeIyQ27BUk5eel9KM8IYBpHc+C7+45CrKc22Q+6oJQ6lfQJRstwB5SvOqRliD3m7uavoVMMMZ6aKV4WP2NtalnxrMQOnU2gsOo3VjyP7uid39nep79erIPOHCwWcW+votS5urqFiTegtGSp1ynU9iyP6fJbbZ7IAgzytz9X7TWVq8kNCHWF3CQJAgMBAAECggEABbEaOL6VXEqhwySmLOq7aBfDApyWIh3fDeutyn8VYZZS+aZLo3qXw7dFmq69JqzYxt+7NnUBQ9lCPoRthlmeEjUauwA/5kwD2YnXiGn7nHLY6Qy5FCleH1Rj9u6fpD7ciSrE4suSO7UNvPehkZATMFO7BCUHDImXGBGw5YHZT9Lw0cNKzSFSjeLcMX+b+7Zep0wpDW/PrZhkjXgDaCb72L6bnh+zywmyIbBSGUQz6otqzP3vLUlKMke1R5TEGPuHuxVOCODmidlRGbiMMy9vaEF8FDmDvg82rVkq2gFiJaeMDIqm0Qyz8Ycn7sLUtRTkw9/Av5MWBf4B2h2n8GAR2QKBgQD1+wpq4iuGaYE0Mq1yx++pIJtZl3Y6kSoixwEvQPG2uLjN1Fp1tKzrB3ogp3hkjY3RjfQWQpizYttYBHiEi9mVZtEjFkS4qnujCH34+YNKSElNs0s1Jp4g/eo75gNKDsEdZ72oJIkS6j7KImTjz7J6MBOHvnYSD3tccfXu2j1d3wKBgQCXxCi+LWYMPXssM0HzHaFVlqj109ErSPBOXtZviFDpzKEGu0ecSboo9CrSKNi7rP/kj20caDhh8oFnTpe1F0e/b72hw23MZeU4VX9zKCZc+znqbP9mz/prnwHpyiII4z6RG2Y7EFlirl2M6OUn3SEm/9Xk5vFGPlR3zVxg1nPrFwKBgHHKAOay26mhMtjfvKcFYJOfdt36GAI91v0hjCW86FQ7nZnx6yraOse6vL9QWXxCk2FmRozpn9QUHh3Ya5v9Sdk4J/UQPxgiHXGs4wGDAz4gtHBSSa87vxjJ/UpaPBlNO/6LWAvpYtOyMvu073EHSqwFg3NfIyV1rf505Wzp3kuHAoGADxub4rKqQfWs2U2Q9x6v/NEuqKoZXHG6WlLejePICj4wmXbzPf+o5ILJ8HnYeXZTUvFkLzvAwR9e+mW8fG6A2fHJsty9hWdvVhhMw63vg7oOneOT2SxDBYuiqR9SHAhjJhbQKnkKLOSms2xLw5gjwLofFNZYlmGGDxcGZy1yBKkCgYBzVy2STs6cbSD+K0VPDdQxTdxiqdR6jWUVjxz8QGWHqTCRSZP6+t1MF5NUVyMT4PxMH3icOfRijT6uRiuzDaDDu5RMQORwANwcbMrb3/uwflpoCeyig9gMotiz5XXrRNwpQrWah8YHZcXqT2WAM5T5gtfW6ybgRIWP2pX3Sn7gYg==",
                "json",
                "utf-8",
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkdOOfvrOcuOVS0zKtwijnD0oeA2vaEgsafTDK4V99ynjkz36LEH503gGmcEiXkX/+NhgscK5biNv3XCxN1uDfDbvzEHxCwADwh7uaW8W3GsE/lyvbO4DrC+RC6r9r2Mn1X9OBHL+IFqdhnEkWQiuk5t0rb13pmTXiMkNuwVJOXnpfSjPCGAaR3Pgu/uOQqynNtkPuqCUOpX0CUbLcAeUrzqkZYg95u7mr6FTDDGemileFj9jbWpZ8azEDp1NoLDqN1Y8j+7ond/Z3qe/XqyDzhwsFnFvr6LUubq6hYk3oLRkqdcp1PYsj+nyW22eyAIM8rc/V+01lavJDQh1hdwkCQIDAQAB",
                "RSA2"); //获得初始化的AlipayClient
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request
        alipayRequest.setReturnUrl("www.baidu.com");
        alipayRequest.setNotifyUrl("http://2455fm9671.zicp.vip/pay/aliadvice");//在公共参数中设置回跳和通知地址
        alipayRequest.setBizContent("{" +
                "    \"out_trade_no\":\"" + order.getOrderid() + "\"," +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "    \"total_amount\":" + order.getAllprice().doubleValue() + "," +
                "    \"subject\":\"" + order.getOrderid() + "\"," +
                "    \"body\":\"" + order.getOrderid() + "\"," +
                "    \"extend_params\":{" +
                "    \"sys_service_provider_id\":\"2088511833207846\"" +
                "    }"+
                "  }");//填充业务参数
        String form="";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(form);//直接将完整的表单html输出到页面
        response.getWriter().flush();
        response.getWriter().close();

    }

    @RequestMapping("/aliadvice")
    @ResponseBody
    public void advice(String out_trade_no,String trade_status){
        System.out.println("怎么回事...");
        Order order = orderService.queryByOrderId(out_trade_no);
        if(trade_status.equals("TRADE_SUCCESS")){
            order.setStatus(1);
            orderService.updateOrder(order);
        }

    }
}
