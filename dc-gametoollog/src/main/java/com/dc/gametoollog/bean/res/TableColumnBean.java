package com.dc.gametoollog.bean.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableColumnBean {
	/** 表头*/
	private String title;
	/** 数据关联字段*/
	private String key;
}
