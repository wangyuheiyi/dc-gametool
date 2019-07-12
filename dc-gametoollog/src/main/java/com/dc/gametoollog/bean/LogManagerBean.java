package com.dc.gametoollog.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogManagerBean {
	/** 日志管理id*/
	private String id;
	/** 消息信道*/
	private String baseChannelName;
	
	/** 项目文件根目录地址*/
	private String baseFilePath;
	
	//////////////////发送服信息///////////////////////
	/** 发送服项目名称*/
	private String sendObjName;
	/** 发送服项目路径*/
	private String sendObjPath;
	/** 发送服服务命名空间*/
	private String sendServicePackage;
	/** 发送服对象命名空间*/
	private String sendBeanPackage;
	/** 发送服务代码路径*/
	private String sendServicePath;
	/** 发送服对象代码路径*/
	private String sendBeanPath;
	/** 发送服生成路径路径*/
	private String sendTargetPath;
	/** 默认基类的名称*/
	private String baseLogClassName;
	/** 发送服消息同步渠道名*/
	private String sendChannelName;
	/** 发送服执行的命令地址*/
	private String sendMvnCom;
	/** 发送服代码版本*/
	private String sendCodeVersion;
	/** 发送服代码版本*/
	private String sendCodeGroupId;
	/** 发送服代码版本*/
	private String sendCodeArtifactId;
	/** mavenId 要和配置文件中的一致*/
	private String mavenId;
	/** release版本上传路径*/
	private String releaseMavenPath;
	/** snapshot版本上传路径*/
	private String snapshotMavenPath;
	//////////////////接收服信息///////////////////////
	/** 接收服项目名称*/
	private String receiverObjName;
	/** 接收服根命名空间*/
	private String receiverBasePackage;
	/** 接收服项目路径*/
	private String receiverObjPath;
	/** 服务名称*/
	private String receiverServerName;
	/** 服务端口*/
	private int receiverServerPort;
	/** 接收服日志消息流的通道名*/
	private String receiverChannelName;
	/** 接收服执行的命令文件*/
	private String receiverMvnCom;
	/** 接收服服务命名空间*/
	private String receiverServicePackage;
	/** 接收服对象命名空间*/
	private String receiverBeanPackage;
	/** 接收服dao命名空间*/
	private String receiverDaoPackage;
	/** 接收服msg命名空间*/
	private String receiverMsgPackage;
	/** 接收服web服务命名空间*/
	private String receiverWebPackage;
	/** 接收服根代码路径*/
	private String receiverBasePath;
	/** 接收服务代码路径*/
	private String receiverServicePath;
	/** 接收服对象代码路径*/
	private String receiverBeanPath;
	/** 接收服dao代码路径*/
	private String receiverDaoPath;
	/** 接收服msg代码路径*/
	private String receiverMsgPath;
	/** 接收服web服务代码路径*/
	private String receiverWebPath;
	/** 接收服生成路径路径*/
	private String receiverTargetPath;
	/** 接收服生成路径路径*/
	private String receiverResourcesPath;
	/** 代码版本*/
	private String receiverCodeVersion;
	/** 代码GroupId*/
	private String receiverCodeGroupId;
	/** 代码ArtifactId*/
	private String receiverCodeArtifactId;
	/** 数据库地址*/
	private String receiverDbUrl;
	/** Rabbit地址*/
	private String receiverRabbitmqHost;
	/** Rabbit端口*/
	private String receiverRabbitmqPort;
	/** Rabbit用户名*/
	private String receiverRabbitmqUsername;
	/** Rabbit密码*/
	private String receiverRabbitmqPassword;
	/**项目docker镜像上传的地址*/
	private String receiverDockerHost;
	/**接受服务查询地址*/
	private String receiverSearchHost;
	@Override
	public String toString(){
		return "sendObjName:["+sendObjName+"],sendObjPath:["+sendObjPath+"],sendServicePackage:["+sendServicePackage
				+"],sendBeanPackage:["+sendBeanPackage+"],sendTargetPath:["+sendTargetPath+"],"
				+"receiverObjName:["+receiverObjPath+"],receiverServerName:["+receiverServerName+"],receiverServerPort:["+receiverServerPort+"],"
				+"receiverServicePackage:["+receiverServicePackage+"]";
	}
	
}
