"use strict";
define(function (require, exports, module) {
    let config = require('../config');
    let Service = require('core/service').Service;

    class counterBalanceService extends Service{
        constructor(){
            super(config.rootUrl + "/bill/counter/balance");
        }
       	save(params){
       		return $.post(`${this.url}/save`,params);
       	}
       	deleteBill(key){
       		return $.post(`${this.url}/deleteBill`,{'billNo':key});
       	}
       	verify(key){
       		return $.post(`${this.url}/verify`,{'billNo':key});
       	}
    	unVerify(key){
       		return $.post(`${this.url}/unVerify`,{'billNo':key});
       	}
    	printCount(key){
       		return $.post(`${this.url}/printCount`,{'billNo':key});
       	}
       	batchVerify(keys){
       		return $.post(this.url+"/batchverify",{'billNos': JSON.stringify(keys)});
       	}
       	batchCreate(params){
       		return $.post(`${this.url}/batchCreate`,params);
       	}
       	getBillInfo(billNo,templateType){
       		return $.get(`${this.url}/print`,{'billNo':billNo,'templateType':templateType});
       	}
       	
    	getPdf(billNo,data){
       		return $.post(`${this.url}/getPdf`,{'billNo':billNo,'data':data});
       	}

        getBillInfos(params,templateType){
            return $.post(`${this.url}/batchprint/`+templateType,params);
        }

        selectByParams(params){
            return $.get(this.url + "/query", params);
        }
    }

    module.exports = counterBalanceService;
});