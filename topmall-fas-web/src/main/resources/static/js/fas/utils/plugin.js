"use strict";
define(function(require, exports, module) {
	let config = require('../config');
	let status = $.fas.datas.status;
	
	function pluginFactory(name, bind) {
		var plugin = function(options, param) {
			if(typeof options == 'string') {
				return plugin.methods[options](this, param);
			}
			options = options || {};
			return this.each(function() {
				var state = $.data(this, name);
				if(state) {
					$.extend(state.options, options);
				} else {
					var op = plugin.parseOptions(this);
					options = $.extend({}, plugin.defaults, options, op);
					$.data(this, name, { options: options });
				}
				if($.isFunction(bind))
					bind(this);
			});
		};
		$.parser.plugins.push(name);
		plugin.parseOptions = function(target) {
			return $.parser.parseOptions(target, [
				'data'
			]);
		};
		plugin.defaults = {

		};
		plugin.methods = {
			options: function(jq) {
				return $.data(jq[0], _name).options;
			}
		};
		return plugin;
	};
	
	$(function($) {
		$.fas.datasFunction();
		var datas=$.fas.datas.endDatas;
		var _name = 'comboDate';
		$.parser.plugins.push(_name);
		$.fn.form.explugins.push(_name);

		$.fn.plugin(_name, bind);

		function bind(target) {
			var options = $.data(target, _name).options;
			var op = $.extend({ valueField: "id", textField: "name" }, options);

			op.data = datas;
			$(target).combobox(op);
		}

		function _disable(jq) {
			$(jq).attr("readonly", true).combobox("disable");
		}

		function _enable(jq) {
			$(jq).attr("readonly", true).combobox("enable");
		}
		$.fn.comboDate.methods = {
			options: function(jq) {
				return $.data(jq[0], _name).options;
			},
			disable: function(jq) {
				$.each(jq, function(index, item) {
					_disable(item);
				});
			},
			enable: function(jq) {
				$.each(jq, function(index, item) {
					_enable(item);
				});
			},
			getValue: function(jq) {
				var op = $.data(jq[0], _name).options;
				if(op.multiple){
					let values=$(jq).combobox('getValues');
					if(values.length>0){
						return values.join(",");
					}
					return null;
				}else{
					return $(jq).combobox('getValue');
				}	
			},
			setValue: function(jq, value) {
				return $(jq).combobox('setValue', value);
			}
		};
	});
	
	$(function($) {
		var datas = $.fas.datas;
		var _name = 'combocommon';
		$.parser.plugins.push(_name);
		$.fn.form.explugins.push(_name);

		$.fn.plugin(_name, bind);

		function bind(target) {
			var options = $.data(target, _name).options;
			var op = $.extend({ valueField: "id", textField: "name" }, options);
			if(op.datarange){
				let dataJson = datas[op.type];
				let datarange = op.datarange.split(',');
				op.data=[];
				for(let i in dataJson){
					for(let t in datarange){
						if(dataJson[i].id==datarange[t]){
							op.data.push(dataJson[i]);
						}
					}
				}
			}else{
				op.data = datas[op.type];
			}

			$(target).combobox(op);
		}

		function _disable(jq) {
			$(jq).attr("readonly", true).combobox("disable");
		}

		function _enable(jq) {
			$(jq).attr("readonly", true).combobox("enable");
		}

		$.fn.combocommon.methods = {
			options: function(jq) {
				return $.data(jq[0], _name).options;
			},
			disable: function(jq) {
				$.each(jq, function(index, item) {
					_disable(item);
				});
			},
			enable: function(jq) {
				$.each(jq, function(index, item) {
					_enable(item);
				});
			},
			getValue: function(jq) {
				return $(jq).combobox('getValue');
			},
			setValue: function(jq, value) {
				return $(jq).combobox('setValue', value);
			}
		};
	});
	
	
	// 结算期等查询下拉列表
	$(function($) {
		var _name = 'settlementDateBox';
		$.fn.form.explugins.push(_name);
		$.fn.plugin(_name, render);
		function _disable(jq) {
			$(jq).attr("readonly", true).combogrid("disable");
		}
	
		function _enable(jq) {
			$(jq).attr("readonly", true).combogrid("enable");
		}
	
		function render(jq) {
			var options = $.data(jq, _name).options;
			var url = `${config.rootUrl}/shop/balance/date/dtl/list`;
			var columns = [{
				field : 'settleStartDate',
				title : '开始时间',
				width : 200,
				align : 'left',
				sortable : true
			},
			{
				field : 'settleEndDate',
				title : '截止时间',
				width : 200,
				align : 'left',
				sortable : true
			}];
			var dataOp = $.extend({}, options, {
				loadMsg:'数据加载中，请稍等......',
				delay : 700,// 延迟
				pageSize : 30,//在设置分页属性的时候初始化页面大小。
				pageList : [ 30, 100, 200, 300, 400, 500 ],//在设置分页属性的时候 初始化页面大小选择列表。
				idField : 'settleStartDate',
				textField : 'settleStartDate',
				valueFeild:'settleStartDate',
				url:url,
				mode:'remote',
				panelWidth:350,
				columns : [ columns ],
				filter : function(q, row) {
					var opts = $(this).combogrid('options');
					return row[opts.textField].indexOf(q) == 0;
				},
				fitColumns : true,
				onHidePanel : function() {// 选中下拉项后关闭面板，填充数据
					var options = $(jq).combogrid('options');
					var g = $(jq).combogrid('grid'); // 获取数据表格对象
					var r = g.datagrid('getSelected'); // 获取选择的行
					if (r) {
						options.selectedGridData = $.extend(true, {}, r);// 设置data
						_setValue(jq, options.selectedGridData);
						//自定义传参的函数（在隐藏下拉网格触发的时候）
						if(options.clickFn) {
							options.clickFn(options.selectedGridData);
						}
					} else {
						if (!(options.selectedGridData && options.selectedGridData[options.textField] == $(jq).parent().find("span").children("input").val())) {
							_clear(jq);
						}
					}
				},
				onShowPanel : function() {
					_getChangedQueryParams(jq);//获取动态的查询条件
					$(jq).combogrid("grid").datagrid('reload');// 若是根据前面已选的条件来查询数据需要重载一遍datagrid
				},
				onClickRow:function(rowIndex,rowData){
					$(jq).parents('tbody').find("#settleEndDate").datebox('setValue',rowData.settleEndDate);
				},
				hasDownArrow:options.hasDownArrow,
				pagination : true
			});
			// 下拉网格
			$(jq).combogrid(dataOp);
			if (dataOp.isRequired) {
				$(jq).combo({
					required : true,
					tipPosition:'none'
				});
			}
		}
		
		//获取动态的查询条件
		function _getChangedQueryParams(jq){
			$(jq).combogrid("grid").datagrid('options').queryParams={};
			$(jq).combogrid("options").queryParams={};//清除combogrid 的QueryParams
			var parents = $(jq).parents('tr').parent();
			var options = $.data(jq, _name).options;
			var settleMonth =parents.find("#settleMonth").datebox('getValue');
			if(options.isSupplier){
				var companyNo =parents.find("#companyNo").combobox('getValue');
				var supplierNo = parents.find("#supplierNo").combobox('getValue');
			}else{
				var shopNo =parents.find("#shopNo").combobox('getValue');
				var counterNo = parents.find("#counterNo").combobox('getValue');
			}
			if (shopNo && counterNo && settleMonth) {
				$(jq).combogrid("grid").datagrid('options').queryParams={shopNo:shopNo,
																		counterNo:counterNo,
																		settleMonth:settleMonth};
				$(jq).combogrid("options").queryParams = {	shopNo:shopNo,
															counterNo:counterNo,
															settleMonth:settleMonth};
			} else if(companyNo&&supplierNo&&settleMonth){
				$(jq).combogrid("grid").datagrid('options').queryParams={companyNo:companyNo,
																		supplierNo:supplierNo,
																		settleMonth:settleMonth};
				$(jq).combogrid("options").queryParams = {	companyNo:companyNo,
															supplierNo:supplierNo,
															settleMonth:settleMonth};
			}else{
				//如果店铺.专柜,结算月等没有值,就设置一个不存在的店铺
				$(jq).combogrid("grid").datagrid('options').queryParams={shopNo:"123445556576"};
				$(jq).combogrid("options").queryParams = {	shopNo:"123445556576"};
			}
		}
		
	    function _setValue(jq,value){
	    	$(jq).combogrid('setValue', value);
	    }
		
		function _getValue(jq) {
			$(jq).combogrid('getValue');
		}
		
		function _getData(jq) {
			var op = $.data(jq, _name).options;
			return op.selectedGridData;
		}
	
		function _clear(jq) {
			$(jq).combogrid('clear');// 没选择数据则清空编辑框
			var op = $(jq).combogrid('options');
			if (op.selectedGridData != undefined && op.selectedGridData != '') {
				op.selectedGridData[op.idField] = null;// 清空查询精灵的value
				op.selectedGridData[op.textField] = null;// 清空查询精灵的name
				_setValue(jq, op);
			}else{
				_setValue(jq, '');
			}
		}
		
		//清空queryParams
		function _clearQueryParams(jq){
			$(jq).combogrid("grid").datagrid('options').queryParams={};
			$(jq).combogrid("options").queryParams={};
		}
		
		$.fn[_name].methods = {
			options : function(jq) {
				return $.data(jq[0], _name).options;
			},
			setValue : function(jq, value) {
				_setValue(jq[0], value);
			},
			getValue : function(jq) {
				return _getValue(jq[0]);
			},
			getData : function(jq) {
				return _getData(jq[0]);
			},
			clear : function(jq, param) {
				$.each(jq, function(index, item) {
					_clear(item);
				});
			},
			disable : function(jq) {
				$.each(jq, function(index, item) {
					_disable(item);
				});
	
			},
			enable : function(jq) {
				$.each(jq, function(index, item) {
					_enable(item);
				});
			},
			clearQueryParams : function(jq) {
				$.each(jq, function(index, item) {
					_clearQueryParams(item);
				});
			}
		};

	});
	
	// 结算期等查询下拉列表
	$(function($) {
		var _name = 'settlementMallDateBox';
		$.fn.form.explugins.push(_name);
		$.fn.plugin(_name, render);
		function _disable(jq) {
			$(jq).attr("readonly", true).combogrid("disable");
		}
	
		function _enable(jq) {
			$(jq).attr("readonly", true).combogrid("enable");
		}
	
		function render(jq) {
			var options = $.data(jq, _name).options;
			var url = `${config.rootUrl}/mall/balance/date/dtl/list`;
			var columns = [{
				field : 'settleStartDate',
				title : '开始时间',
				width : 200,
				align : 'left',
				sortable : true
			},
			{
				field : 'settleEndDate',
				title : '截止时间',
				width : 200,
				align : 'left',
				sortable : true
			}];
			var dataOp = $.extend({}, options, {
				loadMsg:'数据加载中，请稍等......',
				delay : 700,// 延迟
				pageSize : 30,//在设置分页属性的时候初始化页面大小。
				pageList : [ 30, 100, 200, 300, 400, 500 ],//在设置分页属性的时候 初始化页面大小选择列表。
				idField : 'settleStartDate',
				textField : 'settleStartDate',
				valueFeild:'settleStartDate',
				url:url,
				mode:'remote',
				panelWidth:350,
				columns : [ columns ],
				filter : function(q, row) {
					var opts = $(this).combogrid('options');
					return row[opts.textField].indexOf(q) == 0;
				},
				fitColumns : true,
				onHidePanel : function() {// 选中下拉项后关闭面板，填充数据
					var options = $(jq).combogrid('options');
					var g = $(jq).combogrid('grid'); // 获取数据表格对象
					var r = g.datagrid('getSelected'); // 获取选择的行
					if (r) {
						options.selectedGridData = $.extend(true, {}, r);// 设置data
						_setValue(jq, options.selectedGridData);
						//自定义传参的函数（在隐藏下拉网格触发的时候）
						if(options.clickFn) {
							options.clickFn(options.selectedGridData);
						}
					} else {
						if (!(options.selectedGridData && options.selectedGridData[options.textField] == $(jq).parent().find("span").children("input").val())) {
							_clear(jq);
						}
					}
				},
				onShowPanel : function() {
					_getChangedQueryParams(jq);//获取动态的查询条件
					$(jq).combogrid("grid").datagrid('reload');// 若是根据前面已选的条件来查询数据需要重载一遍datagrid
				},
				onClickRow:function(rowIndex,rowData){
					$(jq).parents('tbody').find("#settleEndDate").datebox('setValue',rowData.settleEndDate);
				},
				hasDownArrow:options.hasDownArrow,
				pagination : true
			});
			// 下拉网格
			$(jq).combogrid(dataOp);
			if (dataOp.isRequired) {
				$(jq).combo({
					required : true,
					tipPosition:'none'
				});
			}
		}
		
		//获取动态的查询条件
		function _getChangedQueryParams(jq){
			$(jq).combogrid("grid").datagrid('options').queryParams={};
			$(jq).combogrid("options").queryParams={};//清除combogrid 的QueryParams
			var parents = $(jq).parents('tr').parent();
			var options = $.data(jq, _name).options;
			
			var shopNo =parents.find("#shopNo").combobox('getValue');
			var mallNo = parents.find("#mallNo").combobox('getValue');
			var bunkGroupNo = parents.find("#bunkGroupNo").combobox('getValue');
			var settleMonth =parents.find("#settleMonth").datebox('getValue');
			if (shopNo&& mallNo && settleMonth&&bunkGroupNo) {
				$(jq).combogrid("grid").datagrid('options').queryParams={shopNo:shopNo,
																		mallNo:mallNo,
																		settleMonth:settleMonth,
																		bunkGroupNo:bunkGroupNo};
				$(jq).combogrid("options").queryParams = {	shopNo:shopNo,
															mallNo:mallNo,
															settleMonth:settleMonth,
															bunkGroupNo:bunkGroupNo};
			} else {
				//如果店铺.专柜,结算月等没有值,就设置一个不存在的店铺
				$(jq).combogrid("grid").datagrid('options').queryParams={shopNo:"123445556576"};
				$(jq).combogrid("options").queryParams = {	shopNo:"123445556576"};
			}
		}
		
	    function _setValue(jq,value){
	    	$(jq).combogrid('setValue', value);
	    }
		
		function _getValue(jq) {
			$(jq).combogrid('getValue');
		}
		
		function _getData(jq) {
			var op = $.data(jq, _name).options;
			return op.selectedGridData;
		}
	
		function _clear(jq) {
			$(jq).combogrid('clear');// 没选择数据则清空编辑框
			var op = $(jq).combogrid('options');
			if (op.selectedGridData != undefined && op.selectedGridData != '') {
				op.selectedGridData[op.idField] = null;// 清空查询精灵的value
				op.selectedGridData[op.textField] = null;// 清空查询精灵的name
				_setValue(jq, op);
			}else{
				_setValue(jq, '');
			}
		}
		
		//清空queryParams
		function _clearQueryParams(jq){
			$(jq).combogrid("grid").datagrid('options').queryParams={};
			$(jq).combogrid("options").queryParams={};
		}
		
		$.fn[_name].methods = {
			options : function(jq) {
				return $.data(jq[0], _name).options;
			},
			setValue : function(jq, value) {
				_setValue(jq[0], value);
			},
			getValue : function(jq) {
				return _getValue(jq[0]);
			},
			getData : function(jq) {
				return _getData(jq[0]);
			},
			clear : function(jq, param) {
				$.each(jq, function(index, item) {
					_clear(item);
				});
			},
			disable : function(jq) {
				$.each(jq, function(index, item) {
					_disable(item);
				});
	
			},
			enable : function(jq) {
				$.each(jq, function(index, item) {
					_enable(item);
				});
			},
			clearQueryParams : function(jq) {
				$.each(jq, function(index, item) {
					_clearQueryParams(item);
				});
			}
		};

	});
	
	
	/**
     * fas实体通用下拉网格 combogridmdm  
     * 2017-09-19 create by dai.xw
     * 参数：
     * beanName——实体类名称   
     * urlBeanName——实体类映射名称 比如storeGroup——store/group
     * cnName——中文名称字段（一般都是name字段，个别有特殊情况）
     * onHidePanelFn——自定义传参的函数（在隐藏下拉网格触发的时候）
     * onShowPanelFn——自定义传参的函数（在展开下拉网格触发的时候）
     * required——校验必填
     */
    $(function($) {
		var _name = 'combogridfas';
		$.fn.form.explugins.push(_name);
		$.fn.plugin(_name, render);
		function _disable(jq) {
			$(jq).attr("readonly", true).combogrid("disable");
		}
	
		function _enable(jq) {
			$(jq).attr("readonly", true).combogrid("enable");
		}
	
		function render(jq) {
			var options = $.data(jq, _name).options;
			if(!options.cnName){
				options.cnName = 'name';//默认中文名称字段name
			}
			var url = options.url ? options.url : config.rootUrl + "/" +options.urlBeanName+'/list';
			var columns = [{
				field : `${options.beanName}No`,
				title : `编码`,
				width : 100,
				sortable : true
			}, {
				field : `${options.cnName}`,
				title : `名称`,
				width : 150,
				align : 'left',
				sortable : true
			}];
			var dataOp = $.extend({}, options, {
				loadMsg:'数据加载中，请稍等......',
				delay : 700,// 延迟
				pageSize : 30,// 在设置分页属性的时候初始化页面大小。
				pageList : [ 30, 100, 200, 300, 400, 500 ],// 在设置分页属性的时候初始化页面大小选择列表。
				idField : `${options.beanName}No`,
				textField : `${options.cnName}`,
				valueFeild:`${options.beanName}No`,
				url:url,
				mode:'remote',
				panelWidth:350,
				columns : [ columns ],
				filter : function(q, row) {
					var opts = $(this).combogrid('options');
					return row[opts.textField].indexOf(q) == 0;
				},
				fitColumns : true,
				onHidePanel : function() {// 选中下拉项后关闭面板，填充数据
					var options = $(jq).combogrid('options');
					var g = $(jq).combogrid('grid'); // 获取数据表格对象
					var r = g.datagrid('getSelected'); // 获取选择的行
					if (r) {
						options.selectedGridData = $.extend(true, {}, r);// 设置data
						_setValue(jq, options.selectedGridData);
						//自定义传参的函数（在隐藏下拉网格触发的时候）
						if(options.onHidePanelFn) {
							options.onHidePanelFn(options.selectedGridData);
						}
					} else {
						if (!(options.selectedGridData && options.selectedGridData[options.textField] == $(jq).parent().find("span").children("input").val())) {
							_clear(jq);
						}
					}
				},
				onShowPanel : function() {
					_getChangedQueryParams(jq,url);// 获取动态的查询条件
					//自定义传参的函数（在展开下拉网格触发的时候）
					if(options.onShowPanelFn) {
						options.onShowPanelFn(jq);
					}
					$(jq).combogrid("grid").datagrid('reload');// 若是根据前面已选的条件来查询数据需要重载一遍datagrid
				},
				hasDownArrow:options.hasDownArrow,
				pagination : true
			});
			// 下拉网格
			$(jq).combogrid(dataOp);
			if (dataOp.required) {
				$(jq).combo({
					required : true,
					tipPosition:'none'
				});
			}
		}
		
		// 获取动态的查询条件
		function _getChangedQueryParams(jq){
			$(jq).combogrid("grid").datagrid('options').queryParams={};
			$(jq).combogrid("options").queryParams={};// 清除combogrid的QueryParams
			var options = $.data(jq, _name).options;
		}
		
		function _setValue(jq,value){
	    	var options = $.data(jq, _name).options;
	    	options.selectedGridData = value;
			if (typeof value == 'string') {
				if (value == _getValue(jq))
					return;
				if(value==""){
					return;
				}
				var url = options.url ? options.url : config.rootUrl + "/" +options.urlBeanName+'/list';
				$.ajax({
					url : url,
					cache : true,
					async : false
				}).then(function(data) {
					_setValue(jq, data[0]);
				});
			} else {
				$(jq).combogrid('setValue', value);
			}
	    }
		
		function _getValue(jq) {
			$(jq).combogrid('getValue');
		}
		
		function _getData(jq) {
			var op = $.data(jq, _name).options;
			return op.selectedGridData;
		}
	
		function _clear(jq) {
			$(jq).combogrid('clear');// 没选择数据则清空编辑框
			var op = $.data(jq, _name).options;
			if (op.selectedGridData != undefined) {
				op.selectedGridData[op.valueFeild] = null;// 清空查询精灵的value
				op.selectedGridData[op.textFeild] = null;// 清空查询精灵的name
				_setValue(jq, op);
			}else{
				_setValue(jq, '');
			}
		}
		
		// 清空queryParams
		function _clearQueryParams(jq){
			$(jq).combogrid("grid").datagrid('options').queryParams={};
			$(jq).combogrid("options").queryParams={};
		}
		
		$.fn[_name].methods = {
			options : function(jq) {
				return $.data(jq[0], _name).options;
			},
			setValue : function(jq, value) {
				_setValue('setValue', value);
			},
			getValue : function(jq) {
				return _getValue(jq[0]);
			},
			getData : function(jq) {
				return _getData(jq[0]);
			},
			clear : function(jq, param) {
				$.each(jq, function(index, item) {
					_clear(item);
				});
			},
			disable : function(jq) {
				$.each(jq, function(index, item) {
					_disable(item);
				});
	
			},
			enable : function(jq) {
				$.each(jq, function(index, item) {
					_enable(item);
				});
			},
			clearQueryParams : function(jq) {
				$.each(jq, function(index, item) {
					_clearQueryParams(item);
				});
			}
		};
	});
	
	
	
	
	
	
	
	/**
	 * 扩展的编辑器对象
	 */
	var selectEditor = {
			destroy : function(target) {
				$(target).combogrid('destroy');
			},
			getValue : function(target) {
				return $(target).combogrid('getValue');
			},
			setValue : function(target, value) {
				$.data($(target)[0], 'oldValue', value);
				$(target).combogrid('setValue', value);
			},
			resize : function(target, width) {
				$(target).combogrid('resize', width);
			},
			focus : function(target) {
				$(target).parent().find('.combo-text').focus().select();
			}
		};
	
	
	/**
	 * 扩展的编辑器对象
	 */
	$.fn.datagrid.defaults.editors.counterCostEditor = $.extend({
		init : function(container, options) {
			var input = $('<input type="text" class="datagrid-editable-input">');
			input.appendTo(container);
			var url = `${config.rootUrl}/counter/cost/list?1=1"&balanceBillNoIsNull="+true&status=2`;
			if(options.getCounterNo){
				url+="&counterNo="+$("#counterNo").combobox('getValue');
			}
			if(options.getSettleMonth){
				url+="&settleMonth="+$("#settleMonth").val();
			}
			if(options.getSettleStartDate){
				url+="&settleStartDate="+$("#settleStartDate").val();
			}
			if(options.getSettleEndDate){
				url+="&settleEndDate="+$("#settleEndDate").val();
			}
			if(options.getSupplierNo){
				url+="&supplierNo="+$("#supplierNo").combobox('getValue');
			}
			if(options.getCompanyNo){
				url+="&companyNo="+$("#companyNo").combobox('getValue');
			}
			
			var columns =
			[ {
				"field": "settleMonth",
				"title": "结算月",
				"width": 100
			}, {
				"field": "---",
				"title": "结算期",
				"width": 200,
				"formatter": function (value, row, index) {
                    return row.settleStartDate+"--"+row.settleEndDate;
               },
				"editor" : "readonlytext"
			},{
				"field": "companyNo",
				"title": "公司编码",
				"width": 80
			}, {
				"field": "supplierNo",
				"title": "供应商编码",
				"width": 90
			}, {
				"field": "shopNo",
				"title": "门店编码",
				"width": 80
			}, {
				"field": "counterNo",
				"title": "专柜编码",
				"width": 80
			}, {
				"field": "costNo",
				"title": "扣项编码",
				"width": 80
			}, {
				"field": "costName",
				"title": "扣项名称",
				"$formatter": {valueField: 'costNo', textField: 'name', type: "deduction"},
				"width": 80
			}, {
				"field": "status",
				"title": "状态",
				"width": 80,
				"formatter":(value)=>status.first(c=>c.id == value).name
			}, {
				"field": "ableAmount",
				"title": "应结价款",
				"width": 80
			}, {
				"field": "ableSum",
				"title": "应结总额",
				"width": 80
			}, {
				"field": "taxAmount",
				"title": "税额",
				"width": 80
			}, {
				"field": "taxFlag",
				"title": "是否含税",
				"formatter":(value)=>$.fas.datas.taxFlag.first(c=>c.id == value).name,
				"width": 80
			}, {
				"field": "taxRate",
				"title": "税率(%)",
				"width": 80
			}, {
				"field": "billDebit",
				"title": "票扣",
				"width": 80,
				"formatter":(value)=>$.fas.datas.billDebit.first(c=>c.id == value).name
			}, {
				"field": "accountDebit",
				"title": "账扣",
				"width": 80,
				"formatter":(value)=>$.fas.datas.accountDebit.first(c=>c.id == value).name
			}, {
				"field": "remark",
				"type": "textbox",
				"title": "备注",
				"width": 80
			}, {
				"field": "id",
				"type": "textbox",
				"title": "id",
				"width": 80,
				"hidden":true
			}];

			function selectedValue(r) {
				var oldValue = $.data(input[0], 'oldValue');
				var rowIndex = $page.currentView.rowIndex;
				if (r && r.counterNo != oldValue) {
					var data = $.extend({}, r);// 设置data
					var myOption = $.extend({}, options);
					myOption.target = input;
					//调用options中的回调函数
					if ($.isFunction(options.clickFn)){
						options.clickFn.call(this,data,input);
					}
					//选中下拉网格赋值
					for (var p in data) {
			            var editor = $page.currentView.currentGrid.datagrid('getEditor', {
			                'index': rowIndex,
			                'field': p
			            });
			            if (!editor || !editor.type) {
			                continue;
			            }
			            var target = editor.target;
			            var ed = $.fn.datagrid.defaults.editors[editor.type];
			            if (ed) {
			                ed.setValue(target, data[p]);
			            }
			        }
				}
				// 下拉网格没选择数据（判断文本搜索框中的数据没有被修改则不清空）则清空编辑框
				if (!r && input.parent().find("span").children("input").val() != oldValue) {
					input.combogrid('clear');//没选择数据则清空编辑框
				}
			}

			var dataOp = $.extend({}, options, {
				loadMsg:'数据加载中，请稍等......',
				delay : 700,// 延迟
				pageSize : 30,//在设置分页属性的时候初始化页面大小。
				pageList : [ 30, 100, 200, 300, 400, 500 ],//在设置分页属性的时候 初始化页面大小选择列表。
				mode : 'remote',
				panelWidth : 1200,
				panelHeight : 200,
				width:90, 
				idField : 'counterNo',
				textField : options.textField!=null?options.textField:'counterName',
				url : url,
				columns : [ columns ],
				filter : function(q, row) {
					var opts = $(this).combogrid('options');
					return row[opts.idField].indexOf(q) == 0;
				},
				fitColumns : true,
				onHidePanel : function() {// 选中下拉项后关闭面板，填充选中网格行数据
					var g = input.combogrid('grid'); // 获取数据表格对象
					var r = g.datagrid('getSelected');
					selectedValue(r);
				},
				onShowPanel : function() {
					input.combogrid("grid").datagrid('reload');// 若是改变查询条件后查询数据需要重载一遍datagrid
				},
				pagination : true,
				hasDownArrow:false//定义是否显示向下箭头按钮。
			});
			// 下拉网格
			input.combogrid(dataOp);
			if (dataOp.isRequired) {
				input.combo({
					required : true,
					tipPosition:'none'
				});
			}
			
			return input;
		}
	}, selectEditor);
	
	/**
	 * 扩展的编辑器对象
	 */
	$.fn.datagrid.defaults.editors.mallBalanceDateDtlEditor = $.extend({
		init : function(container, options) {
			var input = $('<input type="text" class="datagrid-editable-input">');
			input.appendTo(container);
			var url = `${config.rootUrl}/mall/balance/date/dtl/list?1=1`;
			if(options.getMallNo){
				url+="&mallNo="+$("#mallNo").combobox('getValue');
			}
			if(options.getShopNo){
				url+="&shopNo="+$("#shopNo").combobox('getValue');
			}
			if(options.getBunkGroupNo){
				url+="&bunkGroupNo="+$("#bunkGroupNo").combobox('getValue');
			}
			
			var columns =
			[ {
				"field": "settleMonth",
				"title": "结算月",
				"width": 100
			}, {
				"field": "settleStartDate",
				"title": "结算开始时间",
				"width": 100,
				"editor" : "readonlytext"
			}, {
				"field": "settleEndDate",
				"title": "结算结束时间",
				"width": 100,
				"editor" : "readonlytext"
			}, {
				"field": "status",
				"title": "状态",
				"width": 80,
				"formatter":(value)=>status.first(c=>c.id == value).name
			}];

			function selectedValue(r) {
				var oldValue = $.data(input[0], 'oldValue');
				var rowIndex = $page.currentView.rowIndex;
				if (r && r.settleMonth != oldValue) {
					var data = $.extend({}, r);// 设置data
					var myOption = $.extend({}, options);
					myOption.target = input;
					//调用options中的回调函数
//					if ($.isFunction(options.clickFn)){
//						options.clickFn.call(this,data,input);
//					}
					//选中下拉网格赋值
					for (var p in data) {
			            var editor = $page.currentView.currentGrid.datagrid('getEditor', {
			                'index': rowIndex,
			                'field': p
			            });
			            if (!editor || !editor.type) {
			                continue;
			            }
			            var target = editor.target;
			            var ed = $.fn.datagrid.defaults.editors[editor.type];
			            if (ed) {
			                ed.setValue(target, data[p]);
			            }
			        }
				}
				// 下拉网格没选择数据（判断文本搜索框中的数据没有被修改则不清空）则清空编辑框
				if (!r && input.parent().find("span").children("input").val() != oldValue) {
					input.combogrid('clear');//没选择数据则清空编辑框
				}
			}

			var dataOp = $.extend({}, options, {
				loadMsg:'数据加载中，请稍等......',
				delay : 700,// 延迟
				pageSize : 30,//在设置分页属性的时候初始化页面大小。
				pageList : [ 30, 100, 200, 300, 400, 500 ],//在设置分页属性的时候 初始化页面大小选择列表。
				mode : 'remote',
				panelWidth : 600,
				panelHeight : 200,
				width:90, 
				idField : 'settleMonth',
//				textField : options.textField!=null?options.textField:'counterName',
				url : url,
				columns : [ columns ],
				filter : function(q, row) {
					var opts = $(this).combogrid('options');
					return row[opts.idField].indexOf(q) == 0;
				},
				fitColumns : true,
				onHidePanel : function() {// 选中下拉项后关闭面板，填充选中网格行数据
					var g = input.combogrid('grid'); // 获取数据表格对象
					var r = g.datagrid('getSelected');
					selectedValue(r);
				},
				onShowPanel : function() {
					input.combogrid("grid").datagrid('reload');// 若是改变查询条件后查询数据需要重载一遍datagrid
				},
				pagination : true,
				hasDownArrow:false//定义是否显示向下箭头按钮。
			});
			// 下拉网格
			input.combogrid(dataOp);
			if (dataOp.isRequired) {
				input.combo({
					required : true,
					tipPosition:'none'
				});
			}
			
			return input;
		}
	}, selectEditor);
	
	
	/**
	 * 扩展的编辑器对象
	 */
	$.fn.datagrid.defaults.editors.mallCostEditor = $.extend({
		init : function(container, options) {
			var input = $('<input type="text" class="datagrid-editable-input">');
			input.appendTo(container);
			var url = `${config.rootUrl}/mall/cost/list?1=1"&balanceBillNoIsNull="+true&status=2`;
			if(options.getShopNo){
				url+="&shopNo="+$("#shopNo").combobox('getValue');
			}
			if(options.getSettleMonth){
				url+="&settleMonth="+$("#settleMonth").val();
			}
			if(options.getSettleStartDate){
				url+="&settleStartDate="+$("#settleStartDate").val();
			}
			if(options.getSettleEndDate){
				url+="&settleEndDate="+$("#settleEndDate").val();
			}
			if(options.getMallNo){
				url+="&mallNo="+$("#mallNo").combobox('getValue');
			}
			
			var columns =
			[ {
				"field": "settleMonth",
				"title": "结算月",
				"width": 100
			}, {
				"field": "---",
				"title": "结算期",
				"width": 200,
				"formatter": function (value, row, index) {
                    return row.settleStartDate+"--"+row.settleEndDate;
               },
				"editor" : "readonlytext"
			},{
				"field": "companyNo",
				"title": "公司编码",
				"width": 80
			}, {
				"field": "shopNo",
				"title": "门店编码",
				"width": 80
			}, {
				"field": "mallNo",
				"title": "物业公司编码",
				"width": 80
			}, {
				"field": "costNo",
				"title": "扣项编码",
				"width": 80
			}, {
				"field": "costName",
				"title": "扣项名称",
				"$formatter": {valueField: 'costNo', textField: 'name', type: "deduction"},
				"width": 80
			}, {
				"field": "status",
				"title": "状态",
				"width": 80,
				"formatter":(value)=>status.first(c=>c.id == value).name
			}, {
				"field": "ableAmount",
				"title": "应结价款",
				"width": 80
			}, {
				"field": "ableSum",
				"title": "应结总额",
				"width": 80
			}, {
				"field": "taxAmount",
				"title": "税额",
				"width": 80
			}, {
				"field": "taxFlag",
				"title": "是否含税",
				"formatter":(value)=>$.fas.datas.taxFlag.first(c=>c.id == value).name,
				"width": 80
			}, {
				"field": "taxRate",
				"title": "税率(%)",
				"width": 80
			}, {
				"field": "billDebit",
				"title": "票扣",
				"width": 80,
				"formatter":(value)=>$.fas.datas.billDebit.first(c=>c.id == value).name
			}, {
				"field": "accountDebit",
				"title": "账扣",
				"width": 80,
				"formatter":(value)=>$.fas.datas.accountDebit.first(c=>c.id == value).name
			}, {
				"field": "remark",
				"type": "textbox",
				"title": "备注",
				"width": 80
			}, {
				"field": "id",
				"type": "textbox",
				"title": "id",
				"width": 80,
				"hidden":true
			}];

			function selectedValue(r) {
				var oldValue = $.data(input[0], 'oldValue');
				var rowIndex = $page.currentView.rowIndex;
				if (r && r.id != oldValue) {
					var data = $.extend({}, r);// 设置data
					var myOption = $.extend({}, options);
					myOption.target = input;
					//调用options中的回调函数
					if ($.isFunction(options.clickFn)){
						options.clickFn.call(this,data,input);
					}
					//选中下拉网格赋值
					for (var p in data) {
			            var editor = $page.currentView.currentGrid.datagrid('getEditor', {
			                'index': rowIndex,
			                'field': p
			            });
			            if (!editor || !editor.type) {
			                continue;
			            }
			            var target = editor.target;
			            var ed = $.fn.datagrid.defaults.editors[editor.type];
			            if (ed) {
			                ed.setValue(target, data[p]);
			            }
			        }
				}
				// 下拉网格没选择数据（判断文本搜索框中的数据没有被修改则不清空）则清空编辑框
				if (!r && input.parent().find("span").children("input").val() != oldValue) {
					input.combogrid('clear');//没选择数据则清空编辑框
				}
			}

			var dataOp = $.extend({}, options, {
				loadMsg:'数据加载中，请稍等......',
				delay : 700,// 延迟
				pageSize : 30,//在设置分页属性的时候初始化页面大小。
				pageList : [ 30, 100, 200, 300, 400, 500 ],//在设置分页属性的时候 初始化页面大小选择列表。
				mode : 'remote',
				panelWidth : 1200,
				panelHeight : 200,
				width:90, 
				idField : 'shopNo',
				textField : options.textField!=null?options.textField:'shopName',
				url : url,
				columns : [ columns ],
				filter : function(q, row) {
					var opts = $(this).combogrid('options');
					return row[opts.idField].indexOf(q) == 0;
				},
				fitColumns : true,
				onHidePanel : function() {// 选中下拉项后关闭面板，填充选中网格行数据
					var g = input.combogrid('grid'); // 获取数据表格对象
					var r = g.datagrid('getSelected');
					selectedValue(r);
				},
				onShowPanel : function() {
					input.combogrid("grid").datagrid('reload');// 若是改变查询条件后查询数据需要重载一遍datagrid
				},
				pagination : true,
				hasDownArrow:false//定义是否显示向下箭头按钮。
			});
			// 下拉网格
			input.combogrid(dataOp);
			if (dataOp.isRequired) {
				input.combo({
					required : true,
					tipPosition:'none'
				});
			}
			
			return input;
		}
	}, selectEditor);
	
	$(function(){
		class Fold{
			constructor(opts){
				this.foldButton(opts);
			}
			foldButton(opts){
				var self = this;
				var _box=$('<div>').attr('id',"searchDiv").addClass('simple-search-box');
			    var _a=$('<a>').attr({'id':"searchArr",'href':'javascript:;'}).addClass('search-up-arr');
			    var _tbM=$('<div>').addClass('toolbar_menu');
			    var opts=opts || {collapsible:true,items:[]};
			    var target=opts.target || $('#subLayout');
			    var appendTo=opts.appendTo || $('#toolbar');
			    var items=opts.items;
			    var _width=opts.width || 250;
			    var _pos=opts.pos || 'right';
			    appendTo.css({'position':'relative'});
			    if(items&&items.length>0){
			        for(var i=0;i<items.length;i++){
			            var _d=$('<div>');
			            _d.attr('name',items[i].name).html(items[i].text);
			            _tbM.append(_d);
			        }
			        _box.append('<input class="tbSearch"/>');
			    }

			    if(opts.collapsible!=false){
			        _a.click(function(){
			        	self.toggleArrow(this,opts);
			        });
			        _box.append(_a);
			    }else{
			        _box.css({right:5});
			        if(_pos=="left"){
			            _box.css({left:5});
			        }
			    }

			    _box.append(_tbM);
			    if(appendTo){
			        $(appendTo).append(_box);
			    }

			    if(items&&items.length>0){
			        $('.tbSearch',appendTo).searchbox({
			            width:_width,
			            searcher:function(value,name){
			                if(typeof opts.callback=="function"){
			                    opts.callback(value,name);
			                }
			            },
			            menu:$('.toolbar_menu',appendTo),
			            prompt:'请输入关键字'
			        });
			    }
			    if($('.search-div').closest('.layout-panel-north').css('display')=="none"){
			        setTimeout(function() {
			            $('#searchArr',appendTo).addClass('search-down-arr').removeClass('search-up-arr');
			        },200);
			    }
			}
			toggleArrow(obj,opts){
				if ($(obj).hasClass("search-down-arr")) {
			        $(opts.target).layout('show', opts.foldDirection);
			        $(obj).attr("class", "search-up-arr");
			    } else {
			        $(opts.target).layout('hidden', opts.foldDirection);
			        $(obj).attr("class", "search-down-arr");
			    }
				
				if(opts.foldDirection == 'center'){
					this.countHeight(obj);
				}
			}
		}
		
		window.Fold = Fold;
	});
});