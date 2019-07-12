//主方法
var mainInfoApp=new Vue({
    el: '#mainInfo',
    data: {
    	isCollapsed: false,
    	isInitTable: false,
    	logBeans:[],
    	searchUrl:"",
    	baseBeanId:"",
    	searchInfo:Object, //查询条件
    	reportUrl:'index.html',
    	tmpPath:"",
    	tmpLogBeanId:"",
    },
    computed: {
    	rotateIcon:function() {
            return [
                'menu-icon',
                this.isCollapsed ? 'rotate-icon' : ''
            ];
        },
    	menuitemClasses:function() {
            return [
                'menu-item',
                this.isCollapsed ? 'collapsed-menu' : ''
            ]
        }
    },
    methods: {
        collapsedSider:function() {
        	this.$refs.side1.toggleCollapse();
        },
        jumptoPath:function(htmlPath){
        	this.reportUrl=htmlPath;
        },
    	searchLogInfo:function(path,logBeanId){
    		this.tmpPath=path;
    		this.tmpLogBeanId=logBeanId;
    		this.reportUrl='tableInfo.html';
    		if(this.isInitTable)
    			document.getElementById("contentInfo").contentWindow.location.reload();
    	}
    }
});

function initHtmlInfo(){
		document.getElementById("contentInfo").contentWindow.searchColumnInfo(mainInfoApp.searchUrl,mainInfoApp.tmpPath,mainInfoApp.baseBeanId,mainInfoApp.tmpLogBeanId);
		mainInfoApp.isInitTable=true;
}

function setSearchUrl(searchUrl){
	mainInfoApp.searchUrl=searchUrl;
}

//添加一个菜单选项
function addMenuList(menuInfo){
	if(menuInfo.isBaseBean=="1") {
		mainInfoApp.baseBeanId=menuInfo.id;
		return;
	}
	for(var i=0;i<mainInfoApp.logBeans.length;i++){
		if(mainInfoApp.logBeans[i].beanName==menuInfo.beanName) return;
	}
	mainInfoApp.logBeans.push(menuInfo);
}

function delMenuList(menuInfo){
	for(var i=0;i<mainInfoApp.logBeans.length;i++){
		if(mainInfoApp.logBeans[i].beanName==menuInfo.beanName)
			Vue.delete(mainInfoApp.logBeans,i);
	}
}