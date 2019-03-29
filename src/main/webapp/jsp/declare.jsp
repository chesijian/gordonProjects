<%@ page language="java" import="java.util.*,com.state.util.AuthoritiesUtil,com.state.util.VersionCtrl" pageEncoding="UTF-8"
	contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://www.state.com/state" prefix="state"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<state:override name="head">
	<title></title>
	<Link Rel="StyleSheet" Href="${pageContext.request.contextPath }/css/button.css?v=<%=VersionCtrl.getVesrsion()%>" Type="Text/Css">
	<Link Rel="StyleSheet" Href="${pageContext.request.contextPath }/css/demo.css?v=<%=VersionCtrl.getVesrsion()%>" Type="Text/Css"> 
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/ui/themes/default/easyui.css?v=<%=VersionCtrl.getVesrsion()%>">
   <script type="text/javascript" src="${pageContext.request.contextPath }/ui/jquery.easyui.min.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	
	<style type="text/css">  
            .divv  
            {  
                position:absolute;  
                top:30%;  
                left:30%;  
                width:80px;  
                height:60px;  
                border:1px solid #666;  
                background-color:#9CF;  
                text-align:center;  
                display:none;  
                z-index:300;  
            }  
            .popup  
            {  
                border:1px solid red;  
                position:absolute;  
                top:0px;  
                left:0px;  
                width:100%;  
                height:100%;  
                background-color:#000;  
                filter:alpha(opacity=45);  
                opacity:0.45;  
                display:none;  
                z-index:200;  
            }  
            .fff  
            {  
                border:1px solid blue;  
                position:relative;  
                background-color:#000;  
            }  
            .icon-ok {background-image: url(../img/icon/sok.gif) !important;}
            .idCardItem{
				width: 160px;
				height: 150px;
				float:left;
				margin-right:10px;
				border: 1px solid #e3e3e3;
				text-align: center;
				padding: 5px;
			}
			.contop span {
    					padding-left: 10px;
    					padding-right: 0px;
					}
					.circle {
					    border: 1px;
					    border-color: red;
					    height: 20px;
					    width: 20px;
					    border-radius: 50px;
					}
		.SBJSImg div {
			position : relative;
			float: left;
		}
		.circle_btn {
			border: 1px;
		    border-color: red;
		    height: 20px;
		    width: 20px;
		    border-radius: 50px;
		}
		.icon-ok {background-image: url(../img/icon/circle-green.png) !important;}
      .icon-nok {background-image: url(../img/icon/circle-gray.png) !important;}
      .icon-buy {background-image: url(../img/icon/buy.png) !important;}
      .icon-sale {background-image: url(../img/icon/sale.png) !important;}
        </style>  
	<script type="text/javascript" src="${pageContext.request.contextPath }/js/common/common-form-1.1.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	
	<script src="${pageContext.request.contextPath }/js/charts/highcharts.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	<script src="${pageContext.request.contextPath }/js/charts/modules/exporting.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	<script src="${pageContext.request.contextPath }/js/state/jqueryFormat.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	<script src="${pageContext.request.contextPath }/js/state/issue.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	<script src="${pageContext.request.contextPath }/js/state/match.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	<script src="${pageContext.request.contextPath }/js/state/declare.js?v=<%=VersionCtrl.getVesrsion()%>"></script>

	<script type="text/javascript">
	
	var fileds_v_ = {"price":{"allowBlank":"不能为空","number":"必须是数字"},"buyNum":{"allowBlank":"不能为空","number":"必须是数字"},"startDate":{"allowBlank":"不能为空"}};
	$(document).ready(function(){
		$("#addTable").find("input").bind("blur",function(){
			formUtil.checkValid(this);
		});
	});
	function saveData(){
		var isValid = formUtil.checkFormValid("addTable");
		if(isValid){
			//alert("验证通过!");
			return true;
		}else{
			//alert("验证不通过!");
			return false;
		}
		
	}
	</script>
	<script type="text/javascript">
	    
	    var match = new Match();
		var declare = new Declare();
		
		$(function(){
			$('.menu1').find('a[name=购售电申报]').attr('class', 'menufocus');
			$('.menu1').find('a[name=购电申报]').attr('class', 'menufocus');
			$('.menu1').find('a[name=售电申报]').attr('class', 'menufocus');
			if(<%=SessionUtil.isState() %>){
				declare.showTab(2);
			}else{
				declare.showTab(1);
			}
			//declare.getDeclare();
			loadTreeData();
			declare.loadElData(${timeType}, ${areaList});
			//declare.showArea("${userInfo.area}"); //update by yangbo
			//$("#declareMenu li").live('click', function(){declare.getDeclareData($(this), '1a');});
			//$("#declareMenu li").live('dblclick', function(){declare.changeDeclareName();});
			$("#declareMenu li").live('blur', function(){declare.finishChangeDeclareName();});
			$("#declareDataDiv input").live("keydown", function(e){declare.copyTableValue($(this), e);});
			//$("#declareDataDiv input").live("click", function(e){declare.changeData();});
			$(".bz").live("click", function(){$('#comment').focus();});
			//declare.firstLoad(${areaList},'1a');
		});
		var defultId='';
		var nid="";
		function loadTreeData(nodeid){
			declare.loadArea();
			declare.clearChartData();
			changeBtnStyle(null);
			
			$("#paramid").val('');
			declare.selectSingleNode = null;
			$('#declareMenu').text('');
			var time = $("#time").val().replace(/[^0-9]/ig, "");
			nid=nodeid;
			//分类树型数据加载
              $.ajax({
						url : 'getTreeList',
						type : 'POST',
						dataType : 'json',
						data : {
							time : time
						},
						//async:false,
						success : function(data) {
							$("#declareMenu").tree("loadData",data);
							
							//如果是新增窗口关闭，调用传最新id
							if(nid!=null && nid!='undefined' && nid!=''){
								var node = $('#declareMenu').tree('find',nid);
									 $('#declareMenu').tree('select', node.target);
									 var array = new Array();
									 array.push(node);
									 $("#paramid").val(node.id);
									 declare.setSave96Btn(array);
									 return;
							}
							
							var nodes  =$('#declareMenu').tree('getChildren');
							var i,count=0;
							if(nodes.length == 0){
								 $("#paramid").val('');
								 declare.setSave96Btn(null);
								 return;
							}
							if(nodes.length == 1){
								changeBtnStyle(null);
								if(nodes[0].id.indexOf("-")>-1){
									 $("#paramid").val('');
									 declare.setSave96Btn(null);
									 return;
								}
							}
							$("#paramid").val('');
							
							for(i=0;i<nodes.length;i++){
								if(nodes[i].id.indexOf("-")<0){
									if(count==0){
										defultId=nodes[i].id;
										if(nid!=null && nid!='undefined' && nid!=''){
											var node = $('#declareMenu').tree('find',nid);
											$('#declareMenu').tree('check', node.target);
										}else{
											if(defultId!=-1&&defultId!=-2&&defultId!=0&&defultId!=null && defultId!='undefined' && defultId!=''){
												var node = $('#declareMenu').tree('find',defultId);
											//	$('#declareMenu').parent().tree('check', node.target);
												$('#declareMenu').tree('check', node.target);
												var children = $('#declareMenu').tree('getChecked');
												/******2016.08.17修改新界面******start******/
												 $('#declareMenu').tree('select', node.target);
												 var array = new Array();
												 array.push(node);
												 $("#paramid").val(node.id);
												 declare.setSave96Btn(array);
												 //declare.setSave96Btn(children);
												  /******2016.08.17修改新界面******end******/ 
											}
										}
										if(nodes[i]){
											if(nodes[i].attributes['status']){
												changeBtnStyle(nodes[i].attributes['status']);
											 }
										}
										var parent = $('#declareMenu').tree('getParent',nodes[i].target);
										$('#declareMenu').tree('expand',parent.target);
									}
									count++;
								}
							}
							$('.count').text(count + '条');
						},
						
						error : function(xhr, status) {
							alert("系统错误!");
						}
					});
				
	
			$("#declareMenu").tree({//分类树型模型加载 
				 animate:true,
				 onClick:function(node){
					 var status = 0;
					 if(declare.selectSingleNode){
						 status = declare.selectSingleNode.attributes['status'];
					 }
					 changeBtnStyle(status);
					 if(node.id.indexOf("-")>-1){
						 return;
					 }
					 var array = new Array();
					 array.push(node);
					 declare.setSave96Btn(array);
					 $("#paramid").val(node.id);
					 /******2016.08.17修改新界面******end******/
				 	Id = node.id;
					name=node.text;
					if(Id!=0){
					    var s = '';
						if(node.checked){
							$('#declareMenu').tree('uncheck', node.target);
						}else{
							$('#declareMenu').tree('check', node.target);
							s+=node.id;
						}
						//declare.getDeclareData($(this), '1a',s);
					}
					flagState=2;
					
				},
				onCheck:function(node, checked){  
				 var children = $('#declareMenu').tree('getChildren', node.target);
				  if (checked){
					  var children = $('#declareMenu').tree('getChecked');
					 
					   if(children.length>0){
						   declare.setSave96Btn(children);
							  $('#declareMenu').tree('expandAll', node.target);
							  var s = '';
				              for(var i = 0; i < children.length; i++) {
				            	  if(children[i].id.indexOf("-")>-1){
				            	    	continue;
				            	    }
				            	  if (s != ''){
				                    	 s += ',';
						            }
				                    s += children[i].id;
				                  
				              }
				              if(children.length==1){
				            	  if(children[0].attributes.type=='1a'){
				            		  $("#modelName").text('全天');
				            	  }else if(children[0].attributes.type=='2a'){
				            		  $("#modelName").text('高峰');
				            	  }else{
				            		  $("#modelName").text('低谷');
				            	  }
				              }else if(children.length>1){
				            	  $("#modelName").text('汇总');
				              }
				              declare.getDeclareData($(this), '1a',s);
				              $("#paramid").val(s);
					    }else{
					    	  declare.setSave96Btn(children);
					    	  var id = node.id;
					    	  var name = node.text;
					    	  var sumprice =  node.attributes.sumPrice;
					    	  var sumQ = node.attributes.sumQ;
					    	  var startDate = node.attributes.startDate;
					    	  var endDate = node.attributes.endDate;
					    	  var rname = node.attributes.rname;
					    	  var descr = node.attributes.descr;
					    	  var opt = node.attributes.type;
					    	  var mdate = node.attributes.mdate;
					    	  var clearstate=node.attributes.clearstate;
					    	  $("#paramid").val(id);
					    	  $("#dname").val(name);
					    	  $("#sumprice").val(sumprice);
					    	  $("#sumq").val(sumQ);
					    	  $("#startdate").val(startDate);
					    	  $("#enddate").val(endDate);
					    	  $("#rname").val(rname);
					    	  $("#descr").val(descr);
					    	  $("#opt").val(opt);
					    	  $("#mdate").val(mdate);
					    	  $("#clearstate").val(clearstate);
							  //挨个勾选时所获取的节点
							  var checkedNodes = $('#declareMenu').tree('getChecked');
				              var s = '';
				              for(var i = 0; i < checkedNodes.length; i++) {
				            	    if(children[i].id.indexOf("-")>-1){
				            	    	continue;
				            	    }
				                    if (s != ''){
				                    	 s += ',';
						            }
				                    s += checkedNodes[i].id;        
				              }
				              var num = s.split(",").length;
				              if(num==1&&checkedNodes[checkedNodes.length-1].attributes.id==3){
				            	  if(checkedNodes[checkedNodes.length-1].attributes.type=='1a'){
				            		  $("#modelName").text('全天');
				            	  }else if(checkedNodes[checkedNodes.length-1].attributes.type=='2a'){
				            		  $("#modelName").text('高峰');
				            	  }else{
				            		  $("#modelName").text('低谷');
				            	  }
				              }
				              if(num>1){
				            	  $("#modelName").text('汇总');
				              }
				              
				              declare.getDeclareData($(this), '1a',s);
				              $("#paramid").val(s);
					  }
					   
				  }else{ 
					  //取消勾选时所获取的当前选中的节点
					  var checkedNodes = $('#declareMenu').tree('getChecked');
					  declare.setSave96Btn(checkedNodes);
			          var s = '';
			          for (var i = 0; i < checkedNodes.length; i++) {
			        	  if(children[i].id.indexOf("-")>-1){
		            	    	continue;
		            	    }
			               if (s != ''){
			               	 s += ',';
				            }
			               s += checkedNodes[i].id;  
			           }
			          
			           if(checkedNodes.length==1&&checkedNodes[0].attributes.id==3){
			            	  if(checkedNodes[0].attributes.type=='1a'){
			            		  $("#modelName").text('全天');
			            		  declare.getDeclareData($(this), '1a',s);
			            	  }else if(checkedNodes[0].attributes.type=='2a'){
			            		  $("#modelName").text('高峰');
			            		  declare.getDeclareData($(this), '2a',s);
			            	  }else{
			            		  $("#modelName").text('低谷');
			            		  declare.getDeclareData($(this), '3a',s);
			            	  }
			            	 
			              }
			              if(checkedNodes.length>1){
			            	  $("#modelName").text('汇总');
			            	  declare.getDeclareData($(this), '1a',s);
			              }
			              if(checkedNodes.length==0){
			            	  $("#modelName").text('');
			              }
			              
			              $("#paramid").val(s);
					   //$('#declareMenu').tree('collapseAll', node.target);
				  }
				}
				
			});
		};
		function changeBtnStyle(status){
			$('#btngetPlan').html('');
			var btn1 = "<span><a class='btn1' style='margin:0px;color:#06938e;' href='#' onclick='declare.updData();'>查阅</a></span>";
			var btn2 = "<span><a class='btn1' style='margin:0px;color:#06938e;' href='#' onclick='declare.updData();'>编辑</a></span>";
			var btn3 = "<span><a class='btn1'  style='margin-right: 10px;margin-left: 0px;color:#06938e;' href='#' onclick='declare.importFromInterface();'>导入</a></span>";
			var btn4 = "<span><a class='btn1' style='margin-right: 10px;margin-left: 0px;color:#06938e;' href='#' onclick='declare.submitDeclare();'>提交</a></span>";
			if(_isState){
				$('#btngetPlan').append(btn1);
				$('#btngetPlan').append(btn3);
			}else{
				if(status && status == 1){
					$('#btngetPlan').append(btn1);
				}else{
					$('#btngetPlan').append(btn2);
					$('#btngetPlan').append(btn4);
				}
			}
		}
		//--------------------------------------
		
		/* setTimeout(defaultData(defultId,nid),10000);
		function defaultData(defultId,nid){
			
		} */
	</script>
