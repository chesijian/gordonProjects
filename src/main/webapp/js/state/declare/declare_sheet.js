/**
 * 
 */
var treeData;
var colorArr = [ "#06938e","rgb(164, 157, 153)","#f77e39","#c19d30","#ff7680","#000000",  "#FF60AF", "#FF44FF", "#B15BFF",
		"#000079", "#003E3E", "#006030", "#00DB00", "#9AFF02", "#E1E100",
		"#EAC100", "#FF9224", "#FF5809", "#B87070", "#AFAF61", "#6FB7B7",
		"#9999CC","#FF0000", "#B766AD" ];
//var isLoad = false;
/**
 * 单击单个单据，加载单据的数据
 * 
 * @description
 * @author 大雄
 * @date 2016年8月26日下午1:57:52
 */
function initData(id) {
	// alert(id);
	hideTip();
	if (validUtil.isNull(id)) {
		clearData();
		return;
	}
	
	mask();
	var options = {
		url : baseUrl + "/declare/getDeclareInfo",
		type : "post",
		data : {
			id : id
		},
		dataType : "json",
		success : function(response) {
			var rs = response;
			// console.info(rs);
			selectData = rs['dataInfo'];
			statusData = rs['statusData'];
			treeData = rs['treeData'];
			var data = new Object();
			var keyStr = null;
			var key = null;
			for (var i = 0; i < 97; i++) {
				keyStr = (100 + i) + "";
				keyStr = keyStr.substring(1, keyStr.length);
				key = "h" + keyStr;
				data[key] = 100 + i * 5;
				//console.info(statusData);
				//alert(statusData[i+1]);
				$('#' + key).css("background-color", colorArr[statusData[i]-1]);
				$('#' + key).show();
			}
			// initSelectData();
			// makeDataToChart(data);
			showChart();
			$('#declareDataDiv').show();

			unmask();
		},
		error : function() {
			alert("系统错误");
			unmask();
		}
	};
	$.ajax(options);
}
$(function() {

	$(this).bind(
			'click',
			function(e) {
				// console.info(e.target);
				var target = e.target;
				if (target.getAttribute("class") != "circle"
						&& target.tagName != "rect") {
					hideTip();
				}
			});
	// 设置所有都做监听事件
	$("div.circle").tooltip();
	var id = parent.$("#paramid").val();
	//alert(id);
	if (!validUtil.isNull(id)) {
		initData(id);
	}else{
		clearData();
	}

});

/**
 * 
 */
function initSelectData() {
	if (typeof selectData != undefined && selectData != null) {
		for ( var key in selectData['data']) {
			// alert(key);
			$("#select_price").append(
					"<option value='" + key + "'>" + key + "</option>");
		}
		$("#select_price option:first").prop("selected", 'selected');
		change($("#select_price").val());
	}
}
/**
 * 电价下拉选择
 * 
 * @param obj
 */
function change(value) {
	// alert(selectData['data'][value]);
	// console.info(selectData['data'][value]);
	// makeDataToChart(selectData['data'][value]);

}

