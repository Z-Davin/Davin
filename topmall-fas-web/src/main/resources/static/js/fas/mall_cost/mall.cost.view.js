"use strict";
define(function (require, exports, module) {
    let UI = require('core/ui');
    let mallCostService = require('./mall.cost.service.js');    
    let config = require('../config');
    let status = $.fas.datas.status;
    
    class BillView extends UI.EditListView {
        constructor() {
            super("main");
            this._service = new mallCostService();
        }
        get service() {
            return this._service;
        }
        getToolbars() {
        	 var items = ['新增', '删除','查询','重置','导出'];
        	 items = $.merge(items, [
        	         	            {id: "btn-import", iconCls: 'icon icon-download2', text: '导入', value: 6, order: "5"},
        	          				{id: "btn-confirm", iconCls: 'icon  icon-file-text', text: '确认', value: 8, order: "8"},
        	          				{id: "btn-unConfirm", iconCls: 'icon  icon-file-text2', text: '反确认', value: 9, order: "11"}
        	                ]);
             return {
                 id: 'toolbar',
                 data: items
             }
        }
       
		getSearchControls() {
			return {
				list: 4,
				controls: [{
					"label": "卖场",
					"type": "combogridmdm",
				    "name": "shopNo",
				    "options": {
				        "width": 150,
				        "required": false,
				        "beanName":"shop"
				    }
				}, {
					"label": "公司",
					"type": "combogridmdm",
					"name": "companyNo",
					"options": { "width": 150,
						"beanName":"company"}
				}, {
					"label": "扣项",
					"type": "combogridmdm",
					"name": "costNo",
					"options": { 
						"width": 150,
						"beanName": "deduction",
						"parameterBeanName":"suitName",
						"parameterBeanValue":"2"
					}
				}, {
					"label": "结算月",
					"type": "datebox",
					"name": "settleMonth",
					"options": { "width": 150,
						"dateFmt":"yyyyMM"}
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
					"label": "状态",
					"type": "combocommon",
					"name": "status",
					"options": {"width": 150, "type": "status", "required": false, "disabled": false}
				}, {
					"label": "制单人",
					"type": "textbox",
					"name": "createUser",
					"options": { "width": 150 }
				}, {
					"label": "生成方式",
					"type": "combocommon",
					"name": "source",
					"options": {"width": 150, "type": "source", "required": false, "disabled": false}
				}, {
					"label": "完结状态",
					"type": "combocommon",
					"name": "settleStatus",
					"options": {"width": 150, "type": "settleStatus", "required": false, "disabled": false}
				}]
			}
		}
		getGridOptions() {
			var dataurl = portalUrl+'/mdm/data/api';
			var options = {
				id: 'grid',
				url: config.rootUrl + '/mall/cost/list',
				title: "",
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
					url: config.rootUrl + '/mall/cost/list'
				},
				enableHeaderContextMenu: true,
				enableHeaderClickMenu: true,
				emptyMsg: "暂无数据",
				showFooter:true,
				columns: [
					[{
						"field": "shopName",
						"type": "textbox",
						"title": "卖场名称",
						"$formatter": {valueField: 'shopNo', textField: 'name', type: "shop"},
						"width": 100,
						"hidden": false,
						"editor" : "readonlytext"
					},{
						"field": "mallName",
						"type": "textbox",
						"title": "物业公司名称",
						"$formatter": {valueField: 'mallNo', textField: 'name', type: "propertyCompany"},
						"width": 100,
						"hidden": false,
						"editor" : "readonlytext"
					},{
                        "field": "bunkGroupNo",
                        "type": "textbox",
                        "title": "铺位组",
                        "width": 80,
                        "hidden": false,
                        "editor" : "readonlytext"
                    },{
						"field": "settleMonth",
						"type": "textbox",
						"title": "结算月",
						"width": 80,
						"hidden": false
					}, {
						"field": "---",
						"type": "textbox",
						"title": "结算期",
						"width": 160,
						"hidden": false,
						"formatter": function (value, row, index) {
							if(row.settleStartDate!=null || row.settleEndDate!=null){
								return row.settleStartDate+"--"+row.settleEndDate;
							}else{
								return null;
							}
                       },
						"editor" : "readonlytext"
					}, {
						"field": "costNo",
						"type": "textbox",
						"title": "扣项编码",
						"width": 80,
						"hidden": false,
						"editor" : "readonlytext"
					}, {
						"field": "costName",
						"type": "textbox",
						"title": "扣项名称",
						"$formatter": {valueField: 'costNo', textField: 'name', type: "deduction"},
						"width": 80,
						"hidden": false,
						"editor" : "readonlytext"
					},{
						"field": "status",
						"type": "textbox",
						"title": "状态",
						"width": 80,
						"hidden": false,
						"editor" : "readonlytext",
						"formatter":(value)=>isNotBlank(value)?status.first(c=>c.id == value).name:null
					}, {
						"field": "balanceBillNo",
						"type": "textbox",
						"title": "结算单号",
						"width": 80,
						"hidden": false,
						"editor" : "readonlytext"
					}, {
						"field": "ableSum",
						"type": "textbox",
						"title": "应结总额(含税)",
						"width": 60,
						"hidden": false,
						"editor" : "readonlytext"
					}, {
						"field": "ableAmount",
						"type": "textbox",
						"title": "应结价款",
						"width": 60,
						"hidden": false,
						"editor" : "readonlytext"
					},{
						"field": "mallAmount",
						"type": "textbox",
						"title": "物业报数",
						"width": 60,
						"hidden": false,
						"editor":"readonlytext"
					},{
						"field": "diffAmount",
						"type": "textbox",
						"title": "差异金额",
						"width": 60,
						"hidden": false,
						"editor":"readonlytext"
					}, {
						"field": "preDiffAmount",
						"type": "textbox",
						"title": "上期差异余额",
						"width": 60,
						"hidden": false,
						"editor" : "readonlytext"
					}, {
						"field": "curDiffAmount",
						"type": "textbox",
						"title": "本期差异余额",
						"width": 60,
						"hidden": false,
						"editor" : "readonlytext"
					}, {
						"field": "taxAmount",
						"type": "textbox",
						"title": "税额",
						"width": 60,
						"hidden": false,
						"editor" : "readonlytext"
					}, {
						"field": "taxFlag",
						"type": "textbox",
						"title": "是否含税",
						"formatter":(value)=>isNotBlank(value)?$.fas.datas.taxFlag.first(c=>c.id == value).name:null,
						"width": 60,
						"hidden": false,
						"editor" : "readonlytext"
					}, {
						"field": "taxRate",
						"type": "textbox",
						"title": "税率(%)",
						"width": 60,
						"hidden": false,
						"editor" : "readonlytext"
					}, {
						"field": "billDebit",
						"type": "textbox",
						"title": "票扣",
						"width": 50,
						"hidden": false,
						"editor" : "readonlytext",
						"formatter":(value)=>isNotBlank(value)?$.fas.datas.billDebit.first(c=>c.id == value).name:null
					}, {
						"field": "accountDebit",
						"type": "textbox",
						"title": "账扣",
						"width": 50,
						"hidden": false,
						"editor" : "readonlytext",
						"formatter":(value)=>isNotBlank(value)?$.fas.datas.accountDebit.first(c=>c.id == value).name:null
					}, {
						"field": "remark",
						"type": "textbox",
						"title": "备注",
						"width": 80,
						"hidden": false,
						"editor" : "readonlytext"
					}, {
						"field": "source",
						"type": "textbox",
						"title": "生成方式",
						"width": 80,
						"hidden": false,
						"editor" : "readonlytext",
						"formatter":(value)=>isNotBlank(value)?$.fas.datas.source.first(c=>c.id == value).name:null
					},{
						"field": "companyNo",
						"type": "textbox",
						"title": "公司编码",
						"width": 80,
						"hidden": false,
						"editor" : "readonlytext"
					}, {
						"field": "companyName",
						"type": "textbox",
						"title": "公司名称",
						"$formatter": {valueField: 'companyNo', textField: 'name', type: "company"},
						"width": 80,
						"hidden": false,
						"editor" : "readonlytext"
					},{
						"field": "shopNo",
						"type": "textbox",
						"title": "卖场编码",
						"width": 80,
						"hidden": false,
						"editor" : "readonlytext"
					},{
						"field": "mallNo",
						"type": "textbox",
						"title": "物业编码",
						"width": 80,
						"hidden": false,
						"editor" : "readonlytext"
					},{
						"field": "settleStatus",
						"type": "textbox",
						"title": "完结状态",
						"width": 70,
						"hidden": false,
						"editor" : "readonlytext",
						"formatter":(value)=>isNotBlank(value)?$.fas.datas.settleStatus.first(c=>c.id == value).name:null
					},{
						"field": "createUser",
						"type": "textbox",
						"title": "创建人",
						"width": 80,
						"hidden": false,
						"editor" : "readonlytext"
					},{
						"field": "createTime",
						"type": "textbox",
						"title": "创建时间",
						"width": 150,
						"hidden": false,
						"editor" : "readonlytext"
					},{
						"field": "auditor",
						"type": "textbox",
						"title": "审核人",
						"width": 80,
						"hidden": false,
						"editor" : "readonlytext"
					},{
						"field": "auditTime",
						"type": "textbox",
						"title": "审核时间",
						"width": 150,
						"hidden": false,
						"editor" : "readonlytext"
					}]
				]
			};
			$.gridFormat(dataurl, options);
				return [options];   
		}
		getDetailControls(){
			return  {
					controls:[
					   {
						"label":"卖场",
						"type":"combogridmdm",
						"name":"shopNo",
						"options":{
							"width":200,
							"required":true,
							"beanName":"shop",
							onHidePanelFn: function(rec) {
								if(rec){
									$('#mallNo','#view_main_panel_edit').combogrid('setValue','');
								}
							}
						}
					},{
						"label":"物业公司",
						"type":"combogridmdm",
						"name":"mallNo",
						"options":{
							"width":200,
							"required":true,
							"beanName":"propertyCompany",
							"beanNo":"companyNo",
							onShowPanelFn: function(jq) {
								var shopNo = $('#shopNo','#view_main_panel_edit').combogrid('getValue');
								$('#mallNo','#view_main_panel_edit').combogrid("grid").datagrid('options').queryParams.shopNo=shopNo;
								$('#mallNo','#view_main_panel_edit').combogrid("options").queryParams.shopNo=shopNo;
							},
							onHidePanelFn: function(rec) {
								if(rec){
									$('#bunkGroupNo','#view_main_panel_edit').combogrid('setValue','');
								}
							}
						}
					},{
						"label":"铺位组",
						"type":"combogridmdm",
						"name":"bunkGroupNo",
						"options":{
							"width":200,
							"required":true,
							"beanName":"bunkGroup",
							"beanNo":"bunkGroupNo",
							"cnName":"bunkGroupNo",
							onShowPanelFn: function(jq) {
								var shopNo = $('#shopNo','#view_main_panel_edit').combogrid('getValue');
								var mallNo = $('#mallNo','#view_main_panel_edit').combogrid('getValue');
								$('#bunkGroupNo','#view_main_panel_edit').combogrid("grid").datagrid('options').queryParams.shopNo=shopNo;
								$('#bunkGroupNo','#view_main_panel_edit').combogrid("options").queryParams.shopNo=shopNo;
								$('#bunkGroupNo','#view_main_panel_edit').combogrid("grid").datagrid('options').queryParams.companyNo=mallNo;
								$('#bunkGroupNo','#view_main_panel_edit').combogrid("options").queryParams.companyNo=mallNo;
							}
						}
					},{
						"label":"费用项目",
						"type":"combogridmdm",
						"name":"costNo",
						"options":{
							"width":200,
							"required":true,
							"beanName": "deduction",
							"parameterBeanName":"suitName",
							"parameterBeanValue":"2"
								//1专柜 2物业
						}
					},{
						"label":"应结总额(含税)",
						"type":"numberbox",
						"name":"ableSum",
						"options":{
							"width":200,
							"required":true,
							"precision":2
							}
					}, {
						"name": "billDebit",
						"type": "combocommon",
						"label": "票扣",
						"options":{
							"width":200,
							"required":true,
							"type":"billDebit"
							}
					}, {
						"name": "accountDebit",
						"type": "combocommon",
						"label": "账扣",
						"options":{
							"width":200,
							"required":true,
							"type":"accountDebit"
							}
					},{
						"label":"税率",
						"type":"numberbox",
						"name":"taxRate",
						"options":{
							"width":200,
							"required":true
							}
					},{
						"label":"结算月",
						"type":"datebox",
						"name":"settleMonth",
						"options":{
							"width":200,
							"required":true,
							"dateFmt":"yyyyMM"
							}
					},{
						"label":"结算期",
						"type":"settlementMallDateBox",
						"name":"settleStartDate",
						"options":{
							"width":200,
							"required":true
							}
					},{
						"label":"---",
						"type":"datebox",
						"name":"settleEndDate",
						"options":{
							"width":200,
							"required":true,
							"disabled":true
							}
					},{
						"label":"备注",
						"type":"textbox",
						"name":"remark",
						"colspan":"2",
						"options":{
							"width":525,
							"required":false
							}
					}]
			};
		}
		
		setDefaultValue(){
			$('#taxRate').numberbox('setValue', 17);
		}
		
//		 getSelectedRows() {
//	            return this.currentGrid.datagrid('getChecked');
//	      }
		//确认
		confirm(){
            let data = this.getSelectedRows();
            if (data == null || data.length == 0)
                return;
            var self = this;
            let keys = _.map(data,function(e){ return e.id; });
            let params = {ids:"'"+keys.join("','")+"'",targetStatus:1};
            self.service.selectByParams(params).then(d=>{
            	if(!d||d.length<1){
					$.messager.confirm("确认", "确定要确认当前所选单据吗？",function (r) {
						if (r) {
							fas.common.loading("show", "正在处理中......");
							self.service.confirm(keys).then(d=>{
								fas.common.loading();
								self.search();
								if(d.errorCode=="0000"){
									showSuc('确认成功');
								}else{
									showWarn('确认失败');
								}
						});
						}
					});
				}else{
                	showWarn("所选单据中存在非制单状态的单据,不能确认.");
                	return;
				}
			});
		}
		unConfirm(){
            let data = this.getSelectedRows();
            if (data == null || data.length == 0)
                return;
            var self = this;
            let keys = _.map(data,function(e){ return e.id; });
			let params = {ids:"'"+keys.join("','")+"'",targetStatus:2};
            this.service.selectByParams(params).then(d=>{
            	if(!d||d.length<1){
					$.messager.confirm("确认", "确定要反确认当前所选单据吗？",function (r) {
						if (r) {
							fas.common.loading("show", "正在处理中......");
							self.service.unConfirm(keys).then(d=>{
								fas.common.loading();
								self.search();
								if(d.errorCode=="0000"){
									showSuc('反确认成功');
								}else{
									showWarn('反确认失败');
								}
						});
						}
					});
				}else{
                	showWarn("所选单据中存在非生效状态的单据,不能反确认.");
                	return;
				}
			});
		}
		
		/**
         * 删除
         */
        delete() {
            let data = this.getSelectedRows();
            if (data == null || data.length == 0)
                return;
            var self = this;
            let key = data[0].id;
            this.service.getById(key).then(c=>{
            	if(1 == c.status){
            		$.messager.confirm('删除', '确认删除所选记录?',
                            r=> {
                                if (r) {
                                    var ids = data.map(i=>i[this.primaryKey]);
                                    self.service.delete(ids).then(c=> {
                                        self.search();
                                    });
                                }
                            }
                        );
            	}else{
            		showWarn("该数据不是未生效状态，不能删除");
            		return;
            	}
            });
            
        }
		//重写双击事件,判断是否是制单状态
		 onGridDblClickRow(grid, rowIndex, rowData) {
            this.data = {rowIndex, rowData};
            this.currentGrid.datagrid('selectRow', rowIndex);
            let key = rowData.id;
            this.service.getById(key).then(c=>{
        		if(1 == c.status) {
        			 this.edit();   	
        		} else {
        			return;
        		} 
        	});
	      }
		 
		 
		 import() {
			 var self=this;
			 if (typeof self.importExcelH2V == 'undefined') {
				seajs.use(['mall_cost/mall.cost.io'], function(Excel) {
					var imports = self.importExcelH2V = new Excel.importer();
					imports.import();
				});
			 } else {
				 self.importExcelH2V.import();
			 }
		 }
    }
    
    
    class Page extends UI.Page {
        constructor() {
            super('4010100',$('#mainPanel'));
            this.views = [new BillView()]
        }
    }
    var page = new Page();
    page.render();
});