function trade() {
	var myTrade = this;
	this.selectedDalare;
	
	
	// 获取交易单数据
	this.getTradeDatas = function() {
		
		var ddate = parent.document.getElementById("time").value;
		mask();
		$.ajax({
			url : baseUrl+"/report/getTradeDatas",
			type : 'POST',
			dataType : 'json',
			data : {
				ddate:ddate
			},
			success : function(result) {
				$("#declareData").empty();	
				$("#dealData").empty();	
				$("#executeData").empty();	
				$("#pathData").empty();	
				if (result != null) {
					var declareData = result.declareData;
					var areas = result.areas;
					var userIds = result.userIds;
					var linePaths = result.linePaths;
					var lineExecutes = result.lineExecutes;
					var dealData = result.dealData;
					var pathData = result.pathData;
					var executeData = result.executeData;
					var issuerId =result.issuerId;
					$("#dateStr").text(result.dateStr);
					
					var tableStr="";
					for(var i=0;i<declareData.length;i++){
						tableStr += "<div class='content'>"+(i+1)+"."+areas[i]+"</div>";
						tableStr += "<table class='table table-hover' border =1><thead><th>开始时段</th><th>结束时段</th><th>电力（MW）</th><th>电价（元/MWh）</th></thead>";
						for(var timeStr in declareData[i]){
							var times = timeStr.split("a");
							for(var j=0; j<declareData[i][timeStr].length; j++){
								tableStr+="<tr><td>"+times[0]+"</td><td>"+times[1]+"</td>";
								tableStr+="<td>"+declareData[i][timeStr][j]["电力"]+"</td>";
								tableStr+="<td>"+declareData[i][timeStr][j]["电价"]+"</td></tr>";
							}
						}
						tableStr+="</table>";
						tableStr+="<div style='margin-top: 0px;text-align: right;'>"
							       +"<img src='/TradingMarket/manager/getImage?id="+userIds[i]+"&sign=false'></div>";
					}
					
					var dealTableStr="";
					var a = 0;
					//console.debug(dealData);
					for(var i=0;i<dealData.length;i++){
						
						if(dealData[i]==null){
							continue;
						}
						a++;
						dealTableStr += "<div class='content'>"+a+"."+areas[i]+"</div>";
						dealTableStr += "<table class='table table-hover' border =1><thead><th>成分</th><th>开始时段</th><th>结束时段</th><th>电力（MW）</th><th>电价（元/MWh）</th></thead>";
						for(var corridor in dealData[i]){
							
							for(var timeStr in dealData[i][corridor]["电力"]){
								var times = timeStr.split("a");
								var power = dealData[i][corridor]["电力"][timeStr]==null||dealData[i][corridor]["电力"][timeStr]==undefined? "":dealData[i][corridor]["电力"][timeStr];
								var price = dealData[i][corridor]["电价"][timeStr]==null||dealData[i][corridor]["电价"][timeStr]==undefined? "":dealData[i][corridor]["电价"][timeStr];
								dealTableStr+="<tr><td>"+corridor+"</td>";
								dealTableStr+="<td>"+times[0]+"</td><td>"+times[1]+"</td>";
								dealTableStr+="<td>"+power+"</td>";
								dealTableStr+="<td>"+price+"</td></tr>";
							}
						}
						dealTableStr+="</table>";
					}
					dealTableStr+="<div style='margin-top: 0px;text-align: right;'>"
					       +"<img src='/TradingMarket/manager/getImage?id="+issuerId+"&sign=false'></div>";
					
					var executeTableStr = myTrade.createTable(executeData,lineExecutes);
					var pathTableStr = myTrade.createTable(pathData,linePaths);
					$("#declareData").append(tableStr);
					$("#dealData").append(dealTableStr);	
					$("#executeData").append(executeTableStr);	
					$("#pathData").append(pathTableStr);
				}
				unmask();
				
			},
			error : function(xhr, status) {
				unmask();
				alert("系统错误!");
			}
		});
	};
	
	
	this.createTable = function(executeData,areas) {
		var executeTableStr="";
		var b =0;
		for(var i=0;i<executeData.length;i++){
			console.info(executeData[i]);
			if(executeData[i]==null){
				continue;
			}
			b++;
			executeTableStr += "<div class='content'>"+b+"."+areas[i]+"</div>";
			executeTableStr += "<table class='table table-hover' border =1><thead><th>直流</th><th>成分</th><th>开始时段</th><th>结束时段</th><th>电量（MWh）</th></thead>";
			for(var corridor in executeData[i]){
				var corridors = corridor.split("-");
				for(var timeStr in executeData[i][corridor]){
					var times = timeStr.split("-");
					executeTableStr+="<tr><td>"+corridors[1]+"</td><td>"+corridors[0]+"</td>";
					executeTableStr+="<td>"+times[0]+"</td><td>"+times[1]+"</td>";
					executeTableStr+="<td>"+executeData[i][corridor][timeStr]+"</td></tr>";
				}
			}
			executeTableStr+="</table>";
		}
		return executeTableStr;
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
	
	
	