function showChart() {
	var seriesData = new Array();
	if (selectData == null) {
		return false;
	}
	//console.info(selectData);
	for ( var key in selectData) {
		var data = new Array();
		// console.info(key);
		for (var i = 1; i < 97; i++) {
			keyStr = (100 + i) + "";
			keyStr = keyStr.substring(1, keyStr.length);
			keyStr = "h" + keyStr;
			data.push(selectData[key][keyStr]);
		}
		seriesData.push({
			name : key,
			data : data
		});
	}
	
	var drole = 'sale';
	var node = parent.declare.selectSingleNode;
	var text = "电力电价曲线图";
	if(node != undefined){
		//console.info(node);
		drole = node['attributes']['drloe'];
		text = node['attributes']['area']+('sale' == drole?"售电":"购电");
	}
	
	// console.info(seriesData);
	chart = $('.cchart').highcharts(
			{
				title : {
					text : text,
					x : -20
				// center
				},
				xAxis : {
					categories : [ '00:15', '00:30', '00:45', '01:00', '01:15',
							'01:30', '01:45', '02:00', '02:15', '02:30',
							'02:45', '03:00', '03:15', '03:30', '03:45',
							'04:00', '04:15', '04:30', '04:45', '05:00',
							'05:15', '05:30', '05:45', '06:00', '06:15',
							'06:30', '06:45', '07:00', '07:15', '07:30',
							'07:45', '08:00', '08:15', '08:30', '08:45',
							'09:00', '09:15', '09:30', '09:45', '10:00',
							'10:15', '10:30', '10:45', '11:00', '11:15',
							'11:30', '11:45', '12:00', '12:15', '12:30',
							'12:45', '13:00', '13:15', '13:30', '13:45',
							'14:00', '14:15', '14:30', '14:45', '15:00',
							'15:15', '15:30', '15:45', '16:00', '16:15',
							'16:30', '16:45', '17:00', '17:15', '17:30',
							'17:45', '18:00', '18:15', '18:30', '18:45',
							'19:00', '19:15', '19:30', '19:45', '20:00',
							'20:15', '20:30', '20:45', '21:00', '21:15',
							'21:30', '21:45', '22:00', '22:15', '22:30',
							'22:45', '23:00', '23:15', '23:30', '23:45',
							'24:00' ]
				},
				yAxis : {
					title : {
						text : '电力单位：MW'
					},
					plotLines : [ {
						value : 0,
						width : 1,
						color : '#808080'
					} ]
				},
				tooltip : {
					formatter : function() {
						var s = '<b>时段：' + this.x + '</b>';
						$.each(this.points, function() {
							if (this.y != 0) {
								s += '<br/>电力:' + this.y + 'MW   ';
								if(!(parent._isState && !parent.jgfbBtn)){
									s +='电价:'+ this.series.name + '元/MWh';}
							}

						});
						return s;
					},
					shared : true
				},
				legend : {
					layout : 'vertical',
					align : 'right',
					verticalAlign : 'middle',
					borderWidth : 0
				},
				series : seriesData
			/*
			 * series: [{ name: 'Tokyo', data: [7.0, 6.9, 9.5, 14.5, 18.2, 21.5,
			 * 25.2, 26.5, 23.3, 18.3, 13.9, 9.6] }, { name: 'New York', data:
			 * [-0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6,
			 * 2.5] }, { name: 'Berlin', data: [-0.9, 0.6, 3.5, 8.4, 13.5, 17.0,
			 * 18.6, 17.9, 14.3, 9.0, 3.9, 1.0] }, { name: 'London', data: [3.9,
			 * 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8] }]
			 */
			});
}


comboxData = [{"text":"00:15","value":"00:15"},{"text":"00:30","value":"00:30"},{"text":"00:45","value":"00:45"},{"text":"01:00","value":"01:00"},{"text":"01:15","value":"01:15"},{"text":"01:30","value":"01:30"},{"text":"01:45","value":"01:45"},{"text":"02:00","value":"02:00"},{"text":"02:15","value":"02:15"},{"text":"02:30","value":"02:30"},{"text":"02:45","value":"02:45"},{"text":"03:00","value":"03:00"},{"text":"03:15","value":"03:15"},{"text":"03:30","value":"03:30"},{"text":"03:45","value":"03:45"},{"text":"04:00","value":"04:00"},{"text":"04:15","value":"04:15"},{"text":"04:30","value":"04:30"},{"text":"04:45","value":"04:45"},{"text":"05:00","value":"05:00"},{"text":"05:15","value":"05:15"},{"text":"05:30","value":"05:30"},{"text":"05:45","value":"05:45"},{"text":"06:00","value":"06:00"},{"text":"06:15","value":"06:15"},{"text":"06:30","value":"06:30"},{"text":"06:45","value":"06:45"},{"text":"07:00","value":"07:00"},{"text":"07:15","value":"07:15"},{"text":"07:30","value":"07:30"},{"text":"07:45","value":"07:45"},{"text":"08:00","value":"08:00"},{"text":"08:15","value":"08:15"},{"text":"08:30","value":"08:30"},{"text":"08:45","value":"08:45"},{"text":"09:00","value":"09:00"},{"text":"09:15","value":"09:15"},{"text":"09:30","value":"09:30"},{"text":"09:45","value":"09:45"},{"text":"10:00","value":"10:00"},{"text":"10:15","value":"10:15"},{"text":"10:30","value":"10:30"},{"text":"10:45","value":"10:45"},{"text":"11:00","value":"11:00"},{"text":"11:15","value":"11:15"},{"text":"11:30","value":"11:30"},{"text":"11:45","value":"11:45"},{"text":"12:00","value":"12:00"},{"text":"12:15","value":"12:15"},{"text":"12:30","value":"12:30"},{"text":"12:45","value":"12:45"},{"text":"13:00","value":"13:00"},{"text":"13:15","value":"13:15"},{"text":"13:30","value":"13:30"},{"text":"13:45","value":"13:45"},{"text":"14:00","value":"14:00"},{"text":"14:15","value":"14:15"},{"text":"14:30","value":"14:30"},{"text":"14:45","value":"14:45"},{"text":"15:00","value":"15:00"},{"text":"15:15","value":"15:15"},{"text":"15:30","value":"15:30"},{"text":"15:45","value":"15:45"},{"text":"16:00","value":"16:00"},{"text":"16:15","value":"16:15"},{"text":"16:30","value":"16:30"},{"text":"16:45","value":"16:45"},{"text":"17:00","value":"17:00"},{"text":"17:15","value":"17:15"},{"text":"17:30","value":"17:30"},{"text":"17:45","value":"17:45"},{"text":"18:00","value":"18:00"},{"text":"18:15","value":"18:15"},{"text":"18:30","value":"18:30"},{"text":"18:45","value":"18:45"},{"text":"19:00","value":"19:00"},{"text":"19:15","value":"19:15"},{"text":"19:30","value":"19:30"},{"text":"19:45","value":"19:45"},{"text":"20:00","value":"20:00"},{"text":"20:15","value":"20:15"},{"text":"20:30","value":"20:30"},{"text":"20:45","value":"20:45"},{"text":"21:00","value":"21:00"},{"text":"21:15","value":"21:15"},{"text":"21:30","value":"21:30"},{"text":"21:45","value":"21:45"},{"text":"22:00","value":"22:00"},{"text":"22:15","value":"22:15"},{"text":"22:30","value":"22:30"},{"text":"22:45","value":"22:45"},{"text":"23:00","value":"23:00"},{"text":"23:15","value":"23:15"},{"text":"23:30","value":"23:30"},{"text":"23:45","value":"23:45"},{"text":"24:00","value":"24:00"}];


