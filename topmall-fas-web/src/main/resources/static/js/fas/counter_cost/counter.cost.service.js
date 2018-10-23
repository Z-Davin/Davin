"use strict";
define(function (require, exports, module) {
    let config = require('../config');
    let Service = require('core/service').BillService;
    
    class CounterCostService extends Service{
        constructor(){
            super(config.rootUrl + "/counter/cost");
        }
        confirm(keys){
        	return $.post( this.url + "/confirm", {'ids': JSON.stringify(keys)});
        }
        
        unConfirm(keys){
        	return $.post( this.url + "/unConfirm", {'ids': JSON.stringify(keys)});
        }
        
        findBySettle(params){
        	return $.ajax({
        			url:this.url + "/settle",
        			data:params,
        			async:false,
        			type:'get'
        		});
        }
        
        selectByParams(params){
        return $.get(this.url + "/query", params);
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

    module.exports = CounterCostService;
});