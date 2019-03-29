function Issue() {
	var myIssue = this;
	this.areaList;
	this.selectedissue;
	this.issueType;
	this.resultId;
	this.inputVal = 0;
	// 加载树
	this.loadTree = function(index,type) {
		//alert(index+"---"+type);
		var area = $('#area').val();
		$('#a_0').css("color","");
		$('#a_0').css("fontWeight","");
		$('#a_1').css("color","");
		$('#a_1').css("fontWeight","");
		$('#a_2').css("color","");
		$('#a_2').css("fontWeight","");
		$('#a_3').css("color","");
		$('#a_3').css("fontWeight","");
		$('#a_4').css("color","");
		$('#a_4').css("fontWeight","");
		$('#a_5').css("color","");
		$('#a_5').css("fontWeight","");
		$('#a_6').css("color","");
		$('#a_6').css("fontWeight","");
		$('#a_7').css("color","");
		$('#a_7').css("fontWeight","");
		if(index == 0){
			//$("#limitconrightt span").remove();
			//$('#limitconrightt').append("<span><a class='btn1' style='margin-left: 850px' href='#' onclick='issue.exportData();'>+导出</a></span>");
			$('#a_0').css("color","#D1B664");
			$('#a_0').css("fontWeight","bold");
			
			$('#result').hide();
			$('#clear').hide();
			$('#map').hide();
			$('#trade').hide();
			$('#survey').show();
			loadSurvey();
			
		}else if(index == 1){
				//$("#limitconrightt span").remove();
				//$('#limitconrightt').append("<span><a class='btn1' style='margin-left: 850px' href='#' onclick='issue.exportData();'>+导出</a></span>");
				$('#a_1').css("color","#D1B664");
				$('#a_1').css("fontWeight","bold");
				
				$('#result').show();
				$('#clear').hide();
				$('#map').hide();
				$('#trade').hide();
				$('#survey').hide();
				//清空
				this.inputValueToTable1({});
				this.makeDataToChart({});
				
			}else if(index == 2){
				//$("#limitconrightt span").remove();
				$('#a_2').css("color","#D1B664");
				$('#a_2').css("fontWeight","bold");
				
				$('#result').show();
				$('#clear').hide();
				$('#map').hide();
				$('#survey').hide();
				$('#trade').hide();
				//清空
				this.inputValueToTable1({});
				this.makeDataToChart({});
			}else if(index == 3){
				$('#a_'+index).css("color","#D1B664");
				$('#a_'+index).css("fontWeight","bold");
				$('#clear').show();
				$('#result').hide();
				$('#map').hide();
				$('#survey').hide();
				$('#trade').hide();
				if(typeof document.getElementsByTagName("iframe")["frame"].contentWindow.loadClearData != "undefined"){
					document.getElementsByTagName("iframe")["frame"].contentWindow.loadClearData();
				}
			}else if(index == 4){
				//$("#limitconrightt span").remove();
				//如果是地图展示
				
				$('#a_4').css("color","#D1B664");
				$('#a_4').css("fontWeight","bold");
				$('#clear').hide();
				$('#result').hide();
				$('#map').show();
				$('#survey').hide();
				$('#trade').hide();
				
				if(typeof document.getElementsByTagName("iframe")["frame_map"].contentWindow.getIsLoad != "undefined"){
					var isLoad = document.getElementsByTagName("iframe")["frame_map"].contentWindow.getIsLoad();
					if(!isLoad){
						document.getElementsByTagName("iframe")["frame_map"].contentWindow.location.reload(true);
					}
				}
				
			}else if(index == 5){
				//$("#limitconrightt span").remove();
				//$('#limitconrightt').append("<span><a class='btn1' style='margin-left: 850px' href='#' onclick='issue.exportData();'>+导出</a></span>");
				$('#a_5').css("color","#D1B664");
				$('#a_5').css("fontWeight","bold");
				$('#clear').hide();
				$('#map').hide();
				$('#result').show();
				$('#survey').hide();
				$('#trade').hide();
				//清空
				this.inputValueToTable1({});
				this.makeDataToChart({});
				
			}else if(index == 6){
				//alert(index);
				//$("#limitconrightt span").remove();
				//$('#limitconrightt').append("<span><a class='btn1' style='margin-left: 850px' href='#' onclick='issue.exportData();'>+导出</a></span>");
				
				$('#a_6').css("color","#D1B664");
				$('#a_6').css("fontWeight","bold");
				$('#result').show();
				$('#clear').hide();
				$('#map').hide();
				$('#trade').hide();
				$('#btn_save').show();
				//清空
				this.inputValueToTable1({});
				this.makeDataToChart({});
			}else if(index == 7){
				
				$('#a_7').css("color","#D1B664");
				$('#a_7').css("fontWeight","bold");
				$('#clear').hide();
				$('#result').hide();
				$('#map').hide();
				$('#survey').hide();
				$('#trade').show();
				if(typeof document.getElementsByTagName("iframe")["frame_trade"].contentWindow.trade.getTradeDatas != "undefined"){
					document.getElementsByTagName("iframe")["frame_trade"].contentWindow.trade.getTradeDatas();
				}
			}else{
				//$("#limitconrightt span").remove();
				//如果是切换日期
				//alert("ok");
				this.loadTree(0,"survey");
				return;
				if(index == undefined){
					if(typeof document.getElementsByTagName("iframe")["frame"].contentWindow.loadClearData != "undefined"){
						document.getElementsByTagName("iframe")["frame"].contentWindow.loadClearData();
					}
					if(typeof document.getElementsByTagName("iframe")["frame_map"].contentWindow.getIsLoad != "undefined"){
						
							document.getElementsByTagName("iframe")["frame_map"].contentWindow.location.reload(true);
						
					}
					//清空
					this.inputValueToTable1({});
					this.makeDataToChart({});
				}else{
					
					$('#a_'+index).css("color","#D1B664");
					$('#a_'+index).css("fontWeight","bold");
					$('#clear').show();
					$('#result').hide();
					$('#map').hide();
					$('#survey').hide();
					
					return;
				}
				
			}
		//获取发布按钮的状态
		//===================
		//getBtnStatus();
		
		//获取SheetCostSum
		//myIssue.getSheetCostSum();
		//===================
		if(index==6){
			$('#IssueDataDiv input').removeAttr("readonly");
			$('#btn_save').show();
		}else{
			$('#IssueDataDiv input').attr('readonly', 'readonly');
			$('#btn_save').hide();
		}
		if(!validUtil.isNull(type)){
			this.issueType = type;
		}
		
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
		var treeData = myIssue.TreeData();
		var zNodes = jQuery.parseJSON(treeData);

		function showIconForTree(treeId, treeNode) {
			return !treeNode.isParent;
		}
		;

		$(document).ready(function() {
			$.fn.zTree.init($("#treeDemo"), setting, zNodes);
		});
		// return;
		// var data = eval(myIssue.TreeData());
		return;
		if (area == '国调') {
			issue.getCountData(zNodes[1].name);
		} else {
			issue.getIssueData(zNodes[1].name);
		}

	}
	
	this.getSheetCostSum = function(){
		var time = $("#time").val().replace(/[^0-9]/ig, "");
		$.ajax({
			url : 'getSheetCostSum',
			type : 'POST',
			dataType : 'json',
			data : {
				time : time
			},
			success : function(result) {
				if(result !=null){
					$("#sheetCostSum").empty();
					$("#sheetCostSum").append('<span>售电费:</span><span class="avenum">'+result.SellCost+'</span><span class="avenum">&nbsp;MWh</span> |'
			  	+'<span>输电费:</span><span class="avenum">'+ result.TranCost + '</span><span class="avenum">&nbsp;MW</span> |'+
			  	'<span>购电费:</span><span class="avenum">'+ result.BuyCost + '</span><span class="avenum">&nbsp;MWh</span> |');
				}else{
					$("#sheetCostSum").empty();
				}
			}
		});
	};
	
	this.TreeData = function() {
		var area = $('#area').val();
		var time = $("#time").val().replace(/[^0-9]/ig, "");
		//alert(this.issueType);
		var treeData = "";
		$.ajax({
			url : 'loadTree',
			type : 'POST',
			dataType : 'json',
			async : false,
			data : {
				area : area,
				type:this.issueType,
				time : time
			},
			success : function(result) {
				treeData = JSON.stringify(result);
				treeData = treeData.replace(/pID/gm, 'pId');
			}
		});
		return treeData;
	};
	// 加载EL数据
	this.loadElData = function(areaList) {
		myIssue.areaList = areaList;
	};
	//导出日报表
	this.exportExcel = function(){
		var area = $('#area').val();
		var url = "exportDailyExcel?mdate="+$('#time').val()+"&area="+area;
		window.open(url);
	};
	/**
	 * 导出日交易单
	 */
	this.exportDailyDoc = function(){
		mask("正在导出...");
		var time = $('#time').val();
		$.ajax({
			url : 'getExportDailyDoc',
			type : 'POST',
			dataType : 'json',
			async : false,
			data : {
				time : time
			},
			success : function(result) {
				unmask();
				if(result != undefined && result != null){
					if(result['status']){
						result = result['msg'];
						var url = "exportDailyDoc?fileName="+encodeURI(encodeURI(result));;
						window.open(url);
						/*for(var key in result){
							var url = "exportDailyDoc?fileName="+encodeURI(encodeURI(result[key]));;
							window.open(url);
						}*/
					}else{
						alert(result['msg']);
					}
					//console.info(result);
					
				}else{
					alert("没有交易数据！");
				}
			}
		});
		//var url = "exportDailyDoc?time="+$('#time').val();
		//window.open(url);
	};
	/**
	 * 本地导出出清结果
	 */
	this.localExportData = function(id) {
		var area = $('#area').val();
		var time = $("#time").val().replace(/[^0-9]/ig, "");
		window.open("localExportData?mdate="+$('#time').val()+"&area="+encodeURI(encodeURI(area)));
		
	};

	
	// 显示发布单数据DIV
	this.showIssueDataDiv = function() {
		$('#IssueDataDiv').show();
	};

	// 隐藏发布单数据DIV
	this.hideIssueDataDiv = function() {
		$('#IssueDataDiv').hide();
	};
	function myTreeOnClick(event, treeId, treeNode) {
		alert(treeNode.tId + ", " + treeNode.name);
	}
	;
	// 显示区域
	this.showArea = function(userArea) {
		$('#area').append('<option value="汇总">汇总 </option>');
		for (var i = 0; i < myIssue.areaList.length; i++) {
			var area = myIssue.areaList[i].area;
			if (area != '国调') {
				$('#area').append(
						'<option value="' + area + '">' + area + '</option>');
			}
		}
		if (userArea == '国调') {
			$('.posty').css('display', 'block');
		} else {
			$("#area  option[value=" + userArea + "]").attr("selected", true);
			$('.posty').css('display', 'none');
		}
		// issue.getIssue();
	};

	// 切换区域
	this.changeArea = function() {
		myIssue.getIssue();
	};
	// 隐藏发布单数据DIV
	this.showData = function() {
		var user = $('#user').val();
		// alert(user);
		if (user == '国调') {
			$('.btn1').show();
		}

		// issue.getCountData($("#hdbill"));

	};
	// 获取发布单列表
	this.getIssue = function() {
		var time = $("#time").val().replace(/[^0-9]/ig, "");
		var area = $('#area').val();
		if (area != "汇总") {
			$("#dataTable").empty();
			$('#IssueMenu1').empty();
			$
					.ajax({
						url : 'getResultList',
						type : 'POST',
						dataType : 'json',
						data : {
							area : $('#area').val(),
							time : time
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
									$('#IssueMenu')
											.append(
													'<li IssueId="'
															+ IssueId
															+ '" IssueName="'
															+ result[i].sheetName
															+ '" IssueComment="'
															+ IssueComment
															+ '">'
															+ '<div class="fl '
															+ typeCls
															+ '">'
															+ result[i].sheetName
															+ '</div><div class="cl"></div></li>');
									myIssue.hideIssueDataDiv();
								}
								$('.count').text(result.length + '条');
								if (result.length != 0) {
									$(".lmenu").children("ul:first").children(
											"li:first").trigger("click");
								} else {
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
		} else {
			$('#IssueMenu').empty();
			$('#IssueMenu1').empty();
			$('#IssueMenu1')
					.append(
							'<li id="hdbill" IssueId="hdbill" IssueName="hdbill" IssueComment="hdbill">华东申报单费用结果<div class="cl"></div></li>'
									+ '<li IssueId="sdbil" IssueName="sdbill" IssueComment="sdbill">山东申报单费用结果<div class="cl"></div></li>'
									+ '<li IssueId="hdcount" IssueName="hdcount" IssueComment="hdcoutn">华东地区费用结果<div class="cl"></div></li>'
									+ '<li IssueId="sdcount" IssueName="sdcount" IssueComment="sdcount">山东华东地区费用结果<div class="cl"></div></li>');

		}

	}
	// 获取发布单详细数据
	this.getCountData = function(treeName) {
		// $('#IssueDataDiv').hide();
		var time = $("#time").val().replace(/[^0-9]/ig, "");
		var isId = "";
		if (treeName == "申报单汇总") {
			isId = "getBillCountMeg";
		} else if (treeName == "费用汇总") {
			isId = "getPriceCountMeg";
		}
		var geturl = "";
		if (isId == "getBillCountMeg") {
			geturl = "getBillCountMeg";
		} else if (isId == "getPriceCountMeg") {
			geturl = "getPriceCountMeg";
		}
		$
				.ajax({
					url : geturl,
					type : 'POST',
					dataType : 'json',
					data : {
						isId : isId,
						time : time
					},
					success : function(result) {
						var content = "";
						if (isId == "getBillCountMeg") {
							$("#dataTable").empty();
							$('#dtc').empty();
							if (result) {
								content = "<thead><th width='7%' class='hdth'>地区</th>"
										+ "<th width='8%' class='hdth'>申报交易名称</th>"
										+ "<th width='10%' class='hdth'>申报电量(MWh)</th>"
										+ "<th width='10%' class='hdth'>出清电量(MWh)</th>"
										+ "<th width='10%' class='hdth'>出清费用(元)</th>"
										+ "<th width='10%' class='hdth'>结算电量(MWh)</th>"
										+ "<th width='10%' class='hdth'>结算费用(元)</th></thead>";
								/* "<th width='10%' class='hdth'>省内输电费用</th>"+ */
								/* "<th width='10%' class='hdth'>跨区输电费用</th>"+ */
								/* "<th width='15%' class='hdth'>从送端累加的总费用</th>"+ */
								/* "<th width='10%' class='hdth'>出清费用(元/MWh)</th></thead>"; */
								for (var i = 0; i < result.length; i++) {
									// alert(result.length);
									content = content
											+ "<tr><td width='7%' class='isTabNum'>"
											+ result[i].area
											+ "</td>"
											+ "<td width='8%' class='isTabNum'>"
											+ result[i].sheetName
											+ "</td>"
											+ "<td width='10%' class='isTabNum'>"
											+ $.format(result[i].sumQ, 3, ',')
											+ "</td>"
											+ "<td width='10%'  class='isTabNum'>"
											+ $.format(result[i].clear, 3, ',')
											+ "</td>"
											+ "<td width='10%' class='isTabNum'>"
											+ $.format(result[i].fdfy, 3, ',')
											+ "</td>"
											+ "<td width='10%' class='hdth'>/</td>"
											+ "<td width='10%' class='hdth'>/</td></tr>";
									/*
									 * "<td width='10%' class='hdth'>"+result[i].sw+"</td>"+ "<td width='10%' class='hdth'>"+result[i].sdsd+"</td>"+ "<td width='10%' class='hdth'>"+result[i].kqsd+"</td>"+ "<td width='15%' class='hdth'>"+result[i].sdfy+"</td>"+
									 */
									/* "<td width='10%' class='isTabNum'>"+result[i].sumPrice+"</td></tr>"; */

								}
								$('#dataTable').append(content);
							}
						}

						if (isId == "getPriceCountMeg") {
							$("#dataTable").empty();
							$('#dtc').empty();
							if (result) {
								content = "<thead><th width='7%' class='hdth'>地区</th>"
										+ "<th width='8%' class='hdth'>出清电量(MWh)</th>"
										+ "<th width='10%' class='hdth'>出清费用(元)</th>"
										+ "<th width='10%' class='hdth'>结算电量(MWh)</th>"
										+ "<th width='10%' class='hdth'>结算费用(元)</th></thead>";
								/*
								 * "<th width='10%' class='hdth'>跨区输电费用</th>"+ "<th width='15%' class='hdth'>从送端累加的总费用</th>"+ "<th width='10%' class='hdth'>总费用(元/MWh)</th></thead>";
								 */
								for (var i = 0; i < result.length; i++) {
									content = content
											+ "<tr><td width='7%' class='isTabNum'>"
											+ result[i].area
											+ "</td>"
											+ "<td width='10%' class='isTabNum'>"
											+ $.format(result[i].clear, 3, ',')
											+ "</td>"
											+ "<td width='10%' class='isTabNum'>"
											+ $.format(result[i].fdfy, 3, ',')
											+ "</td>"
											+ "<td width='10%' class='hdth'>/</td>"
											+ "<td width='10%' class='hdth'>/</td></tr>";
								}
								$('#dataTable').append(content);
							}
						}

					},
					error : function(xhr, status) {
						alert("系统错误!");
					}
				});

	};
	// 根据地区获取发布数据汇总
	this.getAreaData = function(area) {
		var time = $("#time").val().replace(/[^0-9]/ig, "");
		$('#IssueDataDiv').hide();
		$('#dtc').empty();
		$("#dataTable").empty();
		$
				.ajax({
					url : 'getResultByArea',
					type : 'POST',
					dataType : 'json',
					data : {
						area : area,
						time : time
					},
					success : function(result) {
						if (result) {
							/*
							 * if(area!=""){ $('#dtype').text(area+'汇总'); }else{
							 * $('#dtype').text('各省份汇总'); }
							 */
							if (result.areaPrice) {
								content = "<thead><th width='7%' class='hdth'>地区</th>"
										+ "<th width='8%' class='hdth'>总申报电量(MWh)</th>"
										+ "<th width='8%' class='hdth'>总出清电量(MWh)</th>"
										+ "<th width='10%' class='hdth'>总出清费用(元)</th>"
										+ "<th width='10%' class='hdth'>总结算电量(MWh)</th>"
										+ "<th width='10%' class='hdth'>总结算费用(元)</th></thead>";

								content = content
										+ "<tr><td width='7%' class='isTabNum'>"
										+ result.areaPrice.area
										+ "</td>"
										+ "<td width='10%' class='isTabNum'>"
										+ $.format(result.areaPrice.sumQ, 3,
												',')
										+ "</td>"
										+ "<td width='10%' class='isTabNum'>"
										+ $.format(result.areaPrice.clear, 3,
												',')
										+ "</td>"
										+ "<td width='10%' class='isTabNum'>"
										+ $.format(result.areaPrice.fdfy, 3,
												',')
										+ "</td>"
										+ "<td width='10%' class='hdth'>/</td>"
										+ "<td width='10%' class='hdth'>/</td></tr>";
								$('#dataTable').append(content);
							}
							if (result.areaData) {
								myIssue.inputValueToTable(result.areaData);
								myIssue.makeDataToChart(result.areaData);
								// myIssue.inputValueToArea(selectedissue.attr('IssueComment'));
								myIssue.showIssueDataDiv();
							}

						} else {
							alert("获取失败!");
						}
					},
					error : function(xhr, status) {
						alert("系统错误!");
					}
				});
		// }
	};
	// 获取发布单详细数据
	this.getIssueData = function(treeName) {
		var content = "";
		var time = $("#time").val().replace(/[^0-9]/ig, "");
		$('#IssueDataDiv').hide();
		var area = $("#area").val();
		$('#dtc').empty();
		$("#dataTable").empty();

		$
				.ajax({
					url : 'getResult',
					type : 'POST',
					dataType : 'json',
					data : {
						dsheet : treeName,
						time : time
					},
					success : function(result) {
						if (result.count && result.detail) {
							if (area == '国调') {
								if (result.count) {
									content = "<thead><th width='7%' class='hdth'>地区</th>"
											+ "<th width='8%' class='hdth'>申报交易名称</th>"
											+ "<th width='8%' class='hdth'>类型</th>"
											+ "<th width='8%' class='hdth'>申报电量(MWh)</th>"
											+ "<th width='8%' class='hdth'>出清电量(MWh)</th>"
											+ "<th width='10%' class='hdth'>出清费用(元)</th>"
											+ "<th width='10%' class='hdth'>结算电量(MWh)</th>"
											+ "<th width='10%' class='hdth'>结算费用(元)</th></thead>";
									content = content
											+ "<tr><td width='7%' class='isTabNum'>"
											+ result.count.area
											+ "</td>"
											+ "<td width='10%' class='isTabNum'>"
											+ result.count.sheetName
											+ "</td>"
											+ "<td width='10%' class='isTabNum'>"
											+ result.count.dtype
											+ "</td>"
											+ "<td width='10%' class='isTabNum'>"
											+ $.format(result.count.sumQ, 3,
													',')
											+ "</td>"
											+ "<td width='10%' class='isTabNum'>"
											+ $.format(result.count.clear, 3,
													',')
											+ "</td>"
											+ "<td width='10%' class='isTabNum'>"
											+ $.format(result.count.fdfy, 3,
													',')
											+ "</td>"
											+ "<td width='10%' class='hdth'>/</td>"
											+ "<td width='10%' class='hdth'>/</td></tr>";
									$('#dtc').append(content);
									myIssue.showIssueDataDiv();
									$('#IssueDataDiv').show();
								}
								if (result.detail) {
									myIssue.inputValueToTable(result.detail);
									myIssue.makeDataToChart(result.detail);
									myIssue.showIssueDataDiv();
								} else {
									alert("获取失败!");
								}
							} else if (area != '国调' && result.state == "1") {
								if (result.count) {
									content = "<thead><th width='7%' class='hdth'>地区</th>"
											+ "<th width='8%' class='hdth'>申报交易名称</th>"
											+ "<th width='8%' class='hdth'>类型</th>"
											+ "<th width='8%' class='hdth'>申报电量(MWh)</th>"
											+ "<th width='8%' class='hdth'>出清电量(MWh)</th>"
											+ "<th width='10%' class='hdth'>出清费用(元)</th>"
											+ "<th width='10%' class='hdth'>结算电量(MWh)</th>"
											+ "<th width='10%' class='hdth'>结算费用(元)</th></thead>";
									content = content
											+ "<tr><td width='7%' class='isTabNum'>"
											+ result.count.area
											+ "</td>"
											+ "<td width='10%' class='isTabNum'>"
											+ result.count.sheetName
											+ "</td>"
											+ "<td width='10%' class='isTabNum'>"
											+ result.count.dtype
											+ "</td>"
											+ "<td width='10%' class='isTabNum'>"
											+ $.format(result.count.sumQ, 3,
													',')
											+ "</td>"
											+ "<td width='10%' class='isTabNum'>"
											+ $.format(result.count.clear, 3,
													',')
											+ "</td>"
											+ "<td width='10%' class='isTabNum'>"
											+ $.format(result.count.fdfy, 3,
													',')
											+ "</td>"
											+ "<td width='10%' class='hdth'>/</td>"
											+ "<td width='10%' class='hdth'>/</td></tr>";
									$('#dtc').append(content);
									myIssue.showIssueDataDiv();
									$('#IssueDataDiv').show();
								}
								if (result.detail) {
									myIssue.inputValueToTable(result.detail);
									myIssue.makeDataToChart(result.detail);
									myIssue.showIssueDataDiv();
								} else {
									alert("获取失败!");
								}
							}
						} else {
							alert("数据尚未发布！");
						}
					},
					error : function(xhr, status) {
						alert("系统错误!");
					}
				});
		// }
	};
    //yangbo
	this.showDialog = function(){
		var time = $("#time").val();//.replace(/[^0-9]/ig, "");
    	var url = baseUrl+"/issue/issueResult?time="+time;
		createWin(url,350,160,"结果发布");
        return;
    }
	
	 //yangbo
	this.showDeclDialog = function(){
		var time = $("#time").val().replace(/[^0-9]/ig, "");
    	var url = baseUrl+"/issue/declResult?time="+time;
		createWin(url,350,160,"结果发布");
        return;
    }
	// 出清审核
	this.issueData = function() {
		var time = $("#time").val().replace(/[^0-9]/ig, "");
		alert($("#time").val());
		if (confirm("是否确定发布出清信息?")) {
			$.ajax({
				url : 'updateIssue',
				type : 'POST',
				dataType : 'json',
				// contentType:'application/json',
				data : {
					time : $("#time").val()
				},
				timeout : 15000,
				success : function(result) {
					if(result){
						if(result['msg']=='先签名后保存！'){
							$("#dialog").dialog();
						}else{
							alert(result['msg']);
						}
						getProcessBtnStatus();
						
					}else{
						alert("发布失败!");
					}
				},
				error : function(xhr, status) {
					alert("系统错误!");
				}

			});
		}

	};

	// 获取出清数据
	this.getResultData = function(id,node) {
		
		myIssue.resultId =id;
		mask();
		var t = this;
		$.ajax({
			url : 'getResultData',
			type : 'POST',
			dataType : 'json',
			// contentType:'application/json',
			data : {
				id : id,
				time:$('#time').val(),
				type:this.issueType
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
	/*
	this.getLineLimitData = function(){
		
	}
	*/
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
					val = "";
				}
				val = val + "";
				if (val.indexOf(".") > -1) {
					var vals = val.split(".");
					$('#IssueDataDiv input[name=' + key + ']').val(
							$.format(vals[0], 3, ',') + "." + vals[1]);

				} else {
					$('#IssueDataDiv input[name=' + key + ']').val(
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

				$('#IssueDataDiv span[name=sumValue]').text(
						$.format(vals[0], 3, ',') + "." + vals[1]);
			} else {
				$('#IssueDataDiv span[name=sumValue]').text(
						$.format(data.sumQ, 3, ','));
			}
			if ((data.aveP + "").indexOf(".") > -1) {
				var vals = (data.aveP + "").split(".");
				if (vals[1].length > 4) {
					vals[1] = vals[1].substring(0, 3);
				}
				$('#IssueDataDiv span[name=avgValue]').text(
						$.format(vals[0], 3, ',') + "." + vals[1]);
			} else {
				$('#IssueDataDiv span[name=avgValue]').text(
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

	// 结算审核
	this.jsData = function() {
		var time = $("#time").val().replace(/[^0-9]/ig, "");
		if (confirm("是否确定发布结算信息?")) {
			alert("完善中。。。");
		}

	};
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

	};

	// 将发布单类型数据插入表格中
	this.inputValueToTable = function(data) {
		if (data) {
			for ( var key in data) {
				if (/^h[0-9]{2}$/.test(key)) {
					$('#IssueDataDiv input[name=' + key + ']').val(
							data[key] == 0?"":$.format(data[key], 3, ',')).attr('disabled', true);
					;
				}
			}
			if (!data.sumQ || data.sumQ == 'null') {
				data.sumQ = 0;
			}
			if (!data.aveP || data.aveP == 'null') {
				data.aveP = 0;
			}
			$('#IssueDataDiv span[name=sumValue]').text(data.sumQ + ' MWh');
			$('#IssueDataDiv span[name=avgValue]').text(data.aveP + ' MW');
			/*
			 * $('#IssueDataDiv span[name=sumPrice]').text(data.sumPrice+'
			 * 元/MWh');
			 */
		}
	};

	

	// 将发布单类型说明插入文本域中
	this.inputValueToArea = function(data) {
		if (data == 'null') {
			data = '';
		}
		$('#commentDiv').append(data);
	};

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
		Issue['IssueDatas'] = [ IssueTypeData ];
		return JSON.stringify(Issue);
	};

	// 根据表格整理发布单类型数据
	this.makeDataByTable = function() {
		var IssueTypeDataInputs = $('#IssueDataDiv').find('table input');
		var IssueTypeData = {};
		var sum = 0;
		for ( var index in IssueTypeDataInputs) {
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
	};

	// 修改发布单类型样式
	this.changeIssueTypeStyle = function(type) {

		var IssueTypes = $('#IssueDataDiv .conrightt1 a');
		for (var i = 0; i < 3; i++) {
			var IssueType = IssueTypes[i];
			if (IssueType.name == type) {
				IssueType.style.color = '#D1B664';
				IssueType.style.fontWeight = 'bold';
			} else {
				IssueType.style.color = '#7F7F7F';
				IssueType.style.fontWeight = '';
			}
		}
	};
	// 导出出清数据
	this.exportData = function(id) {
		mask();
		var area = $('#area').val();
		var time = $("#time").val().replace(/[^0-9]/ig, "");
		//alert("area==="+area);
		$.ajax({
			url : 'exportInterface',
			type : 'POST',
			dataType : 'json',
			// contentType:'application/json',
			data : {
				area : area,
				time:time
			},
			timeout : 15000,
			success : function(result) {
				unmask();
				if(result){
					alert(result['msg']);
				}else{
					alert("导出失败");
				}
				
			},
			error : function(xhr, status) {
				unmask();
				alert("系统错误!");
			}

		});
	};
	//---------------------------------------------------------------------------------
	// 修改交易记录数据
	this.updateResult = function() {
	    
		var checkFlag=true;
		$('#IssueDataDiv input[class=datainput]').each(function(){
			if(this.value==''){
				checkFlag=false;
				alert("请输入正确的数据!");
			  }
			});
		if(checkFlag){
			$.ajax({
				url : 'updateResult',
				type : 'POST',
				dataType : 'json',
				data : {
					resultStr : myIssue.makeResultData(),
				},
				success : function(result) {
					if (result) {
						//myLimitLine.initChangeData();
						//myLimitLine.refreshLimitLineData();
						if(result['msg']=='先签名后保存！'){
							$("#dialog").dialog();
						}else{
							alert(result['msg']);
							if(result['status']){
								parent.closeWin();
							}
						}
						
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
	// 整理需要修改的交易数据
	this.makeResultData = function() {
		return JSON.stringify(myIssue.makeDataByTable());
	};
	
	// 根据表格整理交易数据
	this.makeDataByTable = function() {
		var resultTypeDataInputs = $('#IssueDataDiv').find('table input');
		var resultTypeData = {};
		var sum = 0;
		var value = 0;
		var valStr = "";
		for (var index in resultTypeDataInputs) {
			var resultTypeDataInput = resultTypeDataInputs[index];
			valStr = resultTypeDataInput.value;
			
			if(valStr && valStr!= null){
				//alert(valStr);
				valStr = valStr.replace(",", "");
				value = Number(valStr);
				resultTypeData[resultTypeDataInput.name] = value;
				sum += value;
			}
			
		}
		
		var avg = sum / 96;
		resultTypeData['sumQ'] = sum;
		//resultTypeData['aveP'] = avg.toFixed(2);
		resultTypeData['id'] = myIssue.resultId;
		return resultTypeData;
	};
}

/**
 * 加载概况数据
 * @description
 * @author 大雄
 * @date 2016年12月18日下午5:45:07
 */
function loadSurvey(){

	var time = $("#time").val().replace(/[^0-9]/ig, "");
	$.ajax({
		url : 'getSurveyData',
		type : 'POST',
		dataType : 'json',
		data : {
			mdate : time,
		},
		success : function(result) {
			if (result) {
				//alert(111);
				showSurvey(result);
				
			} else {
				alert("没有数据!");
			}
		},
		error : function(xhr, status) {
			alert("没有数据!");
		}
	});
}

function showSurvey(data){
	$('#buySumElectricity').text(data['buySumElectricity']);
	$('#saleSumElectricity').text(data['saleSumElectricity']);
	$('#buyClearSumElectricity').text(data['buyClearSumElectricity']);
	$('#saleClearSumElectricity').text(data['saleClearSumElectricity']);
	$('#clearNum').text(data['clearNum']);
	$('#pathMaxClearName').text(data['pathMaxClearName']);
	$('#pathMaxClearElectricity').text(data['pathMaxClearElectricity']);
	$('#pathMaxInterval').text(data['pathMaxInterval']);
	$('#pathMaxIntervalPower').text(data['pathMaxIntervalPower']);
	
	$('#survey_chart1').highcharts({
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false
        },
        colors: ['#7cb5ec', '#90ed7d', '#f7a35c', '#8085e9', '#f15c80', '#e4d354', '#8085e8', '#8d4653', '#91e8e1', '#434348'],
        title: {
            text: '购电省申报电量'
        },
        tooltip: {
            pointFormat: '<b>{point.percentage:.1f}%</b>'
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: true,distance: -40,
                    //format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                    formatter:function(percentage,point,series,total,x,y){
                    	if(this.point.y>25)
                    	return ''+this.point.name+': '+this.point.y+' %';
                    },
                    style: {
                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                    }
                },
                showInLegend: true
            }
        },
        series: [{
            type: 'pie',
            name: '百分比',
            data: data['buyDeclareChartData']
        /*[['江苏',   45.0],['上海',       26.8],{
                    name: '安徽',
                    y: 12.8,
                    sliced: true,
                    selected: true
                },
                ['山东',    8.5],
                ['福建',     6.2],
                ['浙江',   0.7]
            ]*/
        }]
    });
	
	
	$('#survey_chart2').highcharts({
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false
        },
        colors: ['#7cb5ec', '#90ed7d', '#f7a35c', '#8085e9', '#f15c80', '#e4d354', '#8085e8', '#8d4653', '#91e8e1', '#434348'],
        title: {
            text: '购电省成交电量'
        },
        tooltip: {
            pointFormat: '<b>{point.percentage:.1f}%</b>'
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                	 enabled: true,distance: -40,
                     //format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                     formatter:function(percentage,point,series,total,x,y){
                     	if(this.point.y>25)
                     	return '<b>'+this.point.name+'</b>: '+this.point.y+' %';
                     },
                     style: {
                         color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                     }
                },
                showInLegend: true
            }
        },
        series: [{
            type: 'pie',
            data: data['buyClearChartData']
        }]
    });
	$('#survey_chart3').highcharts({
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false
        },
        colors: ['#7cb5ec', '#90ed7d', '#f7a35c', '#8085e9', '#f15c80', '#e4d354', '#8085e8', '#8d4653', '#91e8e1', '#434348'],
        title: {
            text: '售电省申报电量'
        },
        tooltip: {
            pointFormat: '<b>{point.percentage:.1f}%</b>'
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                	 enabled: true,distance: -40,
                     //format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                     formatter:function(percentage,point,series,total,x,y){
                     	if(this.point.y>25)
                     	return '<b>'+this.point.name+'</b>: '+this.point.y+' %';
                     },
                     style: {
                         color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                     }
                },
                showInLegend: true
            }
        },
        series: [{
            type: 'pie',
            data: data['saleDeclareChartData']
        }]
    });
	$('#survey_chart4').highcharts({
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false
        },
        colors: ['#7cb5ec', '#90ed7d', '#f7a35c', '#8085e9', '#f15c80', '#e4d354', '#8085e8', '#8d4653', '#91e8e1', '#434348'],
        title: {
            text: '售电省成交电量'
        },
        tooltip: {
            pointFormat: '<b>{point.percentage:.1f}%</b>'
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                	 enabled: true,distance: -40,
                     //format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                     formatter:function(percentage,point,series,total,x,y){
                     	if(this.point.y>25)
                     	return '<b>'+this.point.name+'</b>: '+this.point.y+' %';
                     },
                     style: {
                         color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                     }
                },
                showInLegend: true
            }
        },
        series: [{
            type: 'pie',
            name: 'Browser share',
            data: data['saleClearChartData']
        }]
    });
	var tableSize = data['tableSize'];
	$('#div_surveytable').height(tableSize*30);
	$('#survey').height(717+tableSize*30);
	showSurveyTable(1,"购电省申报电量",data['buyDeclareTableData'],tableSize);
	showSurveyTable(2,"购电省成交电量",data['buyClearTableData'],tableSize);
	showSurveyTable(3,"售电省申报电量",data['saleDeclareTableData'],tableSize);
	showSurveyTable(4,"售电省成交电量",data['saleClearTableData'],tableSize);
	
	
	$('#survey-column').highcharts({
        chart: {
            type: 'column'
        },
        colors: ['#7cb5ec', '#90ed7d', '#f7a35c', '#8085e9', '#f15c80', '#e4d354', '#8085e8', '#8d4653', '#91e8e1', '#434348'],
        title: {
            text: '购售电申报与交易结果'
        },
        xAxis: {
            categories: data['columnCategoriesData']//['Apples', 'Oranges', 'Pears', 'Grapes', 'Bananas']
        },
        yAxis: {
            allowDecimals: false,
            min: 0,
            title: {
                text: '电量MWh'
            }
        },
        tooltip: {
            formatter: function () {
                return '<b>' + this.x + '</b><br/>' +
                    this.series.name + ': ' + this.y + 'MWh<br/>' ;//+
                   // 'Total: ' + this.point.stackTotal;
            }
        },
        plotOptions: {
            column: {
                stacking: 'normal'
            }
        },
        series: [{
            name: '申报电量',
            data: data['columnDeclareSeriesData'],//[5, 3, 4, 7, 2],
            stack: 'male'
        }, {
            name: '成交电量',
            data: data['columnClearSeriesData'],//[3, 4, 4, 2, 5],
            stack: 'male'
        }]
    });
}

