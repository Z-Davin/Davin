define(function(require, exports, module) {
	let shopCostService = require('./shop.cost.service.js');
	let service = new shopCostService();
	
	
	function getImporter() {
		var Excel = require('core/io/excel/excel');
		var config = require('../config');
		
		var expt = new Excel.Import({
			templateName : '卖场费用登记导入模板.xlsx',
			mapper : 'ShopCostMapper',
			zipFlag : false,
			valuefield : 'number',
			H2V : false,
			fields : {
				"*卖场" : 'shopNo',
				"*专柜" : 'counterNo',
				"*结算月" : 'settleMonth',
				"*开始日期" : 'settleStartDate',
				"*结束日期" : 'settleEndDate',
				"*扣项编码" : 'costNo',
				"*数量" : 'number',
				"备注" : 'remark'
			},
			uniqueFields : ['shopNo', 'counterNo', 'costNo', 'settleMonth','settleStartDate', 'settleEndDate'],
			downloadUrl : config.rootUrl
		});

		expt.validate = function(count, index, row) {
			var errorMsg = '';
			let params = {
				shopNo : row.shopNo,
				counterNo : row.counterNo,
				settleMonth : row.settleMonth,
				settleStartDate : row.settleStartDate,
				settleEndDate : row.settleEndDate,
			}
			
			let shopBalanceDtlDate;
			let def = $.ajax({
    			url:config.rootUrl + "//shop/balance/date/dtl/get",
    			data:params,
    			async:false,
    			type:'get'
    		});
			def.then(d=>shopBalanceDtlDate = d);
			
			
			if(!isNotBlank(shopBalanceDtlDate)){
				errorMsg = '未查询到结算期信息，请检查。';
			}
			
			let query = Q.And(Q.Or(Q.Equals('name',row.costNo),Q.Equals('deductionNo',row.costNo)),Q.Equals("suitName",1));
			let queryParams = {
					statusin : '1,2,3',
					_q:JSON.stringify(query)
			}
			let data = service.getMdmData(config.mdmUrl, 'deduction', queryParams);
			
			if (data.length > 0) {
				row['costNo']=data[0]['deductionNo'];
			}else{
				errorMsg=row.costNo + "在基础数据里面不存在;";
			}
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

			var url = config.rootUrl + "/shop/cost/import.json";
			this.submit(url, {
				details : details
			}).then(function(data) {
				if(isNotBlank(data.errorDefined)){
					showError(data.errorDefined);
				}
				self.close();
			})
		};
		return expt;
	}

	/**
	 * 导入
	 * 
	 * @param type
	 */
	exports.importer = function() {
		this.expt = getImporter();
		this.import = function() {
			this.expt.showPanel();
		};
	};

});