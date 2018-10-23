"use strict";
define(function(require, exports, module) {
	let UI = require('core/ui');
	let config = require('../config');
	let mallPrepayService = require('./mall.prepay.service');
	class BillDetail extends UI.BillView {
		constructor() {
			super("main");
			this.title = "单据明细";
			this._service = new mallPrepayService();
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
						id: "btn-save",
						iconCls: 'icon  icon-floppy-disk',
						text: '保存',
						value: 2
					}
				]
			}
		}
		
		getFootToolbars(){
			var items = ["新增", "删除"] ;
            var item2 = [{id: "btn-saveDetail", iconCls: 'icon  icon-floppy-disk', text: '保存', value: 1, order: "7"}];
            items = $.merge(items, item2);
            
			return [{
                id: 'toolbar',
                data:items
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
						onShowPanelFn: function(jq) {
							var shopNo = $('#shopNo','#main_top_panel').combogrid('getValue');
							var mallNo = $('#mallNo','#main_top_panel').combogrid('getValue');
							$('#bunkGroupNo','#main_top_panel').combogrid("grid").datagrid('options').queryParams.shopNo=shopNo;
							$('#bunkGroupNo','#main_top_panel').combogrid("options").queryParams.shopNo=shopNo;
							$('#bunkGroupNo','#main_top_panel').combogrid("grid").datagrid('options').queryParams.companyNo=mallNo;
							$('#bunkGroupNo','#main_top_panel').combogrid("options").queryParams.companyNo=mallNo;
						}
					}
				},{
					"label": "付款月",
					"type": "datebox",
					"name": "payMonth",
					"options": {
						"width": 150,
						"required": true,
						"dateFmt":"yyyyMM"
						}
				}, {
					"label": "开始日期",
					"type": "datebox",
					"name": "startDate",
					"options": { "width": 150,
						"required": true,
						"dateFmt":"yyyy-MM-dd",
						"maxDate":"endDate"
						}
				}, {
					"label": "结束日期",
					"type": "datebox",
					"name": "endDate",
					"options": { "width": 150,
						"required": true,
						"dateFmt":"yyyy-MM-dd",
						"minDate":"startDate"
						}
				}, {
					"label": "预开票总额",
					"type": "numberbox",
					"name": "prepayAmount",
					"options": { "width": 150,
						"precision":2
						}
				},{
					"label": "票扣",
					"type": "combocommon",
					"name": "billDebit",
					"options": { 
						"width": 150,
						"type":"billDebit",
						"required":true,
					}
				},{
					"label": "备注",
					"type": "textbox",
					"name": "remark",
					"colspan":"2",
					"options": { "width": 565 }
				}]
			}
		}
		
		getGridOptions() {
			var dataurl = portalUrl+'/mdm/data/api';
			var options =[{
				id: 'grid1',
				url: config.rootUrl + '/mall/prepay/dtl/list',
				title: "预付明细",
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
					[ {
						"field": "costNo",
						"type": "textbox",
						"title": "扣项类型 ",
						"width": 150,
						"hidden": false,
						"editor" : {
							type : 'combobox',
							options:{
							    required:true,
							    valueField:'deductionNo',
							    textField:'name',
							    url:`${APP_SETTINGS.mdm}/data/api/deduction/query?1=1&suitName=2`
							}
						},
						"$formatter": {valueField: 'costNo', textField: 'name', type: 'deduction'}
					},{
						"field": "settleMonth",
						"type": "textbox",
						"title": "结算月",
						"width": 100,
						"hidden": false,
						"editor" :{
//							type : 'mallBalanceDateDtlEditor',
							type:"datebox",
							options : {
//								getMallNo:true,
//								getShopNo:true,
//								getBunkGroupNo:true,
								isRequired : true,
								"dateFmt":"yyyyMM"
							}
						}
					},
//					{
//						"field": "settleStartDate",
//						"type": "textbox",
//						"title": "结算期起",
//						"width": 120,
//						"hidden": false,
//						"editor" : "readonlytext"
//					},{
//						"field": "settleEndDate",
//						"type": "textbox",
//						"title": "结算期止",
//						"width": 120,
//						"hidden": false,
//						"editor" : "readonlytext"
//					},
					{
						"field": "conAmount",
						"type": "textbox",
						"title": "合同金额",
						"width": 80,
						"hidden": false,
						"editor" :{type:"numberbox",
							"options":{
								"precision":2,
								"required":true
							}
						}
					},{
						"field": "prepayAmount",
						"type": "textbox",
						"title": "预付金额",
						"width": 80,
						"hidden": false,
						"editor" :{type:"numberbox",
							"options":{
								"precision":2,
								"required":true
							}
						}
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
        	formObj.find('#status,#prepayAmount').combobox('disable');
        	if(status=='0'){
        		formObj.find('#remark').next().find('input').attr("readOnly", false).removeClass("readonly");
        	}
	     };
	     unLockHeader(){
	    	 let formObj = this.searchForm;
	    	 formObj.find(".easyui-combobox").combobox('enable');
	    	 formObj.find(".easyui-combogridmdm").combobox('enable');
	    	 formObj.find(".easyui-datebox").datebox('enable');
	    	 formObj.find("input").attr("readOnly", false).removeClass("readonly");
	    	 formObj.find('#status,#prepayAmount').combobox('disable');
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
			var $dg = $("#view_grid_grid1");
			formData.insertMallPrepayDtlList = $dg.datagrid('getChanges', "inserted");
			formData.updateMallPrepayDtlList = $dg.datagrid('getChanges', "updated");
			formData.deleteMallPrepayDtlList = $dg.datagrid('getChanges', "deleted");
         	return formData;
		}
		
		// 保存单据
		save(){
			var self = this;
			if(!this.searchForm.form('validate')){
				return;
			}
			if(!this.endEdit($("#view_grid_grid1"))){
				return;
			}
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
			this.service.save({'mallPrepay':JSON.stringify(self.getBillDate())}).then(d=>{
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
				url:`${config.rootUrl}/mall/prepay/dtl/list?billNo=${billNo}`,
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
    				if(self.endEdit(this.currentGrid)){
    					self.save();
    				}
    			}
			})
		};
		
		endEdit(grid) {
			var rows = grid.datagrid('getRows');
            for ( var i = 0; i < rows.length; i++) {
            	if(!grid.datagrid('validateRow',i)){
            		return false;
            	}
            	grid.datagrid('endEdit', i);
            }
            return true;
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
	            let mainFootPanelH = $(`#${this.viewId}_foot_panel`).height();
	            let mainCenterPanelH = sublayerH - topPanelH - mainFootPanelH-20;
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
				}, {
					"label": "状态",
					"type": "combocommon",
					"name": "status",
					"options": { 
						"width": 150,
						"type":"status"
					}
				}, {
					"label": "付款月",
					"type": "datebox",
					"name": "payMonth",
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
				url: config.rootUrl + '/mall/prepay/list',
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
					url: config.rootUrl + '/mall/prepay/list'},
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
						"type": "datebox",
						"field": "bunkGroupNo",
						"width": 100
					}, {
						"title": "预付金额",
						"type": "datebox",
						"field": "prepayAmount",
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
						"title": "付款月",
						"type": "datebox",
						"field": "payMonth",
						"width": 100
					}, {
						"title": "开始日期",
						"type": "datebox",
						"field": "startDate",
						"width": 100
					}, {
						"title": "结束日期",
						"type": "datebox",
						"field": "endDate",
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
			super('4010860', $('#mainPanel'));
			this.views = [new BillDetail(),new SearchBill()]
		}
	}

	var page = new Page();

	page.render();
});