define(function (require, exports, module) {
	let storeGroupService = require('./store.group.view.js');
	let service = new storeGroupService();
    function getImporter() {
    	var Excel = require('core/io/excel/excel');
    	var config = require('../config');
    	
//    	var datas=[];
//		datas=[{id:"32",name:'自然月'}];
//		for(var i=1;i<=28;i++){
//			let d={};
//			d.id = i;
//			d.name = i;
//			datas.push(d);
//		}
    	
//    	var datas = 
    	var expt = new Excel.Import({
    		templateName : '结算期设置.xlsx',
			mapper : 'storeGroupMapper',
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
			var self = this;
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
				
//				if(field == 'endDate'){
//					let reg = '^((0?[1-9])|((1|2)[0-9])|30|31)$';
//					let re = new RegExp(reg);
//					let endDateValue = row.endDate;
//					if(!re.test(endDateValue)){
//						errorMsg = errorMsg+ "结束日期'" + row.endDate + "'格式不正确！";
//					}
//				}
			});
			$.fas.datas.endDatas;
			var endDate = $.fas.datas.endDatas.first(c=>c.name == row.endDate).id;
			if (typeof endDate == 'undefined') {
				errorMsg =errorMsg+ "结束日期'" + row.endDate + "'不正确;";
			};
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