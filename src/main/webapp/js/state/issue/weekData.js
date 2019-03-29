

$(function(){
	var time = parent.document.getElementById("time").value;
	$("#startTime").val(getWeekOfFirstDay(time));
	$("#endTime").val(getWeekOfLastDay(time));
	
	
	//loadWeekData();
});
var chart;

function exportHighcharts(){  
	
    chart = $("#pictrue_cchart").highcharts();  
    var svg_line = chart.getSVG();  
    var svg = svg_line;  
    $("#svg").val(svg);  
    $("#form1").prop("action", "exportword.do").submit();  
} 


	// 导出周报
	function exportWeekReport() {
		var startTime = $("#startTime").val().replace(/[^0-9]/ig, "");
		var endTime = $("#endTime").val().replace(/[^0-9]/ig, "");
		var url = "exportWeekReport?startTime="+startTime+"&endTime="+endTime;
		window.open(url);
	};
	// 导出申报情况
	function exportWeekReportExcel() {
		var startTime = $("#startTime").val().replace(/[^0-9]/ig, "");
		var endTime = $("#endTime").val().replace(/[^0-9]/ig, "");
		var url = "exportWeekReportExcel?startTime="+startTime+"&endTime="+endTime;
		window.open(url);
	};
	function refreshChart(){
		loadWeekData("older");
	}
	
	//加载周报展示数据
	function loadWeekData(type){
		mask();
		$("#year").html($("#startTime").val().substr(0,4));
		$.ajax({
			url : "getWeekData", //baseUrl +"/issue/getWeekData",
			type : 'POST',
			dataType : 'json',
			data : {
				type:type,
				startTime:$("#startTime").val(),
				endTime:$("#endTime").val()
			},
			success : function(result) {
				if (result) {
					for(var key in result){
						//var value = Number(result[key]).toFixed(2);
						$('#content span[name='+key+']').text(result[key]);
					}
					//初始化利用率图表
					var data = JSON.parse(result['pictureData']); 
					var chartData = data["mainData"];
					console.info(chartData);
					var time = data["mainTime"];
					loadPicture(chartData,time); 
					//插入卖单表格
					var table = result['saleTable'];
					var saleTable = JSON.parse(table);
					var tableStr = "<table class='table table-hover' border =1><thead><th>省份</th><th>平均电力 </th><th>平均电价 </th></thead>";
					insertTable(saleTable,tableStr,"table1");
					//插入买单表单
					var buyTable = JSON.parse(result['buyTable']);
					insertTable(buyTable,tableStr,"table2");
					//插入成交结果表格
					var dealTableStr = "<table class='table table-hover' border =1><thead><th class='cgr'>区域</th><th class='cgr'>平均出清电力</th><th class='cgr'>平均出清电价 </th><th class='cgr'>累计成交电量 </th></thead>";
					var dealTable = JSON.parse(result['dealTable']);
					insertTable(dealTable,dealTableStr,"table3");
					//插入
					var executeTableStr = "<table class='table table-hover' border =1><thead><th>区域</th><th>累计成交电量（MWh）</th><th>累计执行电量（MWh）</th></thead>";
					var executeTable = JSON.parse(result['biasTable']);
					insertTable(executeTable,executeTableStr,"table4");
					//插入
					var biasTableStr = "<table class='table table-hover' border =1><thead><th>区域</th><th>偏差量（MWh）</th><th>偏差原因</th></thead>";
					var biasTable = JSON.parse(result['executeTable']);
					insertTable(biasTable,biasTableStr,"table5");
					unmask();
				}
			},
			error : function(xhr, status) {
				unmask();
				alert("系统错误!");
			}
           }); 
	}
	
	//插入表格数据
	function insertTable(table,tableStr,tableId){
		if(table){
			for(var area in table){
				tableStr+="<tr><td>"+area+"</td>";
				for(var item in table[area]){
					tableStr+="<td>"+table[area][item]+"</td>";
				}
				tableStr+="</tr>";
			}
			tableStr+="</table>";
		}
		$("#"+tableId).html(tableStr);
	}
	//加载图表
	function loadPicture(data,time){
		var timeArr = new Array();
		if(time.length>0){
			for(var i=0;i<time.length;i++){
				timeArr.push(time[i]);
			}
		}
		Highcharts.wrap(Highcharts.Chart.prototype, 'getSVG', function(proceed) {  
	        return proceed.call(this)  
	            .replace(  
	                /(fill|stroke)="rgba[Math Processing Error]"/g,   
	                '$1="rgb($2)" $1-opacity="$3"'  
	            );  
	    });
		
		chart = $('#pictrue_cchart').highcharts({
	        chart: {
	            type: 'column'
	        },
	        title: {
	            text: '跨区通道平均利用率'
	        },
	        subtitle: {
	            text: ''
	        },
	        xAxis: {
	            categories: time,
	            crosshair: true
	        },
	        yAxis: {
	            min: 0,
	            max:100,
	            title: {
	                text: '利用率%'
	            }
	        },
	        tooltip: {
	            shared: true,
	            useHTML: true
	        },
	        legend : {
				//layout : 'vertical',
				align : 'center',
				//verticalAlign : 'middle',
				borderWidth : 0
			},
	        plotOptions: {
	            column: {
	                    //stacking: 'percent',
	                pointPadding: 0.2,
	                borderWidth: 0
	            }
	        },
	        series: data
	    });
	
	}

	//得到一周的第一天
	function getWeekOfFirstDay(str){
		var day = '';
		var newDate = new Date();
		//使用于2016-10-17形式的时间
		var date = new Date(Date.parse(str.replace(/-/g,"/")));
		//属于一周的第几天
		var dateDay = date.getDay();
		//一星期的星期一到星期天
		var first_day;
		if(dateDay==0){
			first_day = newDate.setTime(date.getTime()-1000*60*60*24*6);
		}else if(dateDay==1){
			first_day = newDate.setTime(date.getTime());
		}else if(dateDay==2){
			first_day = newDate.setTime(date.getTime()-1000*60*60*24*1);
		}else if(dateDay==3){
			first_day = newDate.setTime(date.getTime()-1000*60*60*24*2);
		}else if(dateDay==4){
			first_day = newDate.setTime(date.getTime()-1000*60*60*24*3);
		}else if(dateDay==5){
			first_day = newDate.setTime(date.getTime()-1000*60*60*24*4);
		}else if(dateDay==6){
			first_day = newDate.setTime(date.getTime()-1000*60*60*24*5);
		}
		if(first_day){
			var strYear = newDate.getFullYear();     
		    var strDay = newDate.getDate();     
		    var strMonth = newDate.getMonth()+1;
		    day = strYear+"-"+(strMonth<10?"0"+strMonth:strMonth)+"-"+(strDay<10?"0"+strDay:strDay); 
		}
		return day;
	}
	//得到一个星期的最后一天
	function getWeekOfLastDay(str){
		var day = '';
		var newDate = new Date();
		//使用于2016-10-17形式的时间
		var date = new Date(Date.parse(str.replace(/-/g,"/")));
		//属于一周的第几天
		var dateDay = date.getDay();
		//一星期的星期一到星期天
		var last_day;
		if(dateDay==0){
			last_day = newDate.setTime(date.getTime());
		}else if(dateDay==1){
			last_day = newDate.setTime(date.getTime()+1000*60*60*24*6);
		}else if(dateDay==2){
			last_day = newDate.setTime(date.getTime()+1000*60*60*24*5);
		}else if(dateDay==3){
			last_day = newDate.setTime(date.getTime()+1000*60*60*24*4);
		}else if(dateDay==4){
			last_day = newDate.setTime(date.getTime()+1000*60*60*24*3);
		}else if(dateDay==5){
			last_day = newDate.setTime(date.getTime()+1000*60*60*24*2);
		}else if(dateDay==6){
			last_day = newDate.setTime(date.getTime()+1000*60*60*24*1);
		}
		if(last_day){
			var strYear = newDate.getFullYear();     
		    var strDay = newDate.getDate();     
		    var strMonth = newDate.getMonth()+1;
			day = strYear+"-"+(strMonth<10?"0"+strMonth:strMonth)+"-"+(strDay<10?"0"+strDay:strDay); 
		}
		return day;
	}