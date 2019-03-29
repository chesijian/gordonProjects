function LimitLine() {
	var myLimitLine = this;
	
	var changed = true;
	var unchanged = false;
	
	this.mcorhr;
	this.mdate;
	this.dtype;
	
	this.beforeData;
	this.dayData;
	this.tradeData;
	this.spaceData;
	this.data;
	
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
		var checkFlag=true;
		$('#limitLineDataDiv input[class=datainput]').each(function(){
			if(this.value==''){
				checkFlag=false;
				alert("请输入正确的数据!");
			  }
			});
		if(checkFlag){
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
		}
	};
	// 获取实际的上下限以及计划值
	this.getLimitAndPlanValue = function() {
		var time = parent.document.getElementById("time").value;
		var time1 = parent.document.getElementById("time").value.replace(/[^0-9]/ig, "");
		
		$.ajax({
			url : 'getLineLimitAndPlan',
			type : 'POST',
			dataType : 'json',
			data : {
				time : time,
				time1:time1,
			},
			beforeSend:function(){
				//$("#popupDiv").show(); 
				mask("正在获取中...");
			},
			success : function(result) {
				if (result && result['status']) {
					//$("#popupDiv").hide(); 
					myLimitLine.refreshLimitLineData();
					alert("成功获取电量数据!");
				} else {
					alert("获取电量数据失败!");
				}
				unmask();
			},
			error : function(xhr, status) {
				unmask();
				alert("系统错误!");
			}
		});
	};
	/**
	 * 交易功率导出至计划
	 */
	this.exportToPlan = function() {
		var time = parent.document.getElementById("time").value;
		var time1 = parent.document.getElementById("time").value.replace(/[^0-9]/ig, "");
		$.ajax({
			url : 'exportToPlan',
			type : 'POST',
			dataType : 'json',
			data : {
				time : time,
				time1:time1,
			},
			beforeSend:function(){
				//$("#popupDiv").show(); 
				mask("正在导出中...");
			},
			success : function(result) {
				if (result && result['status']) {
					myLimitLine.refreshLimitLineData();
					alert("导出成功!");
				} else {
					alert("导出失败!");
				}
				unmask();
			},
			error : function(xhr, status) {
				unmask();
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
						
						$('#limitLineMenu').append('<li limitLineId="' + limitLineId + '" style="cursor:pointer;">'
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
	this.getAllLimitLineData = function(selectedDalare, type,isRefresh) {
		
		var time = parent.document.getElementById("time").value.replace(/[^0-9]/ig, "");
		var area= parent.document.getElementById("area").value;
		var state="";
		if(area!='国调'){
			//state=1;
		}
		
		if (isRefresh || ((!myLimitLine.selectedDalare || myLimitLine.selectedDalare.attr('limitLineId') != selectedDalare.attr('limitLineId')) && !myLimitLine.checkDataChanged())) {
			
			if (myLimitLine.selectedDalare) {
				myLimitLine.selectedDalare.attr('class', '');
				myLimitLine.selectedDalare.find('input').attr('class', '');
				myLimitLine.selectedDalare.find('input').attr('readonly', 'readonly');
			}
			selectedDalare.attr('class', 'bghh');
			selectedDalare.find('input').attr('class', 'bghh');
			
			myLimitLine.changeLimitLineTypeStyle("日内计划值");
			myLimitLine.selectedDalare = selectedDalare;
			//设置默认值
			if(myLimitLine.limitLineType == undefined){
				myLimitLine.limitLineType = "日内计划值";
			}
			mask();
			$.ajax({
				url : 'getAllLimitLineData',
				type : 'POST',
				dataType : 'json',
				data : {
					mcorhr : selectedDalare.attr('limitLineId'),
					dtype : type,
					time:time,
					state:state,
				},
				success : function(result) {
					if (result != null) {
						myLimitLine.mcorhr=result.mcorhr;
						myLimitLine.mdate=result.mdate;
						myLimitLine.dtype=result.dtype;
						myLimitLine.data = result;
						myLimitLine.beforeData = result['日前计划值'];
						myLimitLine.dayData = result['日内计划值'];
						
						myLimitLine.getLimitLineDataByLimitLineType(myLimitLine.limitLineType);
						myLimitLine.makeDataToChart();
						myLimitLine.showLimitLineDataDiv();
					} else {
						myLimitLine.mcorhr=myLimitLine.selectedDalare.attr('limitLineId');
						myLimitLine.mdate=time;
						myLimitLine.dtype=type;
						$('#limitLineDataDiv input[class=datainput]').each(function(){this.value=0;});
						myLimitLine.showLimitLineDataDiv();
					}
					unmask();
					
				},
				error : function(xhr, status) {
					unmask();
					alert("系统错误!");
				}
			});
		}
	};
	
	
	// 刷新联络线类型数据
	this.refreshLimitLineData = function() {
		this.getAllLimitLineData($(".lmenu").children("ul:first").children("li:first"),myLimitLine.limitLineType,true);//.trigger("click");
		
	};
	
	// 切换通道联络线管理
	this.getPathPage = function(type) {
		if (!myLimitLine.checkDataChanged()) {
			myLimitLine.changePageStyle(type);
			
			
		}
	};
	
	// 根据联络线类型获取联络线类型数据
	this.getLimitLineDataByLimitLineType = function(type) {
		
		if (!myLimitLine.checkDataChanged()) {
			myLimitLine.changeLimitLineTypeStyle(type);
			myLimitLine.limitLineType = type;
			limitLineType = type;
			myLimitLine.inputValueToTable(this.data[type]);
			
		}
		
	};
	
	// 将联络线类型数据插入表格中
	this.inputValueToTable = function(data) {
		if (data) {
			var sum=0;
			for (var key in data) {
				if (/^h[0-9]{2}$/.test(key)) {
					var val = data[key];
					if(val!=null){
						val = val+"";
						if(val.indexOf(".")>-1){
							var vals = val.split(".");
							$('#limitLineDataDiv input[name=' + key + ']').val($.format(vals[0], 3, ',')+"."+vals[1]);
							
						}else{
							$('#limitLineDataDiv input[name=' + key + ']').val($.format(val, 3, ','));
							
						}
					}else{
						$('#limitLineDataDiv input[name=' + key + ']').val('');
					}
					
					sum += Number(data[key]);
				}
			}
			var avg = sum /96;
			data.sumQ = sum;
			data.aveP =avg.toFixed(0);
			if((data.sumQ+"").indexOf(".")>-1){
				var vals = (data.sumQ+"").split(".");
				if(vals[1].length>4){
					vals[1] = vals[1].substring(0,3);
				}
				$('#limitLineDataDiv span[name=sumValue]').text($.format(vals[0], 3, ',')+"."+vals[1]);
			}else{
				$('#limitLineDataDiv span[name=sumValue]').text($.format(data.sumQ, 3, ','));
			}
			if((data.aveP+"").indexOf(".")>-1){
				var vals = (data.aveP+"").split(".");
				if(vals[1].length>4){
					vals[1] = vals[1].substring(0,3);
				}
				$('#limitLineDataDiv span[name=avgValue]').text($.format(vals[0], 3, ',')+"."+vals[1]);
			}else{
				$('#limitLineDataDiv span[name=avgValue]').text($.format(data.aveP, 3, ','));
				}
			
		}
	};
	
	
	// 将联络线类型数据插入曲线图中
	this.makeDataToChart = function(result) {
		
			var seriesData_dayData = new Array();

			
				for(var i = 1;i<97;i++){
					keyStr = (100+i)+"";
					keyStr = keyStr.substring(1, keyStr.length);
					key = "h"+keyStr;
					seriesData_dayData.push(this.dayData[key] == undefined?0:this.dayData[key]);
					}
				
			
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
						name : '计量',
						data : seriesData_dayData
					}
					]
				};
			$('.cchart').highcharts(charts);	
		
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
		var value = 0;
		var valStr = "";
		for (var index in limitLineTypeDataInputs) {
			var limitLineTypeDataInput = limitLineTypeDataInputs[index];
			valStr = limitLineTypeDataInput.value;
			if(valStr && valStr!= null){
				valStr = valStr.replace(",", "");
				value = Number(valStr);
				limitLineTypeData[limitLineTypeDataInput.name] = value;
				sum += value;
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
		if(limitLineTypes.length>0){
			for (var i = 0; i < 1 ; i++) {//原来i为2
				var limitLineType = limitLineTypes[i];
				//alert(limitLineType.name +"=="+ type);
				if (limitLineType.name == type) {
					limitLineType.style.color = '#D1B664';
					limitLineType.style.fontWeight = 'bold';
				} else {
					limitLineType.style.color = '#7F7F7F';
					limitLineType.style.fontWeight = '';
				}
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
	// 导出通道计划值
	this.exportLimitData = function(){
		var time = parent.document.getElementById("time").value.replace(/[^0-9]/ig, "");
		var state="";
		if(area!='国调'){
			//state=1;
		}
			$.ajax({
				url : 'exportLimitData',
				type : 'POST',
				dataType : 'json',
				data : {
					mcorhr : myLimitLine.selectedDalare.attr('limitLineId'),
					time: time,
					state:state
				},
				success : function(result) {
					if (result=="creatSuccess") {
						alert("导出成功!");
					} else {
						alert("导出失败!");
					}
				},
				error : function(xhr, status) {
					alert("系统错误!");
				}
			});
		
	};
}

String.prototype.replaceall=function(s1,s2){
	return this.replace(new RegExp(s1,"gm"),s2); 
}