var gameCode=6;
var gameName='测试游戏';
//主方法
new Vue({
    el: '#managerInfo',
    data: {
    	panelvalue:"",
    	isCreat:false, //是否需要创建一个日志项目
    	isCreatLog:false, //是否可以创建日志服务文件
    	isCanRun:false, //是否可以执行命令
    	isReceiverCanRun:false, //是否可以执行命令
    	mvnLoading:false,//执行命令的加载
    	dataInfo:Object, //日志项目数据
    	tmpBeanIndex:1,
    	logBeans:[], //日志实体类数据
    	tmpFieldIndex:1,
    	logFields:[] //日志实体类字段数据
    },
    mounted:function(){
    	var url = "/findByGameCodeMongo/"+gameCode;
    	_this= this;
    	axios.get(url).then(function(result) {
    		reqAfterInfo(_this,result);
    	});
    },
    methods: {
    	creatNew: function () {
    		this.isCreat=false;
        },
        creatSendFile: function (){
        	var url = "/creatSendFile/";
        	_this= this;
        	axios.post(url,_this.dataInfo).then(function(result) {
        		var res=result.data;
        		if(res.status == "0"){
        			_this.$Message.success(res.resStr);
        			_this.isCanRun=true;
        		}else{
        			_this.$Message.error(res.resStr);
        			_this.isCanRun=false;
        		}
        	});
        },
        creatReceiverFile: function (){
        	var url = "/creatReceiverFile/";
        	_this= this;
        	axios.post(url,_this.dataInfo).then(function(result) {
        		var res=result.data;
        		if(res.status == "0"){
        			_this.$Message.success(res.resStr);
        			_this.isReceiverCanRun=true;
        		}else{
        			_this.$Message.error(res.resStr);
        			_this.isReceiverCanRun=false;
        		}
        	});
        },
        runMvnCom:function(){
        	this.mvnLoading=true;
        	var url = "/runMvnCom/";
        	_this= this;
        	axios.post(url,_this.dataInfo).then(function(result) {
        		console.log(result);
        		var res=result.data;
        		_this.mvnLoading=false;
        		if(res.status == "0"){
        			_this.$Message.success(res.resStr);
        		}else{
        			_this.$Message.error(res.resStr);
        		}
        	});
        },
        runReceiverMvnCom:function(){
        	this.mvnLoading=true;
        	var url = "/runReceiverMvnCom/";
        	_this= this;
        	axios.post(url,_this.dataInfo).then(function(result) {
        		console.log(result);
        		var res=result.data;
        		_this.mvnLoading=false;
        		if(res.status == "0"){
        			_this.$Message.success(res.resStr);
        		}else{
        			_this.$Message.error(res.resStr);
        		}
        	});
        },
        saveInfo: function () {
        	var url = "/saveLogManager/";
        	_this= this;
        	axios.post(url,_this.dataInfo).then(function(result) {
        		reqAfterInfo(_this,result);
        	});
        },
        deleteLogManager: function(){
        	var url = "/deleteLogManager/";
        	_this= this;
        	axios.post(url,_this.dataInfo).then(function(result) {
        		reqAfterInfo(_this,result);
        	});
        },
        collapseChange: function(key){
        	//清空记录
        	this.logFields= [];
        	//根据名字获取id
        	var tmpBeanId=null;
        	for(var i=0;i<this.logBeans.length;i++){
    			if(this.logBeans[i].beanName==key[0]) tmpBeanId=this.logBeans[i].id;
    		}
        	if(tmpBeanId!=null) getLogFieldInfo(this,tmpBeanId);
        },
        addlogBean: function (){
        	this.logBeans.push({
        		id: null,
        		logManageId: this.dataInfo.id,
        		beanName: "newbean"+this.tmpBeanIndex,
        		beanDescribe: "",
        		fatherBeanName: "",
        		isBaseBean: '0'
            });
        	this.tmpBeanIndex++;
        },
        saveLogBean: function(logBean){
        	var url = "/saveLogBean/";
        	_this= this;
        	axios.post(url,logBean).then(function(result) {
        		var res=result.data;
        		if(res.status == "0"){
        			_this.$Message.success(res.resStr);
        			for(var i=0;i<_this.logBeans.length;i++){
            			if(_this.logBeans[i].beanName==res.resDate.beanName){
            				Vue.set(_this.logBeans,i,res.resDate);
            				window.parent.addMenuList(res.resDate);
            			}
            		}
        		}else{
        			_this.$Message.error(res.resStr);
        		}
        	});
        },
        deleteLogBean: function(logBean){
        	if(logBean.id==null){
        		for(var i=0;i<_this.logBeans.length;i++){
        			if(_this.logBeans[i].beanName==logBean.beanName){
        				Vue.delete(_this.logBeans,i);
        			}
        		}
        	}else{
        		var url = "/deleteLogBean/";
            	_this= this;
            	axios.post(url,logBean).then(function(result) {
            		var res=result.data;
            		if(res.status == "0"){
            			_this.$Message.success(res.resStr);
            			for(var i=0;i<_this.logBeans.length;i++){
                			if(_this.logBeans[i].beanName==logBean.beanName){
                				window.parent.delMenuList(_this.logBeans[i]);
                				Vue.delete(_this.logBeans,i);
                			}
                		}
            		}else{
            			_this.$Message.error(res.resStr);
            		}
            	});
        	}
        	
        },
        addlogField: function(logBeanId){
        	this.logFields.push({
	        	id: null,
	        	logBeanId:logBeanId,
	        	fieldType:"",
	        	fieldName:"newfield"+ this.tmpFieldIndex,
	        	bigName:"Newfield"+ this.tmpFieldIndex,
	        	comment:""
        	});
        	this.tmpFieldIndex++;
        },
        saveLogField: function(logField){
        	var url = "/saveLogField/";
          	_this= this;
          	axios.post(url,logField).then(function(result) {
          		var res=result.data;
          		if(res.status == "0"){
          			_this.$Message.success(res.resStr);
          			for(var i=0;i<_this.logFields.length;i++){
              			if(_this.logFields[i].fieldName==res.resDate.fieldName)
              				Vue.set(_this.logFields,i,res.resDate);
              		}
          		}else{
          			_this.$Message.error(res.resStr);
          		}
          	});
        },
        deleteLogField: function(logField){
        	if(logField.id==null){
        		for(var i=0;i<_this.logFields.length;i++){
        			if(_this.logFields[i].fieldName==logField.fieldName)
        				Vue.delete(_this.logFields,i);
        		}
        	}else{
        		var url = "/deleteLogField/";
            	_this= this;
            	axios.post(url,logField).then(function(result) {
            		var res=result.data;
            		if(res.status == "0"){
            			_this.$Message.success(res.resStr);
            			for(var i=0;i<_this.logFields.length;i++){
                			if(_this.logFields[i].fieldName==logField.fieldName)
                				Vue.delete(_this.logFields,i);
                		}
            		}else{
            			_this.$Message.error(res.resStr);
            		}
            	});
        	}
        }
    }
});

