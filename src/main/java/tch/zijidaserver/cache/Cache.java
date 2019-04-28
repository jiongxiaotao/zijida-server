package tch.zijidaserver.cache;
public interface Cache {

    public boolean checkKeyExists(String key);

    /**
     * 获取	字符串结构	数据
     */
    public String get(String key);

    /**
     * 获取	泛型结构	数据
     */
    public <T> T get(String key, Class<T> clazz);

    /**
     * 设置	字符串结构	数据
     *
     * @param key 需要缓存的键值
     * @param value 需要缓存的字符串
     * @param expiredSeconds 有效时间，单位（秒）
     */
    public boolean set(String key, Object value, int expiredSeconds);

    /**
     * 设置
     */
    public boolean set(String key, Object value);

    public long incrString(String key);

    public long expireAt(String key, int unixTime);

    /**
     * 删除	数据
     */
    public abstract boolean delete(String... keys);
}