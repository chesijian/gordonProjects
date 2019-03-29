<%@ page language="java" import="java.util.*,com.state.util.AuthoritiesUtil,com.state.util.VersionCtrl" pageEncoding="UTF-8"
	contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://www.state.com/state" prefix="state"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	boolean isAllowEdit = AuthoritiesUtil.isAllow("li_tdpz_btn_add");
%>
<state:override name="head">
	<title></title>
	<style>
	.ureg {
       color: #ffffff;
       padding-top: 0px;
    }
    .tableCon{
		position:fixed;
		top:50%;
		left:50%;
		margin:-150px 0 0 -225px;
		width:420px;
		background-color: #FFFFFF;
		z-index: 1000;
		font-size: 14px;
		padding: 10px 10px 40px 40px;
		border-radius: 5px;
    }
    .bCorcon {
    position: fixed;
    top: 50%;
    left: 42%;
    margin: -150px 0 0 -225px;
    width: 680px;
    background-color: #FFFFFF;
    z-index: 1000;
    font-size: 14px;
    padding: 10px 10px 40px 40px;
    border-radius: 5px;
}
	</style>
	<Link Rel="StyleSheet" Href="${pageContext.request.contextPath }/css/line.css?v=<%=VersionCtrl.getVesrsion()%>" Type="Text/Css">
	<link href="${pageContext.request.contextPath }/bootstrap/css/bootstrap-btn.css?v=<%=VersionCtrl.getVesrsion()%>" rel="stylesheet">
   
    <script src="${pageContext.request.contextPath }/js/jquery/jquery-1.8.3.min.js?v=<%=VersionCtrl.getVesrsion()%>" type="text/javascript" ></script>
    <script src="${pageContext.request.contextPath }/js/jquery/jquery.json.min.js?v=<%=VersionCtrl.getVesrsion()%>" type="text/javascript" ></script>
	<script src="${pageContext.request.contextPath }/js/state/path.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	<script type="text/javascript">
	(function ($) {
		  window.Ewin = function () {
		    var html = '<div id="[Id]" class="modal fade" role="dialog" aria-labelledby="modalLabel">' +
		               '<div class="modal-dialog modal-sm">' +
		                 '<div class="modal-content">' +
		                   '<div class="modal-header">' +
		                     '<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">Close</span></button>' +
		                     '<h4 class="modal-title" id="modalLabel">[Title]</h4>' +
		                   '</div>' +
		                   '<div class="modal-body">' +
		                   '<p>[Message]</p>' +
		                   '</div>' +
		                    '<div class="modal-footer">' +
		    '<button type="button" class="btn btn-default cancel" data-dismiss="modal">[BtnCancel]</button>' +
		    '<button type="button" class="btn btn-primary ok" data-dismiss="modal">[BtnOk]</button>' +
		  '</div>' +
		                 '</div>' +
		               '</div>' +
		             '</div>';
		 
		 
		    var dialogdHtml = '<div id="[Id]" class="modal fade" role="dialog" aria-labelledby="modalLabel">' +
		               '<div class="modal-dialog">' +
		                 '<div class="modal-content">' +
		                   '<div class="modal-header">' +
		                     '<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">Close</span></button>' +
		                     '<h4 class="modal-title" id="modalLabel">[Title]</h4>' +
		                   '</div>' +
		                   '<div class="modal-body">' +
		                   '</div>' +
		                 '</div>' +
		               '</div>' +
		             '</div>';
		    var reg = new RegExp("\\[([^\\[\\]]*?)\\]", 'igm');
		    var generateId = function () {
		      var date = new Date();
		      return 'mdl' + date.valueOf();
		    }
		    var init = function (options) {
		      options = $.extend({}, {
		        title: "操作提示",
		        message: "提示内容",
		        btnok: "确定",
		        btncl: "取消",
		        width: 200,
		        auto: false
		      }, options || {});
		      var modalId = generateId();
		      var content = html.replace(reg, function (node, key) {
		        return {
		          Id: modalId,
		          Title: options.title,
		          Message: options.message,
		          BtnOk: options.btnok,
		          BtnCancel: options.btncl
		        }[key];
		      });
		      $('body').append(content);
		      $('#' + modalId).modal({
		        width: options.width,
		        backdrop: 'static'
		      });
		      $('#' + modalId).on('hide.bs.modal', function (e) {
		        $('body').find('#' + modalId).remove();
		      });
		      return modalId;
		    }
		 
		    return {
		      alert: function (options) {
		        if (typeof options == 'string') {
		          options = {
		            message: options
		          };
		        }
		        var id = init(options);
		        var modal = $('#' + id);
		        modal.find('.ok').removeClass('btn-success').addClass('btn-primary');
		        modal.find('.cancel').hide();
		 
		        return {
		          id: id,
		          on: function (callback) {
		            if (callback && callback instanceof Function) {
		              modal.find('.ok').click(function () { callback(true); });
		            }
		          },
		          hide: function (callback) {
		            if (callback && callback instanceof Function) {
		              modal.on('hide.bs.modal', function (e) {
		                callback(e);
		              });
		            }
		          }
		        };
		      },
		      confirm: function (options) {
		        var id = init(options);
		        var modal = $('#' + id);
		        modal.find('.ok').removeClass('btn-primary').addClass('btn-success');
		        modal.find('.cancel').show();
		        return {
		          id: id,
		          on: function (callback) {
		            if (callback && callback instanceof Function) {
		              modal.find('.ok').click(function () { callback(true); });
		              modal.find('.cancel').click(function () { callback(false); });
		            }
		          },
		          hide: function (callback) {
		            if (callback && callback instanceof Function) {
		              modal.on('hide.bs.modal', function (e) {
		                callback(e);
		              });
		            }
		          }
		        };
		      },
		      dialog: function (options) {
		        options = $.extend({}, {
		          title: 'title',
		          url: '',
		          width: 800,
		          height: 550,
		          onReady: function () { },
		          onShown: function (e) { }
		        }, options || {});
		        var modalId = generateId();
		 
		        var content = dialogdHtml.replace(reg, function (node, key) {
		          return {
		            Id: modalId,
		            Title: options.title
		          }[key];
		        });
		        $('body').append(content);
		        var target = $('#' + modalId);
		        target.find('.modal-body').load(options.url);
		        if (options.onReady())
		          options.onReady.call(target);
		        target.modal();
		        target.on('shown.bs.modal', function (e) {
		          if (options.onReady(e))
		            options.onReady.call(target, e);
		        });
		        target.on('hide.bs.modal', function (e) {
		          $('body').find(target).remove();
		        });
		      }
		    }
		  }();
		})(jQuery);
		var isAllow_LineL_Btn_Add = <%=isAllowEdit %>;
		$(function(){
			$('.menu1').find('a[name=基础数据]').attr('class', 'menufocus');
		});
		var isAllow_PathL_Btn_Add = false;
		var path = new Path();
		$(function(){
			document.getElementById("tck").style.display = 'none';
			//refreshPathData();
			//isAllow_PathL_Btn_Add = isBeforeDate();
			path.pathTableHead = '';
			path.pathTableHead = '<thead><th width="7%">通道名称</th>'+
			'<th width="4%">首端</th>'+
			'<th width="4%">末端</th>'+
			'<th width="7%">级数</th>';
			if(isAllow_PathL_Btn_Add){
				path.pathTableHead += '<th width="6%">操作</th></thead>';
			}else{
				path.pathTableHead += '</thead>';
			}
			refreshData();
		});
		function refreshPathData(){
			path.getAllPath('pathTable','pathCount');
			path.getAllLine('lineTable', 'lineCount','lineSelect1');
			
			var linesArea = new Array();
			linesArea.push($("#startLine"));
			linesArea.push($("#endLine"));
			linesArea.push($("#lstartLine"));
			linesArea.push($("#lendLine"));
			areaSelect('${pageContext.request.contextPath}/area/getAllArea',linesArea);
		}
		var isFuture = false;
		function isBeforeDate(){
			var str = $("#time").val();
            var selectDate = Date.parse(str.replace(/-/g,"/"));
            //获取当前系统时间，如：2016-12-13
            var currentStr = '';
            var date = new Date();
            var strYear = date.getFullYear();     
		    var strDay = date.getDate();     
		    var strMonth = date.getMonth()+1;
		    currentStr = strYear+"-"+(strMonth<10?"0"+strMonth:strMonth)+"-"+(strDay<10?"0"+strDay:strDay); 
 			var curryDate = Date.parse(currentStr.replace(/-/g,"/"));
 			if(selectDate>curryDate){
 				return true;
 			}else{
 				return false;
 			}
 			alert(currentStr);
 			var nextStr = '';
            var date1 = new Date();
            var strYear1 = date1.getFullYear();     
		    var strDay1 = date1.getDate()+1;     
		    var strMonth1 = date1.getMonth()+1;
		    nextStr = strYear1+"-"+(strMonth1<10?"0"+strMonth1:strMonth1)+"-"+(strDay1<10?"0"+strDay1:strDay1); 
 			var nextDate = Date.parse(nextStr.replace(/-/g,"/"));
 			alert(nextStr);
 			if(selectDate>nextDate){
				isFuture = false;
			}else{
				isFuture = true;
			}
		}
		
		
		function getIsFuture(){
			
			var str = $("#time").val();
            var selectDate = Date.parse(str.replace(/-/g,"/"));
 			var nextStr = '';
            var date1 = new Date();
            var strYear1 = date1.getFullYear();     
		    var strDay1 = date1.getDate()+1;     
		    var strMonth1 = date1.getMonth()+1;
		    nextStr = strYear1+"-"+(strMonth1<10?"0"+strMonth1:strMonth1)+"-"+(strDay1<10?"0"+strDay1:strDay1); 
 			var nextDate = Date.parse(nextStr.replace(/-/g,"/"));
 			if(selectDate>nextDate){
				return false;
			}else{
				return true;
			}
		}
		var isFuture = false;
        function refreshData(){
        	isAllow_PathL_Btn_Add = isBeforeDate();
        	isFuture = getIsFuture();
            if(isAllow_PathL_Btn_Add){
             	document.getElementById("addPathButton").style.display = 'block';
             	document.getElementById("addCorPathButton").style.display = 'block';
            }else{
             	document.getElementById("addPathButton").style.display = 'none';
             	document.getElementById("addCorPathButton").style.display = 'none';
            }
         	
        	path.getAllPath('pathTable','pathCount');
			path.getAllLine('lineTable', 'lineCount','lineSelect1');
			
			var linesArea = new Array();
			linesArea.push($("#startLine"));
			linesArea.push($("#endLine"));
			linesArea.push($("#lstartLine"));
			linesArea.push($("#lendLine"));
			areaSelect('${pageContext.request.contextPath}/area/getAllArea',linesArea);
		}
		function savePath(){
			var name = $("#addPathName").val();
			var start = $("#addPathStart").val();
			var end = $("#addPathEnd").val();
			var lines = new Array();
			lines.push();
			path.savePath(name, start, end, lines);
		}
		
		function saveLine(){
			var name = $("#addPathName").val();
			var start = $("#addPathStart").val();
			var end = $("#addPathEnd").val();
			path.saveLine(name, start, end);
		}
		
		$(function(){
			changeAddPathName();
		});
		function selectChange(){
			if(isSame()){
				alert("首端和末端不能在同一区域");
			}
			changeAddPathName();
		}
		
		//校验尾端与末端是否相同
		function isSame(){
			if($("#startLine").val() == $("#endLine").val()){
				return true;
			}
			return false;
		}
		
		//更改id为addPathName的input内容
		function changeAddPathName(){
			var startLine = $("#startLine").val();
			var endLine = $("#endLine").val();
			if(startLine!=null && endLine!=null){
				$("#addPathName").val(startLine+"送"+endLine);
			}
		}
		
	</script>
