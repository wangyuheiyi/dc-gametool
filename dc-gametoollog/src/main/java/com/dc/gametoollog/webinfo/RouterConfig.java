package com.dc.gametoollog.webinfo;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterConfig {
	@Autowired
    private VelocityHandler velocityHandler;
	
	 @Bean
     public RouterFunction<ServerResponse> timerRouter() {
		 return route(GET("/init").and(accept(MediaType.APPLICATION_JSON)), velocityHandler::initInfo)
				 .andRoute(POST("/searchColumnInfo").and(accept(MediaType.APPLICATION_JSON)), velocityHandler::searchColumnInfo)
				 .andRoute(POST("/searchTableInfo").and(accept(MediaType.APPLICATION_JSON)), velocityHandler::searchTableInfo)
				 .andRoute(POST("/creatSendFile").and(accept(MediaType.APPLICATION_JSON)), velocityHandler::creatSendFile)
				 .andRoute(POST("/creatReceiverFile").and(accept(MediaType.APPLICATION_JSON)), velocityHandler::creatReceiverFile)
				 //mongo数据库处理
				 //logmanager数据信息操作
				 .andRoute(GET("/findByGameCodeMongo/{gameCode}").and(accept(MediaType.APPLICATION_JSON)), velocityHandler::getInfoByGameCodeMongo)
				 .andRoute(POST("/deleteLogManager").and(accept(MediaType.APPLICATION_JSON)), velocityHandler::deleteLogManager)
				 .andRoute(POST("/saveLogManager").and(accept(MediaType.APPLICATION_JSON)), velocityHandler::saveLogManager)
				 .andRoute(POST("/findBylogManage/").and(accept(MediaType.APPLICATION_JSON)), velocityHandler::getLogBeanByManage)
				 //logBean数据信息处理
				 .andRoute(GET("/findBylogBean/{logManageId}").and(accept(MediaType.APPLICATION_JSON)), velocityHandler::getLogBeanByManageId)
				 .andRoute(POST("/saveLogBean").and(accept(MediaType.APPLICATION_JSON)), velocityHandler::saveLogBean)
				 .andRoute(POST("/deleteLogBean").and(accept(MediaType.APPLICATION_JSON)), velocityHandler::deleteLogBean)
				 //logfield数据信息处理
				 .andRoute(GET("/findBylogField/{logBeanId}").and(accept(MediaType.APPLICATION_JSON)), velocityHandler::getLogFieldByBeanId)
				 .andRoute(POST("/saveLogField").and(accept(MediaType.APPLICATION_JSON)), velocityHandler::saveLogField)
				 .andRoute(POST("/deleteLogField").and(accept(MediaType.APPLICATION_JSON)), velocityHandler::deleteLogField)
				 //发送服务自动打包上传操作
		 		 .andRoute(POST("/runMvnCom").and(accept(MediaType.APPLICATION_JSON)), velocityHandler::runMvnCom)
		 		 .andRoute(POST("/runReceiverMvnCom").and(accept(MediaType.APPLICATION_JSON)), velocityHandler::runReceiverMvnCom)
		 		 .andRoute(POST("/canMvnCom").and(accept(MediaType.APPLICATION_JSON)), velocityHandler::canMvnCom)
		 		 .andRoute(POST("/canReceiverMvnCom").and(accept(MediaType.APPLICATION_JSON)), velocityHandler::canReceiverMvnCom);
	 }
}
