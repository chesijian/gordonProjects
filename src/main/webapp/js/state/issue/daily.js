function daily() {
	var myDaily = this;
	this.areaList;
	this.selectedissue;
	this.resultId;
	this.inputVal = 0;
	
	// 加载树
	this.loadTree = function() {
		//var area = $('#area').val();
		//this.inputValueToTable1({});
		//this.makeDataToChart({});
		var setting = {
			view : {
				showLine : true
			},
			data : {
				simpleData : {
					enable : true
				}
			},
			callback : {
				onClick : zTreeOnClick
			}
		};
		var treeData = myDaily.TreeData();
		var zNodes = jQuery.parseJSON(treeData);

		function showIconForTree(treeId, treeNode) {
			return !treeNode.isParent;
		};

		$(document).ready(function() {
			$.fn.zTree.init($("#treeDemo"), setting, zNodes);
		});		

	};
	
	this.TreeData = function() {
		var area = parent.document.getElementById("area").value;
		var time = parent.document.getElementById("time").value.replace(/[^0-9]/ig, "");
		var treeData = "";
		$.ajax({
			url :baseUrl+"/issue/loadDailyTree",
			type : 'POST',
			dataType : 'json',
			async : false,
			data : {
				area : area,
				time : time
			},
			success : function(result) {
				treeData = JSON.stringify(result);
				treeData = treeData.replace(/pID/gm, 'pId');
			}
		});
		return treeData;
	};
	
	// 获取出清数据
	this.getResultData = function(id,node) {
		//myIssue.resultId =id;
		mask();
		var t = this;
		$.ajax({
			url : 'getResultData',
			type : 'POST',
			dataType : 'json',
			data : {
				id : id,
				time:parent.document.getElementById("time").value,
				type:"result"
			},
			timeout : 15000,
			success : function(result) {
				unmask();
				t.loadTableAndChart(result,node);
			},
			error : function(xhr, status) {
				unmask();
				alert("系统错误!");
			}

		});
	};
	// 加载单条出清结果
	this.loadTableAndChart = function(data,node) {
		//alert(type);
		this.inputValueToTable1(data);
		this.makeDataToChart(data,node);
	};
	// 将联络线类型数据插入表格中
	this.inputValueToTable1 = function(data) {
		if (data) {
			var sum = 0;
			for (var i = 1; i < 97; i++) {
				keyStr = (100 + i) + "";
				keyStr = keyStr.substring(1, keyStr.length);
				key = "h" + keyStr;
				var val = data[key];
				if(validUtil.isNull(val)){
					val = 0;
				}
				val = val + "";
				if (val.indexOf(".") > -1) {
					var vals = val.split(".");
					$('#dailyDataDiv input[name=' + key + ']').val(
							$.format(vals[0], 3, ',') + "." + vals[1]);

				} else {
					$('#dailyDataDiv input[name=' + key + ']').val(
							$.format(val, 3, ','));

				}
				sum += Number(data[key]);

			}
			var avg = sum / 96;
			data.sumQ = sum;
			data.aveP = avg.toFixed(0);
			if ((data.sumQ + "").indexOf(".") > -1) {
				var vals = (data.sumQ + "").split(".");
				if (vals[1].length > 4) {
					vals[1] = vals[1].substring(0, 3);
				}

				$('#dailyDataDiv span[name=sumValue]').text(
						$.format(vals[0], 3, ',') + "." + vals[1]);
			} else {
				$('#dailyDataDiv span[name=sumValue]').text(
						$.format(data.sumQ, 3, ','));
			}
			if ((data.aveP + "").indexOf(".") > -1) {
				var vals = (data.aveP + "").split(".");
				if (vals[1].length > 4) {
					vals[1] = vals[1].substring(0, 3);
				}
				$('#dailyDataDiv span[name=avgValue]').text(
						$.format(vals[0], 3, ',') + "." + vals[1]);
			} else {
				$('#dailyDataDiv span[name=avgValue]').text(
						$.format(data.aveP, 3, ','));
			}
			// sxData="";
			// jhData="";
		}
	};
	
	this.makeDataToChart = function(data,node) {
		//alert(type);
		var text = "";
		var type="电力";
		if(node){
			if(node['dtype'] != null && node['dtype'] != ""){
				text = node['area']+node['drole']+node['dtype'];
				type = node['dtype'];
			}else{
				text = node['name'];
			}
		}
		if (data) {
			// alert("111");
			var charts = {
				title : {
					text : text,
					x : -50
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
						text : '单位：'+(type == "电力"?"MW":"元/MWh")
					},
					plotLines : [ {
						value : 0,
						width : 1,
						color : '#ffef00'
					} ]
				},
				tooltip : {
					valueSuffix : (type == "电力"?"MW":"元/MWh")
				},
				legend : {
					layout : 'vertical',
					align : 'right',
					verticalAlign : 'middle',
					borderWidth : 0
				},
				series : [ {
					name : type,
					events : {
						click : function(e) {
							//console.info(e);
							//alert('如何或者当前点的X轴和Y轴值' + e.point['category'] + "=="+ e.point['y']);
						}
					},
					data : [ data.h01, data.h02, data.h03, data.h04, data.h05,
							data.h06, data.h07, data.h08, data.h09, data.h10,
							data.h11, data.h12, data.h13, data.h14, data.h15,
							data.h16, data.h17, data.h18, data.h19, data.h20,
							data.h21, data.h22, data.h23, data.h24, data.h25,
							data.h26, data.h27, data.h28, data.h29, data.h30,
							data.h31, data.h32, data.h33, data.h34, data.h35,
							data.h36, data.h37, data.h38, data.h39, data.h40,
							data.h41, data.h42, data.h43, data.h44, data.h45,
							data.h46, data.h47, data.h48, data.h49, data.h50,
							data.h51, data.h52, data.h53, data.h54, data.h55,
							data.h56, data.h57, data.h58, data.h59, data.h60,
							data.h61, data.h62, data.h63, data.h64, data.h65,
							data.h66, data.h67, data.h68, data.h69, data.h70,
							data.h71, data.h72, data.h73, data.h74, data.h75,
							data.h76, data.h77, data.h78, data.h79, data.h80,
							data.h81, data.h82, data.h83, data.h84, data.h85,
							data.h86, data.h87, data.h88, data.h89, data.h90,
							data.h91, data.h92, data.h93, data.h94, data.h95,
							data.h96 ]
				} ]
			};
			$('.cchart').highcharts(charts);
		}
	};
	
	//导出日报表
	this.exportDailyExcel = function(){
		var area = parent.document.getElementById("area").value;//$('#area').val();
		var time = parent.document.getElementById("time").value;
		var url =baseUrl+ "/issue/exportDailyExcel?mdate="+time+"&area="+area;
		window.open(url);
	};
	
}

	function getValue(result,resultPo,i){
		
		var h ="h"+(i < 10 ? "0" + i : i);
		var value = new Number(result[resultPo][h]);
		if(result[resultPo][h]==0||result[resultPo][h]==null){
			return "";
		}
		return value.toFixed(2);
	}
	
	
	