</state:override>
<state:override name="content">
	<div>   
       <input type="hidden" id="pathId">
       <input type="hidden" id="pathName">
       <input type="hidden" id="parentPrid">
       <input type="hidden" id="parentPathId">
       <input type="hidden" id="corridorPathId">
       <input type="hidden" id="action">
       <input type="hidden" id="corriAction">
       <div id="limitconrightt" class="limitconrightt" style="text-align: center;width:1200px;margin: auto;">
			<!-- 	<div class="fl mne"><a href="#" name="正向限额" onclick="limitLine.getLimitLineDataByLimitLineType('正向限额');">上限</a></div> -->
				<c:if test='<%=AuthoritiesUtil.isAllow("Market_Kq_Msg") %>'>
				<div class="fl mne"><a href="${pageContext.request.contextPath }/lineLimit/init" name="限额管理" >跨区通道信息</a></div>
				</c:if>
				<c:if test='<%=AuthoritiesUtil.isAllow("Market_Jytd") %>'>
				<div class="fl mne"><a href="${pageContext.request.contextPath }/path/init" name="通道联络线管理" style="color:#D8AD27;font-weight:bold;font-size:16px;">交易通道配置</a></div>
				</c:if>
				
	    </div>
		<div class="mid">
			<div class="contop">
				<div class="fl"><span class="xmenu">交易通道配置</span><span id="pathCount" class="count"></span></div>
				<div class="rl" style="margin-top: 15px;">
				<c:if test='<%=AuthoritiesUtil.isAllow("Li_Btn_Update") %>'>
					<span id="addPathButton"><a class="btn1" href="javascript:void(0);" onclick="path.addPath('tk')">+添加</a></span>
				</c:if>
				
				</div>
				<div class="cl"></div>
			</div>
			<div>
				<div class="fl bd1 ustb">
					<div class="fl">
						<table id="pathTable" width="1170" cellpadding="0" cellspacing="0">
						</table>
					</div>
					<div class="cl"></div>
				</div>
			</div>
			
			<div class="contop">
				<div class="fl" style="margin-top: 15px;"><span class="xmenu" id='parentPathName'>路径配置</span><span id="corridorPathCount" class="count"></span></div>
				<div class="rl" style="margin-top: 20px;">
				<c:if test='<%=AuthoritiesUtil.isAllow("Li_Btn_Update") %>'>
					<span id="addCorPathButton"><a class="btn1" href="javascript:void(0);" onclick="path.addPath('tck')">+添加</a></span>
				</c:if>
				
				</div>
				<div class="cl"></div>
			</div>
			<div>
				<div class="fl bd1 ustb">
					<div class="fl">
						<table id="corridorPathTable" width="1170" cellpadding="0" cellspacing="0">
						</table>
					</div>
					<div class="cl"></div>
				</div>
			</div>
			
			<div class="contop">
				<div class="fl" style="margin-top: 10px;"><span class="xmenu">联络线管理</span><span id="lineCount" class="count"></span></div>
				<div class="rl" style="margin-top: 25px;">
				<c:if test='<%=AuthoritiesUtil.isAllow("Li_Btn_Update") %>'>
					<span><a class="btn1"  href="javascript:void(0);" onclick="path.addLine('tk_line')">+添加</a></span>
				</c:if>
				</div>
				<div class="cl"></div>
			</div>
			<div>
				<div class="fl bd1 ustb">
					<div class="fl">
						<table id="lineTable" width="1170" cellpadding="0" cellspacing="0">
							
						</table>
					</div>
					<div class="cl"></div>
				</div>
			</div>
		</div>
	</div>
	<div id="tk">
			<div class="tableCon">
				<div class="rl"><a class="btn3" href="javascript:void(0);" onclick="path.displayAddPanel('tk')">x</a></div>
				<div class="cl"></div>
				<input type="hidden" id="pathId"/>
				<div class="pdt20" >
					<div>
					 <span>&nbsp;&nbsp;首端&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					 </span><select id="startLine" style="width: 131px;" onchange="selectChange();"> </select>
					 <span>&nbsp;&nbsp;末端&nbsp;&nbsp;</span><select id="endLine" style="width: 131px;" onchange="selectChange();"> </select>    
					</div>
					<div class="pdt20 ">
					  &nbsp; 通道名称&nbsp;<input id="addPathName" readonly="readonly" placeholder="不可更改,由系统生成" size="17"/>
					  &nbsp; 级数&nbsp;&nbsp;<input id="priNum" size="17"/>
					</div>
					<div class="pdt20 " style="display: none;">
						首端区域输电费用(元/兆瓦时)&nbsp;&nbsp;<input id="IAreaTariff" size="25" value="0.0"/>
					</div>
					<div class="pdt20 " style="display: none;">
						末端区域输电费用(元/兆瓦时)&nbsp;&nbsp;<input id="JAreaTariff" size="25" value="0.0"/>
					</div>
					<div class="pdt20 " style="display: none;">
					          首端省内输电费用(元/兆瓦时)&nbsp;&nbsp;<input id="IProTariff" size="25" value="0.0"/>
					</div>
					<div class="pdt20 " style="display: none;">
					         末端省内输电费用(元/兆瓦时)&nbsp;&nbsp;<input id="JProTariff" size="25" value="0.0"/>
					</div>
					
					
					
					<div class="cl"></div>
					<div class="pdt20 rl pdr20"><a class="btn1"  href="javascript:void(0);" onclick="path.savePath('addPathName', 'startLine', 'endLine','lineSelectDiv')">提交</a></div>
				</div>
			</div>
			<div class="blbg">&nbsp;</div>
		</div>
		
		<div id="tck">
			<div class="bCorcon">
				<div class="rl"><a class="btn3" href="javascript:void(0);" onclick="path.displayAddPanel('tck')">x</a></div>
				<div align="center"><span style="font-size: 16px;color:#c19d30;font-weight: bold; " id="dialogTitle"></span></div>
				<input type="hidden" id="pathId"/>
				<div class="pdt20" >
					<div>
					  <span>优先级&nbsp;</span>
					 <!--  <input id="pathPri" size=12/> -->
					  <select id="pathPri" style="width: 80px;height: 23px;"></select>
					  &nbsp;排序&nbsp;&nbsp;
					  <input id="sort_" size=12/>
					
					</div>
					<div class="pdt20 " style="display: none;">
					            跨区网损(%)&nbsp;<input id="transLoss" size=12 value="0.0"/>
						折价系数A&nbsp;&nbsp;<input id="priceRatioA" size='12' value="1.0"/>
						折价系数B&nbsp;&nbsp;<input id="priceRatioB" size='12' value="0.0"/>
						
					</div>
					<!-- tielineTariff1 -->
					
					<div style="border: solid 1px #dcdcdb;padding: 10px;border-top: 0px;border-left: 0px;border-right: 0px;"></div>
					<!-- <div id="writeDiv">
						<div class="fl pdt20" id="firstWritePanel">
							第1段&nbsp;&nbsp;
							输电费&nbsp;<input type="text" id="tielineTariff1" size='8'/>
							&nbsp;网损(%)&nbsp;&nbsp;<input type="text" id="netLoss1" size='8'/>
							<a class="btn3 addPath" href="javascript:void(0);" onclick="path.addWritePanel()">+</a>
							<span class="fenge" style="color:#dcdcdb;">———————————————————————————</span>
						</div>
						
						
					</div> -->
					
					
					<div id="lineSelectDiv">
						<div class="fl pdt20" id="firstSelectPanel">
							 第1段&nbsp;
							<select id="lineSelect1" style="width: 90px;"></select>
							<span>&nbsp;成员方向&nbsp;</span>
							<select id="lineSelectOR1" style="width: 50px;">
							     <option value ="1">正</option>
	                             <option value ="2">反</option>
						    </select>
						          
							输电费(元/兆瓦时)&nbsp;&nbsp;<input type="text" id="tielineTariff1" size='8' value="0.0" />
							网损(%)&nbsp;&nbsp;<input type="text" id="netLoss1" size='8' value="0.0"/>
							<a class="btn3 addPath" href="javascript:void(0);" onclick="path.addLineSelect()">+</a>
						</div>
						<div class="cl"></div>
					</div>
					
					
					<div class="cl"></div>
					<div class="pdt20 rl pdr20"><a class="btn1"  href="javascript:void(0);" onclick="path.saveCorridorPath('addPathName', 'startLine', 'endLine','lineSelectDiv')">提交</a></div>
				</div>
			</div>
			<div class="blbg">&nbsp;</div>
		</div>
		
		<div id="tk_line">
			<div class="bcon">
				<div class="rl"><a class="btn3" href="javascript:void(0);" onclick="path.displayAddLinePanel('tk_line')">x</a></div>
				<div class="cl"></div>
				<input type="hidden" id="lineId"/>
				<div class="pdt20" >
					<div>
					            联络线名称&nbsp;&nbsp;<input id="addLineName" size="15px;"/>
					            排序&nbsp;&nbsp;
						
						<input id="lineSortId" size="15px;"/>
					   
					</div>
					<div class="pdt20 ">
						
						<span>首端&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						</span><select id="lstartLine" style="width: 120px;"> </select>
						<span>末端&nbsp;&nbsp;&nbsp;</span><select id="lendLine" style="width: 120px;"> </select>
					</div>
					<div class="pdt20 " style="display: none;">
						<span>输电费&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						</span><input id="tielinetariff" value="0.0"/>
						<span>网损率&nbsp;</span><input id="rate" size="14" value="0.0"/>
					</div>
					
					<div class="pdt20 "></div>
					<div class="cl"></div>
					<div class="pdt20 rl pdr20"><a class="btn1" href="javascript:void(0);" onclick="path.saveLine('addLineName', 'lstartLine', 'lendLine')">提交</a></div>
				</div>
			</div>
			<div class="blbg">&nbsp;</div>
		</div>
</state:override>

<%@ include file="/common/block/block.jsp" %>