</state:override>
<state:override name="content">

		<div class="mid">
			<div class="contop" style="padding: 10px 10px 5px;">
				
				<div id="limitconrightt" class="limitconrightt" style="text-align: center;width:1200px;margin: auto;border:0px;padding: 5px;padding-left: 0px;">
			  	<c:if test="<%=SessionUtil.isState() %>">
			 	<div class="fl mne"><a id="a_2" href='javascript:;' onclick='declare.showTab(2);return false;' name="申报监视" >申报监视</a></div>
			 	</c:if>
			 	<div class="fl mne"><a id="a_1" href='javascript:;' onclick='declare.showTab(1);return false;' name="购售申报单" style="color:#D1B664;fontWeight:bold">购售申报单</a></div>
			 	
				<div id="btngetPlan" class="rlplan" style="padding-right: 3px;margin-top: 0px;">
								
				<c:if test='<%=AuthoritiesUtil.isAllow("de_btn_look") %>'>
					<c:if test="<%=SessionUtil.isState() %>">
<!-- 						       <span><a class='btn1' style='margin:0px;color:#06938e;' href='#' onclick='declare.updData();'>查阅</a></span> -->
						         </c:if>
						    
						     <c:if test="<%=!SessionUtil.isState() %>">
<!-- 						       <span><a class='btn1' style='margin:0px;color:#06938e;' href='#' onclick='declare.updData();'>编辑</a></span> -->
						          </c:if>
					
				</c:if>
				<c:if test='<%=AuthoritiesUtil.isAllow("de_btn_import") %>'>
