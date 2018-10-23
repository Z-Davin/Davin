"use strict";
define(function(require, exports, module) {
	let UI = require('core/ui');
	let config = require('../config');
	let printer = require('./printCounterBalance');
    let exporter = require('./exportCounterBalance');
	let counterService = require('./counter.balance.service');
	let JSZip = require("core/io//excel/jszip");
	class BillDetail extends UI.BillView {
		constructor() {
			super("main");
			this.title = "单据明细";
			this._service = new counterService();
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
					},{
						id: "btn-newBill",
						iconCls: 'icon icon-plus',
						text: '新单',
						value: 2
					},{
						id: "btn-deleteBill",
						iconCls: 'icon  icon-bin',
						text: '删单',
						value: 4
					},{
						id: "btn-verify",
						iconCls: 'icon  icon-profile',
						text: '审核',
						value: 10
					},{
						id: "btn-unVerify",
						iconCls: 'icon  icon-file-empty',
						text: '反审核',
						value: 11
					},{
						id: "btn-save",
						iconCls: 'icon  icon-floppy-disk',
						text: '保存',
						value: 3
					},{
						id: "btn-batchCreate",
						iconCls: 'icon  icon-plus',
						text: '批量生成',
						value: 2
					},
					{
						id: "btn-print",
						iconCls: 'icon icon-printer',
						text: '打印',
						value: 7
					},{
                        id: "btn-table2Excel",
                        iconCls: 'icon  icon-upload2',
                        text: '导出',
                        value: 5
                    },{
                        id: "btn-exportPdf",
                        iconCls: 'icon  icon-upload2',
                        text: '导出PDF',
                        value: 5
                   }
				]
			}
		}
		
		print(){			
			let billNo = $("#billNo").textbox('getValue');
			if(billNo == ""){
				showWarn('未生成单据信息,无法打印');
				return false;
			}
			let templateType = $("#templateType").combobox('getValue');
			if(templateType == ""){
				showWarn('请选择打印模板类型');
				return false;
			}
			this.service.getBillInfo(billNo,templateType).then(d=>{
				if(d.errorMessage!=null){
					showInfoMes(d,'打印');
					return;
				}
				d.templateType = templateType;
				this.printer = new printer(d);
	        	this.printer.print(d);//结算单打印.
	        	this.service.printCount(billNo);
			});		
		}
		exportPdf(){
			let billNo = $("#billNo").textbox('getValue');
			if(billNo == ""){
				showWarn('未生成单据信息,无法导出');
				return false;
			}
			let templateType = $("#templateType").combobox('getValue');
			if(templateType == ""){
				showWarn('请选择打印模板类型');
				return false;
			}
            this.service.getBillInfo(billNo,templateType).then(d=>{
            	if(d.errorMessage!=null){
            		showInfoMes(d,'导出');
					return;
            	}else{
            		d.templateType = templateType;
    				this.printer = new printer(d);
    				let html=this.printer.prePdf(d);
    				var zip = new JSZip();
    				zip.file("data.txt",html);
    				var expected = zip.generate({compression: "DEFLATE"});
    				getPdf(billNo,expected,config.rootUrl +'/bill/counter/balance/getPdf');
            	}
			});
		}
		
        table2Excel(){
        	let billNo = $("#billNo").textbox('getValue');
			if(billNo == ""){
				showWarn('未生成单据信息,无法打印');
				return false;
			}
			let templateType = $("#templateType").combobox('getValue');
			if(templateType == ""){
				showWarn('请选择打印模板类型');
				return false;
			}
            this.service.getBillInfo(billNo,templateType).then(d=>{
            	if(d.errorMessage!=null){
            		showInfoMes(d,'导出');
					return;
            	}else{
            		d.templateType = templateType;
    				this.exporter = new exporter(d);
    				this.exporter.export2Excel(d);//结算单导出.
            	}
			});
        }
		
		getFootToolbars(){
			var item = ["新增", "删除"] ;
            var items = $.merge(item, 
                [{id: "btn-saveDetail", iconCls: 'icon  icon-floppy-disk', text: '保存', value: 1, order: "7"},
                {id: "btn-cancel", iconCls: 'icon  icon-cancel-circle', text: '取消', value: 1, order: "8"}]
            );
			return [{
                id: 'toolbar1',
                data:[]
            },{
                id: 'toolbar2',
                data:items
            }]
		}
		getDetailControls(){		
			return{
				controls:[{
						"label":"公司",
						"type":"combogridmdm",
						"name":"companyNo",
						"options":{
							"width":200,
							"required":true,
							"beanName":"company",
							onHidePanelFn: function(rec) {
								if(rec){
									$('#shopNo','#view_main_panel_edit').combogrid('setValue','');
								}
							}
						}
					},{
						"label":"卖场",
						"type":"combogridmdm",
						"name":"shopNo",
						"options":{
							"width":200,
							"beanName":"shop",
							onShowPanelFn: function(jq) {
								var companyNo = $('#companyNo','#view_main_panel_edit').combogrid('getValue');
								$('#shopNo','#view_main_panel_edit').combogrid("grid").datagrid('options').queryParams.companyNo=companyNo;
								$('#shopNo','#view_main_panel_edit').combogrid("options").queryParams.companyNo=companyNo;
							},
							onHidePanelFn: function(rec) {
								if(rec){
									$('#counterNo','#view_main_panel_edit').combogrid('setValue','');
								}
							}
						}
					},{
						"label":"供应商名称",
						"type":"combogridmdm",
						"name":"supplierNo",
						"options":{
							"width":200,
							"beanName":"supplier",
							"valueFeild":"supplierNo",
							onHidePanelFn: function(rec) {
								if(rec){
									$('#counterNo','#view_main_panel_edit').combogrid('setValue','');
								}
							}
						}
					},{
						"label":"专柜名称",
						"type":"combogridmdm",
						"name":"counterNo",
						"options":{
							"width":200,
							"beanName":"counter",
							"valueFeild":"counterNo",
							onShowPanelFn: function(jq) {
								var shopNo = $('#shopNo','#view_main_panel_edit').combogrid('getValue');
								$('#counterNo','#view_main_panel_edit').combogrid("grid").datagrid('options').queryParams.shopNo=shopNo;
								$('#counterNo','#view_main_panel_edit').combogrid("options").queryParams.shopNo=shopNo;
								var supplierNo = $('#supplierNo','#view_main_panel_edit').combogrid('getValue');
								$('#counterNo','#view_main_panel_edit').combogrid("grid").datagrid('options').queryParams.supplierNo=supplierNo;
								$('#counterNo','#view_main_panel_edit').combogrid("options").queryParams.supplierNo=supplierNo;
							}
						}
					},{
						"label":"合作性质",
						"type":"combocommon",
						"name":"source",
						"options":{
							"width":200,
							"type":"businessType"
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
					}]
			}
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
								$('#counterNo','#main_top_panel').combogrid('setValue','');
							}
						}
					}
				}, {
					"label": "供应商",
					"type": "combogridmdm",
				    "name": "supplierNo",
				    "options": {
				        "width": 150,
				        "beanName":"supplier",
				        "valueFeild":"supplierNo",
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
					"options": { 
						"width": 150,
						"beanName":"counter",
						"valueFeild":"counterNo",
						onShowPanelFn: function(jq) {
							var shopNo = $('#shopNo','#main_top_panel').combogrid('getValue');
							var supplierNo = $('#supplierNo','#main_top_panel').combogrid('getValue');
							if(supplierNo){
								$('#counterNo','#main_top_panel').combogrid("grid").datagrid('options').queryParams.supplierNo=supplierNo;
								$('#counterNo','#main_top_panel').combogrid("options").queryParams.supplierNo=supplierNo;
							}
							$('#counterNo','#main_top_panel').combogrid("grid").datagrid('options').queryParams.shopNo=shopNo;
							$('#counterNo','#main_top_panel').combogrid("options").queryParams.shopNo=shopNo;
						}
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
					"label": "结算月",
					"type": "datebox",
					"name": "settleMonth",
					"options": { "width": 150,
						"required": true,
						"dateFmt":"yyyyMM"
						}
				}, {
					"label": "开始日期",
					"type": "settlementDateBox",
					"name": "settleStartDate",
					"options": { "width": 150,
						"valueFeild":"settleStartDate"
						}
				}, {
					"label": "结束日期",
					"type": "datebox",
					"name": "settleEndDate",
					"options": { "width": 150,
						"disabled":true
						}
				}, {
					"label": "<a href='javascript:showExplain(\"应结款总额=货款汇总.{销售总额-销售提成+分摊金额} - 扣项明细.{账扣标识为账扣}\");' class='l-btn-text icon-xq l-btn-icon-left'></a>应结款总额",
					"type": "textbox",
					"name": "ableSum",
					"options": { "width": 150,
						"disabled":true,
						}
				}, {
					"label": "预结款总额",
					"type": "numberbox",
					"name": "preAbleSum",
					"options": { "width": 150,
						"precision":2
						}
				}, {
					"label": "未结款总额",
					"type": "textbox",
					"name": "notAbleSum",
					"options": { "width": 150,
						"disabled":true
						}
				}, {
					"label": "<a href='javascript:showExplain(\"应开票总额=货款汇总.{销售总额-销售提成+分摊金额} - 扣项明细.{票扣标识为票扣且账扣标识为账扣}\");' class='l-btn-text icon-xq l-btn-icon-left'></a>应开票总额",
					"type": "textbox",
					"name": "ableBillingSum",
					"options": { "width": 150,
						"disabled":true
						}
				}, {
					"label": "预开票总额",
					"type": "numberbox",
					"name": "preBillingSum",
					"options": { "width": 150,
						"required":false,
						"precision":2
						}
				}, {
					"label": "未开票总额",
					"type": "textbox",
					"name": "notBillingSum",
					"options": { "width": 150,
						"disabled":true
						}
				}, {
					"label": "备注",
					"type": "textbox",
					"name": "remark",
					"colspan":"2",
					"options": { "width": 565 }
				},{
					"label":"打印模板类型",
					"type":"combocommon",
					"name":"templateType",
					"options": { 
						"width": 150,
						"type":"templateType",
						defaultValue:"0"
					}
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
				url: config.rootUrl + '/counter/sale/cost/list',
				title: "货款汇总",
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
						"field": "counterNo",
						"type": "textbox",
						"title": "专柜编码",
						"width": 100,
						"hidden": false
					},{
						"field": "divisionNo",
						"type": "textbox",
						"title": "部类编码",
						"width": 80,
						"hidden": false
					},{
						"field": "rateValue",
						"type": "textbox",
						"title": "商品扣率%",
						"width": 120,
						"hidden": false
					},{
						"field": "raxRate",
						"type": "textbox",
						"title": "税率%",
						"width": 80,
						"hidden": false
					},{
						"field": "settleSum",
						"type": "textbox",
						"title": "销售总额",
						"width": 80,
						"hidden": false
					},{
						"field": "sellingCost",
						"type": "textbox",
						"title": "销售成本",
						"width": 80,
						"hidden": false
					}, {
						"field": "discountAmount",
						"type": "textbox",
						"title": "折扣金额",
						"width": 80,
						"hidden": false
					}, {
						"field": "absorptionAmount",
						"type": "textbox",
						"title": "分摊金额",
						"width": 80,
						"hidden": false
					}, {
						"field": "profitAmount",
						"type": "textbox",
						"title": "销售提成",
						"width": 80,
						"hidden": false
					}]
				]
			},{
				id: 'grid2',
				url: config.rootUrl + '/counter/cost/list',
				title: "扣项明细",
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
						"field": "counterNo",
						"type": "textbox",
						"title": "专柜编码",
						"width": 100,
						"hidden": false,
						"editor" :{
							type : 'counterCostEditor',
							options : {
								getCounterNo:true,
								getSettleMonth:true,
								getSettleStartDate:true,
								getSettleEndDate:true,
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
						"title": "扣项总额",
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
        		formObj.find('#remark,#preBillingSum,#preAbleSum,#templateType').next().find('input').attr("readOnly", false).removeClass("readonly");
        	}
        	formObj.find('#templateType').next().find('input').attr("readOnly", false).removeClass("readonly");
        	formObj.find('#templateType').combocommon('enable');        	
	     };
	     unLockHeader(){
	    	 let formObj = this.searchForm;
	    	 formObj.find(".easyui-combobox").combobox('enable');
	    	 formObj.find(".easyui-combogridmdm").combobox('enable');
	    	 formObj.find(".easyui-datebox").datebox('enable');
	    	 formObj.find(".easyui-settlementDateBox").combobox('enable');
	    	 formObj.find("input").attr("readOnly", false).removeClass("readonly");
	    	 formObj.find('#status,#companyNo,#businessType').combobox('disable');
	     }
	     batchCreate() {
	            this.data = null;
	            this.showEditPanel();
	    }
	     
        /**
         * 显示编辑面板
         */
        showEditPanel() {
            this.createEditPanel();
            let self = this;
            this.detailForm.form('clear');
            ygDialog({
                title: "批量新增",
                target: this.detailPanel,
                width: Math.max(600, this.detailControls.width),
                height: Math.max(280, this.detailControls.height),
                buttons: [{
                    text: '确定',
                    iconCls: 'icon-save',
                    handler: function () {
                        self.doBatchCreate();
                    }
                }, {
                    text: '取消',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        self.detailPanel.dialog('close');
                    }
                }]
            });
        }
        /**
         * 批量生成方法
         */
        doBatchCreate() {
        	let self = this;
            var data = this.getDetail();
            if (!this.validateDetail()) {
                return false;
            }
            fas.common.loading("show", "正在处理中......");
            this.service.batchCreate({'batchCounterBalanceDto':JSON.stringify(data.rowData)}).then(d=>{
            	fas.common.loading();
            	if(d.errorCode=="0000"){
            			$(self.detailPanel).dialog('close')
        			}
        		})
        }
		
		searchBill(){
			this.page.mainTab.tabs('select', 1);
		}
		
		
		// 新单
		newBill(){
			// 清空表头
			try{
				this.searchForm.form('clear');
			}catch(e){
				
			}
			this.unLockHeader();
			// 将表格的数据清空
			$('#view_grid_grid1').datagrid('loadData',{total:0,rows:[]});
			$('#view_grid_grid2').datagrid('loadData',{total:0,rows:[]});
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
			formData.insertedDtlList = $dg.datagrid('getChanges', "inserted");
        	formData.deletedDtlList = $dg.datagrid('getChanges', "deleted");
        	formData.updatedDtlList = $dg.datagrid('getChanges', "updated");
         	return formData;
		}
		
		// 保存单据
		save(){
			var self = this;
			if(!this.searchForm.form('validate')){
				return;
			}
			this.endEdit($("#view_grid_grid2"));
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
			this.service.save({'billCounterBalance':JSON.stringify(self.getBillDate())}).then(d=>{
				fas.common.loading();
				if("0000" == d.errorCode){
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
				url:`${config.rootUrl}/counter/sale/cost/list?balanceBillNo=${billNo}`,
			});
			$("#view_grid_grid2").datagrid({
				url:`${config.rootUrl}/counter/cost/list?balanceBillNo=${billNo}`,
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
			var key = $("#id").val();
			if(!isNotBlank(key)){
				return false;
			}
			this.service.getById(key).then(c=>{
				if(c.status==0){
					this.rowIndex = rowIndex;
					this.endEdit(grid);
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
			this._service = new counterService();
		}
		
		get service(){
			return this._service;
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
						value: 5
					},
					{
						id: "btn-exportBalance",
						iconCls: 'icon  icon-upload2',
						text: '导出结算单',
						value: 5
					},
					{
						id: "btn-batchVerify",
						iconCls: 'icon  icon-profile',
						text: '审核',
						value: 10
					},{
						id: "btn-batchPrinter",
						iconCls: 'icon icon-printer',
						text: '打印',
						value: 7
					}
				]
			}
		}

		getSearchControls() {
			var self = this;
			return {
				list: 4,
				controls: [{
					"label": "单据编码",
					"type": "textbox",
					"name": "billNo",
					"options": { "width": 150}
				}, {
					"label": "供应商",
					"type": "combogridmdm",
				    "name": "supplierNo",
				    "options": {
				        "width": 150,
				        "beanName":"supplier",
				        "valueFeild":"supplierNo",
				    }
				}, {
					"label": "卖场",
					"type": "combogridmdm",
					"name": "shopNo",
					"options": { "width": 150,
						"beanName":"shop",
						"valueFeild":"shopNo",
						onHidePanelFn: function(rec) {
							if(rec){
								self.searchForm.find('#counterNo').combogrid('setValue','');
							}
						}
					}
				}, {
					"label": "专柜",
					"type": "combogridmdm",
					"name": "counterNo",
					"options": { "width": 150,
						"beanName":"counter",
						"valueFeild":"counterNo",
						onShowPanelFn: function(jq) {
							var shopNo = self.searchForm.find('#shopNo').combogrid('getValue');
							self.searchForm.find('#counterNo').combogrid("grid").datagrid('options').queryParams.shopNo=shopNo;
							self.searchForm.find('#counterNo').combogrid("options").queryParams.shopNo=shopNo;
						}
					}
				}, {
					"label": "公司",
					"type": "combogridmdm",
					"name": "companyNo",
					"options": { "width": 150,
						"beanName":"company",
						"valueFeild":"companyNo",
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
				}]
			}
		}

		getGridOptions() {
			var dataurl = portalUrl+'/mdm/data/api';
			var options = {
				id: 'grid',
				url: config.rootUrl + '/bill/counter/balance/list',
				height: "670",
				loadMsg: '请稍等,正在加载...',
				iconCls: 'icon-ok',
				pageSize: "150",
				pageList: [20, 50, 100, 200, 500],
				checkOnSelect: false,
				pagination: true,
				fitColumns: false,
				singleSelect: false,
				rownumbers: true,
				showFooter: true,
				export: {async: true, method:"wss",
					url: config.rootUrl + '/bill/counter/balance/list'
				},
				enableHeaderContextMenu: true,
				enableHeaderClickMenu: true,
				emptyMsg: "暂无数据",
				columns: [
					[{
                        "field": 'ck',
                        "notexport": true,
                        "checkbox": true
                    }, {
						"title": "单据编码",
						"type": "textbox",
						"field": "billNo",
						"sortable" : true,
						"width": 100
					}, {
						"title": "公司",
						"type": "combogridmdm",
						"field": "companyNo",
						"sortable" : true,
						"$formatter": {valueField: 'companyNo', textField: 'name', type: "company"},
						"width": 100
					}, {
						"title": "卖场",
						"type": "combogridmdm",
						"field": "shopNo",
						"sortable" : true,
						"$formatter": {valueField: 'shopNo', textField: 'name', type: "shop"},
						"width": 100
					}, {
						"title": "供应商",
						"type": "combogridmdm",
					    "field": "supplierNo",
					    "sortable" : true,
						"$formatter": {valueField: 'supplierNo', textField: 'name', type: "supplier"},
					    "width": 100
					}, {
						"title": "专柜编码",
						"type": "textbox",
						"field": "counterNo",
						"sortable" : true,
						"width": 100
					}, {
						"title": "专柜",
						"type": "combogridmdm",
						"field": "counterName",
						"sortable" : true,
						"$formatter": {valueField: 'counterNo', textField: 'name', type: "counter"},
						"width": 100
					}, {
						"title": "状态",
						"type": "combocommon",
						"field": "status",
						"sortable" : true,
						"formatter":(value)=>isNotBlank(value)?$.fas.datas.status.first(c=>c.id == value).name:null, 
						"width": 100
					}, {
						"title": "结算月",
						"type": "datebox",
						"field": "settleMonth",
						"sortable" : true,
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
						"title": "应结款总额",
						"type": "textbox",
						"field": "ableSum",
						"dataType":"number",
						"width": 100
					}, {
						"title": "预结款总额",
						"type": "textbox",
						"field": "preAbleSum",
						"dataType":"number",
						"width": 100
					}, {
						"title": "未结款总额",
						"type": "textbox",
						"field": "notAbleSum",
						"dataType":"number",
						"width": 100
					}, {
						"title": "应开票总额",
						"type": "textbox",
						"field": "ableBillingSum",
						"dataType":"number",
						"width": 100
					}, {
						"title": "预开票总额",
						"type": "textbox",
						"field": "preBillingSum",
						"dataType":"number",
						"width": 100
					}, {
						"title": "未开票总额",
						"type": "textbox",
						"field": "notBillingSum",
						"dataType":"number",
						"width": 100
					}, {
						"title": "销售总额",
						"type": "textbox",
						"field": "settleAmount",
						"dataType":"number",
						"width": 100
					}, {
						"title": "提成总额",
						"type": "textbox",
						"field": "profitAmount",
						"dataType":"number",
						"width": 100
					}, {
						"title": "费用总额",
						"type": "textbox",
						"field": "costAmount",
						"dataType":"number",
						"width": 100
					}, {
						"title": "备注",
						"type": "textbox",
						"field": "remark",
						"width": 100
					}, {
						"field": "printCount",
						"type": "textbox",
						"title": "打印次数",
						"width": 80
					}]
				]
			};
			$.gridFormat(dataurl, options);
			return [options];   
		}
	
		onGridDblClickRow(grid, rowIndex, rowData) {
            this.page.switchTab(rowData, rowIndex);
        }
		batchPrinter(){
		   let data = this.getSelectedRows();
            if (data == null || data.length == 0)
                return;
            let ids = _.map(data,function(e){ return e.id; });
            let params = {ids:"'"+ids.join("','")+"'"};
            this.batchPrintAndExcel(params,1);
		}
		

        batchVerify(){
            let data = this.getSelectedRows();
            if (data == null || data.length == 0)
                return;
            var self = this;
            let ids = _.map(data,function(e){ return e.id; });
            let keys = _.map(data,function(e){ return e.billNo; });
            let params = {ids:"'"+ids.join("','")+"'",targetStatus:0};
            self.service.selectByParams(params).then(d=>{
                if(!d||d.length<1){
                $.messager.confirm("确认", "确定要审核当前所选单据吗？",function (r) {
                    if (r) {
                        fas.common.loading("show", "正在处理中......");
                        self.service.batchVerify(keys).then(d=>{
                        	fas.common.loading();
							self.search();
                            if(d.errorCode=="0000"){
								showSuc('审核成功！');
							}else{
                            	showWarn('审核失败！');
							}
                    	});
                    }
                });
            }else{
                showWarn("所选单据中存在非制单状态单据,不能审核。");
                return;
            }
        });
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
		 /**
         * 获取查询参数
         * @returns {*}
         */
        getSearchParams() {
            let data = this.searchForm.form('getData');
            let params = {};
            $.each(data,(i,d)=>{
            	params[i] = $.trim(d);
            });
            params["type"]=1;
            return params;
        }

	    exportBalance() {
	    	let data = this.getSelectedRows();
            if (data == null || data.length == 0)
                return;
            let ids = _.map(data,function(e){ return e.id; });
            let params = {ids:"'"+ids.join("','")+"'"};
            this.batchPrintAndExcel(params,2);
        }
	    
	    batchPrintAndExcel(params,type){
	    	var self = this;
	    	var exportDlg = $('<div id="exportBalanceDlg" />');
            exportDlg.append("body");

            var content = "";
            content += '<div style="padding: 20px 25px;">';
            content += '<span>请选择打印模板类型：</span>';
            content += '<input id="tempType" class="easyui-combobox"  ';
            content += 'data-options="required:true,valueField: ' ;
            content += "'id',textField:" + "'name', data:";
            content += "[{id:0,name:'横排',selected:true},{id:1,name:'竖排'},{id:2,name:'供应商结算'}] ";
            content += '"';
            content += 'style="width:200px;height:26px;line-height:26px;" />';
            content += '</div>';

            var saveHandler = function () {
                let templateType = $("#tempType").combobox('getValue');
                if (templateType == "") {
                    showWarn('请选择打印模板类型');
                    return;
                }
                exportDlg.dialog('close');
                
                self.service.getBillInfos(params,templateType).then(results => {
                	if(results.errorMessage!=null){
    					showInfoMes(results,'操作');
    					return;
    				}
                    var def = $.Deferred();
                	self.bills = results;
                	let count = self.bills.length;
					let index = 0;
                    let task = function () {
                    while (index < count) {
                    	var bill = self.bills[index];
                    	bill.templateType = templateType;
                    	if(type===2){
                            self.exporter = new exporter(bill);
                            self.exporter.export2Excel(bill);//结算单导出.
                    	} else if (type===1){
                        	self.printer = new printer(bill);
                        	self.printer.print(bill);//结算单打印
                            self.service.printCount(bill.billNo);
                    	}
                    	 index += 1;
                         if (index >= count) {
                             def.resolve();
                         }
                    }
                    if (index < count)
                        window.setTimeout(task, 10);
                    };
                	setTimeout(task, 50);
					return def;
                });
            };

            exportDlg.dialog({
                title: "导出结算单",
                width: 320,
                height: 180,
                content: content,
                modal: true,
                buttons: [
                    {
                        text: "确定",
                        iconCls: "icon-save",
                        handler: saveHandler
                    },
                    {
                        text: "取消",
                        iconCls: "icon-cancel",
                        handler: function () {
                            exportDlg.dialog('close');
                        }
                    }
                ],
                onClose: function () {
                    exportDlg.dialog('destroy');
                }
            });
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