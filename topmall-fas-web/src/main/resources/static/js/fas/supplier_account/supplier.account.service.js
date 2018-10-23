"use strict";
define(function (require, exports, module) {
    let config = require('../config');
    let Service = require('core/service').BillService;
    
    class SupplierAccountService extends Service{
        constructor(){
            super(config.rootUrl + "/supplier/account");
        }
        
        selectByParams(params){
        	return $.get(this.url + "/query", params);
        }
        
        operation(keys,operation){
        	return $.post( this.url + "/operation", {'ids': JSON.stringify(keys),'operation':operation});
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

    module.exports = SupplierAccountService;
});