package com.dc.gametoollog.bean.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResInfoBean {
	/** 请求状态*/
	protected int status;
	/** 返回信息*/
	protected String resStr;
	/** 返回数据*/
	protected Object resDate;
}
