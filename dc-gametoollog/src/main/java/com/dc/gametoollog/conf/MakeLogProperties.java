package com.dc.gametoollog.conf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "com.dc.logtool") 
@Component
public class MakeLogProperties {
	private String outPutPath;
	private String mainSrc;
	private String mainResources;
	private String pathStep;
	private String targetPath;
	private String servicePackage;
	private String beanPackage;
	private String daoPackage;
	private String msgPackage;
	private String webPackage;
}
