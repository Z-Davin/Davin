define(function(require, exports, module) {
	let supplierAccountService = require('./supplier.account.service.js');
	let service = new supplierAccountService();
	let Excel = require('core/io/excel/excel');
	let config = require('../config');
	
	/**
	 * 竖排导入
	 */
	function getNormalImporter() {
		
		var expt = new Excel.Import({
			templateName : '供应商账户维护模板.xlsx',
			mapper : 'sulllierAccountMapper',
			zipFlag : false,
			H2V : false,
			fields : {
				"*卖场" : 'shopNo',
				"*专柜" : 'counterNo',
				"公司税号" : 'taxNo',
				"公司名称" : 'suCompanyName',
				"*开户银行" : 'bankName',
				"*银行账号" : 'bankAccount',
				"银行账户名" : 'bankAccountName',
				"公司地址" : 'address'
			},
			excludefields : [ 'shopNo', 'counterNo', 'taxNo', 'suCompanyName','bankName', 'bankAccount', 'bankAccountName', 'address' ],
			uniqueFields : [ 'shopNo', 'counterNo'],
			downloadUrl : config.rootUrl,
			headLine:1
		});
		
		expt.validate = function(count, index, row) {
			var errorMsg = '';
			let mdmUrl = config.mdmUrl;
			$.each(row, function(field, item) {
				if (field == 'shopNo' || field == 'counterNo') {
					var DBNo=field;
					let queryName = field.replace('No', '');
					var query = Q.Or(Q.Equals('name',`${item}`),Q.Equals(`${DBNo}`,`${item}`));
					var params = {
							status : 1,
							_q:JSON.stringify(query)
						};
					// 专柜 状态为正常，停用，待清退都允许导入wang.sj
					if (field == 'counterNo') {
						params = {
							statusin : '1,2,3',
							_q:JSON.stringify(query)
						};
					}
					
					let data = service.getMdmData(mdmUrl, queryName, params);
					if (data.length>0) {
						row[field]=data[0][`${DBNo}`];
					}else{
						errorMsg=errorMsg+item+"在基础数据里面不存在;";
					}
				}
			});
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

			var url = config.rootUrl + "/supplier/account/import.json";
			this.submit(url, {
				details : details
			}).then(function(data) {
				if(isNotBlank(data.errorMessage)){
					showError(data.errorMessage);
				}
				self.close();
			})
		};
		
		return expt;
	}
	
	
	
	
	function getImporter(type){
		if(1 == type){
			return getNormalImporter();
		}
	}
	
	/**
	 * 导入
	 * 
	 * @param type
	 */
	exports.importer = function(type) {
		this.expt = getImporter(type);
		this.import = function() {
			this.expt.showPanel();
		};
	};

});