//主信息请求后的操作
function reqAfterInfo(_this,result){
	 var res=result.data;
	 if(res.status == "0"){
		_this.$Message.success(res.resStr);
		_this.dataInfo = res.resDate;
		if(_this.dataInfo.id==null){
			_this.isCreat=true;
			_this.isCreatLog=false;
			_this.dataInfo.gamecode=gameCode;
    		_this.dataInfo.gamename=gameName;
    		window.parent.setSearchUrl("");
		}
		else{
			_this.isCreat=false;
			_this.isCreatLog=true;
			checkIsCanMvnRun(_this);
			checkIsReceiverCanMvnRun(_this);
			getLogBeanByManage(_this);
			window.parent.setSearchUrl(_this.dataInfo.receiverSearchHost);
		}
	 }else{
		 _this.$Message.error(res.resStr);
	 }
}

function checkIsCanMvnRun(_this){
	var url = "/canMvnCom/";
	axios.post(url,_this.dataInfo).then(function(result) {
		 var res=result.data;
		 if(res.status == "0"){
			 _this.isCanRun=true;
		 }else{
			 _this.isCanRun=false;
		 }
	});
}

function checkIsReceiverCanMvnRun(_this){
	var url = "/canReceiverMvnCom/";
	axios.post(url,_this.dataInfo).then(function(result) {
		 var res=result.data;
		 if(res.status == "0"){
			 _this.isReceiverCanRun=true;
		 }else{
			 _this.isReceiverCanRun=false;
		 }
	});
}

//查询实体类
function getLogBeanInfo(_this,logManageId){
	_this.logBeans= [];
	var url = "/findBylogBean/"+logManageId;
	axios.get(url).then(function(result) {
		for(var i=0;i<result.data.length;i++){
			if(result.data[i].beanName!=null) _this.logBeans.push(result.data[i]);
		}
	});
}

//查询实体类
function getLogBeanByManage(_this){
	_this.logBeans= [];
	var url = "/findBylogManage/";
	axios.post(url,_this.dataInfo).then(function(result) {
		for(var i=0;i<result.data.length;i++){
			if(result.data[i].beanName!=null) _this.logBeans.push(result.data[i]);
			window.parent.addMenuList(result.data[i]);
		}
	});
}

//查询实体类的字段
function getLogFieldInfo(_this,logBeanId){
	var url = "/findBylogField/"+logBeanId;
	axios.get(url).then(function(result) {
		for(var i=0;i<result.data.length;i++){
			if(result.data[i].id!=null) _this.logFields.push(result.data[i]);
		}
	});
}