package com.example.demo;

import com.example.demo.Service.Person;
import com.example.demo.Service.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@SpringBootTest
class DemoApplicationTests {
    @Autowired
    private RedisService redisService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${authCode.code}")
    private String REDIS_KEY_PREFIX_AUTH_CODE;
    @Value("${authCode.time}")
    private Long AUTH_CODE_EXPIRE_SECONDS;

    @Test
    void contextLoads() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        String telephone = "15859965292";
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }
        redisService.set(REDIS_KEY_PREFIX_AUTH_CODE + telephone, sb.toString());
        redisService.expire(REDIS_KEY_PREFIX_AUTH_CODE + telephone, AUTH_CODE_EXPIRE_SECONDS);
    }

    @Test
    void getRedisValue() {
//        String telephone = "15859965292";
        String realAuthCode = redisService.get(":/test/accessLimit:192.168.0.113");
        System.out.println(realAuthCode);
    }

    @Test
    void setObject() {
       User user1 = new User();
       user1.setAge(11);
       user1.setUserName("test");
       User user2 = new User();
       user2.setAge(22);
       user2.setUserName("test2");
        List<Object> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
       redisService.setList("test",userList);
    }

    @Test
    void getObject() {
        System.out.println(redisService.getObject("test"));
    }
    @Test
    void setList() {
        User user1 = new User();
        user1.setAge(13);
        user1.setUserName("test2");
        redisTemplate.opsForList().leftPush("lll",user1);

    }
    @Test
    void getList() {

//        if ()
//        System.out.println( redisTemplate.opsForList().rightPop("lll"));
        System.out.println( redisTemplate.hasKey("lll"));
    }
    @Test
    void testLamdba(){
//        Person[] array = {
//                new Person("古力娜扎", 19),
//                new Person("迪丽热巴", 18),
//                new Person("马尔扎哈", 20) };
//        Arrays.sort(array,(Person a ,Person b)->{
//            return a.getAge()-b.getAge();
//        });
//
//        for (Person person : array) {
//            System.out.println(person);
//        }

//        ArrayList<Person> items = new ArrayList<>();
//        items.add(new Person("古力娜扎", 19));
//        items.add(new Person( "迪丽热巴", 18));
//        items.add(new Person( "马尔扎哈", 20));
//        items.sort(((o1, o2) -> o1.getAge()-o2.getAge()));
//        for (Person p :items) {
//            System.out.println(p.toString());
//        }
//        items.forEach(element->{
//            System.out.println(element.toString());
//        });

        BigDecimal bignum1 = new BigDecimal("10.01");

        BigDecimal bignum2 = new BigDecimal("5");
//        BigDecimal result  = bignum1.add(bignum2);
        BigDecimal result  = bignum1.multiply(bignum2);
        System.out.println(result);
    }



}
