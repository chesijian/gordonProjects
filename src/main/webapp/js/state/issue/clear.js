/**
 * 
 */

$(function(){
		 
		 initClearTable();
});

String.prototype.replaceAll = function(s1,s2){ 
	return this.replace(new RegExp(s1,"gm"),s2); 
	}

/**
 * 导出数据
 */
function exportExcel(){
	var time = parent.document.getElementById("time").value;
	time = time.replaceAll("-", "");
	if(time == "" || time == null){
		alert("日期不能为空！");
		return false;
	}else{
		window.open("exportExcel?mdate="+time)
	}
}
function getLoginParams(params){
	//console.info(params);
	return {
		limit: params.limit,
         pageIndex: params.offset,
		startTime:$('#startTime').val(),
		endTime:$('#endTime').val()
	};
}

function getJson(url){
	var arr;
	$.ajaxSettings.async = false;
	$.getJSON(url,function(data){
		console.debug(data);
		arr =  data;
	});
	return arr;
}


function loadClearData(){
	var time = parent.document.getElementById("time").value;
	time = time.replaceAll("-", "");
	var map = getJson('getJson?mdate='+time);
	//var map = getJson('https://127.0.0.1:8543/market/issue/getJson?mdate=20160910');
	if(map == undefined || map == null){
		map = getJson('../js/state/issue/data/'+time+'.json');
	}
	if(map != undefined){
		$('#clearData').bootstrapTable('load',map['rows']);
	}else{
		$('#clearData').bootstrapTable('load',[]);
	}
	resetView();
	$('#clearData th[data-field="Buyername"]').css("min-width","100px");
	$('#clearData th[data-field="Sellername"]').css("min-width","100px");
	$('#clearData th[data-field="Trans_Path"]').css("min-width","160px");
}

function resetView(){
	$('#clearData').bootstrapTable('resetView',{
    	height: $(parent).height() - 390
    });
	$('#myTabContent .fixed-table-container').height(370);
}

function test(){
	alert("ok");
}


/**
 *初始化数据
 */
function initClearTable(){
	var currPageIndex = 1,currLimit=50;
	
	//console.info(map);
	$('#clearData').bootstrapTable({
		//url: '../js/state/issue/data/'+time+'.json',
		height: 300, //$(parent).height() - 250,
        striped: true,//條紋行  
        //width:'100%',
        //sidePagination: "server",//服务器分页  
        //showRefresh: true,//刷新功能  
        //search: true,//搜索功能 
        //cache: false,   
        clickToSelect: true,//选择行即选择checkbox  
        singleSelect: true,//仅允许单选  
        search: false, //不显示 搜索框
        showColumns: false, //不显示下拉框（选择显示的列）
        //searchOnEnterKey: true,//ENTER键搜索  
        pagination: true,//启用分页  
        escape: true,//过滤危险字符  
        queryParams:  getLoginParams,//携带参数  
        pageCount: 10,//每页行数  
        pageIndex: 0,//其实页  
        //method: "post",//请求格式  
        //contentType: "application/x-www-form-urlencoded",
        pageSize: 50,
        pageList: [10, 50, 100, 200, 500],
        onPageChange: function (number, size) {  
            currPageIndex = number;  
            currLimit = size ;
            resetView();
            //$('#myTabContent .fixed-table-container').height(380);
        }, 
        onLoadSuccess: function (data)  {
        	
        	//alert(data);
        }, 
        onLoadError: function () {
        }/**/,
        columns: [{
        	field: 'Number',
        	title: '序号'
        	}, {
            field: 'Interval',
            title: '时段数'
        }, {
            field: 'SellerArea',
            title: '卖单区域'
        }, {
            field: 'Sellername',
            title: '卖单名称'
        }, {
            field: 'Sellersection',
            title: '卖单段数'
        },  {
            field: 'power_i',formatter: function (value, row, index) {
            	return toFix(value,2);
        	}
        }, {
            field: 'price_i',formatter: function (value, row, index) {
            	return toFix(value,2);
        	}
        },  {
            field: 'clearpower_i',formatter: function (value, row, index) {
            	return toFix(value,2);
        	}
        }, {
            field: 'clearprice_i',formatter: function (value, row, index) {
            	return toFix(value,2);
        	}
        },{
            field: 'BuyerArea',
            title: '买单区域'
        }, {
            field: 'Buyername',
            'min-width':130,
            title: '买单名称'
        }, {
            field: 'Buyersection',
            title: '买单段数'
        }, {
            field: 'power_j',formatter: function (value, row, index) {
            	return toFix(value,2);
        	}
        }, {
            field: 'price_j',formatter: function (value, row, index) {
            	return toFix(value,2);
        	}
        }, {
            field: 'trans_loss',formatter: function (value, row, index) {
            	return toFix(value,2);
        	}
        }, {
            field: 'tanspower_j',formatter: function (value, row, index) {
            	return toFix(value,2);
        	}
        }, {
            field: 'clearpower_j',formatter: function (value, row, index) {
            	return toFix(value,2);
        	}
        }, {
            field: 'clearprice_j',formatter: function (value, row, index) {
            	return toFix(value,2);
        	}
        }, {
            field: 'Pricediff',formatter: function (value, row, index) {
            	return toFix(value,2);
        	}
        }, {
            field: 'SellCost',formatter: function (value, row, index) {
            	return toFix(value,2);
        	}
        }, {
            field: 'TranCost',formatter: function (value, row, index) {
            	return toFix(value,2);
        	}
        }, {
            field: 'LossCost'
        }, {
            field: 'BuyCost',formatter: function (value, row, index) {
            	return toFix(value,2);
        	}
        }, {
            field: 'LineCost'
        }, {
            field: 'LineLossCost'
        }, {
            field: 'LinePower'
        }, {
            field: 'Trans_Path'
        }, {
            field: 'Corridorpower'
        }, {
            field: 'Pri'
        }]
        
	});
	loadClearData();
}

/**
 * 四舍五入数字，事例：zkjs=toFix((zkjs*100),2);
 * 
 * @param num
 * @param n
 * @returns
 */
function toFix(num,n){
	  // num = 0.007;//要四舍五入的数字
	  var fixNum = new Number(num).toFixed(n);// 四舍五入之前加1
	  var fixedNum = new Number(fixNum).toFixed(n);// 四舍五入之后减1，再四舍五入一下
	  // alert(fixedNum);//弹出的数字就是正确的四舍五入结果啦
	  return fixedNum;
}