package com.code.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.*;


public class RedisUtils {
    private static final Logger log = LoggerFactory.getLogger(RedisUtils.class);

    /**
     * Jedis实例获取返回码
     *
     * @author jqlin
     */
    public static class JedisStatus {
        /**
         * Jedis实例获取失败
         */
        public static final long FAIL_LONG = -5L;
        /**
         * Jedis实例获取失败
         */
        public static final int FAIL_INT = -5;
        /**
         * Jedis实例获取失败
         */
        public static final String FAIL_STRING = "-5";
    }

    /**
     * 同步获取Jedis实例
     *
     * @return Jedis
     */
    public static Jedis getJedis(String path) {
        PropertiesUtil propertiesUtil = PropertiesUtil.getRedisProperties(path);
        // 1. 设置IP地址和端口
        Jedis jedis = new Jedis(propertiesUtil.getProperty("host"), Integer.valueOf(propertiesUtil.getProperty("port")));
        jedis.auth(propertiesUtil.getProperty("password"));
        return jedis;
    }

    /**
     * 释放jedis资源
     *
     * @param jedis
     */
    @SuppressWarnings("deprecation")
    public static void returnResource(final Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }


    /**
     * 设置值
     *
     * @param key
     * @param value
     * @return -5：Jedis实例获取失败<br/>OK：操作成功<br/>null：操作失败
     * @author jqlin
     */
    public static String set(Jedis jedis, String key, String value) {
        if (jedis == null) {
            return JedisStatus.FAIL_STRING;
        }

        String result = null;
        try {
            result = jedis.set(key, value);
        } catch (Exception e) {
            log.error("设置值失败：" + e.getMessage(), e);
        } finally {
            returnResource(jedis);
        }

        return result;
    }

    /**
     * 设置值
     *
     * @param key
     * @param value
     * @param expire 过期时间，单位：秒
     * @return -5：Jedis实例获取失败<br/>OK：操作成功<br/>null：操作失败
     * @author jqlin
     */
    public static String set(Jedis jedis, String key, String value, int expire) {
        if (jedis == null) {
            return JedisStatus.FAIL_STRING;
        }

        String result = null;
        try {
            result = jedis.set(key, value);
            jedis.expire(key, expire);
        } catch (Exception e) {
            log.error("设置值失败：" + e.getMessage(), e);
        } finally {
            returnResource(jedis);
        }

        return result;
    }

    /**
     * 获取值
     *
     * @param key
     * @return
     * @author jqlin
     */
    public static String get(Jedis jedis, String key) {
        if (jedis == null) {
            return JedisStatus.FAIL_STRING;
        }

        String result = null;
        try {
            result = jedis.get(key);
        } catch (Exception e) {
            log.error("获取值失败：" + e.getMessage(), e);
        } finally {
            returnResource(jedis);
        }

        return result;
    }

    /**
     * 设置key的过期时间
     *
     * @param key
     * @param seconds -5：Jedis实例获取失败，1：成功，0：失败
     * @return
     * @author jqlin
     */
    public static long expire(Jedis jedis, String key, int seconds) {
        if (jedis == null) {
            return JedisStatus.FAIL_LONG;
        }

        Long result = null;
        try {
            result = jedis.expire(key, seconds);
        } catch (Exception e) {
            log.error(String.format("设置key=%s的过期时间失败：" + e.getMessage(), key), e);
        } finally {
            returnResource(jedis);
        }

        return result;
    }

    /**
     * 判断key是否存在
     *
     * @param key
     * @return
     * @author jqlin
     */
    public static boolean exists(Jedis jedis, String key) {
        if (jedis == null) {
            log.warn("Jedis实例获取为空");
            return false;
        }

        boolean result = false;
        try {
            result = jedis.exists(key);
        } catch (Exception e) {
            log.error(String.format("判断key=%s是否存在失败：" + e.getMessage(), key), e);
        } finally {
            returnResource(jedis);
        }

        return result;
    }

    /**
     * 删除key
     *
     * @param keys
     * @return -5：Jedis实例获取失败，1：成功，0：失败
     * @author jqlin
     */
    public static long del(Jedis jedis, String... keys) {
        if (jedis == null) {
            return JedisStatus.FAIL_LONG;
        }

        long result = JedisStatus.FAIL_LONG;
        try {
            result = jedis.del(keys);
        } catch (Exception e) {
            log.error(String.format("删除key=%s失败：" + e.getMessage(), keys), e);
        } finally {
            returnResource(jedis);
        }

        return result;
    }

