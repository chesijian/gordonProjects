function LimitLine() {
	var myLimitLine = this;
	
	var changed = true;
	var unchanged = false;
	
	this.mcorhr;
	this.mdate;
	this.dtype;
	
	this.selectedDalare;
	this.limitLineType;	
	
	// 联络线中数据变动flag(变动为true,未变动为false)
	this.dataFlag = unchanged;
	
	// 修改数据变动flag状态
	this.changeDataFlag = function(dataFlag) {
		myLimitLine.dataFlag = dataFlag;
	};
	
	// 数据被修改
	this.changeData = function() {
		myLimitLine.changeDataFlag(changed);
	};
	
	// 重置数据修改状态
	this.initChangeData = function() {
		myLimitLine.changeDataFlag(unchanged);
	};
	
	// 校验数据是否有变动(变动返回true,未变动返回false)
	this.checkDataChanged = function() {
		if (myLimitLine.dataFlag) {
			if (confirm("数据变动,是否保存?")) {
				myLimitLine.updateLimitLine();
				myLimitLine.initChangeData();
				return changed;
			} else {
				myLimitLine.initChangeData();
				return unchanged;
			}
		}
		return unchanged;
	};
	
	// 显示联络线类型DIV
	this.showLimitLineDataDiv = function() {
		$('#limitLineDataDiv').show();
	};
	
	// 隐藏联络线类型DIV
	this.hideLimitLineDataDiv = function() {
		$('#limitLineDataDiv').hide();
	};
	
	// 修改联络线数据
	this.updateLimitLine = function() {
		$.ajax({
			url : 'updateLineLimit',
			type : 'POST',
			dataType : 'json',
			data : {
				lineLimitStr : myLimitLine.makeLimitLineData(),
			},
			success : function(result) {
				if (result) {
					myLimitLine.initChangeData();
					myLimitLine.refreshLimitLineData();
					alert("保存成功!");
				} else {
					alert("保存失败!");
				}
			},
			error : function(xhr, status) {
				alert("系统错误!");
			}
		});
	};
	// 获取实际的上下限以及计划值
	this.getLimitAndPlanValue = function() {
		var time = $("#time").val().replace(/[^0-9]/ig, "");
		$.ajax({
			url : 'getLineLimitAndPlan',
			type : 'POST',
			dataType : 'json',
			data : {
				time : time,
			},
			success : function(result) {
				if (result) {
					myLimitLine.refreshLimitLineData();
					alert("成功获取计划数据!");
				} else {
					alert("获取计划数据失败!");
				}
			},
			error : function(xhr, status) {
				alert("系统错误!");
			}
		});
	};
	
	// 获取联络线列表
	this.getLimitLine = function() {
		$.ajax({
			url : 'getAllLine',
			type : 'POST',
			dataType : 'json',
			success : function(result) {
				if (result) {
					$('#limitLineMenu').text('');
					for (var i = 0; i < result.length; i++) {
						var limitLineId = result[i].mcorhr;
						
						$('#limitLineMenu').append('<li limitLineId="' + limitLineId + '" >'
								+ limitLineId
								+ '<div class="cl"></div></li>');
						myLimitLine.hideLimitLineDataDiv();
					}
					$('.count').text(result.length + '条');
					$(".lmenu").children("ul:first").children("li:first").trigger("click");
				} else {
					alert("获取失败!");
				}
			},
			error : function(xhr, status) {
				alert("系统错误!");
			}
		});
	};
	
	// 获取联络线类型数据
	this.getLimitLineData = function(selectedDalare, type) {
		//alert("ccc");
		var time = $("#time").val().replace(/[^0-9]/ig, "");
		if ((!myLimitLine.selectedDalare || myLimitLine.selectedDalare.attr('limitLineId') != selectedDalare.attr('limitLineId')) && !myLimitLine.checkDataChanged()) {
			
			if (myLimitLine.selectedDalare) {
				myLimitLine.selectedDalare.attr('class', '');
				myLimitLine.selectedDalare.find('input').attr('class', '');
				myLimitLine.selectedDalare.find('input').attr('readonly', 'readonly');
			}
			selectedDalare.attr('class', 'bghh');
			selectedDalare.find('input').attr('class', 'bghh');
			
			myLimitLine.changeLimitLineTypeStyle(type);
			myLimitLine.selectedDalare = selectedDalare;
			myLimitLine.limitLineType = type;
			$.ajax({
				url : 'getLineLimit',
				type : 'POST',
				dataType : 'json',
				data : {
					mcorhr : selectedDalare.attr('limitLineId'),
					dtype : type,
					time:time,
				},
				success : function(result) {
					if (result) {
						myLimitLine.mcorhr=result.mcorhr;
						myLimitLine.mdate=result.mdate;
						myLimitLine.dtype=result.dtype;
						myLimitLine.inputValueToTable(result);
						myLimitLine.loadChartData();
						myLimitLine.showLimitLineDataDiv();
					} /*else {
						alert("获取失败!");
					}*/
				},
				error : function(xhr, status) {
					alert("系统错误!");
				}
			});
		}
	};
	
	// 刷新联络线类型数据
	this.refreshLimitLineData = function() {
		var time = $("#time").val().replace(/[^0-9]/ig, "");
		$.ajax({
			url : 'getLineLimit',
			type : 'POST',
			dataType : 'json',
			data : {
				mcorhr : myLimitLine.selectedDalare.attr('limitLineId'),
				dtype : myLimitLine.limitLineType,
				time:time,
			},
			success : function(result) {
				if (result) {
					myLimitLine.mcorhr=result.mcorhr;
					myLimitLine.mdate=result.mdate;
					myLimitLine.dtype=result.dtype;
					myLimitLine.inputValueToTable(result);
					myLimitLine.loadChartData();
					//myLimitLine.showLimitLineDataDiv();
				} /*else {
					alert("获取失败!");
				}*/
			},
			error : function(xhr, status) {
				alert("系统错误!");
			}
		});
	};
	// 切换限额管理
	this.getLimitLinePage = function(type) {
		if (!myLimitLine.checkDataChanged()) {
			myLimitLine.changePageStyle(type);
			//var url=http://127.0.0.1:8080/market/path/init
			//myLimitLine.limitLineType = type;
			//limitLineType = type;
			/*$.post("getTreeList",{"time":time},function(data){//分类树型数据加载
			    });*/
			
		}
	};
	// 切换通道联络线管理
	this.getPathPage = function(type) {
		if (!myLimitLine.checkDataChanged()) {
			myLimitLine.changePageStyle(type);
			//myLimitLine.limitLineType = type;
			//limitLineType = type;
			
		}
	};
	
	var sxData="";
	var jhData="";
	// 根据联络线类型获取联络线类型数据
	this.getLimitLineDataByLimitLineType = function(type) {
		var time = $("#time").val().replace(/[^0-9]/ig, "");
		if (!myLimitLine.checkDataChanged()) {
			myLimitLine.changeLimitLineTypeStyle(type);
			myLimitLine.limitLineType = type;
			limitLineType = type;
			if(type!="差值空间"){
				$.ajax({
					url : 'getLineLimit',
					type : 'POST',
					dataType : 'json',
					data : {
						mcorhr : myLimitLine.selectedDalare.attr('limitLineId'),
						dtype : type,
						time:time,
					},
					success : function(result) {
						if (result) {
							myLimitLine.mcorhr=result.mcorhr;
							myLimitLine.mdate=result.mdate;
							myLimitLine.dtype=result.dtype;
							myLimitLine.inputValueToTable(result);
							//myLimitLine.loadChartData();
						} else {
							alert("获取失败!");
						}
					},
					error : function(xhr, status) {
						alert("系统错误!");
					}
				});
			}else{
				var sum=0;
				for (var key in sxData) {
					if (/^h[0-9]{2}$/.test(key)) {
						$('#limitLineDataDiv input[name=' + key + ']').val(sxData[key]-jhData[key]);
						sum += Number(sxData[key]-jhData[key]);
					}
				}
				var avg = sum /96;
				$('#limitLineDataDiv span[name=sumValue]').text(sum);
				$('#limitLineDataDiv span[name=avgValue]').text(avg.toFixed(0));
				myLimitLine.showLimitLineDataDiv();
				
				
				
			}	
		}
	};
	
	// 将联络线类型数据插入表格中
	this.inputValueToTable = function(data) {
		if (data) {
			var sum=0;
			for (var key in data) {
				if (/^h[0-9]{2}$/.test(key)) {
					$('#limitLineDataDiv input[name=' + key + ']').val(data[key]);
					sum += Number(data[key]);
				}
			}
			var avg = sum /96;
			data.sumQ = sum;
			data.aveP =avg.toFixed(0);
			$('#limitLineDataDiv span[name=sumValue]').text(data.sumQ);
			$('#limitLineDataDiv span[name=avgValue]').text(data.aveP);
		}
	};
	// 根据联络线类型获取联络线类型数据
	this.loadChartData = function() {
		var time = $("#time").val().replace(/[^0-9]/ig, "");
			$.ajax({
				url : 'loadChartData',
				type : 'POST',
				dataType : 'json',
				data : {
					mcorhr : myLimitLine.selectedDalare.attr('limitLineId'),
					time:time,
				},
				success : function(result) {
					if (result) {
						myLimitLine.makeDataToChart(result);
					} else {
						alert("获取失败!");
					}
				},
				error : function(xhr, status) {
					alert("系统错误!");
				}
			});
	};
	// 将联络线类型数据插入曲线图中
	this.makeDataToChart = function(result) {
		if (result) {
			var data=result.data;
			var data1=result.data1;
			var data2=result.data2;
			sxData=data;
			jhData=data2;
			var charts = {
					title : {
						text : '',
						x : -50 //center
					},
					xAxis : {
						categories : [
				              '00:00','00:15','00:30','00:45','01:00','01:15','01:30','01:45',
				              '02:00','02:15','02:30','02:45','03:00','03:15','03:30','03:45',
				              '04:00','04:15','04:30','04:45','05:00','05:15','05:30','05:45',
				              '06:00','06:15','06:30','06:45','07:00','07:15','07:30','07:45',
				              '08:00','08:15','08:30','08:45','09:00','09:15','09:30','09:45',
				              '10:00','10:15','10:30','10:45','11:00','11:15','11:30','11:45',
				              '12:00','12:15','12:30','12:45','13:00','13:15','13:30','13:45',
				              '14:00','14:15','14:30','14:45','15:00','15:15','15:30','15:45',
				              '16:00','16:15','16:30','16:45','17:00','17:15','17:30','17:45',
				              '18:00','18:15','18:30','18:45','19:00','19:15','19:30','19:45',
				              '20:00','20:15','20:30','20:45','21:00','21:15','21:30','21:45',
				              '22:00','22:15','22:30','22:45','23:00','23:15','23:30','23:45'
						] 
					},
					yAxis : {
						title : {
							text : '单位：MW'
						},
						plotLines : [{
							value : 0,
							width : 1,
							color : '#ffef00'
						}]
					},
					tooltip : {
						valueSuffix : 'MW'
					},
					legend : {
						layout : 'vertical',
						align : 'right',
						verticalAlign : 'middle',
						borderWidth : 0
					},
					series : [{
						name : '上限',
						data : [
					        data.h01, data.h02, data.h03, data.h04, data.h05, data.h06, data.h07, data.h08,
					        data.h09, data.h10, data.h11, data.h12, data.h13, data.h14, data.h15, data.h16,
					        data.h17, data.h18, data.h19, data.h20, data.h21, data.h22, data.h23, data.h24,
					        data.h25, data.h26, data.h27, data.h28, data.h29, data.h30, data.h31, data.h32,
					        data.h33, data.h34, data.h35, data.h36, data.h37, data.h38, data.h39, data.h40,
					        data.h41, data.h42, data.h43, data.h44, data.h45, data.h46, data.h47, data.h48,
					        data.h49, data.h50, data.h51, data.h52, data.h53, data.h54, data.h55, data.h56,
					        data.h57, data.h58, data.h59, data.h60, data.h61, data.h62, data.h63, data.h64,
					        data.h65, data.h66, data.h67, data.h68, data.h69, data.h70, data.h71, data.h72,
					        data.h73, data.h74, data.h75, data.h76, data.h77, data.h78, data.h79, data.h80,
					        data.h81, data.h82, data.h83, data.h84, data.h85, data.h86, data.h87, data.h88,
					        data.h89, data.h90, data.h91, data.h92, data.h93, data.h94, data.h95, data.h96
						]
					},
					{
						name : '下限',
						data : [
					        data1.h01, data1.h02, data1.h03, data1.h04, data1.h05, data1.h06, data1.h07, data1.h08,
					        data1.h09, data1.h10, data1.h11, data1.h12, data1.h13, data1.h14, data1.h15, data1.h16,
					        data1.h17, data1.h18, data1.h19, data1.h20, data1.h21, data1.h22, data1.h23, data1.h24,
					        data1.h25, data1.h26, data1.h27, data1.h28, data1.h29, data1.h30, data1.h31, data1.h32,
					        data1.h33, data1.h34, data1.h35, data1.h36, data1.h37, data1.h38, data1.h39, data1.h40,
					        data1.h41, data1.h42, data1.h43, data1.h44, data1.h45, data1.h46, data1.h47, data1.h48,
					        data1.h49, data1.h50, data1.h51, data1.h52, data1.h53, data1.h54, data1.h55, data1.h56,
					        data1.h57, data1.h58, data1.h59, data1.h60, data1.h61, data1.h62, data1.h63, data1.h64,
					        data1.h65, data1.h66, data1.h67, data1.h68, data1.h69, data1.h70, data1.h71, data1.h72,
					        data1.h73, data1.h74, data1.h75, data1.h76, data1.h77, data1.h78, data1.h79, data1.h80,
					        data1.h81, data1.h82, data1.h83, data1.h84, data1.h85, data1.h86, data1.h87, data1.h88,
					        data1.h89, data1.h90, data1.h91, data1.h92, data1.h93, data1.h94, data1.h95, data1.h96
						]
					},
					{
						name : '计划值',
						data : [
					        data2.h01, data2.h02, data2.h03, data2.h04, data2.h05, data2.h06, data2.h07, data2.h08,
					        data2.h09, data2.h10, data2.h11, data2.h12, data2.h13, data2.h14, data2.h15, data2.h16,
					        data2.h17, data2.h18, data2.h19, data2.h20, data2.h21, data2.h22, data2.h23, data2.h24,
					        data2.h25, data2.h26, data2.h27, data2.h28, data2.h29, data2.h30, data2.h31, data2.h32,
					        data2.h33, data2.h34, data2.h35, data2.h36, data2.h37, data2.h38, data2.h39, data2.h40,
					        data2.h41, data2.h42, data2.h43, data2.h44, data2.h45, data2.h46, data2.h47, data2.h48,
					        data2.h49, data2.h50, data2.h51, data2.h52, data2.h53, data2.h54, data2.h55, data2.h56,
					        data2.h57, data2.h58, data2.h59, data2.h60, data2.h61, data2.h62, data2.h63, data2.h64,
					        data2.h65, data2.h66, data2.h67, data2.h68, data2.h69, data2.h70, data2.h71, data2.h72,
					        data2.h73, data2.h74, data2.h75, data2.h76, data2.h77, data2.h78, data2.h79, data2.h80,
					        data2.h81, data2.h82, data2.h83, data2.h84, data2.h85, data2.h86, data2.h87, data2.h88,
					        data2.h89, data2.h90, data2.h91, data2.h92, data2.h93, data2.h94, data2.h95, data2.h96
						]
					}
					]
				};
			$('.cchart').highcharts(charts);	
		}
	};
	
	// 整理需要修改的联络线数据
	this.makeLimitLineData = function() {
		return JSON.stringify(myLimitLine.makeDataByTable());
	};
	
	// 根据表格整理联络线类型数据
	this.makeDataByTable = function() {
		var limitLineTypeDataInputs = $('#limitLineDataDiv').find('table input');
		var limitLineTypeData = {};
		var sum = 0;
		for (var index in limitLineTypeDataInputs) {
			var limitLineTypeDataInput = limitLineTypeDataInputs[index];
			limitLineTypeData[limitLineTypeDataInput.name] = limitLineTypeDataInput.value;
			if (limitLineTypeDataInput.value) {
				sum += Number(limitLineTypeDataInput.value);
			}
		}
		var avg = sum / 96;
		limitLineTypeData['sumQ'] = sum;
		limitLineTypeData['aveP'] = avg.toFixed(2);
		limitLineTypeData['mcorhr'] = myLimitLine.mcorhr;
		limitLineTypeData['mdate'] = myLimitLine.mdate;
		limitLineTypeData['dtype'] = myLimitLine.dtype;
		return limitLineTypeData;
	};
	
	// 联络线类型数据修改后按回车修改所有单元格数据
	this.copyTableValue = function(thisInput, e) {
		// 兼容FF和IE和Opera
		var theEvent = e || window.event;
		var code = theEvent.keyCode || theEvent.which || theEvent.charCode;
		if (thisInput.focus() && code == 13) {
			var value = thisInput.val();
			for (var i = 1; i <= 96 ; i++) {
				var key;
				if (i < 10) {
					key = 'h0' + i;
				} else {
					key = 'h' + i;
				}
				$('#limitLineDataDiv input[name=' + key + ']').val(value);
			}
		}
	};
	
	// 修改联络线类型样式
	this.changeLimitLineTypeStyle = function(type) {
		var limitLineTypes = $('#limitLineDataDiv .conrightt1 a');
		for (var i = 0; i < 4 ; i++) {
			var limitLineType = limitLineTypes[i];
			if (limitLineType.name == type) {
				limitLineType.style.color = '#D1B664';
				limitLineType.style.fontWeight = 'bold';
			} else {
				limitLineType.style.color = '#7F7F7F';
				limitLineType.style.fontWeight = '';
			}
		}
	};
	// 修改切换页面样式
	this.changePageStyle = function(type) {
		
		var limitLineTypes = $('#limitconrightt a');
		//alert(limitLineTypes.length);
		for (var i = 0; i < 2 ; i++) {
			var limitLineType = limitLineTypes[i];
			if (limitLineType.name == type) {
				limitLineType.style.color = '#D1B664';
				limitLineType.style.fontWeight = 'bold';
			} else {
				limitLineType.style.color = '#7F7F7F';
				limitLineType.style.fontWeight = '';
			}
		}
	};
	// 修改联络线名称
	this.changeLimitLineName = function() {
		myLimitLine.selectedDalare.find('input').attr('class', '');
		myLimitLine.selectedDalare.find('input').attr('readonly', false);
		limitLine.changeData();
	};
	
	// 完成修改联络线名称
	this.finishChangeLimitLineName = function() {
		myLimitLine.selectedDalare.find('input').attr('class', 'bghh');
		myLimitLine.selectedDalare.find('input').attr('readonly', true);
	};
}