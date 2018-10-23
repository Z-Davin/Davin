define(function(require, exports, module) {
	let counterCostService = require('./mall.cost.service.js');
	let service = new counterCostService();
	let Excel = require('core/io/excel/excel');
	let XLSX = require('core/io/excel/xlsx');
	let config = require('../config');
	
	/**
	 * 费用横排导入
	 */
	function getH2VImporter(){
		let expt = new Excel.Import({
			templateName : '物业费用导入.xlsx',
			zipFlag : false,
			H2V : false,
			fields : {
				"*大区编码" : 'zoneNo',	
				"*卖场编码" : 'shopNo',
				"*铺位组编码" : 'bunkGroupNo',
				"*结算月" : 'settleMonth',
			},
			uniqueFields : [  'shopNo', 'bunkGroupNo', 'settleMonth' ],
			downloadUrl : config.rootUrl,
		});
		
		expt.validate = function(count, index, row){
			let self = this;
			let errorMsg = '';
			let shopNo = row.shopNo;
			let bunkGroupNo = row.bunkGroupNo;
			let settleMonth = row.settleMonth;
			let query = Q.Equals('shopNo', shopNo);
			let params = {
				//statusin : '1,2,3',	
				_q:JSON.stringify(query)
			};
			let data = service.getMdmData(config.mdmUrl, 'shop', params);
			
			if(data.length <= 0){
				errorMsg= "未获取到卖场【"+ shopNo +" 】信息;";
			}
			
			service.findBySettle({'settleMonth' : settleMonth, 'shopNo' : shopNo, 'bunkGroupNo' : bunkGroupNo}).then(d=>{
				if(d.length > 0){
					row.settleStartDate = d[0].settleStartDate;
					row.settleEndDate = d[0].settleEndDate;
					row.mallNo =  d[0].mallNo;
				} else {
					errorMsg = errorMsg+ "未获取到结算月【" + settleMonth + "】信息";
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
			let datas = result.data;
			let costArray = result.costArray;
			let resultArray = spiltDatas(datas, costArray);
			
			var details = (JSON.stringify(resultArray));

			var url = config.rootUrl + "/mall/cost/import.json";
			this.submit(url, {
				details : details
			}).then(function(data) {
				if(isNotBlank(data.errorDefined)){
					showError("导入失败,请联系管理员");
				}
				self.close();
			})
		};
		
		
		expt.mergeRow = function(desc, src, action){
			var self = this;
			// 1 覆盖 2 累加 3 忽略
			if (action == 3) {
				self.context.info.ignore += 1;
			    self.out(`【${src.__r__}】忽略`, 'info');
			    return false;
			} else if (action == 1){
				for (var p in src) {
			        if (p.indexOf("__") >= 0 || self.options.uniqueFields.indexOf(p) > -1)
				    continue;
				    desc[p] = src[p];
				}
				self.context.info.override += 1;
			    self.out(`【${src.__r__}】覆盖【${desc.__r__}】`, 'info');
			} else {
				var srcAbleSumArray = src.ableSumArray
				var descAbleSumArray = desc.ableSumArray
				
				for ( var i = 0; i < descAbleSumArray.length; i++) {
					descAbleSumArray[i] = parseFloat(descAbleSumArray[i]) + parseFloat(srcAbleSumArray[i]);
				}
				
				self.context.info.merge += 1;
				self.out(`【${src.__r__}】累加到【${desc.__r__}】`, 'info');
			}
			return false;
		}
		
		
		/**
		 * 将导入的Excel拆分
		 */
		function spiltDatas(datas, costArray){
			let detailArray = [];
			$.each(datas, function(i, item) {
				let ableSumArray = item.ableSumArray;
				for ( var j = 0; j < ableSumArray.length; j++) {
					let detail = {};
					var ableSum = parseFloat(ableSumArray[j]).toFixed(2);
					if(0 == ableSum){
						continue;
					}
					var costObj = costArray[j];
					
					detail.costNo = costObj.deductionNo;
					detail.shopNo = item.shopNo;
					detail.mallNo = item.mallNo;
					detail.bunkGroupNo = item.bunkGroupNo
					detail.settleMonth = item.settleMonth;
					detail.settleStartDate = item.settleStartDate;
					detail.settleEndDate = item.settleEndDate;
					detail.status = 1; // 手动导入为未生效的费用
					detail.source = 3; // 费用来源为 手工提交
					detail.taxFlag = costObj.taxFlag; // '是否含税(0:不含税;1:含税)',
					detail.taxRate = costObj.taxRate; // 税率
					detail.refType = costObj.billProperty; // 来源类型：0:手动添加，1：租金条款，2：抽成条款，3：保底条款，4：其他条款,
															// 5: 记成本
					detail.billDebit = costObj.billDebit; // 票扣标识
					detail.accountDebit = costObj.accountDebit; // 账扣标识
					detail.ableSum = ableSum;
					detailArray.push(detail);
				}
			});
			return detailArray;
		}
		
		/**
		 * 根据大区编码和扣项编码 查询扣项的详细信息
		 */
		function findCost(costNoArray, zoneNo, self){
			let costObjArray = [];
			let errorFlag = true;
			for ( var i = 0; i < costNoArray.length; i++) {
			
				let query = Q.And(Q.Equals('deductionNo',costNoArray[i].costNo), Q.Equals('zoneNo', zoneNo)).and(Q.Equals("suitName",2))
				let params = {
					status : 1,
					_q:JSON.stringify(query)
				};
				let data = service.getMdmData(config.mdmUrl, 'depayment', params);
				
				if(data.length>0) {
					costObjArray.push(data[0]);
				} else {
					errorFlag = false;
					let errorMsg= "扣项编码:【" + costNoArray[i].costNo + "】在基础数据里面不存在";
		            self.controls.out.append("<li class='error'>" + errorMsg + "</li>");
		            self.errors = errorMsg;
				}
			}
			if(errorFlag){
				return costObjArray;
			} else {
				return errorFlag;
			}
		}
		
		expt.parserSheet2Json = function(sheet) {
			var val, r, C;
			var self = this;
            var hdr = self.hdr = []; // Excel表头信息列表
            
            var unique = {};
            
            var costNoArray = self.costNoArray = []; // 表头的 扣项列表
            
            
            if (sheet == null || sheet["!ref"] == null) {
                self.callback(null, true);
            }
            var range = sheet["!ref"];
            switch (typeof range) {
                case 'string':
                    r = XLSX.utils.safe_decode_range(range);
                    break;
                case 'number':
                    r = XLSX.utils.safe_decode_range(sheet["!ref"]);
                    r.s.r = range;
                    break;
                default:
                    r = range;
            }
            
            var h = self.h = {};
            h.s = {r: 1, c: 1};
            h.e = {r: 2, c: 1};
            
            var zoneNoCell = sheet['B2'];
            if(zoneNoCell == undefined){
            	throw "请将大区编码填入B2的单元格中。";
            }
            var zoneNo = $.trim(XLSX.utils.format_cell(zoneNoCell));
            

         	for (var i = 0; i <= r.e.c; ++i) { 
         		var c = XLSX.utils.encode_col(i);
         		
         		// 前三列都是 固定的列信息
         		if(i < 3){
                 	val = sheet[c + 3];// 第三行 才是固定的表头信息
                 	if(val == undefined){
                 		continue;
                 	}
                 	var cellVal = $.trim(XLSX.utils.format_cell(val));
                 	var field = cellVal.replace(/\*/ig, '');
         			
         			var required = field.indexOf('*') >= 0;
                 	var cellName = null;
                 	
                 	f2:for (var p in self.options.fields) {
     					if (p == field || p == cellVal || p.replace(/\*/ig, '') == field || field.indexOf(p.replace(/\*/ig, '')) >= 0) {
     						cellName = self.options.fields[p];
     						required = p.indexOf("*") >= 0;
     						break f2;
     					}
     	            }
                 	if (!cellName){
                 		throw "导入Excel的列配置不符合规则。" + field + "列找不到对应信息。";
                    }
                    hdr[i] = {name: cellName, required: required, field: field};
         			
         		} else { // 解析头 从第四列开始解析 前三列都属于 固定信息
                 	let costNo = sheet[c + 2]; // 第二行就是 costNo
                 	let costName = sheet[c + 3]; // 第三行就是 costName
                 	
                 	if(costNo == undefined){
                 		continue;
                 	}
                 	
                 	var costNoVal = $.trim(XLSX.utils.format_cell(costNo));
                 	var costNameVal = $.trim(XLSX.utils.format_cell(costName));
                 	
             		costNoArray.push({'costNo':costNoVal, 'costName': costNameVal})
         		}
         	}
         	
         	
         	
         	var rows = self.rows = [];
         	
         	// r.e.r是总行数 从0 开始计数 ,因为表头从第三行开始（从0开始计数，所以总数是 r.e.r - 3 + 1）
         	var count = self.context.info.total = r.e.r - 2; 
            var R = self.R = 4;// 从第几行 开始解析 这个是从1开始计数
            
            var nop = function (self) {
                if (self.R >= r.e.r + 1) {
                    self.onLoading(count, self.R, null);
                    self.onConvert(count, self.R - h.e.r - 1, null, null);
                }
                self.R += 1;
            };
            
            var task = function () {
            	if (self.R  > r.e.r + 1) {
            		
	                window.clearTimeout(self.interval);
	                var costObjArray = findCost(costNoArray, zoneNo , self);
	                if(costObjArray){
	                	self.callback({data: rows,costArray: costObjArray}, true);
	                } else {
	                	self.callback({data: rows,costArray: costObjArray}, false);
	                }
	                return false;
                }
            	
            	while (self.R <= r.e.r + 1) {
            		var row = {__r__: self.R};
            		var empty = true;
            		
            		var ableSumArray = [];
            		var costAbleSum;
            		
            		f1:for (C = 0; C <= r.e.c; C++) {
            			if(C < 3){
            				f = hdr[C];
            			} else {
            				f = {name: 'ableSum', required: false};
            			}
            			
            			 var c = XLSX.utils.encode_col(C);
                         var val = sheet[c + self.R];
                         if (val == undefined || val.t == undefined || val.v == undefined || !$.trim(val.v)){
                        	 if(C >= 3){
                         		val = {t: "n", v: 0, w: "0"};
                             } else {
                             	// 若有值为空，则看当前行标题范围之内的列的值是否全为空，
                             	// 若全为空则不需要判断是否非空直接忽略当前行
                             	var flag = false;
                             	var RC;
                             	f2:for (RC = 0; RC <= r.e.c; RC++) {
                             		var rc = XLSX.utils.encode_col(RC);
                                     var rv = sheet[rc + self.R];
                             		if(rv && rv.t && rv.v && $.trim(rv.v)){
                             			flag = true;
                             			break f2;
                             		}
                             	}
                             	
                             	if(flag && f.required){
                             		self.error(row, (f.field?f.field:f.name) + "不能为空.");
                             	} else {
                             		break f1;
                             	}
                             }
                         }
                         
                         var v = $.trim(XLSX.utils.format_cell(val));
                         switch (val.t) {
 	                        case 'e':
 	                            continue;
 	                        case 's':
 	                            break;
 	                        case 'b':
 	                        case 'n':
 	                            break;
 	                        default:
 	                            throw 'unrecognized type ' + val.t;
                         }
                         empty = false;
                         
                         if (v !== undefined) {
                        	 
                        	 if(C < 3){
                        		 row[f.name] = v;
                        	 } else {
                        		 costAbleSum =  parseFloat(v);
                        		 if(isNotBlank(costAbleSum)){
                        			 ableSumArray.push(costAbleSum);
                        		 } else {
                        			 self.error(row, '金额格式不正确：' + v);
                        		 }
                        		 
                        	 }
                        	 
                         } else if (!v && f.required) {
                             self.error(row, f.field + "不能为空.");
                         }
            		}
            		
            		
            		if (empty) {// 空行跳过
            			nop(self);
                        continue;
                    }
            		 
            		row.ableSumArray = ableSumArray;
            		
            		if (!self.loadRow(count, self.R, row)) {
            			return false;
            		}
            		
            		if (!self.uniqueValidate(unique, row)) {
                        nop(self);
                        continue;
                    }
            		
            		rows.push(row);
            		 
            		self.onConvert(count, self.R - h.e.r - 1, row, row.skus);
            		
            		if ((self.R % 100) == 0) {
            			self.R += 1;
                        break;
                    }
            		self.R += 1;
            	}
            	return true;
            };
            
            
            var timeout = 250;
            var fn = function () {
                try {
                    if (!self.stoped) {
                        if (task.call(self.R))
                            setTimeout(fn, timeout);
                    } else {
                        $('.excel-info', self.controls.panel).text("已停止");
                        self.callback({data: null, header: null, error: "已中断"}, false);
                    }
                } catch (e) {
                    console.error(e);
                    self.callback({data: null, header: null, error: e + ""}, false);
                }
            };
            self.interval = setTimeout(fn, timeout);
		}
		return expt;
	};
	
	
	function getImporter(){
		return getH2VImporter();
	};
	
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