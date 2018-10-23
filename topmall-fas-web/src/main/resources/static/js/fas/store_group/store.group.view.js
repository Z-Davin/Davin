"use strict";
define(function (require, exports, module) {
	let UI = require('core/ui');
    let controls = require('core/controls');
    let config = require('../config');
    let Service = require('core/service').Service;
    let status = $.fas.datas.status;
    
    class StoreGroupService extends Service {
    	constructor() {
    		 super(config.rootUrl + "/store/group");
    	}
    	confirm(key){
        	return $.post( this.url + "/confirm", {'id': key});
        }
    	
    	findByParam(param){
        	return $.ajax({
        			url:this.url + "/get",
        			data:param,
        			async:false,
        			type:'get'
        		});
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
    module.exports = StoreGroupService;
    
    
    class StoreGroupView extends UI.EditListView {
        constructor() {
            super("main");
            this._service = new StoreGroupService();
        }
        get service() {
            return this._service;
        }
        getToolbars() {
       	 var items = ['新增','查询','重置','导入','导出'];
       	items = $.merge(items, [
    	          				{id: "btn-confirm", iconCls: 'icon  icon-file-text', text: '启用', value: 1, order: "8"},
    	          				{id: "btn-monthBalance", iconCls: 'icon  icon-alarm', text: '停用', value: 1, order: "8"}
    	                ]);
            return {
                id: 'toolbar',
                data: items
            }
       }
        
		getSearchControls() {
			return {
				list: 3,
				controls: [{
					"label": "公司",
					"type": "combogridmdm",
					"name": "companyNo",
					"options": { 
						"width": 150,
						"beanName":"company"
					}
				},{
					"label": "卖场组名称",
					"type": "combogridfas",
				    "name": "shopNo",
				    "options": {
				        "width": 150,
				        "required": false,
				        "beanName":"storeGroup",
				        "urlBeanName":"store/group"
				    }
				},{
					"label": "业务类型",
					"type": "combobox",
				    "name": "bizType",
				    "options": {
				    	"valueField": 'id',
						"textField": 'name',
						"data": $.fas.datas.storeGroupType
				    }
				} ]
			}
		}
		getGridOptions() {
			var dataurl = portalUrl+'/mdm/data/api';
			var self = this;
			var options = {
				id: 'grid',
				url: config.rootUrl + '/store/group/list',
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
					method:"ws"},
				enableHeaderContextMenu: true,
				enableHeaderClickMenu: true,
				emptyMsg: "暂无数据",
				columns: [
		                    [{
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
		                        "width": 80,
		                        "hidden": false,
		                        "editor" : "readonlytext"
		                    }, {
		                        "field": "storeGroupNo",
		                        "type": "textbox",
		                        "title": "卖场组编码",
		                        "width": 80,
		                        "hidden": false,
		                        "editor" : "readonlytext"
		                    }, {
		                        "field": "storeGroupName",
		                        "type": "textbox",
		                        "title": "卖场组名称",
		                        "width": 80,
		                        "hidden": false,
		                        "editor" : "readonlytext"
		                    }, {
		                    	"field": "bizType",
								"type": "textbox",
								"title": "业务类型",
								"width": 80,
								"hidden": false,
								"editor" : "readonlytext",
								"formatter":(value)=>$.fas.datas.storeGroupType.first(c=>c.id == value).name
		                    },{
		                        "field": "effectiveTime",
		                        "type": "textbox",
		                        "title": "生效日期",
		                        "width": 80,
		                        "hidden": false,
		                        "editor" : "readonlytext",
		                    },{
		                        "field": "expiredTime",
		                        "type": "textbox",
		                        "title": "终止日期",
		                        "width": 80,
		                        "hidden": false,
		                        "editor" : "readonlytext",
		                    }, {
		                    	"field": "status",
								"type": "textbox",
								"title": "状态",
								"width": 80,
								"hidden": false,
								"editor" : "readonlytext",
								"formatter":(value)=>$.fas.datas.status.first(c=>c.id == value).name
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
		                        "title": "创建日期",
		                        "width": 130,
		                        "hidden": false,
		                        "editor" : "readonlytext"
		                    }, {
		                        "field": "updateUser",
		                        "type": "textbox",
		                        "title": "修改人",
		                        "width": 80,
		                        "hidden": false,
		                        "editor" : "readonlytext"
		                    }, {
		                        "field": "updateTime",
		                        "type": "textbox",
		                        "title": "修改日期",
		                        "width": 130,
		                        "hidden": false,
		                        "editor" : "readonlytext"
		                    }]
				]
			};
			$.gridFormat(dataurl, options);
				return [options];   
		}
		
		
		getDetailControls(){		
			return{
				controls:[{
							"label":"公司编码",
							"type":"combogridmdm",
							"name":"companyNo",
							"options":{
								"width":200,
								"required":true,
								"editable":false,
								"beanName":"company",
								"onHidePanelFn":function(data){
									alert(data.name);
								}
							}
		                }, {
							"label":"公司名称",
							"type":"textbox",
							"name":"companyName",
							"options":{
								"width":200,
								"disabled":true
							}
		                }, {
		                	"label":"卖场组编码",
							"type":"textbox",
							"name":"storeGroupNo",
							"options":{
								"width":200,
								"required":false
							}
		                }, {
		                	"label":"卖场组名称",
							"type":"textbox",
							"name":"storeGroupName",
							"options":{
								"width":200,
								"required":false
							}
		                },{
							"label":"生效日期",
							"type":"datebox",
							"name":"effectiveTime",
							"options":{
								"width":200,
								"required":true
							}
						},{
							"label":"终止日期",
							"type":"datebox",
							"name":"expiredTime",
							"options":{
								"width":200,
								"required":true
							}
						},{
							"label": "业务类型",
							"type": "combobox",
						    "name": "bizType",
						    "options": {
						    	"width":200,
						    	"valueField": 'id',
								"textField": 'name',
								"data": $.fas.datas.storeGroupType
						    }
						}]
			}
		}
		
		getDialog() {
			
		}
    
		validateDetail() {
			var validateFlag = this.detailForm.form('validate');
			if(validateFlag){
				var params = this.getDetail();
				$.ajax({
			        url: config.rootUrl + '/store/group/validateCreate',
			        async: false,
			        data : params.rowData,
			        success: function (result) {
			        }
				})
			}
			return validateFlag;
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
                        	self.service.confirm(key).then(d=>{
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
		 
		 import(){
			 var self=this;
			 if (typeof self.importExcel == 'undefined') {
					seajs.use(['store_group/store.group.io'], function(Excel) {
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
            super('4010130',$('#mainPanel'));
            this.views = [new StoreGroupView()]
        }
    }
    var page = new Page();
    page.render();
});