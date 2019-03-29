/**
 * 
 */
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
	
	$(function(){
		$('.menu1').find('a[name=用户管理]').attr('class', 'menufocus');
	});
	this.updateUser= function(name,state) {
		$.ajax({
			url : "updateUserStatus",
			type : 'POST',
			dataType : 'json',
			data : {
				name : name,
				state:state
			},
			success : function(result) {
				if (result) {
					changePage(currentPage);
					//window.location.href = "${pageContext.request.contextPath }/manager/init";
				} else {
					alert("修改失败!");
				}
			},
			error : function(xhr, status) {
				alert("系统错误!");
			},
		    });
	};
	this.deleteUser= function(id) {
		Ewin.confirm({ message: "确认要删除选择的数据吗？" }).on(function (e) {
				//alert(id);
		       if (e) {
		    	   $.ajax({
			   			url : "deleteUser",
			   			type : 'POST',
			   			dataType : 'json',
			   			data : {
			   				id : id
			   			},
			   			success : function(result) {
			   				if (result) {
			   					changePage(currentPage);
			   				} else {
			   					alert("删除失败!");
			   				}
			   			},
			   			error : function(xhr, status) {
			   				alert("系统错误!");
			   			},
			   		    });  
		         return;
		       }
		});
		
	};
	this.editUser= function(id) {
		var url = baseUrl+"/manager/editUser?id="+id;
		createWin(url,560,400,"用户信息");
	};
	this.newUser= function(id) {
		var url = baseUrl+"/manager/newUser";
		createWin(url,560,460,"用户信息");
	};
	//注册按钮事件
	function approve(user){
		var pd=confirm("您确定要对用户："+user+"审批吗？");
		if(pd==true){
			var option = {
					url : "${pageContext.request.contextPath }/manager/approveUser",
					type : "post",
					data : {
						user : user,
					},
					dataType : "json",
					success : function(response) {
						if ("success" == response) {
							window.location.href = "${pageContext.request.contextPath }/manager/init";
						}else {
							alert("审批失败");
						}
					},
					error : function() {
						alert("系统错误");
					}
				};
				$.ajax(option);
		}
	}
	
	function post(url, params) {
	    var temp = document.createElement("form");
	    temp.action = url;
	    temp.method = "post";
	    temp.style.display = "none";
	    for (var x in params) {
	        var opt = document.createElement("input");
	        opt.name = x;
	        opt.value = params[x];
	        temp.appendChild(opt);
	    }
	    document.body.appendChild(temp);
	    temp.submit();
	    return temp;
	} 
	
	