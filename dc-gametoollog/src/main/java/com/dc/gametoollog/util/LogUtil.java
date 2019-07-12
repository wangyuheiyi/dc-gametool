package com.dc.gametoollog.util;

import java.io.File;

import org.springframework.stereotype.Component;

@Component
public class LogUtil {
	/**
	 * 创建文件路径
	 * @param path
	 * @return
	 */
	public boolean creatFilePathByStr(String path){
		File _srcDist=new File(path);
		if(!_srcDist.exists())
			if(!_srcDist.mkdirs())	return false;
		return true;
	}
	
	/**
	 * 判断字符串是否为空
	 * @param str
	 * @return
	 */
	public boolean isStrNullOrEmpty(String str){
		if(str==null||"".equals(str)) return true;
		return false;
	}
}
