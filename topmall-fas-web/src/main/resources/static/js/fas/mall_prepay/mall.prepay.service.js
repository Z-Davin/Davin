"use strict";
define(function (require, exports, module) {
    let config = require('../config');
    let Service = require('core/service').Service;

    class mallPrepayService extends Service{
        constructor(){
            super(config.rootUrl + "/mall/prepay");
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
    }

    module.exports = mallPrepayService;
});