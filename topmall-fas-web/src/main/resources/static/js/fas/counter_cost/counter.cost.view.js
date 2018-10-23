"use strict";
define(function (require, exports, module) {
    let UI = require('core/ui');
    let controls = require('core/controls');
    let counterCostService = require('./counter.cost.service.js');    
    let config = require('../config');
    let status = $.fas.datas.status;
    
    class BillView extends UI.EditListView {
        constructor() {
            super("main");
            this._service = new counterCostService();
        }
        get service() {
            return this._service;
        }
        getToolbars() {
        	 var items = ['新增', '删除','查询','重置','导出'];
        	 items = $.merge(items, [
        	                        {id: "btn-import1", iconCls: 'icon icon-download2', text: '导入竖排', value: 6, order: "5"},
        	                        {id: "btn-import2", iconCls: 'icon icon-download2', text: '导入横排', value: 6, order: "5"},
        	          				{id: "btn-confirm", iconCls: 'icon  icon-file-text', text: '确认', value: 8, order: "8"},
        	          				{id: "btn-unConfirm", iconCls: 'icon  icon-file-text2', text: '反确认', value: 9, order: "11"}
//        	          				{id: "btn-calculator", iconCls: 'icon icon-calculator', text: '费用计算', value: 1, order: "8"} 
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
						"parameterBeanValue":"1"
					}
				}, {
					"label": "结算月",
					"type": "datebox",
					"name": "settleMonth",
					"options": { "width": 150,
						"dateFmt":"yyyyMM"}
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
					"label": "费用性质",
					"type": "combocommon",
					"name": "refType",
					"options": {"width": 150, "type": "refType", "required": false, "disabled": false}
				}]
			}
		}
		getGridOptions() {
			var dataurl = portalUrl+'/mdm/data/api';
			var options = {
				id: 'grid',
				url: config.rootUrl + '/counter/cost/list',
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
					url: config.rootUrl + '/counter/cost/list',
					fileName:'专柜费用',
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
							if(row.settleStartDate!=null || row.settleEndDate!=null ){
								return row.settleStartDate+"--"+row.settleEndDate;
							}else{
								return "--";
							}
                       },
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
					}, {
						"field": "shopNo",
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
						"editor" : "readonlytext",
						"formatter":(value)=>value=="合计"?null:value
					}, {
						"field": "counterName",
						"type": "textbox",
						"title": "专柜名称",
						"$formatter": {valueField: 'counterNo', textField: 'name', type: "counter"},
						"width": 80,
						"hidden": false,
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
					}, {
						"field": "status",
						"type": "textbox",
						"title": "状态",
						"width": 80,
						"hidden": false,
						"editor" : "readonlytext",
						"formatter":(value)=>isNotBlank(value)?status.first(c=>c.id == value).name:null
					}, {
						"field": "refType",
						"type": "textbox",
						"title": "费用性质",
						"width": 80,
						"hidden": false,
						"editor" : "readonlytext",
						"formatter":(value)=>isNotBlank(value)?$.fas.datas.refType.first(c=>c.id == value).name:null
					}, {
						"field": "source",
						"type": "textbox",
						"title": "生成方式",
						"width": 90,
						"hidden": false,
						"editor" : "readonlytext",
						"formatter":(value)=>isNotBlank(value)?$.fas.datas.source.first(c=>c.id == value).name:null
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
						"width": 100,
						"hidden": false,
						"editor" : "readonlytext"
					}, {
						"field": "ableAmount",
						"type": "textbox",
						"title": "应结价款",
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
						"label":"费用项目",
						"type":"combogridmdm",
						"name":"costNo",
						"options":{
							"width":200,
							"required":true,
							"beanName": "deduction",
							"parameterBeanName":"suitName",
							"parameterBeanValue":"1"
						}
					}, {
						"name": "refType",
						"type": "combocommon",
						"label": "费用性质",
						"options":{
							"width":200,
							"required":true,
							"type":"refType"
							}
					},{
						"label":"费用金额",
						"type":"numberbox",
						"name":"costAmount",
						"options":{
							"width":200,
							"required":true,
							"precision":2
							}
					}, {
						"name": "taxFlag",
						"type": "combocommon",
						"label": "是否含税",
						"options":{
							"width":200,
							"required":true,
							"type":"taxFlag"
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
							"required":true,
							"precision":2
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
		
		   /**
         * 显示编辑面板
         */
        showEditPanel() {
            this.createEditPanel();

            let self = this;
            var title = (this.data && this.data.rowIndex >= 0) ? "编辑" : "新增";
            try{
            	this.detailForm.form('clear');
            }catch(e){
            }
            ygDialog({
                title: title,
                target: this.detailPanel,
                width: Math.max(600, this.detailControls.width),
                height: Math.max(280, this.detailControls.height),
                buttons: [{
                    text: '保存',
                    iconCls: 'icon-save',
                    handler: function () {
                        self.doSave().done(c=>{
                        	$(self.detailPanel).dialog('close');
                        	self.loadingEnd();
                    	});
                    }
                }, {
                    text: '取消',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        self.detailPanel.dialog('close');
                    }
                }]
            });
            if (this.data && this.data.rowData) {
                this.detailForm.form('load', this.data.rowData);
                
                this.sufEditControl();
            }
        }
		
		setDefaultValue(){
			$('#taxRate').numberbox('setValue', 17);
			$('#taxFlag').combobox('setValue', 1);
		}
		// 确认
		confirm(){
            let data = this.getSelectedRows();
            if (data == null || data.length == 0)
                return;
            var self = this;
            let keys = _.map(data,function(e){ return e.id; });
			let params = {ids:"'"+keys.join("','")+"'",targetStatus:1};
            this.service.selectByParams(params).then(d=>{
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
		 * 费用计算
		 */
		calculator(){
			this.showRedoPanel();
		}
		
		createRedoPanel(){
			if (this.redoPanel != null)
                return;

            let options = {
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
									$('#counterNo','#view_redo_panel_edit').combogrid('setValue','');
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
								var shopNo = $('#shopNo','#view_redo_panel_edit').combogrid('getValue');
								$('#counterNo','#view_redo_panel_edit').combogrid("grid").datagrid('options').queryParams.shopNo=shopNo;
								$('#counterNo','#view_redo_panel_edit').combogrid("options").queryParams.shopNo=shopNo;
							}
						}
        			},{
						"label":"重算结算月",
						"type":"datebox",
						"name":"settleMonth",
						"options":{
							"width":200,
							"required":true,
							"dateFmt":"yyyyMM"
							}
					},{
						"label":"重算结算期",
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
							"required":true
							}
					}
                ]
            }
            this.redoPanel = $(`<div id="view_redo_panel_edit" class="easyui-dialog" data-options="closed:true" ></div>`).appendTo('body');
            this.redoForm = this.renderer.renderControls(this.redoPanel, options.controls, 1);
		}
		
		showRedoPanel(){
			this.createRedoPanel();
			let self = this;
			this.redoForm.form('clear');
			
			ygDialog({
                title: '费用计算',
                target: this.redoPanel,
                width: 330,
                height: 300,
                buttons: [{
                    text: '生成',
                    iconCls: 'icon icon-hammer',
                    handler: function () {
                    	self.recalculation().done(c=>$(self.redoPanel).dialog('close'));
                    }
                }, {
                    text: '取消',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        self.redoPanel.dialog('close');
                    }
                }]
            });
		}
		
		recalculation(){
			let data =  this.redoForm.form('getData');
			let def;
			if(!this.redoForm.form('validate')) {
				def = $.Deferred();
	            setTimeout(()=> def.reject(), 10)
	            return def;
			}
			def = $.post(config.rootUrl + "/counter/cost/recalculation", data);
			return def.done(c=>alert(c.errorMessage));
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
		// 重写双击事件,判断是否是制单状态
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
		 
		 
		 import1(){
			 var self=this;
			 if (typeof self.importExcel == 'undefined') {
				seajs.use(['counter_cost/counter.cost.io'], function(Excel) {
					var imports = self.importExcel = new Excel.importer(1);
					imports.import();
				});
			 } else {
				 self.importExcel.import();
			 }
		 }
		 
		 import2(){
			 var self=this;
			 if (typeof self.importExcelH2V == 'undefined') {
				seajs.use(['counter_cost/counter.cost.io'], function(Excel) {
					var imports = self.importExcelH2V = new Excel.importer(2);
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