"use strict";
define(function (require, exports, module) {
    let config = require('../config');
    let Service = require('core/service').Service;

    class mallBalanceService extends Service{
        constructor(){
            super(config.rootUrl + "/bill/mall/balance");
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
       	batchCreate(params){
       		return $.post(`${this.url}/batchCreate`,params);
       	}
       	
       	unVerify(key){
       		return $.post(`${this.url}/unVerify`,{'billNo':key});
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

    module.exports = mallBalanceService;
});