package com.dc.gametoollog.entity;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 日志总体管理类（各种动态信息填写）mongoDB对应
 * @author Administrator
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "logbean")
@CompoundIndexes({
    //name：索引名称 def：字段(1正序 -1倒序) unique：是否唯一索引
	//直接加到字段上面没用
    @CompoundIndex(name = "uq_id_name", def = "{logManageId:1, beanName:-1}", unique = true)
})
public class LogBeanMongoEntity {
	@Id
    private String id;
	
	/** 外键关联*/
	private String logManageId;
	
	/** 实体类名成*/
	private String beanName;
	
	/** 实体类描述*/
	private String beanDescribe;
	
	/** 基类名称*/
	private String fatherBeanName;
	
	/** 是否是基类*/
	private String isBaseBean;
	
	@Override
	public String toString(){
		return "id:["+id+"],beanName:["+beanName+"],beanDescribe["+beanDescribe+"],fatherBeanName["+fatherBeanName+"],isBaseBean["+isBaseBean+"]";
	}
}
