"use strict";
define(function (require, exports, module) {
    let UI = require('core/ui');
    let controls = require('core/controls');
    let shopCostService = require('./shop.cost.service.js');    
    let config = require('../config');
    let status = $.fas.datas.status;
    
    class BillView extends UI.EditListView {
        constructor() {
            super("main");
            this._service = new shopCostService();
        }
        get service() {
            return this._service;
        }
        getToolbars() {
        	 var items = ['新增', '删除','查询','重置','导出'];
        	 items = $.merge(items, [
        		 		{id: "btn-import", iconCls: 'icon icon-download2', text: '导入', value: 6, order: "5"},
          				{id: "btn-confirm", iconCls: 'icon  icon-file-text', text: '审核', value: 10, order: "8"}
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
					"options": { "width": 150,
						"beanName":"zone"}
				},{
					"label": "公司",
					"type": "combogridmdm",
					"name": "companyNo",
					"options": { "width": 150,
						"beanName":"company"}
				},{
					"label": "卖场",
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
						}
					}
				}, {
					"label": "结算月",
					"type": "datebox",
					"name": "settleMonth",
					"options": { "width": 150,
						"dateFmt":"yyyyMM"}
				}]
			}
		}
		getGridOptions() {
			var dataurl = portalUrl+'/mdm/data/api';
			var self = this;
			var options = {
				id: 'grid',
				url: config.rootUrl + '/shop/cost/list',
				title: "",
				height: "670",
				loadMsg: '请稍等,正在加载...',
				iconCls: 'icon-ok',
				pageSize: "150",
				pageList: [20, 50, 100, 200],
				checkOnSelect: false,
				pagination: true,
				fitColumns: false,
				singleSelect: true,
				rownumbers: true,
				export: {async: true,
					method:"wss",
					url: config.rootUrl + '/bill/counter/balance/list'
				},
				enableHeaderContextMenu: true,
				enableHeaderClickMenu: true,
				emptyMsg: "暂无数据",
				columns: [
					[ {
						"field": "zoneNo",
						"type": "textbox",
						"title": "大区",
						"width": 80,
						"hidden": false
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
						"$formatter": {valueField: 'shopNo', textField: 'name', type: "shop"},
						"width": 80,
						"hidden": false,
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
						"$formatter": {valueField: 'counterNo', textField: 'name', type: "counter"},
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
                            return row.settleStartDate+"--"+row.settleEndDate;
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
						"field": "companyNo",
						"type": "textbox",
						"title": "公司编码",
						"width": 80,
						"hidden": false,
						"editor" : "readonlytext"
					},{
						"field": "number",
						"type": "textbox",
						"title": "数量",
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
						"formatter":(value)=>status.first(c=>c.id == value).name
					}, {
						"field": "remark",
						"type": "textbox",
						"title": "备注",
						"width": 80,
						"hidden": false,
						"editor" : "readonlytext"
					}, {
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
					}, {
						"field": "createUser",
						"type": "textbox",
						"title": "创建人",
						"width": 80,
						"hidden": false,
						"editor" : "readonlytext"
					}, {
						"field": "createTime",
						"type": "textbox",
						"title": "创建时间",
						"width": 80,
						"hidden": false,
						"editor" : "readonlytext"
					}, {
						"field": "auditor",
						"type": "textbox",
						"title": "审核人",
						"width": 80,
						"hidden": false,
						"editor" : "readonlytext"
					}, {
						"field": "auditorTime",
						"type": "textbox",
						"title": "审核时间",
						"width": 80,
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
							},
							onHidePanelFn: function(rec) {
								if(rec){
									$('#costNo','#view_main_panel_edit').combogrid('setValue','');
								}
							}
						}
					},{
						"label":"扣项",
						"type":"combogridmdm",
						"name":"costNo",
						"options":{
							"width":200,
							"required":true,
							"url":config.mcs+"/counter/contract/other/query?conStatus=1&chargingType=3",
							"beanName":"cost",
							"cnName":"costName",
							onShowPanelFn: function(jq) {
								var counterNo = $('#counterNo','#view_main_panel_edit').combogrid('getValue');
								$('#costNo','#view_main_panel_edit').combogrid("grid").datagrid('options').queryParams.counterNo=counterNo;
								$('#costNo','#view_main_panel_edit').combogrid("options").queryParams.counterNo=counterNo;
							}
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
						"type":"settlementDateBox",
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
						"label":"数量",
						"type":"numberbox",
						"name":"number",
						"options":{
							"width":200,
							"required":true
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
		
		//确认
		confirm(){
            let data = this.getSelectedRows();
            if (data == null || data.length == 0)
                return;
            var self = this;
            let key = data[0].id;
            this.service.getById(key).then(c=>{
        		if(1 == c.status) {
        			$.messager.confirm("确认", "确定要确认当前单据吗？",function (r) {
                        if (r) {
                        	fas.common.loading("show", "正在处理中......");
                        	self.service.confirm(key).then(d=>{
                        		fas.common.loading();
                    			self.search();
                    			showSuc('确认成功！');
                        	});
                        }
                    });
        		} else {
        			showWarn("该数据不是制单状态,不能确认..");
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
        			alert("请选择未生效状态的");
        			return;
        		} 
        	});
	      }
		 
		 import(){
			 var self=this;
			 if (typeof self.importExcel == 'undefined') {
					seajs.use(['shop_cost/shop.cost.io'], function(Excel) {
						var imports = self.importExcel = new Excel.importer();
						imports.import();
					});
				} else {
					self.importExcel.import();
				}
		 }
    }
    
    
    class Page extends UI.Page {
        constructor() {
            super('4010450',$('#mainPanel'));
            this.views = [new BillView()]
        }
    }
    var page = new Page();
    page.render();
});