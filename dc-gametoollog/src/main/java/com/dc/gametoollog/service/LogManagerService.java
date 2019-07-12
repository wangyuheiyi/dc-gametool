package com.dc.gametoollog.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.dc.gametoollog.DcGametoollogApplication;
import com.dc.gametoollog.bean.res.ResInfoBean;
import com.dc.gametoollog.bean.res.TableColumnBean;
import com.dc.gametoollog.bean.search.SearchInfoBean;
import com.dc.gametoollog.dao.ILogBeanMongoRepository;
import com.dc.gametoollog.dao.ILogFieldMongoRepository;
import com.dc.gametoollog.dao.ILogManageMongoRepository;
import com.dc.gametoollog.entity.LogBeanMongoEntity;
import com.dc.gametoollog.entity.LogFieldMongoEntity;
import com.dc.gametoollog.entity.LogManageMongoEntity;
import com.dc.gametoollog.util.LogUtil;

/**
 * 日志管理数据库操作
 * @author Administrator
 *
 */
@Service
public class LogManagerService {

	private static Logger logger=LoggerFactory.getLogger(DcGametoollogApplication.class);
	
	@Autowired
	LogUtil util;
	
	/** ============mongoDb============*/
	@Autowired
	ILogManageMongoRepository logManageMongoRepository;
	
	@Autowired
	ILogBeanMongoRepository logBeanMongoRepository;
	
	@Autowired
	ILogFieldMongoRepository lLogFieldMongoRepository;
	
	/**
	 * 保存数据库数据 MongoDB
	 * @param param
	 * @return
	 */
	public Mono<ResInfoBean> saveLogManager(LogManageMongoEntity param){
		return logManageMongoRepository.save(param)
				.flatMap(info-> Mono.just(new ResInfoBean(0,"save is ok",info)))
				.onErrorResume(e-> Mono.just(new ResInfoBean(1,"save is error ! :["+e.getMessage()+"]",new LogManageMongoEntity())));
	}
	
	/**
	 * 查询数据库中的数据MongoDB
	 * @param gameCode
	 * @return
	 */
	public Mono<ResInfoBean> findByGamecodeMongo(int gameCode){
		return logManageMongoRepository.findByGamecode(gameCode)
				.defaultIfEmpty(new LogManageMongoEntity())
				.flatMap(info-> Mono.just(new ResInfoBean(0,"get data info ok",info)))
				.onErrorResume(e-> {return Mono.just(new ResInfoBean(1,"get data info error ! :["+e.getMessage()+"]",new LogManageMongoEntity()));});
	}
	
	/**
	 * 删除整个的日志信息
	 * @param param
	 * @return
	 */
	public Mono<ResInfoBean> deleteLogManager(LogManageMongoEntity param){
		return logBeanMongoRepository.findByLogManageId(param.getId())
		.flatMap(logBean -> lLogFieldMongoRepository.findByLogBeanId(logBean.getId())
				.flatMap(logField ->lLogFieldMongoRepository.delete(logField)).onErrorResume(e->{
					logger.error("=====delete Field is error======="+e.getMessage());
					return Flux.empty();
				}).then(Mono.just(logBean))
		).flatMap(newLogBean->logBeanMongoRepository.delete(newLogBean)).onErrorResume(e->{
			logger.error("=====delete Bean is error======="+e.getMessage());
			return Flux.empty();
		}).then(Mono.just(param))
		.flatMap(logManage ->logManageMongoRepository.delete(logManage)).then(Mono.just(new ResInfoBean(0,"delete LogManager is ok",new LogBeanMongoEntity())))
		.onErrorResume(e-> Mono.just(new ResInfoBean(1,"delete LogManager is error ! :["+e.getMessage()+"]",new LogBeanMongoEntity())));
	}
	
	/**
	 * 获取游戏对象的列表
	 * @param logManageId
	 * @return
	 */
	public Flux<LogBeanMongoEntity> findByLogManageId(String logManageId){
		return logBeanMongoRepository.findByLogManageId(logManageId)
		   		.defaultIfEmpty(new LogBeanMongoEntity())
		   		.onErrorResume(e->{
		   			logger.error(e.getMessage());
		   			return Flux.just(new LogBeanMongoEntity());
		   		});
	}
	
	/**
	 * 获取游戏对象的列表
	 * @param logManageId
	 * @return
	 */
	public Flux<LogBeanMongoEntity> findByLogManage(LogManageMongoEntity param){
		return logBeanMongoRepository.findByLogManageId(param.getId())
				//如果没有查询到就传经基础类
		   		.defaultIfEmpty(initLogBeanMongoEntity(param))
		   		.onErrorResume(e->{
		   			logger.error(e.getMessage());
		   			return Flux.just(new LogBeanMongoEntity());
		   		});
	}
	
