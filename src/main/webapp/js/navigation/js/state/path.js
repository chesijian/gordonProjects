
function Path(){
	
	var mypath = this;
	
	var linesNum = 1;
	
	var maxNum = 5;
	
	var pathTableHead = '<thead><th width="7%">通道名称</th>'+
		'<th width="7%">首端</th>'+
		'<th width="7%">末端</th>'+
		'<th width="4%">成员个数</th>'+
		'<th width="4%">成员方向</th>'+
		'<th width="7%">联络线1</th>'+
		'<th width="7%">联络线2</th>'+
		'<th width="7%">联络线3</th>'+
		'<th width="7%">联络线4</th>'+
		'<th width="7%">联络线5</th>'+
		'<th width="7%">操作</th></thead>';
	
	var lineTableHead = '<thead><th width="7%">联络线名称</th>'+
	'<th width="7%">首端</th>'+
	'<th width="7%">末端</th>'+
	'<th width="7%">操作</th></thead>';
	
	this.getAllPath = function(id,pathCountId){
		$.ajax({
			url : 'getAllPath',
			type : 'POST',
			dataType:'json',
			success : function(result) {
				if (result) {
					$("#"+pathCountId).html(result.length+"条");
					var tableNode = $("#"+id);
					tableNode.html("");
					tableNode.append(pathTableHead);
					for (var i = 0; i < result.length; i++) {
						var tr = '<tr>';
						if(i%2 == 1){
							tr = '<tr class="bgh">';
						}
						 tr = tr 
						+(getTD(result[i].mpath == null ? "" : result[i].mpath))
						+(getTD(result[i].startArea == null ? "" : result[i].startArea))
						+(getTD(result[i].endArea == null ? "" : result[i].endArea))
						+(getTD(result[i].mnum == null ? "" : result[i].mnum))
						+(getTD(result[i].mdirection == null ? "" : result[i].mdirection))
						+(getTD(result[i].corhr1 == null ? "" : result[i].corhr1))
						+(getTD(result[i].corhr2 == null ? "" : result[i].corhr2))
						+(getTD(result[i].corhr3 == null ? "" : result[i].corhr3))
						+(getTD(result[i].corhr4 == null ? "" : result[i].corhr4))
						+(getTD(result[i].corhr5 == null ? "" : result[i].corhr5))
						+(getTD('<a class="btn3" href="javascript:void(0);" onclick="path.deletePath(\''+result[i].mpath+'\')">删除</a>'))
						+('</tr>');
						tableNode.append(tr);
					}
				} 
			},
			error : function(xhr, status) {
				alert("系统错误!");
			}
		});
	};
	
	this.deletePath = function(name){
		$.ajax({
			url : 'deletePath',
			type : 'POST',
			data :'mpath='+name,
			dataType:'json',
			success : function(result) {
				if (result) {
					alert("删除成功");
					mypath.getAllPath('pathTable','pathCount');
				};
			},
			error : function(xhr, status) {
				alert("系统错误!");
			}
		});
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
						+(getTD(result[i].endArea == null ? "" : result[i].endArea))
						+(getTD('<a class="btn3" href="javascript:void(0);" onclick="path.deleteLine(\''+result[i].mcorhr+'\')">删除</a>'))
						+('</tr>');
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
	};
	
	this.savePath = function(nameId,startId,endId,linesDivId){
		var name = $("#"+nameId).val();
		var start = $("#"+startId).val();
		var end = $("#"+endId).val();
		var lines = new Array();
		for (var i = 0; i < linesNum; i++) {
			var mdirection = $("#lineSelectOR"+(i+1)).val();
			var corhr = $("#lineSelect"+(i+1)).val();
			var line = {'mdirection':mdirection,'corhr':corhr};
			lines.push(line);
		}
		
		var path = {'mpath':name,'startArea':start,'endArea':end};
		var mdirection = "";
		for (var i = 0; i < lines.length; i++) {
			mdirection += lines[i].mdirection;
			path['corhr'+(i+1)] = lines[i].corhr;
		}
		path['mdirection'] = mdirection;
		path['mnum'] = lines.length;
		$.ajax({
			url : 'addPath',
			type : 'POST',
			data :'pathDefine='+JSON.stringify(path),
			dataType:'json',
			success : function(result) {
				if ("success"==result) {
					mypath.displayAddPanel('tk');
//					alert("添加成功");
					mypath.getAllPath('pathTable','pathCount');
					$("#"+nameId).val("");
					document.getElementById(startId).options[0].selected = true;
					document.getElementById(endId).options[0].selected = true;
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
		
		var line = {'mcorhr':name,'startArea':start,'endArea':end};
		$.ajax({
			url : 'addLine',
			type : 'POST',
			data :'lineDefine='+JSON.stringify(line),
			dataType:'json',
			success : function(result) {
				if (result) {
					mypath.displayAddLinePanel('tk_line');
//					alert("添加成功");
					mypath.getAllLine('lineTable', 'lineCount','lineSelect1');
					$("#"+nameId).val("");
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
		$("#lineSelectDiv .addPath").remove();
		var lineSelectDiv = '<div class="fl pdt20 linePanel" >成员'+linesNum+'&nbsp;&nbsp;<select id="lineSelect'+linesNum+'"></select>'
				+ '<span>成员方向&nbsp;&nbsp;</span><select id="lineSelectOR'+linesNum+'">'
				+ ' <option value ="1">正</option>'
				+ '<option value ="2">反</option>' + '</select>'
				+ '<a class="btn3 addPath" href="javascript:void(0);" onclick="path.addLineSelect()">+</a></div><div class="cl"></div>' ;
		if(linesNum >= maxNum){
			lineSelectDiv = '<div class="fl pdt20 linePanel" >成员'+linesNum+'&nbsp;&nbsp;<select id="lineSelect'+linesNum+'"></select>'
			+ '<span>成员方向&nbsp;&nbsp;</span><select id="lineSelectOR'+linesNum+'">'
			+ ' <option value ="1">正</option>'
			+ '<option value ="2">反</option>' + '</select></div><div class="cl"></div>';
		}
		$("#lineSelectDiv").append(lineSelectDiv);
		this.getSelectLine();
	};
	
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
		document.getElementById(id).style.display = 'block';
	};
	
	this.displayAddPanel = function(id){
		document.getElementById(id).style.display = 'none';
		if(linesNum > 1){
			
			$("#firstSelectPanel").append('<a class="btn3 addPath" href="javascript:void(0);" onclick="path.addLineSelect()">+</a>');
		}
		$("#lineSelectDiv .linePanel").remove();
		linesNum = 1;
	};
	
	this.addLine = function(id){
		document.getElementById(id).style.display = 'block';
	};
	
	this.displayAddLinePanel = function(id){
		document.getElementById(id).style.display = 'none';
	};
	
	function getTD(val){
		return '<td>'+val+'</td>';
	}
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