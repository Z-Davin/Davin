define(function(require, exports, module) {
	let propertyCostService = require('./property.cost.view.js');
	let service = new propertyCostService();
	let Excel = require('core/io/excel/excel');
	let config = require('../config');

	function getNormalImporter() {
		var expt = new Excel.Import({
			templateName : '物业水电费导入模板.xlsx',
			mapper : 'CounterCostMapper',
			zipFlag : false,
			valuefield : 'ableSum',
			H2V : false,
			fields : {
				"*卖场" : 'shopNo',
				"*物业" : 'mallNo',
				"*铺位组" : 'bunkGroupNo',
				"*结算月" : 'settleMonth',
				"*开始日期" : 'settleStartDate',
				"*结束日期" : 'settleEndDate',
				"*扣项编码" : 'costNo',			
				"期初数" : 'startNum',	
				"期末数" : 'endNum',
				"*数量" : 'number',	
				"系数1" : 'firstRatio',
				"系数2" : 'secondRatio',
				"备注" : 'remark',
			},
			uniqueFields : [ 'shopNo', 'mallNo', 'bunkGroupNo','settleMonth','settleStartDate','settleEndDate','costNo' ],
			downloadUrl : config.rootUrl,
		})
		
		expt.validate = function(count, index, row) {
			var errorMsg = '';
			let params = {
				shopNo : row.shopNo,
				mallNo : row.mallNo,
				bunkGroupNo : row.bunkGroupNo,
				settleMonth : row.settleMonth,
				settleStartDate : row.settleStartDate,
				settleEndDate : row.settleEndDate,
			}
			
			let mallBalanceDtlDate;
			let def = $.ajax({
    			url:config.rootUrl + "/mall/balance/date/dtl/get",
    			data:params,
    			async:false,
    			type:'get'
    		});
			def.then(d=>mallBalanceDtlDate = d);
			
			
			if(!isNotBlank(mallBalanceDtlDate)){
				errorMsg = '未查询到结算期信息，请检查。';
			}
			
			let query = Q.And(Q.Or(Q.Equals('name',row.costNo),Q.Equals('deductionNo',row.costNo)),Q.Equals("suitName",2));
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
		}
		
		expt.onLoaded = function(result) {
			var self = this;
			if (result.errors) {
				$.messager.alert('错误', '导入信息存在错误，请确认!');
				return;
			}
			var data = result.data;
			var details = (JSON.stringify(data));

			var url = config.rootUrl + "/property/cost/import.json";
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
		this.expt = getNormalImporter();
		this.import = function() {
			this.expt.showPanel();
		};
	};
})