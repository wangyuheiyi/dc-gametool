package com.dc.gametoollog.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import reactor.core.publisher.Mono;

import com.dc.gametoollog.DcGametoollogApplication;
import com.dc.gametoollog.bean.LogFieldBean;
import com.dc.gametoollog.bean.LogFileBean;
import com.dc.gametoollog.bean.LogManagerBean;
import com.dc.gametoollog.conf.MakeLogProperties;
import com.dc.gametoollog.entity.LogBeanMongoEntity;
import com.dc.gametoollog.entity.LogFieldMongoEntity;
import com.dc.gametoollog.entity.LogManageMongoEntity;
import com.dc.gametoollog.util.LogUtil;

/**
 * 服务基类
 * @author Administrator
 *
 */
public class BaseFileService {
	protected static Logger logger=LoggerFactory.getLogger(DcGametoollogApplication.class);
	@Autowired
	protected MakeLogProperties makeLogProperties;
	
	@Autowired
	protected LogManagerService logManagerService;
	
	@Autowired
	protected LogUtil util;
	
	
	/**
	 * 数据前期初始化准备的流
	 * @param logManageEntity
	 * @return
	 */
	protected Mono<LogManagerBean> logManagerEntityTransformBean(LogManageMongoEntity logManageEntity){
		LogManagerBean logFileBean=new LogManagerBean();
		logFileBean.setId(logManageEntity.getId());
		logFileBean.setBaseLogClassName(logManageEntity.getBaseLogClassName());
		logFileBean.setBaseChannelName(logManageEntity.getBaseMsgChannelName());
		logFileBean.setBaseFilePath(makeLogProperties.getOutPutPath()+makeLogProperties.getPathStep()+logManageEntity.getGamecode()+makeLogProperties.getPathStep()+logManageEntity.getLogservicename());
		///////////////////发送服数据设置/////////////////////////
		logFileBean.setSendObjName(logManageEntity.getSendObjName());
		logFileBean.setSendServicePackage(logManageEntity.getSendBasePackage()+"."+makeLogProperties.getServicePackage());
		logFileBean.setSendBeanPackage(logManageEntity.getSendBasePackage()+"."+makeLogProperties.getBeanPackage());
		logFileBean.setSendObjPath(logFileBean.getBaseFilePath()+makeLogProperties.getPathStep()+logFileBean.getSendObjName());
		logFileBean.setSendServicePath(logFileBean.getSendObjPath()
				+makeLogProperties.getMainSrc()+makeLogProperties.getPathStep()+logFileBean.getSendServicePackage().replaceAll("\\.","/"));
		logFileBean.setSendBeanPath(logFileBean.getSendObjPath()
				+makeLogProperties.getMainSrc()+makeLogProperties.getPathStep()+logFileBean.getSendBeanPackage().replaceAll("\\.","/"));
		logFileBean.setSendTargetPath(logFileBean.getSendObjPath()+makeLogProperties.getTargetPath());
		logFileBean.setSendChannelName(logManageEntity.getChannelName());
		logFileBean.setSendMvnCom(logManageEntity.getMvnCom());
		//maven数据
		logFileBean.setSendCodeVersion(logManageEntity.getCodeVersion());
		logFileBean.setSendCodeGroupId(logManageEntity.getCodeGroupId());
		logFileBean.setSendCodeArtifactId(logManageEntity.getCodeArtifactId());
		logFileBean.setMavenId(logManageEntity.getMavenId());
		logFileBean.setReleaseMavenPath(logManageEntity.getReleaseMavenPath());
		logFileBean.setSnapshotMavenPath(logManageEntity.getSnapshotMavenPath());
		///////////////////接收服数据设置/////////////////////////
		logFileBean.setReceiverObjName(logManageEntity.getReceiverObjName());
		logFileBean.setReceiverObjPath(logFileBean.getBaseFilePath()+makeLogProperties.getPathStep()+logFileBean.getReceiverObjName());
		logFileBean.setReceiverServerName(logManageEntity.getReceiverServerName());
		logFileBean.setReceiverServerPort(logManageEntity.getReceiverServerPort());
		logFileBean.setReceiverChannelName(logManageEntity.getReceiverChannelName());
		logFileBean.setReceiverMvnCom(logManageEntity.getReceiverMvnCom());
		logFileBean.setReceiverBasePackage(logManageEntity.getReceiverBasePackage());
		logFileBean.setReceiverServicePackage(logManageEntity.getReceiverBasePackage()+"."+makeLogProperties.getServicePackage());
		logFileBean.setReceiverBeanPackage(logManageEntity.getReceiverBasePackage()+"."+makeLogProperties.getBeanPackage());
		logFileBean.setReceiverDaoPackage(logManageEntity.getReceiverBasePackage()+"."+makeLogProperties.getDaoPackage());
		logFileBean.setReceiverMsgPackage(logManageEntity.getReceiverBasePackage()+"."+makeLogProperties.getMsgPackage());
		logFileBean.setReceiverWebPackage(logManageEntity.getReceiverBasePackage()+"."+makeLogProperties.getWebPackage());
		logFileBean.setReceiverBasePath(logFileBean.getReceiverObjPath()
				+makeLogProperties.getMainSrc()+makeLogProperties.getPathStep()+logFileBean.getReceiverBasePackage().replaceAll("\\.","/"));
		logFileBean.setReceiverServicePath(logFileBean.getReceiverObjPath()
				+makeLogProperties.getMainSrc()+makeLogProperties.getPathStep()+logFileBean.getReceiverServicePackage().replaceAll("\\.","/"));
		logFileBean.setReceiverBeanPath(logFileBean.getReceiverObjPath()
				+makeLogProperties.getMainSrc()+makeLogProperties.getPathStep()+logFileBean.getReceiverBeanPackage().replaceAll("\\.","/"));
		logFileBean.setReceiverDaoPath(logFileBean.getReceiverObjPath()
				+makeLogProperties.getMainSrc()+makeLogProperties.getPathStep()+logFileBean.getReceiverDaoPackage().replaceAll("\\.","/"));
		logFileBean.setReceiverMsgPath(logFileBean.getReceiverObjPath()
				+makeLogProperties.getMainSrc()+makeLogProperties.getPathStep()+logFileBean.getReceiverMsgPackage().replaceAll("\\.","/"));
		logFileBean.setReceiverWebPath(logFileBean.getReceiverObjPath()
				+makeLogProperties.getMainSrc()+makeLogProperties.getPathStep()+logFileBean.getReceiverWebPackage().replaceAll("\\.","/"));
		logFileBean.setReceiverTargetPath(logFileBean.getReceiverObjPath()+makeLogProperties.getTargetPath());
		logFileBean.setReceiverResourcesPath(logFileBean.getReceiverObjPath()+makeLogProperties.getMainResources());
		logFileBean.setReceiverCodeVersion(logManageEntity.getReceiverCodeVersion());
		logFileBean.setReceiverCodeGroupId(logManageEntity.getReceiverCodeGroupId());
		logFileBean.setReceiverCodeArtifactId(logManageEntity.getReceiverCodeArtifactId());
		logFileBean.setReceiverDbUrl(logManageEntity.getReceiverDbUrl());
		logFileBean.setReceiverRabbitmqHost(logManageEntity.getReceiverRabbitmqHost());
		logFileBean.setReceiverRabbitmqPort(logManageEntity.getReceiverRabbitmqPort());
		logFileBean.setReceiverRabbitmqUsername(logManageEntity.getReceiverRabbitmqUsername());
		logFileBean.setReceiverRabbitmqPassword(logManageEntity.getReceiverRabbitmqPassword());
		logFileBean.setReceiverDockerHost(logManageEntity.getReceiverDockerHost());
		logFileBean.setReceiverSearchHost(logManageEntity.getReceiverSearchHost());
		return Mono.just(logFileBean);
	}
	
