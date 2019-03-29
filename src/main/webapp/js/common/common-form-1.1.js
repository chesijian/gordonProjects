/**
 * @author 大雄 处理表单的通用js
 */

function FormUtil() {

	this._v = validUtil;

	this.checkValid = function(field, fileds_v) {
		fileds_v_ = isExtis(fileds_v) ? fileds : fileds_v_;
		if (isExtis(fileds_v_)) {
			var vtypeArr = fileds_v_[field.id];
			if (isExtis(vtypeArr)) {
				this.getValidData(field, vtypeArr);
			}
		}
	};
	/**
	 * id表示要遍历那个元素下面的所有input 验证表单
	 */
	this.checkFormValid = function(id_, fileds_v) {
		var data = "";
		var dom;
		if (isExtis(id_)) {
			dom = $('#' + id_);
		} else {
			dom = $(document);
		}
		fileds_v_ = isExtis(fileds_v) ? fileds_v : fileds_v_;
		// log(fileds_v_);
		if (isExtis(fileds_v_)) {
			for (id in fileds_v_) {
				var fields = dom.find('[id=' + id + ']');
				for (var j = 0; j < fields.length; j++) {
					var obj = fields[j];
					data = data + this.getValidData(obj, fileds_v_[id]);

				}
			}
		}
		if (this._v.isNull(data) == false) {
			//alert(data);
			return false;
		}
		return true;
	};

	this.getValidData = function(obj, vtypes) {

		widgetId = obj.id;
		var value = obj.value;
		// alert(widgetId+"==="+value);
		var data = "";
		isValid = true;
		var vtype;
		if (obj.tagName == "INPUT"
				&& (obj.getAttribute("type") == "radio" || obj
						.getAttribute("type") == "checkbox")) {
			for ( var vtype in vtypes) {
				if (vtype == "allowBlank") {
					var isnull = true;
					var fields = $('input[name=' + obj.name + ']');
					for (var m = 0; m < fields.length; m++) {
						if (fields[m].checked == true) {
							isnull = false;
							break;
						}
					}
					if (isnull == true) {
						vtypeText = vtypes[vtype];
						if (this._v.isNull(vtypeText) == true) {
							data = data + widgetId + "没有被选择！" + "\n";
						} else {
							data = data + vtypeText + "\n";
						}
					}
					break;
				}
			}

		} else {
			for ( var vtype in vtypes) {
				vtypeText = vtypes[vtype];
				// alert(vtype +"===="+vtypeText);
				if (vtype == "allowBlank") {
					if (this._v.isNull(value) == true) {
						// alert(value+isNull(value)+isNull(blankText)+blankText);
						isValid = false;
						if (this._v.isNull(vtypeText) == true) {
							data = data + widgetId + "不能为空！" + "\n";
						} else {
							data = data + vtypeText + "\n";
						}
					}
				} else if (vtype == "integer") {
					// 字母
					if (!this._v.isInteger(value)) {
						isValid = false;
						if (this._v.isNull(vtypeText) == true) {
							data = data + widgetId + "必须是整数！" + "\n";
						} else {
							data = data + vtypeText + "\n";
						}
					}
				} else if (vtype == "number") {

					// 数字
					if (!this._v.isNumber(value)) {
						isValid = false;
						if (this._v.isNull(vtypeText) == true) {
							data = data + widgetId + "必须是数字！" + "\n";
						} else {
							data = data + vtypeText + "\n";
						}
					}
				} else if (vtype == "alpha") {
					// 字母
					if (!this._v.isEnglish(value)) {
						isValid = false;
						if (this._v.isNull(vtypeText) == true) {
							data = data + widgetId + "必须是字母！" + "\n";
						} else {
							data = data + vtypeText + "\n";
						}
					}
				} else if (vtype == "alphanum") {
					// 数字或字母
					if (!this._v.isNumberOrLetter(value)) {
						isValid = false;
						if (this._v.isNull(vtypeText) == true) {
							data = data + widgetId + "必须是数字或字母！" + "\n";
						} else {
							data = data + vtypeText + "\n";
						}
					}
				} else if (vtype == "email") {
					if (!isEmail(value)) {
						isValid = false;
						if (this._v.isNull(vtypeText) == true) {
							data = data + widgetId + "必须是E-Mail格式！" + "\n";
						} else {
							data = data + vtypeText + "\n";
						}
					}
				} else if (vtype == "ip") {
					if (!this._v.isIP(value)) {
						isValid = false;
						if (this._v.isNull(vtypeText) == true) {
							data = data + widgetId + "必须是ip地址！" + "\n";
						} else {
							data = data + vtypeText + "\n";
						}
					}
				} else if (vtype == "mobile") {
					if (!this._v.isMobile(value)) {
						isValid = false;
						if (this._v.isNull(vtypeText) == true) {
							data = data + widgetId + "必须是手机号码！" + "\n";
						} else {
							data = data + vtypeText + "\n";
						}
					}
				} else if (vtype == "decimal") {
					if (!this._v.isDecimal(value)) {
						isValid = false;
						if (this._v.isNull(vtypeText) == true) {
							data = data + widgetId + "必须是小数！" + "\n";
						} else {
							data = data + vtypeText + "\n";
						}
					}
				} else if (vtype == "chinese") {
					if (!this._v.isChinese(value)) {
						isValid = false;
						if (this._v.isNull(vtypeText) == true) {
							data = data + widgetId + "必须是中文！" + "\n";
						} else {
							data = data + vtypeText + "\n";
						}
					}
				}
			}
		}
		this.setValidTip(widgetId, isValid, data);

		return data;
	};

	/**
	 * widgetId表示控件id isValid为true时出现提示符
	 */
	this.setValidTip = function(widgetId, isValid, data) {
		var self = $("#" + widgetId);
		if (isValid == false) {
			var next = $("#_image_valid_" + widgetId);
			if (next != undefined && next.length > 0) {
				next.attr("title", data);
				next.show();
			} else {
				self.after('<span title="' + data + '"  id="_image_valid_'
						+ widgetId + '"  ><img src="' + baseUrl
						+ '/img/erinfo.gif"  /></span>');
			}

			if (self.attr("class") != undefined && self.attr("class").indexOf("notValid") == -1) {
				self.addClass("notValid");
			}
		} else {
			var next = $("#_image_valid_" + widgetId);
			if (next != undefined) {
				next.remove();
			}
			if (self.attr("class") != undefined && self.attr("class").indexOf("notValid") > -1) {
				self.removeClass("notValid");
			}
		}
	};

}
var formUtil = new FormUtil();