package com.dc.redistool.reactiveutil;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.dc.redistool.DcRedistoolApplication;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 响应式redis工具操作类
 * @author Administrator
 *
 */
@Component
public class RedisReactiveUtil {
	private static Logger logger=LoggerFactory.getLogger(DcRedistoolApplication.class);
	@Autowired
    private ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;
	
	// =============================common============================
		/**
		 * 
		 * 指定缓存失效时间
		 * 
		 * @param key  键
		 * @param time 时间(秒)
		 * @return
		 */
		public Mono<Boolean> expire(String key, long timeout) {
			if(key==null||key.isEmpty()) throw new RuntimeException("key must not be null");
			try {
				if (timeout > 0) {
					return reactiveRedisTemplate.expire(key, Duration.of(timeout, ChronoUnit.MILLIS));
				}else
					return Mono.just(false);
			} catch (Exception e) {
				logger.error(MessageFormat.format(" RedisReactive set timeout error fun={0}","expire"),e);
				return Mono.just(false);
			}
		}
		
		/**
		 * 
		 * 根据key 获取过期时间
		 * 
		 * @param key 键 不能为null
		 * @return 时间(秒) 返回0代表为永久有效
		 */
		public Mono<Duration> getExpire(String key) {
			if(key==null) throw new RuntimeException("key must not be null");
			return reactiveRedisTemplate.getExpire(key);
		}
		
		/**
		 * 
		 * 判断key是否存在
		 * 
		 * @param key 键
		 * @return true 存在 false不存在
		 */
		public Mono<Boolean> hasKey(String key) {
			if(key==null||key.isEmpty()) throw new RuntimeException("key must not be null");
			try {
				return reactiveRedisTemplate.hasKey(key);
			} catch (Exception e) {
				logger.error(MessageFormat.format(" RedisReactive hasKey timeout error fun={0}","hasKey"),e);
				return Mono.just(false);
			}
		}
		
		/**
		 * 
		 * 删除缓存
		 * 
		 * @param key 可以传一个值 或多个
		 */
		public Mono<Long> del(String... keys) {
			if(keys==null) throw new RuntimeException("key must not be null");
			return reactiveRedisTemplate.delete(keys);
		}
		
		// ============================String=============================
		/**
		 * 
		 * 普通缓存获取
		 * 
		 * @param key 键
		 * @return 值
		 * 
		 */
		public Object get(String key) {
			if(key==null||key.isEmpty()) return Mono.empty();
			return reactiveRedisTemplate.opsForValue().get(key);
		}
		
		/**
		 * 
		 * 普通缓存放入
		 * 
		 * @param key   键
		 * @param value 值
		 * @return true成功 false失败
		 * 
		 */
		public Mono<Boolean> set(String key, Object value) {
			if(key==null||key.isEmpty()) return Mono.just(false);
			try {
				return reactiveRedisTemplate.opsForValue().set(key, value);
			} catch (Exception e) {
				logger.error(MessageFormat.format("RedisReactive set value is error fun={0}","set"),e);
				return Mono.just(false);
			}
		}
		
		/**
		 * 
		 * 普通缓存放入并设置时间
		 * 
		 * @param key   键
		 * @param value 值
		 * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
		 * @return true成功 false 失败
		 * 
		 */
		public Mono<Boolean> set(String key, Object value, long timeout) {
			try {
				if (timeout > 0) {
					return reactiveRedisTemplate.opsForValue().set(key, value,Duration.of(timeout, ChronoUnit.MILLIS));
				} else {
					return set(key, value);
				}
			} catch (Exception e) {
				logger.error(MessageFormat.format("RedisReactive set value is error fun={0}","set tiemout"),e);
				return Mono.just(false);
			}
		}
		
		/**
		 * 
		 * 递增
		 * 
		 * @param key   键 不能为空
		 * @param delta 要增加几(大于0)
		 * @return
		 * 
		 */
		public Mono<Long> incr(String key, long delta) {
			if(key==null||key.isEmpty()) throw new RuntimeException("key must not be null");
			return reactiveRedisTemplate.opsForValue().increment(key, delta);
		}
		
		/**
		 * 
		 * 递增
		 * 
		 * @param key   键 不能为空
		 * @param delta 要增加几(大于0)
		 * @return
		 * 
		 */
		public Mono<Double> incr(String key, double delta) {
			if(key==null||key.isEmpty()) throw new RuntimeException("key must not be null");
			return reactiveRedisTemplate.opsForValue().increment(key, delta);
		}
		
