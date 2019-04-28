package tch.zijidaserver.cache;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils implements Cache {
	private static final Logger log = LoggerFactory.getLogger(RedisUtils.class);

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired(required = false)

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        RedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(stringSerializer);
        this.redisTemplate = redisTemplate;
    }
    public String getRedisFullKey(String prefix,String appKey,String ...time) {
    	if (time != null && time.length>0) {
    		return prefix + time[0] + "_" + appKey;
    	}
    	return prefix + appKey;
    }


    @Override
    public boolean checkKeyExists(String key) {
    	log.info("get data from redis:key-{}",key);
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error("[haier Cache]check key exists(" + key + ")error|check key", e);
            return false;
        }
    }

    @Override
    public String get(String key) {
        try {
            if (key != null) {
                Object result = null;
                ValueOperations<String, Object> operations = redisTemplate.opsForValue();
                result = operations.get(key);
                return (String) result;
            } else {
                return null;
            }

        } catch (Exception e) {
            
            log.error("[haier Cache]get key(" + key + ")error|get", e);
            return null;
        }
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        try {
            if (key != null) {
                Object result = null;
                ValueOperations<String, Object> operations = redisTemplate.opsForValue();
                result = operations.get(key);
                return JSON.parseObject((String) result, clazz);
            } else {
                return null;
            }
        } catch (Exception e) {
            
            log.error("[haier Cache]get key(" + key + "),class(" + clazz.toString() + ")error|get key,clazz", e);
            return null;
        }
    }

    @Override
    public boolean set(String key, Object value, int expiredSeconds) {
    	log.info("set data to redis,key:{},value:{},expired:{}",key,value,expiredSeconds);
        String objectJson = JSON.toJSONString(value);
        try {
            if (expiredSeconds > 0) {
                ValueOperations<String, Object> operations = redisTemplate.opsForValue();
                operations.set(key, objectJson);
                redisTemplate.expire(key, expiredSeconds, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            
            log.error(
                "[haier Cache]set key(" + key + "),value(" + objectJson + "),secs(" + expiredSeconds
                    + ") error set", e);
            return false;
        }
    }

    @Override
    public boolean set(String key, Object value) {
        String objectJson = JSON.toJSONString(value);
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            operations.set(key, objectJson);
            return true;
        } catch (Exception e) {
            
            log.error("[haier Cache]set key(" + key + "),value(" + objectJson + "), error set key", e);
            return false;
        }
    }

    @Override
    public long incrString(String key) {
        try {
            return redisTemplate.opsForValue().increment(key, 1L);
        } catch (Exception e) {
            
            log.error("[haier Cache]incr key (" + key + "), error incr", e);
            return -1;
        }
    }

    @Override
    public long expireAt(String key, int unixTime) {
        try {
            if (unixTime > 0) {
                redisTemplate.expire(key, unixTime, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            
            log.error("[haier Cache]expireAt key (" + key + ") unixTime (" + unixTime + "), error expire",
                e);
            return -1;
        }
        return 0;
    }

    @Override
    public boolean delete(String... keys) {
        if (keys == null || keys.length == 0) {
            return true;
        }
        boolean ret = true;
        try {
            if ( keys.length > 0) {
                if (keys.length == 1) {
                    redisTemplate.delete(keys[0]);
                } else {
                    redisTemplate.delete(CollectionUtils.arrayToList(keys));
                }
            }
        } catch (Exception e) {
            
            log.error("[haier Cache]del key(" + keys + "),error delete", e);
            ret = false;
        }
        return ret;
    }


}