function showSurveyTable(id,title,data,maxSize){
	var index = 0;
	//alert(id);
	if(data != null){
		//var str = '<tr class="th" style="border-left: 0px;" align="center"><td  colspan="2"   ">'+title+'(MWh)</td></tr>';
		var str= "" ;
		for(var key in data){
			//alert(id+"--"+key+"--"+data[key]);
			if(index%2 == 0){
				str += '<tr class="tr" style="border-left: 0px;" ><td width="50"  style="border-left: 0px;">'+key+'</td><td width="50" style="border-right: 0px;">'+data[key]+'</td></tr>';
			}else{
				str += '<tr  style="border-left: 0px;" ><td width="50"  style="border-left: 0px;">'+key+'</td><td width="50"  style="border-right: 0px;">'+data[key]+'</td></tr>';
			}
			index++;
		}
		
	}
	if(index<maxSize){
		for(var j = index;j<maxSize;j++){
			if(j%2 == 0){
				str += '<tr class="tr" style="border-left: 0px;border-right: 0px;" ><td width="50"  style="border-left: 0px;"></td><td width="50"  style="border-right: 0px;"></td></tr>';
			}else{
				str += '<tr  style="border-left: 0px;border-right: 0px;" ><td width="50"  style="border-left: 0px;"></td><td width="50"  style="border-right: 0px;"></td></tr>';
			}
		}
	}
	$('#survey_table'+id).html(str);
}