		/**
		 * 
		 * 递减
		 * 
		 * @param key   键 不能为空
		 * @param delta 要减少几(小于0)
		 * @return
		 * 
		 */
		public Mono<Long> decr(String key, long delta) {
			if(key==null||key.isEmpty()) throw new RuntimeException("key must not be null");
			return reactiveRedisTemplate.opsForValue().increment(key, -delta);
		}
		
		/**
		 * 
		 * 递减
		 * 
		 * @param key   键 不能为空
		 * @param delta 要减少几(小于0)
		 * @return
		 * 
		 */
		public Mono<Double> decr(String key, double delta) {
			if(key==null||key.isEmpty()) throw new RuntimeException("key must not be null");
			return reactiveRedisTemplate.opsForValue().increment(key, -delta);
		}
		
		// ================================Map=================================
		/**
		 * 
		 * HashGet
		 * 
		 * @param key  键 不能为null
		 * @param item 项 不能为null
		 * @return 值
		 * 
		 */
		public Mono<Object> hget(String key,Object hashKey) {
			if(hashKey==null||key==null||key.isEmpty()) throw new RuntimeException("key must not be null");
			return reactiveRedisTemplate.opsForHash().get(key, hashKey);
		}
		
		/**
		 * 
		 * 获取hashKey对应的所有键值
		 * 
		 * @param key 键
		 * @return 对应的多个键值
		 * 
		 */
		public Flux<Map.Entry<Object, Object>> hmget(String key) {
			if(key==null||key.isEmpty()) throw new RuntimeException("key must not be null");
			return reactiveRedisTemplate.opsForHash().entries(key);
		}
		
		
		
		/**
		 * 
		 * SetMap
		 * 
		 * @param key 键
		 * @param map 对应多个键值
		 * @return true 成功 false 失败
		 * 
		 */
		public Mono<Boolean> hmset(String key, Map<Object, Object> map) {
			if(key==null||key.isEmpty()) throw new RuntimeException("key must not be null");
			try {
				return reactiveRedisTemplate.opsForHash().putAll(key, map);
			} catch (Exception e) {
				logger.error(MessageFormat.format("RedisReactive hmset value is error fun={0}","hmset"),e);
				return Mono.just(false);
			}
		}
		
		/**
		 * 
		 * SetMap
		 * 
		 * @param key 键
		 * @param map 对应多个键值
		 * @return true 成功 false 失败
		 * 
		 */
		public Mono<Boolean> hmset(String key, Map<Object, Object> map,long timeout) {
			if(key==null||key.isEmpty()) throw new RuntimeException("key must not be null");
			try {
				if(timeout>0)
					expire(key,timeout);
				return hmset(key,map);
			} catch (Exception e) {
				logger.error(MessageFormat.format("RedisReactive hmset value is error fun={0}","hmset"),e);
				return Mono.just(false);
			}
		}
		
		/**
		 * 
		 * 向一张hash表中放入数据,如果不存在将创建
		 * 
		 * @param key   键
		 * @param item  项
		 * @param value 值
		 * @return true 成功 false失败
		 * 
		 */
		public Mono<Boolean> hset(String key, Object hashKey, Object value) {
			if(hashKey==null||key==null||key.isEmpty()) throw new RuntimeException("key must not be null");
			try {
				return reactiveRedisTemplate.opsForHash().put(key, hashKey, value);
			} catch (Exception e) {
				logger.error(MessageFormat.format("RedisReactive hset value is error fun={0}","hset"),e);
				return Mono.just(false);
			}
		}
		
		/**
		 * 
		 * 向一张hash表中放入数据,如果不存在将创建
		 * 
		 * @param key   键
		 * @param item  项
		 * @param value 值
		 * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
		 * @return true 成功 false失败
		 * 
		 */
		public Mono<Boolean> hset(String key, Object hashKey, Object value, long timeout) {
			if(hashKey==null||key==null||key.isEmpty()) throw new RuntimeException("key must not be null");
			try {
				if(timeout>0)
					expire(key,timeout);
				return hset(key,hashKey,value);
			} catch (Exception e) {
				logger.error(MessageFormat.format("RedisReactive hset value is error fun={0}","hset timeout"),e);
				return Mono.just(false);
			}
		}
		
		/**
		 * 
		 * 删除hash表中的值
		 * 
		 * @param key  键 不能为null
		 * @param item 项 可以使多个 不能为null
		 * 
		 */
		public Mono<Boolean> hdel(String key) {
			if(key==null||key.isEmpty()) throw new RuntimeException("key must not be null");
			return reactiveRedisTemplate.opsForHash().delete(key);
		}
		
