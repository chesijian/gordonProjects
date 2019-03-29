/**
 * JQuery Tooltip Plugin
 *
 * Licensed under the MIT (http://www.opensource.org/licenses/mit-license.php)
 *
 * Written by Shahrier Akram <shahrier.akram@gmail.com>
 *
 * Tooltip is a jQuery plugin implementing unobtrusive javascript interaction that appears
 * near DOM elements, wherever they may be, that require extra information. 
 *
 * Visit http://gdakram.github.com/JQuery-Tooltip-Plugin for demo.
**/
function hideTip(){
		$("body").find("div.jquery-gdakram-tooltip").stop().remove();
	}
(function($) {

	
   $.fn.tooltip = function(settings) {
     // Configuration setup
     config = { 
       'dialog_content_selector' : 'div.tooltip_description',
       'animation_distance' : 50,
       'opacity' : 1,
       'arrow_left_offset' : 70,
       'arrow_top_offset' : 50,
       'arrow_height' : 20,
       'arrow_width' : 20,
       'animation_duration_ms' : 300,
       'event_in':'click',
       'event_out':'dbclick'
     }; 
     if (settings) $.extend(config, settings);

     /**
      * Apply interaction to all the matching elements
      **/
     this.each(function() {
       $(this).bind(config.event_in,function(){
         _show(this);
       })
       .bind(config.event_out,function(){
         _hide(this);
       });
     });
          
     /**
      * Positions the dialog box based on the target
      * element's location
      **/
     function _show(target_elm) {
    	 _hide(this); 
       var dialog_content = $(target_elm).find(config.dialog_content_selector);
       var dialog_box = _create(dialog_content);
       createIntervalChart($(target_elm).attr('id'));
       var is_top_right = $(target_elm).hasClass("tooltiptopright");
       var is_bottom_right = $(target_elm).hasClass("tooltipbottomright");
       var is_top = $(target_elm).hasClass("tooltiptop");
       var is_bottom = $(target_elm).hasClass("tooltipbottom");
       var has_position = is_top_right || is_bottom_right || is_top || is_bottom;
       var position;
       
      
      //  alert($(window).width());
       // position and show the box
       var top = ($(window).height()/2-150);
       var left = ($(window).width()/2-200);
       
       //console.info($("body").width()+"=="+left);
       top = 50;
       //left = 0;
       $(dialog_box).css({ 
    	  // 'margin-left':'auto',
    	  // 'margin-right':'auto',
    	   position:'absolute',
         top : top + "px", 
         left : left + "px", 
         opacity : config.opacity
       });       
       $(dialog_box).find("div.right_arrow").show();
      // $(dialog_box).find(position.arrow_class).show();
       $(dialog_box).show();
       // begin animation
       /*
       $(dialog_box).animate({
         top : position.end.top,
         left: position.end.left,
         opacity : "toggle"
       }, config.animation_duration_ms);       
       */
     }; // -- end _show function
     
     /**
      * Stop the animation (if any) and remove from dialog box from the DOM
      */
     function _hide(target_elm) {
       $("body").find("div.jquery-gdakram-tooltip").stop().remove();
     };
     
     /**
      * Creates the dialog box element
      * and appends it to the body
      **/
     function _create(content_elm) {
       var header = ($(content_elm).attr("title")) ? "<h1>" + $(content_elm).attr("title") + "</h1>" : '';
       return $("<div class='jquery-gdakram-tooltip' >\
         <div class='right_arrow arrow' style='display:\"\"'><button title='cancel' i='close' class='ui-dialog-close'>×</button></div>\
         <div class='content' id='div_chart' style='width:400px;height:300px;'>" + header + $(content_elm).html() + "</div>\
         <div style='clear:both'></div>\
       </div>").appendTo('body');
     };
          
     return this; 
   };
 
 })(jQuery);