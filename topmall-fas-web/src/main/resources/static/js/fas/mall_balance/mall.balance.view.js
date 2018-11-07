"use strict";
define(function(require, exports, module) {
	let UI = require('core/ui');
	let config = require('../config');
	let mallBalanceService = require('./mall.balance.service');
	class BillDetail extends UI.BillView {
		constructor() {
			super("main");
			this.title = "单据明细";
			this._service = new mallBalanceService();
		}
		
		get service(){
			return this._service;
		}
		
		getToolbars() {
			return {
				id: 'toolbar2',
				data: [{
						id: "btn-searchBill",
						iconCls: 'icon icon-search',
						text: '查单',
						value: 1
					},
					{
						id: "btn-newBill",
						iconCls: 'icon icon-plus',
						text: '新单',
						value: 2
					},
					{
						id: "btn-deleteBill",
						iconCls: 'icon  icon-bin',
						text: '删单',
						value: 4
					},
					{
						id: "btn-verify",
						iconCls: 'icon  icon-profile',
						text: '审核',
						value: 10
					},{
						id: "btn-unVerify",
						iconCls: 'icon  icon-file-empty',
						text: '反审核',
						value: 11
					},
					{
						id: "btn-save",
						iconCls: 'icon  icon-floppy-disk',
						text: '保存',
						value: 3
					}
				]
			}
		}
		
		getFootToolbars(){
			var items = ["新增", "删除"] ;
            var item2 = [{id: "btn-saveDetail", iconCls: 'icon  icon-floppy-disk', text: '保存', value: 1, order: "7"}];
            items = $.merge(items, item2);
            
			return [{
                id: 'toolbar1',
                data:[]
            },{
                id: 'toolbar2',
                data:items
            },{
                id: 'toolbar3',
                data:items
            },{
                id: 'toolbar4',
                data:item2
            }]
		}
		getSearchControls() {
			return {
				list: 4,
				controls: [{
					"label": "单据编码",
					"type": "textbox",
					"name": "billNo",
					"options": { "width": 150,
						"disabled":true}
				}, {
					"label": "状态",
					"type": "combocommon",
					"name": "status",
					"options": { 
						"width": 150,
						"type":"status",
						"disabled":true
					}
				},{
					"label": "卖场",
					"type": "combogridmdm",
					"name": "shopNo",
					"options": {
						"width": 150,
						"required": true,
						"beanName":"shop",
						"valueFeild":"shopNo",
						onHidePanelFn: function(rec) {
							if(rec){
								$('#mallNo','#main_top_panel').combogrid('setValue','');
							}
						}
					}
				}, {
					"label": "物业公司",
					"type": "combogridmdm",
				    "name": "mallNo",
				    "options": {
				        "width": 150,
				        "beanName":"propertyCompany",
				        "beanNo":"companyNo",
				        "valueFeild":"companyNo",
				        "required":true,
				        onShowPanelFn: function(jq) {
							var shopNo = $('#shopNo','#main_top_panel').combogrid('getValue');
							$('#mallNo','#main_top_panel').combogrid("grid").datagrid('options').queryParams.shopNo=shopNo;
							$('#mallNo','#main_top_panel').combogrid("options").queryParams.shopNo=shopNo;
						},
						onHidePanelFn: function(rec) {
							if(rec){
								$('#bunkGroupNo','#main_top_panel').combogrid('setValue','');
							}
						}
				    }
				},{
					"label":"铺位组",
					"type":"combogridmdm",
					"name":"bunkGroupNo",
					"options":{
						"width":150,
						"required":true,
						"beanName":"bunkGroup",
						"beanNo":"bunkGroupNo",
						"cnName":"bunkGroupNo",
						"valueFeild":"bunkGroupNo",
						onShowPanelFn: function(jq) {
							var shopNo = $('#shopNo','#main_top_panel').combogrid('getValue');
							var mallNo = $('#mallNo','#main_top_panel').combogrid('getValue');
							$('#bunkGroupNo','#main_top_panel').combogrid("grid").datagrid('options').queryParams.shopNo=shopNo;
							$('#bunkGroupNo','#main_top_panel').combogrid("options").queryParams.shopNo=shopNo;
							$('#bunkGroupNo','#main_top_panel').combogrid("grid").datagrid('options').queryParams.companyNo=mallNo;
							$('#bunkGroupNo','#main_top_panel').combogrid("options").queryParams.companyNo=mallNo;
						}
					}
				}, {
					"label": "公司",
					"type": "combogridmdm",
					"name": "companyNo",
					"options": { "width": 150,
						"beanName":"company",
						"valueFeild":"companyNo",
						"disabled":true
						}
				}, {
					"label": "合作性质",
					"type": "combocommon",
					"name": "businessType",
					"options": { 
						"width": 150,
						"type":"businessType",
						"disabled":true
					}
				}, {
					"label": "合同面积",
					"type": "textbox",
					"name": "areaCompact",
					"options": { 
						"width": 150,
						"disabled":true
					}
				}, {
					"label": "开店日期",
					"type": "datebox",
					"name": "openDate",
					"options": { 
						"width": 150,
						"disabled":true
					}
				}, 
				{
					"label": "结算月",
					"type": "datebox",
					"name": "settleMonth",
					"options": {
						"width": 150,
						"required": true,
						"dateFmt":"yyyyMM"
						}
				}, {
					"label": "开始日期",
					"type": "settlementMallDateBox",
					"name": "settleStartDate",
					"options": { "width": 150,
						"required": true
						}
				}, {
					"label": "结束日期",
					"type": "datebox",
					"name": "settleEndDate",
					"options": { "width": 150,
						"required": true,
						"disabled":true
						}
				}, {
					"label": "预开票总额",
					"type": "numberbox",
					"name": "preBillingAmount",
					"options": { "width": 150,
						"precision":2
						}
				}, {
					"label": "未开票总额",
					"type": "numberbox",
					"name": "notBillingAmount",
					"options": { "width": 150,
						"precision":2
						}
				}, {
					"label": "预结款总额",
					"type": "numberbox",
					"name": "preAbleAmount",
					"options": { "width": 150,
						"precision":2
						}
				}, {
					"label": "商场开票金额",
					"type": "numberbox",
					"name": "mallBillingAmount",
					"options": { "width": 150,
						"precision":2
						}
				}, {
					"label": "物业计入保底金额",
					"type": "numberbox",
					"name": "mallMinimumAmount",
					"options": { "width": 150,
						"precision":2
						}
				}, {
					"label": "备注",
					"type": "textbox",
					"name": "remark",
					"colspan":"2",
					"options": { "width": 565 }
				}]
			}
		}
		
		//专柜费用选择
		onSelectedItem(data, target) {
			
		}
		
		getGridOptions() {
			var self = this;
			var dataurl = portalUrl+'/mdm/data/api';
			var options =[{
				id: 'grid1',
				url: config.rootUrl + '/bill/mall/balance/summary',
				title: "结算汇总",
				height: "670",
				loadMsg: '请稍等,正在加载...',
				iconCls: 'icon-ok',
				pageSize: "150",
				pageList: [20, 50, 100, 200],
				checkOnSelect: false,
				pagination: false,
				fitColumns: false,
				singleSelect: false,
				rownumbers: true,
				enableHeaderContextMenu: true,
				enableHeaderClickMenu: true,
				emptyMsg: "暂无数据",
				columns: [
					[{
						"field": "projectName",
						"type": "textbox",
						"title": "项目",
						"width": 100,
						"hidden": false
					},{
						"field": "detail",
						"type": "textbox",
						"title": "明细",
						"width": 140,
						"hidden": false,
						"formatter":function (value, row, index) {
							if(value=="应收物业方款项"){
								return "<a href='javascript:showExplain(\"同意抵扣：应收物业方=物业方收银-账扣费用(销售费用和物业费用)-预付(物业预付款)+现金(销售费用和物业费用)</br>不同意抵扣：应收物业方=物业方收银-费用为账扣费用(销售费用和物业费用)\");' class='l-btn-text icon-xq l-btn-icon-left'></a>"+value;
							}else if(value=="应付物业方款项"){
								return "<a href='javascript:showExplain(\"同意抵扣：应收物业方=0</br>不同意抵扣：应付物业方=0-预付(物业预付款)+费用为付现(销售费用和物业费用)\");' class='l-btn-text icon-xq l-btn-icon-left'></a>"+value;
							}else{
								return value;
							}
						}
					},{
						"field": "curAmount",
						"type": "textbox",
						"title": "本期金额",
						"width": 120,
						"hidden": false
					},{
						"field": "mallAmount",
						"type": "textbox",
						"title": "物业报数",
						"width": 80,
						"hidden": false
					},{
						"field": "diffAmount",
						"type": "textbox",
						"title": "差异金额",
						"width": 80,
						"hidden": false
					},{
						"field": "diffReason",
						"type": "textbox",
						"title": "差异原因",
						"width": 80,
						"hidden": false
					}]
				],
				onLoadSuccess: function (data) {
			        var start = 0;
			        var end = 0;
			        if (data.total > 0) {
			            var temp = data.rows[0].projectName;
			            for (var i = 1; i < data.rows.length; i++) {
			                if (temp == data.rows[i].projectName) {
			                    end++;
			                } else {
			                    if (end > start) {
			                        $(this).datagrid('mergeCells', {
			                            index: start,
			                            rowspan: end - start + 1,
			                            field: 'projectName'
			                        })
			                    }
			                    temp = data.rows[i].projectName;
			                    start = i;
			                    end = i;
			                }
			            }
			            /*这里是为了判断重复内容出现在最后的情况，如ABCC*/
			            if (end > start) {
			                $(this).datagrid('mergeCells', {
			                    index: start,
			                    rowspan: end - start + 1,
			                    field: 'projectName'
			                })
			            }
			        }
			    }
			},{
				id: 'grid2',
				url: config.rootUrl + '/mall/sale/cost/list',
				title: "销售明细",
				height: "670",
				loadMsg: '请稍等,正在加载...',
				iconCls: 'icon-ok',
				pageSize: "150",
				pageList: [20, 50, 100, 200],
				checkOnSelect: false,
				pagination: true,
				fitColumns: false,
				singleSelect: false,
				rownumbers: true,
				enableHeaderContextMenu: true,
				enableHeaderClickMenu: true,
				emptyMsg: "暂无数据",
				showFooter:true,
				columns: [
					[{
						"field": "id",
						"type": "textbox",
						"title": "id",
						"width": 100,
						"hidden": true,
						"editor" : "readonlytext"
					},{
						"field": "settleMonth",
						"type": "textbox",
						"title": "结算期",
						"width": 100,
						"hidden": false,
						"editor" : "readonlytext"
					},{
						"field": "priceType",
						"type": "textbox",
						"title": "价码类型",
						"width": 80,
						"hidden": false,
						"editor" : "readonlytext"
					},{
						"field": "settleSum",
						"type": "textbox",
						"title": "系统收入(A)",
						"width": 80,
						"hidden": false,
						"editor" : "readonlytext"
					},{
						"field": "pointsAmount",
						"type": "textbox",
						"title": "积分抵现金额(B)",
						"width": 100,
						"hidden": false,
						"editor" : "readonlytext"
					},{
						"field": "deductPointsAmount",
						"type": "textbox",
						"title": "A-B",
						"width": 80,
						"hidden": false,
						"editor" : "readonlytext"
					},{
						"field": "mallAmount",
						"type": "textbox",
						"title": "物业报数",
						"width": 120,
						"hidden": false,
						"editor" :{type:"numberbox",
							"options":{
								"precision":2
							}
						}
					},{
						"field": "diffAmount",
						"type": "textbox",
						"title": "差异金额",
						"width": 80,
						"hidden": false,
						"editor" : {type:"readonlytext",
							"options":{
								"precision":2
							}
						}
					},{
						"field": "rateValue",
						"type": "textbox",
						"title": "扣率%",
						"width": 80,
						"hidden": false,
						"editor" : "readonlytext"
					}, {
						"field": "profitAmount",
						"type": "textbox",
						"title": "销售扣费",
						"width": 80,
						"hidden": false,
						"editor" : "readonlytext"
					},{
						"field": "mallProfitAmount",
						"type": "textbox",
						"title": "物业扣费",
						"width": 120,
						"hidden": false,
						"editor" :{type:"numberbox",
							"options":{
								"precision":2
							}
						}
					},{
						"field": "diffProfitAmount",
						"type": "textbox",
						"title": "扣费差异",
						"width": 80,
						"hidden": false,
						"editor" : "readonlytext"
					}, {
						"field": "preDiffAmount",
						"type": "textbox",
						"title": "上期差异余额",
						"width": 80,
						"hidden": false,
						"editor" : "readonlytext"
					}, {
						"field": "curDiffAmount",
						"type": "textbox",
						"title": "本期差异余额",
						"width": 80,
						"hidden": false,
						"editor" : "readonlytext"
					}, {
						"field": "diffReason",
						"type": "textbox",
						"title": "差异原因",
						"width": 80,
						"hidden": false,
						"editor" : "text"
					}, {
						"field": "adjustAmount",
						"type": "textbox",
						"title": "调整金额",
						"width": 80,
						"hidden": false,
						"editor" :{type:"numberbox",
							"options":{
								"precision":2
							}
						}
					}, {
						"field": "backAmount",
						"type": "textbox",
						"title": "回款金额",
						"width": 80,
						"hidden": false,
						"editor" :{type:"numberbox",
							"options":{
								"precision":2
							}
						}
					}, {
						"field": "settleStatus",
						"type": "textbox",
						"title": "状态 ",
						"width": 80,
						"hidden": false,
						"editor" : {
							type : 'combobox',
							options:{
							    required:true,
							    valueField:'id',
							    textField:'name',
							    data:$.fas.datas.settleStatus
							}
						}
					,
						"formatter":(value)=>isNotBlank(value)?$.fas.datas.settleStatus.first(c=>c.id == value).name:null,
					}, {
						"field": "accountDebit",
						"type": "textbox",
						"title": "账扣",
						"width": 80,
						"hidden": false,
						"formatter":(value)=>isNotBlank(value)?$.fas.datas.accountDebit.first(c=>c.id == value).name:null
					}, {
						"field": "billDebit",
						"type": "textbox",
						"title": "票扣",
						"width": 80,
						"hidden": false,
						"formatter":(value)=>isNotBlank(value)?$.fas.datas.billDebit.first(c=>c.id == value).name:null
					}]
				]
			},{
				id: 'grid3',
				url: config.rootUrl + '/mall/cost/list',
				title: "费用明细",
				height: "670",
				loadMsg: '请稍等,正在加载...',
				iconCls: 'icon-ok',
				pageSize: "150",
				pageList: [20, 50, 100, 200],
				checkOnSelect: false,
				pagination: true,
				fitColumns: false,
				singleSelect: false,
				rownumbers: true,
				enableHeaderContextMenu: true,
				enableHeaderClickMenu: true,
				emptyMsg: "暂无数据",
				showFooter:true,
				columns: [
				    [{
						"field": "id",
						"type": "textbox",
						"title": "id",
						"width": 100,
						"hidden": true,
						"editor" : "readonlytext"
					},{
						"field": "settleMonth",
						"type": "textbox",
						"title": "结算期",
						"width": 100,
						"hidden": false,
						"editor" :{
							type : 'mallCostEditor',
							options : {
								getShopNo:true,
								getSettleMonth:true,
								getSettleStartDate:true,
								getSettleEndDate:true,
								getMallNo:true,
								clickFn : function(data, target){
									self.onSelectedItem(data, target);
								},
								isRequired : true
							}
						}
					},{
						"field": "costNo",
						"type": "textbox",
						"title": "扣项编码",
						"width": 80,
						"hidden": false,
						"editor" : "readonlytext"
					},{
						"field": "costName",
						"type": "textbox",
						"title": "扣项名称",
						"width": 120,
						"hidden": false,
						"$formatter": {valueField: 'costNo', textField: 'name', type: 'deduction'}
					},{
						"field": "taxRate",
						"type": "textbox",
						"title": "税率%",
						"width": 80,
						"hidden": false,
						"editor" : "readonlytext"
					},{
						"field": "ableSum",
						"type": "textbox",
						"title": "应结总额(含税)",
						"width": 80,
						"hidden": false,
						"editor" : "readonlytext"
					},{
						"field": "mallAmount",
						"type": "textbox",
						"title": "物业报数",
						"width": 120,
						"hidden": false,
						"editor" :{type:"numberbox",
							"options":{
								"precision":2
							}
						}
					},{
						"field": "diffAmount",
						"type": "textbox",
						"title": "差异金额",
						"width": 80,
						"hidden": false,
						"editor" : "readonlytext"
					},{
						"field": "ableAmount",
						"type": "textbox",
						"title": "扣项价款",
						"width": 80,
						"hidden": false,
						"editor" : "readonlytext"
					},{
						"field": "taxAmount",
						"type": "textbox",
						"title": "扣项税款",
						"width": 80,
						"hidden": false,
						"editor" : "readonlytext"
					}, {
						"field": "preDiffAmount",
						"type": "textbox",
						"title": "上期差异余额",
						"width": 80,
						"hidden": false,
						"editor" : "readonlytext"
					}, {
						"field": "curDiffAmount",
						"type": "textbox",
						"title": "本期差异余额",
						"width": 80,
						"hidden": false,
						"editor" : "readonlytext"
					}, {
						"field": "diffReason",
						"type": "textbox",
						"title": "差异原因",
						"width": 80,
						"hidden": false,
						"editor" : "text"
					}, {
						"field": "adjustAmount",
						"type": "textbox",
						"title": "调整金额",
						"width": 80,
						"hidden": false,
						"editor" : {type:"numberbox",
							"options":{
								"precision":2
							}
						}
					}, {
						"field": "backAmount",
						"type": "textbox",
						"title": "回款金额",
						"width": 80,
						"hidden": false,
						"editor" : {type:"numberbox",
							"options":{
								"precision":2
							}
						}
					}, {
						"field": "settleStatus",
						"type": "textbox",
						"title": "状态 ",
						"width": 80,
						"hidden": false,
						"editor" : {
							type : 'combobox',
							options:{
							    required:true,
							    valueField:'id',
							    textField:'name',
							    data:$.fas.datas.settleStatus
							}
						},
						"formatter":(value)=>isNotBlank(value)?$.fas.datas.settleStatus.first(c=>c.id == value).name:null,
					}, {
						"field": "billDebit",
						"type": "textbox",
						"title": "票扣标识",
						"width": 80,
						"hidden": false,
						"formatter":(value)=>isNotBlank(value)?$.fas.datas.billDebit.first(c=>c.id == value).name:null
					}, {
						"field": "accountDebit",
						"type": "textbox",
						"title": "账扣标识",
						"width": 80,
						"hidden": false,
						"formatter":(value)=>isNotBlank(value)?$.fas.datas.accountDebit.first(c=>c.id == value).name:null
					}, {
						"field": "remark",
						"type": "textbox",
						"title": "备注",
						"width": 80,
						"hidden": false,
						"editor" : "readonlytext"
					}]
				]
			},{
				id: 'grid4',
				url: config.rootUrl + '/mall/pay/list',
				title: "支付明细",
				height: "670",
				loadMsg: '请稍等,正在加载...',
				iconCls: 'icon-ok',
				pageSize: "150",
				pageList: [20, 50, 100, 200],
				checkOnSelect: false,
				pagination: true,
				fitColumns: false,
				singleSelect: false,
				rownumbers: true,
				enableHeaderContextMenu: true,
				enableHeaderClickMenu: true,
				emptyMsg: "暂无数据",
				showFooter:true,
				columns: [
				    [{
						"field": "id",
						"type": "textbox",
						"title": "id",
						"width": 100,
						"hidden": true,
						"editor" : "readonlytext"
					},{
						"field": "payName",
						"type": "textbox",
						"title": "支付名称",
						"width": 100,
						"hidden": false,
						"editor" : "readonlytext"
					},{
						"field": "payAmount",
						"type": "textbox",
						"title": "支付金额",
						"width": 80,
						"hidden": false,
						"editor" : "readonlytext"
					},{
						"field": "mallPayAmount",
						"type": "textbox",
						"title": "物业支付金额",
						"width": 100,
						"hidden": false,
						"editor" : {type:"numberbox",
							"options":{
								"precision":2
							}
						}
					},{
						"field": "diffPayAmount",
						"type": "textbox",
						"title": "差异金额",
						"width": 80,
						"hidden": false,
						"editor" :'readonlytext'
					},{
						"field": "paidWay",
						"type": "textbox",
						"title": "收款方",
						"width": 120,
						"hidden": false,
						"editor" : "readonlytext",
						"formatter":(value)=>isNotBlank(value)?$.fas.datas.paidWay.first(c=>c.id == value).name:null
					}]
				]
			}];
			
			$.gridFormat(dataurl, options);
			return options; 
			
		}
		getFootControls() {
            return {
                controls: [
                    {
                        "label": "制单人",
                        "type": "textbox",
                        "name": "createUser",
                        "options": {"width": 150}
                    }, {
                        "label": "制单时间",
                        "type": "textbox",
                        "name": "createTime",
                        "options": {"width": 150}
                    }, {
                        "label": "审核人",
                        "type": "textbox",
                        "name": "auditor",
                        "options": {"inputWidth": 200, "required": false}
                    }, {
                        "label": "审核时间",
                        "type": "textbox",
                        "name": "auditTime",
                        "options": {"inputWidth": 200, "required": false}
                    }]
            }
        }
		
		 lockHeader(status) {
        	let formObj = this.searchForm;
        	formObj.find("input").attr("readOnly", true).addClass("readonly");
        	formObj.find(".easyui-combobox").combobox('disable');
        	formObj.find(".easyui-combogridmdm").combobox('disable');
        	formObj.find(".easyui-datebox").datebox('disable');
        	formObj.find('#status,#companyNo,#businessType').combobox('disable');
        	if(status=='0'){
        		formObj.find('#remark,#preBillingAmount,#notBillingAmount,#preAbleAmount,#mallBillingAmount,#mallMinimumAmount').next().find('input').attr("readOnly", false).removeClass("readonly");
        	}
	     };
	     unLockHeader(){
	    	 let formObj = this.searchForm;
	    	 formObj.find(".easyui-combobox").combobox('enable');
	    	 formObj.find(".easyui-combogridmdm").combobox('enable');
	    	 formObj.find(".easyui-datebox").datebox('enable');
	    	 formObj.find("input").attr("readOnly", false).removeClass("readonly");
	    	 formObj.find('#status,#companyNo,#businessType').combobox('disable');
	     }
		searchBill(){
			this.page.mainTab.tabs('select', 1);
		}
		
		
		// 新单
		newBill(){
			// 清空表头
			this.searchForm.form('clear');
			this.searchForm.form('enable');
			this.unLockHeader();
			// 将表格的数据清空
			$('#view_grid_grid1').datagrid('loadData',{total:0,rows:[]});
			$('#view_grid_grid2').datagrid('loadData',{total:0,rows:[]});
			$('#view_grid_grid3').datagrid('loadData',{total:0,rows:[]});
			$('#view_grid_grid4').datagrid('loadData',{total:0,rows:[]});
		}
		
		verify(){
			var self = this;
			var id = $("#id").val();
			var key = $("#billNo").textbox('getValue');
			 this.service.getById(id).then(c=>{
			 if(0 == c.status) {
				$.messager.confirm("确认", "你确定要审核当前单据吗？",function (r) {
	                if (r) {  
	                	fas.common.loading("show", "正在处理中......");
	                	self.service.verify(key).then(d=>{
	                		fas.common.loading();
	                		if(d.errorCode=="0000"){
	                			self.searchForm.form('load',d.data);    
	                			self.footerForm.form('load', d.data)
	                			self.lockHeader(d.data.status);
	                		}
	                		showInfoMes(d,'审核');
						});
					}
	         });
			 }
		   });
		}
		
		unVerify(){
			var self = this;
			var id = $("#id").val();
			var key = $("#billNo").textbox('getValue');
			 this.service.getById(id).then(c=>{
			 if(4 == c.status) {
				$.messager.confirm("确认", "你确定要反审核当前单据吗？",function (r) {
	                if (r) {
	                	fas.common.loading("show", "正在处理中......");
	                	self.service.unVerify(key).then(d=>{
	                		fas.common.loading();
	                		if(d.errorCode=="0000"){
	                			self.searchForm.form('load',d.data);    
	                			self.footerForm.form('load', d.data)
	                			self.lockHeader(d.data.status);
	                		}
	                		showInfoMes(d,'反审核');
						});
					}
	         })
			 }else{
				 showWarn("该数据不是审核状态，不能反审核");
			 }
		   });
		}
		
		deleteBill(){
			var self = this;
			var id = $("#id").val();
			var key = $("#billNo").textbox('getValue');
			 this.service.getById(id).then(c=>{
			 if(0 == c.status) {
					$.messager.confirm("确认", "你确定要删除当前单据吗？",function (r) {
		                if (r) {
		                	fas.common.loading("show", "正在处理中......");
		                	self.service.deleteBill(key).then(d=>{
		                		fas.common.loading();
		                		if(d.errorCode=="0000"){
		                			self.newBill();
		                		}
		                		showInfoMes(d,'删除');
							});
						}
		         });
			 }
		   });
		}
		
		getBillDate(){
			var formData = this.searchForm.form('getData');
			var $dg = $("#view_grid_grid2");
			formData.insertMallSaleCostList = $dg.datagrid('getChanges', "inserted");
        	formData.deleteMallSaleCostList = $dg.datagrid('getChanges', "deleted");
        	formData.updateMallSaleCostList = $dg.datagrid('getChanges', "updated");
        	
        	var $dg = $("#view_grid_grid3");
			formData.insertMallCostList = $dg.datagrid('getChanges', "inserted");
        	formData.deleteMallCostList = $dg.datagrid('getChanges', "deleted");
        	formData.updateMallCostList = $dg.datagrid('getChanges', "updated");
        	var $dg = $("#view_grid_grid4");
        	formData.updateMallPayList = $dg.datagrid('getChanges', "updated");
         	return formData;
		}
		
		// 保存单据
		save(){
			var self = this;
			if(!this.searchForm.form('validate')){
				return;
			}
			this.endEdit($("#view_grid_grid2"));
			this.endEdit($("#view_grid_grid3"));
			this.endEdit($("#view_grid_grid4"));
			var id = $("#id").val();
			if(!isNotBlank(id)){
				self.saveBill();
			}else{
				this.service.getById(id).then(c=>{
					 if(0 == c.status) {
						 self.saveBill();
					 }
				})
			}
		}
		
		saveBill(){
			var self = this;
			fas.common.loading("show", "正在处理中......");
			this.service.save({'billMallBalance':JSON.stringify(self.getBillDate())}).then(d=>{
				fas.common.loading();
				if(d.data.billNo){
					self.searchForm.form('load',d.data);
					self.footerForm.form('load', d.data);
					self.loadedDtl(d.data.billNo);
					self.lockHeader(d.data.status);
				}
				showInfoMes(d,'保存');
			 })
		}
		
		// 双击跳转重新加载明细
		 loadedData(rowData, rowIndex) {
			 this.lockHeader(rowData.status);
			 this.loadedDtl(rowData.billNo);
			 this.footerForm.form('load', rowData);
	     };
		
		loadedDtl(billNo){
			if(!billNo){
				return;
			}
			$("#view_grid_grid1").datagrid({
				url:`${config.rootUrl}/bill/mall/balance/summary?balanceBillNo=${billNo}`,
			});
			$("#view_grid_grid2").datagrid({
				url:`${config.rootUrl}/mall/sale/cost/list?balanceBillNo=${billNo}`,
			});
			$("#view_grid_grid3").datagrid({
				url:`${config.rootUrl}/mall/cost/list?balanceBillNo=${billNo}`,
			});
			$("#view_grid_grid4").datagrid({
				url:`${config.rootUrl}/mall/pay/list?balanceBillNo=${billNo}`,
			});
		}
		//新增明细
		create(){
			var key = $("#id").val();
			if(!isNotBlank(key)){
				return false;
			}
			this.service.getById(key).then(c=>{
				if(c.status==0){
					var rows = this.currentGrid.datagrid('getRows');
					//校验行必填
					let falg = true;
					for ( var i = 0; i < rows.length; i++) {
		            	if(!this.currentGrid.datagrid('validateRow',i)){
		            		falg = false;
		            		break;
		            	}
					 }
			        if(!falg){
			           return;
			        }
			        var rowIndex = rows.length;
		            if(rows.length==0){
		            	this.currentGrid.datagrid("options").pageNumber = 1;
		            }
					this.currentGrid.datagrid('insertRow', {
		                index: rowIndex,
		                row: {
		                }
		            });
		            this.currentGrid.datagrid('selectRow', rowIndex);
		            this.currentGrid.datagrid('beginEdit', rowIndex);
		            this.rowIndex = rowIndex;
				}
			})
		}
		
		saveDetail(){
			var self = this;	
			var key = $("#id").val();
			if(!isNotBlank(key)){
				return false;
			}
			this.service.getById(key).then(c=>{
    			if(c.status==0){
    				self.endEdit(this.currentGrid);
    				self.save();
    			}
			})
		};
		
		endEdit(grid) {
			var rows = grid.datagrid('getRows');
            for ( var i = 0; i < rows.length; i++) {
            	grid.datagrid('endEdit', i);
            }
        };

        validate() {
            return this.currentGrid.datagrid('validateRow', this.rowIndex);
        }
        
        getSelectRowIndex(){
			var row = this.currentGrid.datagrid('getSelected');
			return this.currentGrid.datagrid('getRowIndex',row);	
		}
        
    	delete(){
    		var id = $("#id").val();
    		this.service.getById(id).then(c=>{
    			if(c.status==0){
    				var index = this.getSelectRowIndex();
    	            if (index >= 0)
    	                this.currentGrid.datagrid('deleteRow',index);
    			}
    		})
		}
		//取消编辑
		cancel(){
			var billNo = $("#billNo").textbox('getValue');
			this.loadedDtl(billNo);
		}
		//双击事件
		onGridDblClickRow(grid, rowIndex, rowData){
			if(grid[0].id=='view_grid_grid1'){
				return;
			}
			var self = this;	
			var key = $("#id").val();
			if(!isNotBlank(key)){
				return false;
			}
			this.service.getById(key).then(c=>{
				if(c.status==0){
					self.rowIndex = rowIndex;
					self.endEdit(grid);
		            grid.rowIndex = rowIndex;
		            grid.datagrid('beginEdit', rowIndex);
				}
			})
		}
		
		resize() {
            var h = null;
            if (window.top == window.self) {// 不存在父页面
                h = document.body.clientHeight;
            } else {
                h = $(window.document.body).height();
            }
            $("#main_layer").height(h-28);
            let mainH = $("#main_layer").height();
            let main_toolbarH = $("#main_toolbar").outerHeight();
            let topPanelH = $(".search-div").outerHeight();
            let sublayerH = mainH - main_toolbarH;
            $(`#${this.viewId}_foot_panel`).height(10)
            let mainCenterPanelH = sublayerH - topPanelH -20;
            $(`#${this.viewId}_main_panel`).height(mainCenterPanelH);
        }
	}

	class SearchBill extends UI.EditListView {
		constructor() {
			super("main");
			this.title = "单据列表";
		}
		getToolbars() {
			return {
				id: 'toolbar',
				data: [{
						id: "btn-search",
						iconCls: 'icon icon-search',
						text: '查询',
						value: 1,
					},
					{
						id: "btn-clear",
						iconCls: 'icon icon-spinner11',
						text: '重置',
						value: 2
					},
					{
						id: "btn-export",
						iconCls: 'icon  icon-upload2',
						text: '导出',
						value: 2
					}
				]
			}
		}

		getSearchControls() {
			return {
				list: 4,
				controls: [{
					"label": "单据编码",
					"type": "textbox",
					"name": "billNo",
					"options": { "width": 150}
				}, {
					"label": "卖场",
					"type": "combogridmdm",
					"name": "shopNo",
					"options": { "width": 150,
						"beanName":"shop"}
				}, {
					"label": "物业公司",
					"type": "combogridmdm",
					"name": "mallNo",
					"options": { "width": 150,
						"beanName":"propertyCompany",
						"beanNo":"companyNo"}
				},{
					"label":"铺位组",
					"type":"combogridmdm",
					"name":"bunkGroupNo",
					"options":{
						"width":150,
						"beanName":"bunkGroup",
						"beanNo":"bunkGroupNo",
						"cnName":"bunkGroupNo"
					}
				}, {
					"label": "公司",
					"type": "combogridmdm",
					"name": "companyNo",
					"options": { "width": 150,
						"beanName":"company"
						}
				}, {
					"label": "状态",
					"type": "combocommon",
					"name": "status",
					"options": { 
						"width": 150,
						"type":"status"
					}
				}, {
					"label": "结算月",
					"type": "datebox",
					"name": "settleMonth",
					"options": { "width": 150,
						"dateFmt":"yyyyMM"
						}
				}, {
					"label": "制单人",
					"type": "textbox",
					"name": "createUser",
				    "options": { "width": 150}
				}, {
					"label": "审核人",
					"type": "textbox",
					"name": "auditor",
				    "options": { "width": 150}
				}]
			}
		}

		getGridOptions() {
			var dataurl = portalUrl+'/mdm/data/api';
			var options = {
				id: 'grid',
				url: config.rootUrl + '/bill/mall/balance/list',
				height: "670",
				loadMsg: '请稍等,正在加载...',
				iconCls: 'icon-ok',
				pageSize: "150",
				pageList: [20, 50, 100, 200],
				checkOnSelect: false,
				pagination: true,
				fitColumns: false,
				singleSelect: false,
				rownumbers: true,
				export: {async: true,
					method:"wss",
					url: config.rootUrl + '/bill/mall/balance/list'},
				enableHeaderContextMenu: true,
				enableHeaderClickMenu: true,
				emptyMsg: "暂无数据",
				columns: [
					[ {
						"title": "单据编码",
						"type": "textbox",
						"field": "billNo",
						"width": 150
					}, {
						"title": "公司",
						"type": "combogridmdm",
						"field": "companyNo",
						"$formatter": {valueField: 'companyNo', textField: 'name', type: "company"},
						"width": 100
					}, {
						"title": "卖场",
						"type": "combogridmdm",
						"field": "shopNo",
						"$formatter": {valueField: 'shopNo', textField: 'name', type: "shop"},
						"width": 100
					}, {
						"title": "物业公司",
						"type": "combogridmdm",
						"field": "mallNo",
						"$formatter": {valueField: 'mallNo', textField: 'name', type: "propertyCompany"},
						"width": 100
					}, {
						"title": "铺位组",
						"type": "textbox",
						"field": "bunkGroupNo",
						"width": 100
					}, {
						"title": "状态",
						"type": "combocommon",
						"field": "status",
						"formatter":function(value){
							if(isNotBlank(value)){
								return $.fas.datas.status.first(c=>c.id == value).name;
							}else{
								return value;
							}
						},
						"width": 100
					}, {
						"title": "结算月",
						"type": "datebox",
						"field": "settleMonth",
						"width": 100
					}, {
						"title": "开始日期",
						"type": "datebox",
						"field": "settleStartDate",
						"width": 100
					}, {
						"title": "结束日期",
						"type": "datebox",
						"field": "settleEndDate",
						"width": 100
					}, {
						"title": "开店时间",
						"type": "datebox",
						"field": "openDate",
						"width": 100
					}, {
						"title": "合作性质",
						"type": "combocommon",
						"field": "businessType",
						"formatter":function(value){
							if(isNotBlank(value)){
								return $.fas.datas.businessType.first(c=>c.id == value).name;
							}else{
								return value;
							}
						},
						"width": 100
					}, {
						"title": "合同面积",
						"type": "textbox",
						"field": "areaCompact",
						"width": 100
					}, {
						"title": "备注",
						"type": "textbox",
						"field": "remark",
						"width": 100
					}, {
						"title": "制单时间",
						"type": "datebox",
						"field": "createTime",
						"width": 100
					}, {
						"title": "制单人",
						"type": "textbox",
						"field": "createUser",
						"width": 100
					}, {
						"title": "审核时间",
						"type": "datebox",
						"field": "auditTime",
						"width": 100
					}, {
						"title": "审核人",
						"type": "textbox",
						"field": "auditor",
						"width": 100
					}]
				]
			};
			$.gridFormat(dataurl, options);
			return [options];   
		}
	
		onGridDblClickRow(grid, rowIndex, rowData) {
            this.page.switchTab(rowData, rowIndex);
        }
		
	    resize(container) {
            var viewId = this.viewId;
            var h = null;
            if (window.top == window.self) {// 不存在父页面
                h = document.body.clientHeight;
            } else {
                h = $(window.document.body).height();
            }
            let main_toolbarH = $("#main_toolbar").outerHeight();
            let searchH = container.find(".search-div").outerHeight();
            let hh = h - main_toolbarH - searchH - 26;
            if (container.length >= 1)
                container.find(`#${viewId}_main_panel`).height(hh);
            else
                $(`#${viewId}_main_panel`).height(hh);
        }
	}

	class Page extends UI.Page {
		constructor() {
			super('4010141', $('#mainPanel'));
			this.views = [new BillDetail(),new SearchBill()]
		}
	}

	var page = new Page();

	page.render();
});