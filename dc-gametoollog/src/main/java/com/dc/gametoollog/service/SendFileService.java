package com.dc.gametoollog.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.velocity.VelocityContext;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import com.dc.gametoollog.bean.LogFileBean;
import com.dc.gametoollog.bean.LogManagerBean;
import com.dc.gametoollog.bean.res.ResInfoBean;
import com.dc.gametoollog.entity.LogManageMongoEntity;

/**
 * 发送服务
 * @author Administrator
 *
 */
@Service
public class SendFileService extends BaseFileService {
	
	

	
	public Mono<ResInfoBean> creatSendObjFile(LogManageMongoEntity logManageEntity){
		return logManagerEntityTransformBean(logManageEntity)
		.publishOn(Schedulers.elastic()) //切换到io线程操作
		.doOnNext(info->creatFilePath(info)) //创建文件夹
		.doOnNext(info->generateLogManageByTempl(info)) //生成发送服务器的服务类
		.doOnNext(info->creatPomFile(info)) //生成pom文件
		.doOnNext(info->creatMvnComdFile(info.getSendObjPath(),info.getSendMvnCom(),"/templates/mvncmd.vm")) //生成mvncomd文件
		.flatMap(info->{return logManagerService.findByLogManageId(info.getId()).flatMap(logFile->LogBeanEntityTransformBean(info,logFile)).collectList();})//获取日志对象实体
		.doOnNext(logFileList->getLogFieldBean(logFileList))//将日志对象字段封装进日志对象中，生成所有实体类
		.flatMap(info-> Mono.just(new ResInfoBean(0,"creat send file is ok",info)))
		.onErrorResume(e-> Mono.just(new ResInfoBean(1,"creat send file is error ! :["+e.getMessage()+"]",new LogFileBean())));
	}
	
	/**
	 * 数据创建文件路径
	 * @param logFileBean
	 * @return
	 */
	private void creatFilePath(LogManagerBean logFileBean){
		if(!util.creatFilePathByStr(logFileBean.getSendServicePath())) throw new RuntimeException("Can't create dir");
		if(!util.creatFilePathByStr(logFileBean.getSendBeanPath())) throw new RuntimeException("Can't create dir");
		if(!util.creatFilePathByStr(logFileBean.getSendTargetPath())) throw new RuntimeException("Can't create dir");
	}
	
