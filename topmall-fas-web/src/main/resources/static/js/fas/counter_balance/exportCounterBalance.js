"use strict";
/**
 * 结算单打印
 */
define(function (require, exports, module) {
    let tableExport = require('core/io/excel/tableExport');
    let template = require('lib/template');
    let printCounterBalance = require('./printCounterBalance');
    let acrossTemplate = `
<div class="receipt" style="font-size: 12px; font-weight:600;width:98%; margin-left:1%; margin-bottom:5px; font-size: 10px; font-family:'Microsoft YaHei';">
    <div class="bTitle" style="margin: 0; margin-top:5px ; padding: 0; font-size:22px; font-weight:600; text-align: center;">{{shopName}}</div>
    <div class="title" style="text-align: center; font-size:16px; margin-bottom:10px; font-weight: 600;">结算单</div>
    <table class="border" cellspacing="0" style="width:98%; margin-left:1%; margin-bottom:5px; font-size: 10px; font-family:'Microsoft YaHei';border:1px solid #000;">        
		    <tbody>
		    <tr>
		        <td style="border:1px solid #000;" align="center" colspan="2">结算单号:</td>
		        <td style="border:1px solid #000;" align="center" colspan="2">{{billNo}}</td>
		        <td style="border:1px solid #000;" align="center" colspan="2">公司税号:</td>
		        <td style='border:1px solid #000;mso-number-format:\"\@\";' align="center" colspan="2">{{suTaxNo}}</td>                
		    </tr>
		    <tr>
		        <td style="border:1px solid #000;" align="center" colspan="2">公司名称</td>
				<td style="border:1px solid #000;" align="center" colspan="2">{{suCompanyName}}</td>
		        <td style="border:1px solid #000;" align="center" colspan="2">银行账号</td>
		        <td style='border:1px solid #000;mso-number-format:\"\@\";' align="center" colspan="2">{{suBankAccount}}</td>
		    </tr>
		    <tr>
		        <td style="border:1px solid #000;" align="center" colspan="2">公司地址</td>
		        <td style="border:1px solid #000;" align="center" colspan="2">{{suAddress}}</td>
		        <td style="border:1px solid #000;" align="center" colspan="2">制单日期</td>
		        <td style="border:1px solid #000;" align="center" colspan="2">{{printDate}}</td>	            
		    </tr>
		    <tr>
		        <td style="border:1px solid #000;" align="center" colspan="2">开户行及行号</td>
		        <td style="border:1px solid #000;" align="center" colspan="2">{{suBankName}}</td>
		        <td style="border:1px solid #000;" align="center" colspan="2">结算区间</td>
		        <td style="border:1px solid #000;" align="center" colspan="2">{{settleMonth}}</td>	            
		    </tr> 
		    <tr>
		        <td style="border:1px solid #000;" align="center" colspan="2">专柜名称</td>
		        <td style="border:1px solid #000;" align="center" colspan="2">{{counterName}}</td>
		        <td style="border:1px solid #000;" align="center" colspan="2">合作方式</td>
		        <td style="border:1px solid #000;" align="center" colspan="2">{{businessType}}</td>	            
		    </tr>
		    <tr>
		        <td style="border:1px solid #000;" align="center" colspan="2">专柜编码</td>
		        <td style="border:1px solid #000;" align="center" colspan="2">{{counterNo}}</td>
		        <td style="border:1px solid #000;" align="center" colspan="2">银行账户名</td>
		        <td style="border:1px solid #000;" align="center" colspan="2">{{subBankAccountName}}</td>	            
		    </tr>             
		</tbody>
    </table>
    <div style="font-size: 12px; font-weight:600;">一、货款结算部分</div>
    <table class="border" cellspacing="0" style="width:98%; margin-left:1%; margin-bottom:5px; font-size: 10px; font-family:'Microsoft YaHei';border:1px solid #000;">            
        <tbody>            
            <tr>
            	<td align="center" style="border:1px solid #000;" colspan="4">结算项目</td>
            	<td align="center" style="border:1px solid #000;" colspan="2">销售金额</td>
            	<td align="center" style="border:1px solid #000;" colspan="2">销售扣款</td>                 
            </tr>
            {{each counterSaleCostList as counterSale index}}
            <tr>            	           
              	<td align="center" style="border:1px solid #000;" colspan="4">{{counterSale.divisionNo}}{{counterSale.divisionName}}</td>
              	<td align="center" style="border:1px solid #000;" colspan="2">{{counterSale.settleSum}}</td>
              	<td align="center" style="border:1px solid #000;" colspan="2">{{counterSale.profitAmount}}</td>            	
            </tr>
            {{/each}}
            <tr>            	
	          	<td align="center" style="border:1px solid #000;" colspan="4">合计</td>
	          	<td align="center" style="border:1px solid #000;" colspan="2">{{settleSumTotal}}</td>
	          	<td align="center" style="border:1px solid #000;" colspan="2">{{profitAmountTotal}}</td>
	        </tr>
        </tbody>
    </table>
    <div style="font-size: 12px; font-weight:600;">二、费用结算部分</div>
    <table><tr><td>
    <table class="half border" cellspacing="0" style="margin-left:1%; margin-bottom:5px; font-size: 10px; font-family:'Microsoft YaHei';width:48.9%; float:left;border:1px solid #000;">     	     
        <tbody>            
            <tr>
            	<td align="center" style="border:1px solid #000;">序号</td>
                <td align="center" style="border:1px solid #000;">费用项目</td>
              	<td align="center" style="border:1px solid #000;">金额</td> 
              	<td align="center" style="border:1px solid #000;">费用类别</td>
            </tr>
            {{each counterCostList1 as counter index}}
            <tr>	            
              	<td align="center" style="border:1px solid #000;">{{index+1}}</td>
              	<td align="center" style="border:1px solid #000;">{{counter.costName}}</td>
              	<td align="center" style="border:1px solid #000;">{{counter.ableSum}}</td>
              	<td align="center" style="border:1px solid #000;">{{counter.billDebit}}</td>
            </tr>
            {{/each}}   
        	<tr>
		        <td align="center" style="border:1px solid #000;" colspan="2">票扣费用合计</td>
		        <td align="center" style="border:1px solid #000;" colspan="2">{{billDebitTotal}}</td>
	        </tr>	        
    	</tbody>
    </table>
    </td>
    <td>
    <table class="half leftborder border" cellspacing="0" style=" margin-bottom:5px; font-size: 10px; font-family:'Microsoft YaHei';width:48.9%; float:left;border:1px solid #000;margin-left:0;">   
	    <tbody>            
	        <tr>
	        	<td align="center" style="border:1px solid #000;">序号</td>
	            <td align="center" style="border:1px solid #000;">费用项目</td>
	          	<td align="center" style="border:1px solid #000;">金额</td>
	          	<td align="center" style="border:1px solid #000;">费用类别</td> 
	        </tr>
	        {{each counterCostList2 as counter index}}
	        <tr>	            
	          	<td align="center" style="border:1px solid #000;">{{counterCostList1.length + index+1}}</td>
	          	<td align="center" style="border:1px solid #000;">{{counter.costName}}</td>
	          	<td align="center" style="border:1px solid #000;">{{counter.ableSum}}</td>
	          	<td align="center" style="border:1px solid #000;">{{counter.billDebit}}</td>
	        </tr>
	        {{/each}}  
	        <tr>
		        <td align="center" style="border:1px solid #000;" colspan="2">非票扣费用合计</td>
		        <td align="center" style="border:1px solid #000;" colspan="2">{{notBillDebitTotal}}</td>
	        </tr>
    	</tbody>
	</table>
	</td></tr></table>
	<div style="font-size: 12px; font-weight:600;">三、其他部分</div>
    <table><tr><td>
    <table class="half border" cellspacing="0" style="margin-left:1%; margin-bottom:5px; font-size: 10px; font-family:'Microsoft YaHei';width:48.9%; float:left;border:1px solid #000;">     	     
        <tbody>            
            <tr>
            	<td align="center" style="border:1px solid #000;">序号</td>
                <td align="center" style="border:1px solid #000;">费用项目</td>
              	<td align="center" style="border:1px solid #000;">金额</td> 
              	<td align="center" style="border:1px solid #000;">费用类别</td>
            </tr>
            {{each otherCostList1 as other index}}
            <tr>	            
              	<td align="center" style="border:1px solid #000;">{{index+1}}</td>
              	<td align="center" style="border:1px solid #000;">{{other.costName}}</td>
              	<td align="center" style="border:1px solid #000;">{{other.ableSum}}</td>
              	<td align="center" style="border:1px solid #000;">{{other.accountDebit}}</td>
            </tr>
            {{/each}}   
        	<tr>
		        <td align="center" style="border:1px solid #000;" colspan="2">账扣合计</td>
		        <td align="center" style="border:1px solid #000;" colspan="2">{{accountDebitTotal}}</td>
	        </tr>	        
    	</tbody>
    </table>
    </td>
    <td>
    <table class="half leftborder border" cellspacing="0" style=" margin-bottom:5px; font-size: 10px; font-family:'Microsoft YaHei';width:48.9%; float:left;border:1px solid #000;margin-left:0;">   
	    <tbody>            
	        <tr>
	        	<td align="center" style="border:1px solid #000;">序号</td>
	            <td align="center" style="border:1px solid #000;">费用项目</td>
	          	<td align="center" style="border:1px solid #000;">金额</td>
	          	<td align="center" style="border:1px solid #000;">费用类别</td> 
	        </tr>
	        {{each otherCostList2 as other index}}
	        <tr>	            
	          	<td align="center" style="border:1px solid #000;">{{otherCostList1.length + index+1}}</td>
	          	<td align="center" style="border:1px solid #000;">{{other.costName}}</td>
	          	<td align="center" style="border:1px solid #000;">{{other.ableSum}}</td>
	          	<td align="center" style="border:1px solid #000;">{{other.accountDebit}}</td>
	        </tr>
	        {{/each}}  
	        <tr>
		        <td align="center" style="border:1px solid #000;" colspan="2">现金合计</td>
		        <td align="center" style="border:1px solid #000;" colspan="2">{{cashTotal}}</td>
	        </tr>
    	</tbody>
	</table>
	</td></tr></table>
	{{if !(businessType == '租赁' && (zoneNo == 'C' || zoneNo == 'E'))}}
	<table class="border" cellspacing="0" style="width:98%; margin-left:1%; margin-bottom:5px; font-size: 10px; font-family:'Microsoft YaHei';border:1px solid #000;">    			
        <tbody>
            <tr>
            	<td class="leftTd" width="40%" style="border:1px solid #000;" colspan="2">四、开发票金额</td>
                <td align="center" width="20%" style="border:1px solid #000;">开票数量</td>
                <td align="center" style="border:1px solid #000;" colspan="2">{{saleQty}}</td>             
                <td align="center" style="border:1px solid #000;">开票总额</td>
	            <td align="center" style="border:1px solid #000;" colspan="2">{{ableBillingSum}}</td>
            </tr>
        </tbody>
    </table>
    {{/if}}
    <table class="border" cellspacing="0" style="width:98%; margin-left:1%; margin-bottom:5px; font-size: 10px; font-family:'Microsoft YaHei';border:1px solid #000;">
    	<tbody>
    		<tr>
    			<td width="200" class="leftTd" style="border:1px solid #000;" colspan="2">
    			{{if (businessType == '租赁' && zoneNo == "C") || (businessType == '租赁' && zoneNo == 'E')}}
				四、
				{{else}}
				五、
				{{/if}}
				应付款项(大写)
    			</td>
    			<td width="50%" align="center" style="border:1px solid #000;" colspan="4">{{payName}}</td>
    			<td align="center" style="border:1px solid #000;" colspan="2" >{{ableSum}}</td>
    		</tr>
    	</tbody>
    </table>
    <table style="width:98%; margin-left:1%; margin-bottom:5px; font-size: 10px; font-family:'Microsoft YaHei';border:1px solid #000;">
    <tbody>
    <tr>
    	<td style="border:1px solid #000;border-left:none;" align="left" rowspan="9">说明</td>
        <td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" width="50" rowspan="2">1 </td>
        <td style="border:1px solid #000;border-left:none;border-bottom:none;" align="left" colspan="5">{{remark1}}</td>
        <td style="border:1px solid #000;border-left:none;border-bottom:none;" align="left" rowspan="9">供应商负责人签字盖章</td>
    </tr>
    <tr> 
		<td align="left" style="border:1px solid #000;border-left:none;border-bottom:none;" colspan="5">{{remark2}}</td>
	</tr>
    <tr>
        <td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" width="50" rowspan="6">2 </td>
        <td style="border:1px solid #000;border-left:none;border-bottom:none;" align="left" colspan="5">请一定按我司提供资料开票，如开错发票，由贵司负责所造成之一切问题。</td>
    </tr>
    <tr> 
	    <td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" width="100">公司全称：</td> 
		<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" colspan="4">{{companyName}}</td>
    </tr>
    <tr> 
		<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" width="100">公司地址：</td>
		<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" colspan="4">{{address}}</td>
	</tr>
	<tr> 
		<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" width="100">开户行：</td> 
		<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center"colspan="4">{{bankName}}</td>
	</tr>
	<tr> 
		<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" width="100">账号：</td> 
		<td style='border:1px solid #000;border-left:none;border-bottom:none;mso-number-format:\"\@\";' align="center"colspan="4">{{bankAccount}}</td>
	</tr>
	<tr> 
		<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" width="100">税号：</td> 
		<td style='border:1px solid #000;border-left:none;border-bottom:none;mso-number-format:\"\@\";' align="center" colspan="4">{{taxNo}}</td>
	</tr>
    <tr>
	    <td style="border:1px solid #000;border-left:none;" align="center" width="50">3 </td>
		<td style="border:1px solid #000;border-left:none;" align="center" width="100">制单人：</td>
		<td style="border:1px solid #000;border-left:none;" align="center" width="150">{{createUser}}</td>     
		<td style="border:1px solid #000;border-left:none;" align="center" width="100">店长：</td>
		<td style="border:1px solid #000;border-left:none;" align="center" width="150" colspan="2"></td>
	</tr>
</tbody>
</table>
</div>
    `;
    let verticalTemplate = `
    <div class="receipt" style="font-size:10px;font-weight:600;width:98%;margin-left:1%;margin-bottom:5px;font-family:'Microsoft YaHei';">
    <div class="bTitle" style="margin: 0;margin-top:5px ;padding: 0;font-size:22px;font-weight:600;text-align: center;">{{shopName}}</div>
	<div class="title" style="text-align: center;font-size:16px;margin-bottom:10px;font-weight: 600;">结算单</div>
        <table class="border" style="width:98%;margin-left:1%;margin-bottom:5px;border:1px solid #000;border-top:none;border-right:none;" cellspacing="0">           
            <tbody>            
                <tr>
                	<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center">结算单号:</td>
                	<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" colspan="2">{{billNo}}</td>                           	
                  	<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center">公司税号:</td>
                  	<td style='border:1px solid #000;mso-number-format:\"\@\";' align="center" colspan="2">{{suTaxNo}}</td>
                </tr>
                <tr>
            		<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center">公司名称:</td>
            		<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" colspan="2">{{suCompanyName}}</td>                          	
                	<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center">银行账号:</td>
                	<td style='border:1px solid #000;mso-number-format:\"\@\";' align="center" colspan="2">{{suBankAccount}}</td>
                </tr>
                <tr>
                	<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center">公司地址:</td>
                	<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" colspan="2">{{suAddress}}</td>                            
                	<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center">制单日期:</td>
                	<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" colspan="2">{{printDate}}</td>
                </tr>
                <tr>
                	<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center">开户行及行号:</td>
                	<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" colspan="2">{{suBankName}}</td>                  
                	<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center">结算区间:</td>
                	<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" colspan="2">{{settleMonth}}</td>
                </tr>
                <tr>
	            	<td style="border:1px solid #000;border-left:none;" align="center">供应商:</td>
	            	<td style="border:1px solid #000;border-left:none;" align="center" colspan="2">{{supplierName}}</td>                   
	            	<td style="border:1px solid #000;border-left:none;" align="center">专柜:</td>
	            	<td style="border:1px solid #000;border-left:none;" align="center" colspan="2">{{counterName}}</td>
            	</tr>
            	<tr>
	            	<td style="border:1px solid #000;border-left:none;" align="center">银行账户名:</td>
	            	<td style="border:1px solid #000;border-left:none;" align="center" colspan="2">{{subBankAccountName}}</td>                   
	            	<td style="border:1px solid #000;border-left:none;" align="center">专柜编码:</td>
	            	<td style="border:1px solid #000;border-left:none;" align="center" colspan="2">{{counterNo}}</td>
	             </tr>
            </tbody>
        </table>
        <table class="border" style="width:98%;margin-left:1%;margin-bottom:5px;border:1px solid #000;border-top:none;border-right:none;" cellspacing="0">     
            <tbody>            
                <tr>
                	<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" colspan="3">结算项目</td>
                    <td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center">营业额</td>
                  	<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center">扣率</td> 
                  	<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center">抽成</td> 
                </tr>
                {{each counterSaleCostList as counterSale index}}
                <tr>	                     	
                  	<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" colspan="3">{{counterSale.divisionNo}}{{counterSale.divisionName}}</td>
                  	<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center">{{counterSale.settleSum}}</td>
                  	<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center">{{counterSale.rateValue}}</td>   
                  	<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center">{{counterSale.profitAmount}}</td>                  	
                </tr>
                {{/each}}
        		<tr> 
              		<td style="border:1px solid #000;border-left:none;" align="center" colspan="3">合计1:</td>
              		<td style="border:1px solid #000;border-left:none;" align="center">{{settleSumTotal}}</td>
              		<td style="border:1px solid #000;border-left:none;" align="center">{{rateValueTotal}}</td>
              		<td style="border:1px solid #000;border-left:none;" align="center">{{profitAmountTotal}}</td>
              	</tr>
            </tbody>
        </table>
        <table class="border" style="width:98%;margin-left:1%;margin-bottom:5px;border:1px solid #000;border-top:none;border-right:none;" cellspacing="0">   
    	    <tbody>            
    	        <tr>
    	        	<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" colspan="2" rowspan={{UniqcounterCostList.length+1}}>费用扣款明细</td>
    	            <td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" colspan="1">扣款项目</td>
    	          	<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center">相关税费</td>
    	          	<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center">扣款金额</td>
    	          	<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center">含税金额</td> 
    	        </tr>   	                 	        	
	        	{{each UniqcounterCostList as counter index}}
	        	<tr>	
    	          	<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" colspan="1">{{counter.costName}}</td>
    	          	<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center">{{counter.taxAmount}}</td>
    	          	<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center">{{counter.ableAmount}}</td>
    	          	<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center">{{counter.ableSum}}</td>
	          	</tr>
	          	{{/each}}  	        
    	        <tr>
            		<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" colspan="2"></td>
                	<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" colspan="1">费用总计</td>
              		<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center">税费总计</td>
              		<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center">扣款金额总计</td> 
              		<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center">含税金额总计</td>
              	</tr>
              	<tr> 
              		<td style="border:1px solid #000;border-left:none;" align="center" colspan="2">合计2:</td> 
              		<td style="border:1px solid #000;border-left:none;" align="center" colspan="1">{{costTotal}}</td>
              		<td style="border:1px solid #000;border-left:none;" align="center">{{taxAmountTotal}}</td>
              		<td style="border:1px solid #000;border-left:none;" align="center">{{ableAmountTotal}}</td>
              		<td style="border:1px solid #000;border-left:none;" align="center">{{ableSumTotal}}</td>
        		</tr>
    	    </tbody>
    	</table>
    	<table class="border" style="width:98%;margin-left:1%;margin-bottom:5px;border:1px solid #000;border-top:none;border-right:none;" cellspacing="0">   
		    <tbody>            
		        <tr>
		        	<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" colspan="2" rowspan={{UniqOtherCostList.length+1}}>其他部分</td>
		            <td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" colspan="2">扣款项目</td>
		          	<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center">相关税费</td>
		          	<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center">扣款金额</td>              	
		        </tr>   	                 	        	
	        	{{each UniqOtherCostList as other index}}
	        	<tr>	
		          	<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" colspan="2">{{other.costName}}</td>
		          	<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center">{{other.taxAmount}}</td>
		          	<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center">{{other.ableAmount}}</td>
	          	</tr>
	          	{{/each}}  	        
		        <tr>
	        		<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" colspan="2"></td>
	            	<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" colspan="2">费用总计</td>
	          		<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center">税费总计</td>
	          		<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center">扣款金额总计</td>              	
	          	</tr>
	          	<tr> 
	          		<td style="border:1px solid #000;border-left:none;" align="center" colspan="2">合计3:</td> 
	          		<td style="border:1px solid #000;border-left:none;" align="center" colspan="2">{{otherCostTotal}}</td>
	          		<td style="border:1px solid #000;border-left:none;" align="center">{{otherTaxAmountTotal}}</td>
	          		<td style="border:1px solid #000;border-left:none;" align="center">{{otherAbleAmountTotal}}</td>
	    		</tr>
		    </tbody>
	    </table>       
    	<table class="border" style="width:98%;margin-left:1%;margin-bottom:5px;border:1px solid #000;border-top:none;border-right:none;" cellspacing="0">    			
            <tbody>
                <tr>
                    <td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" colspan="3" width="50%">本期实际应付款</td>
                    <td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" colspan="3">{{ableSum}}</td>             
                </tr>
                <tr>
                    <td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" colspan="3">本期销售数量</td>
                    <td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" colspan="3">{{saleQty}}</td>              
                </tr>
                <tr>
    	            <td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" colspan="3">本期提供增值税发票金额</td>
    	            <td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" colspan="3">{{currentInvoice}}</td>              
    	        </tr>
    	        <tr>
	                <td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" colspan="3">税额</td>
	                <td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" colspan="3">{{currentTaxValue}}</td>              
                </tr>
                <tr>
	                <td style="border:1px solid #000;border-left:none;" align="center" colspan="3">价税合计</td>
	                <td style="border:1px solid #000;border-left:none;" align="center" colspan="3">{{ableBillingSum}}</td>              
                </tr>
            </tbody>
        </table>
        <table class="border" style="width:98%;margin-left:1%;margin-bottom:5px;border:1px solid #000;border-top:none;border-right:none;" cellspacing="0">    			
        <tbody>
            <tr>
            	<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="left" rowspan="11">注明</td>
                <td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" width="50">1 </td>
                <td style="border:1px solid #000;border-left:none;border-bottom:none;" align="left" colspan="4">我司将与各专柜确认结算单金额</td>             
            </tr>
            <tr>
                <td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" width="50" rowspan="2">2 </td>
                <td style="border:1px solid #000;border-left:none;border-bottom:none;" align="left" colspan="4">{{remark1}}</td>
            </tr>
            <tr> 
            	<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="left" colspan="4">{{remark2}}</td>
            </tr>
            <tr>
                <td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" width="50" rowspan="6">3 </td>
                <td style="border:1px solid #000;border-left:none;border-bottom:none;" align="left" colspan="4">请一定按我司提供资料开票，如开错发票，由贵司负责所造成之一切问题。</td>
            </tr>
            <tr> 
            	<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" width="100">公司全称：</td> 
            	<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" colspan="3">{{companyName}}</td>
            </tr>
            <tr> 
        		<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" width="100">公司地址：</td>
        		<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" colspan="3">{{address}}</td>
        	</tr>
        	<tr> 
        		<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" width="100">开户行：</td> 
        		<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center"colspan="3">{{bankName}}</td>
        	</tr>
        	<tr> 
    			<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" width="100">账号：</td> 
    			<td style='border:1px solid #000;border-left:none;border-bottom:none;mso-number-format:\"\@\";' align="center"colspan="3">{{bankAccount}}</td>
    		</tr>
    		<tr> 
    			<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" width="100">税号：</td> 
    			<td style='border:1px solid #000;border-left:none;border-bottom:none;mso-number-format:\"\@\";' align="center"colspan="3">{{taxNo}}</td>
    		</tr>
            <tr>
            	<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" width="50" rowspan="2">4 </td>
            	<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="left"colspan="4">厂商对本结算单有疑义，请于发票送达前与我司商务部联系。</td>
            </tr>
            <tr>      
        		<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" width="100">联系人：</td>
        		<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" width="150">{{createUser}}</td>     
    			<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" width="100">联系人电话：</td>
    			<td style='border:1px solid #000;border-left:none;border-bottom:none;mso-number-format:\"\@\";' align="center" width="150">{{tel}}</td>
    		</tr>
    		<tr>      
	    		<td style="border:1px solid #000;border-left:none;" align="center" colspan="3">厂商负责人签字、盖章：</td>  
				<td style="border:1px solid #000;border-left:none;" align="center" colspan="3">（另：请确认付款方式：A电汇 B汇票 C支票）</td>
			</tr>
        </tbody>
    </table>
   <div>
        `;
        let supplierTemplate = `    
        <div class="receipt" style="font-size: 12px; font-weight:600;width:98%; margin-left:1%; margin-bottom:5px; font-size: 10px; font-family:'Microsoft YaHei';">
        <div class="bTitle" style="margin: 0; margin-top:5px ; padding: 0; font-size:22px; font-weight:600; text-align: center;">{{supplierName}}</div>
        <div class="title" style="text-align: center; font-size:16px; margin-bottom:10px; font-weight: 600;">结算单</div>
    	    <table class="border" cellspacing="0" style="width:98%; margin-left:1%; margin-bottom:5px; font-size: 10px; font-family:'Microsoft YaHei';border:1px solid #000;">        
    			    <tbody>
    			    <tr>
    			        <td style="border:1px solid #000;" align="center" colspan="2">结算单号:</td>
    			        <td style="border:1px solid #000;" align="center" colspan="2">{{billNo}}</td>
    			        <td style="border:1px solid #000;" align="center" colspan="2">供应商税号:</td>
    			        <td style='border:1px solid #000;mso-number-format:\"\@\";' align="center" colspan="2">{{suTaxNo}}</td>                
    			    </tr>
    			    <tr>
    			        <td style="border:1px solid #000;" align="center" colspan="2">供应商名称</td>
    					<td style="border:1px solid #000;" align="center" colspan="2">{{supplierName}}</td>
    			        <td style="border:1px solid #000;" align="center" colspan="2">银行账号</td>
    			        <td style='border:1px solid #000;mso-number-format:\"\@\";' align="center" colspan="2">{{suBankAccount}}</td>
    			    </tr>
    			    <tr>
    			        <td style="border:1px solid #000;" align="center" colspan="2">供应商地址</td>
    			        <td style="border:1px solid #000;" align="center" colspan="2">{{suAddress}}</td>
    			        <td style="border:1px solid #000;" align="center" colspan="2">制单日期</td>
    			        <td style="border:1px solid #000;" align="center" colspan="2">{{printDate}}</td>	            
    			    </tr>
    			    <tr>
    			        <td style="border:1px solid #000;" align="center" colspan="2">供应商开户行</td>
    			        <td style="border:1px solid #000;" align="center" colspan="2">{{suBankName}}</td>
    			        <td style="border:1px solid #000;" align="center" colspan="2">结算区间</td>
    			        <td style="border:1px solid #000;" align="center" colspan="2">{{settleMonth}}</td>	            
    			    </tr>           
    			</tbody>
    	    </table>
    	    <table class="border" cellspacing="0" style="width:98%; margin-left:1%; margin-bottom:5px; font-size: 10px; font-family:'Microsoft YaHei';border:1px solid #000;">
		    	<tbody>
			    	<tr>
			        	<td align="center" style="border:1px solid #000;">本期销售额</td>
			            <td align="center" style="border:1px solid #000;" colspan="2">{{saleAmount}}</td>
			          	<td align="center" style="border:1px solid #000;">本期销售数量</td> 
			          	<td align="center" style="border:1px solid #000;">{{saleQty}}</td>
			          	<td align="center" style="border:1px solid #000;">应结总额</td> 
			          	<td align="center" style="border:1px solid #000;" colspan="2">{{ableSum}}</td>
			        </tr>
			        <tr>
			        	<td align="center" style="border:1px solid #000;">开票价款</td>
			            <td align="center" style="border:1px solid #000;" colspan="2">{{currentInvoice}}</td>
			          	<td align="center" style="border:1px solid #000;">开票税款</td> 
			          	<td align="center" style="border:1px solid #000;">{{currentTaxValue}}</td>
			          	<td align="center" style="border:1px solid #000;">开票额</td> 
			          	<td align="center" style="border:1px solid #000;" colspan="2">{{ableBillingSum}}</td>
			        </tr>
		    	</tbody>
		    </table>
    	    <table class="border" cellspacing="0" style="width:98%; margin-left:1%; margin-bottom:5px; font-size: 10px; font-family:'Microsoft YaHei';border:1px solid #000;">            
    	        <tbody>            
    	            <tr>
    	            	<td align="center" style="border:1px solid #000;" colspan="2">门店名称</td>
    	            	<td align="center" style="border:1px solid #000;">专柜名称</td>
    	            	<td align="center" style="border:1px solid #000;" colspan="2">结算项目</td>
    	            	<td align="center" style="border:1px solid #000;">营业额</td>
    	            	<td align="center" style="border:1px solid #000;">扣率</td> 
    	            	<td align="center" style="border:1px solid #000;">抽成</td>
    	            </tr>
    	            {{each counterSaleCostList as counterSale index}}
    	            <tr>
    	            	<td align="center" style="border:1px solid #000;" colspan="2">{{counterSale.shopName}}</td>   
    	            	<td align="center">{{counterSale.counterName}}</td>
    	              	<td align="center" style="border:1px solid #000;" colspan="2">{{counterSale.divisionNo}}{{counterSale.divisionName}}</td>
    	              	<td align="center" style="border:1px solid #000;">{{counterSale.settleSum}}</td>
    	              	<td align="center" style="border:1px solid #000;">{{counterSale.rateValue}}</td>
    	              	<td align="center" style="border:1px solid #000;">{{counterSale.profitAmount}}</td>            	
    	            </tr>
    	            {{/each}}
    	            <tr>            	
    		          	<td align="center" style="border:1px solid #000;" colspan="2">合计</td>
    		          	<td align="center" style="border:1px solid #000;"></td>
    	          		<td align="center" style="border:1px solid #000;" colspan="2"></td>
    		          	<td align="center" style="border:1px solid #000;">{{settleSumTotal}}</td>
    		          	<td align="center" style="border:1px solid #000;"></td>
    		          	<td align="center" style="border:1px solid #000;">{{profitAmountTotal}}</td>
    		        </tr>
    	        </tbody>
    	    </table>
    	    <table class="border" cellspacing="0" style="width:98%; margin-left:1%; margin-bottom:5px; font-size: 10px; font-family:'Microsoft YaHei';border:1px solid #000;">            
	        <tbody>            
	            <tr>
	            	<td align="center" style="border:1px solid #000;" colspan="2">门店名称</td>
	            	<td align="center" style="border:1px solid #000;">专柜名称</td>
	            	<td align="center" style="border:1px solid #000;">扣项名称</td>
	            	<td align="center" style="border:1px solid #000;" colspan="2">扣项总额</td>
	            	<td align="center" style="border:1px solid #000;">扣项类别</td>
	            	<td align="center" style="border:1px solid #000;">支付方式</td>
	            </tr>
	            {{each UniqcounterCostList as counter index}}
	            <tr>               	
	            	<td align="center" style="border:1px solid #000;" colspan="2">{{counter.shopName}}</td>   
	            	<td align="center" style="border:1px solid #000;">{{counter.counterName}}</td>
	              	<td align="center" style="border:1px solid #000;">{{counter.costName}}</td>
	              	<td align="center" style="border:1px solid #000;" colspan="2">{{counter.ableSum}}</td>
	              	<td align="center" style="border:1px solid #000;">{{counter.billDebit}}</td>
	              	<td align="center" style="border:1px solid #000;">{{counter.accountDebit}}</td>
	            </tr>
	            {{/each}}
	            <tr>            	
		          	<td align="center" style="border:1px solid #000;" colspan="2">合计</td>
		          	<td align="center" style="border:1px solid #000;"></td>
	          		<td align="center" style="border:1px solid #000;"></td>
		          	<td align="center" style="border:1px solid #000;" colspan="2">{{ableSumTotal}}</td>
		          	<td align="center" style="border:1px solid #000;">{{billDebitTotal}}</td>
		          	<td align="center" style="border:1px solid #000;">{{notBillDebitTotal}}</td>
		        </tr>
	        </tbody>
	    </table>
	    <table style="width:98%; margin-left:1%; margin-bottom:5px; font-size: 10px; font-family:'Microsoft YaHei';border:1px solid #000;">
    	    <tbody>
    	    <tr>
    	    	<td style="border:1px solid #000;border-left:none;" align="left" colspan="2" rowspan="10">注明</td>
    	        <td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" colspan="2" width="50">1 </td>
    	        <td align="left" colspan="4" style="border:1px solid #000;border-left:none;border-bottom:none;">我司将与各专柜确认结算单金额</td>
    	    </tr>
    	    <tr> 
    			<td align="center" style="border:1px solid #000;border-left:none;border-bottom:none;" colspan="2" width="50" rowspan="2">2 </td>
	            <td align="left" style="border:1px solid #000;border-left:none;border-bottom:none;" colspan="4">{{remark1}}</td>
			</tr>
    	    <tr>
    	    	<td align="left" style="border:1px solid #000;border-left:none;border-bottom:none;" colspan="4">{{remark2}}</td>
    	    </tr>
    	    <tr>
	            <td align="center" style="border:1px solid #000;border-left:none;border-bottom:none;" width="50" colspan="2" rowspan="6">3 </td>
	            <td align="left" style="border:1px solid #000;border-left:none;border-bottom:none;" colspan="4">请一定按我司提供资料开票，如开错发票，由贵司负责所造成之一切问题。</td>
	        </tr>
    	    <tr> 
    		    <td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" width="100">公司全称：</td> 
    			<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" colspan="3">{{companyName}}</td>
    	    </tr>
    	    <tr> 
    			<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" width="100">公司地址：</td>
    			<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" colspan="3">{{address}}</td>
    		</tr>
    		<tr> 
    			<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" width="100">开户行：</td> 
    			<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" colspan="3">{{bankName}}</td>
    		</tr>
    		<tr> 
    			<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" width="100">账号：</td> 
    			<td style='border:1px solid #000;border-left:none;border-bottom:none;mso-number-format:\"\@\";' align="center" colspan="3">{{bankAccount}}</td>
    		</tr>
    		<tr> 
    			<td style="border:1px solid #000;border-left:none;border-bottom:none;" align="center" width="100">税号：</td> 
    			<td style='border:1px solid #000;border-left:none;border-bottom:none;mso-number-format:\"\@\";' align="center" colspan="3">{{taxNo}}</td>
    		</tr>
    	    <tr>
    		    <td style="border:1px solid #000;border-left:none;" align="center" width="50" colspan="2">4</td>
    		    <td align="left" style="border:1px solid #000;border-left:none;" colspan="4">供应商对本结算单有疑义，请于发票送达前与我司商务部联系。</td>
    		</tr>
    		<tr>      
	    		<td align="left" style="border:1px solid #000;border-left:none;" colspan="5">供应商负责人签字、盖章：</td>  
				<td align="center" style="border:1px solid #000;border-left:none;" colspan="3">（另：请确认付款方式：A电汇 B支票 C汇票）</td>
			</tr>
    	</tbody>
    	</table>
    	</div>
    	    `;
    class CounterBalanceExporter {
        constructor(billInfo) {
            this.billInfo = billInfo;
            this.template = null;
            this.colspan = 1;
            this.init();
        }

        init() {
            if (this.billInfo.templateType == 0) {
            	this.template = acrossTemplate;
                this.colspan = 10;
            }else if(this.billInfo.templateType == 1){
            	this.template = verticalTemplate;
                this.colspan = 6;
            }else if(this.billInfo.templateType == 2){
            	this.template = supplierTemplate;
            	this.colspan = 8;
            }
            var html = `<body>${this.template}</body>`;
            this.renderer = template.compile(html);
            this.printer = new printCounterBalance(this.billInfo);
        }

        getExportHtml(order) {
            var data = $.extend({}, order);     	     
            let html = this.renderer(data);
            return html;
        }         

        export2Excel(order) {
        	var self = this;
        	self.printer.dealCounterSaleCostList(order);
        	self.printer.dealCounterCostList(order);
        	self.printer.dealOtherCostList(order);
        	self.html = this.getExportHtml(order);
        	self.$html = $(self.html);
        	self.bTitle = $("div.bTitle",self.$html)[0].innerHTML; 
        	self.title = $("div.title",self.$html)[0].innerHTML; 
        	let name="";
        	if(order.templateType == 2){
        		name=order.companyName;
        	}else{
        		name=order.counterName;
        	}
        	
            let options = {
            		bTitle:self.bTitle, 
            		title:self.title, 
            		filename:self.bTitle+self.title+"_"+name, 
            		type:'xls', 
            		colspan:this.colspan
            }

            var exporter = new tableExport.Export(options, function(){
	            	var officeHtml = '<table style="margin: 0; margin-top:5px ; padding: 0; font-size:22px; font-weight:600; text-align: center;"><thead><tr><td align="center" colspan="'+self.colspan+'">'+self.bTitle+'</td></tr></thead></table>';
	            	officeHtml += '<table style="text-align: center; font-size:16px; margin-bottom:10px; font-weight: 600;"><thead><tr><td align="center" colspan="'+self.colspan+'">'+self.title+'</td></tr></thead></table>';
	                var tableTitle = "";
	                var exportHtml = self.$html[1].children;
	                for(var ti=2,tlen=exportHtml.length;ti<tlen;ti++){
	                    var el = exportHtml[ti];
	                    if(el.nodeName == 'DIV'){
	                        tableTitle = el.innerHTML;
	                    }else if(el.nodeName == 'TABLE'){
	                    	officeHtml += '<table><thead><tr>'+tableTitle+'</tr></thead>' +el.innerHTML+'</table>';
	                        tableTitle = "";
	                    }
	                }
	                
	                return officeHtml;
	            });
            
            exporter.toOffice();
        }
            
    }

    module.exports = CounterBalanceExporter;
});