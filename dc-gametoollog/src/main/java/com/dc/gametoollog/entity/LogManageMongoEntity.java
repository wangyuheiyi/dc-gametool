package com.dc.gametoollog.entity;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 日志总体管理类（各种动态信息填写）mongoDB对应
 * @author Administrator
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "logmanage")
public class LogManageMongoEntity {
	@Id
    private String id;
	
	/** 游戏服代码*/
	@Indexed(unique = true)
	private int gamecode;
	
	/** 游戏名称*/
	private String gamename;

	/** 日志服名称*/
	private String logservicename;
	
	/** 默认基类的名称*/
	private String baseLogClassName;
	/** 消息接收的公共通道*/
	private String baseMsgChannelName;
	/////////////////发送服务信息///////////////////
	/** 发送服务项目名称*/
	private String sendObjName;
	/** 发送服根命名空间*/
	private String sendBasePackage;
	
	/** 日志消息流的通道名*/
	private String channelName;
	/** 执行的命令文件*/
	private String mvnCom;
	/////////////////发送服务版本信息///////////////////
	/** 代码版本*/
	private String codeVersion;
	/** 代码GroupId*/
	private String codeGroupId;
	/** 代码ArtifactId*/
	private String codeArtifactId;
	/** mavenId 要和配置文件中的一致*/
	private String mavenId;
	/** release版本上传路径*/
	private String releaseMavenPath;
	/** snapshot版本上传路径*/
	private String snapshotMavenPath;
	
	/////////////////接收服务信息///////////////////
	/** 接收服务项目名称*/
	private String receiverObjName;
	/** 接收服根命名空间*/
	private String receiverBasePackage;
	/** 接收服日志消息流的通道名*/
	private String receiverChannelName;
	/** 接收服执行的命令文件*/
	private String receiverMvnCom;
	/** 服务名称*/
	private String receiverServerName;
	/** 服务端口*/
	private int receiverServerPort;
	/////////////////接收服务版本信息///////////////////
	/** 代码版本*/
	private String receiverCodeVersion;
	/** 代码GroupId*/
	private String receiverCodeGroupId;
	/** 代码ArtifactId*/
	private String receiverCodeArtifactId;
	/////////////////接收服务数据库信息///////////////////
	/** 数据库地址*/
	private String receiverDbUrl;
	/////////////////接收服务消息信息///////////////////
	/** Rabbit地址*/
	private String receiverRabbitmqHost;
	/** Rabbit端口*/
	private String receiverRabbitmqPort;
	/** Rabbit用户名*/
	private String receiverRabbitmqUsername;
	/** Rabbit密码*/
	private String receiverRabbitmqPassword;
	/////////////////docker上传地址///////////////////
	/**项目docker镜像上传的地址*/
	private String receiverDockerHost;
	/**接受服务查询地址*/
	private String receiverSearchHost;
	@Override
	public String toString(){
		return "id:["+id+"],gamecode:["+gamecode+"],gamename["+gamename+"],logservicename["+logservicename+"]";
	}
}
