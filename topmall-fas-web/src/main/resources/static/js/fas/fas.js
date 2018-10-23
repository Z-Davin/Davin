(function(){
    var path = "/fas/resources/js";
    seajs.config({
        "base": path+ "/fas",
        map: [
            [/^(.*\.(?:css|js))(.*)$/i, '$1?' + window.version]
        ],
        "paths": APP_SETTINGS.config("fas"),
//        console.info(APP_SETTINGS.mdm);
        alias: {
            'mdm_plugin':APP_SETTINGS.mdm+'/../resources/js/mdm/utils/plugin.js'
          }
//        "paths": {
//            "core": app + "/core",
//            "lib": resourcesUrl +"/lib",
//            "security": resourcesUrl + "/security"
//        }
    });
})();
