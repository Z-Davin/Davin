"use strict";
define(function (require, exports, module) {
	let UI = require('core/ui');
    let config = require('../config');
    let Service = require('core/service').Service;
    
    class MallBalanceDateService extends Service {
    	constructor() {
    		 super(config.rootUrl + "/mall/balance/date");
    	}
    	confirm(key){
        	return $.post( this.url + "/confirm", {'id': key});
        }
    	
    	monthBalance(key){
    		return $.post( this.url + "/monthBalance", {'idList': key});
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
    module.exports = MallBalanceDateService;
    
    
    class MallBalanceDateView extends UI.EditListView {
        constructor() {
            super("main");
            this._service = new MallBalanceDateService();
        }
        get service() {
            return this._service;
        }
        getToolbars() {
       	 var items = ['新增','查询','重置','导出'];
       	items = $.merge(items, [
    	          				{id: "btn-confirm", iconCls: 'icon  icon-file-text', text: '确认', value: 8, order: "8"},
     	          				{id: "btn-monthBalance", iconCls: 'icon  icon-alarm', text: '月结', value: 51, order: "8"}
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
					"label": "大区",
					"type": "combogridmdm",
					"name": "zoneNo",
					"options": { 
						"width": 150,
						"beanName":"zone"
					 }
				}, {
					"label": "公司",
					"type": "combogridmdm",
					"name": "companyNo",
					"options": { 
						"width": 150,
						"beanName":"company"
					}
				},{
					"label": "卖场",
					"type": "combogridmdm",
				    "name": "shopNo",
				    "options": {
				        "width": 150,
				        "required": false,
				        "beanName":"shop"
				    }
				},{
					"label": "物业公司",
					"type": "combogridmdm",
				    "name": "mallNo",
				    "options": {
				        "width": 150,
				        "required": false,
				        "beanName":"propertyCompany",
				        "beanNo":"companyNo"
				    }
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
					"label": "结算月",
					"type": "datebox",
					"name": "settleMonth",
					"options": { "width": 150,
						"dateFmt":"yyyyMM"}
				},{
					"label": "状态",
					"type": "combocommon",
					"name": "status",
					"options": {"width": 150, "type": "status", "required": false, "disabled": false,
						"datarange":"1,2"
					}
				},{
					"label": "积分抵现是否结算",
					"type": "combocommon",
					"name": "pointsCalculateFlag",
					"options": {"width": 150, "type": "pointsCalculateFlag", "required": false, "disabled": false
					}
				},{
					"label": "结算形式",
					"type": "combocommon",
					"name": "balanceType",
					"options": {"width": 150, "type": "balanceType", "required": false, "disabled": false
					}
				}]
			}
		}
		getGridOptions() {
			var dataurl = portalUrl+'/mdm/data/api';
			var options = {
				id: 'grid',
				url: config.rootUrl + '/mall/balance/date/list',
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
					url: config.rootUrl + '/mall/balance/date/list'},
				enableHeaderContextMenu: true,
				enableHeaderClickMenu: true,
				emptyMsg: "暂无数据",
				columns: [
		                    [{
		                        "field": "zoneName",
		                        "type": "textbox",
		                        "title": "大区",
		                        "width": 80,
		                        "hidden": false,
		                        "$formatter": {valueField: 'zoneNo', textField: 'name', type: "zone"},
		                        "editor" : "readonlytext"
		                    },{
		                        "field": "companyName",
		                        "type": "textbox",
		                        "title": "公司名称",
		                        "width": 80,
		                        "hidden": false,
		                        "editor" : "readonlytext",
		                        "$formatter": {valueField: 'companyNo', textField: 'name', type: "company"}
		                    },{
		                        "field": "companyNo",
		                        "type": "textbox",
		                        "title": "公司编码",
		                        "width": 120,
		                        "hidden": false,
		                        "editor" : "readonlytext"
		                    }, {
		                        "field": "shopName",
		                        "type": "textbox",
		                        "title": "卖场名称",
		                        "width": 120,
		                        "hidden": false,
		                        "$formatter": {valueField: 'shopNo', textField: 'name', type: "shop"},
		                        "editor" : "readonlytext"
		                    }, {
		                        "field": "shopNo",
		                        "type": "textbox",
		                        "title": "卖场编码",
		                        "width": 80,
		                        "hidden": false,
		                        "editor" : "readonlytext"
		                    },{
		                        "field": "mallName",
		                        "type": "textbox",
		                        "title": "物业公司名称",
		                        "width": 120,
		                        "hidden": false,
		                        "$formatter": {valueField: 'mallNo', textField: 'name', type: "propertyCompany"},
		                        "editor" : "readonlytext"
		                    },{
		                        "field": "bunkGroupNo",
		                        "type": "textbox",
		                        "title": "铺位组",
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
								"formatter":(value)=>$.fas.datas.status.first(c=>c.id == value).name
		                    },{
		                        "field": "endDate",
		                        "type": "textbox",
		                        "title": "结束日期",
		                        "width": 80,
		                        "hidden": false,
		                        "editor" : "readonlytext",
		                        "formatter":function (value, row, index) {
		                        	$.fas.datas.endDatas;
		                        	let values= value.split(",");
		                        	let param="";
		                        	for(var i=0;i<values.length;i++){
		                        		if(i==values.length-1){
		                        			param+=$.fas.datas.endDatas.first(c=>c.id == values[i]).name;
		                        		}else{
		                        			param+=$.fas.datas.endDatas.first(c=>c.id == values[i]).name+",";
		                        		}
		                        	}
		                        	return param;
		                        }
		                    },{
		                        "field": "closeDate",
		                        "type": "textbox",
		                        "title": "关账日期",
		                        "width": 80,
		                        "hidden": false,
		                        "editor" : "readonlytext"
		                    },{
		                        "field": "balanceType",
		                        "type": "textbox",
		                        "title": "结算形式",
		                        "width": 80,
		                        "hidden": false,
		                        "editor" : "readonlytext",
		                        "formatter":(value)=>isNotBlank(value)?$.fas.datas.balanceType.first(c=>c.id == value).name:null
		                    },{
		                        "field": "pointsCalculateFlag",
		                        "type": "textbox",
		                        "title": "积分抵现是否结算",
		                        "width": 100,
		                        "hidden": false,
		                        "editor" : "readonlytext",
		                        "formatter":(value)=>isNotBlank(value)?$.fas.datas.pointsCalculateFlag.first(c=>c.id == value).name:null
		                    },{
		                        "field": "remark",
		                        "type": "textbox",
		                        "title": "备注",
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
		                    }]
				],
			};
			$.gridFormat(dataurl, options);
				return [options];   
		}
		
		
		getDetailControls(){
			return{
				controls:[{
						"label":"卖场",
						"type":"combogridmdm",
						"name":"shopNo",
						"disable":"disable",
						"options":{
							"width":200,
							"required":true,
							"beanName":"shop",
							"disable":true,
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
						"label":"结束日期",
						"type":"comboDate",
						"name":"endDate",
						"options":{
							"width":200,
							"required":true,
							"multiple":true
							}
					},{
						"name": "balanceType",
						"type": "combocommon",
						"label": "结算形式",
						"options":{
							"width":200,
							"required":true,
							"type":"balanceType"
							}
					},{
						"name": "pointsCalculateFlag",
						"type": "combocommon",
						"label": "积分抵现是否结算",
						"options":{
							"width":200,
							"required":true,
							"type":"pointsCalculateFlag"
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
			}
		}
		
		getDialog() {
			
		}
    
		validateDetail() {
			var validateFlag = this.detailForm.form('validate');
			if(validateFlag){
				var params = this.getDetail();
				$.ajax({
			        url: config.rootUrl + '/mall/balance/date/validateCreate',
			        async: false,
			        data : params.rowData,
			        success: function (result) {
			        	if(result.errorCode != '0000'){
			        		validateFlag = false;
			        		showWarn(result.errorMessage);
			        	}
			        }
				})
			}
			return validateFlag;
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
		
		monthBalance(){
			let data = this.getSelectedRows();
	        if(data == null || data.length == 0)
	             return;
	        var self = this;
	     	var flag = true;
	     	let idList='';
	        for(let i = 0; i < data.length; i++){
	        	self.service.findByParam({id:data[0].id}).then(c=>{
	         		if(2!= c.status) {
	         			showWarn("该数据不是生效状态,不能月结");
	         			flag=false;
	         			return false;
	         		}else{
	         			idList= idList +data[i].id + ",";
	         		}
	         	});
	         }
	         if(!flag){
	        	 return;
	         }
	         self.service.monthBalance(idList).then(c=>{
	        	self.search();
	        	showInfoMes(c,'月结');
	         });
		}
		
		sufEditControl(){
			if(this.data.rowData.status&&this.data.rowData.status==2){
				//表示生效
				this.handleHeader(this.data.rowData.status);
			}else{
				this.handleHeader();
			}
        }
		
	       createEditPanel() {
	    	   super.createEditPanel();
	    	   this.handleHeader();
	        }
		
	       handleHeader(status) {
			 let formObj = this.detailForm;
			 if(status=='2'){
	        	formObj.find("input").attr("readOnly", true).addClass("readonly");
	        	formObj.find(".easyui-combobox").combobox('disable');
	        	formObj.find(".easyui-combogridmdm").combobox('disable');
	        	formObj.find(".easyui-datebox").datebox('disable');
			 }else{
		    	 formObj.find(".easyui-combobox").combobox('enable');
		    	 formObj.find(".easyui-combogridmdm").combobox('enable');
		    	 formObj.find(".easyui-datebox").datebox('enable');
		    	 formObj.find("input").attr("readOnly", false).removeClass("readonly");
			 }
			 formObj.find('#balanceType').next().find('input').attr("readOnly", false).removeClass("readonly");
		     formObj.find('#balanceType').combocommon('enable');
	        	
		};
		
		// 重写双击事件,判断是否是制单状态
		 onGridDblClickRow(grid, rowIndex, rowData) {
			 if(grid[0].id=='view_grid_grid1'){
				 return;
			 }
            this.data = {rowIndex, rowData};
            this.currentGrid.datagrid('selectRow', rowIndex);
            let key = rowData.id;
            this.service.getById(key).then(c=>{
    			 this.edit();   	
        	});
	      }
		 getSecondOptions() {
			 var dataurl = portalUrl+'/mdm/data/api';
				var secondoptions = {
					id: 'grid3',
					title: '属性明细',
					loadMsg: '请稍后',
					lconCls: 'icon-ok',
					pageSize: "20",
					pageList: [20, 50, 100, 200],
					checkOnSelect: false,
					pagination: true,
					singleSelect: true,
					fitColumns: false,
					rownumbers: true,
					enableHeaderContextMenu: true,
					enableHeaderClickMenu: true,
					onRowContextMenu: onDtlRowContextMenu,
					emptyMsg: "暂无数据",
					columns: [
						[{
	                        "field": "shopName",
	                        "type": "textbox",
	                        "title": "卖场名称",
	                        "width": 120,
	                        "hidden": false,
	                        "$formatter": {valueField: 'shopNo', textField: 'name', type: "shop"},
	                        "editor" : "readonlytext"
	                    }, {
	                        "field": "shopNo",
	                        "type": "textbox",
	                        "title": "卖场编码",
	                        "width": 80,
	                        "hidden": false,
	                        "editor" : "readonlytext"
	                    }, {
	                        "field": "mallName",
	                        "type": "textbox",
	                        "title": "物业公司名称",
	                        "width": 120,
	                        "hidden": false,
	                        "$formatter": {valueField: 'mallNo', textField: 'name', type: "propertyCompany"},
	                        "editor" : "readonlytext"
	                    }, {
	                        "field": "mallNo",
	                        "type": "textbox",
	                        "title": "物业公司编码",
	                        "width": 80,
	                        "hidden": false,
	                        "editor" : "readonlytext"
	                    }, {
	                        "field": "bunkGroupNo",
	                        "type": "textbox",
	                        "title": "铺位组",
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
							"formatter":(value)=>$.fas.datas.status.first(c=>c.id == value).name
	                    },{
	                        "field": "settleMonth",
	                        "type": "textbox",
	                        "title": "结算期",
	                        "width": 100,
	                        "hidden": false,
	                        "editor" : "readonlytext"
	                    },{
	                        "field": "settleStartDate",
	                        "type": "textbox",
	                        "title": "开始日期",
	                        "width": 100,
	                        "hidden": false,
	                        "editor" : "readonlytext"
	                    },{
	                        "field": "settleEndDate",
	                        "type": "textbox",
	                        "title": "截止日期",
	                        "width": 100,
	                        "hidden": false,
	                        "editor" : "readonlytext"
	                    }]
					]
				};
				$.gridFormat(dataurl, secondoptions);
				return [secondoptions];
			}
		 
		 createPanel(container) {
				var viewId = this.viewId;
				var html = `
				<div easyui-layout id="${viewId}_layer">
                <div data-options="region:'north',border:false" style="height:32px;" >
                    <div id="${viewId}_toolbar"></div>
                </div>
                <div class="easyui-layout" data-options="region:'center'" id="${viewId}_sublayer" >
                    <div data-options="region:'north',border:true" style="overflow: hidden;">
                        <div class="search-div searcher" id="${viewId}_top_panel"></div>
                    </div>
                    <div data-options="region:'center',border:true" id="${viewId}_main_panel">

                    </div>
                    <div data-options="region:'south',border:true" id="${viewId}_second_panel">
                    </div>
                   
                </div>
            </div>`;
				return $(html).appendTo(container);
			}
		 resize() {
				var viewId = this.viewId;
				var h = null;
				if (window.top == window.self) { // 不存在父页面
					h = document.body.clientHeight;
				} else {
					h = $(window.document.body).height();
				}
				$("#main_layer").height(h);
				let mainH = $("#main_layer").height();
				let main_toolbarH = $("#main_toolbar").outerHeight();
	            $("#main_sublayer").height(mainH-32);
				let searchH = $(".search-div").outerHeight();
				let hh = h - main_toolbarH - searchH;
				$(`#${viewId}_main_panel`).height(hh * 2 / 3);
				$(`#${viewId}_second_panel`).height(hh / 3);
			}
		 render(parent) {
				let self = this;
				var viewId = this.viewId;
				$(window).resize(function() {
					self.resize();
					$(`#${viewId}_main_panel`).datagrid('resize');
					$(`#${viewId}_second_panel`).datagrid('resize');
				});

				this.container = this.createPanel(parent);

				this.toobar = $(`#${this.viewId}_toolbar`);
				this.gridPanel = $(`#${this.viewId}_main_panel`);
				this.gridPanel2 = $(`#${this.viewId}_second_panel`);
				$(`#${viewId}_toolbar2`).find("a").linkbutton();
				super.render();
				this.resize();

				var options = this.getGridOptions();
				if (!options)
					return;
				var options3 = this.getSecondOptions();
				if (!options3)
					return;
				var parent2 = this.gridPanel2;
				if (options3.length == 1) {
					this.renderGrid(parent2, options3[0], 0);
				}

		}
		 onGridClickRow(grid, rowIndex, rowData) {
			 let searchParams = this.getSearchParams();
			 
			 if('view_grid_grid3' == grid[0].id){
				 return;
			 } else {
				 $('#view_grid_grid3').datagrid('options').url = config.rootUrl + '/mall/balance/date/dtl/list?shopNo='+rowData.shopNo+'&mallNo='+rowData.mallNo+'&bunkGroupNo='+rowData.bunkGroupNo+ '&settleMonth='+searchParams.settleMonth;
				 $('#view_grid_grid3').datagrid('load');
			 }
	     }
    }
    
    
    class Page extends UI.Page {
        constructor() {
            super('4010500',$('#mainPanel'));
            this.views = [new MallBalanceDateView()]
        }
        
        rendered(){
			$('#loading').remove();
			$("#main_sublayer").layout();
			var fold = new Fold({
			        		appendTo : $('#main_toolbar'),
			        		target:$('#main_sublayer'),
			        		foldDirection:'center'
			        	});
			fold.countHeight = function(obj){
				let boxH = $("#main_sublayer").height();
				let topH = $("#main_top_panel").outerHeight();
				let mainPanelH = $("#main_main_panel").outerHeight();				
				if ($(obj).hasClass("search-up-arr")) {
					$("#main_second_panel").height(boxH - topH - mainPanelH);
			    }else{
			    	$("#main_second_panel").height(boxH - topH);
			    	$('#main_second_panel').parent('.layout-panel-south').css({top:topH});
			    	$('#view_grid_grid3').datagrid('resize');
			    }
			}
		}
    }
    var page = new Page();
    page.render();
    
    function onDtlRowContextMenu(e, rowIndex, rowData){
		e.preventDefault();
		let delFlag = false;
		let recalFlag = false;
		operations.each(operation=>{
			if(-1 == operation){
				delFlag = true;
				recalFlag = true;
			} else if(4 == operation){
				delFlag = true;
			} else if (51 == operation) {
				recalFlag = true;
			} 
		});
 
		// 当这两个按钮 有一个是有权限的时候就弹出右键权限
		if(delFlag || recalFlag) {
			$('#dtlRowContextMenu').menu('show', {
			left:e.pageX,
			top:e.pageY
			}); 
			if(!delFlag){
				$("#delDtl").remove();
			}
			if(!recalFlag){
				$("#reCalculator").remove();
			}
		}  
    }
});


function reCalculator(){
	let data = $("#view_grid_grid3").datagrid('getSelected');
	if(isNotBlank(data)){
		if(5 == data.status || 7 == data.status) {
			fas.common.loading("show", "正在处理中......");
			let def = $.post(APP_SETTINGS.fas + "/mall/cost/recalculation", data);
			def.done(c=>{fas.common.loading();
				if('0000' == c.errorCode){
					showSuc('重算成功');
				} else {
					showError(c.errorMessage);
				}
			});
		} else {
			showWarn("请选择已生成过费用的记录.");
		}
	} else {
		showWarn("请选择一条数据.");
	}
}


function delDtl(){
	let data = $("#view_grid_grid3").datagrid('getSelected');
	if(isNotBlank(data)){
		if(2 == data.status) {
			$.messager.confirm("确认", "确定要删除结算期明细吗？",function (r) {
				if (r) {
					fas.common.loading("show", "正在处理中......");
					let def = $.post(APP_SETTINGS.fas + "/mall/balance/date/dtl/delete/"+data.id);
					def.done(c=>{
						fas.common.loading();
						if(isNotBlank(c.errorMessage)){
							showError(c.errorMessage);
						} else {
							if(1 == c) {
								$("#view_grid_grid3").datagrid('reload');
								showSuc('删除成功');
							} else {
								showError('删除失败');
							}
						}
					})
				}
			})
		} else {
			showWarn("请选择生效状态的记录删除");
		}
	} else {
		showWarn("请选择一条数据.");
	}
}