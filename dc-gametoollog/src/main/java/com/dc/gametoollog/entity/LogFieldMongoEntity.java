package com.dc.gametoollog.entity;

import org.springframework.data.annotation.Id;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

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
@Document(collection = "logfield")
@CompoundIndexes({
    //name：索引名称 def：字段(1正序 -1倒序) unique：是否唯一索引
	//直接加到字段上面没用
    @CompoundIndex(name = "uq_id_name", def = "{logBeanId:1, fieldName:-1}", unique = true)
})
public class LogFieldMongoEntity {
	@Id
    private String id;
	
	/** 外键关联对应实体类id*/
	private String logBeanId;
	
	/** 字段类型*/
	private String fieldType;
	
	/** 字段名称*/
	private String fieldName;
	
	/** 字段名字大写字母*/
	private String bigName;
	
	/** 字段注释*/
	private String comment;
	
	@Override
	public String toString(){
		return "id:["+id+"],logBeanId:["+logBeanId+"],fieldType["+fieldType+"],fieldName["+fieldName+"],bigName["+bigName+"]";
	}
}
