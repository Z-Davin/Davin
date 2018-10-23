"use strict";
define(function (require, exports, module) {
    let UI = require('core/ui');
    let Service = require('core/service').Service;
    let config = require('../config');
    let status = $.fas.datas.status;
    
    class PropertyCostService extends Service {
		constructor() {
			super(config.rootUrl + "/property/cost");
		}
		
		confirm(key){
        	return $.post( this.url + "/confirm", {'id': key});
        }
		
		
		getMdmData(mdmUrl,name,params){
            var data = null;
            var def = $.ajax({
                url: `${mdmUrl}/${name}/query`,
                type: 'GET',
                cache: true,
                data: params,
                async: false
            });
            def.then(d=>data = d);
            return data;
        }
    }
    
    
    class BillView extends UI.EditListView {
        constructor() {
            super("main");
            this._service = new PropertyCostService();
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
				list: 5,
				controls: [{
					"label": "地区",
					"type": "combogridmdm",
					"name": "zoneNo",
					"options": { "width": 150,
						"beanName":"zone"
					}
				},{
					"label": "公司",
					"type": "combogridmdm",
					"name": "companyNo",
					"options": { "width": 150,
						"beanName":"company",
						onShowPanelFn: function(jq) {
							var zoneNo = $('#zoneNo',this.searchForm).combogrid('getValue');
							$('#companyNo',this.searchForm).combogrid("grid").datagrid('options').queryParams.zoneNo=zoneNo;
							$('#companyNo',this.searchForm).combogrid("options").queryParams.zoneNo=zoneNo;
						}
					}
				},{
					"label": "卖场",
					"type": "combogridmdm",
				    "name": "shopNo",
				    "options": {
				        "width": 150,
				        "required": false,
				        "beanName":"shop",
						onShowPanelFn: function(jq) {
							var companyNo = $('#companyNo',this.searchForm).combogrid('getValue');
							$('#shopNo',this.searchForm).combogrid("grid").datagrid('options').queryParams.companyNo=companyNo;
							$('#shopNo',this.searchForm).combogrid("options").queryParams.companyNo=companyNo;
						}
				    }
				},{
					"label": "物业公司",
					"type": "combogridmdm",
				    "name": "mallNo",
				    "options": {
				        "width": 150,
				        "required": false,
				        "beanName":"propertyCompany",
				        "beanNo":"companyNo",
				        onShowPanelFn: function(jq) {
							var shopNo = $('#shopNo',this.searchForm).combogrid('getValue');
							$('#mallNo',this.searchForm).combogrid("grid").datagrid('options').queryParams.shopNo=shopNo;
							$('#mallNo',this.searchForm).combogrid("options").queryParams.shopNo=shopNo;
						}
				    }
				},{
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
				url: config.rootUrl + '/property/cost/list',
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
					url: config.rootUrl + '/property/cost/list'},
				enableHeaderContextMenu: true,
				enableHeaderClickMenu: true,
				emptyMsg: "暂无数据",
				columns: [
					[ {
						"field": "zoneNo",
						"type": "textbox",
						"title": "大区编码",
						"width": 75,
						"hidden": false
					},{
						"field": "zoneName",
						"type": "textbox",
						"title": "大区名称",
						"$formatter": {valueField: 'zoneNo', textField: 'name', type: "zone"},
						"width": 75,
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
						"width": 110,
						"hidden": false,
						"editor" : "readonlytext"
					}, {
						"field": "mallNo",
						"type": "textbox",
						"title": "物业公司编码",
						"width": 100,
						"hidden": false,
						"editor" : "readonlytext"
					}, {
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
						"field": "startNum",
						"type": "textbox",
						"title": "期初数",
						"width": 80,
						"align": "right",
						"editor" : "readonlytext"
					},{
						"field": "endNum",
						"type": "textbox",
						"title": "期末数",
						"width": 80,
						"align": "right",
						"editor" : "readonlytext"
					},{
						"field": "number",
						"type": "textbox",
						"title": "数量",
						"width": 80,
						"align": "right",
						"editor" : "readonlytext"
					},{
						"field": "firstRatio",
						"type": "textbox",
						"title": "系数1",
						"width": 80,
						"align": "right",
						"editor" : "readonlytext"
					},{
						"field": "secondRatio",
						"type": "textbox",
						"title": "系数2",
						"width": 80,
						"align": "right",
						"editor" : "readonlytext"
					}, {
						"field": "status",
						"type": "textbox",
						"title": "状态",
						"width": 80,
						"hidden": false,
						"editor" : "readonlytext",
						"formatter": function(value, row, index){return value ? status.first(c=>c.id == value).name : "";}
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
			var self = this;
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
							var shopNo = $('#shopNo','#view_main_panel_edit').combogrid('getValue');
							$('#costNo','#view_main_panel_edit').combogrid("grid").datagrid('options').queryParams.shopNo=shopNo;
							$('#costNo','#view_main_panel_edit').combogrid("options").queryParams.shopNo=shopNo;
							var mallNo = $('#mallNo','#view_main_panel_edit').combogrid('getValue');
							$('#costNo','#view_main_panel_edit').combogrid("grid").datagrid('options').queryParams.mallNo=mallNo;
							$('#costNo','#view_main_panel_edit').combogrid("options").queryParams.mallNo=mallNo;
							var bunkGroupNo = $('#bunkGroupNo','#view_main_panel_edit').combogrid('getValue');
							$('#costNo','#view_main_panel_edit').combogrid("grid").datagrid('options').queryParams.bunkGroupNo=bunkGroupNo;
							$('#costNo','#view_main_panel_edit').combogrid("options").queryParams.bunkGroupNo=bunkGroupNo;
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
					"label":"期初数",
					"type":"numberbox",
					"name":"startNum",
					"options":{
						"width":200,
						"required":false
					}
				},{
					"label":"期末数",
					"type":"numberbox",
					"name":"endNum",
					"options":{
						"width":200,
						"required":false,
						onChange : function(newValue,oldValue){
							let startNum = $('#startNum','#view_main_panel_edit').numberbox('getValue');
							let endNum =  newValue;
							if(isNotBlank(startNum) && isNotBlank(endNum)){
								let number = endNum - startNum;
								$('#number','#view_main_panel_edit').numberbox('setValue', number);
							}
						}
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
					"label":"系数1",
					"type":"numberbox",
					"name":"firstRatio",
					"options":{
						"width":200,
						"required":true,
						"precision":2
					}
				},{
					"label":"系数2",
					"type":"numberbox",
					"name":"secondRatio",
					"options":{
						"width":200,
						"required":true,
						"precision":2
					}
				},{
					"label":"备注",
					"type":"textbox",
					"name":"remark",
					"options":{
						"width":200,
						"required":false
					}
				}]
			};
		}
		
		getEntity() {
            var data = super.getEntity();
            data.status = 1;
            return data;
        }
		
		// 确认
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
        			showWarn("请选择未生效状态的记录审核");
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
        
		// 重写双击事件,判断是否是制单状态
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
				seajs.use(['property_cost/property.cost.io'], function(Excel) {
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
            super('4010720',$('#mainPanel'));
            this.views = [new BillView()]
        }
    }
    var page = new Page();
    page.render();
    
    module.exports = PropertyCostService;
});