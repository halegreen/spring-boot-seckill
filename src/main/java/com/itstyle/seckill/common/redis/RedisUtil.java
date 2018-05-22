package com.itstyle.seckill.common.redis;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
/**
 * 缓存工具类
 * 创建者 科帮网
 * 创建时间	2018年4月8日
 */
@Component
public class RedisUtil {
	
   private static final Logger logger = LoggerFactory.getLogger(RedisUtil.class);
	
    @Resource
	private RedisTemplate<Serializable, Serializable> redisTemplate;
	
	/**
     * 前缀
     */
    public static final String KEY_PREFIX_VALUE = "itstyle:seckill:value:";

	
	/**
     * 缓存value操作
     * @param k
     * @param v
     * @param time
     * @return
     */
    public  boolean cacheValue(String k, Serializable v, long time) {
        String key = KEY_PREFIX_VALUE + k;
        try {
            ValueOperations<Serializable, Serializable> valueOps =  redisTemplate.opsForValue();
            valueOps.set(key, v);
            if (time > 0) redisTemplate.expire(key, time, TimeUnit.SECONDS);
            return true;
        } catch (Throwable t) {
            logger.error("缓存[{}]失败, value[{}]",key,v,t);
        }
        return false;
    }
    /**
     * 缓存value操作
     * @Author  科帮网
     * @param k
     * @param v
     * @param time
     * @param unit
     * @return  boolean
     * @Date	2017年12月23日
     * 更新日志
     * 2017年12月23日  科帮网  首次创建
     *
     */
    public  boolean cacheValue(String k, Serializable v, long time,TimeUnit unit) {
        String key = KEY_PREFIX_VALUE + k;
        try {
            ValueOperations<Serializable, Serializable> valueOps =  redisTemplate.opsForValue();
            valueOps.set(key, v);
            if (time > 0) redisTemplate.expire(key, time, unit);
            return true;
        } catch (Throwable t) {
            logger.error("缓存[{}]失败, value[{}]",key,v,t);
        }
        return false;
    }

    /**
     * 缓存value操作
     * @param k
     * @param v
     * @return
     */
    public  boolean cacheValue(String k, Serializable v) {
        return cacheValue(k, v, -1);
    }

    /**
     * 判断缓存是否存在
     * @param k
     * @return
     */
    public  boolean containsValueKey(String k) {
        String key = KEY_PREFIX_VALUE + k;
        try {
            return redisTemplate.hasKey(key);
        } catch (Throwable t) {
            logger.error("判断缓存存在失败key[" + key + ", error[" + t + "]");
        }
        return false;
    }
    /**
     * 获取缓存
     * @param k
     * @return
     */
    public  Serializable getValue(String k) {
        try {
            ValueOperations<Serializable, Serializable> valueOps =  redisTemplate.opsForValue();
            return valueOps.get(KEY_PREFIX_VALUE + k);
        } catch (Throwable t) {
            logger.error("获取缓存失败key[" + KEY_PREFIX_VALUE + k + ", error[" + t + "]");
        }
        return null;
    }
    /**
     * 移除缓存
     * @param k
     * @return
     */
    public  boolean removeValue(String k) {
    	String key = KEY_PREFIX_VALUE + k;
    	try {
            redisTemplate.delete(key);
            return true;
        } catch (Throwable t) {
            logger.error("获取缓存失败key[" + key + ", error[" + t + "]");
        }
        return false;
    }
}
