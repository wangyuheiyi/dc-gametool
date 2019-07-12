package com.dc.gametoollog.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.velocity.VelocityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import com.dc.gametoollog.bean.LogFileBean;
import com.dc.gametoollog.bean.LogManagerBean;
import com.dc.gametoollog.bean.res.ResInfoBean;
import com.dc.gametoollog.conf.MakeLogProperties;
import com.dc.gametoollog.entity.LogManageMongoEntity;

/**
 * 接收服务
 * @author Administrator
 *
 */
@Service
public class ReceiverFileService extends BaseFileService{
	@Autowired
	MakeLogProperties makeLogProperties;
	public Mono<ResInfoBean> creatReceiverObjFile(LogManageMongoEntity logManageEntity){
		return logManagerEntityTransformBean(logManageEntity)
				.publishOn(Schedulers.elastic()) //切换到io线程操作
				.doOnNext(info->creatFilePath(info)) //创建文件夹
				.doOnNext(info->generateReceiverByTempl(info)) //生成基础文件
				.doOnNext(info->creatConfFile(info)) //生成pom文件
				.doOnNext(info->creatMvnComdFile(info.getReceiverObjPath(),info.getReceiverMvnCom(),"/templates/receiver/mvncmd.vm"))//生成命令文件
				.flatMap(info->{return logManagerService.findByLogManageId(info.getId())
						.flatMap(logFile->LogBeanEntityTransformBean(info,logFile))
						.doOnNext(logFile->generateReceiverDaoByTempl(info,logFile)).collectList()
						.doOnNext(logFileList->generateServiceAndSinkByTempl(info,logFileList));})//获取日志对象实体
				.doOnNext(logFileList->getLogFieldBean(logFileList))//将日志对象字段封装进日志对象中，生成所有实体类
				.flatMap(info-> Mono.just(new ResInfoBean(0,"creat Receiver file is ok",info)))
				.onErrorResume(e-> Mono.just(new ResInfoBean(1,"creat Receiver file is error ! :["+e.getMessage()+"]",new LogFileBean())));
	}
	
	/**
	 * 数据创建文件路径
	 * @param logFileBean
	 * @return
	 */
	private void creatFilePath(LogManagerBean logFileBean){
		//创建代码路径
		if(!util.creatFilePathByStr(logFileBean.getReceiverServicePath())) throw new RuntimeException("Can't create dir");
		if(!util.creatFilePathByStr(logFileBean.getReceiverBeanPath())) throw new RuntimeException("Can't create dir");
		if(!util.creatFilePathByStr(logFileBean.getReceiverDaoPath())) throw new RuntimeException("Can't create dir");
		if(!util.creatFilePathByStr(logFileBean.getReceiverMsgPath())) throw new RuntimeException("Can't create dir");
		if(!util.creatFilePathByStr(logFileBean.getReceiverWebPath())) throw new RuntimeException("Can't create dir");
		//创建生成文件的路径
		if(!util.creatFilePathByStr(logFileBean.getReceiverTargetPath())) throw new RuntimeException("Can't create dir");
		//创建配置文件路径
		if(!util.creatFilePathByStr(logFileBean.getReceiverResourcesPath())) throw new RuntimeException("Can't create dir");
	}
	
