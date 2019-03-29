<%@ page language="java" import="java.util.*,com.state.util.VersionCtrl" pageEncoding="UTF-8"
	contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://www.state.com/state" prefix="state"%>

<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="ECharts">
    <meta name="author" content="kener.linfeng@gmail.com">
    <title>ECharts · Example</title>

 <style type="text/css">
      * { font-family: monaco; color: white; }
      div.item { width:100px; height:50px; background-color: maroon; text-align:center; padding-top:25px; }
      div#item_1 { position: absolute; top: 50px; left: 50px; }
      div#item_2 { position: absolute; top: 500px; left: 0px; }      
      div#item_3 { position: absolute; top: 0px; left: 500px; }            
      div#item_4 { position: absolute; top: 500px; left: 500px; }   
       .circle  
{  
	border:1px;
	border-color:red;
	background-color:red;
    height: 20px;  
    width: 20px;  
    border-radius: 50px;  
}                  
    </style>
    <link rel="stylesheet" href="jquery.tooltip/jquery.tooltip.css" type="text/css" />
    
    <script type="text/javascript" src="javascripts/jquery.min.js"></script>
    <script type="text/javascript" src="javascripts/jquery.tooltip.js"></script>
    
    <script src="${pageContext.request.contextPath }/js/charts/highcharts.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
	
    
    <script type="text/javascript">
      $j = jQuery.noConflict();
      $j(document).ready(function(){
        $j("div.item").tooltip();
        $j("#h01").tooltip();
      });
      function  createIntervalChart(){
    	 // alert("ok");
    	  $j('#div_chart').highcharts({
    	        title: {
    	            text: 'Monthly Average Temperature',
    	            x: -20 //center
    	        },
    	        subtitle: {
    	            text: 'Source: WorldClimate.com',
    	            x: -20
    	        },
    	        xAxis: {
    	            categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
    	                'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
    	        },
    	        yAxis: {
    	            title: {
    	                text: 'Temperature (°C)'
    	            },
    	            plotLines: [{
    	                value: 0,
    	                width: 1,
    	                color: '#808080'
    	            }]
    	        },
    	        tooltip: {
    	            valueSuffix: '°C'
    	        },
    	        legend: {
    	            layout: 'vertical',
    	            align: 'right',
    	            verticalAlign: 'middle',
    	            borderWidth: 0
    	        },
    	        series: [{
    	            name: 'Tokyo',
    	            data: [7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6]
    	        }, {
    	            name: 'New York',
    	            data: [-0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6, 2.5]
    	        }, {
    	            name: 'Berlin',
    	            data: [-0.9, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 1.0]
    	        }, {
    	            name: 'London',
    	            data: [3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8]
    	        }]
    	    });
      }
    </script>
  
  <body>
	<div id="h01" class="circle"></div>
      <div id="item_1" class="item">
        Item 1
        <div class="tooltip_description" style="display:none" title="Item 1 Description">
          Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.
        	<p>
          asdasdada
          </p>
        </div>
      </div>

      <div id="item_2" class="item">
        Item 2
        <div class="tooltip_description" style="display:none" title="Item 2 Description">
          Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.
        </div>
      </div>

      <div id="item_3" class="item">
        Item 3
        <div class="tooltip_description" style="display:none" title="Item 3 Description">
          Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.
        </div>
      </div>

      <div id="item_4" class="item">
        Item 4
        <div class="tooltip_description" style="display:none" title="Item 4 Description">
          Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.
        </div>
      </div>

</body>
</html>