package com.osen.aqms.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * User: PangYi
 * Date: 2019-12-02
 * Time: 14:50
 * Description: Redis缓存工具类
 */
@Component
public class RedisOpsUtil {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 判断Map中是否包好查询的key值
     *
     * @param key    键值
     * @param search 查询的key值
     * @return 信息
     */
    public boolean hashKeyToMap(String key, String search) {
        Boolean aBoolean = stringRedisTemplate.boundHashOps(key).hasKey(search);
        if (aBoolean == null)
            return false;
        return aBoolean;
    }

    /**
     * 保存数据到指定的键值中
     *
     * @param key 键值
     * @param k   key值
     * @param v   value值
     */
    public void putToMap(String key, String k, String v) {
        stringRedisTemplate.boundHashOps(key).put(k, v);
    }

    /**
     * 获取Map中的key中value值
     *
     * @param key    键值
     * @param target key值
     * @return 信息
     */
    public String getToMap(String key, String target) {
        Object obj = stringRedisTemplate.boundHashOps(key).get(target);
        if (obj == null)
            return null;
        return (String) obj;
    }

    /**
     * 删除Map中的某个key值
     *
     * @param key    键值
     * @param target 目标
     */
    public void deleteToMap(String key, String target) {
        boolean keyToMap = hashKeyToMap(key, target);
        if (!keyToMap)
            return;
        stringRedisTemplate.boundHashOps(key).delete(target);
    }
}
