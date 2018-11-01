if(typeof window.fas == 'undefined' ){
	fas =  {};
}
fas.common = {};
$.fas = {
        datas: {
        		// 合作性质
        		source: [{id: "1", name: "系统生产"},
        		         {id: "2", name: "店铺录入"},
        		         {id: "3", name: "手工提交"}],
        		// 确认id值  票扣标识 
                billDebit:[{id:"1",name:"票扣"},
                           {id:"2",name:"非票扣"}],
                // 确认id值  账扣标识 
                accountDebit:[{id:"1",name:"账扣"},
                              {id:"2",name:"现金"}],
                //是否含税
                taxFlag:[{id:"0",name:"否"},
                         {id:"1",name:"是"}],
				// 单据状态（0-制单 99-作废 100-审核）
				status : [{id : "0", name : "制单"},
				          {id : "1", name : "未生效"},
				          {id : "2", name : "生效"}, 
				          {id : "3", name : "确认"}, 
				          {id : "4", name : "审核"},
				          {id : "5", name : "生成结算单"},
				          {id:"6",name:"已结算"},
				          {id:"7",name:"已生成费用"}, 
				          {id:"99",name:"作废"},
				          {id:"8",name:"未启用"},
				          {id:"9",name:"启用"}],
				// 合作性质
			    businessType: [{id:"1", name: "联营"}, 
			                   {id:"2", name: "租赁"},
			                   {id:"3",name:"场地出租"},
			                   {id:"4",name:"其它"}],
			    endDatas:[{id:"32",name:'自然月'}],
			    // 卖场组设置业务类型
			    storeGroupType: [
			                    {
									'id': '0',
									'name': '存现'
								},{
									'id': '1',
									'name': '终端'
								},{
									'id': '2',
									'name': '卡券'
								}],
				//打印模板类型
				templateType:[{'id':"0",'name':"横排"},{'id':"1",'name':"竖排"},{'id':"2",'name':"供应商结算"}],
				paidWay:[{'id':"1",'name':"自收银"},{'id':"2",'name':"物业收银"}],
				settleStatus:[{'id':"0",'name':"未完结"},{'id':"100",'name':"完结"}],
				refType:[{'id':'0','name':'计费用'},{'id':'1','name':'租金'},{'id':'2','name':'抽成'},{'id':'3','name':'保底'},{'id':'4','name':'其他'},{'id':'5','name':'计成本'}],
				balanceType:[{'id':"0",'name':"同意抵扣"},{'id':"1",'name':"不同意抵扣"}],
				pointsCalculateFlag:[{'id':"0",'name':"否"},{'id':"1",'name':"是"}]
		},
	    datasFunction:function(){
	    	for(let i=1;i<=28;i++){
				let d={};
				d.id = i;
				d.name = i;
				this.datas.endDatas.push(d);
			}
	    }
		
    };


//加载效果
fas.common.loading = function (type, msg) {
    if (msg == null || msg == "") {
        msg = "请稍后,正在加载......";
    }
    var body_width = document.body.clientWidth;
    var body_height = document.body.clientHeight;
    // 展示loading
    if (type == "show") {
    	if($("#myload").length>0){
    		return;
    	}
        var myload = $(
                "<div id='myload' style='border:2px solid #95B8E7;display:inline-block;padding:10px 8px;;position:absolute;z-index:999999999;top:0px;left:0px;background:#ffffff'>"
                + "<div style='float:left;'><img width='20px' src='"+APP_SETTINGS.fas+"/resources/images/loading2.jpg'></div>"
                + "<div style='float:left;display:inline-block;margin-top:2px;margin-left:5px;'>"
                + msg
                + "</div>" + "</div>").appendTo($("body"));
        var myloadwidth = myload.width();
        var myloadheight = myload.height();
        myload.css({
            "left": (body_width - myloadwidth) / 2,
            "top": (body_height - myloadheight) / 2
        });
        $("<div id='remote_load' style='position:fixed;width:100%;height:"
            + body_height
            + "px;z-index:99999999;top:0px;left:0px;background-color: #ccc;opacity: 0.3;filter: alpha(opacity = 30);'></div>")
        .appendTo($("body"));
    } else {
        $("#myload").remove();
        $("#remote_load").remove();
    }
};

function getPdf(fileName,html,url){
	$("<form id='exportExcelForm'  method='post'></form>").appendTo("body");
	var fromObj = $('#exportExcelForm');
	fromObj.form('submit', {
		url : url,
		onSubmit : function(param) {
			param.fileName = fileName;
			param.data = html;
		},
		success : function(result) {
			if (isNotBlank(result.errorMessage)) {
				showError('操作失败!' + result.errorMessage + " " + result.errorDefined);
			} else {
				showSuc("导出成功！");
			}
		}
	});
}

//D为后台返回的CommonResult 对象
function showInfoMes(d,msg){ 
	if(d.errorCode=='0000'){
		$.messager.show({
			title: '消息',
			msg: msg+'成功!',
			showType: 'show',
			timeout:500,
			bottom:" ",
			right:" "
		});
	}else{
		showWarn(d.errorMessage);
	}
	
}

function showExplain(msg){
	 $.messager.show({
			title:'名词解释',
			msg:msg,
			timeout:5000,
			width:600,
			showType:'slide'
		});
}

//检查对象是否为空
function isNotBlank(obj) {
	if (!obj || typeof obj == 'undefined' || obj == '') {
		if('0' == obj){
			return true;
		}
		return false;
	}
	return true;
}

function debug(name) {
	console.info(name);
}

//清除combogrid 的QueryParams
fas.common.clearComboxGridQueryParams=function(jq){
	$(jq).combogrid("grid").datagrid('options').queryParams={};//清除
	$(jq).combogrid("options").queryParams={};
};
var convertCurrency = function(n) {  
    var fraction = ['角', '分'];  
    var digit = [  
        '零', '壹', '贰', '叁', '肆',  
        '伍', '陆', '柒', '捌', '玖'  
    ];  
    var unit = [  
        ['元', '万', '亿'],  
        ['', '拾', '佰', '仟']  
    ];  
    var head = n < 0 ? '负' : '';  
    n = Math.abs(n);  
    var s = '';  
    for (var i = 0; i < fraction.length; i++) {       
	  if (i === fraction.length-1) {
    	  s += (digit[Math.round(n * 10 * Math.pow(10, i)) % 10] + fraction[i]).replace(/零./, '');
	  } else {
    	  s += (digit[Math.floor(n * 10 * Math.pow(10, i)) % 10] + fraction[i]).replace(/零./, '');
	  }
    } 
    s = s || '整';  
    n = Math.floor(n);  
    for (var i = 0; i < unit[0].length && n > 0; i++) {  
        var p = '';  
        for (var j = 0; j < unit[1].length && n > 0; j++) {  
            p = digit[n % 10] + unit[1][j] + p;  
            n = Math.floor(n / 10);  
        }  
        s = p.replace(/(零.)*零$/, '').replace(/^$/, '零') + unit[0][i] + s;  
    }  
    return head + s.replace(/(零.)*零元/, '元')  
        .replace(/(零.)+/g, '零')  
        .replace(/^整$/, '零元整');  
};  