	/**
	 * 默认初始化一个基类
	 * @param param
	 * @return
	 */
	private LogBeanMongoEntity initLogBeanMongoEntity(LogManageMongoEntity param){
		if(util.isStrNullOrEmpty(param.getBaseLogClassName())) return new LogBeanMongoEntity();
		LogBeanMongoEntity logBeanMongoEntity=new LogBeanMongoEntity();
		logBeanMongoEntity.setLogManageId(param.getId());
		logBeanMongoEntity.setBeanName(param.getBaseLogClassName());
		logBeanMongoEntity.setBeanDescribe("基类");
		logBeanMongoEntity.setIsBaseBean("1");
		return logBeanMongoEntity;
	}
	
	/**
	 * 保存日志实体对象
	 * @param logBeanMongoEntity
	 * @return
	 */
	public Mono<ResInfoBean> saveLogBeanMongo(LogBeanMongoEntity logBeanMongoEntity){
		return logBeanMongoRepository.save(logBeanMongoEntity)
				.flatMap(info-> Mono.just(new ResInfoBean(0,"save is ok",info)))
				.onErrorResume(e-> Mono.just(new ResInfoBean(1,"save is error ! :["+e.getMessage()+"]",new LogBeanMongoEntity())));
	}
	
	/**
	 * 删除所有实体bean下所有的字段，再删除bean本身的数据
	 * @param logBeanMongoEntity
	 * @return
	 */
	public Mono<ResInfoBean> deleteLogMongoMongo(LogBeanMongoEntity logBeanMongoEntity){
		return lLogFieldMongoRepository.findByLogBeanId(logBeanMongoEntity.getId())
		.flatMap(logField->lLogFieldMongoRepository.delete(logField)).onErrorResume(e->{
			logger.error("=====delete Field is error======="+e.getMessage());
			return Flux.empty();
		}).then(Mono.just(logBeanMongoEntity))
		.flatMap(pram->logBeanMongoRepository.delete(pram))
		.then(Mono.just(new ResInfoBean(0,"delete bean is ok",Mono.empty())))
		.onErrorResume(e-> Mono.just(new ResInfoBean(1,"delete bean is error ! :["+e.getMessage()+"]",Mono.empty())));
	}
	
	/**
	 * 根据实体id查询实体中所有的字段
	 * @param logBeanId
	 * @return
	 */
	public Flux<LogFieldMongoEntity> findFieldsByLogBeanId(String logBeanId){
		return lLogFieldMongoRepository.findByLogBeanId(logBeanId)
		   		.defaultIfEmpty(new LogFieldMongoEntity())
		   		.onErrorResume(e->{
		   			logger.error(e.getMessage());
		   			return Flux.just(new LogFieldMongoEntity());
		   		});
	}
	
	/**
	 * 保存实体类字段
	 * @param logFieldMongoEntity
	 * @return
	 */
	public Mono<ResInfoBean> saveLogFieldMongo(LogFieldMongoEntity logFieldMongoEntity){
		return lLogFieldMongoRepository.save(logFieldMongoEntity)
				.flatMap(info-> Mono.just(new ResInfoBean(0,"save is ok",info)))
				.onErrorResume(e-> Mono.just(new ResInfoBean(1,"save is error ! :["+e.getMessage()+"]",new LogFieldMongoEntity())));
	} 
	
	/**
	 * 删除实体类中的某一个字段
	 * @param logFieldMongoEntity
	 * @return
	 */
	public Mono<ResInfoBean> deleteLogFieldMongo(LogFieldMongoEntity logFieldMongoEntity){
		return lLogFieldMongoRepository.delete(logFieldMongoEntity).then(Mono.just(new ResInfoBean(0,"delete field is ok",Mono.empty())))
				.onErrorResume(e-> Mono.just(new ResInfoBean(1,"delete field is error ! :["+e.getMessage()+"]",Mono.empty())));
	}
	
	public Flux<TableColumnBean> searchColumnInfo(SearchInfoBean param){
		if(param==null) return Flux.empty();
		return lLogFieldMongoRepository.findByLogBeanId(param.getBaseBeanId())
				.defaultIfEmpty(new LogFieldMongoEntity())
				.concatWith(lLogFieldMongoRepository.findByLogBeanId(param.getParam()))
				.flatMap(info->{
					return Flux.just(new TableColumnBean(info.getComment(),info.getFieldName()));
				});
	}
	
	public Flux<Object> searchTableInfo(SearchInfoBean param){
		if(param==null) return Flux.empty();
		WebClient webClient = WebClient.create("http://"+param.getUrl());
		return webClient.get().uri(param.getPath()).retrieve().bodyToFlux(Object.class);
	}
}
