package com.dc.gametoollog.bean;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogFieldBean {
	/** 类型 */
	private String fieldType;
	/** 名字 */
	private String fieldName;
	/** 第一个字母大写的名字 */
	private String bigName;
	/** 注解 */
	private List<String> anotations;
	/** 注释 */
	private String comment;
}
