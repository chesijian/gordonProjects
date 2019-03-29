
function Path(){
	var mypath = this;
	this.lineArr;
	var linesNum = 1;
	var netLossMax = 10;
	var newPathName = "";	//新添加的通道的名称	蒋园泽	2016年11月16日15:38:45
	
	var maxNum = 3;
	var pathTableHead = '';
	//var pathTableHead = '<thead><th width="7%">通道名称</th>'+
	//'<th width="4%">首端</th>'+
	//'<th width="4%">末端</th>'+
	//'<th width="7%">级数</th>';
	/*'<th width="10%">首端区域输电费(元/千瓦时)</th>'+
	'<th width="10%">末端区域输电费(元/千瓦时)</th>'+
	'<th width="10%">首端省内输电费(元/千瓦时)</th>'+
	'<th width="10%">末端省内输电费(元/千瓦时)</th>';*/
	//if(isAllow_PathL_Btn_Add){
//		pathTableHead += '<th width="6%">操作</th>';
	//}
	//pathTableHead += '</thead>';
	var lineTableHead = '<thead><th width="7%">联络线名称</th>'+
	'<th width="5%">首端</th>'+
	'<th width="5%">末端</th>';
	/*'<th width="5%">输电费</th>'+
	'<th width="5%">网损率(%)</th>';*/
	if(isAllow_LineL_Btn_Add){
		lineTableHead += '<th width="7%">操作</th>';
	}
	lineTableHead += '</thead>';
	this.getAllPath = function(id,pathCountId){
		$.ajax({
			url : 'getAllPath?time='+$("#time").val(),
			type : 'POST',
			//data:'session='+session,
			dataType:'json',
			success : function(result) {
				if (result) {
					$("#"+pathCountId).html(result.length+"条");
					var tableNode = $("#"+id);
					tableNode.html("");
					tableNode.append(pathTableHead);
					debugger;
					if(result.length==0){
						$('#pathId').val("");
						$('#pathName').val("");
						$('#parentPrid').val("0");
					}else{
						var firstElement = result[0];
						$('#pathId').val(firstElement.id);
						$('#pathName').val(firstElement.mpath);
						$('#parentPrid').val(firstElement.priNum);
						for (var i = 0; i < result.length; i++) {
							var tr = '<tr>';
							if(i%2 == 1){
								tr = '<tr class="bgh">';
							}
							
							/* 如果是新添加的通道(newPathName不为空),并且当前循环的通道就是新添加的通道,则进行以下操作
							 * 当执行path.getCorridorPathListByPathId()时就会以新添加的通道作为参数
							 * 蒋园泽	2016年11月16日16:03:29	*/	
							if(newPathName != "" && result[i].mpath == newPathName){
								$('#pathId').val(result[i].id);
								$('#pathName').val(result[i].mpath);
								$('#parentPrid').val(result[i].priNum);
								newPathName = "";
							}
							 
							tr = tr 
							+(getTD('<a href="javascript:void(0);" class="pathNameA" style="color:#c19d30" onclick="path.getCorridorPathListByPathId(\''+result[i].id+'\',\''+result[i].mpath+'\',\''+result[i].priNum+'\')">'+(result[i].mpath == null ? "" : result[i].mpath)+'</a>'))
							+(getTD(result[i].startArea == null ? "" : result[i].startArea))
							+(getTD(result[i].endArea == null ? "" : result[i].endArea))
							+(getTD(result[i].priNum == null ? "" : result[i].priNum))
							/*+(getTD(result[i].iareaTariff == null ? "" : result[i].iareaTariff)
							+(getTD(result[i].jareaTariff == null ? "" : result[i].jareaTariff))
							+(getTD(result[i].jproTariff == null ? "" : result[i].jproTariff))
							+(getTD(result[i].iproTariff == null ? "" : result[i].iproTariff)));*/
							
							 if(isAllow_PathL_Btn_Add&&isFuture){
								 var editAction = 'edit';
								 var copyAction = 'copy';
								 tr+=(getTD('<a class="btn3" style="background-color: rgb(191, 87, 141);" href="javascript:void(0);" onclick="path.editPath(\''+result[i].id+'\',\''+editAction+'\')">编辑</a>&nbsp;&nbsp;' + '<a class="btn3" href="javascript:void(0);" onclick="path.deletePath(\''+result[i].mpath+'\',\''+result[i].id+'\',\''+result[i].priNum+'\')">删除</a>&nbsp;&nbsp;'+'<a class="btn3" style="background-color: rgb(191, 87, 141);" href="javascript:void(0);" onclick="path.editPath(\''+result[i].id+'\',\''+copyAction+'\')">拷贝</a>'));	 
							 }else if(isAllow_PathL_Btn_Add&&isFuture==false){
								 var editAction = 'edit';
								 var copyAction = 'copy';
								 tr+=(getTD('<a class="btn3" href="javascript:void(0);" onclick="path.deletePath(\''+result[i].mpath+'\',\''+result[i].id+'\',\''+result[i].priNum+'\')">删除</a>&nbsp;&nbsp;'+'<a class="btn3" style="background-color: rgb(191, 87, 141);" href="javascript:void(0);" onclick="path.editPath(\''+result[i].id+'\',\''+copyAction+'\')">拷贝</a>'));
							 }
							tr+=('</tr>');
							tableNode.append(tr);
						}
					}
					var pathId = $('#pathId').val();
					var pathName = $('#pathName').val();
					var priNum = $('#parentPrid').val();
					path.getCorridorPathListByPathId(pathId,pathName,priNum);
				} 
			},
			error : function(xhr, status) {
				alert("系统错误!");
			}
		});
	};

	this.editPath = function(pathId,action){
		$("#action").val(action);
		$.ajax({
			url : 'editPath',
			type : 'POST',
			data :'pathId='+pathId,
			dataType:'json',
			success : function(result) {
				$("#addPathName").val(result.mpath);
				$("#startLine").val(result.startArea);
				$("#endLine").val(result.endArea);
				$("#IAreaTariff").val(result.iareaTariff);
				$("#JAreaTariff").val(result.jareaTariff);
				$("#IProTariff").val(result.iproTariff);
				$("#JProTariff").val(result.jproTariff);
				$("#sortId").val(result.sort);
				$("#priNum").val(result.priNum);
				$("#pathId").val(pathId);
				document.getElementById("tk").style.display = 'block';
			},
			error : function(xhr, status) {
				alert("系统错误!");
			}
		});
	};
	
	this.editCorridorPath = function(corridorPathId,action){
		$('#corriAction').val(action);
		$('#corridorPathId').val(corridorPathId);
		$('#dialogTitle').html($('#parentPathName').html());
		$("#pathPri").html("");
		var priNum = $('#parentPrid').val();
		for(var i=1;i<=priNum;i++){
			var option = '<option value="'+i+'">'+i+'</option>';
			$("#pathPri").append(option);
		}
		$.ajax({
			url : 'editCorPath',
			type : 'POST',
			data :'id='+corridorPathId,
			dataType:'json',
			success : function(result) {
				$("#pathPri").val(result.pathPri);
				$("#sort_").val(result.sort);
				$("#priceRatioA").val(result.priceRatioA);
				$("#priceRatioB").val(result.priceRatioB);
				$('#transLoss').val(result.transLoss);
				//debugger;
				var mdirection =result.mdirection;
				var myDatas =eval(result.data);
				//var myDatas = result.data;
				if(mdirection.length>1){
					
					for(var i=1;i<=mdirection.length;i++){
						if(i>1){
							path.addLineSelect();
						}
						$("#lineSelect"+i).val(result["corhr"+i]);
						if(mdirection[i-1]=="+"){
							$("#lineSelectOR"+i).val(1);
						}else{
							$("#lineSelectOR"+i).val(2);
						}
						if(myDatas!=null && myDatas.length>(i-1)){
							$("#tielineTariff"+i).val(myDatas[i-1].tielineTariff);
							$("#netLoss"+i).val(myDatas[i-1].netLoss);
						}else{
							$("#tielineTariff"+i).val("");
							$("#netLoss"+i).val("");
						}
						
					}
				}else{
					$("#lineSelect1").val(result.corhr1);
					$("#lineSelectOR1").val(result.mdirection);
					if(myDatas!=null){
						$("#tielineTariff1").val(myDatas[0].tielineTariff);
						$("#netLoss1").val(myDatas[0].netLoss);
					}
					
				}
				
				/*if(myDatas!=null && myDatas.length>0){
					for (var i = 1; i <= myDatas.length; i++) {
						if(i>1){
							path.addWritePanel();
						}
						$("#tielineTariff"+i).val(myDatas[i-1].tielineTariff);
						$("#netLoss"+i).val(myDatas[i-1].netLoss);
					}
				}else{
					$("#tielineTariff1").val("");
					$("#netLoss1").val("");
				}*/
				document.getElementById("tck").style.display = 'block';
			},
			error : function(xhr, status) {
				alert("系统错误!");
			}
		});
	};
	
	this.getCorridorPathListByPathId = function(pathId,pathName,priNum){
		var pathTableHead = '<thead><th width="7%">通道名称</th>'+
		'<th width="3%">优先级</th>'+
		'<th width="7%">联络线1</th>'+
		'<th width="7%">联络线2</th>'+
		'<th width="7%">联络线3</th>'+
		/*'<th width="7%">联络线4</th>'+
		'<th width="7%">联络线5</th>'+*/
		'<th width="6%">跨区网损(%)</th>'+
		'<th width="6%">折价系数A</th>'+
		'<th width="6%">折价系数B</th>';
		if(isAllow_PathL_Btn_Add){
			pathTableHead += '<th width="15%">操作</th>';
		}
		pathTableHead += '</thead>';
		$('#parentPrid').val(priNum);
		$('#parentPathId').val(pathId);
		
		if(pathId==""){
			$('#parentPathName').html("路径配置");
			$("#corridorPathCount").html("0条");
			var tableNode = $("#corridorPathTable");
			tableNode.html("");
			tableNode.append(pathTableHead);
		}else{
			$('#parentPathName').html(pathName+'--路径配置');
			$.ajax({
				url : 'getCorridorPathList',
				type : 'POST',
				data :'pathId='+pathId,
				dataType:'json',
				success : function(result) {
					
					if (result) {
						$("#corridorPathCount").html(result.length+"条");
						var tableNode = $("#corridorPathTable");
						tableNode.html("");
						tableNode.append(pathTableHead);
						debugger;
						for (var i = 0; i < result.length; i++) {
							var tr = '<tr>';
							if(i%2 == 1){
								tr = '<tr class="bgh">';
							}
							
							tr = tr 
							+(getTD(pathName))
							+(getTD(result[i].pathPri == null ? "" :result[i].pathPri))
							+(getTD(result[i].corhr1 == null ? "" : result[i].corhr1+result[i].mdirection[0]))
							+(getTD(result[i].corhr2 == null ? "" : result[i].corhr2+result[i].mdirection[1]))
							+(getTD(result[i].corhr3 == null ? "" : result[i].corhr3+result[i].mdirection[2]))
							/*+(getTD(result[i].corhr4 == null ? "" : result[i].corhr4+result[i].mdirection[3]))
							+(getTD(result[i].corhr5 == null ? "" : result[i].corhr5+result[i].mdirection[4]))*/
							+(getTD(result[i].transLoss == null ? "" : result[i].transLoss))
							+(getTD(result[i].priceRatioA == null ? "" : result[i].priceRatioA))
							+(getTD(result[i].priceRatioB == null ? "" : result[i].priceRatioB));
							var editAction = 'edit';
							var copyAction = 'copy';
							if(isAllow_PathL_Btn_Add){
								tr+=(getTD('<a class="btn3" style="background-color: rgb(191, 87, 141);" href="javascript:void(0);" onclick="path.editCorridorPath(\''+result[i].id+'\',\''+editAction+'\')">编辑</a>&nbsp;&nbsp;' + '<a class="btn3" href="javascript:void(0);" onclick="path.deleteCorridorPath(\''+result[i].id+'\')">删除</a>&nbsp;&nbsp;'+'<a class="btn3" style="background-color: rgb(191, 87, 141);" href="javascript:void(0);" onclick="path.editCorridorPath(\''+result[i].id+'\',\''+copyAction+'\')">拷贝</a>'));	
							}
							tableNode.append(tr);
						}
					} 
				},
				error : function(xhr, status) {
					alert("系统错误!");
				}
			});
		}
		
	};
	

	this.editLine = function(lineId){
		$.ajax({
			url : 'editLine',
			type : 'POST',
			data :'lineId='+lineId,
			dataType:'json',
			success : function(result) {
				$("#addLineName").val(result.mcorhr);
				$("#lstartLine").val(result.startArea);
				$("#lendLine").val(result.endArea);
				$("#rate").val(result.rate);
				$("#tielinetariff").val(result.tielinetariff);
				$("#lineId").val(lineId);
				$("#lineSortId").val(result['sort']);
				document.getElementById("tk_line").style.display = 'block';
			},
			error : function(xhr, status) {
				alert("系统错误!");
			}
		});
		
	};

	this.deletePath = function(name,id,priNum){
		var pd=confirm("确定要删除当前通道吗？");
		if(pd==true){
			$.ajax({
	   			url : 'deletePath',
	   			type : 'POST',
	   			data :{
	   				mpath:name,
	   				pathId:id,
	   				time:$("#time").val().replace(/[^0-9]/ig, "")
	   			},
	   			dataType:'json',
	   			success : function(result) {
	   				if (result) {
	   					alert("删除成功");
	   					mypath.getAllPath('pathTable','pathCount');
	   					mypath.getCorridorPathListByPathId(id,name,priNum);
	   				};
	   			},
	   			error : function(xhr, status) {
	   				alert("系统错误!");
	   			}
	   		});
		}
		 
		
		
		
		
	};
	
	this.deleteCorridorPath = function(id){
		var pathName = $('#parentPathName').html().split('-')[0];
		var pathId =$("#parentPathId").val();
		var priNum =$("#parentPrid").val();
		var pd=confirm("确定要删除该条数据吗？");
		if(pd==true){
			$.ajax({
				url : 'deleteCorPath',
				type : 'POST',
				data :'id='+id,
				dataType:'json',
				success : function(result) {
					if (result) {
						alert("删除成功");
						mypath.getCorridorPathListByPathId(pathId,pathName,priNum);
					};
				},
				error : function(xhr, status) {
					alert("系统错误!");
				}
			});
		}
		
	};
	
	this.getAllLine = function(id,lineCountId,selectId){
		
		$.ajax({
			url : 'getAllLine',
			type : 'POST',
			dataType:'json',
			success : function(result) {
				if (result) {
					
					if(selectId){
						$('#'+selectId).html("");
					}
					path.lineArr = result;
					$("#"+lineCountId).html(result.length+"条");
					var tableNode = $("#"+id);
					tableNode.html("");
					tableNode.append(lineTableHead);
					for (var i = 0; i < result.length; i++) {
						var tr = '<tr>';
						if(i%2 == 1){
							tr = '<tr class="bgh">';
						}
						tr = tr 
						+(getTD(result[i].mcorhr == null ? "" : result[i].mcorhr))
						+(getTD(result[i].startArea == null ? "" : result[i].startArea))
						+(getTD(result[i].endArea == null ? "" : result[i].endArea));
						/*+(getTD(result[i].tielinetariff == null ? "" : result[i].tielinetariff))
						+(getTD(result[i].rate == null ? "" : result[i].rate));*/
						if(isAllow_LineL_Btn_Add){
							 tr+=(getTD('<a class="btn3" style="background-color: rgb(191, 87, 141);" href="javascript:void(0);" onclick="path.editLine(\''+result[i].id+'\')">编辑</a>&nbsp;&nbsp;' + '<a class="btn3" href="javascript:void(0);" onclick="path.deleteLine(\''+result[i].mcorhr+'\')">删除</a>')); 
						 }
						tr+=('</tr>');
						tableNode.append(tr);
						
						if(selectId){
							var name = result[i].mcorhr;
							var option = '<option value="'+name+'">'+name+'</option>';
							$('#'+selectId).append(option);
						}
					}
					
				} 
			},
			error : function(xhr, status) {
				alert("系统错误!");
			}
		});
	};
	
	this.deleteLine = function(name){
		var pd=confirm("确定要删除当前联络线吗？");
		if(pd==true){
			$.ajax({
				url : 'deleteLine',
				type : 'POST',
				data :'mcorhr='+name,
				dataType:'json',
				success : function(result) {
					if (result) {
						alert("删除成功");
						mypath.getAllLine('lineTable', 'lineCount','lineSelect1');
					};
				},
				error : function(xhr, status) {
					alert("系统错误!");
				}
			});
		}
		
	};
	
	this.savePath = function(nameId,startId,endId,linesDivId){
		debugger;
		var action = $("#action").val();
		var name = $("#"+nameId).val();
		var start = $("#"+startId).val();
		var end = $("#"+endId).val();
		var pathId =$("#pathId").val();
		var IAreaTariffStr =new Number($("#IAreaTariff").val());
		var IAreaTariff = IAreaTariffStr.toFixed(1);
		
		var JAreaTariffStr =new Number($("#JAreaTariff").val());
		var JAreaTariff = JAreaTariffStr.toFixed(1);
		var IProTariffStr =new Number($("#IProTariff").val());
		var IProTariff = IProTariffStr.toFixed(1);
		var JProTariffStr =new Number($("#JProTariff").val());
		var JProTariff = JProTariffStr.toFixed(1);
		var sortId =$("#sortId").val();
		var priNum =$("#priNum").val();
		//如果尾端与末端相同则放弃提交
		if(isSame()){
			alert("首端和末端不能在同一区域");
			return false;
		}
		if(sortId == ""){
			sortId = 0;
		}
		
		if(priNum == ""){
			alert('级数不能为空！');
			return false;
		}
		if(priNum=='0'){
			alert('级数不能为0！');
			return false;
		}
		var path = {'mpath':name,'startArea':start,'endArea':end,'iareaTariff':IAreaTariff,'jareaTariff':JAreaTariff,'iproTariff':IProTariff,'jproTariff':JProTariff,'sort':sortId,'priNum':priNum,'mnum':'0'};
		$.ajax({
			url : 'addPath',
			type : 'POST',
			data :{
				pathDefine:JSON.stringify(path),
				pathId:pathId,
				time:$("#time").val(),
				action:action
			},     
			dataType:'json',
			success : function(result) {
				if ("success"==result) {
					alert('提交成功');
					mypath.displayAddPanel('tk');
					mypath.getAllPath('pathTable','pathCount');
					$("#"+nameId).val("");
					document.getElementById(startId).options[0].selected = true;
					document.getElementById(endId).options[0].selected = true;
					newPathName = name;	//把新添加的通道名称存储为全局变量	蒋园泽	2016年11月16日15:42:45
				}else{
					alert(result);
				};
			},
			error : function(xhr, status) {
				alert("系统错误!");
			}
		});
	};
	
	function refreshData(){
		if(_isState){
			declare.loadArea();
		}
		loadTreeData();
	}
	
	this.saveCorridorPath = function(nameId,startId,endId,linesDivId){
		
		var action = $('#corriAction').val();
		var corridorPathId = $('#corridorPathId').val();
		var priNum = $('#parentPrid').val();
		var pathId =$("#parentPathId").val();
		var pathName = $('#parentPathName').html().split('-')[0];
		var pathPri = $("#pathPri").val();
		var sort = $("#sort_").val();
		var priceRatioA = $("#priceRatioA").val();
		var priceRatioB = $("#priceRatioB").val();
		var transLoss = $('#transLoss').val();
		
		if(priNum == ""){
			priNum = 0;
		}else{
			priNum = parseInt(priNum);
		}
		if(pathPri == ""){
			pathPri = 0;
		}else{
			pathPri = parseInt(pathPri);
		}
		if(pathPri>priNum){
			alert('优先级不能大于'+priNum+"!");
			return;
		}
		if(priceRatioA==""){
			priceRatioA=0;
		}
		if(priceRatioB==""){
			priceRatioB=0;
		}
		if(sort==""){
			sort = 0;
		}
		if(transLoss==""){
			transLoss = 0;
		}
		var lines = new Array();
		var writesArray = new Array();
		//检测通道是否重复
		for(var i=0;i<linesNum;i++){
			var corhr = $("#lineSelect"+(i+1)).val();
			for(var j=0;j<linesNum;j++){
				var temp = $("#lineSelect"+(j+1)).val();
				
				if(corhr==temp&&i!=j){
					alert('成员'+(i+1)+' 与 成员'+(j+1)+' 通道路径重复了！');
					return;
				}
			}
		}
		
		for (var i = 0; i < linesNum; i++) {
			var mdirection = $("#lineSelectOR"+(i+1)).val();
			var corhr = $("#lineSelect"+(i+1)).val();
			var line = {'mdirection':mdirection,'corhr':corhr};
			lines.push(line);
		}
		//debugger;
		for (var i = 0; i < linesNum; i++) {
			/*var tielineTariff = $("#tielineTariff"+(i+1)).val();
			var netLoss = $("#netLoss"+(i+1)).val();*/
			var tielineTariff = $("#tielineTariff"+(i+1)).val()==""? "0.0":$("#tielineTariff"+(i+1)).val();
			var netLoss = $("#netLoss"+(i+1)).val() == ""? "0.0":$("#netLoss"+(i+1)).val();
			var writeContent = {'tielineTariff':tielineTariff,'netLoss':netLoss};
			writesArray.push(writeContent);
		}
		if(sort == ""){
			sort = 0;
		}
		if(pathPri == ""){
			pathPri = 0;
		}
		if(priceRatioB == ""){
			priceRatioB = 0.0;
		}
		if(priceRatioA == ""){
			priceRatioA = 0.0;
		}
		var mdirection = "";
		var corhrString = "";
		var corridorPath = {'pathUid':pathId,'priceRatioA':priceRatioA,'priceRatioB':priceRatioB,'sort':sort,'pathPri':pathPri,'transLoss':transLoss};
		for (var i = 0; i < lines.length; i++) {
			mdirection += (lines[i].mdirection==1?'+':'-');
			corridorPath['corhr'+(i+1)] = lines[i].corhr;
			corhrString+=lines[i].corhr;
		}
		corridorPath['mdirection'] = mdirection;
		$.ajax({
			url : 'addCorridorPath',
			type : 'POST',
			
			data :{
				pathDefine:JSON.stringify(corridorPath),
				id:corridorPathId,
				action:action,
				newStr:corhrString+mdirection,
				datas:JSON.stringify(writesArray)
			},     
			dataType:'json',
			success : function(result) {
				if ("success"==result) {
					alert('提交成功！');
					mypath.displayAddPanel('tck');
					mypath.getCorridorPathListByPathId(pathId,pathName,priNum);
					$("#"+nameId).val("");
					document.getElementById("lineSelect1").options[0].selected = true;
					document.getElementById("lineSelectOR1").options[0].selected = true;
				}else{
					alert(result);
				};
			},
			error : function(xhr, status) {
				alert("系统错误!");
			}
		});
	};
	
	this.saveLine = function(nameId,startId,endId){
		var name = $("#"+nameId).val();
		var start = $("#"+startId).val();
		var end = $("#"+endId).val();
		var tielinetariff = $("#tielinetariff").val();
		var rate =new Number($("#rate").val());
		rate = rate.toFixed(1);
		var sortId =$("#lineSortId").val();
		var lineId =$("#lineId").val();
		//alert(sortId);
		if(sortId == ""){
			sortId = 0;
		}
		var line = {'mcorhr':name,'startArea':start,'endArea':end,'rate':rate,'sort':sortId,'tielinetariff':tielinetariff};
		$.ajax({
			url : 'addLine',
			type : 'POST',
			data :{
				lineDefine:JSON.stringify(line),
				lineId:lineId
			},
			dataType:'json',
			success : function(result) {
				if (result) {
					mypath.displayAddLinePanel('tk_line');
//					alert("添加成功");
					mypath.getAllLine('lineTable', 'lineCount','lineSelect1');
					$("#"+nameId).val("");
					$("#rate").val("");
					document.getElementById(startId).options[0].selected = true;
					document.getElementById(endId).options[0].selected = true;
				};
			},
			error : function(xhr, status) {
				alert("系统错误!");
			}
		});
	};
	
	this.addLineSelect = function(){
		if(linesNum >= maxNum){
			return ;
		}
		linesNum += 1;
		var lines =this.lineArr;
		var linesOption ="";
		for(var i=0;i<lines.length;i++){
			linesOption+=' <option value ="'+lines[i].mcorhr+'">'+lines[i].mcorhr+'</option>';
		}
		$("#lineSelectDiv .addPath").remove();
		var lineSelectDiv = '<div id="linePanelDiv'+linesNum+'" class="fl pdt20 linePanel" ><span id="cygs">第'+linesNum+'段</span>&nbsp;&nbsp;<select id="lineSelect'+linesNum+'" style="width: 90px;">'+linesOption+'</select>'
			+ '<span>&nbsp;&nbsp;成员方向&nbsp;&nbsp;</span><select id="lineSelectOR'+linesNum+'" style="width: 50px;">'
			+ '<option value ="1">正</option>'
			+ '<option value ="2">反</option>' + '</select>'
			+ '&nbsp;输电费(元/兆瓦时)&nbsp;&nbsp;<input type="text" id="tielineTariff'+linesNum+'" size="8" value="0.0" />'
			+ '&nbsp;网损(%)&nbsp;&nbsp;<input type="text" size="8" id="netLoss'+linesNum+'" value="0.0" />'
			+ '<a class="btn3 addPath" href="javascript:void(0);" style="margin-left: 4px" onclick="path.addLineSelect()">+</a>'
			+ '<a class="btn3 delPath" href="javascript:void(0);" style="margin-left: 4px" onclick="path.delLineSelect(\'linePanelDiv'+linesNum+'\',\''+linesNum+'\')"> - </a></div>' ;
        if(linesNum >= maxNum){
			lineSelectDiv = '<div id="linePanelDiv'+linesNum+'" class="fl pdt20 linePanel" ><span id="cygs">成员'+linesNum+'</span>&nbsp;&nbsp;<select id="lineSelect'+linesNum+'" style="width: 90px;">'+linesOption+'</select>'
			+ '<span>&nbsp;&nbsp;成员方向&nbsp;&nbsp;</span><select id="lineSelectOR'+linesNum+'" style="width: 50px;">'
			+ '<option value ="1">正</option>'
			+ '<option value ="2">反</option>' + '</select>'
			+ '&nbsp;输电费(元/兆瓦时)&nbsp;&nbsp;<input type="text" id="tielineTariff'+linesNum+'" size="8" value="0.0" />'
			+ '&nbsp;网损(%)&nbsp;&nbsp;<input type="text" size="8" id="netLoss'+linesNum+'" value="0.0" />'
			+ '<a class="btn3 delPath" href="javascript:void(0);" style="margin-left: 4px" onclick="path.delLineSelect(\'linePanelDiv'+linesNum+'\',\''+linesNum+'\')"> - </a></div>'
			+'</div><div class="cl"></div>';
        }
		$("#lineSelectDiv").append(lineSelectDiv);
		
	};
	
	//删除某一条	蒋园泽	2016年11月16日19:15:50
	this.delLineSelect = function(linesId,curryLines){
		curryLines = parseInt(curryLines);
		var lineArray = new Array();
		debugger;
		if(curryLines==linesNum){
			$("#linePanelDiv"+linesNum).remove();
			linesNum -= 1;
			for (var i = 0; i < linesNum; i++) {
				var mdirection = $("#lineSelectOR"+(i+1)).val();
				var corhr = $("#lineSelect"+(i+1)).val();
				var tielineTariff = $('#tielineTariff'+(i+1)).val();
				var netLoss = $('#netLoss'+(i+1)).val();
				var line = {'mdirection':mdirection,'corhr':corhr,'tielineTariff':tielineTariff,'netLoss':netLoss};
				lineArray.push(line);
			}
		}else{
			for (var i = curryLines; i < linesNum; i++) {
				var mdirection = $("#lineSelectOR"+(i+1)).val();
				var corhr = $("#lineSelect"+(i+1)).val();
				var tielineTariff = $('#tielineTariff'+(i+1)).val();
				var netLoss = $('#netLoss'+(i+1)).val();
				var line = {'mdirection':mdirection,'corhr':corhr,'tielineTariff':tielineTariff,'netLoss':netLoss};
				lineArray.push(line);
			}
			
			for(var i = curryLines; i <= linesNum; i++){
				$("#linePanelDiv"+i).remove();
			}
			linesNum -= 1;
		}		
		
		var lines =this.lineArr;
		var linesOption ="";
		for(var i=0;i<lines.length;i++){
			linesOption+=' <option value ="'+lines[i].mcorhr+'">'+lines[i].mcorhr+'</option>';
		}
		for (var i = curryLines; i <= linesNum; i++) {
			var lineSelectDiv = '<div id="linePanelDiv'+i+'" class="fl pdt20 linePanel" ><span id="cygs">第'+i+'段</span>&nbsp;&nbsp;<select id="lineSelect'+i+'" style="width: 90px;">'+linesOption+'</select>'
			+ '<span>&nbsp;成员方向&nbsp;&nbsp;&nbsp;</span><select id="lineSelectOR'+i+'" style="width: 50px;">'
			+ '<option value ="1">正</option>'
			+ '<option value ="2">反</option>' + '</select>'
			+ '&nbsp;输电费(元/兆瓦时)&nbsp;&nbsp;<input type="text" id="tielineTariff'+linesNum+'" size="8" value="0.0"/>'
			+ '&nbsp;网损(%)&nbsp;&nbsp;<input type="text" size="8" id="netLoss'+linesNum+'" value="0.0"/>'
			+ '<a class="btn3 addPath" href="javascript:void(0);" style="margin-left: 4px" onclick="path.addLineSelect()">+</a>'
			+ '<a class="btn3 delPath" href="javascript:void(0);" style="margin-left: 4px" onclick="path.delLineSelect(\'linePanelDiv'+i+'\',\''+i+'\')"> - </a></div>' ;
			$("#lineSelectDiv").append(lineSelectDiv);
			
			$('#lineSelect'+i).val(lineArray[i-curryLines].corhr);
			$('#lineSelectOR'+i).val(lineArray[i-curryLines].mdirection==1?'+':'-');
			$('#tielineTariff'+i).val(lineArray[i-curryLines].tielineTariff);
			$('#netLoss'+i).val(lineArray[i-curryLines].netLoss);
		}
		
		$("#lineSelectDiv .addPath").remove();
		$("#lineSelectDiv .delPath").remove();
		$("#lineSelectDiv #cygs").remove();
		
		var linePanels = $("#lineSelectDiv .linePanel");
		for (var i = 0; i < linePanels.length; i++) {
			var linePanel = $(linePanels[i]);
			linePanel.prepend('<span id="cygs">第'+(i+2)+'段</span>');
			var addA = '<a class="btn3 addPath" href="javascript:void(0);" style="margin-left: 4px" onclick="path.addLineSelect()">+</a>';
			var delA = '<a class="btn3 delPath" href="javascript:void(0);" style="margin-left: 4px" onclick="path.delLineSelect(\''+linePanel.attr("id")+'\',\''+(i+2)+'\')"> - </a></div>';
			if(i != linePanels.length-1){
				$(linePanels[i]).append(delA);
			}else{
				$(linePanels[i]).append(addA + delA);
			}
		}
		if(linePanels.length == 0){
			$("#firstSelectPanel").append('<a class="btn3 addPath" href="javascript:void(0);" style="margin-left: 4px" onclick="path.addLineSelect()">+</a>');
		}
	}
	
	this.getSelectLine = function(){
		$.ajax({
			url : 'getAllLine',
			type : 'POST',
			dataType:'json',
			success : function(result) {
				if (result) {
					var selectId = "lineSelect"+linesNum;
					if(selectId){
						$('#'+selectId).html("");
					}
					
					for (var i = 0; i < result.length; i++) {
						
						
						if(selectId){
							var name = result[i].mcorhr;
							var option = '<option value="'+name+'">'+name+'</option>';
							$('#'+selectId).append(option);
						}
					}
					
				} 
			},
			error : function(xhr, status) {
				alert("系统错误!");
			}
		});
	};

	
	this.addPath = function(id){
		debugger;
		if(id=='tk'){
			$("#pathId").val("");
		}else if(id=='tck'){
			$('#dialogTitle').html($('#parentPathName').html());
            var priNum = $('#parentPrid').val();
			$("#pathPri").html("");
			for(var i=1;i<=priNum;i++){
				var option = '<option value="'+i+'">'+i+'</option>';
				$("#pathPri").append(option);
			}
		}
		document.getElementById(id).style.display = 'block';
	};
	
	this.displayAddPanel = function(id){
		document.getElementById(id).style.display = 'none';
		$('#corriAction').val("");
		$("#addPathName").val("");
		$("#startLine").val("");
		$("#endLine").val("");
		$("#priNum").val("");
		$("#IAreaTariff").val("0.0");
		$("#JAreaTariff").val("0.0");
	    $("#IProTariff").val("0.0");
		$("#JProTariff").val("0.0");
		$("#sortId").val("");
		$("#pathId").val("");
		$("#pathPri").val("");
		$("#sort_").val("");
		$("#priceRatioA").val("");
		$("#priceRatioB").val("");
		$('#transLoss').val("");
		if(id=='tck'){
			$('#corridorPathId').val('');
		}
		if(linesNum > 1){
			
			$("#firstSelectPanel").append('<a class="btn3 addPath" href="javascript:void(0);" onclick="path.addLineSelect()">+</a>');
		}
		$("#lineSelectDiv .linePanel").remove();
		linesNum = 1;
		
		$('#tielineTariff1').val("0.0");
	    $('#netLoss1').val("0.0");
		/*if(writeNum > 1){
			$("#firstWritePanel").append('<a class="btn3 addPath" href="javascript:void(0);" onclick="path.addWritePanel()">+</a>');
		}
		$("#writeDiv .writePanel").remove();
		writeNum = 1;*/
	};
	
	this.addLine = function(id){
		$("#lineId").val("");
		$("#lineId").val('');
		$("#lineSortId").val("");
		document.getElementById(id).style.display = 'block';
	};
	
	this.displayAddLinePanel = function(id){
		document.getElementById(id).style.display = 'none';
		$("#addLineName").val("");
		$("#lstartLine").val("");
		$("#lendLine").val("");
		$("#rate").val("");
		$("#tielinetariff").val("");
		
	};
	
	function getTD(val){
		return '<td align="center" width="6%">'+val+'</td>';
	}
	/*var writeNum = 1;
	this.addWritePanel = function(){
		if(writeNum >= netLossMax){
			return ;
		}
		writeNum += 1;
		$("#writeDiv .fenge").remove();
		$("#writeDiv .addPath").remove();
		var writeSelectDiv = '<div id="writePanelDiv'+writeNum+'" class="fl pdt20 writePanel" ><span id="sd">第'+writeNum+'段</span>&nbsp;&nbsp;&nbsp;输电费&nbsp;&nbsp;<input style="width: 80px;border:1px solid #C3D0DD;" type="text" id="tielineTariff'+writeNum+'" size="8" />'
				+ '&nbsp;网损(%)&nbsp;&nbsp;<input style="width: 80px;border:1px solid #C3D0DD;" type="text" size="8" id="netLoss'+writeNum+'" />'
				+ '<a class="btn3 addPath" href="javascript:void(0);" style="margin-left: 4px" onclick="path.addWritePanel()">+</a></div>'
		        + '<span class="fenge" style="color:#dcdcdb;">———————————————————————————</span></div>';
				
		$("#writeDiv").append(writeSelectDiv);

	};*/
}













function areaSelect(url,selectNodes){
	
	$.ajax({
		url : url,
		type : 'POST',
		dataType:'json',
		success : function(result) {
			if (result) {
				for (var j = 0; j < selectNodes.length; j++) {
					var selectNode = selectNodes[j];
					selectNode.html("");
					for (var i = 0; i < result.length; i++) {
						var name = result[i].area;
						var option = '<option value="'+name+'">'+name+'</option>';
						selectNode.append(option);
					}
				}
			} 
		},
		error : function(xhr, status) {
			alert("系统错误!");
		}
	});
	
}