	/**
	 * 生成pom文件
	 * @param logFileBean
	 */
	private void creatPomFile(LogManagerBean logFileBean){
		//生成pom文件
		VelocityContext _pomContext=new VelocityContext();
		_pomContext.put("codeGroupId", logFileBean.getSendCodeGroupId());
		_pomContext.put("codeArtifactId", logFileBean.getSendCodeArtifactId());
		_pomContext.put("codeVersion", logFileBean.getSendCodeVersion());
		_pomContext.put("objName", logFileBean.getSendObjName());
		_pomContext.put("mavenId", logFileBean.getMavenId());
		_pomContext.put("releaseMavenPath", logFileBean.getReleaseMavenPath());
		_pomContext.put("snapshotMavenPath", logFileBean.getSnapshotMavenPath());
		File pomFile=new File(logFileBean.getSendObjPath(),"pom.xml");
		try {
			creatFile(_pomContext,"/templates/pom.vm",pomFile);
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/**
	 * 根据模板生成发送服务文件
	 * @param logFileBean
	 */
	private void generateLogManageByTempl(LogManagerBean logManagerBean){
		VelocityContext _context=new VelocityContext();
		_context.put("packageName",logManagerBean.getSendServicePackage());
		_context.put("channelName",logManagerBean.getSendChannelName());
		File targetFile=new File(logManagerBean.getSendServicePath(),"SendSource.java");
		VelocityContext _sinkSendcontext=new VelocityContext();
		_sinkSendcontext.put("packageName",logManagerBean.getSendServicePackage());
		_sinkSendcontext.put("packageBeanName",logManagerBean.getSendBeanPackage());
		_sinkSendcontext.put("father",logManagerBean.getBaseLogClassName());
		_sinkSendcontext.put("fatherName",logManagerBean.getBaseLogClassName().toLowerCase());
		File sinkSendFile=new File(logManagerBean.getSendServicePath(),"SinkSend.java");
		try {
			creatFile(_context,"/templates/SendSourceClass.vm",targetFile);
			creatFile(_sinkSendcontext,"/templates/SinkSendClass.vm",sinkSendFile);
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/**
	 * 获取日志字段实体类
	 * @param logFileBean
	 */
	private void getLogFieldBean(List<LogFileBean> logFileBeans){
		logger.info("creatLogBean: size ["+logFileBeans.size()+"]");
		for(LogFileBean logFileBean:logFileBeans){
			logManagerService.findFieldsByLogBeanId(logFileBean.getId())
			.flatMap(logField->logFieldEntityTransformBean(logField))
			.collectList().subscribe(logFields->{
				logger.info("logFields: size ["+logFields.size()+"]");
				logFileBean.setLogFieldList(logFields);
				generateLogFile(logFileBean,"/templates/SendBeanClass.vm");
			});
		}
	}
	
	/**
	 * 生成日志实体类
	 * @param beanPackage
	 * @param beanPath
	 * @param logFileBean
	 */
	public void generateLogFile(LogFileBean logFileBean,String vmstr){
		logger.info("generateLogFile: logFileBean["+logFileBean.toString()+"]");
		VelocityContext logContext=new VelocityContext();
		logContext.put("packageBeanName",logFileBean.getBeanPackage());
		logContext.put("className",logFileBean.getBeanName());
		logContext.put("beanDescribe",logFileBean.getBeanDescribe());
		if(!logFileBean.isBaseBean())
			logContext.put("father",logFileBean.getFatherBeanName());
		
		File targetLogFile=new File(logFileBean.getBeanPath(),logFileBean.getBeanName()+".java");
		logContext.put("fields",logFileBean.getLogFieldList());
		try {
			creatFile(logContext,vmstr,targetLogFile);
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/**
	 * 获取日志字段实体类
	 * @param logFileBean
	 */
	private Mono<List<LogFileBean>> getLogFieldBeanByStearm(List<LogFileBean> logFileBeans){
		logger.info("creatLogBean: size ["+logFileBeans.size()+"]");
		Flux<LogFileBean> logFileFlux=Flux.empty();
		for(LogFileBean logFileBean:logFileBeans){
			Mono<LogFileBean> tmpInfo=logManagerService.findFieldsByLogBeanId(logFileBean.getId())
			.flatMap(logFieldEntity->logFieldEntityTransformBean(logFieldEntity))
			.collectList().flatMap( logFields ->{
				logger.info("logFields: size ["+logFields.size()+"]");
				logFileBean.setLogFieldList(logFields);
				generateLogFile(logFileBean,"/templates/SendBeanClass.vm");
				return Mono.just(logFileBean);
			});
			logFileFlux.concatWith(tmpInfo);
		}
		return logFileFlux.collectList();
	}
	
	
	
	/**
	 * 运行mvn操作命令
	 * @param logManageEntity
	 * @return
	 */
	public Mono<ResInfoBean> runCom(LogManageMongoEntity logManageEntity){
		return logManagerEntityTransformBean(logManageEntity)
			   .doOnNext(info->runMvncom(info.getSendMvnCom(),info.getSendObjPath()))
			   .flatMap(info-> Mono.just(new ResInfoBean(0,"runMvncom is ok",info)))
			   .onErrorResume(e-> Mono.just(new ResInfoBean(1,"runMvncom is error ! :["+e.getMessage()+"]",new LogManageMongoEntity())));
	}
	
	/**
	 * 判断文件是否可以运行
	 * @param logFileBean
	 * @return
	 */
	public Mono<ResInfoBean> canCom(LogManageMongoEntity logManageEntity){
		return logManagerEntityTransformBean(logManageEntity)
				.flatMap(info->{
					File _srcDist=new File(info.getSendObjPath()+makeLogProperties.getPathStep()+info.getSendMvnCom());
					Mono<ResInfoBean> resInfoBean=null;
					if(_srcDist.exists())
						resInfoBean=Mono.just(new ResInfoBean(0,"canCom is ok",info));
					else
						resInfoBean=Mono.just(new ResInfoBean(1,"canCom is not",info));
					return resInfoBean;
				});
	}
}
