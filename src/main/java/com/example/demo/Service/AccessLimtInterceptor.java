package com.example.demo.Service;

import com.example.demo.Util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;

@Component
public class AccessLimtInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisService redisService;
    @Value("${authCode.code}")
    private String REDIS_KEY_PREFIX_AUTH_CODE;
    @Value("${authCode.time}")
    private Long AUTH_CODE_EXPIRE_SECONDS;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if (null == accessLimit) {
                return true;
            }
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();

            if (needLogin) {
                //判断是否登录
            }

            String ipAddress = null;
            try {
                ipAddress = request.getHeader("x-forwarded-for");
                if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                    ipAddress = request.getHeader("Proxy-Client-IP");
                }
                if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                    ipAddress = request.getHeader("WL-Proxy-Client-IP");
                }
                if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                    ipAddress = request.getRemoteAddr();
                    if (ipAddress.equals("127.0.0.1")) {
                        // 根据网卡取本机配置的IP
                        InetAddress inet = null;
                        try {
                            inet = InetAddress.getLocalHost();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ipAddress = inet.getHostAddress();
                    }
                }
                // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
                if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
                    // = 15
                    if (ipAddress.indexOf(",") > 0) {
                        ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                    }
                }
            } catch (Exception e) {
                ipAddress="";
            }

            String key = REDIS_KEY_PREFIX_AUTH_CODE;
            if (redisUtil.get(key)==null) {
                redisUtil.set(key, 1);
                redisUtil.expire(key,AUTH_CODE_EXPIRE_SECONDS);
//                redisService.set(key, "1");
//                redisService.expire(key, AUTH_CODE_EXPIRE_SECONDS);
                return true;
            }

//            if (redisService.get(key)==null) {
//                redisUtil.set(key,1);
//                redisUtil.expire(key,AUTH_CODE_EXPIRE_SECONDS);
////                redisService.set(key, "1");
////                redisService.expire(key, AUTH_CODE_EXPIRE_SECONDS);
//                return true;
//            }
//            int count = Integer.parseInt(redisService.get(key));
            Integer count  = (Integer) redisUtil.get(key);

//            int count = (int) redisUtil.get(key);
            if (count < maxCount) {
                redisUtil.incr(key,1);
//                stringRedisTemplate.boundValueOps(key).increment(1);
                System.out.println("=============");
                System.out.println(key);
                System.out.println(count);
                System.out.println(maxCount);
                System.out.println("=============");
                return true;
            }
//
            if (count >= maxCount) {
//                response 返回 json 请求过于频繁请稍后再试
                System.out.println("===rrrrrrrrr=============");
                System.out.println("求求你了 不要在刷新了");
                System.out.println("===rrrrrrrrr=============");
                return false;
            }
        }

        return true;
    }
}