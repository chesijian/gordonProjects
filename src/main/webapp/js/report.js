/**
 * 
 */
//var issue = new Issue();
//设置默认当前页  
	var liId ="a";

    var currentPage = 1;
    var areaCurrentPage = 1;  
    var areaTotalPage = 1;  
    
    var lineCurrentPage = 1;  
    var lineTotalPage = 1;
 
	$(function(){
		 $('.menu1').find('a[name=统计查询]').attr('class', 'menufocus');
		 var str=$("#str").val();
		 if(str=="area"){
			 $('#myTab li:eq(0) a').tab('show');
			 $('#myTab li:eq(0)').trigger("click");;
		 }else if(str=="line"){
			 $('#myTab li:eq(1) a').tab('show');
			 $('#myTab li:eq(1)').trigger("click");;
		 }

       });
	
	function changeTime(){
		areaCurrentPage=1;
		lineCurrentPage=1;
		var str=$("#str").val();
		var type=$("#type").val();
		var startTime=$("#startTime").val();
		var endTime=$("#endTime").val();
		changeTab(str,type,startTime,endTime);
	}
	function changeType(type){
		$("#type").val(type);
		areaCurrentPage=1;
		lineCurrentPage=1;
		var str=$("#str").val();
		changeTab(str,type);
	}
	//---------------------------------------------------------------------	
	function changeLiId(str){
		liId = str;
	}
	function changeTab(str,type,startTime,endTime){
		if(str=="measureData"){
			$(".input-append").hide();
			liId="a";
			return;
		}else{
			$(".input-append").show();
			liId="b";
		}
		$("#str").val(str);
		$("#type").val(type);
		$("#tabType").val(type);
		if(str=="area"){
			$('#pagination').empty();  
		    $('#pagination').removeData("twbs-pagination");  
		    $('#pagination').unbind("page"); 
		    currentPage =areaCurrentPage;
		}else if(str=="line"){
			$('#pagination1').empty();  
		    $('#pagination1').removeData("twbs-pagination");  
		    $('#pagination1').unbind("page");
		    currentPage = lineCurrentPage;
		}
		//alert("areaCurrentPage===="+areaCurrentPage);
		var ts="";
		if(type=="year"){
			ts="年度";
		}else if(type=="month"){
			ts="月份";
		}
		$.ajax({
			url : "getData",
			type : 'POST',
			dataType : 'json',
			data : {
				str : str,
				type:type,
				startTime:startTime,
				endTime:endTime,
				pageIndex:currentPage,
				limit:8
			},
			success : function(result) {
                console.debug(result);
				if (result.list) {
					 if(str=='area'){
						 areaTotalPage = result.totalPage;//将后台数据复制给总页数  
						 $("#areaData").empty();
						/*var areaContent='<thead><th width="7%" class="cgr">地区</th>'+
							'<th width="7%" class="cgr">申报电量</th>'+
							'<th width="7%" class="cgr">出清电量</th>'+
							'<th width="7%" class="cgr">结算电量</th>'+
							'<th width="7%" class="cgr">出清费用</th>'+
							'<th width="7%" class="cgr">结算费用</th>'+
							'<th width="7%" class="cgr"><font color="#f44336">'+ts+'</font></th>'+
							'<th width="7%" class="cgr">说明</th></thead>';*/
						 var areaContent='<thead><th width="7%" class="cgr">地区</th>'+
							'<th width="7%" class="cgr">交易电量</th>'+
							'<th width="7%" class="cgr">成交比数</th>'+
							'<th width="7%" class="cgr">平均电价</th>'+
							'<th width="7%" class="cgr"><font color="#f44336">'+ts+'</font></th>'+
							'<th width="7%" class="cgr">说明</th></thead>';
						for(var i=0;i<result.list.length;i++){
							 areaContent=areaContent+'<tr><td width="7%">'+result.list[i].area+'</td>'+
							'<td width="7%">'+result.list[i].sumq+'</td>'+
							'<td width="7%">'+result.list[i].cqq+'</td>'+
							'<td width="7%">'+result.list[i].cqp+'</td>';
							
							if(type=='year'){
							 areaContent=areaContent+'<td width="7%" align="center"><font color="#f44336">'+result.list[i].year+'</font></td>';
							}else if(type=='month'){
							 areaContent=areaContent+'<td width="7%" align="center"><font color="#f44336">'+result.list[i].year+'-'+result.list[i].month+'</font></td>';
							}
							areaContent=areaContent+'<td width="7%"></td></tr>';
						}
						$("#areaData").append(areaContent);
						$("#pagination").twbsPagination({
							totalPages:areaTotalPage,
							visiblePages:"5",
							startPage:areaCurrentPage,
							first:"首页",
							prev:"上一页",
							next:"下一页",
							last:"最后一页",
							onPageClick:function(e,page){	
								 // 将当前页数重置为page 
								var str=$("#str").val();
								var type=$("#type").val();
								var startTime=$("#startTime").val();
								var endTime=$("#endTime").val();
								areaCurrentPage = page;  
								changeTab(str,type,startTime,endTime);
							}
						});
					}else if(str=='line'){
						lineTotalPage = result.totalPage;//将后台数据复制给总页数  
						$("#lineData").empty();
						var lineContent='<thead><th width="7%" class="cgr">联络线名称</th>'+
						'<th width="7%" class="cgr">交易电量</th>'+
						'<th width="7%" class="cgr">上限电量</th>'+
						'<th width="7%" class="cgr">下限电量</th>'+
						'<th width="7%" class="cgr">计划电量</th>'+
						'<th width="7%" class="cgr">剩余能力</th>'+
						'<th width="7%" class="cgr">利用率</th>'+
						'<th width="7%" class="cgr"><font color="#f44336">'+ts+'</font></th>'+
						'<th width="7%" class="cgr">说明</th></thead>';
					 for(var i=0;i<result.list.length;i++){
						 var ms=result.list[i].maxq-result.list[i].syq;
						 lineContent=lineContent+'<tr><td width="7%">'+result.list[i].mcorhr+'</td>'+
						'<td width="7%">'+result.list[i].syq/4+'</td>'+
						'<td width="7%">'+result.list[i].maxq+'</td>'+
						'<td width="7%">'+result.list[i].minq+'</td>'+
						'<td width="7%">'+result.list[i].planq+'</td>'+
					    '<td width="7%">'+ms+'</td>'+ 
						'<td width="7%"></td>';
						 if(type=='year'){
						  lineContent=lineContent+'<td width="7%" align="center"><font color="#f44336">'+result.list[i].year+'</font></td>';
						}else if(type=='month'){
						  lineContent=lineContent+'<td width="7%" align="center"><font color="#f44336">'+result.list[i].year+'-'+result.list[i].month+'</font></td>';
						}
						  lineContent=lineContent+'<td width="7%"></td></tr>';
					} 
					$("#lineData").append(lineContent);
					$("#pagination1").twbsPagination({
						totalPages:lineTotalPage,
						visiblePages:"5",
						startPage:lineCurrentPage,
						first:"首页",
						prev:"上一页",
						next:"下一页",
						last:"最后一页",
						onPageClick:function(e,page){	
							 // 将当前页数重置为page  
							var str=$("#str").val();
							var type=$("#type").val();
							var startTime=$("#startTime").val();
							var endTime=$("#endTime").val();
							lineCurrentPage = page;  
							changeTab(str,type,startTime,endTime);
						}
					});
					}
				} 
			},
			error : function(xhr, status) {
				alert("系统错误!");
			}
           }); 
	}
	
	