    /**
     * set if not exists，若key已存在，则setnx不做任何操作
     *
     * @param key
     * @param value key已存在，1：key赋值成功
     * @return
     * @author jqlin
     */
    public static long setnx(Jedis jedis, String key, String value) {
        long result = JedisStatus.FAIL_LONG;

        if (jedis == null) {
            return result;
        }

        try {
            result = jedis.setnx(key, value);
        } catch (Exception e) {
            log.error("设置值失败：" + e.getMessage(), e);
        } finally {
            returnResource(jedis);
        }

        return result;
    }

    /**
     * set if not exists，若key已存在，则setnx不做任何操作
     *
     * @param key
     * @param value  key已存在，1：key赋值成功
     * @param expire 过期时间，单位：秒
     * @return
     * @author jqlin
     */
    public static long setnx(Jedis jedis, String key, String value, int expire) {
        long result = JedisStatus.FAIL_LONG;

        if (jedis == null) {
            return result;
        }

        try {
            result = jedis.setnx(key, value);
            jedis.expire(key, expire);
        } catch (Exception e) {
            log.error("设置值失败：" + e.getMessage(), e);
        } finally {
            returnResource(jedis);
        }

        return result;
    }

    /**
     * 在列表key的头部插入元素
     *
     * @param key
     * @param values -5：Jedis实例获取失败，>0：返回操作成功的条数，0：失败
     * @return
     * @author jqlin
     */
    public static long lpush(Jedis jedis, String key, String... values) {
        long result = JedisStatus.FAIL_LONG;

        if (jedis == null) {
            return result;
        }

        try {
            result = jedis.lpush(key, values);
        } catch (Exception e) {
            log.error("在列表key的头部插入元素失败：" + e.getMessage(), e);
        } finally {
            returnResource(jedis);
        }

        return result;
    }

    /**
     * 在列表key的尾部插入元素
     *
     * @param key
     * @param values -5：Jedis实例获取失败，>0：返回操作成功的条数，0：失败
     * @return
     * @author jqlin
     */
    public static long rpush(Jedis jedis, String key, String... values) {
        long result = JedisStatus.FAIL_LONG;

        if (jedis == null) {
            return result;
        }

        try {
            result = jedis.rpush(key, values);
        } catch (Exception e) {
            log.error("在列表key的尾部插入元素失败：" + e.getMessage(), e);
        } finally {
            returnResource(jedis);
        }

        return result;
    }

    /**
     * 返回存储在key列表的特定元素
     *
     * @param key
     * @param start 开始索引，索引从0开始，0表示第一个元素，1表示第二个元素
     * @param end   结束索引，-1表示最后一个元素，-2表示倒数第二个元素
     * @return redis client获取失败返回null
     * @author jqlin
     */
    public static List<String> lrange(Jedis jedis, String key, long start, long end) {
        if (jedis == null) {
            return null;
        }

        List<String> result = null;
        try {
            result = jedis.lrange(key, start, end);
        } catch (Exception e) {
            log.error("查询列表元素失败：" + e.getMessage(), e);
        } finally {
            returnResource(jedis);
        }

        return result;
    }


    /**
     * 获取列表长度
     *
     * @param key -5：Jedis实例获取失败
     * @return
     * @author jqlin
     */
    public static long llen(Jedis jedis, String key) {
        if (jedis == null) {
            return JedisStatus.FAIL_LONG;
        }

        long result = 0;
        try {
            result = jedis.llen(key);
        } catch (Exception e) {
            log.error("获取列表长度失败：" + e.getMessage(), e);
        } finally {
            returnResource(jedis);
        }

        return result;
    }

    /**
     * 移除等于value的元素<br/><br/>
     * 当count>0时，从表头开始查找，移除count个；<br/>
     * 当count=0时，从表头开始查找，移除所有等于value的；<br/>
     * 当count<0时，从表尾开始查找，移除count个
     *
     * @param key
     * @param count
     * @param value
     * @return -5:Jedis实例获取失败
     * @author jqlin
     */
    public static long lrem(Jedis jedis, String key, long count, String value) {
        if (jedis == null) {
            return JedisStatus.FAIL_LONG;
        }

        long result = 0;
        try {
            result = jedis.lrem(key, count, value);
        } catch (Exception e) {
            log.error("获取列表长度失败：" + e.getMessage(), e);
        } finally {
            returnResource(jedis);
        }

        return result;
    }

