package com.dc.redistool;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import reactor.core.publisher.Mono;

import com.dc.redistool.unreactiveutil.RedisUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DcRedistoolApplicationTests {
	@Autowired
    private ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;
	@Autowired
	private RedisUtil redisUtil;
	@Test
	public void contextLoads() {
//		Mono<Boolean> mono2 = reactiveRedisTemplate.opsForValue().set("test3", "wangyu1");
//		mono2.subscribe(s-> System.out.println(String.valueOf(s)+"========================="));
		Mono mono1 = reactiveRedisTemplate.opsForValue().get("test3");
        mono1.subscribe(s-> System.out.println(String.valueOf(s)+"========================="));
		
//		redisUtil.set("name3", "wangyu3");
//		System.out.println("name3:  " + redisUtil.get("name3"));
	}

}
