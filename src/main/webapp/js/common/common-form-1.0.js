/*
var fileds_v_ = [{id:"price",vtypeArr:[{vtype:"allowBlank",text:"不能为空"},{vtype:"number",text:"必须是数字"}]},{id:"startDate",vtypeArr:[{vtype:"allowBlank",text:"不能为空"}]}];
*/	
/**
 * @author 大雄 处理表单的通用js
 */

function FormUtil() {

	this.validUtil =  new ValidUtil();
	
	this.checkValid = function (field,fileds_v){
		fileds_v_ =  isExtis(fileds_v)?fileds:fileds_v_;
		var vtypeArr=null;
		if (isExtis(fileds_v_)) {
			for (var i = 0; i < fileds_v_.length; i++) {
				if(fileds_v_[i].id == field.id){
					vtypeArr = fileds_v_[i].vtypeArr;
					break;
				}
			}
			if(isExtis(vtypeArr)){
				this.getValidData (field,vtypeArr);
			}
		}
	};
	/**
	 * id表示要遍历那个元素下面的所有input
	 * 验证表单
	 */
	this.checkFormValid = function(id_, fileds_v) {
		var data = "";
		var dom;
		if (isExtis(id_)) {
			dom = $('#' + id_);
		} else {
			dom = $(document);
		}
		fileds_v_ =  isExtis(fileds_v)?fileds_v:fileds_v_;
		//log(fileds_v_);
		if (isExtis(fileds_v_)) {
			for (var i = 0; i < fileds_v_.length; i++) {
				var fields = dom.find('[id=' + fileds_v_[i].id + ']');
				for (var j = 0; j < fields.length; j++) {
					var obj = fields[j];
					data = data + this.getValidData(obj,fileds_v_[i].vtypeArr);
	                 
				}
			}
		}
		if(this.validUtil.isNull(data) == false){
	    	alert(data);
	    	return false;
	    }
		return true;
	};
	
	this.getValidData = function (obj,vtypeArr){
		
		widgetId = obj.id;
		var value = obj.value;
		//alert(widgetId+"==="+value);
		var data="";
		isValid = true;
		var vtype;
		if(obj.tagName == "INPUT" && (obj.getAttribute("type") == "radio" || obj.getAttribute("type") == "checkbox")){
			for(var i = 0;i<vtypeArr.length;i++){
				vtype = vtypeArr[i].vtype;
				if(vtype == "allowBlank"){
					var isnull = true;
					var fields = $('input[name=' + obj.name + ']');
		        	for (var m = 0; m < fields.length; m++){
		        		if(fields[m].checked == true){
		        			isnull = false;
		        			break;
		        		}
		        	}
		        	if(isnull == true){
		        		vtypeText = vtypeArr[i].text;
		        		if (this.validUtil.isNull(vtypeText) == true){
		                    data = data + widgetId + "没有被选择！" + "\n";
		                }else{
		                    data = data + vtypeText + "\n";
		                } 
		        	}
					break;
				}
			}
			
        }else{
        	for(var i = 0;i<vtypeArr.length;i++){
    			vtype = vtypeArr[i].vtype;
    			vtypeText = vtypeArr[i].text;
    			//alert(vtype +"===="+vtypeText);
    			if (vtype == "allowBlank"){
    				if (this.validUtil.isNull(value) == true)
    			    {
    					// alert(value+isNull(value)+isNull(blankText)+blankText);
    			    	isValid = false;
    			        if (this.validUtil.isNull(vtypeText) == true){
    			            data = data + widgetId + "不能为空！" + "\n";
    			        }else{
    			            data = data + vtypeText + "\n";
    			        }
    			    }
    			}
    			else if (vtype == "integer")
    		    {
    		        // 字母
    		    	if(!this.validUtil.isInteger(value)){
    		    		isValid = false;
    		            if (this.validUtil.isNull(vtypeText) == true){
    		                data = data + widgetId + "必须是整数！" + "\n";
    		            }else{
    		                data = data + vtypeText + "\n";
    		            }
    		    	}
    		    }else if (vtype == "number"){
    		    	
    		        // 数字
    		    	if(!this.validUtil.isNumber(value)){
    		    		isValid = false;
    		            if (this.validUtil.isNull(vtypeText) == true){
    		                data = data + widgetId + "必须是数字！" + "\n";
    		            }else{
    		                data = data + vtypeText + "\n";
    		            }
    		    	}
    		    }else if (vtype == "alpha"){
    		        // 字母
    		    	if(!this.validUtil.isEnglish(value)){
    		    		isValid = false;
    		            if (this.validUtil.isNull(vtypeText) == true){
    		                data = data + widgetId + "必须是字母！" + "\n";
    		            }else{
    		                data = data + vtypeText + "\n";
    		            }
    		    	}
    		    }else if (vtype == "alphanum"){
    		        // 数字或字母
    		    	if(!this.validUtil.isNumberOrLetter(value)){
    		    		isValid = false;
    		            if (this.validUtil.isNull(vtypeText) == true){
    		                data = data + widgetId + "必须是数字或字母！" + "\n";
    		            }else{
    		                data = data + vtypeText + "\n";
    		            }
    		    	}
    		    }else if (vtype == "email"){
    		    	 if(!isEmail(value)){
    		    		 isValid = false;
    		             if (this.validUtil.isNull(vtypeText) == true){
    		                 data = data + widgetId + "必须是E-Mail格式！" + "\n";
    		             }else{
    		                 data = data + vtypeText + "\n";
    		             } 
    		    	 }
    		    }else if (vtype == "ip"){
    		    	 if(!this.validUtil.isIP(value)){
    		    		 isValid = false;
    		             if (this.validUtil.isNull(vtypeText) == true){
    		                 data = data + widgetId + "必须是ip地址！" + "\n";
    		             }else{
    		                 data = data + vtypeText + "\n";
    		             } 
    		    	 }
    		    }else if (vtype == "mobile"){
    		    	if(!this.validUtil.isMobile(value)){
    		    		isValid = false;
    		            if (this.validUtil.isNull(vtypeText) == true){
    		                data = data + widgetId + "必须是手机号码！" + "\n";
    		            }else{
    		                data = data + vtypeText + "\n";
    		            } 
    		    	}
    		    }else if (vtype == "decimal"){
    		    	if(!this.validUtil.isDecimal(value)){
    		    		isValid = false;
    		            if (this.validUtil.isNull(vtypeText) == true){
    		                data = data + widgetId + "必须是小数！" + "\n";
    		            }else{
    		                data = data + vtypeText + "\n";
    		            } 
    		    	}
    		    }else if (vtype == "chinese"){
    		    	if(!this.validUtil.isChinese(value)){
    		    		isValid = false;
    		            if (this.validUtil.isNull(vtypeText) == true){
    		                data = data + widgetId + "必须是中文！" + "\n";
    		            }else{
    		                data = data + vtypeText + "\n";
    		            } 
    		    	}
    		    }
    		}
        }
	    if(isValid == false){
	    		var self = $("#"+widgetId);
	    		var next = $("#_image_valid_"+widgetId);
		    	if(next != undefined && next.length>0){
		    		next.attr("title",data);
		    		next.show();
		    	}else{
		    		self.after('<span title="'+data+'"  id="_image_valid_'+widgetId+'"  ><img src="'+baseUrl+'/img/erinfo.gif"  /></span>');
		    	}
	    		/*
	    		var span_required = self.next("span[class=css_span_required]");
	    		if(!this.validUtil.isNull(span_required) && span_required.length>0){
	    			alert("ok");
	    			span_required.after('<span title="'+data+'"  id="_image_valid_'+widgetId+'"   ><img src="'+baseUrl+'/img/erinfo.gif"  /></span>');
	    		}else{
	    			
	    			
	    		}*/
	    		
	    	if(obj.className.indexOf("notValid")==-1){
	    		obj.className = (obj.className+" notValid");
	    	}
	    }else{
	    	var next = $("#_image_valid_"+widgetId);
	    	if(next != undefined){
	    		next.remove();
	    	}
	    	if(obj.className.indexOf("notValid") > -1){
	    		obj.className = (obj.className.replace(" notValid",""));
	    	}
	    }
	    
	    return data;
	};

}
var formUtil = new FormUtil();