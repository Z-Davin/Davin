"use strict";
define(function (require, exports, module) {
    let config = require('../config');
    let Service = require('core/service').BillService;
    
    class ShopCostService extends Service{
        constructor(){
            super(config.rootUrl + "/shop/cost");
        }
        confirm(key){
        	return $.post( this.url + "/confirm", {'id': key});
        }
        
        findBySettle(params){
        	return $.ajax({
        			url:this.url + "/settle",
        			data:params,
        			async:false,
        			type:'get'
        		});
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

    module.exports = ShopCostService;
});