	/**
	 * 类实体数据库对象转换成生成文件用对象
	 * @param logBeanMongoEntity
	 * @param logFields
	 * @return
	 */
	protected Mono<LogFileBean> LogBeanEntityTransformBean(LogManagerBean logManagerBean,LogBeanMongoEntity logBeanMongoEntity){
		LogFileBean logFileBean=new LogFileBean();
		logFileBean.setId(logBeanMongoEntity.getId());
		logFileBean.setBeanPackage(logManagerBean.getSendBeanPackage());
		logFileBean.setBeanPath(logManagerBean.getSendBeanPath());
		logFileBean.setReceiverBeanPackage(logManagerBean.getReceiverBeanPackage());
		logFileBean.setReceiverBeanPath(logManagerBean.getReceiverBeanPath());
		logFileBean.setBeanName(logBeanMongoEntity.getBeanName());
		logFileBean.setBeanDescribe(logBeanMongoEntity.getBeanDescribe());
		logFileBean.setFatherBeanName(logBeanMongoEntity.getFatherBeanName());
		if(logBeanMongoEntity.getIsBaseBean()!=null&&"1".equals(logBeanMongoEntity.getIsBaseBean()))
		{
			logFileBean.setBaseBean(true);
		}else{
			logFileBean.setBaseBean(false);
		}
		return Mono.just(logFileBean);
	}
	
