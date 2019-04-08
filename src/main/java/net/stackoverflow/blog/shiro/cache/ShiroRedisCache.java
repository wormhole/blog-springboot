package net.stackoverflow.blog.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Shiro-Redis缓存
 *
 * @param <K>
 * @param <V>
 * @author 凉衫薄
 */
public class ShiroRedisCache<K, V> implements Cache<K, V> {

    private org.springframework.cache.Cache cache;
    private RedisTemplate redisTemplate;
    private String prefix;

    public ShiroRedisCache(org.springframework.cache.Cache cache, RedisTemplate redisTemplate) {
        this.cache = cache;
        this.redisTemplate = redisTemplate;
        this.prefix = "shiro:" + cache.getName() + ":";
    }

    private String getKey(K k) {
        String key = null;
        if (k instanceof PrincipalCollection) {
            PrincipalCollection principalCollection = (PrincipalCollection) k;
            key = principalCollection.getPrimaryPrincipal().toString();
        } else {
            key = (String) k;
        }
        key = this.prefix + key;
        return key;
    }

    @Override
    public V get(K k) throws CacheException {
        if (k == null) {
            return null;
        }
        org.springframework.cache.Cache.ValueWrapper valueWrapper = null;
        String key = getKey(k);
        valueWrapper = cache.get(key);
        if (valueWrapper == null) {
            return null;
        }
        return (V) valueWrapper.get();
    }

    @Override
    public V put(K k, V v) throws CacheException {
        String key = getKey(k);
        cache.put(key, v);
        return get(k);
    }

    @Override
    public V remove(K k) throws CacheException {
        V v = get(k);
        String key = getKey(k);
        cache.evict(key);
        return v;
    }

    @Override
    public void clear() throws CacheException {
        cache.clear();
    }

    @Override
    public int size() {
        return redisTemplate.opsForZSet().size(cache.getName() + "~keys").intValue();
    }

    @Override
    public Set<K> keys() {
        return (Set<K>) redisTemplate.opsForZSet().range(cache.getName() + "~keys", 0, -1);
    }

    @Override
    public Collection<V> values() {
        Set<K> set = keys();
        List<V> list = new ArrayList<>();
        for (K k : set) {
            list.add(get(k));
        }
        return list;
    }
}
