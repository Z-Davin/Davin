"use strict";
define(function(require,exports,module){
	let UI = require('core/ui');
    let controls = require('core/controls');
    let config = require('../config');
    let Service = require('core/service').Service;
	class BalanceSummaryService extends Service {
        constructor() {
            super(config.rootUrl + "/balance/summary/report");
        }
    }

    class BalanceSummaryView extends UI.EditListView {
        constructor() {
            super("main");
            this._service = new BalanceSummaryService().proxy();
        }
        
        get service() {
            return this._service;
        }

        getToolbars() {
        	var items = ['查询', '重置', '导出'];
            return {
                id: 'toolbar',
                data: items
            }
        }
	
		getSearchControls(){
			return{
				controls:[ {
					"label": "结算月",
					"type": "datebox",
					"name": "settleMonth",
					"options": { "width": 150,
						"dateFmt":"yyyyMM",
						"required":true
						}
				},{
					"label": "大区",
					"type": "combogridmdm",
					"name": "zoneNo",
					"options": { 
						"width": 150,
						"beanName":"zone"
					 }
				},{
				    "label": "店铺",
				    "type": "combogridmdm",
				    "name": "shopNo",
				    "options": {
				        "width": 150,
				        "required": false,
				        "beanName":"shop",
				        "multiple":true
				    }
				},{
					"label":"专柜",
					"type":"combogridmdm",
					"name":"counterNo",
					"options":{
						"width":150,
						"required":false,
						"beanName":'counter'
					}
				}]
			}
		}
	
		getGridOptions(){
			var dataurl = portalUrl+'/mdm/data/api';
			var options = {
				id:'grid',
				url:config.rootUrl + '/balance/summary/report/reportList',
				title:"结算汇总",
				height:"670",
				loadMsg:'正在加载',
				iconCls:'icon-ok',
				pageSize:"20",
				pageList:[20,50,100,200],
				checkOnSelect:false,
				pagination:true,
				fitColumns:false,
				singleSelect:false,
				rownumbers:true,
				export: {async: true,
					method:'get',
					fileName:'结算汇总报表导出',
					url:config.rootUrl + '/balance/summary/report/reportList'
				},
				enableHeaderContextMenu:true,
				enableHeaderClickMenu:true,
				emptyMsg:"暂无数据",
				columns:[
				    [{
					    "field": "year",
					    "type": "textbox",
					    "title": "年份",
					    "width": 50,
					    "hidden": false,
					    "formatter":function(value,row,index){
					    	return row.settleMonth.substring(0,4);
					    },
					    "rowspan":2
					},{
					    "field": "month",
					    "type": "textbox",
					    "title": "月份",
					    "width": 40,
					    "hidden": false,
					    "formatter":function(value,row,index){
					    	return row.settleMonth.substring(4,6);
					    },
					    "rowspan":2
					},{
						 "title": "合同基本信息",
						 "colspan":9
					}],[{
					    "field": "shopName",
					    "type": "textbox",
					    "title": "卖场名称",
					    "width": 100,
					    "hidden": false,
						"$formatter": {valueField: 'shopNo', textField: 'name', type: "shop"}
					},{
					    "field": "counterNo",
					    "type": "textbox",
					    "title": "专柜编码",
					    "width": 80,
					    "dataType":"varchar",
					    "hidden": false
					},{
					    "field": "counterName",
					    "type": "textbox",
					    "title": "专柜名称",
					    "width": 100,
					    "hidden": false,
					    "$formatter": {valueField: 'counterNo', textField: 'name', type: "counter"}
					},{
				    	"field":"supplierNo",
				    	"type":"textbox",
				    	"title":"供应商",
				    	"$formatter": {valueField: 'supplierNo', textField: 'name', type: "supplier"},
				    	"width":100,
				    	"hidden":false
				    },{
				    	"field":"businessType",
					    "type":"textbox",
					    "title":"合作方式",
					    "width":80,
					    "hidden":false,
					    "formatter":(value)=>$.fas.datas.businessType.first(c=>c.id == value).name
				    },{
				    	"field":"areaCounter",
					    "type":"numberbox",
					    "title":"面积",
					    "width":100,
					    "dataType":"number",
					    "hidden":false
				    },{
				    	"field":"settleSum",
					    "type":"numberbox",
					    "title":"销售总额",
					    "width":100,
					    "dataType":"number",
					    "hidden":false
				    },{
				    	"field":"guaraSaleSum",
					    "type":"numberbox",
					    "title":"保底销售",
					    "width":80,
					    "dataType":"number",
					    "hidden":false
				    },{
				    	"field":"guaraProfitSum",
					    "type":"numberbox",
					    "title":"保底利润",
					    "width":100,
					    "dataType":"number",
					    "hidden":false
					  
				    }]
				 ]
			};
			
			$.gridFormat(dataurl, options);
				return [options];           
		}
		
		preLoadData(grid, params) {	
			var options = this.getGridOptions();
			var columns = options[0].columns[0];
			var columns1 = options[0].columns[1];
			$.ajax({
		        url: config.rootUrl + '/balance/summary/report/queryPriceTypeList',
		        async: false,
		        data : params,
		        success: function (result) {
		        	columns.push({"title": "销售扣费","colspan":result.length})
		        	for(var i = 0;i < result.length;i++) {
		        		columns1.push({ "field":result[i].type, "type":"numberbox", "title": result[i].type+'码扣费', "width": 60, align :"right","dataType":"number"});
		        	}
		        }
		    });
			
			$.ajax({
		        url: config.rootUrl + '/balance/summary/report/queryCostTypeList',
		        async: false,
		        data : params,
		        success: function (result) {
		        	columns.push({"title": "扣项费用","colspan":result.length})
		        	for(var i = 0;i < result.length;i++) {
		        		columns1.push({ "field": result[i].costNo, "type":"numberbox", "title": result[i].costName, "width": 80, align :"right" ,"dataType":"number"});
		        	}
		        }
		    });
			
			var url = grid.datagrid('options').url;
			grid.datagrid('options').url = "";
			grid.datagrid({
				columns : [columns,columns1]
			});
			grid.datagrid('options').url = url;
			return true;
		}
	
		doAsyncExport(options) {
			if (!this.preSearch()){
				return;
			}
			let grid = this.currentGrid;
			
        	options.params = this.getSearchParams();
        	options.params.fileName=options.fileName;
        	options.url = config.rootUrl + '/balance/summary/report/reportList';
        	options.method = '';
        	this.preLoadData(grid, options.params);
            
            seajs.use(["core/io/excel/excel"], function (Excel) {
                options.header = Excel.utils.getColumns(false, grid);
                var optTemp = grid.datagrid('options');
                var columns = optTemp.columns[optTemp.columns.length - 1];
                for (var m = 0; m < columns.length; m++) {
                	var headerAry = options.header[options.header.length - 1];
                	headerAry[m].hidden = columns[m].hidden;
                }
                var excel = new Excel.Export(options);
                excel.export(options.fileName||"导出", options.params);
            });
        }

	}
	
	class Page extends UI.Page{
		constructor(){
			super('4010681',$('#mainPanel'));
			this.views=[new BalanceSummaryView()];
		}
		
		
	}
	var page =new Page();
	page.render();
	
});