	/**
	 * 字段数据实体转换成生成文件所需要的bean
	 * @param logField
	 * @return
	 */
	protected Mono<LogFieldBean> logFieldEntityTransformBean(LogFieldMongoEntity logField){
		LogFieldBean logBeanField=new LogFieldBean();
		logBeanField.setFieldType(logField.getFieldType());
		logBeanField.setFieldName(logField.getFieldName());
		logBeanField.setBigName(logField.getBigName());
		logBeanField.setComment(logField.getComment());
		return Mono.just(logBeanField);
	}
	
	/**
	 * 创建文件
	 * @param _context
	 * @param templPath
	 * @param targetFile
	 * @throws IOException 
	 */
	protected void creatFile(VelocityContext _context,String templPath,File targetFile) throws IOException{
		StringWriter _readWriter=new StringWriter();
		Velocity.setProperty("resource.loader", "class");
		Velocity.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		Velocity.mergeTemplate(templPath,"UTF-8",_context,_readWriter);
		Writer _fileWriter=null;
		try{
			_fileWriter = new OutputStreamWriter(new FileOutputStream(targetFile),"UTF-8");
			_fileWriter.write(_readWriter.toString());
		}catch(IOException e){
			throw new IOException(e.getMessage());
		}finally{
			if(_fileWriter!=null) _fileWriter.close();
		}
	}
	
	/**
	 * 生成maven运行文件
	 * @param logFileBean
	 */
	protected void creatMvnComdFile(String outputPath,String fileName,String str){
		File mvnComFile=new File(outputPath,fileName);
		VelocityContext _mvnComContext=new VelocityContext();
		try {
			creatFile(_mvnComContext,str,mvnComFile);
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/**
	 * 执行命令语句
	 * @param logFileBean
	 * @return
	 */
	protected void runMvncom(String mvnCom,String objPath){
		 logger.info("mvnCom=========["+mvnCom+"],objPath============["+objPath+"]");
		logger.info("start mvncommond");
		if(util.isStrNullOrEmpty(mvnCom)){
			throw new RuntimeException("commond file is null");
		}
		Process ps=null;
		BufferedReader br =null;
		try { 
			//去项目的指定目录执行命令
			File dir = new File(objPath);
			ps = Runtime.getRuntime().exec("chmod -R 777 "+objPath+makeLogProperties.getPathStep()+mvnCom);
            ps = Runtime.getRuntime().exec(objPath+makeLogProperties.getPathStep()+mvnCom,null,dir);
            br = new BufferedReader(new InputStreamReader(ps.getInputStream()));  
            StringBuffer sb = new StringBuffer();  
            String line;  
            while ((line = br.readLine()) != null) {  
                sb.append(line).append("\n");  
            }  
            String result = sb.toString();  
            logger.info("mvncmd=========["+result+"]");
            }   
        catch (Exception e) {  
        	logger.error("runMvncom error :"+e.getMessage());
            throw new RuntimeException("mvnCom error"+e.getMessage());
        }finally{
        	try {
				br.close();
			} catch (IOException e) {
				logger.error("runMvncom error :"+e.getMessage());
				throw new RuntimeException("mvnCom error"+e.getMessage());
			}
        }
	}
}
