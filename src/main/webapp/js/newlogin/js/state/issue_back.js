function Issue() {
	var myIssue = this;
	
	this.areaList;
	this.selectedissue;
	this.issueType;
	
	// 加载EL数据
	this.loadElData = function(areaList) {
		myIssue.areaList = areaList;
	}
	 
	// 显示发布单数据DIV
	this.showIssueDataDiv = function() {
		$('#IssueDataDiv').show();
	}
	
	// 隐藏发布单数据DIV
	this.hideIssueDataDiv = function() {
		$('#IssueDataDiv').hide();
	}
	
	// 显示区域
	this.showArea = function(userArea) {
		$('#area').append('<option value="汇总">汇总 </option>');
		for (var i = 0; i < myIssue.areaList.length; i++) {
			var area = myIssue.areaList[i].area;
			if (area != '国调') {
				$('#area').append('<option value="' + area + '">' + area + '</option>');
			}
		}
		if (userArea == '国调') {
			$('.posty').css('display', 'block');
		} else {
			$("#area  option[value="+userArea+"]").attr("selected",true);
			$('.posty').css('display', 'none');
		}
		issue.getIssue();
	}
	
	// 切换区域
	this.changeArea = function() {
		myIssue.getIssue();
	}
	// 隐藏发布单数据DIV
	this.showData = function() {
		var user = $('#user').val();
		//alert(user);
		if (user == '国调') {
			$('.btn1').show();
		} 
		
		issue.getCountData($("#hdbill"));
		
	}
	// 获取发布单列表
	this.getIssue = function() {
		var time = $("#time").val().replace(/[^0-9]/ig, ""); 
		var area=$('#area').val();
		if(area!="汇总"){
			$("#dataTable").empty();
			$('#IssueMenu1').empty();
			$.ajax({
				url : 'getResultList',
				type : 'POST',
				dataType : 'json',
				data : {
					area : $('#area').val(),
					time:time
				},
				success : function(result) {
					if (result) {
						$('#IssueMenu').text('');
						for (var i = 0; i < result.length; i++) {
							var IssueId = result[i].id;
							var IssueName = result[i].sheetName;
							var type = result[i].drloe;
							var IssueComment = result[i].descr;
							var typeCls;
							if ('buy' == type) {
								typeCls = 'bgy';
							} else {
								typeCls = 'bgi';
							}
							$('#IssueMenu').append('<li IssueId="' + IssueId + '" IssueName="' + result[i].sheetName + '" IssueComment="' + IssueComment + '">'
									+ '<div class="fl ' + typeCls + '">'
									+ result[i].sheetName
									+ '</div><div class="cl"></div></li>');
							myIssue.hideIssueDataDiv()
						}
						$('.count').text(result.length + '条');
						 if(result.length!=0){
							 $(".lmenu").children("ul:first").children("li:first").trigger("click");
						 }else{
							 myIssue.hideIssueDataDiv();
						 }
					} else {
						alert("获取失败!");
					}
				},
				error : function(xhr, status) {
					alert("系统错误!");
				}
			});
		}else{
			$('#IssueMenu').empty();
			$('#IssueMenu1').empty();
			$('#IssueMenu1').append('<li id="hdbill" IssueId="hdbill" IssueName="hdbill" IssueComment="hdbill">华东申报单费用结果<div class="cl"></div></li>'+
					'<li IssueId="sdbil" IssueName="sdbill" IssueComment="sdbill">山东申报单费用结果<div class="cl"></div></li>'+
					'<li IssueId="hdcount" IssueName="hdcount" IssueComment="hdcoutn">华东地区费用结果<div class="cl"></div></li>'+
					'<li IssueId="sdcount" IssueName="sdcount" IssueComment="sdcount">山东华东地区费用结果<div class="cl"></div></li>');
			
		}
	
	}
	// 获取发布单详细数据
	this.getCountData = function(selectedissue) {
		    $('#IssueDataDiv').hide(); 
		    var time = $("#time").val().replace(/[^0-9]/ig, "");
			if (myIssue.selectedissue) {
				myIssue.selectedissue.attr('class', '');
			}
			selectedissue.attr('class', 'bghh');
			myIssue.selectedissue = selectedissue;
			var isId=myIssue.selectedissue.attr('IssueId');
			var geturl="";
			if(isId=="hdbill"){
				geturl="getHdBillMeg";
			}else if(isId=="sdbil"){
				geturl="geSdBillMeg";
			}else if(isId=="hdcount"){
				geturl="getHdAreaMeg";
			}else if(isId=="sdcount"){
				geturl="getSdAreaMeg";
			}
			$.ajax({
				url : geturl,
				type : 'POST',
				dataType : 'json',
				data : {
					isId : isId,
					time : time
				},
				success : function(result) {
					//alert(result.length);
					var content=""
						if(isId=="hdbill"){
							$("#dataTable").empty();
							if (result) {
								 content="<thead><th width='7%' class='hdth'>地区</th>"+
									"<th width='8%' class='hdth'>单子名称</th>"+
									"<th width='10%' class='hdth'>送端出清电量</th>"+
									"<th width='10%' class='hdth'>受端出清电量</th>"+
									"<th width='10%' class='hdth'>单子总电量</th>"+
									"<th width='10%' class='hdth'>送端上网费用</th>"+
									"<th width='10%' class='hdth'>省内输电费用</th>"+
									"<th width='10%' class='hdth'>跨区输电费用</th>"+
									"<th width='15%' class='hdth'>从送端累加的总费用</th>"+
									"<th width='10%' class='hdth'>受端总费用</th></thead>";
								for(var i=0;i<result.length;i++){
								content=content+"<tr><td width='7%' class='hdth'>"+result[i].area+"</td>"+
									"<td width='8%' class='hdth'>"+result[i].dSheet+"</td>"+
									"<td width='10%' class='hdth'>"+result[i].clearQI+"</td>"+
									"<td width='10%' class='hdth'>"+result[i].clearQJ+"</td>"+
									"<td width='10%' class='hdth'>"+result[i].sumQ+"</td>"+
									"<td width='10%' class='hdth'>"+result[i].sw+"</td>"+
									"<td width='10%' class='hdth'>"+result[i].sdsd+"</td>"+
									"<td width='10%' class='hdth'>"+result[i].kqsd+"</td>"+
									"<td width='15%' class='hdth'>"+result[i].sdfy+"</td>"+
									"<td width='10%' class='hdth'>"+result[i].fdfy+"</td></tr>";				
									
								}
								$('#dataTable').append(content);
							} 
						}
					if(isId=="sdbil"){
						$("#dataTable").empty();
						if (result) {
							 content="<thead><th width='4%' class='hdth'>地区</th>"+
								"<th width='8%' class='hdth'>单子名称</th>"+
								"<th width='8%' class='hdth'>送端出清电量</th>"+
								"<th width='8%' class='hdth'>受端出清电量</th>"+
								"<th width='8%' class='hdth'>单子总电量</th>"+
								"<th width='8%' class='hdth'>送端上网费用</th>"+
								"<th width='8%' class='hdth'>省内输电费用</th>"+
								"<th width='8%' class='hdth'>华中输电费用</th>"+
								"<th width='8%' class='hdth'>德宝输电费用</th>"+
								"<th width='8%' class='hdth'>西北输电费用</th>"+
								"<th width='8%' class='hdth'>宁东输电费用</th>"+
								"<th width='8%' class='hdth'>从送端累加的总费用</th>"+
								"<th width='8%' class='hdth'>受端总费用</th></thead>";
							for(var i=0;i<result.length;i++){
							content=content+"<tr><td width='4%' class='hdth'>"+result[i].area+"</td>"+
								"<td width='8%' class='hdth'>"+result[i].dSheet+"</td>"+
								"<td width='8%' class='hdth'>"+result[i].clearQI+"</td>"+
								"<td width='8%' class='hdth'>"+result[i].clearQJ+"</td>"+
								"<td width='8%' class='hdth'>"+result[i].sumQ+"</td>"+
								"<td width='8%' class='hdth'>"+result[i].sw+"</td>"+
								"<td width='8%' class='hdth'>"+result[i].sdsd+"</td>"+
								"<td width='8%' class='hdth'>"+result[i].hzsd+"</td>"+
								"<td width='8%' class='hdth'>"+result[i].dbsd+"</td>"+
								"<td width='8%' class='hdth'>"+result[i].xbsd+"</td>"+
								"<td width='8%' class='hdth'>"+result[i].ndsd+"</td>"+
								
								"<td width='8%' class='hdth'>"+result[i].sdfy+"</td>"+
								"<td width='8%' class='hdth'>"+result[i].fdfy+"</td></tr>";				
								
							}
							$('#dataTable').append(content);
						} 
					}
					if(isId=="hdcount"){
						$("#dataTable").empty();
						if (result) {	
							 content="<thead><th width='7%' class='hdth'>地区</th>"+
								"<th width='8%' class='hdth'>送端出清电量</th>"+
								"<th width='10%' class='hdth'>送端出清电量</th>"+
								"<th width='10%' class='hdth'>受端出清电量</th>"+
								"<th width='10%' class='hdth'>省内输电费用</th>"+
								"<th width='10%' class='hdth'>跨区输电费用</th>"+
								"<th width='15%' class='hdth'>从送端累加的总费用</th>"+
								"<th width='10%' class='hdth'>受端总费用</th></thead>";
							for(var i=0;i<result.length;i++){
							content=content+"<tr><td width='7%' class='hdth'>"+result[i].area+"</td>"+
								"<td width='10%' class='hdth'>"+result[i].clearQI+"</td>"+
								"<td width='10%' class='hdth'>"+result[i].clearQJ+"</td>"+
								"<td width='10%' class='hdth'>"+result[i].sw+"</td>"+
								"<td width='10%' class='hdth'>"+result[i].sdsd+"</td>"+
								"<td width='10%' class='hdth'>"+result[i].kqsd+"</td>"+
								"<td width='15%' class='hdth'>"+result[i].sdfy+"</td>"+
								"<td width='10%' class='hdth'>"+result[i].fdfy+"</td></tr>";				
								
							}
							$('#dataTable').append(content);
						} 
					}
					if(isId=="sdcount"){
						$("#dataTable").empty();
						if (result) {
							 content="<thead><th width='10%' class='hdth'>地区</th>"+
								"<th width='8%' class='hdth'>送端出清电量</th>"+
								"<th width='8%' class='hdth'>受端出清电量</th>"+
								"<th width='8%' class='hdth'>送端上网费用</th>"+
								"<th width='8%' class='hdth'>省内输电费用</th>"+
								"<th width='8%' class='hdth'>华中输电费用</th>"+
								"<th width='10%' class='hdth'>德宝输电费用</th>"+
								"<th width='10%' class='hdth'>西北输电费用</th>"+
								"<th width='10%' class='hdth'>宁东输电费用</th>"+
								"<th width='10%' class='hdth'>从送端累加的总费用</th>"+
								"<th width='10%' class='hdth'>受端总费用</th></thead>";
							for(var i=0;i<result.length;i++){
							content=content+"<tr><td width='10%' class='hdth'>"+result[i].area+"</td>"+
								"<td width='8%' class='hdth'>"+result[i].clearQI+"</td>"+
								"<td width='8%' class='hdth'>"+result[i].clearQJ+"</td>"+
								"<td width='8%' class='hdth'>"+result[i].sw+"</td>"+
								"<td width='8%' class='hdth'>"+result[i].sdsd+"</td>"+
								"<td width='8%' class='hdth'>"+result[i].hzsd+"</td>"+
								"<td width='10%' class='hdth'>"+result[i].dbsd+"</td>"+
								"<td width='10%' class='hdth'>"+result[i].xbsd+"</td>"+
								"<td width='10%' class='hdth'>"+result[i].ndsd+"</td>"+		
								"<td width='10%' class='hdth'>"+result[i].sdfy+"</td>"+
								"<td width='10%' class='hdth'>"+result[i].fdfy+"</td></tr>";				
								
							}
							$('#dataTable').append(content);
							 $('#IssueDataDiv1').show();
						} 
					}
				},
				error : function(xhr, status) {
					alert("系统错误!");
				}
			});
		
	}
	// 获取发布单详细数据
	this.getIssueData = function(selectedissue, type) {
		$("#dataTable").empty();
		if ((!myIssue.selectedissue || myIssue.selectedissue.attr('IssueId') != selectedissue.attr('IssueId')) ) {
			if (myIssue.selectedissue) {
				myIssue.selectedissue.attr('class', '');
			}
			selectedissue.attr('class', 'bghh');
			myIssue.changeIssueTypeStyle(type);
			myIssue.selectedissue = selectedissue;
			myIssue.issueType = type;

			$.ajax({
				url : 'getResult',
				type : 'POST',
				dataType : 'json',
				data : {
					dsheet : selectedissue.attr('IssueId'),
					dtype : type
				},
				success : function(result) {
					if (result) {
						myIssue.inputValueToTable(result);
						myIssue.makeDataToChart(result);
						myIssue.inputValueToArea(selectedissue.attr('IssueComment'));
						myIssue.showIssueDataDiv();
					} else {
						alert("获取失败!");
					}
				},
				error : function(xhr, status) {
					alert("系统错误!");
				}
			});
		}
	}
	
	// 发布数据
	this.issueData = function() {
		 var time = $("#time").val().replace(/[^0-9]/ig, "");
		$.ajax({
			url : 'updateIssue',
			type : 'POST',
			dataType : 'json',
			data : {
				time : time
			},
			success : function(result) {
				alert("发布成功!");
			},
			error : function(xhr, status) {
				alert("系统错误!");
			}
		});
	}
	// 根据发布单类型获取发布单类型数据
	this.getIssueDataByIssueType = function(type) {
		myIssue.changeIssueTypeStyle(type);
		myIssue.issueType = type;
		IssueType = type;
		$.ajax({
			url : 'getResult',
			type : 'POST',
			dataType : 'json',
			data : {
				dsheet : myIssue.selectedissue.attr('IssueId'),
				dtype : type
			},
			success : function(result) {
				if (result) {
					myIssue.inputValueToTable(result);
					myIssue.makeDataToChart(result);
				} else {
					alert("获取失败!");
				}
			},
			error : function(xhr, status) {
				alert("系统错误!");
			}
		});
		
	}
	
	// 将发布单类型数据插入表格中
	this.inputValueToTable = function(data) {
		if (data) {
			for (var key in data) {
				if (/^h[0-9]{2}$/.test(key)) {
					$('#IssueDataDiv input[name=' + key + ']').val(data[key]).attr('disabled', true);;
				}
			}
			if (!data.sumQ || data.sumQ == 'null') {
				data.sumQ = 0;
			}
			if (!data.aveP || data.aveP == 'null') {
				data.aveP = 0;
			}
			$('#IssueDataDiv span[name=sumValue]').text(data.sumQ+' MWh');
			$('#IssueDataDiv span[name=avgValue]').text(data.aveP+' MW');
			$('#IssueDataDiv span[name=sumPrice]').text(data.sumPrice+' 元/MWh');
		}
	}
	
	// 将发布单类型数据插入曲线图中
	this.makeDataToChart = function(data) {
		if (data) {
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
							text : '单位：条'
						},
						plotLines : [{
							value : 0,
							width : 1,
							color : '#ffef00'
						}]
					},
					tooltip : {
						valueSuffix : '条'
					},
					legend : {
						layout : 'vertical',
						align : 'right',
						verticalAlign : 'middle',
						borderWidth : 0
					},
					series : [{
						name : '电力',
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
					}]
				};
			$('.cchart').highcharts(charts);	
		}
	}
	
	// 将发布单类型说明插入文本域中
	this.inputValueToArea = function(data) {
		if (data == 'null') {
			data = '';
		}
		$('#commentDiv').append(data);
	}
	
	// 整理需要修改的发布单数据
	this.makeIssueData = function() {
		var Issue = {};
		var IssueId = myIssue.selectedissue.attr('IssueId');
		var IssueName = myIssue.selectedissue.find('input').val();
		var comment = $('#comment').text();
		var IssueType = myIssue.issueType;
		var IssueTypeData = myIssue.makeDataByTable();
		Issue['id'] = IssueId;
		Issue['sheetName'] = IssueName;
		Issue['descr'] = comment;
		Issue['IssueDatas'] = [IssueTypeData];
		return JSON.stringify(Issue);
	}
	
	// 根据表格整理发布单类型数据
	this.makeDataByTable = function() {
		var IssueTypeDataInputs = $('#IssueDataDiv').find('table input');
		var IssueTypeData = {};
		var sum = 0;
		for (var index in IssueTypeDataInputs) {
			var IssueTypeDataInput = IssueTypeDataInputs[index];
			IssueTypeData[IssueTypeDataInput.name] = IssueTypeDataInput.value;
			if (IssueTypeDataInput.value) {
				sum += Number(IssueTypeDataInput.value);
			}
		}
		var avg = sum / 96;
		IssueTypeData['id'] = myIssue.selectedissue.attr('IssueId');
		IssueTypeData['dtype'] = myIssue.issueType;
		IssueTypeData['sumQ'] = sum;
		IssueTypeData['aveP'] = avg.toFixed(2);
		return IssueTypeData;
	}
	
	
	// 修改发布单类型样式
	this.changeIssueTypeStyle = function(type) {
		
		var IssueTypes = $('#IssueDataDiv .conrightt1 a');
		for (var i = 0; i < 3 ; i++) {
			var IssueType = IssueTypes[i];
			if (IssueType.name == type) {
				IssueType.style.color = '#D1B664';
				IssueType.style.fontWeight = 'bold';
			} else {
				IssueType.style.color = '#7F7F7F';
				IssueType.style.fontWeight = '';
			}
		}
	}
}