    /**
     * 对列表进行修剪
     *
     * @param key
     * @param start
     * @param end
     * @return -5：Jedis实例获取失败，OK：命令执行成功
     * @author jqlin
     */
    public static String ltrim(Jedis jedis, String key, long start, long end) {
        if (jedis == null) {
            return JedisStatus.FAIL_STRING;
        }

        String result = "";
        try {
            result = jedis.ltrim(key, start, end);
        } catch (Exception e) {
            log.error("获取列表长度失败：" + e.getMessage(), e);
        } finally {
            returnResource(jedis);
        }

        return result;
    }


    /**
     * 缓存Map赋值
     *
     * @param key
     * @param field
     * @param value
     * @return -5：Jedis实例获取失败
     * @author jqlin
     */
    public static long hset(Jedis jedis, String key, String field, String value) {
        if (jedis == null) {
            return JedisStatus.FAIL_LONG;
        }

        long result = 0L;
        try {
            result = jedis.hset(key, field, value);
        } catch (Exception e) {
            log.error("缓存Map赋值失败：" + e.getMessage(), e);
        } finally {
            returnResource(jedis);
        }

        return result;
    }


    /**
     * 获取缓存的Map值
     *
     * @param key
     * @return
     */
    public static String hget(Jedis jedis, String key, String field) {
        if (jedis == null) {
            return null;
        }

        String result = null;
        try {
            result = jedis.hget(key, field);
        } catch (Exception e) {
            log.error("获取缓存的Map值失败：" + e.getMessage(), e);
        } finally {
            returnResource(jedis);
        }

        return result;
    }

    /**
     * 获取map所有的字段和值
     *
     * @param key
     * @return
     * @author jqlin
     */
    public static Map<String, String> hgetAll(Jedis jedis, String key) {
        Map<String, String> map = new HashMap<String, String>();

        if (jedis == null) {
            log.warn("Jedis实例获取为空");
            return map;
        }

        try {
            map = jedis.hgetAll(key);
        } catch (Exception e) {
            log.error("获取map所有的字段和值失败：" + e.getMessage(), e);
        } finally {
            returnResource(jedis);
        }

        return map;
    }

    /**
     * 查看哈希表 key 中，指定的field字段是否存在。
     *
     * @param key
     * @param field
     * @return
     * @author jqlin
     */
    public static Boolean hexists(Jedis jedis, String key, String field) {
        if (jedis == null) {
            log.warn("Jedis实例获取为空");
            return null;
        }

        try {
            return jedis.hexists(key, field);
        } catch (Exception e) {
            log.error("查看哈希表field字段是否存在失败：" + e.getMessage(), e);
        } finally {
            returnResource(jedis);
        }

        return null;
    }

    /**
     * 获取所有哈希表中的字段
     *
     * @param key
     * @return
     * @author jqlin
     */
    public static Set<String> hkeys(Jedis jedis, String key) {
        Set<String> set = new HashSet<String>();
        if (jedis == null) {
            log.warn("Jedis实例获取为空");
            return set;
        }

        try {
            return jedis.hkeys(key);
        } catch (Exception e) {
            log.error("获取所有哈希表中的字段失败：" + e.getMessage(), e);
        } finally {
            returnResource(jedis);
        }

        return null;
    }

    /**
     * 获取所有哈希表中的值
     *
     * @param key
     * @return
     * @author jqlin
     */
    public static List<String> hvals(Jedis jedis, String key) {
        List<String> list = new ArrayList<String>();
        if (jedis == null) {
            log.warn("Jedis实例获取为空");
            return list;
        }

        try {
            return jedis.hvals(key);
        } catch (Exception e) {
            log.error("获取所有哈希表中的值失败：" + e.getMessage(), e);
        } finally {
            returnResource(jedis);
        }

        return null;
    }

    /**
     * 从哈希表 key 中删除指定的field
     *
     * @param key
     * @param fields
     * @return
     * @author jqlin
     */
    public static long hdel(Jedis jedis, String key, String... fields) {
        if (jedis == null) {
            log.warn("Jedis实例获取为空");
            return JedisStatus.FAIL_LONG;
        }

        try {
            return jedis.hdel(key, fields);
        } catch (Exception e) {
            log.error("map删除指定的field失败：" + e.getMessage(), e);
        } finally {
            returnResource(jedis);
        }

        return 0;
    }

    public static Set<String> keys(Jedis jedis, String pattern) {
        Set<String> keyList = new HashSet<String>();
        if (jedis == null) {
            log.warn("Jedis实例获取为空");
            return keyList;
        }

        try {
            keyList = jedis.keys(pattern);
        } catch (Exception e) {
            log.error("操作keys失败：" + e.getMessage(), e);
        } finally {
            returnResource(jedis);
        }

        return keyList;
    }

}