	/**
	 * 生成一些配置文件文件
	 * @param logFileBean
	 */
	private void creatConfFile(LogManagerBean logFileBean){
		//生成pom文件
		VelocityContext _pomContext=new VelocityContext();
		_pomContext.put("codeGroupId", logFileBean.getReceiverCodeGroupId());
		_pomContext.put("codeArtifactId", logFileBean.getReceiverCodeArtifactId());
		_pomContext.put("codeVersion", logFileBean.getReceiverCodeVersion());
		_pomContext.put("objName", logFileBean.getReceiverObjName());
		_pomContext.put("receiverDockerHost", logFileBean.getReceiverDockerHost());
		File pomFile=new File(logFileBean.getReceiverObjPath(),"pom.xml");
		
		//生成Dockerfile文件
		VelocityContext _dockerFileContext=new VelocityContext();
		_dockerFileContext.put("receiverCodeArtifactId", logFileBean.getReceiverCodeArtifactId());
		_dockerFileContext.put("receiverCodeVersion", logFileBean.getReceiverCodeVersion());
		_dockerFileContext.put("receiverServerPort", logFileBean.getReceiverServerPort());
		File dockerFile=new File(logFileBean.getReceiverResourcesPath(),"Dockerfile");
		
		//生成application.properties文件
		VelocityContext _applicationpropertContext=new VelocityContext();
		_applicationpropertContext.put("receiverServerPort", logFileBean.getReceiverServerPort());
		_applicationpropertContext.put("receiverServerName", logFileBean.getReceiverServerName());
		_applicationpropertContext.put("receiverDbUrl", logFileBean.getReceiverDbUrl());
		_applicationpropertContext.put("receiverRabbitmqHost", logFileBean.getReceiverRabbitmqHost());
		_applicationpropertContext.put("receiverRabbitmqPort", logFileBean.getReceiverRabbitmqPort());
		_applicationpropertContext.put("receiverRabbitmqUsername", logFileBean.getReceiverRabbitmqUsername());
		_applicationpropertContext.put("receiverRabbitmqPassword", logFileBean.getReceiverRabbitmqPassword());
		_applicationpropertContext.put("receiverChannelName", logFileBean.getReceiverChannelName());
		_applicationpropertContext.put("baseMsgChannelName", logFileBean.getBaseChannelName());
		File applicationpropertFile=new File(logFileBean.getReceiverResourcesPath(),"application.properties");
		try {
			creatFile(_pomContext,"/templates/receiver/pom.vm",pomFile);
			creatFile(_dockerFileContext,"/templates/receiver/dockerFile.vm",dockerFile);
			creatFile(_applicationpropertContext,"/templates/receiver/application.properties.vm",applicationpropertFile);
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/**
	 * 生成简单的基础文件
	 * @param logManagerBean
	 */
	private void generateReceiverByTempl(LogManagerBean logManagerBean){
		VelocityContext _Appcontext=new VelocityContext();
		_Appcontext.put("packageName",logManagerBean.getReceiverBasePackage());
		_Appcontext.put("receiverObjName",logManagerBean.getReceiverObjName());
		File appFile=new File(logManagerBean.getReceiverBasePath(),logManagerBean.getReceiverObjName()+"Application.java");
		VelocityContext _Sourcecontext=new VelocityContext();
		_Sourcecontext.put("packageName",logManagerBean.getReceiverMsgPackage());
		_Sourcecontext.put("channelName",logManagerBean.getReceiverChannelName());
		File sourceFile=new File(logManagerBean.getReceiverMsgPath(),"SaveInfoSource.java");
		try {
			creatFile(_Appcontext,"/templates/receiver/ReceiverApplicationClass.vm",appFile);
			creatFile(_Sourcecontext,"/templates/receiver/ReceiverSourceClass.vm",sourceFile);
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}
	
	
	
	/**
	 * 根据实体类逐个生成dao文件
	 * @param logManagerBean
	 * @param logFileBean
	 */
	private void generateReceiverDaoByTempl(LogManagerBean logManagerBean,LogFileBean logFileBean){
		if(logFileBean.isBaseBean()) return;
		VelocityContext _context=new VelocityContext();
		_context.put("packageName",logManagerBean.getReceiverDaoPackage());
		_context.put("beanPackage",logFileBean.getReceiverBeanPackage());
		_context.put("beanName",logFileBean.getBeanName());
		File targetFile=new File(logManagerBean.getReceiverDaoPath(),logFileBean.getBeanName()+"Repository.java");
		try {
			creatFile(_context,"/templates/receiver/ReceiverDaoClass.vm",targetFile);
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/**
	 * 根据实体类生成对应的service文件和sink文件
	 * @param logManagerBean
	 * @param logFileBeans
	 */
	private void generateServiceAndSinkByTempl(LogManagerBean logManagerBean,List<LogFileBean> logFileBeans){
		//排除基类
		List<LogFileBean> tmplogFileBeans=new ArrayList<LogFileBean>();
		for(LogFileBean logFileBean:logFileBeans){
			if(!logFileBean.isBaseBean()) tmplogFileBeans.add(logFileBean);
		}
		//对应服务类生成
		VelocityContext _Servicecontext=new VelocityContext();
		_Servicecontext.put("packageName",logManagerBean.getReceiverServicePackage());
		_Servicecontext.put("basePackage",logManagerBean.getReceiverBasePackage());
		_Servicecontext.put("receiverObjName",logManagerBean.getReceiverObjName());
		_Servicecontext.put("daoPackage",logManagerBean.getReceiverDaoPackage());
		_Servicecontext.put("logFiles",tmplogFileBeans);
		File serviceFile=new File(logManagerBean.getReceiverServicePath(),"SaveInfoService.java");
		//接受消息的类生成
		VelocityContext _Sinkcontext=new VelocityContext();
		_Sinkcontext.put("packageName",logManagerBean.getReceiverMsgPackage());
		_Sinkcontext.put("basePackage",logManagerBean.getReceiverBasePackage());
		_Sinkcontext.put("receiverObjName",logManagerBean.getReceiverObjName());
		_Sinkcontext.put("servicePackage",logManagerBean.getReceiverServicePackage());
		_Sinkcontext.put("logFiles",tmplogFileBeans);
		File sinkFile=new File(logManagerBean.getReceiverMsgPath(),"SinkReceiver.java");
		//页面处理handler生成
		VelocityContext _Handlercontext=new VelocityContext();
		_Handlercontext.put("packageName",logManagerBean.getReceiverWebPackage());
		_Handlercontext.put("servicePackage",logManagerBean.getReceiverServicePackage());
		_Handlercontext.put("logFiles",tmplogFileBeans);
		File handlerFile=new File(logManagerBean.getReceiverWebPath(),"LogInfoHandler.java");
		//页面路由类生成
		VelocityContext _Routercontext=new VelocityContext();
		_Routercontext.put("packageName",logManagerBean.getReceiverWebPackage());
		_Routercontext.put("logFiles",tmplogFileBeans);
		File routerconFile=new File(logManagerBean.getReceiverWebPath(),"ReceiverRouterConfig.java");
		try {
			creatFile(_Servicecontext,"/templates/receiver/ReceiverServiceClass.vm",serviceFile);
			creatFile(_Sinkcontext,"/templates/receiver/ReceiverSinkClass.vm",sinkFile);
			creatFile(_Handlercontext,"/templates/receiver/ReceiverHandlerClass.vm",handlerFile);
			creatFile(_Routercontext,"/templates/receiver/ReceiverRouterClass.vm",routerconFile);
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
		logger.info("ReceivercreatLogBean: size ["+logFileBeans.size()+"]");
		for(LogFileBean logFileBean:logFileBeans){
			logManagerService.findFieldsByLogBeanId(logFileBean.getId())
			.flatMap(logField->logFieldEntityTransformBean(logField))
			.collectList().subscribe(logFields->{
				logger.info("Receiver logFields: size ["+logFields.size()+"]");
				logFileBean.setLogFieldList(logFields);
				generateLogFile(logFileBean,"/templates/receiver/ReceiverBeanClass.vm");
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
		logContext.put("receiverBeanPackage",logFileBean.getReceiverBeanPackage());
		logContext.put("className",logFileBean.getBeanName());
		logContext.put("beanDescribe",logFileBean.getBeanDescribe());
		if(!logFileBean.isBaseBean())
			logContext.put("father",logFileBean.getFatherBeanName());
		
		File targetLogFile=new File(logFileBean.getReceiverBeanPath(),logFileBean.getBeanName()+".java");
		logContext.put("fields",logFileBean.getLogFieldList());
		try {
			creatFile(logContext,vmstr,targetLogFile);
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/**
	 * 运行mvn操作命令
	 * @param logManageEntity
	 * @return
	 */
	public Mono<ResInfoBean> runCom(LogManageMongoEntity logManageEntity){
		return logManagerEntityTransformBean(logManageEntity)
			   .doOnNext(info->runMvncom(info.getReceiverMvnCom(),info.getReceiverObjPath()))
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
					File _srcDist=new File(info.getReceiverObjPath()+makeLogProperties.getPathStep()+info.getReceiverMvnCom());
					Mono<ResInfoBean> resInfoBean=null;
					if(_srcDist.exists())
						resInfoBean=Mono.just(new ResInfoBean(0,"canCom is ok",info));
					else
						resInfoBean=Mono.just(new ResInfoBean(1,"canCom is not",info));
					return resInfoBean;
				});
	}
}
