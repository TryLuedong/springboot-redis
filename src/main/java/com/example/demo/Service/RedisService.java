package com.example.demo.Service;

import java.util.List;

/**
 * redis操作Service,
 * 对象和数组都以json形式进行存储
 * Created by macro on 2018/8/7.
 */
public interface RedisService {
    /**
     * 存储数据
     */
    void set(String key, String value);
    void setLong(String key, Long data);

    /**
     * 获取数据
     */
    String get(String key);

    /**
     * 设置超期时间
     */
    boolean expire(String key, long expire);

    /**
     * 删除数据
     */
    void remove(String key);

    /**
     * 自增操作
     * @param delta 自增步长
     */
    Long increment(String key, long delta);

    void setObject(String key,Object object);
    Object getObject(String key);
    void setList(String key, List<Object>objects);
    Object getList(String key);
    public long incr(String key, long delta);
}
