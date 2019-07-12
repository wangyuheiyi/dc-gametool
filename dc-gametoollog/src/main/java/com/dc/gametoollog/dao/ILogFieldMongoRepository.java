package com.dc.gametoollog.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.dc.gametoollog.entity.LogFieldMongoEntity;

public interface ILogFieldMongoRepository extends ReactiveMongoRepository<LogFieldMongoEntity,String>{
	public Flux<LogFieldMongoEntity> findByLogBeanId(String logBeanId);
	/** 删除指定实体bean下的所有数据 */
	public Mono<Void> deleteByLogBeanId(String logBeanId);
}