		/**
		 * 
		 * 判断hash表中是否有该项的值
		 * 
		 * @param key  键 不能为null
		 * @param item 项 不能为null
		 * @return true 存在 false不存在
		 * 
		 */
		public Mono<Boolean> hHasKey(String key, Object hashKey) {
			if(hashKey==null||key==null||key.isEmpty()) throw new RuntimeException("key must not be null");
			return reactiveRedisTemplate.opsForHash().hasKey(key, hashKey);
		}
		
		/**
		 * 
		 * hash递增 如果不存在,就会创建一个 并把新增后的值返回
		 * 
		 * @param key  键
		 * @param item 项
		 * @param by   要增加几(大于0)
		 * @return
		 * 
		 */
		public Mono<Double> hincr(String key,Object hashKey, double delta) {
			if(hashKey==null||key==null||key.isEmpty()) throw new RuntimeException("key must not be null");
			return reactiveRedisTemplate.opsForHash().increment(key, hashKey, delta);
		}
		
		/**
		 * 
		 * hash递增 如果不存在,就会创建一个 并把新增后的值返回
		 * 
		 * @param key  键
		 * @param item 项
		 * @param by   要增加几(大于0)
		 * @return
		 * 
		 */
		public Mono<Long> hincr(String key,Object hashKey, long delta) {
			if(hashKey==null||key==null||key.isEmpty()) throw new RuntimeException("key must not be null");
			return reactiveRedisTemplate.opsForHash().increment(key, hashKey, delta);
		}
		
		/**
		 * 
		 * hash递减
		 * 
		 * @param key  键
		 * @param item 项
		 * @param by   要减少记(小于0)
		 * @return
		 * 
		 */
		public Mono<Double> hdecr(String key, Object hashKey, double delta) {
			if(hashKey==null||key==null||key.isEmpty()) throw new RuntimeException("key must not be null");
			return reactiveRedisTemplate.opsForHash().increment(key, hashKey, -delta);
		}
		
		/**
		 * 
		 * hash递减
		 * 
		 * @param key  键
		 * @param item 项
		 * @param by   要减少记(小于0)
		 * @return
		 * 
		 */
		public Mono<Long> hdecr(String key, Object hashKey, long delta) {
			if(hashKey==null||key==null||key.isEmpty()) throw new RuntimeException("key must not be null");
			return reactiveRedisTemplate.opsForHash().increment(key, hashKey, -delta);
		}
		
		// ============================set=============================
		/**
		 * 
		 * 根据key获取Set中的所有值
		 * 
		 * @param key 键
		 * @return
		 * 
		 */
		public Flux<Object> sGet(String key) {
			if(key==null||key.isEmpty()) throw new RuntimeException("key must not be null");
			try {
				return reactiveRedisTemplate.opsForSet().members(key);
			} catch (Exception e) {
				logger.error(MessageFormat.format("RedisReactive sGet value is error fun={0}","sGet"),e);
				return Flux.empty();
			}
		}
		
		/**
		 * 
		 * 根据value从一个set中查询,是否存在
		 * 
		 * @param key   键
		 * @param value 值
		 * @return true 存在 false不存在
		 * 
		 */
		public Mono<Boolean> sHasKey(String key, Object value) {
			if(key==null||key.isEmpty()) throw new RuntimeException("key must not be null");
			try {
				return reactiveRedisTemplate.opsForSet().isMember(key, value);
			} catch (Exception e) {
				logger.error(MessageFormat.format("RedisReactive sHasKey value is error fun={0}","sHasKey"),e);
				return Mono.just(false);
			}
		}
		
		/**
		 * 
		 * 将数据放入set缓存
		 * 
		 * @param key    键
		 * @param values 值 可以是多个
		 * @return 成功个数
		 * 
		 */
		public Mono<Long> sSet(String key, Object... values) {
			if(key==null||key.isEmpty()) throw new RuntimeException("key must not be null");
			try {
				return reactiveRedisTemplate.opsForSet().add(key, values);
			} catch (Exception e) {
				logger.error(MessageFormat.format("RedisReactive sSet value is error fun={0}","sSet"),e);
				return Mono.just(0L);
			}
		}
		
		/**
		 * 
		 * 将set数据放入缓存
		 * 
		 * @param key    键
		 * @param time   时间(秒)
		 * @param values 值 可以是多个
		 * @return 成功个数
		 * 
		 */
		public Mono<Long> sSetAndTime(String key, long time, Object... values) {
			if(key==null||key.isEmpty()) throw new RuntimeException("key must not be null");
			try {
				Mono<Long> count = reactiveRedisTemplate.opsForSet().add(key, values);
				if (time > 0)
					expire(key, time);
				return count;
			} catch (Exception e) {
				logger.error(MessageFormat.format("RedisReactive sSetAndTime value is error fun={0}","sSetAndTime"),e);
				return Mono.just(0L);
			}
		}
}
