//主方法
var tableInfo=new Vue({
    el: '#tableInfo',
    data: {
    	columns:[],
    	datas:[],
    },
    mounted:function(){
    	window.parent.initHtmlInfo();
    },
    methods: {
    }
});



function searchColumnInfo(searchUrl,path,baseBeanId,logBeanId){
	var url = "/searchColumnInfo/";
	//设置查询条件
	var searchInfo=new Object;
	searchInfo.url=searchUrl;
	searchInfo.path="/get"+path;
	searchInfo.baseBeanId=baseBeanId;
	searchInfo.param=logBeanId;
	axios.post(url,searchInfo).then(function(result) {
		if(result){
			for(var i=0;i<result.data.length;i++){
				tableInfo.columns.push(result.data[i]);
			}
			searchTableInfo(searchInfo);
		}
	});
}

function searchTableInfo(searchInfo){
	var url = "/searchTableInfo/";
	axios.post(url,searchInfo).then(function(result) {
		if(result){
			for(var i=0;i<result.data.length;i++){
				tableInfo.datas.push(result.data[i]);
			}
		}
	});
}
