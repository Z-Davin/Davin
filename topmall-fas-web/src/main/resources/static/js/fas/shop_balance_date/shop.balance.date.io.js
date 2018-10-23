define(function (require, exports, module) {
	let shopBalanceDateService = require('./shop.balance.date.view.js');
	let service = new shopBalanceDateService();
    function getImporter() {
    	var Excel = require('core/io/excel/excel');
    	var config = require('../config');
    	
    	var expt = new Excel.Import({
    		templateName : '结算期设置.xlsx',
			mapper : 'ShopBalanceDateMapper',
			zipFlag : false,
			valuefield : '',
			H2V : false,
			fields : {
				"*卖场编号" : 'shopNo',
				"*结束日期" : 'endDate',
				"备注":'remark'
			},
			excludefields : ['shopNo','endDate','remark'],
			uniqueFields : ['shopNo'],
			downloadUrl:config.rootUrl
    	});
    	
    	expt.validate = function(count, index, row) {
			var errorMsg = '';
			let mdmUrl = config.mdmUrl;
			$.each(row, function(field, item) {
				if (field == 'shopNo') {
					let param = {'shopNo':item};
					service.findByParam(param).then(d=>{
						if(d){
							errorMsg="数据'"+row.shopNo+"'已存在";
						}
					});
					let queryName = field.replace('No', '');
					var query = Q.Or(Q.Equals('name',`${item}`),Q.Equals(`${field}`,`${item}`));
					var params = {
						status : 1,
						_q:JSON.stringify(query)
					};
					let data = service.getMdmData(mdmUrl, queryName, params);
					if (data.length>0) {
						row[field]=data[0][`${field}`];
					}else{
						errorMsg=errorMsg+item+"在基础数据里面不存在;";
					}
					
				}
			});
			$.fas.datas.endDatas;
			var endDate = $.fas.datas.endDatas.first(c=>c.id == row.endDate).id;
			if (typeof endDate == 'undefined') {
				errorMsg =errorMsg+ "结束日期'" + row.endDate + "'不正确;";
			}
			row.endDate=endDate;
			return errorMsg;
		};

    	
    	
    	
    	expt.onLoaded = function(result) {
			var self = this;
			if (result.errors) {
				$.messager.alert('错误', '导入信息存在错误，请确认!');
				return;
			}
			var data = result.data;
			var details = (JSON.stringify(data));
			
			var url = config.rootUrl + "/shop/balance/date/import.json";
			this.submit( url, {details: details}).then(function(data) {
				self.close();
			})
    	};
    	return expt;
    }
    
    
    
    function getDtlImporter() {
    	var Excel = require('core/io/excel/excel');
    	var config = require('../config');
    	
    	var expt = new Excel.Import({
    		templateName : '结算期明细设置.xlsx',
			mapper : 'ShopBalanceDateDtlMapper',
			zipFlag : false,
			valuefield : '',
			H2V : false,
			fields : {
				"*专柜编号" : 'counterNo',
				"*结算月" : 'settleMonth',
				"*结算开始日期" : 'settleStartDate',
				"拆分日期" : 'splitDate',
				"*结算结束日期" : 'settleEndDate',
				"*操作" : 'action' // 0-删除，1-新增，2-修改，3-拆分
			},
			excludefields : ['counterNo','settleMonth','settleStartDate','settleEndDate'],
			uniqueFields : ['counterNo','settleMonth','settleStartDate','settleEndDate'],
			downloadUrl:config.rootUrl 
    	});
    	
    	expt.validate = function(count, index, row) {
			var errorMsg = '';
			let param = {'counterNo':row.counterNo,'settleMonth':row.settleMonth,'status':2};
			let mdmUrl = config.mdmUrl;
			var query = Q.Or(Q.Equals('name', row.counterNo),Q.Equals("counterNo", row.counterNo));
			var params = {
				status : 1,
				_q:JSON.stringify(query)
			};
			
			let shopNo;
			
			let counterDate = service.getMdmData(mdmUrl, "counter", params);
			if (counterDate.length>0) {
				row.supplierNo = counterDate[0].supplierNo;
				shopNo = counterDate[0].shopNo;
			}else{
				errorMsg=errorMsg+ "专柜：" + row.counterNo +"在基础数据里面不存在;";
			}
			
			var shopQuery = Q.Equals("shopNo", shopNo);
			var shopParams = {
				status : 1,
				_q:JSON.stringify(shopQuery)
			};
			let shopDate = service.getMdmData(mdmUrl, "shop", shopParams);
			if (shopDate.length>0) {
				row.shopNo=shopDate[0].shopNo;
				row.companyNo = shopDate[0].companyNo;
				row.zoneNo = shopDate[0].zoneNo;
			}else{
				errorMsg=errorMsg+ "卖场：" + shopNo +"在基础数据里面不存在;";
			}
			
			if(0 == row.action){
				param.settleStartDate = row.settleStartDate;
				param.settleEndDate = row.settleEndDate;
				service.findDtlByParam(param).then(d=>{
					if(d.length == 0){
						errorMsg=errorMsg + "数据不存在，无法删除";
					} else {
						row.id = d[0].id;
					}
				});
			} else if (1 == row.action) {
				param.settleStartDate = row.settleStartDate;
				param.settleEndDate = row.settleEndDate;
				service.findDtlByParam(param).then(d=>{
					if(d.length > 0){
						errorMsg=errorMsg + "数据已存在，不能新增";
					} else {
						row.status = 2
					}
				});
			} else if (2 == row.action || 3 == row.action) {
				if(3 == row.action && !isNotBlank(row.splitDate)){
					errorMsg=errorMsg + "拆分结算期拆分日期不能为空";
				} 
				service.findDtlByParam(param).then(d=>{
					if(d.length == 0){
						errorMsg=errorMsg + "数据不存在, 无法修改";
					} else if (d.length > 1){
						errorMsg=errorMsg + "数据存在多条记录, 无法修改";
					} else {
						row.id = d[0].id;
					}
				});
			} else {
				errorMsg = errorMsg + "不支持的操作："+ row.action;
			}
			row.remark = "导入Excel修改";
			return errorMsg;
		};

    	expt.onLoaded = function(result) {
			var self = this;
			if (result.errors) {
				$.messager.alert('错误', '导入信息存在错误，请确认!');
				return;
			}
			var data = result.data;
			var details = (JSON.stringify(data));
			var url = config.rootUrl + "/shop/balance/date/dtl/import.json";
			this.submit( url, {details: details}).then(function(data) {
				self.close();
			})
    	};
    	return expt;
    }
    
    
    
    function getImporterByType(type) {
    	if(1 == type){
    		return getImporter();
    	} else {
    		return getDtlImporter();
    	}
    }
    
    /**
	 * 导入
	 * 
	 * @param type
	 */
	exports.importer = function(type) {
		this.expt = getImporterByType(type);
		this.import = function() {
			this.expt.showPanel();
		};
	};
	
});