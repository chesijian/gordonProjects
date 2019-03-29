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
    </style>
    <link rel="stylesheet" href="test/jquery.tooltip/jquery.tooltip.css" type="text/css" />
    <script type="text/javascript" src="test/javascripts/jquery.min.js"></script>
    <script type="text/javascript" src="test/javascripts/jquery.tooltip.js"></script>
    <script type="text/javascript">
      $j = jQuery.noConflict();
      $j(document).ready(function(){
        $j("div.item").tooltip();
      });
    </script>
  <script charset="utf-8" async="true" src="test/javascripts/i5.js"></script></head>

  <body>

      <div id="item_1" class="item">
        Item 1
        <div class="tooltip_description" style="display:none" title="Item 1 Description">
          Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.
        </div>
      </div>

      <div id="item_2" class="item">
        Item 2
        <div class="tooltip_description" style="display:none" title="Item 2 Description">
          Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.
          <p>
          asdasdada
          </p>
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