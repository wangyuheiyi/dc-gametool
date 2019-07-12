package com.dc.gametoollog.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import reactor.core.publisher.Mono;

import com.dc.gametoollog.entity.LogManageMongoEntity;

public interface ILogManageMongoRepository extends ReactiveMongoRepository<LogManageMongoEntity,String>{
	public Mono<LogManageMongoEntity> findByGamecode(int gamecode);
}
