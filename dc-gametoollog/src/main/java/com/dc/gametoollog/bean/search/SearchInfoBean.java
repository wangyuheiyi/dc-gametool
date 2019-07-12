package com.dc.gametoollog.bean.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 查询类的基础类
 * @author Administrator
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchInfoBean {
	/** 请求地址*/
	private String url;
	/** 请求方法*/
	private String path;
	/** 对象积累id*/
	private String baseBeanId;
	/** 查询关键参数*/
	private String param;
}