<!-- 					<span><a class='btn1'  style="margin-right: 10px;margin-left: 0px;color:#06938e;" href='#' onclick='declare.importFromInterface();'>导入</a></span> -->
				</c:if>
				
				<c:if test='<%=AuthoritiesUtil.isAllow("De_Btn_Submit") %>'>
<!-- 					<span><a class='btn1' style="margin-right: 10px;margin-left: 0px;color:#06938e;" href='#' onclick='declare.submitDeclare();'>提交</a></span> -->
				</c:if>
					
				  </div>
				  <div id="btnSBJSImg" class="SBJSImg" style="padding-right: 3px;margin-top: 0px;float: right;width: 345px;position: relative;">
				  		<div class='circle_btn' style='background-color:rgb(6, 142, 147)'></div>
				  		<div style="color:#C19D30;font-weight: bold;">&nbsp;&nbsp;已提交&nbsp;&nbsp;</div>
				  		<div class='circle_btn' style='background-color:rgb(51, 122, 183)'></div>
				  		<div style="color:#C19D30;font-weight: bold;">&nbsp;&nbsp;未提交&nbsp;&nbsp;</div>
				  		<div class='circle_btn' style='background-color:rgb(164, 157, 153)'></div>
				  		<div style="color:#C19D30;font-weight: bold;">&nbsp;&nbsp;未申报&nbsp;&nbsp;</div>
				  </div>
				  </div>
			    </div> 
			
			<div id="tab1" style="display: none">
				<div class="lmenu" style="height:635px;">
					<ul id="declareMenu">
						
					</ul>
				</div>
				
				<div id="declareDataDiv" class="fl bd1" style="display:none;">
					<div class="conrightt1">
						<input id="paramid" name="paramid" value="" type="hidden"/>
						<input id="dname" name="dname" value="" type="hidden"/>
						<input id="sumprice" name="sumprice" value="" type="hidden"/>
						<input id="sumq" name="sumq" value="" type="hidden"/>
						<input id="startdate" name="startdate" value="" type="hidden"/>
						<input id="enddate" name="enddate" value="" type="hidden"/>
						<input id="rname" name="rname" value="" type="hidden"/>
						<input id="descr" name="descr" value="" type="hidden"/>
						<input id="opt" name="opt" value="" type="hidden"/>
						<input id="mdate" name="mdate" value="" type="hidden"/>
						<input id="clearstate" name="clearstate" value="" type="hidden"/>
						<div class="fl mne"><a href="#" name="1a" onclick="declare.getDeclareDataByDeclareType('1a');"><span id="modelName">全天</span></a></div>
						<div style="text-align:right;"><span ><a class='btn1' id="btn_save96Data" style="display:none" href='javascript:;' onclick='declare.save96Data();return false;'>保存</a></span>
						</div>
					</div>
                         
					<div class="cl"></div>
					 <div class="fl conrightt2"><span>总电量:</span><span  class="avenum" name="sumData">0 MWh</span>
					 <span id="fl_price">|<span class="pdl30">电价:</span><span  class="avenum" name="sumPrice">0 元/MWh</span></span>
					|<span class="pdl30">平均出力:</span><span name="avgValue" class="avenum" id="avenum">0 MW</span>
					</div>
				     
					<div class="cl"></div>
					<div class="fl pdl10">
						<table  width="968" height="302" cellpadding="0" cellspacing="0">
							<thead>
								<th width="16%" class="cgr">时间</th>
								<th width="7%" class="cgr">0</th>
								<th width="7%" class="cgr">1</th>
								<th width="7%" class="cgr">2</th>
								<th width="7%" class="cgr">3</th>
								<th width="7%" class="cgr">4</th>
								<th width="7%" class="cgr">5</th>
								<th width="7%" class="cgr">6</th>
								<th width="7%" class="cgr">7</th>
								<th width="7%" class="cgr">8</th>
								<th width="7%" class="cgr">9</th>
								<th width="7%" class="cgr">10</th>
								<th width="7%" class="cgr">11</th>
							</thead>
							<tr>
								<td width="16%" class="cgr">00:15</td>
								<td width="7%"><input name="h01" class="datainput" id="3a"></td>
								<td width="7%"><input name="h05" class="datainput" id="3a"></td>
								<td width="7%"><input name="h09" class="datainput" id="3a"></td>
								<td width="7%"><input name="h13" class="datainput" id="3a"></td>
								<td width="7%"><input name="h17" class="datainput" id="3a"></td>
								<td width="7%"><input name="h21" class="datainput" id="3a"></td>
								<td width="7%"><input name="h25" class="datainput" id="3a"></td>
								<td width="7%"><input name="h29" class="datainput" id="3a"></td>
								<td width="7%"><input name="h33" class="datainput" id="2a"></td>
								<td width="7%"><input name="h37" class="datainput" id="2a"></td>
								<td width="7%"><input name="h41" class="datainput" id="2a"></td>
								<td width="7%"><input name="h45" class="datainput" id="2a"></td>
							</tr>
							<tr class="bgh">
								<td width="16%" class="cgr">00:30</td>
								<td width="7%"><input name="h02" class="datainput" id="3a"></td>
								<td width="7%"><input name="h06" class="datainput" id="3a"></td>
								<td width="7%"><input name="h10" class="datainput" id="3a"></td>
								<td width="7%"><input name="h14" class="datainput" id="3a"></td>
								<td width="7%"><input name="h18" class="datainput" id="3a"></td>
								<td width="7%"><input name="h22" class="datainput" id="3a"></td>
								<td width="7%"><input name="h26" class="datainput" id="3a"></td>
								<td width="7%"><input name="h30" class="datainput" id="3a"></td>
								<td width="7%"><input name="h34" class="datainput" id="2a"></td>
								<td width="7%"><input name="h38" class="datainput" id="2a"></td>
								<td width="7%"><input name="h42" class="datainput" id="2a"></td>
								<td width="7%"><input name="h46" class="datainput" id="2a"></td>
							</tr>
							<tr>
								<td width="16%" class="cgr">00:45</td>
								<td width="7%"><input name="h03" class="datainput" id="3a"></td>
								<td width="7%"><input name="h07" class="datainput" id="3a"></td>
								<td width="7%"><input name="h11" class="datainput" id="3a"></td>
								<td width="7%"><input name="h15" class="datainput" id="3a"></td>
								<td width="7%"><input name="h19" class="datainput" id="3a"></td>
								<td width="7%"><input name="h23" class="datainput" id="3a"></td>
								<td width="7%"><input name="h27" class="datainput" id="3a"></td>
								<td width="7%"><input name="h31" class="datainput" id="3a"></td>
								<td width="7%"><input name="h35" class="datainput" id="2a"></td>
								<td width="7%"><input name="h39" class="datainput" id="2a"></td>
								<td width="7%"><input name="h43" class="datainput" id="2a"></td>
								<td width="7%"><input name="h47" class="datainput" id="2a"></td>
							</tr>
							<tr class="bgh">
								<td width="16%" class="cgr">00:00</td>
								<td width="7%"><input name="h04" class="datainput" id="3a"></td>
								<td width="7%"><input name="h08" class="datainput" id="3a"></td>
								<td width="7%"><input name="h12" class="datainput" id="3a"></td>
								<td width="7%"><input name="h16" class="datainput" id="3a"></td>
								<td width="7%"><input name="h20" class="datainput" id="3a"></td>
								<td width="7%"><input name="h24" class="datainput" id="3a"></td>
								<td width="7%"><input name="h28" class="datainput" id="3a"></td>
								<td width="7%"><input name="h32" class="datainput" id="3a"></td>
								<td width="7%"><input name="h36" class="datainput" id="2a"></td>
								<td width="7%"><input name="h40" class="datainput" id="2a"></td>
								<td width="7%"><input name="h44" class="datainput" id="2a"></td>
								<td width="7%"><input name="h48" class="datainput" id="2a"></td>
							</tr>
							<tr>
								<td width="16%" class="cgr">时间</td>
								<td width="7%" class="cgr">12</td>
								<td width="7%" class="cgr">13</td>
								<td width="7%" class="cgr">14</td>
								<td width="7%" class="cgr">15</td>
								<td width="7%" class="cgr">16</td>
								<td width="7%" class="cgr">17</td>
								<td width="7%" class="cgr">18</td>
								<td width="7%" class="cgr">19</td>
								<td width="7%" class="cgr">20</td>
								<td width="7%" class="cgr">21</td>
								<td width="7%" class="cgr">22</td>
								<td width="7%" class="cgr">23</td>
							</tr>
							<tr class="bgh">
								<td width="16%" class="cgr">00:15</td>
								<td width="7%"><input name="h49" class="datainput" id="2a"></td>
								<td width="7%"><input name="h53" class="datainput" id="2a"></td>
								<td width="7%"><input name="h57" class="datainput" id="2a"></td>
								<td width="7%"><input name="h61" class="datainput" id="2a"></td>
								<td width="7%"><input name="h65" class="datainput" id="2a"></td>
								<td width="7%"><input name="h69" class="datainput" id="2a"></td>
								<td width="7%"><input name="h73" class="datainput" id="2a"></td>
								<td width="7%"><input name="h77" class="datainput" id="2a"></td>
								<td width="7%"><input name="h81" class="datainput" id="2a"></td>
								<td width="7%"><input name="h85" class="datainput" id="2a"></td>
								<td width="7%"><input name="h89" class="datainput" id="3a"></td>
								<td width="7%"><input name="h93" class="datainput" id="3a"></td>
							</tr>
							<tr>
								<td width="16%" class="cgr">00:30</td>
								<td width="7%"><input name="h50" class="datainput" id="2a"></td>
								<td width="7%"><input name="h54" class="datainput" id="2a"></td>
								<td width="7%"><input name="h58" class="datainput" id="2a"></td>
								<td width="7%"><input name="h62" class="datainput" id="2a"></td>
								<td width="7%"><input name="h66" class="datainput" id="2a"></td>
								<td width="7%"><input name="h70" class="datainput" id="2a"></td>
								<td width="7%"><input name="h74" class="datainput" id="2a"></td>
								<td width="7%"><input name="h78" class="datainput" id="2a"></td>
								<td width="7%"><input name="h82" class="datainput" id="2a"></td>
								<td width="7%"><input name="h86" class="datainput" id="2a"></td>
								<td width="7%"><input name="h90" class="datainput" id="3a"></td>
								<td width="7%"><input name="h94" class="datainput" id="3a"></td>
							</tr>
							<tr class="bgh">
								<td width="16%" class="cgr">00:45</td>
								<td width="7%"><input name="h51" class="datainput" id="2a"></td>
								<td width="7%"><input name="h55" class="datainput" id="2a"></td>
								<td width="7%"><input name="h59" class="datainput" id="2a"></td>
								<td width="7%"><input name="h63" class="datainput" id="2a"></td>
								<td width="7%"><input name="h67" class="datainput" id="2a"></td>
								<td width="7%"><input name="h71" class="datainput" id="2a"></td>
								<td width="7%"><input name="h75" class="datainput" id="2a"></td>
								<td width="7%"><input name="h79" class="datainput" id="2a"></td>
								<td width="7%"><input name="h83" class="datainput" id="2a"></td>
								<td width="7%"><input name="h87" class="datainput" id="2a"></td>
								<td width="7%"><input name="h91" class="datainput" id="3a"></td>
								<td width="7%"><input name="h95" class="datainput" id="3a"></td>
							</tr>
							<tr>
								<td width="16%" class="cgr">00:00</td>
								<td width="7%"><input name="h52" class="datainput" id="2a"></td>
								<td width="7%"><input name="h56" class="datainput" id="2a"></td>
								<td width="7%"><input name="h60" class="datainput" id="2a"></td>
								<td width="7%"><input name="h64" class="datainput" id="2a"></td>
								<td width="7%"><input name="h68" class="datainput" id="2a"></td>
								<td width="7%"><input name="h72" class="datainput" id="2a"></td>
								<td width="7%"><input name="h76" class="datainput" id="2a"></td>
								<td width="7%"><input name="h80" class="datainput" id="2a"></td>
								<td width="7%"><input name="h84" class="datainput" id="2a"></td>
								<td width="7%"><input name="h88" class="datainput" id="2a"></td>
								<td width="7%"><input name="h92" class="datainput" id="3a"></td>
								<td width="7%"><input name="h96" class="datainput" id="3a"></td>
							</tr>
						</table>
					</div>
					<div class="cl"></div>
					<div class="cchart"></div>
					<!-- <div class="bz"><textarea id="comment" onfocus="declare.changeData();"></textarea></div> -->
				</div>
				
				<div class="fl bd1" style="width: 83%; height: 635px;">
					<iframe id="_frame" src="chart" style="width: 100%;height: 100%;border:0"></iframe>
				</div>
				</div>
				<div id="tab2" style="display: none;border-top: solid 1px #dcdcdb;padding-top: 10px;">
					<div class="pdl10">
					<table  id="table_area" width="868" height="402" cellpadding="0" cellspacing="0" style="margin: auto;">
					</table>
					</div>
				</div>
				<div id="tab3" style="display: none;border-top: solid 1px #dcdcdb;">
				<div class="contop">
					<div class="fl"><span class="xmenu">报价申报单</span><span id="lineCount" class="count">0条</span></div>
					<div class="rl" style="margin-top: -23px;">
						<span><a class="btn1" href="javascript:void(0);" onclick="declare.addBidOffer('tk')">+添加</a></span>
					</div>
					<div class="cl"></div>
				</div>
				<div>
					<div class="fl bd1 ustb">
						<div class="fl">
							<table id="bidOfferTable" width="1170" cellpadding="0" cellspacing="0">
								<thead>
								<th width="7%">时间</th>
								<th width="7%">地区</th>
								<th width="7%">价格上限</th>
								<th width="7%">价格下限</th>
								<th width="7%">交易时间段</th>
								</thead>
							</table>
							<div style="width: 1200px;margin-top: 37px;" >
								<ul id="pagination" class="pagination" style="margin:0px;margin-left: 435px;"></ul>
							</div>
						</div>
						<div class="cl"></div>
					</div>
				</div>
			</div>
			</div>
			<div id="tk" style="display: none;">
			<div class="tableCon">
				<div class="rl"><a class="btn3" href="javascript:void(0);" onclick="declare.displayAddPanel('tk')">x</a></div>
				<div class="cl"></div>
				<input type="hidden" id="bid_offer_id"/>
				<div class="pdt20" >
					<div>
					   价格上限：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input id="price_max"/>
					</div>
					<div>
					   价格下限：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input id="price_min" />
					</div>
					<div>
					   交易时间段：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<select id="bid_offer_session" style="width: 80px;"></select>
					</div>
					<div class="cl"></div>
					<div class="pdt20 rl pdr20"><a class="btn1"  href="javascript:void(0);" onclick="saveBidOffer()">提交</a></div>
				</div>
			</div>
			<div class="blbg">&nbsp;</div>
		</div>
