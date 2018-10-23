"use strict";
define(function (require, exports, module) {
    let UI = require('core/ui');
    let supplierAccountService = require('./supplier.account.service.js');    
    let config = require('../config');
    let status = $.fas.datas.status;
    
    class BillView extends UI.EditListView {
        constructor() {
            super("main");
            this._service = new supplierAccountService();
        }
        get service() {
            return this._service;
        }
        getToolbars() {
        	 var items = ['新增', '删除','查询','重置','导出'];
        	 items = $.merge(items, [
        	                        {id: "btn-import", iconCls: 'icon icon-download2', text: '导入', value: 6, order: "5"},
        	          				{id: "btn-enable", iconCls: 'icon icon-eye-plus', text: '启用', value: 21, order: "8"},
        	          				{id: "btn-disable", iconCls: 'icon  icon-eye-blocked', text: '禁用', value: 22, order: "8"}
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
					"label": "大区",
					"type": "combogridmdm",
					"name": "zoneNo",
					"options": { 
						"width": 150,
						"beanName":"zone"
					 }
				},{
					"label": "供应商",
					"type": "combogridmdm",
					"name": "supplierNo",
					"options": { "width": 150,
						"beanName":"supplier"}
				},{
					"label": "卖场名称",
					"type": "combogridmdm",
				    "name": "shopNo",
				    "options": {
				        "width": 150,
				        "required": false,
				        "beanName":"shop",
				        onHidePanelFn: function(rec) {
							if(rec){
								$('#counterNo','#main_top_panel').combogrid('setValue','');
							}
				        }
				    }
				}, {
					"label": "专柜",
					"type": "combogridmdm",
					"name": "counterNo",
					"options": { "width": 150,
						"beanName":"counter",
						onShowPanelFn: function(jq) {
							var shopNo = $('#shopNo','#main_top_panel').combogrid('getValue');
							$('#counterNo','#main_top_panel').combogrid("grid").datagrid('options').queryParams.shopNo=shopNo;
							$('#counterNo','#main_top_panel').combogrid("options").queryParams.shopNo=shopNo;
						}}
				}, {
					"label": "卖场公司",
					"type": "combogridmdm",
					"name": "companyNo",
					"options": { "width": 150,
						"beanName":"company"}
				}, {
					"label": "状态",
					"type": "combocommon",
					"name": "status",
					"options": {"width": 150, "type": "status", "required": false, "disabled": false,
						"datarange":"8,9"
					}
				}, {
					"label": "制单人",
					"type": "textbox",
					"name": "createUser",
					"options": { "width": 150 }
				}]
			}
		}
		getGridOptions() {
			var dataurl = portalUrl+'/mdm/data/api';
			var options = {
				id: 'grid',
				url: config.rootUrl + '/supplier/account/list',
				title: "",
				height: "670",
				loadMsg: '请稍等,正在加载...',
				iconCls: 'icon-ok',
				pageSize: "150",
				pageList: [20, 50, 100, 200],
				checkOnSelect: false,
				pagination: true,
				fitColumns: false,
				rownumbers: true,
				singleSelect: false,
				export: {async: true,
					method:"wss",
					url: config.rootUrl + '/supplier/account/list'
				},
				enableHeaderContextMenu: true,
				enableHeaderClickMenu: true,
				emptyMsg: "暂无数据",
				showFooter:true,
				columns: [
					[{
                        "field": 'ck',
                        "notexport": true,
                        "checkbox": true
                    },{
                        "field": "zoneName",
                        "type": "textbox",
                        "title": "大区",
                        "width": 80,
                        "hidden": false,
                        "$formatter": {valueField: 'zoneNo', textField: 'name', type: "zone"},
                        "editor" : "readonlytext"
                    }, {
                        "field": "counterNo",
                        "type": "textbox",
                        "title": "专柜编码",
                        "width": 80,
                        "hidden": false,
                        "editor" : "readonlytext"
                    }, {
                        "field": "counterName",
                        "type": "textbox",
                        "title": "专柜名称",
                        "width": 80,
                        "hidden": false,
                        "$formatter": {valueField: 'counterNo', textField: 'name', type: "counter"},
                        "editor" : "readonlytext"
                    }, {
                        "field": "shopNo",
                        "type": "textbox",
                        "title": "卖场编码",
                        "width": 80,
                        "hidden": false,
                        "editor" : "readonlytext"
                    }, {
                        "field": "shopName",
                        "type": "textbox",
                        "title": "卖场名称",
                        "width": 80,
                        "hidden": false,
                        "$formatter": {valueField: 'shopNo', textField: 'name', type: "shop"},
                        "editor" : "readonlytext"
                    }, {
                        "field": "taxNo",
                        "type": "textbox",
                        "title": "公司税号",
                        "width": 80,
                        "hidden": false,
                        "editor" : "readonlytext"
                    }, {
                        "field": "suCompanyName",
                        "type": "textbox",
                        "title": "供应商公司名称",
                        "width": 80,
                        "hidden": false,
                        "editor" : "readonlytext"
                    },{
                        "field": "bankName",
                        "type": "textbox",
                        "title": "开户银行",
                        "width": 80,
                        "hidden": false,
                        "editor" : "readonlytext"
                    },{
                        "field": "bankAccount",
                        "type": "textbox",
                        "title": "银行账号",
                        "width": 80,
                        "hidden": false,
                        "editor" : "readonlytext"
                    },{
                        "field": "bankAccountName",
                        "type": "textbox",
                        "title": "银行账户名",
                        "width": 80,
                        "hidden": false,
                        "editor" : "readonlytext"
                    },{
                        "field": "address",
                        "type": "textbox",
                        "title": "公司地址",
                        "width": 80,
                        "hidden": false,
                        "editor" : "readonlytext"
                    }, {
						"field": "status",
						"type": "textbox",
						"title": "状态",
						"width": 80,
						"hidden": false,
						"editor" : "readonlytext",
						"formatter":(value)=>isNotBlank(value)?status.first(c=>c.id == value).name:null
					}, {
						"field": "remark",
						"type": "textbox",
						"title": "备注",
						"width": 80,
						"hidden": false,
						"editor" : "readonlytext"
					}, {
						"field": "supplierNo",
						"type": "textbox",
						"title": "供应商编码",
						"width": 80,
						"hidden": false,
						"editor" : "readonlytext"
					}, {
						"field": "supplierName",
						"type": "textbox",
						"title": "供应商名称",
						"$formatter": {valueField: 'supplierNo', textField: 'name', type: "supplier"},
						"width": 80,
						"hidden": false,
						"editor" : "readonlytext"
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
			var options =  {
					colcount:2,
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
									$('#counterNo','#view_main_panel_edit').combogrid('setValue','');
								}
							}
						}
					},{
						"label":"专柜",
						"type":"combogridmdm",
						"name":"counterNo",
						"options":{
							"width":200,
							"required":true,
							"beanName":"counter",
							onShowPanelFn: function(jq) {
								var shopNo = $('#shopNo','#view_main_panel_edit').combogrid('getValue');
								$('#counterNo','#view_main_panel_edit').combogrid("grid").datagrid('options').queryParams.shopNo=shopNo;
								$('#counterNo','#view_main_panel_edit').combogrid("options").queryParams.shopNo=shopNo;
							}
						}
					},{
						"label":"公司税号",
						"type":"textbox",
						"name":"taxNo",
						"options":{
							"width":200,
							"required":false
						}
					},{
						"label":"供应商公司名称",
						"type":"textbox",
						"name":"suCompanyName",
						"options":{
							"width":200,
							"required":false
						}
					},{
						"label":"开户银行",
						"type":"textbox",
						"name":"bankName",
						"options":{
							"width":200,
							"required":true
						}
					},{
						"label":"银行账号",
						"type":"textbox",
						"name":"bankAccount",
						"options":{
							"width":200,
							"required":true
						}
					},{
						"label":"银行账户名",
						"type":"textbox",
						"name":"bankAccountName",
						"options":{
							"width":200,
							"required":false
						}
					},{
						"label":"公司地址",
						"type":"textbox",
						"name":"address",
						"options":{
							"width":200,
							"required":false
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
			return options;
		}
		
		//启用
		enable(){
			this.operation(8);
		}
		//禁用
		disable(){
			this.operation(9);
		}
		
		operation(operation){
            let data = this.getSelectedRows();
            if (data == null || data.length == 0)
                return;
            var self = this;
            let keys = _.map(data,function(e){ return e.id; });
			let params = {ids:"'"+keys.join("','")+"'",targetStatus:operation};
            this.service.selectByParams(params).then(d=>{
            	if(!d||d.length<1){
					$.messager.confirm("确认", "确定要操作当前所选单据吗？",function (r) {
						if (r) {
							fas.common.loading("show", "正在处理中......");
							self.service.operation(keys,operation).then(d=>{
								fas.common.loading();
								self.search();
								showInfoMes(d,'操作');
						});
						}
					});
				}else{
                	showWarn("所选数据中存在状态不正确的数据，不能操作");
                	return;
				}
			});
		}
		
		 getSelectedRows() {
	            return this.currentGrid.datagrid('getChecked');
	      }
		
		/**
		 * 删除
		 */
        delete() {
            let data = this.getSelectedRows();
            if (data == null || data.length == 0)
                return;
            var self = this;
            let keys = _.map(data,function(data){ return data.id; });
            let params = {ids:"'"+keys.join("','")+"'",targetStatus:8};
            this.service.selectByParams(params).then(d=>{
            	if(!d||d.length<1){
            		$.messager.confirm('删除', '确认删除所选记录?',r=> {
                        if (r) {
                            var ids = data.map(i=>i[this.primaryKey]);
                            self.service.delete(ids).then(c=> {
                                self.search();
                            });
                        }
                    });
            	}else{
            		showWarn("该数据不是启用状态，不能删除");
            		return;
            	}
            });
            
        }
		// 重写双击事件,判断是否是制单状态
		 onGridDblClickRow(grid, rowIndex, rowData) {
            this.data = {rowIndex, rowData};
            this.currentGrid.datagrid('selectRow', rowIndex);
            let key = rowData.id;
            this.service.getById(key).then(c=>{
        		if(8 == c.status) {
        			 this.edit();   	
        		} else {
        			return;
        		} 
        	});
	      }
		 
		 import(){
			 var self=this;
			 if (typeof self.importExcel == 'undefined') {
				seajs.use(['supplier_account/supplier.account.io'], function(Excel) {
					var imports = self.importExcel = new Excel.importer(1);
					imports.import();
				});
			 } else {
				 self.importExcel.import();
			 }
		 }
    }
    
    class Page extends UI.Page {
        constructor() {
            super('4010850',$('#mainPanel'));
            this.views = [new BillView()]
        }
    }
    var page = new Page();
    page.render();
});