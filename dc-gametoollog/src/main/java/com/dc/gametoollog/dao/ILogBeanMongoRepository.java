package com.dc.gametoollog.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import reactor.core.publisher.Flux;

import com.dc.gametoollog.entity.LogBeanMongoEntity;

public interface ILogBeanMongoRepository extends ReactiveMongoRepository<LogBeanMongoEntity,String>{
	public Flux<LogBeanMongoEntity> findByLogManageId(String logManageId);
}