</state:override>
<script type="text/javascript">
    // Firefox, Google Chrome, Opera, Safari, Internet Explorer from version 9
        function OnInput (event) {
            var sum=event.target.value;
            var type=document.getElementById('type').value;
            var a2=document.getElementById('a2').value;
	        var a3=document.getElementById('a3').value;
            var avgnum="";
            if(type=="1a"){
	        	avgnum=sum*4/96;
	        }else if(type=="2a"){
	        	avgnum=sum*4/a2;
	        }else if(type=="3a"){
	        	avgnum=sum*4/a3;
	        }
            document.getElementById("avep").innerHTML=Math.round(avgnum);  
        };
      
    // Internet Explorer
        function OnPropChanged (event) {
    	//alert(aaa);
            if (event.propertyName.toLowerCase () == "value") {
                var sum=event.srcElement.value;
                var type=document.getElementById('type').value;
                var a2=document.getElementById('a2').value;
    	        var a3=document.getElementById('a3').value;
                var avgnum="";
                if(type=="1a"){
    	        	avgnum=sum*4/96;
    	        }else if(type=="2a"){
    	        	avgnum=sum*4/a2;
    	        }else if(type=="3a"){
    	        	avgnum=sum*4/a3;
    	        }
                document.getElementById("avep").innerHTML=Math.round(avgnum); 
            }
        };
        // 改变默认值ya
        function selectChange() {
        	 var sum=document.getElementById('buyNum').value;
        	 var type=document.getElementById('type').value;
             var a2=document.getElementById('a2').value;
 	         var a3=document.getElementById('a3').value;
             var avgnum="";
             if(type=="1a"){
 	        	avgnum=sum*4/96;
 	        }else if(type=="2a"){
 	        	avgnum=sum*4/a2;
 	        }else if(type=="3a"){
 	        	avgnum=sum*4/a3;
 	        }
             document.getElementById("avep").innerHTML=Math.round(avgnum); 
       	}; 
     // 改变默认值ya
     function pre (event) {
    	 event.style.color = "black";
    	 event.value="";
    	}; 
    </script>
    
<%@ include file="/common/block/block.jsp" %>