/**
 * 创建单个时段的电力曲线图
 * @description
 * @author 大雄
 * @date 2016年9月10日下午2:53:25
 * @param id
 */
var chart;
function createIntervalChart(id) {
	//alert(parent._isState+"----"+parent.jgfbBtn);
	if(parent._isState && !parent.jgfbBtn){
		hideTip();
		alert("当前时间不可查看电价信息！");
		
		return;
	}
	// alert("ok");
	var s = "";
	s.substring(1, s.length);
	id = id.substring(1, id.length);
	var index = parseInt(id);
	//console.info(index);
	var data = treeData[index];
	//console.info(data);
	var categoriesData = new Array();
	var seriesArr = new Array();
	//var seriesData = new Array();
	var sum = 0;
	var itemArr = null;
	//var start = 0;
	var drole = 'sale';
	var node = parent.declare.selectSingleNode;
	var text = "电力电价曲线图";
	if(node != undefined){
		//console.info(node);
		drole = node['attributes']['drloe'];
		//text = node['attributes']['text']+('sale' == drole?"售电":"购电");
	}
	//console.info(data);
	//alert(drole);
	var tempArr = new Array();
	var len = 0;
	if(drole == 'sale'){
		for ( var item in data) {
			
			categoriesData.push(Number(item));
			tempArr.push(data[item]);
		}
		len = tempArr.length;
		var i = 0;
		
		var j = 0;
		for(var i = len-1;i>-1;i--){
			//itemArr = new Array();
			//var temp = new Array();
			var temp1 = new Array();
			var temp2 = new Array();
			
			temp1.push(categoriesData[i]);
			temp1.push(sum);
			sum += tempArr[i];
			
			temp2.push(categoriesData[i]);
			temp2.push(sum);
			seriesArr.push(temp1);
			seriesArr.push(temp2);
			j++;
		}
	}else{
		for ( var item in data) {
			
			categoriesData.push(Number(item));
			var temp1 = new Array();
			var temp2 = new Array();
			
			temp1.push(Number(item));
			temp1.push(sum);
			sum += data[item];
			
			temp2.push(Number(item));
			temp2.push(sum);
			seriesArr.push(temp1);
			seriesArr.push(temp2);
			//tempArr.push(data[item]);
		}
		len = data.length;
	}
	//console.info(seriesArr);
	
	var maxPrice = categoriesData[0];
	var minPrice = categoriesData[categoriesData.length-1];
	var minxAxis = minPrice - 10;
	var maxxAxis = categoriesData[0];
	//if(maxPrice == minPrice){
		maxxAxis = maxxAxis+10;
	//}
	
	//console.info(categoriesData);
	//alert(maxxAxis+"=="+minxAxis);
	//console.info(seriesArr);
	//console.info($('#div_chart'));
	
		$('#div_chart').highcharts({
			chart: {
	            type: 'spline',
	            inverted: true
	        },
	        title: {
	        	text : text
	        },
	        xAxis: {
	            reversed: false,
	            title: {
	                text: '电价',
	                align: 'high'
	            },
	            labels: {
	                formatter: function () {
	                    return this.value;
	                }
	            },
	            //tickPositions: [0, 50, 100, 150,200,250,300,350,400],
	            min :minxAxis,
	            max : maxxAxis,
	            //min: 0,
	            maxPadding: 0.05,
	            showLastLabel: true
	        },
	        yAxis: {
	        	title: {
	                text: '电力(MW)',
	                align: 'high'
	            },
	            labels: {
	                formatter: function () {
	                    return this.value ;
	                }
	            },
	            lineWidth: 2
	        },
	        legend: {
	            enabled: false
	        },
	        tooltip: {
	            headerFormat: '<b>{series.name}</b><br/>',
	            pointFormat: '{point.y}MW: {point.x}元/MWh'
	        },
	        plotOptions: {
	            spline: {
	            	dataLabels: {
	            		formatter:function(percentag,point,series,total,x,y){
	            			//console.info(percentag);
	            			//console.info(this.point);
	            			//console.info(total);
	            			//console.info(x);
	            			//console.info(y);
	            			//console.info((this.point['index']%2 == 0));
	            			if(this.point['series']['data'].length<2 || (this.point['index']%2 == 0)){
	            				return "";
	            			}else{
	            				return this.x+"元/MWh";
	            			}
	            			
	            		},
	                    enabled: true
	                },
	                marker: {
	                    enable: true
	                }
	            }
	        },
	        series: [{
	        	name : comboxData[index - 1]['text'],
	            data: seriesArr//[[100, 0], [100, 100], [200, 100], [200, 200], [300, 200],[300, 400]]
	        }]
			
			});
	
	/*
	$('#div_chart').highcharts({
        chart: {
            type: 'line'
        },
        title : {
			text : '电力电价区间',
			x : -20
		},
        xAxis: {
						type:'linear'
        },
        yAxis: {
            title: {
                text: 'Snow depth (m)'
            },
            min: 0
        },
        plotOptions: {
            spline: {
                marker: {
                    enabled: true
                }
            },
            dataLabels: {
                    enabled: true,
                    format: '{y} mm'
                }
        },
        series: seriesArr

    });
	
	*/
	return;
	
	$('#div_chart').highcharts({
        chart: {
            type: 'columnrange',
            inverted: true
        },
		
        title : {
			text : '电力电价区间',
			x : -20
		},
        xAxis: {
        	reversed: true,
        	labels: {
                //rotation: -45,
                style: {
                    fontSize: '13px',
                    fontFamily: 'Verdana, sans-serif'
                }
            },
            title: {
                text: '电价',
                align: 'high'
            },
        	categories:categoriesData//[360,345,205]//
            //categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
        },
        yAxis: {
        	labels: {
               // rotation: -45,
                style: {
                    fontSize: '13px',
                    fontFamily: 'Verdana, sans-serif'
                }
            },
        	title: {
                text: '电力(MW)',
                align: 'high'
            }
        },
        plotOptions: {
            columnrange: {
                dataLabels: {
                    enabled: true,
                    formatter: function () {
                        return this.y + 'MW';
                    }
                }
            }
        },
        tooltip: {
        	formatter: function () {
        		console.info(this)
                var s = '<b>' + this.x + '(元/MWh)</b>';
                s += '<br/>' + this.points[0]['point']['low'] +'~'+this.points[0]['point']['high']+ '(MW)';
                
                return s;
            },
            shared: true
        },
        legend: {
            enabled: false,
            backgroundColor: ((Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF')
        },
        series: [{
        	name : comboxData[index - 1]['text'],
            data: seriesData//[107, 31, 635, 203, 2]//seriesData//[107, 31, 635, 203, 2]
            
        }]
    });
}

/**
 * 如果没有选择的时候清空数据
 * @description
 * @author 大雄
 * @date 2016年8月29日下午1:49:53
 */
function clearData(){
	//alert("clearData");
	hideTip();
	var keyStr;
	for (var i = 0; i < 97; i++) {
		keyStr = (100 + i) + "";
		keyStr = keyStr.substring(1, keyStr.length);
		key = "h" + keyStr;
		//$('#' + key).css("background-color", 'white');
		$('#' + key).hide();
	}
	$('#div_chart').html('');
	
}

function clearChartData(){
	chart = $('.cchart').highcharts();
	if(chart != undefined){
		//console.info(chart);
		if(chart.series[0] != undefined)
			chart.series[0].setData([]);
	}
}